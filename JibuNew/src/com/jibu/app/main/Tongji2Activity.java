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
import com.jibu.app.entity.MoveData;
import com.jibu.app.entity.NoMoveData;
import com.jibu.app.entity.User;
import com.jibu.app.view.BFBView;
import com.jibu.app.view.HistogramView;
import com.jibu.app.view.NoMoveTongJiView;
import com.jibu.app.view.RoundProgressBar;
import com.umeng.analytics.MobclickAgent;
import com.veclink.bracelet.bean.BleUserInfoBean;
import com.veclink.bracelet.bean.BluetoothDeviceBean;
import com.veclink.bracelet.bean.DeviceInfo;
import com.veclink.bracelet.bean.DeviceSleepData;
import com.veclink.bracelet.bean.DeviceSportAndSleepData;
import com.veclink.bracelet.bean.DeviceSportData;
import com.veclink.bracelet.bletask.BleCallBack;
import com.veclink.bracelet.bletask.BleScanDeviceTask;
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
import android.graphics.Color;
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

public class Tongji2Activity extends WaitingActivity implements OnClickListener, OnTouchListener{

	public static Tongji2Activity activity = null;
	public static String TAG = "Tongji2Activity";

	MainApplication mainApplication;
	User user;
	
	
	
	private ViewFlipper mainFlipper = null;
	private GestureDetector mGestureDetector;
	private MoveDataService moveDataService;
	
	
	Vector<MoveData> showMoveDataForday;
	MoveData currShowMoveDataForday;
	
	TextView textViewTime;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tongji2);
		((MainApplication) this.getApplication()).addActivity(this);

		
		activity = this;
		mainApplication = (MainApplication) this.getApplication();
		if (null == mainApplication.user) {
			WelcomeActivity.gotoActivity(this);
		}
		user = mainApplication.user;
		moveDataService = new MoveDataService(this);
		
		
		
		initShowMoveDataForDay();
		
		initView();
		
		setFlipperView();
		
		initReciver();
		Log.e(TAG , "onCreate");
	//	showday(null);
	}
	
	
	private void initShowMoveDataForDay(){
		
		long currTime = System.currentTimeMillis();
		showMoveDataForday = new Vector<MoveData>();
		
		if (moveDataService != null) {
			currShowMoveDataForday = moveDataService.queryMoveDataByUserSpecDay(user.userId,currTime);
		}
		
		if(currShowMoveDataForday!=null){
			showMoveDataForday.add(currShowMoveDataForday);
		}else{
			currShowMoveDataForday = new MoveData(user.userId, currTime);
			moveDataService.insertMoveData(currShowMoveDataForday);
			showMoveDataForday.add(currShowMoveDataForday);
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
		unregisterReceiver(RemindMessageReceiver);
		activity = null;
		

	}
	
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			
		case R.id.id_linearlayout_title_left:
				MainActivity.gotoActivity(Tongji2Activity.this);
				this.finish();
				break;
		
		}
		
	}
	
	
	private void setFlipperView(){
		mainFlipper = (ViewFlipper) findViewById(R.id.id_viewflipper_main);
		mainFlipper.setOnTouchListener(this);
		mGestureDetector = new GestureDetector(this,new DefaultGestureListener());
		
		mainFlipper.removeAllViews();
		
		

			
			if (showMoveDataForday != null && showMoveDataForday.size() > 0) {
				
				/** add by ctc 将之前日期的数据添加至data中*/
				
				int index = mainApplication.filler_index;
				
				while (index > 0) {
					if(User.isExsitLastMoveData(currShowMoveDataForday,user)){
						currShowMoveDataForday =User.getLastMoveData(currShowMoveDataForday,moveDataService,user);
						showMoveDataForday.addElement(currShowMoveDataForday);
					}
					index --;
				}
				
				/**add by ctc*/
				for (int i = 0; i < showMoveDataForday.size(); i++) {

					LinearLayout moveDataLinearLayout = getShowSpecDayLinearLayout(showMoveDataForday.elementAt(i));

					mainFlipper.addView(moveDataLinearLayout);
				}

				mainFlipper.setOnTouchListener(this);
			}
			
			/*设置为上拉之前的日期对应的View*/
			mainFlipper.setDisplayedChild(mainApplication.filler_index);
			currShowMoveDataForday = showMoveDataForday.elementAt(mainFlipper.getDisplayedChild());
			setTitle(currShowMoveDataForday);
			mainApplication.filler_index = 0;
		
	}
	
	private void initView(){
		
				
		findViewById(R.id.id_linearlayout_title_left).setOnClickListener(this);
		
		setTitle(currShowMoveDataForday);
	}
	
	
	private LinearLayout getShowSpecDayLinearLayout(MoveData moveData){
		
		
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.linearlayout_tongji2, null);

		LinearLayout linearlayout = (LinearLayout) view
				.findViewById(R.id.id_linearlayout);
		
		BFBView bfbView = (BFBView)view.findViewById(R.id.id_bfbview);
		bfbView.setTime(moveData.getMoveTime(), moveData.getNoMoveTime());
	
		((TextView)view.findViewById(R.id.id_textview_move_time_bfb)).setText(String.valueOf(moveData.getMoveBFB()));
		((TextView)view.findViewById(R.id.id_textview_no_move_time_bfb)).setText(String.valueOf(moveData.getNoMoveBFB()));
		
		
		
		NoMoveTongJiView noMoveTongJiView = (NoMoveTongJiView)view.findViewById(R.id.id_nomovetongjiview);
		
		Vector<NoMoveData> noMoveDatas =  moveData.getNoMoveDatas();
		Vector<Float> values = new Vector<Float>();
		Vector<String> times = new Vector<String>();
		
		for(int i=0; i<noMoveDatas.size(); i++){
			
			NoMoveData noMoveData = noMoveDatas.elementAt(i);
			times.add(noMoveData.getNoMoveTimeDesc());
			values.add(noMoveData.getNoMoveTime());
		}
		
		
		noMoveTongJiView.setValue(values, times);
		
		
		return linearlayout;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public class DefaultGestureListener extends SimpleOnGestureListener {
		@Override
		public boolean onSingleTapUp(MotionEvent e) {

			
			
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {

		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {

			return false;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			Log.i("log","onFling");
			
			   // 手势向上 up
	           if ((e2.getY() - e1.getY()) > 80) {
	        	   
	        	   /** add by ctc 设置当前的ViewFlipper的页面位置*/
	           		if (mainFlipper != null && mainFlipper.getChildCount() >= 0) {
	           			if (mainApplication != null) {
	           				mainApplication.filler_index = mainFlipper.getDisplayedChild();
	           			}
	           		}
	        	   MainActivity.gotoActivity(Tongji2Activity.this);
//	           	   gotoMainActivity(0);
	           	   overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out);

	           		return true;
	           }
			
			
			
			
			if (mainFlipper != null && mainFlipper.getDisplayedChild()>=0) {

				int x = (int) (e2.getX() - e1.getX());
				int size = mainFlipper.getChildCount();
				int index = mainFlipper.getDisplayedChild();
				
				Log.i("log","size = "+size+"  index = "+index+ " x="+x);
				
				if (x > 0) {
					
					if(index==(size-1)){
						
						
							if(User.isExsitLastMoveData(currShowMoveDataForday,user)){
								MoveData movedata =User.getLastMoveData(currShowMoveDataForday,moveDataService,user);
								
								showMoveDataForday.addElement(movedata);
								
								LinearLayout moveDataLinearLayout = getShowSpecDayLinearLayout(movedata);

								mainFlipper.addView(moveDataLinearLayout);
								
								Log.i("log",movedata.day+ " size = "+mainFlipper.getChildCount()+"  index = "+mainFlipper.getDisplayedChild()+ " x="+x);
								
								mainFlipper.setInAnimation(Tongji2Activity.this, R.anim.push_right_in);
								mainFlipper.setOutAnimation(Tongji2Activity.this, R.anim.push_right_out);
								mainFlipper.showNext();
								
							}else{
								
								ToastUtil.toast(R.string.is_lastest_data);
							}
							
						
						
						
						
					}else{
						mainFlipper.setInAnimation(Tongji2Activity.this, R.anim.push_right_in);
						mainFlipper.setOutAnimation(Tongji2Activity.this, R.anim.push_right_out);
						mainFlipper.showNext();
						
						//currShowMoveData = showMoveData.elementAt(location)
					}
					
					
					
				//	mainFlipper.getChild

				} else {
					
					if(index==0){
			
					}else{
						mainFlipper.setInAnimation(Tongji2Activity.this, R.anim.push_left_in);
						mainFlipper.setOutAnimation(Tongji2Activity.this, R.anim.push_left_out);
						mainFlipper.showPrevious();
					}
					
					
					
					
				
				}

				
					currShowMoveDataForday = showMoveDataForday.elementAt(mainFlipper.getDisplayedChild());
					setTitle(currShowMoveDataForday);
				

			}

			//findAdNeedFlipper = false;

			return true;

		}

		@Override
		public void onShowPress(MotionEvent e) {

			Log.i("sino", "onShowPress");

		}

		@Override
		public boolean onDown(MotionEvent e) {

			return false;
		}

		@Override
		public boolean onDoubleTap(MotionEvent e) {

			return false;
		}

		@Override
		public boolean onDoubleTapEvent(MotionEvent e) {
			return false;
		}

		/**
		 * 这个方法不同于onSingleTapUp，他是在GestureDetector确信用户在第一次触摸屏幕后，没有紧跟着第二次触摸屏幕，
		 * 也就是不是“双击”的时候触�?
		 * */
		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {

			return false;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		return mGestureDetector.onTouchEvent(event);

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		Log.i("sino", "onTouch:v.getId()=" + v.getId());

		

		return mGestureDetector.onTouchEvent(event);

	}

	public static void gotoActivity(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, Tongji2Activity.class);
		context.startActivity(intent);
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			MainActivity.gotoActivity(Tongji2Activity.this);
		}
		return false;
	}
	
	private void initReciver(){
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(VLBleService.ACTION_LONGSIITING_REMIND);	
		registerReceiver(RemindMessageReceiver, intentFilter);
		
		
	}
	BroadcastReceiver RemindMessageReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context arg0, Intent  intent) {
			String action = intent.getAction();
			if(action.equals(VLBleService.ACTION_LONGSIITING_REMIND)){				
				Log.e(TAG, "ACTION_LONGSIITING_REMIND + 设备已发送久坐提醒");
				MyNotification notification = new MyNotification(Tongji2Activity.this);
				if (null != notification) {
					notification.sendRemindNotification();
				}
			}
		}
		
	};
	
	private void setTitle(MoveData movedata){
		
	    int day = movedata.day;
		
	    int month = movedata.mounth;
	   
	    Date date = new Date(System.currentTimeMillis());
	    
	    int today_month = date.getMonth();
	    int today_date  = date.getDate();
		String title = (month+1)+getString(R.string.month)+day+getString(R.string.day);
		if (today_month == month && today_date == day) {
			title = getString(R.string.today);
		} else if (today_month == month && today_date == day + 1) {
			title = getString(R.string.yestoday);
		}
		((TextView)findViewById(R.id.id_textview_title_center)).setText(title);
	}
}
