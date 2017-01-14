package com.jibu.app.main;

import com.jibu.app.R;
import com.jibu.app.server.AntiLostPhoneService;
import com.szants.bracelet.bletask.BleCallBack;
import com.szants.hw.bleservice.util.Keeper;
import com.szants.sdk.AntsBeltSDK;
import com.szants.sdk.FindPhoneObserver;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class AntiLostActivity extends Activity implements OnClickListener, OnSeekBarChangeListener{
	
	private final String TAG = "AntiLostActivity";
	
	private final int SET_REMIND_RING_INTENT = 0;
	private final int SET_NO_ALERT_AREA_INTENT = 1;
	
	
	private boolean isOpenAntiLost = false;
	private boolean isOpenBeltLost = false;

	
	private SeekBar seekbar_distance;
	
	private ImageView anti_lost_switch, belt_remind_switch;
	private TextView  anti_lost_ring_filename;
	private TextView  textview_is_open_no_alarm_area;
	
	private AntiLostActivity activity;
	private final int MAX_PROGRESS = 100;
	private final int MIN_PROGRESS = 0;
	private AntiLostPhoneService bindService = null;
	boolean flag = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_anti_lost);
		findViewById(R.id.id_linearlayout_title_left).setOnClickListener(this);
		findViewById(R.id.id_linearlayout_title_right).setOnClickListener(this);
		findViewById(R.id.id_relativelayout_anti_lost).setOnClickListener(this);
		findViewById(R.id.id_rl_ring_set).setOnClickListener(this);
		findViewById(R.id.id_relativelayout_no_alert_area).setOnClickListener(this);
		findViewById(R.id.id_relativelayout_anti_lost_belt).setOnClickListener(this);
		
		anti_lost_switch = (ImageView) findViewById(R.id.id_imageview_anti_lost_switch);
		belt_remind_switch = (ImageView) findViewById(R.id.id_imageview_anti_lost_belt_switch);
		seekbar_distance = (SeekBar) findViewById(R.id.id_seekbar_rssi);
		anti_lost_ring_filename = (TextView)findViewById(R.id.id_textview_ring_filename);
		textview_is_open_no_alarm_area = (TextView) findViewById(R.id.id_textview_no_alert_switch);
		
		activity = this;
		initView();
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initView2();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	private void initView() {
		seekbar_distance.setMax(MAX_PROGRESS);
		isOpenAntiLost = ApplicationSharedPreferences.getHasOpenAntiLostRemind(this);
		isOpenBeltLost = ApplicationSharedPreferences.getHasOpenBeltRemind(this);
		int rssiValue  = ApplicationSharedPreferences.getRSSIValue(this);
		
		if (isOpenAntiLost) {
			anti_lost_switch.setBackgroundResource(R.drawable.open_remind);
		} else {
			anti_lost_switch.setBackgroundResource(R.drawable.close_remind);
		}
		
		if (isOpenBeltLost) {
			belt_remind_switch.setBackgroundResource(R.drawable.open_remind);
		} else {
			belt_remind_switch.setBackgroundResource(R.drawable.close_remind);
		}
		
		//设置铃声名
		String path = ApplicationSharedPreferences.getRingPath(this);
		if (!path.equals(ApplicationSharedPreferences.PREF_RING_PAHT_DEFAULT_PATH)) {
			setSavedRingFileName(path);
		} 

		
		Log.e(TAG, "RssiToProgress(rssiValue) = " + RssiToProgress(rssiValue) + " RSSI = " + rssiValue);
		seekbar_distance.setProgress(RssiToProgress(rssiValue));
		
		//松开设置信号强度提醒值
		seekbar_distance.setOnSeekBarChangeListener(this);
		

	}
	private void initView2() {
		//勿扰区域是否打开了
		if (ApplicationSharedPreferences.getIsOpenNoAlarmArea(AntiLostActivity.this)) {
			textview_is_open_no_alarm_area.setText(R.string.open);
		} else {
			textview_is_open_no_alarm_area.setText(R.string.close);
		}
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.id_linearlayout_title_left:
			this.finish();
			break;
		case R.id.id_linearlayout_title_right:
			break;
		case R.id.id_relativelayout_anti_lost:
			open_close_remind();
			break;
		case R.id.id_relativelayout_anti_lost_belt:
			open_close_belt_remind();
			break;
		case R.id.id_rl_ring_set:
			set_antilost_ring();
			break;
		case R.id.id_relativelayout_no_alert_area:
			set_antilost_no_alarm_area();
			break;
	
		}
	}
	
	/**
	 * 
	 */
	private void open_close_remind() {
		if (isOpenAntiLost) {
			anti_lost_switch.setBackgroundResource(R.drawable.close_remind);
			isOpenAntiLost = false;
			stopAntiLostService();
		} else {
			anti_lost_switch.setBackgroundResource(R.drawable.open_remind);
			isOpenAntiLost = true;
			startAntiLostService();
		}
		ApplicationSharedPreferences.setHasOpenAntiLostRemind(this, isOpenAntiLost);
		
	}
	
	private void open_close_belt_remind() {
		final boolean isOpenBeltRemind = ApplicationSharedPreferences.getHasOpenBeltRemind(this);
		AntsBeltSDK.getInstance().setKeptNonRemind(new BleCallBack() {
			
			@Override
			public void onStart(Object startObject) {
				
				
			}
			
			@Override
			public void onFinish(Object result) {
				if (isOpenBeltRemind) {
					belt_remind_switch.setBackgroundResource(R.drawable.close_remind);
				} else {
					belt_remind_switch.setBackgroundResource(R.drawable.open_remind);
				}
				ApplicationSharedPreferences.setHasOpenBeltRemind(AntiLostActivity.this, !isOpenBeltLost);
			}
			
			@Override
			public void onFailed(Object error) {
				ToastUtil.toast("设置皮带提醒失败");
			}
		}, 120, !isOpenBeltRemind);
		
//		if (isOpenBeltLost) {
//			belt_remind_switch.setBackgroundResource(R.drawable.close_remind);
//			isOpenBeltLost = false;
////			stopAntiLostService();
//		} else {
//			belt_remind_switch.setBackgroundResource(R.drawable.open_remind);
//			isOpenBeltLost = true;
////			startAntiLostService();
//		}
//		ApplicationSharedPreferences.setHasOpenAntiLostRemind(this, isOpenBeltLost);
		
	}
	private void set_antilost_ring() {
		Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
		startActivityForResult(intent, SET_REMIND_RING_INTENT);
	}
	
	/*设置不提醒区域*/
	private void set_antilost_no_alarm_area() {
		Intent intent = new Intent(AntiLostActivity.this, NoAlarmAreaActivity.class);
		startActivityForResult(intent, SET_NO_ALERT_AREA_INTENT);
	}
	private void startAntiLostService() {
		if (Keeper.getUserHasBindBand(this)) {
			Intent i = new Intent(this, AntiLostPhoneService.class);
			startService(i);
		} else {
			ToastUtil.toast(R.string.unbind_ebelt_yet);
		}
//		bindService(i, conn, Context.BIND_AUTO_CREATE);
	}
	private void stopAntiLostService() {
		if (Keeper.getUserHasBindBand(this)) {
			stopService(new Intent(this, AntiLostPhoneService.class));
		} else {
			ToastUtil.toast(R.string.unbind_ebelt_yet);
		}
//		if (flag) {
//			unbindService(conn);
//			flag = false;
//		}
	}
	/**
	 * progress 0 - 100
	 * RSSI  60 - 110
	 * */
	
	private int progressToRSSI(int progress) {
		int rssi = -1;
		if (progress < MIN_PROGRESS || progress > MAX_PROGRESS) return -1;
		if (progress <  50) {
			rssi =(int)(60.0 + 0.6 * progress);
		} else if (progress >= 50 && progress < 100) {
			rssi =(int)(90.0 + (progress - 50) / 3.0);
		} else {
			rssi =(int)(100.0 + (progress - 80) / 2.0);
		}
		return rssi;
	}
	/***
	 * 
	 * @param rssi
	 * @return progress
	 */
	private int RssiToProgress(int rssi) {
		if (rssi < 60) return 0;
		if (rssi > 110) return 110;
		if (rssi < 90) {
			return (int)((rssi - 60) / 0.6f);
		} else if (rssi < 100){
			return (rssi - 90) * 3 + 50;
		} else {
			return (rssi - 100) * 2 +  80;
		}
	}
	public static void gotoAntiLostActivity(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, AntiLostActivity.class);
		context.startActivity(intent);
	}
	
//    private ServiceConnection conn = new ServiceConnection() {
//        
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            // TODO Auto-generated method stub
//            
//        }
//        
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            // TODO Auto-generated method stub
//        	AntiLostBinder binder = (AntiLostBinder)service;
//            bindService = binder.getService();
//            flag = true;
//        }
//    };
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		int rssi = progressToRSSI(seekBar.getProgress());
		if (rssi != -1) {
			AntiLostPhoneService.getInstance().setRemindRSSI(-rssi);
			ApplicationSharedPreferences.setRSSIValue(activity, rssi);
		}
		Log.e(TAG, "progressToRSSI(seekBar.getProgress()) = " + rssi);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case SET_REMIND_RING_INTENT: {
			if (resultCode == Activity.RESULT_OK) {
				if (requestCode == SET_REMIND_RING_INTENT) {
					try {  
                     //得到我们选择的铃声  
						Uri pickedUri=data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
						String path = getRealFilePath(activity, pickedUri);
						ApplicationSharedPreferences.setRingPath(activity, path);
						Log.e(TAG, "RealFilePath = " + path);
						setSavedRingFileName(path);
						} catch (Exception e) {  
							e.printStackTrace();  
					}  
				}
				
			}
			break;
		}
	   case SET_NO_ALERT_AREA_INTENT: {
		  if (resultCode == Activity.RESULT_OK) {
			  
		  }
	   }
	 }
	}
	
	private void setSavedRingFileName(String path) {
        String[] parts = path.split("/");
        String str = parts[parts.length-1];
        if(str.length() >  20) {
        	str = str.substring(0, 20);
        	str += "...";
        }
        anti_lost_ring_filename.setText(str);
	}
	
	public String getRealFilePath( final Context context, final Uri uri ) {
	    if ( null == uri ) return null;
	    final String scheme = uri.getScheme();
	    String data = null;
	    if ( scheme == null )
	        data = uri.getPath();
	    else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
	        data = uri.getPath();
	    } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
	        Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaColumns.DATA }, null, null, null );
	        if ( null != cursor ) {
	            if ( cursor.moveToFirst() ) {
	                int index = cursor.getColumnIndex( MediaColumns.DATA );
	                if ( index > -1 ) {
	                    data = cursor.getString( index );
	                }
	            }
	            cursor.close();
	        }
	    }
	    return data;
	}
	
	
}
