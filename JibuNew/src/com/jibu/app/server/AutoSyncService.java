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
import com.veclink.bracelet.bean.BleUserInfoBean;
import com.veclink.bracelet.bean.DeviceSportAndSleepData;
import com.veclink.bracelet.bletask.BleCallBack;
import com.veclink.bracelet.bletask.BleSyncNewDeviceDataTask;
import com.veclink.bracelet.bletask.BleSyncParamsTask;
import com.veclink.bracelet.bletask.BleTask;
import com.veclink.hw.bledevice.BraceletNewDevice;
import com.veclink.hw.bleservice.util.Keeper;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class AutoSyncService extends IntentService {

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
    

	public AutoSyncService() {
		super("AutoSyncService");
	}
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
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		if (!isAutoSync) {
			autoSyncParams();
		}
		
	}
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
	Handler syncParamsBeforeStepHandler =  new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BleCallBack.TASK_START:
				Log.e(TAG, "syncParams start");
//				if (null != titleTextView) {
//					titleTextView.setText(R.string.refreshing);
//				}
				break;

			case BleCallBack.TASK_PROGRESS:
				Log.e(TAG, "syncParams TASK_PROGRESS");
				break;

			case BleCallBack.TASK_FINISH:
				Log.e(TAG, "syncParams TASK_FINISH");
				mHandler.sendEmptyMessage(CONNECT_SUCCEED);
//				if (null != titleTextView) {
//					titleTextView.setText(R.string.refresh_succeed);
//				}
				new Handler()
				{
					@Override
					public void handleMessage(Message msg)
					{
//						isAutoSync = false;
						autoSyncStep();
					}
				}.sendEmptyMessageDelayed(0, 300);
				break;

			case BleCallBack.TASK_FAILED:
				Log.e(TAG, "syncParams TASK_FAILED");
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
				break;
			}
		}
	};
	BleCallBack syncParamsBeforeStepCallback = new BleCallBack(syncParamsBeforeStepHandler);
	
	public BleSyncParamsTask getBleSyncParamsTask(BleCallBack syncParmasCallBack) {
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
		BleSyncParamsTask bleSyncParamsTask = new BleSyncParamsTask(this, syncParmasCallBack, bean);
		return bleSyncParamsTask;
	}
	
	private void syncParamsBeforeStep() {
		BleTask task = null;
		task = getBleSyncParamsTask(syncParamsBeforeStepCallback);
		if(null !=  task) {
			task.work();
		}
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
	Handler syncStepDataHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BleCallBack.TASK_START:
				Log.e(TAG, "syncStepData start" );
//				titleTextView.setText("正在同步中...0%");
				mHandler.sendEmptyMessage(SYNC_STEP);
				break;

			case BleCallBack.TASK_PROGRESS:
				if(msg.obj!=null) {
					int progress = (Integer) msg.obj;
//					titleTextView.setText("正在同步中..." + progress + "%");
					Message  MSG = Message.obtain();
					Bundle buddle = new Bundle();
					buddle.putInt(PROGRESS, progress);
					MSG.what = SYNC_STEP;
					MSG.setData(buddle);
					mHandler.sendMessage(MSG);
				}
				break;

			case BleCallBack.TASK_FINISH:
				int step = -1;
				if(msg.obj!=null){
					DeviceSportAndSleepData deviceSportAndSleepData= (DeviceSportAndSleepData) msg.obj;	
					
					Gson gon = new Gson();
					String result = gon.toJson( msg.obj);
					Log.i("log","result="+result);
					MoveData.hdlrSyncStepData(user,moveDataService,result);
					
				}
				if(isAutoSync) {
					isAutoSync = false;
//					showMoveData.removeAllElements();
					long currTime = System.currentTimeMillis();
					
//					MoveData todayMoveData = moveDataService.queryMoveDataByUserSpecDay(user.userId,currTime);
//					
//					if(todayMoveData!=null){
//						showMoveData.add(todayMoveData);
//					}else{
//						todayMoveData = new MoveData(user.userId, currTime);
//						moveDataService.insertMoveData(todayMoveData);
//						showMoveData.add(todayMoveData);
//					}
//					currShowMoveData = todayMoveData;
//					setTitle(currShowMoveData);
					user.updateTime = System.currentTimeMillis();
					userService.updateUser(user);
//					initMainFlipper();
//					setUpdateTime();
				}
				
				Message  MSG = Message.obtain();
				Bundle buddle = new Bundle();
				buddle.putInt(STEP, step);
				MSG.what = SYNC_COMPLETE;
				MSG.setData(buddle);
				
				mHandler.sendMessage(MSG);
				break;

			case BleCallBack.TASK_FAILED:
				ToastUtil.toast(R.string.sync_fail_try_again);				
				isAutoSync = false;
				mHandler.sendEmptyMessage(SYNC_FAILD);
//				int index = mainFlipper.getDisplayedChild() >=0 ? mainFlipper.getDisplayedChild() : 0;
//				currShowMoveData = showMoveData.elementAt(index);
//				setTitle(currShowMoveData);
//				setUpdateTime();
				break;
			}
		}
		
	};
	BleCallBack syncStepDataCallBack = new BleCallBack(syncStepDataHandler);
	
	private void syncSportDataByUpdateTime(long updateTime){
		
			BleTask task = null;
			task = new BleSyncNewDeviceDataTask(this, syncStepDataCallBack,BraceletNewDevice.SPORT_DATA,new Date(updateTime),new Date(System.currentTimeMillis()));
			if(task!=null){
				task.work();
			}
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

	
	
}
