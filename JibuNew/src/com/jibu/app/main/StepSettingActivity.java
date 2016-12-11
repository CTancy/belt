package com.jibu.app.main;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import com.google.gson.Gson;
import com.jibu.app.R;
import com.jibu.app.R.layout;
import com.jibu.app.R.menu;
import com.jibu.app.database.MoveDataService;
import com.jibu.app.database.UserService;
import com.jibu.app.entity.HuanSuanUtil;
import com.jibu.app.entity.MoveData;
import com.jibu.app.entity.User;
import com.jibu.app.login.LoginAndRegActivity;
import com.jibu.app.user.HeightActivity;
import com.jibu.app.user.WaistlineActivity;
import com.jibu.app.user.WeightActivity;
import com.jibu.app.view.ProgressView;
import com.jibu.app.view.RoundProgressBar;
import com.umeng.analytics.MobclickAgent;
import com.veclink.bracelet.bean.BleLongSittingRemindParam;
import com.veclink.bracelet.bean.BleUserInfoBean;
import com.veclink.bracelet.bean.BluetoothDeviceBean;
import com.veclink.bracelet.bean.DeviceInfo;
import com.veclink.bracelet.bean.DeviceSleepData;
import com.veclink.bracelet.bean.DeviceSportAndSleepData;
import com.veclink.bracelet.bean.DeviceSportData;
import com.veclink.bracelet.bletask.BleCallBack;
import com.veclink.bracelet.bletask.BleScanDeviceTask;
import com.veclink.bracelet.bletask.BleSettingRemindParamsTask;
import com.veclink.bracelet.bletask.BleSyncNewDeviceDataTask;
import com.veclink.bracelet.bletask.BleSyncParamsTask;
import com.veclink.bracelet.bletask.BleTask;
import com.veclink.hw.bledevice.BraceletNewDevice;
import com.veclink.hw.bleservice.VLBleService;
import com.veclink.hw.bleservice.VLBleServiceManager;
import com.veclink.hw.bleservice.util.Debug;
import com.veclink.hw.bleservice.util.Keeper;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;

public class StepSettingActivity extends WaitingActivity implements OnClickListener ,OnTouchListener{

	public static StepSettingActivity activity = null;

	
	public static String ACTION_STEP_CHANGE = "action.step.change";

	MainApplication mainApplication;
	
	

	UserService userService;
	
	int progress;
	
	TextView textViewLength,textViewStep,textViewCal;
	
	ProgressView progressView;
	
	private final static int MAX_STEP = 30000;
	
	int currStep;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_step_setting);
		((MainApplication) this.getApplication()).addActivity(this);

		
		activity = this;
		mainApplication = (MainApplication) this.getApplication();
		
		userService = new UserService(this);
		if (null == mainApplication.user) {
			WelcomeActivity.gotoActivity(this);
			this.finish();
		} else {
			progress = mainApplication.user.step*300/MAX_STEP;
		
			currStep = mainApplication.user.step;
		
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
		case R.id.id_linearlayout_title_right:
				
			mainApplication.user.step= currStep;
			userService.updateUser(mainApplication.user);
			
			/*更新目标数据*/
			MoveDataService moveDataService = new MoveDataService(this);
			MoveData moveData = moveDataService.queryMoveDataByUserSpecDay(mainApplication.user.userId, System.currentTimeMillis());
			
			if (moveData != null) {
				moveData.todayTarget =  currStep;
				moveDataService.insertMoveData(moveData);
			}
			
			Intent intent = new Intent(ACTION_STEP_CHANGE);
			intent.putExtra("step", mainApplication.user.step);
			sendBroadcast(intent);
			
			this.finish();
				break;
		}
	}
	
	int x,y;
	int showProgress;
	@Override
	public boolean onTouch(View v, MotionEvent event) {

	int action=event.getAction();
	Log.i("action", action+"");



	  switch(action){  
	                case MotionEvent.ACTION_DOWN:  
	                	x=(int)event.getX();
	                	y=(int)event.getY();
	                    break;  
	        
	                case MotionEvent.ACTION_MOVE:
	                	 
	                    
	                    	 int mx=(int)event.getX();
	                    	// int my=(int)event.getY();
	                    	
	                    	 int dx=mx-x;
	                    	 //int dy=my-y;
	                    	 
	                    	 
	                    	 
	                    	 int width = progressView.getMeasuredWidth();
	                    	 
	                    	 int moveProgress = dx*300/width; 
	                    	 
	                    	 showProgress = progress+moveProgress;
	                    	 
	                    	// progress = progress+moveProgress;
	                    	 
	                    	 Log.i("log","x="+x+" mx="+mx+" width="+width+" moveProgress="+moveProgress+" progress="+progress);
	                    	 
	                    	 if(showProgress>300){
	                    		 showProgress = 300;
	                    	 }else if(showProgress<1){
	                    		 showProgress = 1;
	                    	 }
	                    	 
	                    	 progressView.setProgress(showProgress);
	                    	 
	                    	 currStep = showProgress*MAX_STEP/300;
	                    	 textViewStep.setText(String.valueOf(currStep));
	             			
	             			textViewLength.setText(String.valueOf(HuanSuanUtil.getLengthByStep(currStep)));
	             			
	             			textViewCal.setText(String.valueOf(HuanSuanUtil.getCalByStep(currStep)));
	                    	 
	                    	// x=(int)event.getX();
	                    	 //y=(int)event.getY();
	                    	 
	                     
	                      
	                    break;  
	                case MotionEvent.ACTION_UP:  
	                	
	                	progress=showProgress;
	                    break;                
	                }  
	return true;
	}
	
	
	private void initView(){
		
		
		textViewStep = (TextView)findViewById(R.id.textview_step);
		textViewLength = (TextView)findViewById(R.id.id_textview_length);
		textViewCal = (TextView)findViewById(R.id.id_textview_cal);
		
		progressView = (ProgressView)findViewById(R.id.id_progressview);
		progressView.setOnTouchListener(this);
		progressView.setProgress(progress);
		
		findViewById(R.id.id_linearlayout_title_left).setOnClickListener(this);
		findViewById(R.id.id_linearlayout_title_right).setOnClickListener(this);
		
		

		
		if(mainApplication.user!=null){
			
			
			textViewStep.setText(String.valueOf(currStep));
			
			textViewLength.setText(String.valueOf(HuanSuanUtil.getLengthByStep(currStep)));
			
			textViewCal.setText(String.valueOf(HuanSuanUtil.getCalByStep(currStep)));
			
			
		}

		
		
		
	}

	
	
	public static void gotoActivity(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, StepSettingActivity.class);
		context.startActivity(intent);
	}
	
}
