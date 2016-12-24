package com.jibu.app.main;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import com.google.gson.Gson;
import com.jibu.app.R;
import com.jibu.app.database.MoveDataService;
import com.jibu.app.database.UserService;
import com.jibu.app.entity.MoveData;
import com.jibu.app.entity.User;
import com.jibu.app.view.RoundProgressBar2;
import com.szants.bracelet.bean.BeltDeviceInfo;
import com.szants.bracelet.bean.BleDeviceData;
import com.szants.bracelet.bean.BleUserInfoBean;
import com.szants.bracelet.bletask.BleCallBack;
import com.szants.bracelet.bletask.BleProgressCallback;
import com.szants.bracelet.bletask.BleReBootOrResetParamsDeviceRask;
import com.szants.bracelet.bletask.UpdateFirmwareUtil;
import com.szants.hw.bledevice.BraceletNewDevice;
import com.szants.hw.bleservice.util.Keeper;
import com.szants.sdk.AntsBeltSDK;
import com.szants.sdk.UnBindDeviceListener;


import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class YaodaiActivity extends WaitingActivity implements OnClickListener{

	public static YaodaiActivity activity = null;
	
	public final int REQUREST_UPDATEFIREWARE = 0xC;
	
	public static final String FIREWARE_NAME = "SMART_BELT_V4.img";
	
	private static int HW_VERION_BY_ASSERT = 4;
	
	MainApplication mainApplication;
	
//	BleCallBack scanDeviceCallBack;
//	
//	BleScanDeviceTask scanTask = null;
//	
//	Vector<BluetoothDeviceBean> devices = null;
		
	public User user;
	
	boolean isShowSync = false;
	
	String firmware_version = "1";
	
	MoveDataService  moveDataService;
	UserService userService;
	
	RoundProgressBar2 roundProgressBar;
	TextView textViewDc,textViewXinhao,textViewUpdateTime;
	ImageView imageViewDc;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_yaodai);
		((MainApplication) this.getApplication()).addActivity(this);

		
		activity = this;
		mainApplication = (MainApplication) this.getApplication();
		if (null == mainApplication.user) {
			WelcomeActivity.gotoActivity(this);
			this.finish();
		} else {
			user = mainApplication.user;
		
			moveDataService = new MoveDataService(this);
			userService = new UserService(this);
		
//		syncParams(user);
//			queryPower();
			initView();

		}
		
	}
	

	@Override
	protected void onStart() {

		super.onStart();

		

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
		
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

//			((MainApplication) this.getApplication()).exit();
			this.finish();
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			
		case R.id.id_linearlayout_title_left:
			if(!isShowSync){
				this.finish();
			}
				break;
		case R.id.id_imageview_huifu:
			if(!isShowSync){
				hdlrResetOnclick();
			}
				break;
		case R.id.id_imageview_update:
			if(!isShowSync){
				hdlrUpdateOnclick();
//				Intent intent = new Intent(YaodaiActivity.this, UpdateFirewareActivity.class);
//				startActivityForResult(intent, REQUREST_UPDATEFIREWARE);
			}
			break;
		case R.id.id_imageview_unbind:
			if(!isShowSync){
				hdlrUnbindOnclick();
			}
			break;
		case R.id.id_roundProgressBar2:
			
			if(!isShowSync){
				
//				long timeJiange = System.currentTimeMillis()-user.updateTime;
//				if(timeJiange>=(15*60*1000)){
				
					isShowSync = true;
					
					textViewUpdateTime.setText(R.string.syncing);
					syncSportDataByUpdateTime(user.updateTime);
//				}else{
//					ToastUtil.toast("您已同步，请稍后在同步数据！");
//				}
			}
			
			break;
		}
		
	}
	
	
	private void hdlrUnbindOnclick(){
		
		
		new AlertDialog.Builder(YaodaiActivity.this)
		.setMessage(R.string.confirm_unbind_ebelt)
		.setNeutralButton(R.string.confirm, new DialogInterface.OnClickListener() {

			@Override

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				unBindDevice();
				ScanActivity.gotoActivity(YaodaiActivity.this);
				YaodaiActivity.this.finish();
				if(MainActivity.activity!=null){
					MainActivity.activity.finish();
				}
				
			}

		})
		.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

			@Override

			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss(); //关闭alertDialog

			}

		})
		.show();

	
		
	}
	
	private void hdlrUpdateOnclick(){
		
		
		new AlertDialog.Builder(YaodaiActivity.this)
		.setMessage(R.string.confirm_update_fireware_version)
		.setNeutralButton(R.string.confirm, new DialogInterface.OnClickListener() {

			@Override

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				//V35版本缺少查询固件版本的指令，暂时修改
//				queryHwVersion();
				
				syncParams(user);
			}

		})
		.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

			@Override

			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss(); //关闭alertDialog

			}

		})
		.show();

	
		
	}
	
	private void hdlrResetOnclick(){
		
		
		new AlertDialog.Builder(YaodaiActivity.this)
		.setMessage(R.string.confirm_revert_factory_set)
		.setNeutralButton(R.string.confirm, new DialogInterface.OnClickListener() {

			@Override

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				reset();
				
			}

		})
		.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

			@Override

			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss(); //关闭alertDialog

			}

		})
		.show();

	
		
	}

	private void initView(){
		
		if (Keeper.getUserHasBindBand(YaodaiActivity.this)) {
			firmware_version = Keeper.getDeviceRomVersion(YaodaiActivity.this);
		}
		textViewDc = (TextView)findViewById(R.id.id_textview_dc);
		textViewXinhao = (TextView)findViewById(R.id.id_textview_xinghao);
		textViewUpdateTime = (TextView)findViewById(R.id.id_textview_update_time);
		
		imageViewDc = (ImageView)findViewById(R.id.id_imageview_dc);
		textViewXinhao.setText(getString(R.string.xinghao_d013)+firmware_version);
		Log.i("log","initView"+firmware_version);
		
		roundProgressBar = (RoundProgressBar2)findViewById(R.id.id_roundProgressBar2);
		roundProgressBar.setMax(100);
		roundProgressBar.setProgress(0);
		
		findViewById(R.id.id_linearlayout_title_left).setOnClickListener(this);
		findViewById(R.id.id_imageview_huifu).setOnClickListener(this);
		findViewById(R.id.id_imageview_update).setOnClickListener(this);
		findViewById(R.id.id_imageview_unbind).setOnClickListener(this);
		findViewById(R.id.id_roundProgressBar2).setOnClickListener(this);
		
		showUpdateTime();
		
		int dcValue = ApplicationSharedPreferences.getDcValue(this);
		showDc(dcValue);
	}
	

	
	

	
	private void showUpdateTime(){
		SimpleDateFormat    formatter    =   new    SimpleDateFormat    (getString(R.string.mm_month_dd_day)+" "+getString(R.string.hh_mm_update));     
		Date    curDate    =   new    Date(user.updateTime);//获取当前时间     
		textViewUpdateTime.setText(formatter.format(curDate));
	}
	
	
	
	
	
	
	private void unBindDevice(){
		AntsBeltSDK.getInstance().unBindDevice(new UnBindDeviceListener() {
			
			@Override
			public void onFail(String arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onComplete() {
				// TODO Auto-generated method stub
				Keeper.clearBindDeviceMessage(YaodaiActivity.this);
			}
		});
//		VLBleServiceManager.getInstance().unBindService(getApplication());
		
	}
	
	
	
	
//	Handler syncParamHandler = new Handler(){
//
//		@Override
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case BleCallBack.TASK_START:
//				//showMsgView.setText(mContext.getString(R.string.str_start_sync_params));
//				break;
//
//			case BleCallBack.TASK_PROGRESS:
//				
//				break;
//
//			case BleCallBack.TASK_FINISH:
//			{
//				if(msg.obj!=null){
//					DeviceInfo deviceInfo = (DeviceInfo)msg.obj;
//					
//					//在这里把固件版本转成INT型。
//					int version = deviceInfo.firmware_version;
//					
//					if(version<HW_VERION_BY_ASSERT){
//						Intent intent = new Intent(YaodaiActivity.this, UpdateFirewareActivity.class);
//						startActivityForResult(intent, REQUREST_UPDATEFIREWARE);
//					}else{
//						ToastUtil.toast(R.string.fireware_is_lastest);
//					}
//					
//					
//				}
//				waitClose();
//			}
//				break;
//
//			case BleCallBack.TASK_FAILED:
//				//showMsgView.setText(mContext.getString(R.string.str_sync_data_fail));
//				waitClose();
//				ToastUtil.toast(R.string.update_fireware_fail);
//				break;
//			}
//		}
//		
//	};
	
//	BleCallBack syncParmasCallBack = new BleCallBack(syncParamHandler);
//	
//	public BleSyncParamsTask getBleSyncParamsTask(User user) {
//		int targetStep = user.step;
//		int wearLocation = 0;
//		int sport_mode = 1;
//		int sex = user.sex;
//		int year= user.year;
//		int nowYear = Calendar.getInstance().get(Calendar.YEAR);
//		int age = nowYear-year;
//		float height = user.height;
//		float weight = user.waist;
//		int distanceUnit = 0;
//		boolean keptOnOffblean = false;
//		int keptOnOff = keptOnOffblean==true?1:0;
//		BleUserInfoBean bean = new BleUserInfoBean(targetStep, wearLocation, sport_mode, sex, age, weight, height, distanceUnit, keptOnOff);
//		BleSyncParamsTask bleSyncParamsTask = new BleSyncParamsTask(this, syncParmasCallBack, bean);
//		return bleSyncParamsTask;
//	}
//	
	private void syncParams(User user){
//		BleTask task = null;
//		task = getBleSyncParamsTask(user);
//		
//		if(task!=null){
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
			}
			
			@Override
			public void onFinish(Object result) {
				BeltDeviceInfo deviceInfo = (BeltDeviceInfo)result;
					
				//在这里把固件版本转成INT型。
				int version = deviceInfo.firmware_version;
				
				if(version<HW_VERION_BY_ASSERT){
					Intent intent = new Intent(YaodaiActivity.this, UpdateFirewareActivity.class);
					startActivityForResult(intent, REQUREST_UPDATEFIREWARE);
				}else{
					ToastUtil.toast(R.string.fireware_is_lastest);
				}
					
					
				waitClose();

			}
			
			@Override
			public void onFailed(Object error) {
				waitClose();
				ToastUtil.toast(R.string.update_fireware_fail);
			}
		});
	}
//	
//	
//	
//	
//	Handler syncStepDataHandler = new Handler(){
//
//		@Override
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case BleCallBack.TASK_START:
//				//showMsgView.setText(mContext.getString(R.string.star_sync_data));
//				break;
//
//			case BleCallBack.TASK_PROGRESS:
//				if(msg.obj!=null){
//					int progress = (Integer) msg.obj;
//					
//					if(isShowSync){
//						
//						roundProgressBar.setProgress(progress);
//					}
//					
//				}
//				break;
//
//			case BleCallBack.TASK_FINISH:
//				
//				if(msg.obj!=null){
//					DeviceSportAndSleepData deviceSportAndSleepData= (DeviceSportAndSleepData) msg.obj;	
//					
//					Gson gon = new Gson();
//					String result = gon.toJson( msg.obj);
//					Log.i("log","result="+result);
//					
//					MoveData.hdlrSyncStepData(user,moveDataService,result);
//					
//				}
//				
//				
//				if(isShowSync){
//					
//					roundProgressBar.setProgress(100);					
//					//mainFlipper.removeViewAt(0);
//				
//					isShowSync = false;
//		
//					user.updateTime = System.currentTimeMillis();
//					userService.updateUser(user);
//					showUpdateTime();
//					ToastUtil.toast(R.string.sync_complete);
//					roundProgressBar.setProgress(0);
//				}
//				
//				break;
//
//			case BleCallBack.TASK_FAILED:
//				//showMsgView.setText(mContext.getString(R.string.str_sync_data_fail));
//				ToastUtil.toast(R.string.sync_fail_try_again);
//				
//				showUpdateTime();
//				
//				isShowSync = false;
//
//				break;
//			}
//		}
//		
//	};
//	
//	BleCallBack syncStepDataCallBack = new BleCallBack(syncStepDataHandler);
	
	private void syncSportDataByUpdateTime(long updateTime){

//		BleTask task = null;
//		task = new BleSyncNewDeviceDataTask(this, syncStepDataCallBack,BraceletNewDevice.SPORT_DATA,new Date(updateTime),new Date(System.currentTimeMillis()));
//		if(task!=null){
//			task.work();
//		}
		AntsBeltSDK.getInstance().syncStepData(updateTime, updateTime, new BleProgressCallback() {
			
			@Override
			public void onStart(Object startObject) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFinish(Object res) {
				Gson gon = new Gson();
				String result = gon.toJson(res);
				Log.i("log","result="+result);
				
				MoveData.hdlrSyncStepData(user,moveDataService,result);
				if(isShowSync){
				
					roundProgressBar.setProgress(100);					
					//mainFlipper.removeViewAt(0);
				
					isShowSync = false;
		
					user.updateTime = System.currentTimeMillis();
					userService.updateUser(user);
					showUpdateTime();
					ToastUtil.toast(R.string.sync_complete);
					roundProgressBar.setProgress(0);
				}
			}
			
			@Override
			public void onFailed(Object error) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgress(Object progress) {
				int progrs = (Integer)progress;	
				
				if(isShowSync){
					
					roundProgressBar.setProgress(progrs);
				}
			}
		});
	}
	
//	private void syncAllSportData(){
//		BleTask task = null;
//		task = new BleSyncNewDeviceDataTask(this, syncStepDataCallBack,BraceletNewDevice.SPORT_DATA);
//		if(task!=null){
//			task.work();
//		}
//	}
	
	private void showDc(int powerValue){
		
		textViewDc.setText(powerValue+"%");
		if(powerValue>=0&&powerValue<10){
			imageViewDc.setBackgroundResource(R.drawable.dc_10);
		}else if(powerValue>=10&&powerValue<20){
			imageViewDc.setBackgroundResource(R.drawable.dc_20);
		}else if(powerValue>=20&&powerValue<30){
			imageViewDc.setBackgroundResource(R.drawable.dc_30);
		}else if(powerValue>=30&&powerValue<40){
			imageViewDc.setBackgroundResource(R.drawable.dc_40);
		}else if(powerValue>=40&&powerValue<50){
			imageViewDc.setBackgroundResource(R.drawable.dc_50);
		}else if(powerValue>=50&&powerValue<60){
			imageViewDc.setBackgroundResource(R.drawable.dc_60);
		}else if(powerValue>=60&&powerValue<70){
			imageViewDc.setBackgroundResource(R.drawable.dc_70);
		}else if(powerValue>=70&&powerValue<80){
			imageViewDc.setBackgroundResource(R.drawable.dc_80);
		}else if(powerValue>=80&&powerValue<90){
			imageViewDc.setBackgroundResource(R.drawable.dc_90);
		}else if(powerValue>=90){
			imageViewDc.setBackgroundResource(R.drawable.dc_100);
		}
		
	}
//	
//	Handler queryPowerHandler = new Handler(){
//
//		@Override
//		public void handleMessage(Message msg) {
//			
//			if(msg.what == BleCallBack.TASK_FINISH){
//				int powerValue = (Integer) msg.obj;
////				if(activity!=null){
//					ApplicationSharedPreferences.setDcValue(YaodaiActivity.this, powerValue);
//					showDc(powerValue);
////				}
//				
//				
//				Log.i("log","powerValue:"+powerValue);
//			}
//		}
//		
//	};
//	
//	BleCallBack queryPowerCallBack = new BleCallBack(queryPowerHandler);
//	
//	
//	private void queryPower(){
//		BleTask task = null;
//		task = new BleQueryPowerValueTask(this, queryPowerCallBack);
//		if(task!=null){
//			task.work();
//		}
//	}
	
	
//	Handler updateHandler = new Handler(){
//
//		@Override
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case BleCallBack.TASK_START:
//				//showMsgView.setText(mContext.getString(R.string.str_start_update));
//				
//				break;
//
//			case BleCallBack.TASK_PROGRESS:
//				
//				if(msg.obj!=null){
//					int progress = (Integer)msg.obj;
//				//	showMsgView.setText(mContext.getString(R.string.sync_data_progress_format,progress));
//					changeWaitMsg(getString(R.string.update_progress)+progress+"%");
//					Log.i("log",getString(R.string.update_progress)+progress+"%");
//				}
//				break;
//
//			case BleCallBack.TASK_FINISH:
//				//showMsgView.setText(mContext.getString(R.string.str_update_success));
//				changeWaitMsg(getString(R.string.update_progress)+100+"%");
//				Log.i("log",getString(R.string.update_progress)+100+"%");
//				waitClose();
//				ToastUtil.toast(R.string.update_fireware_complete);
//				break;
//
//			case BleCallBack.TASK_FAILED:
//				//showMsgView.setText(mContext.getString(R.string.str_update_fail));
//				waitClose();
//				ToastUtil.toast(R.string.update_fireware_fail);
//				break;
//			}
//		}
//		
//	};
	
//	BleCallBack updateCallBack = new Up(updateHandler);
	public final int FILE_SELECT_CODE = 2000;
	
	
	
//	Handler queryVersionHandler = new Handler(){
//
//		@Override
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case BleCallBack.TASK_START:
//				//showMsgView.setText(mContext.getString(R.string.str_start_query_version));
//				break;
//
//			
//			case BleCallBack.TASK_FINISH:
//				if(msg.obj!=null){
//					DeviceInfo deviceInfo = (DeviceInfo)msg.obj;
//				//	showMsgView.setText(mContext.getString(R.string.str_query_result_is,deviceInfo.toString()));
//					
//					///ToastUtil.toast(deviceInfo.toString());
//					
//					//在这里把固件版本转成INT型。
//					int version = deviceInfo.firmware_version;
//					
//					if(version<HW_VERION_BY_ASSERT){
////						try {
////							updateHwVersion();
////							
////						} catch (IOException e) {
////							// TODO Auto-generated catch block
////							e.printStackTrace();
////						}
//						Intent intent = new Intent(YaodaiActivity.this, UpdateFirewareActivity.class);
//						startActivityForResult(intent, REQUREST_UPDATEFIREWARE);
//						waitClose();
//					}else{
//						waitClose();
//						ToastUtil.toast(R.string.fireware_is_lastest);
//					}
//					
//					
//				}
//				waitClose();
//				break;
//
//			case BleCallBack.TASK_FAILED:
//				waitClose();
//				ToastUtil.toast(R.string.update_fireware_fail);
//				break;
//			}
//		}
//		
//	};
	
	
//	BleCallBack queryVersionCallBack = new BleCallBack(queryVersionHandler);
//	
//	private void queryHwVersion(){
//		showWait(getString(R.string.qurey_fireware_version));
//		BleTask task = null;
//		task = new BleQueryFirmareVersionTask(this, queryVersionCallBack);
//		
//		if(task!=null){
//			task.work();
//		}
//	}
	
	private void updateHwVersion() throws IOException{


		Intent mIntent = new Intent(getApplicationContext(),
				FileSelectActivity.class);
		startActivityForResult(mIntent,	FILE_SELECT_CODE);
//		ToastUtil.toast("当前固件已经是最新固件");

//		changeWaitMsg(getString(R.string.begin_update_fireware_version));
//
//		String filePath = Environment.getExternalStorageDirectory().getPath()+"/D013A_V33.img";
//
//		copyBigDataToSD(filePath);
//		
//		UpdateFirmwareUtil.update(this, new BleProgressCallback() {
//			
//			@Override
//			public void onStart(Object arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onFinish(Object arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onFailed(Object arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onProgress(Object arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		}, filePath);
		
		
	}
	
	
//	Handler resetHandler = new Handler(){
//
//		@Override
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			
//			case BleCallBack.TASK_FINISH:
//				waitClose();
//				ToastUtil.toast(R.string.revert_factory_set_success);
//				break;
//
//			case BleCallBack.TASK_FAILED:
//				waitClose();
//				ToastUtil.toast(R.string.revert_factory_set_fail);
//				break;
//			}
//		}
//		
//	};
//	
//	BleCallBack resetDeviceCallBack = new BleCallBack(resetHandler);
	
	private void reset(){
		showWait();
		new BleReBootOrResetParamsDeviceRask(this, new BleCallBack() {
			
			@Override
			public void onStart(Object arg0) {
				// TODO Auto-generated method stub

			}
			
			@Override
			public void onFinish(Object arg0) {
				// TODO Auto-generated method stub
				waitClose();
				ToastUtil.toast(R.string.revert_factory_set_success);
			}
			
			@Override
			public void onFailed(Object arg0) {
				// TODO Auto-generated method stub
				waitClose();
				ToastUtil.toast(R.string.revert_factory_set_fail);
				
			}
		}, BraceletNewDevice.RESET_ALL_CONTENT_AND_REBOOT);
	}	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		Log.i("log","onActivityResult:requestCode="+requestCode);
		
		switch(requestCode){
		case FILE_SELECT_CODE:
			if(resultCode == RESULT_OK)
			{
				// Get the Uri of the selected file
				String filePath = null;
				filePath = data.getStringExtra("selectfile");
				if(filePath != null)
				{					
					showWait(getString(R.string.begin_update_fireware_version));
										
				}
			}
			break;
		case REQUREST_UPDATEFIREWARE:
			if (resultCode == RESULT_OK) {
				ToastUtil.toast(R.string.update_fireware_complete);
//				textViewXinhao.setText(getString(R.string.xinghao_d013) + HW_VERION_BY_ASSERT);
			} else if (resultCode == RESULT_CANCELED) {
				ToastUtil.toast(R.string.update_fireware_fail);
			}
		}
		super.onActivityResult(requestCode,resultCode,data);
	}
	

public static void copyBigDataToSD(String strOutFileName) throws IOException 
    {  
        InputStream myInput;  
        OutputStream myOutput = new FileOutputStream(strOutFileName);  
        
        myInput = MainApplication.context.getAssets().open(FIREWARE_NAME);  
        byte[] buffer = new byte[1024];  
        int length = myInput.read(buffer);
        while(length > 0)
        {
            myOutput.write(buffer, 0, length); 
            length = myInput.read(buffer);
        }
        
        myOutput.flush();  
        myInput.close();  
        myOutput.close();        
    }	
	
	public static void gotoActivity(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, YaodaiActivity.class);
		context.startActivity(intent);
	}
	
	
}
