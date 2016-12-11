package com.jibu.app.user;

import com.jibu.app.R;
import com.jibu.app.database.UserService;
import com.jibu.app.main.MainApplication;
import com.jibu.app.main.MyActivity;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
/**
 * 个人信息设置界面
 * created on 2015/8/24 by zhangwm
 * */
public class PersonalinforActivity extends Activity implements OnClickListener{
	
	
	public final static int ENTRY_MODE_INIT  = 0;
	public final static int ENTRY_MODE_SETTING  = 1;
	private int entryMode = ENTRY_MODE_INIT;
	MainApplication mainApplication;
	UserService userService;
	public static PersonalinforActivity activity = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.personalinfor);
		entryMode = getIntent().getExtras().getInt("mode");
		
		((MainApplication) this.getApplication()).addActivity(this);

		
		activity = this;
		mainApplication = (MainApplication) this.getApplication();
		
		userService = new UserService(this);
		
		
		initView();
	}
	private void initView(){
		
		findViewById(R.id.id_linearlayout_title_left).setOnClickListener(this);
		
		findViewById(R.id.id_imageview_sex_man).setOnClickListener(this);
		findViewById(R.id.id_imageview_sex_woman).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_linearlayout_title_left:
			finish();
			overridePendingTransition(R.anim.activity_back, R.anim.activity_finish);
			break;
		case R.id.id_imageview_sex_man:
			mainApplication.user.sex = 0;
			userService.updateUser(mainApplication.user);
			if(entryMode == ENTRY_MODE_INIT){
				HeightActivity.gotoActivity(this, HeightActivity.ENTRY_MODE_INIT);
			}else{
				finish();
			}
			break;
		case R.id.id_imageview_sex_woman:
			mainApplication.user.sex = 1;
			userService.updateUser(mainApplication.user);
			if(entryMode == ENTRY_MODE_INIT){
				BornActivity.gotoActivity(this, BornActivity.ENTRY_MODE_INIT);
			}else{
				finish();
			}
			break;	
		default:
			break;
		}
	}
	
	@Override
	protected void onDestroy() {

		super.onDestroy();
	
		activity = null;
		

	}
	
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.activity_back, R.anim.activity_finish);
	}
	
	public static void gotoActivity(Context context,int mode) {
		Intent intent = new Intent();
		intent.setClass(context, PersonalinforActivity.class);
		intent.putExtra("mode", mode);
		context.startActivity(intent);
	}
	
	@Override
	protected void onPause() {
		super.onStop();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
}
