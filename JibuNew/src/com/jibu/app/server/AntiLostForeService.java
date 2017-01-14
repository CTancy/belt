package com.jibu.app.server;


import mybleservice.FindPhoneNotify;

import com.jibu.app.R;
import com.jibu.app.main.MainActivity;
import com.jibu.app.main.MyNotification;
import com.szants.sdk.AntsBeltSDK;
import com.szants.sdk.DeviceStateObserver;
import com.szants.sdk.FindPhoneObserver;
import com.szants.sdk.SittingRemindObserver;
import com.szants.sdk.StepObserver;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


public class AntiLostForeService extends Service {

	protected static final String TAG = "AntiLostForeService";
	private AntsBeltSDK sdk;
	private boolean hasConnected = false;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Notification notification = new Notification.Builder(this.getApplicationContext())
		.setContentText("已开启手机防丢模式")
		.setContentTitle("FreeBelt")
		.setSmallIcon(R.drawable.ic_launcher)
		.setWhen(System.currentTimeMillis())
		.build();
		startForeground(110, notification);
		registerBeltObesever();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		stopForeground(true);
		super.onDestroy();
	}
	
	private void registerBeltObesever() {
        sdk = AntsBeltSDK.getInstance();
        
        sdk.registerDeviceStateObserver(deviceStateObserver);
       
        
	}
	
	public DeviceStateObserver deviceStateObserver = new DeviceStateObserver() {
		
		@Override
		public void disConnected() {
//			ToastUtil.toast("断开了设备连接");
			Log.e(TAG, "断开了设备连接");
            if (hasConnected) {
            	FindPhoneNotify.getInstance().phoneIfNotify(getBaseContext());
            }
        	hasConnected = false;
		}
		
		@Override
		public void connecting() {
			
		}
		
		@Override
		public void connected() {
//			ToastUtil.toast("已成功连接设备");
			Log.e(TAG, "已成功连接设备");
			hasConnected = true;
		}
		
		@Override
		public void blueToothClose() {
               if (hasConnected) {
                	FindPhoneNotify.getInstance().phoneIfNotify(getBaseContext());
                }
            	hasConnected = false;
		}
	};
	
	

}
