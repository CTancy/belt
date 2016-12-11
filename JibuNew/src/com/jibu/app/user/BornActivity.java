package com.jibu.app.user;

import com.jibu.app.R;
import com.jibu.app.database.UserService;
import com.jibu.app.main.MainApplication;
import com.jibu.app.main.TongjiActivity;
import com.jibu.app.view.ObservableHorizontalScrollView;
import com.jibu.app.view.ObservableHorizontalScrollView.OnScrollStopListner;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/** 鍑虹敓 */
public class BornActivity extends Activity implements OnClickListener {
	private ImageView back = null;
	private Context context;
	private TextView born_value;
	private Button next_btn = null;
	
	public final static int ENTRY_MODE_INIT  = 0;
	public final static int ENTRY_MODE_SETTING  = 1;
	private int entryMode = ENTRY_MODE_INIT;
	MainApplication mainApplication;
	UserService userService;
	
	int year;
	
	public static BornActivity activity = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.born);
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
		born_value = (TextView) findViewById(R.id.born_value);
		final ObservableHorizontalScrollView scrollView = (ObservableHorizontalScrollView) findViewById(R.id.born_scrollview);
		
		int value = (mainApplication.user.year-1840)*7;
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
					born_value.setText("1840");
					year = 1840;
				} else {//褰撳墠鍒诲害 1涓埢搴�瀵瑰簲 index 涓�
					int value = px2dip(context, index);
					born_value.setText((value / 7 + 1840) + "");
					year = (value / 7 + 1840);
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
			overridePendingTransition(R.anim.activity_back,
					R.anim.activity_finish);
			break;
		case R.id.next_btn:
			mainApplication.user.year = year;
			userService.updateUser(mainApplication.user);
			if(entryMode == ENTRY_MODE_INIT){
				HeightActivity.gotoActivity(this, HeightActivity.ENTRY_MODE_INIT);
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
	
	public static void gotoActivity(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, BornActivity.class);
		context.startActivity(intent);
	}
	
	public static void gotoActivity(Context context,int mode) {
		Intent intent = new Intent();
		intent.setClass(context, BornActivity.class);
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
