package com.jibu.app.main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import mybleservice.FindPhoneNotify;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gelitenight.waveview.sample.SlidingUpPanelLayout.PanelSlideListener;
import com.gelitenight.waveview.sample.SlidingUpPanelLayout.PanelState;
import com.handmark.pulltorefresh.extras.viewpager.PullToRefreshViewPager;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.jibu.app.R;
import com.jibu.app.database.MoveDataService;
import com.jibu.app.database.PersonalInfoService;
import com.jibu.app.database.UserService;
import com.jibu.app.entity.MoveData;
import com.jibu.app.entity.User;
import com.jibu.app.entity.UserPersonalInfo;
import com.jibu.app.fragment.SportDataFragment;
import com.jibu.app.server.AntiLostNotification;
import com.jibu.app.server.AntiLostPhoneService;
import com.jibu.app.server.AutoSyncService;
import com.jibu.app.view.RoundProgressBar;
import com.nineoldandroids.view.ViewHelper;
import com.szants.bracelet.bean.BeltDeviceInfo;
import com.szants.bracelet.bean.BleUserInfoBean;
import com.szants.bracelet.bletask.BleCallBack;
import com.szants.hw.bleservice.util.Keeper;
import com.szants.sdk.AntsBeltSDK;
import com.szants.sdk.DeviceStateObserver;
import com.szants.sdk.FindPhoneObserver;
import com.szants.sdk.SittingRemindObserver;
import com.szants.sdk.StepObserver;

public class MainActivity extends WaitingActivity implements OnClickListener, 
								 PanelSlideListener{
	public static MainActivity activity = null;
	public final String TAG = "MainActivity";
	private final int DEVICE_CONNECTED = 0x61;
	private final int DEVICE_DISCONNECTED = 0x62;
	private final int DEVICE_SERVERDISCOVER = 0x63;
	private static final int REQUEST_ENABLE_BT = 3;
	
    private boolean hasConnected = false;
	
	private AntsBeltSDK sdk;

	/**
	 * 设置自动更新时间间隔
	 */
	private final int autoSyncTime = 5;
	private final int autoSyncTimeGap = 2;
	
	MainApplication mainApplication;
	
	RoundProgressBar roundProgressBar;
	
	Vector<MoveData> showMoveData;
	
	MoveData currShowMoveData;
	
	public User user;
	
	boolean isShowSync = false;
	boolean isAutoSync = false;
	
	
	MoveDataService  moveDataService;
	UserService userService;
	PersonalInfoService personalInfoService;
	
	MoveData todayMoveData;
	
	LinearLayout mainLinearLayout;
	RelativeLayout connecting_layout;
	FrameLayout refresh_framelayout;
	
	LinearLayout linearLayoutNoMove;
	
	
	private BluetoothAdapter mBluetoothAdapter = null;
	
	private TextView titleTextView;
	
	ProgressBar refreshProcessBar;
	
	
	private DrawerLayout mDrawerLayout;
	
	
	//返回ViewPager 有效页面个数
	public int NUM_ITEMS = 9999;
	
	TodayStepAdapter mAdapter;
	
	ViewPager  mPager;
	private PullToRefreshViewPager mPullToRefreshViewPager;
	
	public PanelState panelState = PanelState.COLLAPSED;
	
	ArrayList<MainSlideupChangeListener> ListenerArray = new ArrayList<MainActivity.MainSlideupChangeListener>();
	
	Handler connectHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DEVICE_CONNECTED:
				break;
			case DEVICE_DISCONNECTED:
				break;	
			case DEVICE_SERVERDISCOVER:
				
				break;
			}
		}
	};
	
	
	public interface MainSlideupChangeListener {
		/**
		 * @param panelState
		 * 设置相邻的Fragments状态与当前的Fragment状态一致
		 * 
		 **/
		public void onCurrentViewChangeState(PanelState panelState);
		
		/**
		 * 
		 * @param newTop
		 * 设置相邻的Fragments随当前Fragment滑动
		 */
		public void onCurrentViewSlide(float slideOffset);
		
		/**
		 * 
		 * @param movedata
		 * 自动更新后改变当前FragmentView
		 */
		public void onShowDataChange();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		getWindow().setBackgroundDrawable(null);
		
		((MainApplication) this.getApplication()).addActivity(this);

		
		activity = this;
		mainApplication = (MainApplication) this.getApplication();
		mainApplication.addActivity(this);
		
		registerBeltObesever();
		initReciver();
		
		
		if (null == mainApplication || null == mainApplication.user) {
			WelcomeActivity.gotoActivity(this);
			this.finish();
		} else {
			user = mainApplication.user;
			moveDataService = new MoveDataService(this);
			userService = new UserService(this);
			personalInfoService = new PersonalInfoService(this);
			
			showMoveData = new Vector<MoveData>();
			long currTime = System.currentTimeMillis();
		
			Log.e(TAG, "" + (user == null));
			todayMoveData = moveDataService.queryMoveDataByUserSpecDay(user.userId,currTime);
			if(todayMoveData!=null){
				showMoveData.add(todayMoveData);
			}else{
				todayMoveData = new MoveData(user.userId, currTime);
				todayMoveData.todayTarget = user.step;
				moveDataService.insertMoveData(todayMoveData);
				showMoveData.add(todayMoveData);
			}
			
			try {
				NUM_ITEMS = moveDataService.qureyAllDaysByUser(user.userId);
				Log.e(TAG, "NUM_ITEMS = " + NUM_ITEMS);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			if(showMoveData!=null){
				currShowMoveData = showMoveData.firstElement();
				setTitle(0);
				initView();
				initDrawLayout();
			}
			
		
			Log.e(TAG, "onCreate run");
		
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
			Log.e(TAG, "showmoveData.size  = " + showMoveData.size()); 
			
			initViewPager();
		}

	}
	

	private void registerBeltObesever() {
        sdk = AntsBeltSDK.getInstance();
        
        sdk.registerDeviceStateObserver(new DeviceStateObserver() {
			
			@Override
			public void disConnected() {
//				ToastUtil.toast("断开了设备连接");
				Log.e(TAG, "断开了设备连接");
                if (hasConnected) {
                	FindPhoneNotify.getInstance().phoneIfNotify(MainActivity.this);
                }
            	hasConnected = false;
			}
			
			@Override
			public void connecting() {
				
			}
			
			@Override
			public void connected() {
//				ToastUtil.toast("已成功连接设备");
				Log.e(TAG, "已成功连接设备");
				hasConnected = true;
			}
			
			@Override
			public void blueToothClose() {
	               if (hasConnected) {
	                	FindPhoneNotify.getInstance().phoneIfNotify(MainActivity.this);
	                }
	            	hasConnected = false;
			}
		});
        
        sdk.registerStepObserver(new StepObserver() {
			
			@Override
			public void stepCountChange(int totalStep) {
//				total_step_tv.setText("总步数："+totalStep);
			}
		});
        
        sdk.registerSittingRemindObserver(new SittingRemindObserver() {
			
			@Override
			public void onReceiveSittingRemind() {
				Log.e(TAG, "ACTION_LONGSIITING_REMIND + 设备已发送久坐提醒");
				MyNotification notification = new MyNotification(MainActivity.this);
				if (null != notification) {
					notification.sendRemindNotification();
				}
			}
		});
        
        sdk.registerFindPhoneObserver(new FindPhoneObserver() {
			
			@Override
			public void onReceiveFindPhoneCmd(boolean arg0) {
				// TODO Auto-generated method stub
	        	if (!arg0) { 
	            	FindPhoneNotify.getInstance().stopPhoneNotify(MainActivity.this);
	        	} else {
	            	FindPhoneNotify.getInstance().phoneNotify(MainActivity.this);
	        	}
			}
		});
        
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
		Log.e(TAG, "onResume run");
		if (activity == null){
			activity = this;
		}
		
		if (!sdk.isConnected()) {
			sdk.reConnectDevice();
		}
		
		//更新同步时间
		if (!AutoSyncService.isAutoSync || !Keeper.getUserHasBindBand(this)) {
			setUpdateTime();
			AutoSyncService.isAutoSync = false;
		} else {
			changeSycnState(AutoSyncService.State, -1);
		}
		
		//自动同步
		if (null != user && !isShowSync && mBluetoothAdapter.isEnabled()) { 
			long currentTime = System.currentTimeMillis(); ;
			if (currentTime - user.updateTime > autoSyncTime * 60 * 1000 
					&& currentTime - user.autoUpdateTime > autoSyncTimeGap * 60 * 1000) { //执行同步参数命令，同步参数命令成功后将同步数据
				user.autoUpdateTime = currentTime;
				Intent i = new Intent(MainActivity.this, AutoSyncService.class);
				this.startService(i);
			}
		}
		
		new insertTodayMoveDataTask().execute();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(getdeviceMessageReceiver);
		activity = null;
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.id_linearlayout_title_left:
			openLeftDrawer();
			break;
		case R.id.id_linearlayout_title_right:
			TongjiActivity.gotoActivity(MainActivity.this);
			break;
		}
	}
	
	private void initView() {
		findViewById(R.id.id_linearlayout_title_left).setOnClickListener(this);
		findViewById(R.id.id_linearlayout_title_right).setOnClickListener(this);
		titleTextView =  (TextView) findViewById(R.id.id_textview_title_update_time);
		refreshProcessBar = (ProgressBar) findViewById(R.id.id_processbar_refreshing);
		setUpdateTime();
	}
	
	private void initViewPager() {
		//Fragment
		
		
		mPullToRefreshViewPager = (PullToRefreshViewPager) findViewById(R.id.id_pull_refresh_viewpager);
		mPullToRefreshViewPager.setOnRefreshListener(new OnRefreshListener<ViewPager>() {

			@Override
			public void onRefresh(PullToRefreshBase<ViewPager> arg0) {
						mPullToRefreshViewPager.onRefreshComplete();
						Intent i = new Intent(MainActivity.this, AutoSyncService.class);
						startService(i);
			}
		});
		
		mAdapter = new TodayStepAdapter(getSupportFragmentManager());
	        
		
		mPager = mPullToRefreshViewPager.getRefreshableView();
	        
	    mPager.setAdapter(mAdapter);
	    	    		
	    mPager.setCurrentItem(NUM_ITEMS - 1);
	    
	    mPager.setOffscreenPageLimit(2);
	    
	    mPager.setOnPageChangeListener(mAdapter);
	    
	}
	
	/**
	 * 根据与当前时间的间隔天数显示今天，昨天或具体日期
	 * */
	
	public void setTitle(int lastDays) {
		Calendar cl = Calendar.getInstance();
		cl.setTimeInMillis(System.currentTimeMillis());
		cl.set(cl.get(Calendar.YEAR), cl.get(Calendar.MONTH), cl.get(Calendar.DATE) -  lastDays);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getString(R.string.mm_month_dd_day));
		String title = simpleDateFormat.format(cl.getTime());
		if (lastDays == 0) {
			title = getString(R.string.today);
		} else if (lastDays == 1) {
			title = getString(R.string.yestoday);
		}
		((TextView)findViewById(R.id.id_textview_title_center)).setText(title);
	}
	
	/**
	 * 显示 上次同步时间 
	 * 
	 */
	private void setUpdateTime()
	{
		//更新updateTime
		if (!isShowSync && !AutoSyncService.isAutoSync) {
			try {
				((TextView)findViewById(R.id.id_textview_title_update_time))
				.setText(getUpdateTimeString(user.updateTime));
				refreshProcessBar.setVisibility(View.INVISIBLE);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private String getUpdateTimeString(long updateTime)
	{
		long currentTime = System.currentTimeMillis();
		long timeInterval = currentTime - updateTime;
		Date currentDate = new Date(currentTime);
		Date updateDate = new Date(updateTime);

		if (timeInterval < 60 * 1000) { //小于一分钟 
			return  getString(R.string.sync_over_just);
		} else if (timeInterval < 3600 * 1000) { //小于一小时
			return  getString(R.string.pre_sync_at) + 
					timeInterval/1000/60 + getString(R.string.several_minutes_ago);
		} else if (timeInterval < 3600 * 1000 * 24) {//  小于一天
			return   getString(R.string.pre_sync_at) + 
					timeInterval/1000/3600 + getString(R.string.several_hours_ago);
		} else if (currentDate.getMonth() == updateDate.getMonth() 
				&& currentDate.getDate() == updateDate.getDate()) { //昨天更新
			SimpleDateFormat    formatter    =   new    SimpleDateFormat    (getString(R.string.at_yesterday_hh_mm_sync));		
			return formatter.format(updateTime);
		}
		
		SimpleDateFormat    formatter    =   new    SimpleDateFormat    (getString(R.string.at_month_day_hh_mm_sync));  	
		return formatter.format(updateTime);
	}
	
	private void initReciver(){
		IntentFilter intentFilter = new IntentFilter();
//		intentFilter.addAction(VLBleService.ACTION_GATT_SERVICES_DISCOVERED);
//		intentFilter.addAction(VLBleService.ACTION_GATT_CONNECTED);
//		intentFilter.addAction(VLBleService.ACTION_GATT_DISCONNECTED);	
//		intentFilter.addAction(VLBleService.ACTION_SHORT_SPORT_DATA);
//		intentFilter.addAction(VLBleService.ACTION_POWER_CHANGE_DATA);	
//		intentFilter.addAction(VLBleService.ACTION_LONGSIITING_REMIND);	
		
		intentFilter.addAction(StepSettingActivity.ACTION_STEP_CHANGE);
		intentFilter.addAction(AutoSyncService.ACTION_STATE);
		intentFilter.addAction(Intent.ACTION_DATE_CHANGED);
		intentFilter.addAction(MyActivity.ACTION_SYNC_ALL_DATA);
		
		registerReceiver(getdeviceMessageReceiver, intentFilter);
		
	}
	BroadcastReceiver getdeviceMessageReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context arg0, Intent intent) {
			String action = intent.getAction();
//			if(action.equals(VLBleService.ACTION_GATT_SERVICES_DISCOVERED)){
//				Log.e(TAG, "ACTION_GATT_SERVICES_DISCOVERED + 可以执行同步数据广播");
//				connectHandler.postDelayed(new Runnable() {					
//					@Override
//					public void run() {
//						VLBleServiceManager.setAutoReConnect(true);
//						if (ApplicationSharedPreferences.getHasOpenAntiLostRemind(getApplicationContext())) {
//							MainActivity.this.startService(new Intent(MainActivity.this, AntiLostPhoneService.class));
//						}
//					}
//				}, 2000);
//			}else if(action.equals(VLBleService.ACTION_GATT_DISCONNECTED)){
//				Log.e(TAG, "ACTION_GATT_DISCONNECTED + 设备已断开连接广播");
//			}else if(action.equals(VLBleService.ACTION_GATT_CONNECTED)){				
//				Log.e(TAG, "ACTION_GATT_CONNECTED + 设备已连接广播");
//			}else if(action.equals(VLBleService.ACTION_SHORT_SPORT_DATA)){
//				int sportSteps = intent.getIntExtra(VLBleService.EXTRA_DATA, 0);	
//			}else if(action.equals(VLBleService.ACTION_POWER_CHANGE_DATA)){
//				int powerValue = intent.getIntExtra(VLBleService.EXTRA_DATA, 0);	
//				ApplicationSharedPreferences.setDcValue(MainActivity.this, powerValue);
//			}else if(action.equals(VLBleService.ACTION_LONGSIITING_REMIND)){				
//				Log.e(TAG, "ACTION_LONGSIITING_REMIND + 设备已发送久坐提醒");
//				MyNotification notification = new MyNotification(MainActivity.this);
//				if (null != notification) {
//					notification.sendRemindNotification();
//				}
//			}else 
			
			if(action.equals(AutoSyncService.ACTION_STATE)) {
				Log.e(TAG, "设备收到来自自动同步参数的广播");
				int state = intent.getIntExtra(AutoSyncService.ACTION_STATE, -1);
				int progress = intent.getIntExtra(AutoSyncService.PROGRESS, -1);
				changeSycnState(state, progress);
				
			}else if(action.equals(StepSettingActivity.ACTION_STEP_CHANGE)){
				stepTargetChange();
				Log.e(TAG, "设备收到步数设置改变的广播");
			}else if(action.equals(Intent.ACTION_DATE_CHANGED)) {
				Log.e(TAG, "系统监听到日期变化");
				new insertTodayMoveDataTask().execute();
			}else if(action.equals(MyActivity.ACTION_SYNC_ALL_DATA)) {
				adapterPageSizeChanged();
			}
			
		}
		
	};
	
	/**
	 * 
	 * @param value
	 * @param progress
	 * 自动同步时的状态栏更新
	 */
	private void changeSycnState(int value, int progress) {
		switch(value) {
		case AutoSyncService.CHECKING_CONNECT:
			titleTextView.setText(R.string.check_connecting);
			refreshProcessBar.setVisibility(View.VISIBLE);
			break;
		case AutoSyncService.CONNECTING:
			titleTextView.setText(R.string.refreshing);
			refreshProcessBar.setVisibility(View.VISIBLE);
			break;
		case AutoSyncService.CONNECTED_FAILD:
			titleTextView.setText(R.string.refresh_fail);
			connectHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					setUpdateTime();
				}
			}, 1000);
			break;
		case AutoSyncService.SYNC_FAILD:
			titleTextView.setText(R.string.sync_fail);
			connectHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					setUpdateTime();
				}
			}, 1000);
			break;
		case AutoSyncService.SYNC_STEP:
			if (-1 == progress) progress = 0; 
			titleTextView.setText(getString(R.string.syncing) + progress + "%");
			refreshProcessBar.setVisibility(View.VISIBLE);
			break;
		case AutoSyncService.CONNECT_SUCCEED:
			titleTextView.setText(R.string.refresh_succeed);
			refreshProcessBar.setVisibility(View.VISIBLE);
			break;
		case AutoSyncService.SYNC_COMPLETE:
			autoSyncCompleted();
			break;
		default:
			Log.e(TAG, "changeSycnState error");
			setUpdateTime();
			break;
		}
	}
	
	private void autoSyncCompleted() {
		setUpdateTime();
//		mAdapter.notifyDataSetChanged();
		for(MainSlideupChangeListener listener : ListenerArray) {

			int position = ((SportDataFragment)listener).getPosition(); 
//			MoveData moveData = 
//					User.getLastServalDayMoveData(todayMoveData, moveDataService, user, NUM_ITEMS - position - 1);
			listener.onShowDataChange();
		}
	}
	
	private void stepTargetChange() {
		for(MainSlideupChangeListener listener : ListenerArray) {

			int position = ((SportDataFragment)listener).getPosition(); 
			if (position == NUM_ITEMS - 1) {
				listener.onShowDataChange();
			}
		}
	}
	
	private void initDrawLayout(){
		mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerlayout);
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.LEFT);
		mDrawerLayout.setDrawerListener(new DrawerListener() {
			
			@Override
			public void onDrawerStateChanged(int arg0) {
				
			}
			
			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				
                View mContent = mDrawerLayout.getChildAt(0);  
                View mMenu = drawerView;  
                float scale = 1 - slideOffset;  
                float rightScale = 0.8f + scale * 0.2f;  
  
                if (drawerView.getTag().equals("LEFT"))  
                {  
  
                    float leftScale = 1 - 0.3f * scale;  
  
                    ViewHelper.setScaleX(mMenu, leftScale);  
                    ViewHelper.setScaleY(mMenu, leftScale);  
                    ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));  
                    ViewHelper.setTranslationX(mContent,  
                            mMenu.getMeasuredWidth() * (1 - scale));  
                    ViewHelper.setPivotX(mContent, 0);  
                    ViewHelper.setPivotY(mContent,  
                            mContent.getMeasuredHeight() / 2);  
                    mContent.invalidate();  
                    ViewHelper.setScaleX(mContent, rightScale);  
                    ViewHelper.setScaleY(mContent, rightScale);  
                    
                } 
			}
			
			@Override
			public void onDrawerOpened(View arg0) {
				
			}
			
			@Override
			public void onDrawerClosed(View arg0) {
				mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,Gravity.LEFT);
			}
		});
	}

	public class TodayStepAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener{

		
		public TodayStepAdapter(FragmentManager fm) {
			super(fm);
		}

		/***
		 */
		@Override
		public Fragment getItem(int position) {
			SportDataFragment sportDataFragment = SportDataFragment.
						newInstance(currShowMoveData, position, panelState);
			return sportDataFragment;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
//			super.destroyItem(container, position, object);
		}


		@Override
		public int getCount() {
			return NUM_ITEMS;
		}
		
		boolean dontLoadList;
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
			if (arg0 ==  ViewPager.SCROLL_STATE_IDLE) {
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						if (!dontLoadList) {
							SportDataFragment sp=(SportDataFragment) instantiateItem(mPager, mPager.getCurrentItem());
//							Log.e("getPosition", ""+sp.getPosition());
							sp.loadData();
						}
					}
				}, 200);
			}
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			if ( positionOffset == 0 && positionOffsetPixels ==0){
				dontLoadList = false;
			} else {
				dontLoadList = true;
			}
		}

		@Override
		public void onPageSelected(int position) {
			
			if(showMoveData != null && NUM_ITEMS - position - 1 >= 0) {
//				setTitle(showMoveData.get(NUM_ITEMS - position - 1));
				setTitle(NUM_ITEMS - position - 1);
				setUpdateTime();
			}
		}
		
		public PanelState getPanelState() {
			return panelState;
		}
		
	}
	
	public static void gotoActivity(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, MainActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onPanelSlide(View panel, float slideOffset, int position) {
		if (null == mPager || position != mPager.getCurrentItem()) {
			return ;
		}
		for(MainSlideupChangeListener listener : ListenerArray) {
			if (((SportDataFragment)listener).getPosition() 
					!= mPager.getCurrentItem()) {
				listener.onCurrentViewSlide(slideOffset);
			}
		}
		this.panelState = PanelState.DRAGGING;
	}
	

	@Override
	public void onPanelCollapsed(View panel) {
		for(MainSlideupChangeListener listener : ListenerArray) {
			if (((SportDataFragment)listener).getPosition() 
					!= mPager.getCurrentItem()) {
					listener.onCurrentViewChangeState(PanelState.COLLAPSED);
			}
		}
		this.panelState = PanelState.COLLAPSED;
	}


	@Override
	public void onPanelExpanded(View panel) {
		for(MainSlideupChangeListener listener : ListenerArray) {
			if (((SportDataFragment)listener).getPosition() 
					!= mPager.getCurrentItem()) {
					listener.onCurrentViewChangeState(PanelState.EXPANDED);
			}
		}
		this.panelState = PanelState.EXPANDED;
	}


	@Override
	public void onPanelAnchored(View panel) {
		Log.e(TAG, "panel.getTop = "+ panel.getTop());
	}


	@Override
	public void onPanelHidden(View panel) {
		
	}
	

	public void setFragmentListener(MainSlideupChangeListener listener) {
		ListenerArray.add(listener);
	}
	
	public void removeFragmentListener(MainSlideupChangeListener listener) {
		ListenerArray.remove(listener);
	}
	
	private void openLeftDrawer() {
		mDrawerLayout.openDrawer(Gravity.LEFT);
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,Gravity.LEFT);
		mDrawerLayout.setScrimColor(Color.TRANSPARENT);
//		testData();
	}


	@Override
	public void onTouch(MotionEvent ev, int position) {
		
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.e(TAG, "onBackPressed");
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			moveTaskToBack(true);
		}
		return false;
	}
	
	public MoveData getPositionMoveData(int position) {
		return User.getLastServalDayMoveData(todayMoveData, moveDataService, user, NUM_ITEMS - position - 1);
	}
	

	
	private void insertTodayMoveData() {
		try {
			long currTime = System.currentTimeMillis();
			todayMoveData = moveDataService.queryMoveDataByUserSpecDay(user.userId,currTime);
			if(todayMoveData == null){
				todayMoveData = new MoveData(user.userId, currTime);
				todayMoveData.todayTarget = user.step;
				moveDataService.insertMoveData(todayMoveData);
			}
			NUM_ITEMS = moveDataService.qureyAllMoveDataSizeByUser(user.userId);
			if (mAdapter != null) {
				mAdapter.notifyDataSetChanged();
			}
		} catch (Exception e) {
			
		}
	}
	
	
//	//Params[0] User Params[1] PersonalInfoService
//	private class updateUserPersonalInfoTask extends AsyncTask<Void, Void, Void> {
//
//		@Override
//		protected Void doInBackground(Void... params) {
//			try {
//				 float longestSitTime = 0;
//				 int longestSportStep = 0;
//				 int sportDistanceTotal = 0;
//				 float sitTimeTotal = 0;
//				 int dabiaotianshu = 0;
//				 MoveData longestSitDayData = null;
//				 MoveData longestSportDayData = null;
//				UserPersonalInfo upi = personalInfoService.queryPersonalInfoByUser(user.userId);
//				long CurrentUpdateTime = user.updateTime;
//				Vector<MoveData> moveDatas = moveDataService.queryMoveDataByBeginEndDay(user.userId, upi.lastDate , CurrentUpdateTime);
//				/*计算最长久坐日和最长运动日*/
//				for(int i = 0; i < moveDatas.size(); i++){
//					MoveData moveData = moveDatas.elementAt(i);
//					if (moveData.todayTarget <= 0) {
//						moveData.todayTarget = user.step;
//						moveDataService.insertMoveData(moveData);
//					}
//					float noMoveTime = moveData.getNoMoveTime();
//					int moveStep = moveData.getStep();
//					if(longestSitTime <= noMoveTime) {
//						longestSitTime = noMoveTime;
//						longestSitDayData = moveData;
//					}
//					if(longestSportStep <= moveStep) {
//						longestSportStep = moveStep;
//						longestSportDayData = moveData;
//					}
//					
//					/*总路程计算*/
//					sportDistanceTotal += moveStep;
//					/*久坐总时长计算*/
//					sitTimeTotal += noMoveTime;
//					/*达标天数计算*/
//					if (moveStep < moveData.todayTarget) {
//						dabiaotianshu++;
//					}
//				}
//				
//				/*总路程计算要减去当天的*/
//				sportDistanceTotal -= moveDatas.elementAt(moveDatas.size() - 1).getStep();
//				sitTimeTotal -= moveDatas.elementAt(moveDatas.size() - 1).getNoMoveTime();
//				
//				/*更新最长久坐日*/
//				if (longestSitDayData != null) {
//					Calendar cl = Calendar.getInstance();
//					cl.set(longestSitDayData.year, longestSitDayData.mounth, longestSitDayData.day);
//					if (upi.longestSitDayTime <= longestSitTime) {
//						upi.longestSitDayTime = longestSitTime;
//						upi.longestSitDay = cl.getTimeInMillis();
//					}
//				} 
//				
//				/*更新最长运动日*/
//				if (longestSportDayData != null) {
//					Calendar cl = Calendar.getInstance();
//					cl.set(longestSportDayData.year, longestSportDayData.mounth, longestSportDayData.day);
//					if (upi.longestSportDayStep <= longestSportStep) {
//						upi.longestSportDayStep = longestSportStep;
//						upi.longestSportDay = cl.getTimeInMillis();
//					}
//				} 
//				
//				upi.sitTimeTotal += sitTimeTotal;
//				upi.sportDistanceTotal += sportDistanceTotal;
//				upi.dabiaotianshu += dabiaotianshu;
//				upi.lastDate = CurrentUpdateTime;
//				
//				personalInfoService.UpdateUserPersonalInfo(upi);
//				
//			} catch (Exception e){
//				e.printStackTrace();
//			}
//			return null;
//		}
//	}
	
	private class insertTodayMoveDataTask extends AsyncTask<Void, Void, Boolean> {

		
		/**
		 * @return true 需要更新数据
		 * */
		
		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				long currTime = System.currentTimeMillis();
				todayMoveData = moveDataService.queryMoveDataByUserSpecDay(user.userId,currTime);
				if(todayMoveData == null){
					todayMoveData = new MoveData(user.userId, currTime);
					todayMoveData.todayTarget = user.step;
					moveDataService.insertMoveData(todayMoveData);
					return true;
				}

			} catch (Exception e) {
				return false;
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				NUM_ITEMS = moveDataService.qureyAllMoveDataSizeByUser(user.userId);
				if (mAdapter != null && mPager != null) {
					mAdapter.notifyDataSetChanged();
					mAdapter.onPageSelected(mPager.getCurrentItem());
				}
			}
		}
		
	}
	
	private void adapterPageSizeChanged() {
		NUM_ITEMS = moveDataService.qureyAllMoveDataSizeByUser(user.userId);
		if (mAdapter != null && mPager != null) {
			mAdapter.notifyDataSetChanged();
			mAdapter.onPageSelected(mPager.getCurrentItem());
		}
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
		sdk.syncParams(bean, new BleCallBack() {
			
			@Override
			public void onStart(Object startObject) {
			    Log.e(TAG, "开始自动同步参数");	
			}
			
			@Override
			public void onFinish(Object result) {
				BeltDeviceInfo deviceInfo = (BeltDeviceInfo)result;
			    Log.e(TAG, "同步参数成功");	

			}
			
			@Override
			public void onFailed(Object error) {
				Log.e(TAG, "同步参数失败");

			}
		});
	}
}
