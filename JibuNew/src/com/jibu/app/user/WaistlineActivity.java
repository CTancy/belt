package com.jibu.app.user;


import com.jibu.app.R;
import com.jibu.app.database.UserService;
import com.jibu.app.main.MainActivity;
import com.jibu.app.main.MainApplication;
import com.jibu.app.main.ScanActivity;
import com.jibu.app.view.ObservableHorizontalScrollView;
import com.jibu.app.view.ObservableHorizontalScrollView.OnScrollStopListner;
import com.szants.hw.bleservice.util.Keeper;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**鑵板洿璁剧疆*/
public class WaistlineActivity extends Activity implements OnClickListener {
	
	private Context context;
	private TextView waistline_value;
	private Button next_btn = null;

	public final static int ENTRY_MODE_INIT  = 0;
	public final static int ENTRY_MODE_SETTING  = 1;
	private int entryMode = ENTRY_MODE_INIT;
	MainApplication mainApplication;
	UserService userService;
	
	float waist;
	
	public static WaistlineActivity activity = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.waistline);
		context = this;
		
		entryMode = getIntent().getExtras().getInt("mode");
		
		((MainApplication) this.getApplication()).addActivity(this);

		
		activity = this;
		mainApplication = (MainApplication) this.getApplication();
		
		userService = new UserService(this);
		
		initView();
	}

	private void initView() {

		if(mainApplication.user.sex==0){
			
			((ImageView)findViewById(R.id.userinfo_head_2)).setBackgroundResource(R.drawable.man);
		}else{
			((ImageView)findViewById(R.id.userinfo_head_2)).setBackgroundResource(R.drawable.woman);
		}
		next_btn = (Button) findViewById(R.id.next_btn);

		findViewById(R.id.id_linearlayout_title_left).setOnClickListener(this);
		next_btn.setOnClickListener(this);
		waistline_value = (TextView) findViewById(R.id.waistline_value);
		final ObservableHorizontalScrollView scrollView = (ObservableHorizontalScrollView) findViewById(R.id.waistline_scrollview);
		
		int value = (int)(mainApplication.user.waist-10)*35;
		scrollView.setStarScrallValue(Math.round(value));
		
		scrollView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_UP) {
					scrollView.startScrollerTask();
				}
				return false;
			}
		});
		scrollView.setOnScrollStopListner(new OnScrollStopListner() {
			public void onScrollChange(int index) {
				if (index == 0) {
					waistline_value.setText("10");
					waist = 10;
				} else {
					int value = px2dip(context, index);
					//杩欓噷鐢变簬璺ㄥ害澶э紝鍋氬洓鑸嶄簲鍏ワ紝涓嶇劧闇� 婊戝姩鍒�鍦嗙偣涓棿鍚庯紝鎵�1锛屼綋楠岃緝宸�
					double newvalue = (double) (Math.round((value / (7*5) + 10)*10)/10.0);
					waistline_value.setText((value / (7*5) + 10) + "");
					
					waist = (value / (7*5) + 10);
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_linearlayout_title_left:
			finish();
			overridePendingTransition(R.anim.activity_back,
					R.anim.activity_finish);
			break;
		case R.id.next_btn:
			
			mainApplication.user.waist = waist;
			userService.updateUser(mainApplication.user);
			if(entryMode == ENTRY_MODE_INIT){
				if(Keeper.getUserHasBindBand(this)){
//					VLBleServiceManager.getInstance().bindService(getApplication());
					MainActivity.gotoActivity(this);
				}else{
					ScanActivity.gotoActivity(this);
					
					//MainActivity.gotoActivity(this);
					
				}
				
				if(BornActivity.activity!=null){
					BornActivity.activity.finish();
				}
				if(HeightActivity.activity!=null){
					HeightActivity.activity.finish();
				}
				if(PersonalinforActivity.activity!=null){
					PersonalinforActivity.activity.finish();
				}
				if(WeightActivity.activity!=null){
					WeightActivity.activity.finish();
				}
				if(WaistlineActivity.activity!=null){
					WaistlineActivity.activity.finish();
				}
				
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
		intent.setClass(context, WaistlineActivity.class);
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
