package com.jibu.app.main;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import mybleservice.E3AKeeper;

import com.google.gson.Gson;
import com.jibu.app.R;
import com.jibu.app.database.MoveDataService;
import com.jibu.app.database.UserService;
import com.jibu.app.entity.MoveData;
import com.jibu.app.entity.User;
import com.jibu.app.view.RoundProgressBar2;
import com.veclink.bracelet.bean.BleUserInfoBean;
import com.veclink.bracelet.bean.BluetoothDeviceBean;
import com.veclink.bracelet.bean.DeviceInfo;
import com.veclink.bracelet.bean.DeviceSportAndSleepData;
import com.veclink.bracelet.bletask.BleCallBack;
import com.veclink.bracelet.bletask.BleQueryFirmareVersionTask;
import com.veclink.bracelet.bletask.BleQueryPowerValueTask;
import com.veclink.bracelet.bletask.BleReBootOrResetParamsDeviceRask;
import com.veclink.bracelet.bletask.BleScanDeviceTask;
import com.veclink.bracelet.bletask.BleSyncNewDeviceDataTask;
import com.veclink.bracelet.bletask.BleSyncParamsTask;
import com.veclink.bracelet.bletask.BleTask;
import com.veclink.bracelet.bletask.UpdateFirmwareUtil;
import com.veclink.hw.bledevice.BraceletNewDevice;
import com.veclink.hw.bleservice.VLBleServiceManager;
import com.veclink.hw.bleservice.util.Keeper;

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

public class YaodaiE3AActivity extends WaitingActivity implements OnClickListener{

	public static YaodaiE3AActivity activity = null;
	
	public final int REQUREST_UPDATEFIREWARE = 0xC;
	
	public static final String FIREWARE_NAME = "D013A_V43.img";
	
	private static int HW_VERION_BY_ASSERT = 43;
	
	MainApplication mainApplication;
	
	BleCallBack scanDeviceCallBack;
	
	BleScanDeviceTask scanTask = null;
	
	Vector<BluetoothDeviceBean> devices = null;
		
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
		setContentView(R.layout.activity_yaodai_e3a);
		((MainApplication) this.getApplication()).addActivity(this);

		
		activity = this;
		mainApplication = (MainApplication) this.getApplication();
		if (null == mainApplication.user) {
			WelcomeActivity.gotoActivity(this);
			this.finish();
		} else {
			user = mainApplication.user;
		
	
//		syncParams(user);
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
			 this.finish();
				break;
		case R.id.id_imageview_huifu:
			ToastUtil.toast("该功能未开放");
				break;
		case R.id.id_imageview_update:
			ToastUtil.toast("该功能未开放");
			break;
		case R.id.id_imageview_unbind:
			hdlrUnbindOnclick();
			break;
		case R.id.id_roundProgressBar2:
		
			
			break;
		}
		
	}
	
	
	private void hdlrUnbindOnclick(){
		
		
		new AlertDialog.Builder(YaodaiE3AActivity.this)
		.setMessage(R.string.confirm_unbind_ebelt)
		.setNeutralButton(R.string.confirm, new DialogInterface.OnClickListener() {

			@Override

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				unBindDevice();
				ScanActivity.gotoActivity(YaodaiE3AActivity.this);
				YaodaiE3AActivity.this.finish();
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
		
		
		new AlertDialog.Builder(YaodaiE3AActivity.this)
		.setMessage("该功能未开放")
		.setNeutralButton(R.string.confirm, new DialogInterface.OnClickListener() {

			@Override

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				//V35版本缺少查询固件版本的指令，暂时修改
//				queryHwVersion();
				
			}

		})
		.show();

	
		
	}
	
	private void hdlrResetOnclick(){
		
		
		new AlertDialog.Builder(YaodaiE3AActivity.this)
		.setMessage("该功能未开放")
		.setNeutralButton(R.string.confirm, new DialogInterface.OnClickListener() {

			@Override

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();				
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
		
		if (Keeper.getUserHasBindBand(YaodaiE3AActivity.this)) {
			firmware_version = Keeper.getDeviceRomVersion(YaodaiE3AActivity.this);
		}
		textViewDc = (TextView)findViewById(R.id.id_textview_dc);
		textViewXinhao = (TextView)findViewById(R.id.id_textview_xinghao);
		textViewUpdateTime = (TextView)findViewById(R.id.id_textview_update_time);
		
		imageViewDc = (ImageView)findViewById(R.id.id_imageview_dc);
//		textViewXinhao.setText(getString(R.string.xinghao_d013)+firmware_version);
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
		E3AKeeper.getInstance().clearBindDeviceMessage(this);
		E3AKeeper.getInstance().unBinderDevice(getApplication());
	}
	
	
	
	
	
	
	

	

	
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
	
	
	public static void gotoActivity(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, YaodaiE3AActivity.class);
		context.startActivity(intent);
	}
	
	
}
