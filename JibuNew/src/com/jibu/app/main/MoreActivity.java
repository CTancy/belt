package com.jibu.app.main;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.jibu.app.R;
import com.jibu.app.entity.User;
import com.veclink.hw.bleservice.util.Keeper;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

public class MoreActivity extends WaitingActivity implements OnClickListener{

	public static MoreActivity activity = null;


	MainApplication mainApplication;
	
	User user;

	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_more);
		((MainApplication) this.getApplication()).addActivity(this);

		
		activity = this;
		mainApplication = (MainApplication) this.getApplication();
		if (null == mainApplication.user) {
			WelcomeActivity.gotoActivity(this);
			this.finish();
		} else {
		user = mainApplication.user;
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
		showUpdateTime();
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
		case R.id.id_imageview_yaodai:
			if(Keeper.getUserHasBindBand(MoreActivity.this)){
				YaodaiActivity.gotoActivity(this);
			}else{
				ScanActivity.gotoActivity(this);
				finish();
				if(MainActivity.activity!=null){
					MainActivity.activity.finish();
				}
			}
			
				break;
		case R.id.id_imageview_my:
			MyActivity.gotoActivity(this);
				break;
		case R.id.id_imageview_share:
			//TongjiActivity.gotoActivity(this);
//			ToastUtil.toast("即将开放");
			ShareActivity.gotoActivity(this);
			break;
		case R.id.id_imageview_more_anti_lost:
			AntiLostActivity.gotoAntiLostActivity(this);
			break;
		case R.id.id_imageview_more_long_sit:
			SetupLongSitRemindActivity.gotoActivity(this);
			break;
		}
		
	}
	
	
	
	private void initView(){
		
		
		findViewById(R.id.id_linearlayout_title_left).setOnClickListener(this);
		findViewById(R.id.id_imageview_yaodai).setOnClickListener(this);
		findViewById(R.id.id_imageview_my).setOnClickListener(this);
		findViewById(R.id.id_imageview_share).setOnClickListener(this);
		findViewById(R.id.id_imageview_more_anti_lost).setOnClickListener(this);
		findViewById(R.id.id_imageview_more_long_sit).setOnClickListener(this);
		
		if(Keeper.getUserHasBindBand(activity)){
			
			((TextView)findViewById(R.id.id_textview_yaodai)).setText(R.string.my_ebelt);
			
		}else{
			((TextView)findViewById(R.id.id_textview_yaodai)).setText(R.string.search_ebelt);
		}
		
		
		
	}

	
	private void showUpdateTime(){
		SimpleDateFormat    formatter1    =   new    SimpleDateFormat    (getString(R.string.mm_month_dd_day));     
		SimpleDateFormat    formatter2    =   new    SimpleDateFormat    (getString(R.string.hh_mm_update));   
		if (user != null) {
			Date    curDate    =   new    Date(user.updateTime);//获取当前时间     
			((TextView)findViewById(R.id.id_textview_title_center)).setText(formatter1.format(curDate));
			((TextView)findViewById(R.id.id_textview_update_time)).setText(formatter2.format(curDate));
		}

	}
	
	
	public static void gotoActivity(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, MoreActivity.class);
		context.startActivity(intent);
	}
	

}
