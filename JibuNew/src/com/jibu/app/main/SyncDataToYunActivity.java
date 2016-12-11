package com.jibu.app.main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.jibu.app.R;
import com.jibu.app.database.MoveDataService;
import com.jibu.app.database.UserService;
import com.jibu.app.entity.MoveData;
import com.jibu.app.entity.User;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

/**
 * 
 * @author CTC
 *
 */
public class SyncDataToYunActivity extends WaitingActivity implements OnClickListener{

	private final String TAG = "SyncDataToYunActivity";
	MainApplication mainApplication;
	MoveDataService moveDataService;
	UserService  userService;
	User user;
	
	private final String key_date = "date";
	private final String key_data = "data";
	
	private final int NO_EXIT_DATA = 31;
	
	private final String UPDATE_TIME  = "30000101";
	private final String EARLY_TIME = "20000101";
	
	private Vector<MoveData> moveDatas ;
	private int SyncMaxSize = 0;
	private int SyncCompleteCount = 0;
	private ArrayList<Integer> arrayList= new ArrayList<Integer>();
	
	private MoveData moveDataLast, moveDataEarly;
	
	private final int SAVE_EARLY_TIME = 0x20;
	private final int SAVE_UPDATE_TIME = 0x21;
	private final int SAVE_MOVE_DATA   = 0x22;
	
	private final int FETCH_EARLY_TIME = 0x30;
	private final int FETCH_UPDATE_TIME = 0x31;
	private final int FETCH_MOVE_DATA   = 0x32;	
	private final int FETCH_UPDATE_TIME_DOWNLOAD = 0x33;
	private final int FETCH_UPDATE_TIME_FOR_SHOW = 0x34;
	
	private final int GET_SCODE_UPLOAD = 0x40;
	private final int GET_SCODE_DOWNLOAD = 0x41;
	private final int GET_SCODE = 0x42;
	
	RequestQueue queue;
	ExecutorService executorService ;
	
	TextView textview_updateTime ;
	
	boolean isSync  = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_syncdatatoyun);
		
		mainApplication = (MainApplication) this.getApplication();
	
		mainApplication.addActivity(this);
		
		user  = mainApplication.user;
		
		if (user == null) {
			WelcomeActivity.gotoActivity(this);
			this.finish();
		} else {
			moveDataService = new MoveDataService(SyncDataToYunActivity.this);
			userService     = new UserService(SyncDataToYunActivity.this);
			queue = Volley.newRequestQueue(SyncDataToYunActivity.this.getApplicationContext());
			executorService = Executors.newSingleThreadExecutor();
			initView();
//			getScodeTask(GET_SCODE, new String[]{user.userId, user.password});
		}
	}
	
	
	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	private void getScodeTask(final int type, String... params) {
		String loginUrl = "http://api.ebelt.cn:8001/ebelt/login/login1?phone="+params[0]+"&password="+params[1];
		JsonObjectRequest getScodeReq = new JsonObjectRequest(Method.GET, 
			loginUrl, null, new Response.Listener<JSONObject>() {


				@Override
				public void onResponse(JSONObject jsonObject) {
					try {
						parseGetScodeResult(jsonObject, type);
					} catch (JSONException e) {
						e.printStackTrace();
						isSync = false;
					}
				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError arg0) {
					Log.e(TAG, "onError Response");
					isSync = false;
					if (type == GET_SCODE) {
//						ToastUtil.toast(R.string.get_scode_error);
					} else {
						ToastUtil.toast(R.string.net_error);
					}
					
				}
			});
		queue.add(getScodeReq);
	}
	/**
	 * 
	 * @param 
	 *
	 */
	 public class getLocalDataTask extends AsyncTask<Long, Void, Vector<MoveData>> {

		@Override
		protected Vector<MoveData> doInBackground(Long... params) {
			Vector<MoveData> ret = new Vector<MoveData>(); 
			try {
				ret = moveDataService.queryMoveDataByBeginEndDay(user.userId, params[0], params[1]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return ret;
		}

		@Override
		protected void onPostExecute(Vector<MoveData> result) {
			moveDatas = result;
			syncMoveDataToYun(result);
		}
		 
	 }
	/**
	 * 
	 * @param isUpLoad
	 * @param params
	 */
	private void FetchDataTask(final int type,final String... params) {
		String fetchDataUrl = "http://api.ebelt.cn:8001/ebelt/data/fetch?scode=" 
	+ params[0] + "&ymd="+params[1] + "&type=step";
		
		JsonObjectRequest fetchDataRequest = new JsonObjectRequest(Method.GET, 
				fetchDataUrl, null, new Response.Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject jsonObject) {
					try {
						
						parseFetchDataResult(jsonObject, type);
					
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError arg0) {
					FetchDataTask(type, params);
				}
			});
		queue.add(fetchDataRequest);
	}
	
	private Handler mHandler = new Handler();
	private void SaveDataTask(final String scode, final String ymd, final String data,final int type) {
		String saveDataUrl = "http://api.ebelt.cn:8001/ebelt/data/save";
		StringRequest saveDataRequest = new StringRequest(Method.POST, saveDataUrl, 
				 new Response.Listener<String>() {

					@Override
					public void onResponse(String result) {

						try {
							JSONObject jsonObject;
							jsonObject = new JSONObject(result);
							parseSaveDataResult(jsonObject, type);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				}, new Response.ErrorListener(){

					@Override
					public void onErrorResponse(VolleyError arg0) {
						Log.e(TAG, "SaveData Error No net");
						mHandler.postDelayed(new Runnable() {
							
							@Override
							public void run() {
								SaveDataTask(scode, ymd, data, type);
							}
						}, 2000);
					}
					
				})
        	{
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("scode", scode);
                map.put("ymd", ymd);
                map.put("type", "step");
                map.put("data", data);
                return map;
            }
        };
        queue.add(saveDataRequest);
	}
	
	private class InsertMoveDataTask extends AsyncTask<MoveData, Void, Void> {

		@Override
		protected Void doInBackground(MoveData... params) {
			moveDataService.insertMoveData(params[0]);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			SyncCompleteCount++;
			if (SyncCompleteCount >= SyncMaxSize) {
				changeWaitMsg(getString(R.string.download_yet, 100));
				new setFirstTimeTask().execute();
			} else {
				changeWaitMsg(getString(R.string.download_yet, SyncCompleteCount * 100/SyncMaxSize));
			}
		}
		
		
	}
	
	private void parseGetScodeResult(JSONObject jsonObject, int type) throws JSONException{
		int ret = jsonObject.getInt("ret");
		if (ret == 0) {
			mainApplication.scode = jsonObject.getString("scode");
			switch (type) {
			case GET_SCODE_UPLOAD:
				beginSyncToYun();
				break;
			case GET_SCODE_DOWNLOAD:
				beginDownLoadToLocal();
				break;
			case GET_SCODE:
				FetchDataTask(FETCH_UPDATE_TIME_FOR_SHOW, new String[]{mainApplication.scode, UPDATE_TIME});
				break;
			}
			
			
		} else {
			isSync = false;
		}
	}
	
	private void parseSaveDataResult(JSONObject jsonObject, int type) throws JSONException{
		int ret = jsonObject.getInt("ret");
		Log.e(TAG, "saveData = " + jsonObject.toString());
		if (ret == 0) {
			switch (type) {
			case SAVE_EARLY_TIME:
				Log.e(TAG, "UPLOAD COMPLETE 5%");
//				changeWaitMsg(getString(R.string.upload_yet, 5));
				{
					MoveData moveData  = moveDataService.qureyEarliestMoveDataByUser(user.userId);
					MoveData moveData2 = moveDataService.qureyLastestMoveDataByUser(user.userId);
					setLastMoveData(moveData2);
		
					if (null != moveData && null != moveData2) {
						new getLocalDataTask().execute(moveData.toLongTime(), moveData2.toLongTime());
					}
				}
				break;
			case SAVE_UPDATE_TIME:
				Log.e(TAG, "UPLOAD COMPLETE 100%");
				if (moveDataLast != null) {
					ApplicationSharedPreferences.setLastUpdateTime(SyncDataToYunActivity.this,
							moveDataLast.toFormatdate(getString(R.string.sync_date_format)));
				}
				mHandler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						waitClose();
						updateView();
						isSync = false;
					}
				}, 500);
				
				break;
			case SAVE_MOVE_DATA:
				SyncCompleteCount++;
				if (SyncCompleteCount >= SyncMaxSize) {
					changeWaitMsg(getString(R.string.upload_yet, 100));
					SaveDataTask(mainApplication.scode, UPDATE_TIME, dataToGson(moveDataLast), SAVE_UPDATE_TIME);
				} else {
					changeWaitMsg(getString(R.string.upload_yet, SyncCompleteCount * 100/SyncMaxSize));
				}
				break;
			}
		} 
	}
	
	private void parseFetchDataResult(JSONObject jsonObject, int type) throws JSONException {
		int ret = jsonObject.getInt("ret");
		Log.e(TAG, "parseFetchDataResult ret = " + ret);
		if (ret == 0) {
			String jsonString = jsonObject.getString("data");
			Gson gson = new Gson();
			MoveData moveData = gson.fromJson(jsonString, MoveData.class);
			
			switch (type) {
			case FETCH_EARLY_TIME:
				setEarlyMoveData(moveData);
				FetchDataTask(FETCH_UPDATE_TIME_DOWNLOAD, new String[]{mainApplication.scode, UPDATE_TIME});
				break;
			case FETCH_UPDATE_TIME:
				{
					MoveData moveData2 = moveDataService.qureyLastestMoveDataByUser(user.userId);
					setLastMoveData(moveData2);
		
					if (null != moveData && null != moveData2) {
						new getLocalDataTask().execute(moveData.toLongTime(), moveData2.toLongTime());
					}
				}
				break;
			case FETCH_MOVE_DATA:
				executeInsertMoveData(moveData);
				break;

			case FETCH_UPDATE_TIME_DOWNLOAD:
				{
					syncMoveDataToLocal(moveDataEarly, moveData);
				}
				break;
				
			case FETCH_UPDATE_TIME_FOR_SHOW:
				{
					updateView(moveData);
					isSync = false;
					ApplicationSharedPreferences.setLastUpdateTime(SyncDataToYunActivity.this,
							moveData.toFormatdate(getString(R.string.sync_date_format)));
				}
				break;

			}
		} else if (ret == NO_EXIT_DATA){
			
			switch (type) {
				case FETCH_EARLY_TIME:
					ToastUtil.toast(R.string.un_sync_yet);
					break;
				case FETCH_UPDATE_TIME: //从未成功同步过数据
					MoveData moveData = moveDataService.qureyEarliestMoveDataByUser(user.userId);
					SaveDataTask(mainApplication.scode, EARLY_TIME, dataToGson(moveData), SAVE_EARLY_TIME);
					break;
				case FETCH_MOVE_DATA:
					break;
			}
			
		}
	}
	
	private void syncMoveDataToYun(Vector<MoveData> moveDatas) {
		if (moveDatas == null) {
			return;
		}
		
		SyncCompleteCount = 0;
		SyncMaxSize  = moveDatas.size();
		
//		ExecutorService executorService = Executors.newFixedThreadPool(30);
		MoveData moveData;
		for (int i = 0; i < moveDatas.size(); i++) {

			moveData = moveDatas.elementAt(i);
			
			if (moveData != null) {
			
				SaveDataTask(mainApplication.scode, moveData.to8date(), dataToGson(moveData), SAVE_MOVE_DATA);
		
			}
		}
	}
	
	/***
	 * 
	 * @param moveDataEarly
	 * @param moveDataLast
	 * 将两个日期内的数据获取下来
	 */
	private void syncMoveDataToLocal(MoveData moveDataEarly, MoveData moveDataLast) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		
		if (null != moveDataLast && null != moveDataEarly) {
			Calendar cl_begin = Calendar.getInstance();
			cl_begin.setTimeInMillis(moveDataEarly.toLongTime());
			Calendar cl_end = Calendar.getInstance();
			cl_end.setTimeInMillis(moveDataLast.toLongTime());
			
			SyncCompleteCount = 0;
			SyncMaxSize  = moveDataService.daysBetween(moveDataEarly.year, moveDataEarly.mounth, moveDataEarly.day,
					moveDataLast.year, moveDataLast.mounth, moveDataLast.day);
			
			for( ; MoveDataService.CompareCalendarByDate(cl_begin, cl_end) <= 0; ) {
				int lastYear  = cl_begin.get(Calendar.YEAR);
				int lastMonth = cl_begin.get(Calendar.MONTH);
				int lastDay   = cl_begin.get(Calendar.DATE);
				
				
				FetchDataTask(FETCH_MOVE_DATA, new String[]{mainApplication.scode, sdf.format(cl_begin.getTime())});
				
				cl_begin.set(lastYear, lastMonth, lastDay + 1);
			}
		}
		
	}
	
	private void executeInsertMoveData(MoveData moveData) {
		InsertMoveDataTask insertMoveDataTask = new InsertMoveDataTask();
		insertMoveDataTask.executeOnExecutor(executorService, moveData);
	}
	
	private void initView() {
		findViewById(R.id.id_relativelayout_sync_to_yun).setOnClickListener(this);
		findViewById(R.id.id_relativelayout_data_revert).setOnClickListener(this);
		findViewById(R.id.id_linearlayout_title_left).setOnClickListener(this);
		textview_updateTime = (TextView) findViewById(R.id.id_textview_sync_time);
		
		updateView();
		getUpdateTimeForShow();
	} 
	
	private void updateView(MoveData moveData) {
		if (moveData == null) {
			return ;
		}
		String updateTime = moveData.toFormatdate(getString(R.string.sync_date_format));
		if (null != updateTime && !updateTime.equals("")) {
			textview_updateTime.setText(getString(R.string.sync_update_time) + updateTime);
		} else {
			textview_updateTime.setText(R.string.un_sync_yet);
		}
	}
	
	private void updateView() {

		String updateTime = ApplicationSharedPreferences.getLastUpdateTime(SyncDataToYunActivity.this);
		if (null != updateTime && !updateTime.equals("")) {
			textview_updateTime.setText(getString(R.string.sync_update_time) + updateTime);
		} else {
			textview_updateTime.setText(R.string.un_sync_yet);
		}
	}
	
	public static void gotoActivity(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, SyncDataToYunActivity.class);
		context.startActivity(intent);
	}
	
	public String dataToGson(MoveData moveData) {
		Gson gson = new Gson();
		String jsonString  = gson.toJson(moveData);
		
		Log.e(TAG, "jsonString = " + jsonString);
		
		return jsonString;
	}
	private void setEarlyMoveData(MoveData moveData) {
		this.moveDataEarly = moveData;
	}
	private void setLastMoveData(MoveData moveData) {
		this.moveDataLast = moveData;
	}

	
	/**
	 * 
	 */
	private void beginSyncToYun() {
		showWait(getString(R.string.upload_yet, 0));
		FetchDataTask(FETCH_UPDATE_TIME, new String[]{mainApplication.scode, UPDATE_TIME});
	}
	
	/**
	 * 将mainActivity finish  退回去后重新加载数据
	 */
	private void beginDownLoadToLocal() {
		if (MainActivity.activity != null) {
			MainActivity.activity.finish();
		}
		showWait(getString(R.string.download_yet, 0));
		FetchDataTask(FETCH_EARLY_TIME, new String[]{mainApplication.scode, EARLY_TIME});
	}
	/**
	 * 
	 * @author Administrator
	 * 设置最早拥有的数据时间点 为了统计时用到
	 */
	private class setFirstTimeTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			MoveData moveData =  moveDataService.qureyEarliestMoveDataByUser(user.userId);
			user.firstTime = moveData.toLongTime();
			userService.insertItem(user);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			waitClose();
			isSync = false;
		}
		
	}
	/*
	 * 显示上次云同步的时间
	 * 
	 */
	private void getUpdateTimeForShow() {
		if (!isSync) {
			isSync = true;
			getScodeTask(GET_SCODE, new String[]{user.userId, user.password});
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_relativelayout_sync_to_yun:
//			MoveData moveData = moveDataService.qureyEarliestMoveDataByUser(user.userId);
//			SaveDataTask(mainApplication.scode, EARLY_TIME, dataToGson(moveData), SAVE_EARLY_TIME);
			if (!isSync) {
				isSync = true;
				getScodeTask(GET_SCODE_UPLOAD, new String[]{user.userId, user.password});
//				Log.e(TAG, "onClick sync to yun");
			}
			
			break;
		case R.id.id_relativelayout_data_revert:
			if (!isSync) {
				isSync = true;
				getScodeTask(GET_SCODE_DOWNLOAD, new String[]{user.userId, user.password});
			}
			break;
		case R.id.id_linearlayout_title_left:
			this.finish();
			MainActivity.gotoActivity(SyncDataToYunActivity.this);
			break;
		}
		
	}
}
