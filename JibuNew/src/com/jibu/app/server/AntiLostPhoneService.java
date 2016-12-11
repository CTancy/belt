package com.jibu.app.server;
import java.util.Calendar;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import com.jibu.app.main.ApplicationSharedPreferences;
import com.jibu.app.main.NoAlarmAreaActivity;
import com.veclink.bracelet.bean.BleUserInfoBean;
import com.veclink.bracelet.bletask.BleCallBack;
import com.veclink.bracelet.bletask.BleSyncParamsTask;
import com.veclink.bracelet.bletask.BleTask;
import com.veclink.hw.bleservice.VLBleService;
import com.veclink.hw.bleservice.VLBleServiceManager;
import com.veclink.hw.bleservice.util.Keeper;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class AntiLostPhoneService extends Service {
	
	private final String TAG = "AntiLostPhoneService";
	public static final String ACTION = "AntiLostRemind";
	public static final String RSSI_VALUE = "rssi_value";
	private final int RSSI_TEST = 0;
	private final int OPEN_ANTILOST_REMIND = 1;
	
	int count = 0;
	double distance_sum = 0;
	int xinhao_sum   = 0;
	final int count_max = 6;
	private String RSSI = "";
	
	//设置提醒的信号强度值以及1M处的信号强度值，实际应由计算得出
	private int REMIND_RSSI = -90;
	private int M1_RSSI = -82;
	
	private static AntiLostPhoneService instance;
	
	private final int timegap = 150;
	
	private boolean isConnected = false;
	
	private int away_times = 0;
	private boolean is_away_once = false;
	Intent broadcastIntent = new Intent(); 
	
	int[] rssi_array  = new int[count_max];
	Timer mTimer = null;
	TimerTask mTimerTask = null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		initReciver();
		init();
		if (mTimer == null && mTimerTask == null) {
			startTimer();
		}
		Log.e(TAG, "onStartCommand");
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		Log.e(TAG, "onDestory~");
		stopTimer();
		unregisterReceiver(getdeviceMessageReceiver);
		super.onDestroy();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}

	BroadcastReceiver getdeviceMessageReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context arg0, Intent intent) {
			String action = intent.getAction();
			if (action.equals(VLBleService.ACTION_GATT_SERVICES_DISCOVERED)){
//				connectHandler.sendEmptyMessage(DEVICE_SERVERDISCOVER);
				Log.e(TAG, "已可以同步数据");
			} else if(action.equals(VLBleService.ACTION_GATT_DISCONNECTED)){
//				showMsgView.setText("设备信号强度：" + RSSI);
//				Log.e(TAG, "设备已断开");
//				Log.e(TAG, "设备信号强度：" + RSSI);
				if (isConnected && Keeper.getUserHasBindBand(getBaseContext())) {
					phoneNotify();
//					isConnected = false;
					clearAllRssi();
				}
				syncParams();
				isConnected = false;
			} else if(action.equals(VLBleService.ACTION_GATT_CONNECTED)){
				Log.e(TAG, "设备已连接");
			} else if(action.equals(AntiLostNotification.NOTIFICATION_DELETED_ACTION)){
				AntiLostNotification.getInstance(getBaseContext()).setFlag(true);
				Log.e(TAG, "通知栏被取消");
			} else if(action.equals(VLBleService.ACTION_GATT_RSSI)){
				if (!isConnected) {
					isConnected = true;
				}
//				Log.e(getPackageName() , "设备收到了信号强度的广播");
				int rssi = intent.getIntExtra(VLBleService.EXTRA_DATA, 0);
				
				if (count >= count_max){
					int rssi_now = calculate(rssi_array);
					Log.e(TAG, "average rssi_array " + rssi_now);
					count = 0;
					mySendBroadCast(ACTION, rssi_now);
					//连续两次信号值过弱
					if (Math.abs(rssi_now) > -REMIND_RSSI) {					
						if (is_away_once) {
							phoneNotify();
							is_away_once = false;
						} else {
							is_away_once = true;
						}
					} else {
						is_away_once = false;
					}
				} else {
					rssi_array[count] = rssi;
					count++;
				}
				
				
				
			} else {
//				showMsgView.setText("收到了未知广播" + action);
			}
			
		}
		
	};
	/**
	 * 初始化
	 */
	private void init() {
		int rssi = ApplicationSharedPreferences.getRSSIValue(this);
		setRemindRSSI(-rssi);
	}
	private void initReciver(){
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(VLBleService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(VLBleService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(VLBleService.ACTION_GATT_DISCONNECTED);	
		intentFilter.addAction(VLBleService.ACTION_GATT_RSSI);
		intentFilter.addAction(AntiLostNotification.NOTIFICATION_DELETED_ACTION);
		registerReceiver(getdeviceMessageReceiver, intentFilter);
	}
	
    

    TimerTask task = new TimerTask( ) {

        public void run () {
        	VLBleServiceManager.getInstance().readRssi();
        }

    };
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return myBinder;
	}
	/**
	 * 计算每秒的RSSI
	 * 
	 * */
	private int calculate(int[] rssi) {
//		double distance = Math.pow(10, ( Math.abs(rssi) - 60) / (10 * 2.0));
		int length = rssi.length;
		int sum = 0;
		bubbleSort(rssi);
//		for (int i = 0; i < length; i++){
//			Log.e(TAG, rssi[i]+" ");
//		}
		for(int i = 1; i < length - 1; i++) {
			sum += rssi[i];
		}
		return sum/(length - 2);
	}
	public static void bubbleSort(int[] numbers) {   
	    int temp; // 记录临时中间值   
	    int size = numbers.length; // 数组大小   
	    for (int i = 0; i < size - 1; i++) {   
	        for (int j = i + 1; j < size; j++) {   
	            if (numbers[i] > numbers[j]) { // 交换两数的位置   
	                temp = numbers[i];   
	                numbers[i] = numbers[j];   
	                numbers[j] = temp;   
	            }   
	        }   
	    }   
	}  
	private boolean isNeedRemind() {
		if(isConnected) 
			return false;
		return true;
	}
    private void startTimer(){  
        if (mTimer == null) {  
            mTimer = new Timer();  
        }  
  
        if (mTimerTask == null) {  
            mTimerTask = new TimerTask() {  
                @Override  
                public void run() {  
                    VLBleServiceManager.getInstance().readRssi();
                }  
            };  
        }  
  
        if(mTimer != null && mTimerTask != null )  
            mTimer.schedule(mTimerTask, 1000, timegap);  
  
    }  
  
    private void stopTimer(){  
          
        if (mTimer != null) {  
            mTimer.cancel();  
            mTimer = null;  
        }  
  
        if (mTimerTask != null) {  
            mTimerTask.cancel();  
            mTimerTask = null;  
        }     
  
        count = 0;  
  
    } 
    private void phoneNotify() {
    	final Set<String> selectedWifi = ApplicationSharedPreferences.getNoAlarmArea(getBaseContext());
    	final String wifiInfo = NoAlarmAreaActivity.getConnectWifiSsid(getBaseContext());
    	final boolean isOpen  =  ApplicationSharedPreferences.getIsOpenNoAlarmArea(getBaseContext());
    	if (isOpen && selectedWifi != null && selectedWifi.contains(wifiInfo)) {
    		Log.e(TAG, "is in no alarm area!");
    		return; 
    	}
    	
    	AntiLostNotification notification = AntiLostNotification.getInstance(getBaseContext());
		if (null != notification) {
			notification.sendRemindNotification();
		}
    }
    
    /**清除信号变量相关
     * */
    private void clearAllRssi() {
    	for(int i = 0; i < rssi_array.length; i++) {
    		rssi_array[i] = 0;
    	}
    	count = 0;
    	is_away_once = false;
    }
    
	private void mySendBroadCast(String action, int rssi) {
		if (action == null || action.equals("")) {
			return ;
		}
		broadcastIntent.setAction(action);
		broadcastIntent.putExtra(RSSI_VALUE, rssi);
		getBaseContext().sendBroadcast(broadcastIntent);
	}
	
	public synchronized void setRemindRSSI(int remindRssi) {
		REMIND_RSSI = remindRssi;
		Log.e(TAG, "Current Remind_rssi = " + remindRssi);
	}
	
	public int getRemindRSSI() {
		return REMIND_RSSI;
	}
	
	public static AntiLostPhoneService getInstance() {
		if(instance == null) {
			return new AntiLostPhoneService();
		}
		return instance;
	}
	
	public class AntiLostBinder extends Binder {
		
		public AntiLostPhoneService getService() {
			return AntiLostPhoneService.this;
		}
	}
	
	private AntiLostBinder myBinder = new AntiLostBinder();
	Handler syncParamsHandler =  new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BleCallBack.TASK_START:
				Log.e(TAG, "syncParams TASK_START");
				break;

			case BleCallBack.TASK_PROGRESS:
				Log.e(TAG, "syncParams TASK_PROGRESS");
				break;

			case BleCallBack.TASK_FINISH:
				break;

			case BleCallBack.TASK_FAILED:
				syncParamsHandler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						syncParams();
					}
				}, 1000);
				break;
			};
		}
	};
	BleCallBack syncParamsCallback = new BleCallBack(syncParamsHandler);
	
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
	
	private void syncParams() {
		BleTask task = null;
		task = getBleSyncParamsTask(syncParamsCallback);
		if(null !=  task) {
			task.work();
		}
	}
}
