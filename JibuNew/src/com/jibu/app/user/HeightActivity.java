package com.jibu.app.user;


import java.text.DecimalFormat;

import com.jibu.app.R;
import com.jibu.app.database.UserService;
import com.jibu.app.main.MainApplication;
import com.jibu.app.view.ObservableVerScrollView;
import com.jibu.app.view.ObservableVerScrollView.OnScrollStopListner_ver;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
/**身高设置*/
public class HeightActivity extends Activity implements OnClickListener{
	
	private Context context;
	private TextView height_value;
	private Button next_btn = null;
	DecimalFormat    df   = new DecimalFormat("######0.00");
	
	
	
	public final static int ENTRY_MODE_INIT  = 0;
	public final static int ENTRY_MODE_SETTING  = 1;
	private int entryMode = ENTRY_MODE_INIT;
	MainApplication mainApplication;
	UserService userService;
	
	float height;
	
	public static HeightActivity activity = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.height);
		context = this;
		
		entryMode = getIntent().getExtras().getInt("mode");
		
		((MainApplication) this.getApplication()).addActivity(this);

		
		activity = this;
		mainApplication = (MainApplication) this.getApplication();
		
		userService = new UserService(this);
		
		
		initView();
	}
	private void initView(){
		
		if(mainApplication.user.sex==0){
			
			((ImageView)findViewById(R.id.userinfo_head_2)).setBackgroundResource(R.drawable.man);
		}else{
			((ImageView)findViewById(R.id.userinfo_head_2)).setBackgroundResource(R.drawable.woman);
		}
		
		
		
		
		next_btn = (Button) findViewById(R.id.next_btn);
		
		findViewById(R.id.id_linearlayout_title_left).setOnClickListener(this);
		next_btn.setOnClickListener(this);
		height_value = (TextView) findViewById(R.id.height_value);
		//height_value.setText(String.valueOf(mainApplication.user.height));
		final ObservableVerScrollView scrollView = (ObservableVerScrollView) findViewById(R.id.height_scrollview);
		
		float value = ((float)3.68-mainApplication.user.height)*350;
		scrollView.setStarScrallValue(Math.round(value));
		
		scrollView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.i("log","ontouch");
				if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_UP) {
					scrollView.startScrollerTask();
				}
				return false;
			}
		});
		scrollView.setOnScrollStopListner_Ver(new OnScrollStopListner_ver() {
			@Override
			public void onScrollChange(int index) {
				if (index == 0) {
					height_value.setText("3.68");
					height = (float)3.68;
				} else {
					int value = px2dip(context, index);
					height = (float) (Math.round((3.68-(value*1.00 / 350))*100)/100.0);
					
					height_value.setText(df.format(height) + "");
				}
			}
		});
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
			finish();
			overridePendingTransition(R.anim.activity_back, R.anim.activity_finish);
			break;
		case R.id.next_btn:
			
			mainApplication.user.height = height;
			userService.updateUser(mainApplication.user);
			if(entryMode == ENTRY_MODE_INIT){
				WeightActivity.gotoActivity(this, WeightActivity.ENTRY_MODE_INIT);
			}else{
				finish();
			}
			break;
		default:
			break;
		}
	}
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.activity_back, R.anim.activity_finish);
	}
	
	public static void gotoActivity(Context context,int mode) {
		Intent intent = new Intent();
		intent.setClass(context, HeightActivity.class);
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
