package com.jibu.app.server;

import java.io.File;
import java.util.Vector;

import com.jibu.app.main.WelcomeActivity;
import com.veclink.bracelet.bean.BluetoothDeviceBean;
import com.veclink.bracelet.bletask.BleCallBack;
import com.veclink.bracelet.bletask.BleScanDeviceTask;
import com.veclink.hw.bleservice.VLBleServiceManager;
import com.veclink.hw.bleservice.util.Keeper;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class JibuServer extends Service {


	public int state;
	
	public static final int STATE_BINDED = 1;
	public static final int STATE_SCANING = 2;
	public static final int STATE_UNCONNECT = 3;
	public static final int STATE_BINDING = 4;
	
	public String deviceAddr = "";
	public String deviceName = "";
	
	
	private BleScanDeviceTask scanTask;
	
	private Vector<BluetoothDeviceBean> devices;
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@SuppressWarnings("deprecation")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			
			}
		}

	};

	@Override
	public IBinder onBind(Intent intent) {
		
		if(Keeper.getUserHasBindBand(this)){
			VLBleServiceManager.getInstance().bindService(getApplication());
			state = STATE_BINDED;
		}else{
			
			
		}
		
		
		
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		
		return super.onStartCommand(intent, flags, startId);
	}

	
	Handler scanBleDeviceHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BleCallBack.TASK_START:

				break;

			case BleCallBack.TASK_PROGRESS:
				BluetoothDeviceBean device = (BluetoothDeviceBean) msg.obj;
				
				break;	
			case BleCallBack.TASK_FINISH:
				
				
				break;

			case BleCallBack.TASK_FAILED:
				
				break;
			}
		}
	};
	
	
	private void startScan(){
		BleCallBack scanDeviceCallBack = new BleCallBack(scanBleDeviceHandler);
		BleScanDeviceTask scanTask = new BleScanDeviceTask(WelcomeActivity.activity, scanDeviceCallBack);
 		scanTask.execute(0);
	}
	
	class AppUpgradeThread extends Thread {

		@Override
		public void run() {
			
		}
	}

	
	


}
