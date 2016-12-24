package com.jibu.app.server;

import java.util.Calendar;
import java.util.Date;
import com.google.gson.Gson;
import com.jibu.app.R;
import com.jibu.app.database.MoveDataService;
import com.jibu.app.database.UserService;
import com.jibu.app.entity.MoveData;
import com.jibu.app.entity.User;
import com.jibu.app.main.MainApplication;
import com.jibu.app.main.ToastUtil;
import com.szants.bracelet.bean.BeltDeviceInfo;
import com.szants.bracelet.bean.BleDeviceData;
import com.szants.bracelet.bean.BleUserInfoBean;
import com.szants.bracelet.bean.DeviceSportAndSleepData;
import com.szants.bracelet.bletask.BleCallBack;
import com.szants.bracelet.bletask.BleProgressCallback;
import com.szants.hw.bleservice.util.Keeper;
import com.szants.sdk.AntsBeltSDK;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class AutoSyncService extends Service {



	private static final String TAG = "AutoSyncService";
	
	private int syncParamsTimes = 0;
	
	public  final static int CHECKING_CONNECT = 0;
	public  final static int CONNECTING = 1;
	public  final static int SYNC_STEP  = 2;
	public  final static int SYNC_FAILD = 3;
	public  final static int CONNECT_SUCCEED = 4;
	public  final static int SYNC_COMPLETE = 5;
	public  final static int CONNECTED_FAILD = 6;
	public  final static String ACTION_STATE = "state_action";
	public  final static String PROGRESS = "progress";
	public  final static String STEP = "step";
	public  static int State = 0;
	public  static boolean isAutoSync = false;
	
	MainApplication mainApplication;
	MoveDataService  moveDataService;
	UserService userService;
	User user;
	
    Intent broadcastIntent = new Intent(); 

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		mainApplication = (MainApplication) this.getApplication();
		user = mainApplication.user;
		moveDataService = new MoveDataService(this);
		userService = new UserService(this);
		

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (!isAutoSync) {
			autoSyncParams();
		}
		return super.onStartCommand(intent, flags, startId);
	}

//		@Override
//	protected void onHandleIntent(Intent intent) {
//		// TODO Auto-generated method stub
//		if (!isAutoSync) {
//			autoSyncParams();
//		}
//		
//	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.e(TAG, "AutoSyncService destory");
	}
	//用于发送广播给MainActivity 改变UI
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case CHECKING_CONNECT: 
				case CONNECTING:
				case SYNC_FAILD:
				case CONNECT_SUCCEED:
				case SYNC_COMPLETE:
				case CONNECTED_FAILD:
					broadcastIntent.setAction(ACTION_STATE);
					broadcastIntent.putExtra(ACTION_STATE, msg.what);
					getBaseContext().sendBroadcast(broadcastIntent);
					State = msg.what;
				break;
				case SYNC_STEP:
					broadcastIntent.setAction(ACTION_STATE);
					broadcastIntent.putExtra(ACTION_STATE, msg.what);
					broadcastIntent.putExtra(PROGRESS, msg.getData().getInt(PROGRESS));
					getBaseContext().sendBroadcast(broadcastIntent);
					State = msg.what;
					break;
			}
		}
	};
	
	public void syncUserInfoToDevice() {

		
	}
	
	private void syncParamsBeforeStep() {
//		BleTask task = null;
//		task = getBleSyncParamsTask(syncParamsBeforeStepCallback);
//		if(null !=  task) {
//			task.work();
//		}
		int targetStep = 100;
		int wearLocation = 0;
		int sport_mode = 1;
		int sex = 0;
		int year= 1990;
		int nowYear = Calendar.getInstance().get(Calendar.YEAR);
		int age = nowYear-year;
		float height = 169;
		float weight = 58;
		int distanceUnit = 0;
		boolean keptOnOffblean = false;
		int keptOnOff = keptOnOffblean==true?1:0;
		BleUserInfoBean bean = new BleUserInfoBean(targetStep, wearLocation, sport_mode, sex, age, weight, height, distanceUnit, keptOnOff);
		AntsBeltSDK.getInstance().syncParams(bean, new BleCallBack() {
			
			@Override
			public void onStart(Object startObject) {
			    Log.e(TAG, "开始自动同步参数");	
			}
			
			@Override
			public void onFinish(Object result) {
				BeltDeviceInfo deviceInfo = (BeltDeviceInfo)result;
				mHandler.sendEmptyMessage(CONNECT_SUCCEED);
			    Log.e(TAG, "同步参数成功");	
			    autoSyncStep();
			}
			
			@Override
			public void onFailed(Object error) {
				Log.e(TAG, "同步参数失败");
				if (syncParamsTimes < 3) {
					syncParamsTimes++;
//					titleTextView.setText(R.string.refreshing);
					mHandler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							autoSyncParams();
						}
					}, 2000);
				} else {
					isAutoSync = false;
					syncParamsTimes = 0;
//					titleTextView.setText(R.string.refresh_fail);
					mHandler.sendEmptyMessage(CONNECTED_FAILD);
				}
			}
		});
	}
	private  void autoSyncParams() {
		if(Keeper.getUserHasBindBand(getApplicationContext())){
			//未同步参数 或者 同步参数失败了一次
			if (!isAutoSync || syncParamsTimes > 0) {
				isAutoSync = true;
				try {
					if (syncParamsTimes == 0) { 
						mHandler.sendEmptyMessage(CHECKING_CONNECT);
					} else {
						mHandler.sendEmptyMessage(CONNECTING);
					}
					mHandler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							syncParamsBeforeStep();
						}
					}, 2000);
					
				} catch(Exception e) {
					isAutoSync = false;
				}
			} 
		}
	}

	
	private void syncSportDataByUpdateTime(long updateTime){
		
		AntsBeltSDK.getInstance().syncStepData(updateTime, System.currentTimeMillis(), new BleProgressCallback() {
			
			@Override
			public void onStart(Object startObject) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFinish(Object res) {

				int step = -1;
				if(res != null){
//					DeviceSportAndSleepData deviceSportAndSleepData= (DeviceSportAndSleepData) res;	
					Gson gon = new Gson();
					String result = gon.toJson(res);
					Log.i("log","result="+result);
					MoveData.hdlrSyncStepData(user,moveDataService,result);
					
				}
				if(isAutoSync) {
					isAutoSync = false;
					long currTime = System.currentTimeMillis();
					
					user.updateTime = System.currentTimeMillis();
					userService.updateUser(user);
				}
				
				Message  MSG = Message.obtain();
				Bundle buddle = new Bundle();
				buddle.putInt(STEP, step);
				MSG.what = SYNC_COMPLETE;
				MSG.setData(buddle);
				
				mHandler.sendMessage(MSG);
				
			}
			
			@Override
			public void onFailed(Object error) {
				ToastUtil.toast(R.string.sync_fail_try_again);				
				isAutoSync = false;
				mHandler.sendEmptyMessage(SYNC_FAILD);
			}
			
			@Override
			public void onProgress(Object pro) {

				int progress = (Integer) pro;
				Message  MSG = Message.obtain();
				Bundle buddle = new Bundle();
				buddle.putInt(PROGRESS, progress);
				MSG.what = SYNC_STEP;
				MSG.setData(buddle);
				mHandler.sendMessage(MSG);
							
			}
		});
	}
	private  void autoSyncStep() {
		Log.e(TAG, "autoSyncStep start");
		if(Keeper.getUserHasBindBand(getApplicationContext())){
//			if(!isAutoSync){
				isAutoSync = true;
				try {
					syncSportDataByUpdateTime(user.updateTime);
				} catch (Exception e) {
					isAutoSync = false;
					e.printStackTrace();
				}
//			}
		}
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
