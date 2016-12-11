package com.jibu.app.main;

/**
 * 久坐提醒设置activity
 */
import com.jibu.app.R;
import com.veclink.bracelet.bean.BleLongSittingRemindParam;
import com.veclink.bracelet.bletask.BleCallBack;
import com.veclink.bracelet.bletask.BleSettingRemindParamsTask;
import com.veclink.bracelet.bletask.BleTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class SetupLongSitRemindActivity extends WaitingActivity implements  OnClickListener{
	public static SetupLongSitRemindActivity activity = null;
	
	ImageView  imageView_no_move, imageView_ring_remind, imageView_shock_remind;
	
	TextView   textView_interval_time, textView_begin_time, textView_end_time;
	
	
	private boolean hasOpenRing = false, hasOpenShock = false, hasOpenRemind = false;
	
	public final static int REQUEST_CODE_INTERVAL_TIME = 0;
	public final static int REQUEST_CODE_BEGIN_TIME    = 1;
	public final static int REQUEST_CODE_END_TIME	   = 2;
	
	private long interval_time = 0, begin_time = 0, end_time = 0;
	
	//是否已保存设置
	private boolean isSaved = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_remind_setup);	
		
		((MainApplication) this.getApplication()).addActivity(this);
		
		activity = this;
		
		imageView_no_move 	  = (ImageView) findViewById(R.id.id_imageview_my_no_move_next);
		imageView_ring_remind = (ImageView) findViewById(R.id.id_imageview_ring_next);
		imageView_shock_remind= (ImageView) findViewById(R.id.id_imageview_my_shock_remind);
		
		textView_interval_time= (TextView) findViewById(R.id.id_textview_time_interval);
		textView_begin_time= (TextView) findViewById(R.id.id_textview_begin_time);
		textView_end_time= (TextView) findViewById(R.id.id_textview_end_time);
		
		findViewById(R.id.id_linearlayout_title_left).setOnClickListener(this);
		findViewById(R.id.id_relativelayout_my_no_move).setOnClickListener(this);
		findViewById(R.id.id_relativelayout_ring_remind).setOnClickListener(this);
		findViewById(R.id.id_relativelayout_shock_remind).setOnClickListener(this);
		findViewById(R.id.id_relativelayout_time_interval).setOnClickListener(this);
		findViewById(R.id.id_relativelayout_begin_time).setOnClickListener(this);
		findViewById(R.id.id_relativelayout_end_time).setOnClickListener(this);
		findViewById(R.id.id_linearlayout_title_right).setOnClickListener(this);
		
		initView();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initView() {
		hasOpenRing = ApplicationSharedPreferences.getHasOpenRing(this);
		hasOpenShock= ApplicationSharedPreferences.getHasOpenShock(this);
		hasOpenRemind = ApplicationSharedPreferences.getRemindStatus(this);
		
		interval_time = ApplicationSharedPreferences.getTimeInterval(this);
		begin_time = ApplicationSharedPreferences.getBeginTime(this);
		end_time = ApplicationSharedPreferences.getEndTime(this);
		
		
		if (hasOpenRemind) {
			imageView_no_move.setBackgroundResource(R.drawable.open_remind);
		} else {
			imageView_no_move.setBackgroundResource(R.drawable.close_remind);
		}
		
		if (hasOpenRing) {
			imageView_ring_remind.setBackgroundResource(R.drawable.open_remind);
		} else {
			imageView_ring_remind.setBackgroundResource(R.drawable.close_remind);
		}
		
		
		if (hasOpenShock) {
			imageView_shock_remind.setBackgroundResource(R.drawable.open_remind);
		} else {
			imageView_shock_remind.setBackgroundResource(R.drawable.close_remind);
		}
		
		//显示时间间隔
		textView_interval_time.setText(convertToTimeInterval(interval_time));
		textView_begin_time.setText(convertToDate(begin_time));
		textView_end_time.setText(convertToDate(end_time));
	}
	
	private String convertToDate(long time) {
		long hour = time/3600;
		long min  = time%3600/60;
		return hour + ":" + String.format("%02d", min);
	}
	
	private String convertToTimeInterval(long time) {
		String intervalTime = "";
		if (time/3600 > 0) {
			intervalTime += (time/3600 + getString(R.string.hour));
		}
		intervalTime += ((time % 3600) / 60 + getString(R.string.minutes));
		return intervalTime;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.id_linearlayout_title_left:
			if (!isSaved) {
				showExitDialog();
			} else {
				this.finish();
			}
			break;
		case R.id.id_relativelayout_my_no_move:
			remind();
			isSaved = false;
			break;
		case R.id.id_relativelayout_ring_remind:
			openRing();
			isSaved = false;
			break;
		case R.id.id_relativelayout_shock_remind:
			openShockRemind();
			isSaved = false;
			break;
		case R.id.id_relativelayout_time_interval:
			startActivity(REQUEST_CODE_INTERVAL_TIME);
			isSaved = false;
			break;
		case R.id.id_relativelayout_begin_time:
			startActivity(REQUEST_CODE_BEGIN_TIME);
			isSaved = false;
			break;
		case R.id.id_relativelayout_end_time:
			startActivity(REQUEST_CODE_END_TIME);
			isSaved = false;
			break;
		case R.id.id_linearlayout_title_right:
			saveSetupAndRemind();
			isSaved = true;
			break;
		}
	}
	
	private void remind() {
//		boolean statusIsOpen = ApplicationSharedPreferences.getRemindStatus(this);
		
		if(hasOpenRemind){
//			showWaitCanelable();
//			closeRemind();
			imageView_no_move.setBackgroundResource(R.drawable.close_remind);
			hasOpenRemind = false;
		}else{
//			showWaitCanelable();
//			openRemind();
			imageView_no_move.setBackgroundResource(R.drawable.open_remind);
			hasOpenRemind = true;
		}
	}
	private void openRing() {
//		boolean ringIsOpen = ApplicationSharedPreferences.getHasOpenRing(this);
		
		if (hasOpenRing) { //打开状态将其关闭
			imageView_ring_remind.setBackgroundResource(R.drawable.close_remind);
//			ApplicationSharedPreferences.setHasOpenRing(this, false);
			hasOpenRing = false;
		} else {
			imageView_ring_remind.setBackgroundResource(R.drawable.open_remind);
//			ApplicationSharedPreferences.setHasOpenRing(this, true);
			hasOpenRing = true;
		}
	}
	
	private void openShockRemind() {
//		boolean shockIsOpen = ApplicationSharedPreferences.getHasOpenShock(this);
		
		if (hasOpenShock) { //打开状态将其关闭
			imageView_shock_remind.setBackgroundResource(R.drawable.close_remind);
//			ApplicationSharedPreferences.setHasOpenShock(this, false);
			hasOpenShock = false;
		} else {
			imageView_shock_remind.setBackgroundResource(R.drawable.open_remind);
//			ApplicationSharedPreferences.setHasOpenShock(this, true);
			hasOpenShock = true;
		}
	}
	
	private void saveSetupAndRemind() 
	{
		ApplicationSharedPreferences.setTimeInterval(SetupLongSitRemindActivity.this, interval_time);
		ApplicationSharedPreferences.setBeginTime(SetupLongSitRemindActivity.this, begin_time);
		ApplicationSharedPreferences.setEndTime(SetupLongSitRemindActivity.this, end_time);
		ApplicationSharedPreferences.setHasOpenRing(this, hasOpenRing);
		ApplicationSharedPreferences.setHasOpenShock(this,hasOpenShock);
		
		if(hasOpenRemind){
			showWaitCanelable();
			openRemind();
		}else{
			showWaitCanelable();
			closeRemind();
		}
	}
	
	Handler openRemindHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BleCallBack.TASK_START:
				
				break;

			case BleCallBack.TASK_FINISH:
				waitClose();
				ApplicationSharedPreferences.setRemindStatus(SetupLongSitRemindActivity.this, true);
				imageView_no_move.setBackgroundResource(R.drawable.open_remind);
				ToastUtil.toast(R.string.setup_longsit_success);
				SetupLongSitRemindActivity.this.finish();
				break;

			case BleCallBack.TASK_FAILED:
				waitClose();
				imageView_no_move.setBackgroundResource(R.drawable.btn_toggle_normal);
				ToastUtil.toast(R.string.setup_longsit_open_fail);
				break;
			}
		}
		
	};
	
	Handler closeRemindHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BleCallBack.TASK_START:
				
				break;

			case BleCallBack.TASK_FINISH:
				//showMsgView.setText("set remind success,return result is "+msg.obj);
				waitClose();
				ApplicationSharedPreferences.setRemindStatus(SetupLongSitRemindActivity.this, false);
				imageView_no_move.setBackgroundResource(R.drawable.btn_toggle_normal);
				ToastUtil.toast(R.string.longsit_setup_close_success);
				SetupLongSitRemindActivity.this.finish();
				break;

			case BleCallBack.TASK_FAILED:
				waitClose();
				imageView_no_move.setBackgroundResource(R.drawable.btn_toggle_selected);
				ToastUtil.toast(R.string.longsit_setup_close_fail);
				break;
			}
		}
		
	};

	BleCallBack closeRemindCallBack = new BleCallBack(closeRemindHandler);
	
	BleCallBack openRemindCallBack = new BleCallBack(openRemindHandler);
	
	private void openRemind(){
		
	    interval_time = ApplicationSharedPreferences.getTimeInterval(this);
		begin_time = ApplicationSharedPreferences.getBeginTime(this);
		end_time = ApplicationSharedPreferences.getEndTime(this);
		
		int hour_interval = (int) interval_time/3600;
		int min_interval =  (int) interval_time%3600/60;
		
		int hour_begin = (int) begin_time/3600;
		int min_begin =  (int) begin_time%3600/60;
		
		int hour_end = (int) end_time/3600;
		int min_end =  (int) end_time%3600/60;
		
		Log.e("TAG", hour_interval +"\n" + min_interval +"\n"+ hour_begin+"\n" + min_begin+"\n" + hour_end +"\n" + min_end);
		BleTask task = null;
		
		BleLongSittingRemindParam bleLongSittingRemindParam = new BleLongSittingRemindParam(hour_interval, min_interval,
				hour_begin, min_begin, hour_end, min_end,BleLongSittingRemindParam.OPEN_REMIND);
		
		task = new BleSettingRemindParamsTask(this, openRemindCallBack, bleLongSittingRemindParam);
		
		if(task!=null){
			task.work();	
		}
	}
	
	private void closeRemind(){
		
		BleTask task = null;
		BleLongSittingRemindParam bleLongSittingRemindParam = new BleLongSittingRemindParam(1, 0,
				7, 0, 23, 59,BleLongSittingRemindParam.CLOSE_REMIND);
		task = new BleSettingRemindParamsTask(this, closeRemindCallBack, bleLongSittingRemindParam);
		
		if(task!=null){
			task.work();
		}
	}
	


	private  void startActivity(int requestCode) {
		Intent intent = new Intent();
		intent.putExtra("requestCode", requestCode);
		intent.setClass(SetupLongSitRemindActivity.this, TimePickerActivity.class);
		startActivityForResult(intent, requestCode);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			long hour = data.getLongExtra("hour", -1);
			long min  = data.getLongExtra("min",  -1);
			long value = hour * 3600 + min * 60;
			switch(requestCode) {
			case REQUEST_CODE_INTERVAL_TIME:
				if (hour >= 0 && min >= 0) {
//					ApplicationSharedPreferences.setTimeInterval(SetupLongSitRemindActivity.this, value);
					interval_time = value;
					textView_interval_time.setText(convertToTimeInterval(value));
				}
				break;
			case REQUEST_CODE_BEGIN_TIME:
				if (hour >= 0 && min >= 0) {
//					ApplicationSharedPreferences.setBeginTime(SetupLongSitRemindActivity.this, value);
					begin_time = value;
					textView_begin_time.setText(convertToDate(value));
				}
				break;
			case REQUEST_CODE_END_TIME:
				if (hour >= 0 && min >= 0) {
//					ApplicationSharedPreferences.setEndTime(SetupLongSitRemindActivity.this, value);
					if (value > begin_time) {
						end_time = value;
						textView_end_time.setText(convertToDate(value));
					} else {
						showAlertDialog(getString(R.string.end_time_not_earlier_than_start_time));
					}
				}
				break;
			}
		}
	}

	public static void gotoActivity(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, SetupLongSitRemindActivity.class);
		context.startActivity(intent);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!isSaved) {
				showExitDialog();
			} else {
				this.finish();
			}
		}
		return false;
	}
	
	private void showExitDialog()
	{
		new AlertDialog.Builder(SetupLongSitRemindActivity.this)
		.setTitle(R.string.confirm_back)
		.setMessage(R.string.back_will_lost_unsave_settings)
		.setNeutralButton(R.string.confirm, new DialogInterface.OnClickListener() {

			@Override

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
				SetupLongSitRemindActivity.this.finish();
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
	
	private void showAlertDialog(String msg)
	{
		new AlertDialog.Builder(SetupLongSitRemindActivity.this)
		.setTitle(R.string.app_name)
		.setMessage(msg)
		.setNeutralButton(R.string.confirm, new DialogInterface.OnClickListener() {

			@Override

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}

		}).show();
	}
}
