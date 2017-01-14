package com.jibu.app.main;

import java.util.Calendar;
import java.util.Vector;

import mybleservice.BluetoothLeService;
import mybleservice.E3AKeeper;

import com.jibu.app.R;
import com.jibu.app.entity.User;
import com.szants.bracelet.bean.BluetoothDeviceBean;
import com.szants.bracelet.bletask.BleCallBack;
import com.szants.bracelet.bletask.BleScanDeviceTask;
import com.szants.hw.bleservice.util.Keeper;
import com.szants.sdk.AntsBeltSDK;
import com.szants.sdk.BindDeviceListener;
import com.szants.sdk.DeviceStateObserver;
import com.szants.sdk.ScanDeviceListener;
import com.szants.sdk.UnBindDeviceListener;
import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ScanActivity extends WaitingActivity implements OnClickListener, OnItemClickListener{
	public static String TAG = "ScanActivity";
	public static ScanActivity activity = null;


	MainApplication mainApplication;
	
	BleCallBack scanDeviceCallBack;
	
	BleScanDeviceTask scanTask = null;
	
	Vector<BluetoothDeviceBean> devices = null;
	
	private IntentFilter intentFilter = new IntentFilter();
	
	private final int DEVICE_CONNECTED = 0x21;
	private final int DEVICE_DISCONNECTED = 0x22;
	private final int DEVICE_SYNCPARAMSDONE = 0x23;
	private final int USER_HAS_CLICK_DEVICE = 0x25;//用户已经敲击设备，设备已回应
	
	private final int E3A_DEVICE_HAS_CONNECTED = 0x31;
	private final int E3A_DEVICE_HAS_DISCONNECTED = 0x32;
	
	private static final int REQUEST_ENABLE_BT = 3;
	
	private LinearLayout linearLayoutDevices;
	
	private BleDeviceListAdapter adapter;
	
	private ProgressBar processbar_searching;
	
	private TextView textview_searching;
	
	private BluetoothAdapter mBluetoothAdapter = null;
	
	private boolean isConnecting = false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_scan);
		((MainApplication) this.getApplication()).addActivity(this);

		
		activity = this;
		mainApplication = (MainApplication) this.getApplication();
		Log.i("sino","onCreate");

		findViewById(R.id.id_imageview_pidai).setOnClickListener(this);
		findViewById(R.id.id_linearlayout_title_right).setOnClickListener(this);
		findViewById(R.id.id_textview_connect_at_scan).setOnClickListener(this);
		
		initReciver();
		
//		UmengUpdateAgent.update(this);
		
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		sdk = AntsBeltSDK.getInstance();
	}

	@Override
	protected void onStart() {

		super.onStart();

		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
	    }

	}

	@Override
	protected void onPause() {

		super.onPause();
		
		
	}

	@Override
	protected void onStop() {

		super.onStop();
	
	}

	@Override
	protected void onResume() {

		super.onResume();
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	
		activity = null;
		unregisterReceiver(connectDeviceInfoReceiver);
		closeAllDialog();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			((MainApplication) this.getApplication()).exit();
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_linearlayout_title_right:
			MainActivity.gotoActivity(this);
			this.finish();
			break;
		case R.id.id_imageview_pidai:
		case R.id.id_textview_connect_at_scan:
			Log.e("sino","ldskfalsjfd");
			showWaitCanelable("开始扫描设备");
			startScan();
			//showBindFailDialog();
			//showClickDeviceDialog();
			showDeviceSelDialog();
			break;
		}
		
	}
	
//	Handler scanBleDeviceHandler = new Handler(){
//		@Override
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case BleCallBack.TASK_START:
//				Log.i("log","scanBleDeviceHandler:TASK_START");
//				adapter.clearAllDevieceItem();
//				break;
//
//			case BleCallBack.TASK_PROGRESS:
//				Log.i("log","scanBleDeviceHandler:TASK_PROGRESS");
//				BluetoothDeviceBean device = (BluetoothDeviceBean) msg.obj;
////				if(device.getDevice_name().equals("D013A") || device.getDevice_name().equals("E3-A")) {
//					devices.add(device);
////					Log.e(TAG, device.getDevice_address());
//					adapter.addDeviceItem(device);
////					linearLayoutDevicesAddView(device);
////				}
//				break;	
//			case BleCallBack.TASK_FINISH:
//				Log.i("log","scanBleDeviceHandler:TASK_FINISH");
//							
//				if(scanTask!=null){
//					scanTask.stopScanTask();
//					scanTask=null;
//				}
//				if(processbar_searching != null) {
//					processbar_searching.setVisibility(View.INVISIBLE);	
//				}
//				if(textview_searching != null) {
//					textview_searching.setText(R.string.select_devices);
//				}
//				if(devices.size()==0){
//					waitClose();
//					ToastUtil.toast(MainApplication.context, R.string.no_device);
//				}else{
//					
////					waitClose();
////					showDeviceSelDialog();
//					
//					/*
//					setWaitMessage("正在绑定设备");
//					Log.i("sino","开始绑定");
//					
//					
//					BluetoothDeviceBean firstDevice = devices.firstElement();
//					String addr = firstDevice.getDevice_address();
//					String name = firstDevice.getDevice_name();
//					Keeper.setBindDeviceMacAddress(activity, addr);
//					Keeper.setBindDeviceName(activity, name);
//														
//					VLBleServiceManager.setAutoReConnect(true);
//					VLBleServiceManager.getInstance().bindService(getApplication(),new BraceletGattAttributes());
//					
//					*/
//				}
//				
//				break;
//
//			case BleCallBack.TASK_FAILED:
//				ToastUtil.toast(MainApplication.context, R.string.scan_failed);
//				
//				waitClose();
//				
//				if(processbar_searching != null) {
//					processbar_searching.setVisibility(View.INVISIBLE);	
//				}
//				if(textview_searching != null) {
//					textview_searching.setText(R.string.searching_failed);
//				}
//				scanTask=null;
//				break;
//			}
//		}
//	};

	private void startScan(){
   	 if(sdk.isScanning()==false){
   		 if (devices == null) {
   			 devices = new Vector<BluetoothDeviceBean>();
   		 } else {
   			 devices.removeAllElements();
   		 }
//   		adapter.clearAllDevieceItem();
   		sdk.startScanDevice(scanDeviceListener);
      }
	}
	
	private AntsBeltSDK sdk;
	private ScanDeviceListener scanDeviceListener = new ScanDeviceListener() {
		
		@Override
		public void startScan() {
			showDeviceSelDialog();
			adapter.clearAllDevieceItem();
			
		}
		
		@Override
		public void scanFinish() {
			// TODO Auto-generated method stub
			stopScan();
			if(processbar_searching != null) {
				processbar_searching.setVisibility(View.INVISIBLE);	
			}
			if(textview_searching != null) {
				textview_searching.setText(R.string.select_devices);
			}
			if(devices.size()==0){
				waitClose();
				ToastUtil.toast(MainApplication.context, R.string.no_device);
			}
		}
		
		@Override
		public void scanFindOneDevice(BluetoothDeviceBean device) {
			adapter.addDeviceItem(device);
			
		}
	};
	
	private void stopScan(){
		
    	if(sdk.isScanning()){
        	sdk.stopScanDevice();
        }
	}
	
	private void initReciver(){
//		intentFilter.addAction(VLBleService.ACTION_GATT_CONNECTED);
//		intentFilter.addAction(VLBleService.ACTION_GATT_DISCONNECTED);
//		intentFilter.addAction(VLBleService.ACTION_GATT_SERVICES_DISCOVERED);
//		intentFilter.addAction(VLBleService.ACTION_USER_HAD_CLICK_DEVICE);
		
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);

		registerReceiver(connectDeviceInfoReceiver, intentFilter);
	}
	
	BroadcastReceiver connectDeviceInfoReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context arg0, Intent intent) {
			
			
			String action = intent.getAction();
			Log.i("sino","onReceive:action="+action);
//			if(action.equals(VLBleService.ACTION_GATT_SERVICES_DISCOVERED)){
//				try {
//					BleSyncParamsTask task = getBleSyncParamsTask();
//					task.work();
//				} catch (Exception e) {
//					MobclickAgent.reportError(ScanActivity.this, e);
//				}
//			}else if(action.equals(VLBleService.ACTION_USER_HAD_CLICK_DEVICE)){
//				connectHandler.sendEmptyMessage(USER_HAS_CLICK_DEVICE);				
//			}else if(action.equals(VLBleService.ACTION_GATT_DISCONNECTED)){
//				connectHandler.sendEmptyMessage(DEVICE_DISCONNECTED);
//			}else if(action.equals(VLBleService.ACTION_GATT_CONNECTED)){				
//				connectHandler.sendEmptyMessage(DEVICE_CONNECTED);
//			}else
				
				
			if(action.equals(BluetoothLeService.ACTION_GATT_CONNECTED)) {
				connectHandler.sendEmptyMessage(E3A_DEVICE_HAS_CONNECTED);
			}else if(action.equals(BluetoothLeService.ACTION_GATT_DISCONNECTED)) {
				connectHandler.sendEmptyMessage(E3A_DEVICE_HAS_DISCONNECTED);
			}
			
		}
		
	};
	
	Handler connectHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
//			case DEVICE_CONNECTED:
//			//	show_connect_info_tv.setText(getString(R.string.menu_connect));
//				setWaitMessage(getString(R.string.device_has_connected));
//				break;
//
//			case DEVICE_DISCONNECTED:
//				waitClose();
//				VLBleServiceManager.getInstance().unBindService(getApplication());
//				if (isConnecting) {
//					closeAllDialog();
//					//ToastUtil.toast("设备已断开连接");
//					showBindFailDialog();
//					isConnecting = false;
//				}
//				Log.e(TAG, "ScanActivity 收到了DEVICE_DISCONNECTED的广播");
//				break;	
//			case DEVICE_SYNCPARAMSDONE:
//				BaseDeviceProduct deviceProduct = DeviceProductFactory.createDeviceProduct(Keeper.getDeviceType(getApplicationContext()));
//				if(deviceProduct.bindDeviceWay==BaseDeviceProduct.CLICK_BIND_DEVICE_WAY){			
//					BleRequestBindDevice bleRequestBindDevice = new BleRequestBindDevice(getApplicationContext(), requestBindDeviceCallBack);
//					bleRequestBindDevice.work();					
//				}else{
//					
//					waitClose();
//					closeAllDialog();
//					ToastUtil.toast(R.string.device_bind_success);
//					Keeper.setUserHasBindBand(getApplicationContext(), true);
//					MainActivity.gotoActivity(ScanActivity.this);
//					ScanActivity.this.finish();
//				}
//				break;
//			
//			case USER_HAS_CLICK_DEVICE:
//				BleAppConfirmBindSuccess appConfirmBindSuccess = new BleAppConfirmBindSuccess(getApplicationContext(), new BleCallBack(new Handler()));
//				appConfirmBindSuccess.work();
//				Keeper.setUserHasBindBand(getApplicationContext(), true);				
//					
//				waitClose();
//				closeAllDialog();
//				ToastUtil.toast(R.string.device_bind_success);
//				
//				MainActivity.gotoActivity(ScanActivity.this);
//				ScanActivity.this.finish();
//				break;
			case E3A_DEVICE_HAS_CONNECTED:
				LostOnlyMainActivity.gotoActivity(ScanActivity.this);
				if (connectingDialog != null) {
					connectingDialog.dismiss();
				}
				break;
			case E3A_DEVICE_HAS_DISCONNECTED:
				E3AKeeper.getInstance().unBinderDevice(getApplication());
				showBindFailDialog();
				break;
			}
		}
	};
	
//	public BleSyncParamsTask getBleSyncParamsTask() {
//		
//		User user = mainApplication.user;
//		
//		int targetStep = user.step;
//		int wearLocation = 0;
//		int sport_mode = 1;
//		int sex = user.sex;
//		int year= user.year;
//		int nowYear = Calendar.getInstance().get(Calendar.YEAR);
//		int age = nowYear-year;
//		float height = user.height;
//		float weight = user.weight;
//		int distanceUnit = 0;
//		boolean keptOnOffblean = false;
//		int keptOnOff = keptOnOffblean==true?1:0;
//		BleUserInfoBean bean = new BleUserInfoBean(targetStep, wearLocation, sport_mode, sex, age, weight, height, distanceUnit, keptOnOff);
//		BleSyncParamsTask bleSyncParamsTask = new BleSyncParamsTask(this, syncParmasCallBack, bean);
//		return bleSyncParamsTask;
//	}
//	
//	Handler syncParamHandler = new Handler(){
//		@Override
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case BleCallBack.TASK_START:
//				
//				break;
//			case BleCallBack.TASK_PROGRESS:				
//				break;
//			case BleCallBack.TASK_FINISH:
//				connectHandler.sendEmptyMessage(DEVICE_SYNCPARAMSDONE);
//				break;
//			case BleCallBack.TASK_FAILED:
//				BleSyncParamsTask task = getBleSyncParamsTask();
//				task.work();
//				break;
//			}
//		}
//		
//	};
//	
//	BleCallBack syncParmasCallBack = new BleCallBack(syncParamHandler);
//
//	Handler requestBindDeviceHandler = new Handler(){
//		
//		@Override
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case BleCallBack.TASK_START:
//
//				break;
//
//			case BleCallBack.TASK_PROGRESS:
//				
//				break;	
//			case BleCallBack.TASK_FINISH:
//				//这里提示用户敲击手环进行绑定
//				//setWaitMessage("请您敲击皮带头进行绑定确认");
//				waitClose();
//				showClickDeviceDialog();
//				break;
//			case BleCallBack.TASK_FAILED:
//				waitClose();
//				closeAllDialog();
//				break;
//			}
//		}
//		
//	};
//	
//	BleCallBack requestBindDeviceCallBack = new BleCallBack(requestBindDeviceHandler);
	
	
	AlertDialog clickDeviceDialog = null;
	AlertDialog deviceSelDialog = null;
	AlertDialog bindFailDialog = null;
	AlertDialog connectingDialog = null;
	
	public void showBindFailDialog() {
		
		if(bindFailDialog!=null&&bindFailDialog.isShowing()){
			return;
		}
		
		bindFailDialog = new AlertDialog.Builder(this).create();
		Window view = bindFailDialog.getWindow();
		bindFailDialog.show();
		bindFailDialog.setContentView(R.layout.popup_bind_fail);

		TextView textViewReSel = (TextView) view
				.findViewById(R.id.id_textview_resel);

		class PopupClickListener implements OnClickListener {

			

			public PopupClickListener() {
				
			}

			@Override
			public void onClick(View v) {
				switch (v.getId()) {

				case R.id.id_textview_resel:

					if (bindFailDialog != null) {
						bindFailDialog.dismiss();
					}
					bindFailDialog = null;
					
					if(devices!=null&&devices.size()>0){
						showDeviceSelDialog();
					} else {
						
					}
					
					break;
				}
			}
		}

		PopupClickListener clicklistener = new PopupClickListener();

		textViewReSel.setOnClickListener(clicklistener);

	}
	
	
//	public void showClickDeviceDialog() {
//		
//		if(clickDeviceDialog!=null&&clickDeviceDialog.isShowing()){
//			return;
//		}
//		
//		clickDeviceDialog = new AlertDialog.Builder(this).create();
//		Window view = clickDeviceDialog.getWindow();
//		clickDeviceDialog.show();
//		clickDeviceDialog.setContentView(R.layout.popup_qiaoji);
//
//		ImageView imageView = (ImageView) view
//				.findViewById(R.id.id_imageview_popup);
//		
//		TextView cancle_textView = (TextView) view.findViewById(R.id.id_textview_cancel_at_qiaoji);
//		
//		clickDeviceDialog.setCancelable(false);
//		
//		class PopupClickListener implements OnClickListener {
//
//			AlertDialog alertDialog;
//
//			public PopupClickListener(AlertDialog alertDialog) {
//				this.alertDialog = alertDialog;
//			}
//
//			@Override
//			public void onClick(View v) {
//				switch (v.getId()) {
//
//				case R.id.id_imageview_popup:
//
//					break;
//				case R.id.id_textview_cancel_at_qiaoji:
//					VLBleServiceManager.getInstance().unBindService(getApplication());
//					if (alertDialog != null) {
//						alertDialog.dismiss();
//					}
//					alertDialog = null;
//					break;
//				}
//			}
//		}
//
//		PopupClickListener clicklistener = new PopupClickListener(clickDeviceDialog);
//
//		imageView.setOnClickListener(clicklistener);
//		cancle_textView.setOnClickListener(clicklistener);
//	}

	
	

	
	
	public void showDeviceSelDialog() {
		
		if(deviceSelDialog!=null&&deviceSelDialog.isShowing()){
			return;
		}
		
		
		
		deviceSelDialog = new AlertDialog.Builder(this).create();
		Window view = deviceSelDialog.getWindow();
		
		deviceSelDialog.setOnDismissListener(new OnDismissListener() {
			//窗口消失就要停止扫描任务
			@Override
			public void onDismiss(DialogInterface dialog) {
				 stopScan();
			}
		});
		
		deviceSelDialog.show();
		deviceSelDialog.setContentView(R.layout.popup_sel_device);
		
		
		ListView lv = (ListView) view.findViewById(R.id.id_listView_device_list);
		adapter = new BleDeviceListAdapter(ScanActivity.this);
		lv.setAdapter(adapter);
		
		lv.setOnItemClickListener(ScanActivity.this);
		
		//设置状态为搜索设备中
		processbar_searching = (ProgressBar) view.findViewById(R.id.id_processbar_searching);
		textview_searching   = (TextView)	view.findViewById(R.id.id_textview_searching);
		
		textview_searching.setText(R.string.searching_devices);
		processbar_searching.setVisibility(View.VISIBLE);
		
		//已扫描过,重新选择设备连接，不进行搜索
		if (null != devices && devices.size() > 0) {
			for (int i = 0; i < devices.size(); i++) {
				adapter.addDeviceItem(devices.get(i));
			}
			textview_searching.setText(R.string.select_devices);
			processbar_searching.setVisibility(View.INVISIBLE);
		}
		
		
		class PopupClickListener implements OnClickListener {

			

			public PopupClickListener() {
			
			}

			@Override
			public void onClick(View v) {
				
				
				switch (v.getId()) {

				case R.id.id_textview_cancel:
	
					if (deviceSelDialog != null) {
						deviceSelDialog.dismiss();
					}
					deviceSelDialog = null;
					waitClose();
					break;
				}
			}
		}

		PopupClickListener clicklistener = new PopupClickListener();
		
		TextView textViewCancel = (TextView) view
				.findViewById(R.id.id_textview_cancel);
		
		textViewCancel.setOnClickListener(clicklistener);

	}
	
	private void ShowConnectingDialog() {
		if (connectingDialog != null && connectingDialog.isShowing()) {
			return ;
		}
		
		connectingDialog = new AlertDialog.Builder(this).create();
		Window view = connectingDialog.getWindow();
		
		connectingDialog.show();
		connectingDialog.setContentView(R.layout.dialog_connecting_belt);
		
		connectingDialog.setCancelable(false);
		
		class PopupClickListener implements OnClickListener {

			

			public PopupClickListener() {
			
			}

			@Override
			public void onClick(View v) {
				
				
				switch (v.getId()) {

				case R.id.id_textview_cancel_at_connecting:
//					VLBleServiceManager.getInstance().unBindService(getApplication());
//					sdk.unBindDevice(null);
					waitClose();
					sdk.disConnectDevice();
					E3AKeeper.getInstance().unBinderDevice(getApplication());
					if (connectingDialog != null) {
						connectingDialog.dismiss();
					}
					connectingDialog = null;
					
					break;
				}
			}
		}

		PopupClickListener clicklistener = new PopupClickListener();
		
		TextView textViewCancel = (TextView) view
				.findViewById(R.id.id_textview_cancel_at_connecting);
		
		textViewCancel.setOnClickListener(clicklistener);
	}
	
	private void closeAllDialog(){
		
		if(clickDeviceDialog!=null){
			clickDeviceDialog.dismiss();
			clickDeviceDialog = null;
		}
		if(deviceSelDialog!=null){
			deviceSelDialog.dismiss();
			deviceSelDialog = null;
		}
		if(bindFailDialog!=null){
			bindFailDialog.dismiss();
			bindFailDialog = null;
		}
		if(connectingDialog!=null) {
			connectingDialog.dismiss();
			connectingDialog = null;
		}
		
	}
		 


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
//		if(scanTask != null) stopScan();
		stopScan();
		
		if(deviceSelDialog!=null){
			deviceSelDialog.dismiss();
			deviceSelDialog = null;
		}
		BluetoothDeviceBean device = adapter.listItems
				.get(position);
//		showWaitCanelable(getString(R.string.binding_device));
		
		Log.i("sino","开始绑定");
		
		String addr = device.getAddress();
		String name = device.getName();
		
		ShowConnectingDialog();

		//单防丢皮带
		if (name.equals("E3-A")) {
//	        final Intent intent = new Intent(this, LostOnlyMainActivity.class);
//	        intent.putExtra(LostOnlyMainActivity.EXTRAS_DEVICE_NAME, device.getDevice_name());
//	        intent.putExtra(LostOnlyMainActivity.EXTRAS_DEVICE_ADDRESS, device.getDevice_address());
//	        startActivity(intent);
//	        return ;
			E3AKeeper.getInstance().setRemoteDevice(device.getName(), device.getAddress());
			E3AKeeper.getInstance().binderDevice(getApplication());
			return;
		}

		Log.e(TAG, "address = " + addr);
		Log.e(TAG, "name = " + name);
		Keeper.setBindDeviceMacAddress(ScanActivity.this, addr);
		Keeper.setBindDeviceName(ScanActivity.this, name);
											
		sdk.bindDevice(Keeper.getBindDeviceName(ScanActivity.this), Keeper.getBindDeviceMacAddress(ScanActivity.this), bindDeviceListener);
		sdk.registerDeviceStateObserver(new DeviceStateObserver() {
			
			@Override
			public void disConnected() {
				Log.e(TAG, "断开连接");
			} 
			
			@Override
			public void connecting() {
				Log.e(TAG, "正在连接");

			}
			
			@Override
			public void connected() {
				Log.e(TAG, "连接成功");
			}
			
			@Override
			public void blueToothClose() {
				// TODO Auto-generated method stub
				
			}
		});		
		isConnecting = true;
	}
	
	private BindDeviceListener bindDeviceListener = new BindDeviceListener() {
		
		@Override
		public void onFail(String errormessge) {
			Log.e(TAG, "连接失败");
		}
		
		@Override
		public void onComplete() {
			Keeper.setUserHasBindBand(getApplicationContext(), true);				
			
			waitClose();
			closeAllDialog();
			ToastUtil.toast(R.string.device_bind_success);
		
			MainActivity.gotoActivity(ScanActivity.this);
			ScanActivity.this.finish();
			
		}
		
		@Override
		public void onClickToBind() {			
		}
	};
	
	public static void gotoActivity(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, ScanActivity.class);
		context.startActivity(intent);
	}
	
	
}

