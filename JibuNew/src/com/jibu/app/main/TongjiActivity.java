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
import com.jibu.app.entity.User;
import com.jibu.app.view.HistogramView;
import com.jibu.app.view.RoundProgressBar;
import com.umeng.analytics.MobclickAgent;


import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class TongjiActivity extends WaitingActivity implements OnClickListener, OnTouchListener{

	public static TongjiActivity activity = null;


	MainApplication mainApplication;
	User user;
	TextView textViewTitleDay,textViewTitleMounth,textViewTitleWeek;
	
	TextView textViewBiaoDu1,textViewBiaoDu2,textViewBiaoDu3,textViewBiaoDu4,textViewBiaoDu5,textViewBiaoDu6,textViewBiaoDu7;
	
	LinearLayout linearlayoutBiaoDu1,linearlayoutBiaoDu2,linearlayoutBiaoDu3,linearlayoutBiaoDu4,linearlayoutBiaoDu5,linearlayoutBiaoDu6,linearlayoutBiaoDu7;
	
	
	TextView textViewDay;
	
	TextView textViewNoMoveTime,textViewNoMoveIndex,textViewMoveTime,textViewMoveStep,textViewMoveLength,textViewMoveCal;
	
	HistogramView histogramView;
	
	private ViewFlipper mainFlipper = null;
	private GestureDetector mGestureDetector;
	private MoveDataService moveDataService;
	
	private static final int SHOW_TAB_DAY = 1;
	private static final int SHOW_TAB_WEEK = 2;
	private static final int SHOW_TAB_MONTH = 3;
	private int showTab = SHOW_TAB_DAY;
	
	Vector<MoveData> showMoveDataForday;
	MoveData currShowMoveDataForday;
	
	Vector<Vector<MoveData>> showMoveDataForMonth;
	Vector<MoveData> currShowMoveDataForMonth;
	
	Vector<Vector<MoveData>> showMoveDataForWeek;
	Vector<MoveData> currShowMoveDataForWeek;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tongji);
		((MainApplication) this.getApplication()).addActivity(this);

		
		activity = this;
		mainApplication = (MainApplication) this.getApplication();
		if (null == mainApplication.user) {
			WelcomeActivity.gotoActivity(this);
			this.finish();
		} else {
		user = mainApplication.user;
		moveDataService = new MoveDataService(this);
		showTab = SHOW_TAB_DAY;
		
		
		initShowMoveDataForDay();
		initShowMoveDataForMonth();
		initShowMoveDataForWeek();
		initView();
		
		
		setFlipperView();
		}
	//	showday(null);
	}
	
	
	private void initShowMoveDataForDay(){
		
		long currTime = System.currentTimeMillis();
		showMoveDataForday = new Vector<MoveData>();
		currShowMoveDataForday = moveDataService.queryMoveDataByUserSpecDay(user.userId,currTime);
		
		if(currShowMoveDataForday!=null){
			showMoveDataForday.add(currShowMoveDataForday);
		}else{
			currShowMoveDataForday = new MoveData(user.userId, currTime);
			moveDataService.insertMoveData(currShowMoveDataForday);
			showMoveDataForday.add(currShowMoveDataForday);
		}
	}
	
	private void initShowMoveDataForMonth(){
		
		long currTime = System.currentTimeMillis();
		showMoveDataForMonth = new Vector<Vector<MoveData>>();
		currShowMoveDataForMonth = moveDataService.queryMoveDataByUserSpecMonth(user.userId,currTime);
		
		if(currShowMoveDataForMonth!=null){
			
			
			
			showMoveDataForMonth.add(currShowMoveDataForMonth);
		}
	}

	private void initShowMoveDataForWeek(){
		
		long currTime = System.currentTimeMillis();
		showMoveDataForWeek = new Vector<Vector<MoveData>>();
		currShowMoveDataForWeek = moveDataService.queryMoveDataByUserSpecWeek(user.userId,currTime);
		
		if(currShowMoveDataForWeek!=null){
			
			
			
			showMoveDataForWeek.add(currShowMoveDataForWeek);
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
	public void onClick(View v) {
		switch (v.getId()) {
			
		case R.id.id_linearlayout_title_left:
				this.finish();
				break;
		case R.id.id_textview_title_tongji_day:
			//showday(null);
			showTab=SHOW_TAB_DAY;
			setFlipperView();
			break;
		case R.id.id_textview_title_tongji_week:
			showTab=SHOW_TAB_WEEK;
			setFlipperView();
		break;
		case R.id.id_textview_title_tongji_month:
			showTab=SHOW_TAB_MONTH;
			setFlipperView();
			break;
		}
		
	}
	
	
	private void setFlipperView(){
		mainFlipper = (ViewFlipper) findViewById(R.id.id_viewflipper_main);
		mainFlipper.setOnTouchListener(this);
		mGestureDetector = new GestureDetector(this,new DefaultGestureListener());
		
		mainFlipper.removeAllViews();
		
		
		if(showTab == SHOW_TAB_DAY){
			
			
			if (showMoveDataForday != null && showMoveDataForday.size() > 0) {

				for (int i = 0; i < showMoveDataForday.size(); i++) {

					LinearLayout moveDataLinearLayout = getShowSpecDayLinearLayout(showMoveDataForday.elementAt(i));

					mainFlipper.addView(moveDataLinearLayout);
				}

				mainFlipper.setOnTouchListener(this);
			}
			
		}else if(showTab == SHOW_TAB_MONTH){
			
			for (int i = 0; i < showMoveDataForMonth.size(); i++) {

				LinearLayout moveDataLinearLayout = getShowSpecMonthLinearLayout(showMoveDataForMonth.elementAt(i));

				mainFlipper.addView(moveDataLinearLayout);
			}

			mainFlipper.setOnTouchListener(this);
			
		}else if(showTab == SHOW_TAB_WEEK){
			
			for (int i = 0; i < showMoveDataForWeek.size(); i++) {

				LinearLayout moveDataLinearLayout = getShowSpecWeekLinearLayout(showMoveDataForWeek.elementAt(i));

				mainFlipper.addView(moveDataLinearLayout);
			}

			mainFlipper.setOnTouchListener(this);
			
		}
		
	}
	
	private void initView(){
		
		
		
		textViewTitleDay = (TextView)findViewById(R.id.id_textview_title_tongji_day);
		textViewTitleWeek = (TextView)findViewById(R.id.id_textview_title_tongji_week);
		textViewTitleMounth = (TextView)findViewById(R.id.id_textview_title_tongji_month);
		
		
		
		textViewTitleDay.setOnClickListener(this);
		textViewTitleWeek.setOnClickListener(this);
		textViewTitleMounth.setOnClickListener(this);
		findViewById(R.id.id_linearlayout_title_left).setOnClickListener(this);
	}
	
	
	private void initWigit(View view){
		
		textViewBiaoDu1 = (TextView)view.findViewById(R.id.id_textview_biaodu_1);
		textViewBiaoDu2 = (TextView)view.findViewById(R.id.id_textview_biaodu_2);
		textViewBiaoDu3 = (TextView)view.findViewById(R.id.id_textview_biaodu_3);
		textViewBiaoDu4 = (TextView)view.findViewById(R.id.id_textview_biaodu_4);
		textViewBiaoDu5 = (TextView)view.findViewById(R.id.id_textview_biaodu_5);
		textViewBiaoDu6 = (TextView)view.findViewById(R.id.id_textview_biaodu_6);
		textViewBiaoDu7 = (TextView)view.findViewById(R.id.id_textview_biaodu_7);
		
		linearlayoutBiaoDu1 = (LinearLayout)view.findViewById(R.id.id_linearlayout_biaodu_1);
		linearlayoutBiaoDu2 = (LinearLayout)view.findViewById(R.id.id_linearlayout_biaodu_2);
		linearlayoutBiaoDu3 = (LinearLayout)view.findViewById(R.id.id_linearlayout_biaodu_3);
		linearlayoutBiaoDu4 = (LinearLayout)view.findViewById(R.id.id_linearlayout_biaodu_4);
		linearlayoutBiaoDu5 = (LinearLayout)view.findViewById(R.id.id_linearlayout_biaodu_5);
		linearlayoutBiaoDu6 = (LinearLayout)view.findViewById(R.id.id_linearlayout_biaodu_6);
		linearlayoutBiaoDu7 = (LinearLayout)view.findViewById(R.id.id_linearlayout_biaodu_7);
		
		textViewDay = (TextView)view.findViewById(R.id.id_textview_tongji_time);
		
		
		textViewNoMoveTime = (TextView)view.findViewById(R.id.id_textview_tongji_no_move);
		textViewNoMoveIndex = (TextView)view.findViewById(R.id.id_textview_tongji_no_move_index);
		textViewMoveTime = (TextView)view.findViewById(R.id.id_textview_tongji_move);
		textViewMoveStep = (TextView)view.findViewById(R.id.id_textview_tongji_step);
		textViewMoveLength = (TextView)view.findViewById(R.id.id_textview_tongji_length);
		textViewMoveCal = (TextView)view.findViewById(R.id.id_textview_tongji_cal);
		
		histogramView = (HistogramView)view.findViewById(R.id.id_histogramview);
	}
	
	private LinearLayout getShowSpecDayLinearLayout(MoveData moveData){
		
		
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.linearlayout_tongji, null);

		LinearLayout linearlayout = (LinearLayout) view
				.findViewById(R.id.id_linearlayout);
		
		initWigit(view);
		
	//	moveData.year = Calendar.getInstance().get(Calendar.YEAR);
	//	moveData.mounth = Calendar.getInstance().get(Calendar.MONTH);
	//	moveData.day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	//	moveData.week = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		
		
		textViewTitleDay.setBackgroundResource(R.drawable.tongji_bg);
		textViewTitleMounth.setBackgroundColor(Color.TRANSPARENT);
		textViewTitleWeek.setBackgroundColor(Color.TRANSPARENT);
		
		if (moveData.getWeek() != -1) {
			textViewDay.setText((moveData.mounth+1)+getString(R.string.month)+moveData.day+getString(R.string.day)+" "+getString(moveData.getWeek()));
		}
		linearlayoutBiaoDu1.setVisibility(View.VISIBLE);
		textViewBiaoDu1.setText("02:00");
		linearlayoutBiaoDu2.setVisibility(View.VISIBLE);
		textViewBiaoDu2.setText("07:00");
		linearlayoutBiaoDu3.setVisibility(View.VISIBLE);
		textViewBiaoDu3.setText("12:00");
		linearlayoutBiaoDu4.setVisibility(View.VISIBLE);
		textViewBiaoDu4.setText("17:00");
		linearlayoutBiaoDu5.setVisibility(View.VISIBLE);
		textViewBiaoDu5.setText("22:00");
		linearlayoutBiaoDu6.setVisibility(View.GONE);
		linearlayoutBiaoDu7.setVisibility(View.GONE);
		
		textViewNoMoveTime.setText(String.valueOf(moveData.getNoMoveTime()*60));
		textViewNoMoveIndex.setText(String.valueOf(moveData.getNoMoveIndex()));
		textViewMoveTime.setText(String.valueOf(moveData.getMoveTime()*60));
		textViewMoveStep.setText(String.valueOf(moveData.getStep()));
		textViewMoveLength.setText(String.valueOf(moveData.getMoveLength()));
		textViewMoveCal.setText(String.valueOf(moveData.getMoveCal()));
		
		Vector<Integer> progress = moveData.getProgress();
		histogramView.setProgress(progress);
		
		return linearlayout;
		
	}
	
	private LinearLayout getShowSpecMonthLinearLayout(Vector<MoveData> moveDatas){
		
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.linearlayout_tongji, null);

		LinearLayout linearlayout = (LinearLayout) view
				.findViewById(R.id.id_linearlayout);
		
		initWigit(view);
		
		MoveData firstMoveData = moveDatas.firstElement();
		MoveData endMoveData = moveDatas.elementAt(moveDatas.size()-1);
		
		textViewTitleDay.setBackgroundColor(Color.TRANSPARENT);
		textViewTitleMounth.setBackgroundResource(R.drawable.tongji_bg);
		textViewTitleWeek.setBackgroundColor(Color.TRANSPARENT);
		
		//int month = firstMoveData.mounth+1;
		
		textViewDay.setText((firstMoveData.mounth+1)+getString(R.string.month)+firstMoveData.day+getString(R.string.day)+" - "+(endMoveData.mounth+1)+getString(R.string.month)+endMoveData.day+getString(R.string.day)+" ");
		
		
		
		int beishu = moveDatas.size()/6;
		int yushu = moveDatas.size()%6;

		linearlayoutBiaoDu7.setVisibility(View.GONE);
		
		Vector<LinearLayout> linearlayoutBiaoDus = new Vector<LinearLayout>();
		linearlayoutBiaoDus.add(linearlayoutBiaoDu1);
		linearlayoutBiaoDus.add(linearlayoutBiaoDu2);
		linearlayoutBiaoDus.add(linearlayoutBiaoDu3);
		linearlayoutBiaoDus.add(linearlayoutBiaoDu4);
		linearlayoutBiaoDus.add(linearlayoutBiaoDu5);
		linearlayoutBiaoDus.add(linearlayoutBiaoDu6);
		
		Vector<TextView> textViewBiaoDus = new Vector<TextView>();
		textViewBiaoDus.add(textViewBiaoDu1);
		textViewBiaoDus.add(textViewBiaoDu2);
		textViewBiaoDus.add(textViewBiaoDu3);
		textViewBiaoDus.add(textViewBiaoDu4);
		textViewBiaoDus.add(textViewBiaoDu5);
		textViewBiaoDus.add(textViewBiaoDu6);
		
		if(beishu==0){
			
			for(int i=0; i<6; i++){
				linearlayoutBiaoDus.elementAt(i).setVisibility(View.GONE);
			}
			
			for(int i=0; i<yushu; i++){
				linearlayoutBiaoDus.elementAt(i).setVisibility(View.VISIBLE);
				textViewBiaoDus.elementAt(i).setText((moveDatas.elementAt(i).mounth+1)+getString(R.string.month)+moveDatas.elementAt(i).day+getString(R.string.day));
			}
			
		}else if(beishu>0){
			
			for(int i=0; i<6; i++){
				linearlayoutBiaoDus.elementAt(i).setVisibility(View.VISIBLE);
				
				if(i<yushu){
					textViewBiaoDus.elementAt(i).setText((moveDatas.elementAt(i*beishu).mounth+1)+getString(R.string.month)+moveDatas.elementAt(i*beishu).day+getString(R.string.day));
				}else{
					textViewBiaoDus.elementAt(i).setText((moveDatas.elementAt(i*beishu).mounth+1)+getString(R.string.month)+moveDatas.elementAt(i*beishu).day+getString(R.string.day));
				}
				
			}
			
			
		}

		
		
		
		
		textViewNoMoveTime.setText(String.valueOf(MoveData.getTotalNoMoveTime(moveDatas)*60));
		textViewNoMoveIndex.setText(String.valueOf(MoveData.getTotalNoMoveIndex(moveDatas)));
		textViewMoveTime.setText(String.valueOf(MoveData.getTotalMoveTime(moveDatas)*60));
		textViewMoveStep.setText(String.valueOf(MoveData.getTotalStep(moveDatas)));
		textViewMoveLength.setText(String.valueOf(MoveData.getTotalMoveLength(moveDatas)));
		textViewMoveCal.setText(String.valueOf(MoveData.getTotalMoveCal(moveDatas)));
		
		histogramView.setProgress(MoveData.getTotalProgress(moveDatas));
		
		return linearlayout;
		
	}
	
	private LinearLayout getShowSpecWeekLinearLayout(Vector<MoveData> moveDatas){
	
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.linearlayout_tongji, null);

		LinearLayout linearlayout = (LinearLayout) view
				.findViewById(R.id.id_linearlayout);
		
		initWigit(view);
		
		MoveData firstMoveData = moveDatas.firstElement();
		MoveData endMoveData = moveDatas.elementAt(moveDatas.size()-1);
		
		textViewTitleDay.setBackgroundColor(Color.TRANSPARENT);
		textViewTitleMounth.setBackgroundColor(Color.TRANSPARENT);
		textViewTitleWeek.setBackgroundResource(R.drawable.tongji_bg);
		
		textViewDay.setText((firstMoveData.mounth+1)+getString(R.string.month)+firstMoveData.day+getString(R.string.day)+" - "
		+(endMoveData.mounth+1)+getString(R.string.month)+endMoveData.day+getString(R.string.day)+"  ");
		
		Vector<LinearLayout> linearlayoutBiaoDus = new Vector<LinearLayout>();
		linearlayoutBiaoDus.add(linearlayoutBiaoDu1);
		linearlayoutBiaoDus.add(linearlayoutBiaoDu2);
		linearlayoutBiaoDus.add(linearlayoutBiaoDu3);
		linearlayoutBiaoDus.add(linearlayoutBiaoDu4);
		linearlayoutBiaoDus.add(linearlayoutBiaoDu5);
		linearlayoutBiaoDus.add(linearlayoutBiaoDu6);
		linearlayoutBiaoDus.add(linearlayoutBiaoDu7);
		
		Vector<TextView> textViewBiaoDus = new Vector<TextView>();
		textViewBiaoDus.add(textViewBiaoDu1);
		textViewBiaoDus.add(textViewBiaoDu2);
		textViewBiaoDus.add(textViewBiaoDu3);
		textViewBiaoDus.add(textViewBiaoDu4);
		textViewBiaoDus.add(textViewBiaoDu5);
		textViewBiaoDus.add(textViewBiaoDu6);
		textViewBiaoDus.add(textViewBiaoDu7);
		
		for(int i=0; i<7; i++){
			linearlayoutBiaoDus.elementAt(i).setVisibility(View.GONE);
		}
		
		
			
		for(int i=0;i<moveDatas.size(); i++){
				
			linearlayoutBiaoDus.elementAt(i).setVisibility(View.VISIBLE);
			if (moveDatas.elementAt(i).getWeekDesc() != -1) {
				textViewBiaoDus.elementAt(i).setText(getString(moveDatas.elementAt(i).getWeekDesc()));	
			}
		}
		
		
		
		textViewNoMoveTime.setText(String.valueOf(MoveData.getTotalNoMoveTime(moveDatas)*60));
		textViewNoMoveIndex.setText(String.valueOf(MoveData.getTotalNoMoveIndex(moveDatas)));
		textViewMoveTime.setText(String.valueOf(MoveData.getTotalMoveTime(moveDatas)*60));
		textViewMoveStep.setText(String.valueOf(MoveData.getTotalStep(moveDatas)));
		textViewMoveLength.setText(String.valueOf(MoveData.getTotalMoveLength(moveDatas)));
		textViewMoveCal.setText(String.valueOf(MoveData.getTotalMoveCal(moveDatas)));
		
		histogramView.setProgress(MoveData.getTotalProgress(moveDatas));
		
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
			
			
			
			
			
			
			if (mainFlipper != null && mainFlipper.getDisplayedChild()>=0) {

				int x = (int) (e2.getX() - e1.getX());
				int size = mainFlipper.getChildCount();
				int index = mainFlipper.getDisplayedChild();
				
				Log.i("log","size = "+size+"  index = "+index+ " x="+x);
				
				if (x > 0) {
					
					if(index==(size-1)){
						
						if(showTab==SHOW_TAB_DAY){
							if(User.isExsitLastMoveData(currShowMoveDataForday,user)){
								MoveData movedata =User.getLastMoveData(currShowMoveDataForday,moveDataService,user);
								
								showMoveDataForday.addElement(movedata);
								
								LinearLayout moveDataLinearLayout = getShowSpecDayLinearLayout(movedata);

								mainFlipper.addView(moveDataLinearLayout);
								
								Log.i("log",movedata.day+ " size = "+mainFlipper.getChildCount()+"  index = "+mainFlipper.getDisplayedChild()+ " x="+x);
								
								mainFlipper.setInAnimation(activity, R.anim.push_right_in);
								mainFlipper.setOutAnimation(activity, R.anim.push_right_out);
								mainFlipper.showNext();
								
							}else{
								
								ToastUtil.toast(R.string.is_lastest_data);
							}
							
						}else if(showTab==SHOW_TAB_MONTH){
							
							if(User.isExsitLastMonthMoveData(currShowMoveDataForMonth,user)){
								
								Vector<MoveData> lastMovedatas =User.getLastMonthMoveData(currShowMoveDataForMonth,moveDataService,user);
								
								showMoveDataForMonth.addElement(lastMovedatas);
								
								LinearLayout moveDataLinearLayout = getShowSpecMonthLinearLayout(lastMovedatas);

								mainFlipper.addView(moveDataLinearLayout);
								
								
								mainFlipper.setInAnimation(activity, R.anim.push_right_in);
								mainFlipper.setOutAnimation(activity, R.anim.push_right_out);
								mainFlipper.showNext();
							}else{
								
								ToastUtil.toast(R.string.is_lastest_data);
							}
							
						}else if(showTab==SHOW_TAB_WEEK){
							
							if(User.isExsitLastWeekMoveData(currShowMoveDataForWeek,user)){
								
								Vector<MoveData> lastMovedatas =User.getLastWeekMoveData(currShowMoveDataForWeek,moveDataService,user);
								
								showMoveDataForWeek.addElement(lastMovedatas);
								
								LinearLayout moveDataLinearLayout = getShowSpecWeekLinearLayout(lastMovedatas);

								mainFlipper.addView(moveDataLinearLayout);
								
								
								mainFlipper.setInAnimation(activity, R.anim.push_right_in);
								mainFlipper.setOutAnimation(activity, R.anim.push_right_out);
								mainFlipper.showNext();
							}else{
								
								ToastUtil.toast(R.string.is_lastest_data);
							}
							
						}
						
						
						
					}else{
						mainFlipper.setInAnimation(activity, R.anim.push_right_in);
						mainFlipper.setOutAnimation(activity, R.anim.push_right_out);
						mainFlipper.showNext();
						
						//currShowMoveData = showMoveData.elementAt(location)
					}
					
					
					
				//	mainFlipper.getChild

				} else {
					
					if(index==0){
						
					}else{
						mainFlipper.setInAnimation(activity, R.anim.push_left_in);
						mainFlipper.setOutAnimation(activity, R.anim.push_left_out);
						mainFlipper.showPrevious();
					}
					
					
					
					
				
				}

				if(showTab==SHOW_TAB_DAY){
					currShowMoveDataForday = showMoveDataForday.elementAt(mainFlipper.getDisplayedChild());
					
				}else if(showTab==SHOW_TAB_MONTH){
					currShowMoveDataForMonth = showMoveDataForMonth.elementAt(mainFlipper.getDisplayedChild());
				}else if(showTab==SHOW_TAB_WEEK){
					currShowMoveDataForWeek = showMoveDataForWeek.elementAt(mainFlipper.getDisplayedChild());
				}
				
				
				Log.i("log","size = "+mainFlipper.getChildCount()+"  index = "+mainFlipper.getDisplayedChild()+ " aaa");

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
		intent.setClass(context, TongjiActivity.class);
		context.startActivity(intent);
	}
	
}
