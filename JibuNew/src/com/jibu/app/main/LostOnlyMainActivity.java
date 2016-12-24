package com.jibu.app.main;

import java.util.Set;

import mybleservice.BluetoothLeService;
import mybleservice.E3AKeeper;
import mybleservice.FindPhoneNotify;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.jibu.app.R;
import com.jibu.app.server.AntiLostNotification;
import com.jibu.app.view.RoundProgressBar3;

public class LostOnlyMainActivity  extends Activity implements OnClickListener {
    private final static String TAG = LostOnlyMainActivity.class.getSimpleName();

    private boolean hasConnected = false;
    private TextView mCallTextView;
	private static final int REQUEST_ENABLE_BT = 3;

	private BluetoothAdapter mBluetoothAdapter = null;
	RoundProgressBar3 progress;
    Animation mAnimation = null;

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
            	
                ToastUtil.toast("已成功连接设备");
                hasConnected = true;
                mCallTextView.setText("呼叫");
                
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
            	
                mCallTextView.setText("连接");
                if (hasConnected) {
                	FindPhoneNotify.getInstance().phoneIfNotify(LostOnlyMainActivity.this);
                }
            	hasConnected = false;
            	
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
            	
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) { 
            	String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
            	if (data != null) {
		        	if (data.contains("B1 24")) { //单击
//		            	ToastUtil.toast("单击了设备");
		            	FindPhoneNotify.getInstance().stopPhoneNotify(LostOnlyMainActivity.this);
		        	} else if (data.contains("FD E8")) {
//		            	ToastUtil.toast("双击了设备");
		            	FindPhoneNotify.getInstance().phoneNotify(LostOnlyMainActivity.this);
		        	}
            	}
            }
        }
    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_lost_only_main);
		findViewById(R.id.id_linearlayout_title_left).setOnClickListener(this);
		findViewById(R.id.id_textview_call).setOnClickListener(this);
		findViewById(R.id.id_textview_setup).setOnClickListener(this);
		findViewById(R.id.id_textview_my_belt).setOnClickListener(this);
		mCallTextView =  (TextView) findViewById(R.id.id_textview_call);
		progress = (RoundProgressBar3) findViewById(R.id.id_roundProgressBar3);
		mAnimation = AnimationUtils.loadAnimation(this, R.anim.shandong);
		progress.setAlpha((float) 0.0);
		
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
	    }
	}

	@Override
	protected void onResume() {
		super.onResume();
        if (!E3AKeeper.getInstance().hasContectedDevice()) { //绑定设备
        	E3AKeeper.getInstance().binderDevice(getApplication());
        	mCallTextView.setText("连接");
        } else {
        	mCallTextView.setText("呼叫");
        }
	}
	@Override
	protected void onPause() {
	    super.onPause();
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mGattUpdateReceiver);
    }



    boolean isCall = true;
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.id_linearlayout_title_left:
			this.finish();
			break;
		case R.id.id_textview_call:
			if (E3AKeeper.getInstance().hasContectedDevice()) {
				callDevice();
			}else {
	        	E3AKeeper.getInstance().unBinderDevice(getApplication());
	        	E3AKeeper.getInstance().binderDevice(getApplication());
			}
			break;
		case R.id.id_textview_my_belt:
			YaodaiE3AActivity.gotoActivity(LostOnlyMainActivity.this);
			break;
		case R.id.id_textview_setup:
			LostSetupActivity.gotoAntiLostActivity(LostOnlyMainActivity.this);
			break;
		}
	}


    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
    
    public void callDevice() {
    	if (isCall) {// 呼叫
    		E3AKeeper.getInstance().callDevice();
    		progress.setAlpha((float) 1.0);
    		progress.startAnimation(mAnimation);
			mCallTextView.setText("停止");
		} else { //停止
    		E3AKeeper.getInstance().stopDevice();
			progress.setAlpha((float) 0.0);
    		progress.clearAnimation();
			mCallTextView.setText("呼叫");
		}
    	isCall = !isCall;
    }
    
 
    
    private void phoneIfNotify() {
    	final Set<String> selectedWifi = ApplicationSharedPreferences.getNoAlarmArea(getBaseContext());
    	final String wifiInfo = NoAlarmAreaActivity.getConnectWifiSsid(getBaseContext());
    	final boolean isOpen  =  ApplicationSharedPreferences.getIsOpenNoAlarmArea(getBaseContext());
    	final boolean isOpenPhoneAlert = ApplicationSharedPreferences.getHasOpenAntiLostRemind(LostOnlyMainActivity.this);
    	if (isOpen && selectedWifi != null && selectedWifi.contains(wifiInfo) || !isOpenPhoneAlert) {
    		Log.e(TAG, "is in no alarm area!");
    		return; 
    	}
    
    	
    	AntiLostNotification notification = AntiLostNotification.getInstance(getBaseContext());
		if (null != notification) {
			notification.setFlag(true);
			notification.sendRemindNotification(true);
		}
    }
	public static void gotoActivity(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, LostOnlyMainActivity.class);
		context.startActivity(intent);
	}
}