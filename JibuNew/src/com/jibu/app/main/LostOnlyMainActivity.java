package com.jibu.app.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import mybleservice.BluetoothLeService;
import mybleservice.SampleGattAttributes;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.jibu.app.R;
import com.jibu.app.server.AntiLostNotification;

public class LostOnlyMainActivity  extends Activity implements OnClickListener {
    private final static String TAG = LostOnlyMainActivity.class.getSimpleName();

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private TextView mConnectionState;
    private TextView mDataField;
    private String mDeviceName;
    private String mDeviceAddress;
    private ExpandableListView mGattServicesList;
    private BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private boolean mConnected = false;
    private BluetoothGattCharacteristic mNotifyCharacteristic;

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";

    private EditText mEditText;
    private Button  mCallButton;
    private TextView mCallTextView;
    
    private BluetoothGattCharacteristic mAuthNotifyCharacteristic;
    private BluetoothGattCharacteristic mKeyEnableNotifyCharacteristic;
    private BluetoothGattCharacteristic mAlertCharacteristic;
    
    private BluetoothGattService mAuthBluetoothLeService;
    
 // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };
    
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
                mConnected = true;
//                updateConnectionState(R.string.connected);
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
//                updateConnectionState(R.string.disconnected);
                invalidateOptionsMenu();
//                clearUI();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) { 
//                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            	Log.e(TAG, "receivce data" + intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            	String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
            	if (data != null) {
		        	if (data.contains("B1 24")) { //单击
		            	ToastUtil.toast("单击了设备");
		            	stopPhoneNotify();
		        	} else if (data.contains("FD E8")) {
		            	ToastUtil.toast("双击了设备");
		            	phoneNotify();
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
		
        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
	}

	@Override
	protected void onResume() {
		super.onResume();
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
	}
	@Override
	protected void onPause() {
	    super.onPause();
//	    unregisterReceiver(mGattUpdateReceiver);
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mGattUpdateReceiver);
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }



    boolean isCall = true;
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.id_linearlayout_title_left:
			this.finish();
			break;
		case R.id.id_textview_call:
			callDevice();
			break;
		case R.id.id_textview_my_belt:
			break;
		case R.id.id_textview_setup:
			LostSetupActivity.gotoAntiLostActivity(LostOnlyMainActivity.this);
			break;
		}
	}

    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
//        String unknownServiceString = getResources().getString(R.string.unknown_service);
//        String unknownCharaString = getResources().getString(R.string.unknown_characteristic);
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData
                = new ArrayList<ArrayList<HashMap<String, String>>>();
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            if (uuid.toLowerCase().contains("ffe0")) { //这是需要的sevice
            	mAuthBluetoothLeService = gattService;
            }
            currentServiceData.put(
                    LIST_NAME, SampleGattAttributes.lookup(uuid, "unknow"));
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData =
                    new ArrayList<HashMap<String, String>>();
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas =
                    new ArrayList<BluetoothGattCharacteristic>();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                uuid = gattCharacteristic.getUuid().toString();
                if (uuid.toLowerCase().contains("ffe2")) {
                	mAuthNotifyCharacteristic = gattCharacteristic;
					byte[] bytes = new byte[]{(byte) 0x8a, (byte) 0x92, (byte) 0x18, (byte) 0x1c};
					mAuthNotifyCharacteristic.setValue(bytes);
					mBluetoothLeService.writeCharateristic(mAuthNotifyCharacteristic);
                } else if (uuid.toLowerCase().contains("ffe1")) {
                	mKeyEnableNotifyCharacteristic = gattCharacteristic;
                	mBluetoothLeService.setCharacteristicNotification(mKeyEnableNotifyCharacteristic, true);
                } else if (uuid.toLowerCase().contains("2a06")) {
                	mAlertCharacteristic = gattCharacteristic;
                }
                currentCharaData.put(
                        LIST_NAME, SampleGattAttributes.lookup(uuid, "unknow"));
                currentCharaData.put(LIST_UUID, uuid);
                gattCharacteristicGroupData.add(currentCharaData);
            }
            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
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
			if (mAlertCharacteristic != null && mBluetoothLeService != null) {
				mAlertCharacteristic.setValue(new byte[]{0x01});
				mBluetoothLeService.writeCharateristic(mAlertCharacteristic);
			}
			mCallTextView.setText("停止");
		} else { //停止
			if (mAlertCharacteristic != null && mBluetoothLeService != null) {
				mAlertCharacteristic.setValue(new byte[]{0x00});
				mBluetoothLeService.writeCharateristic(mAlertCharacteristic);
			}
			mCallTextView.setText("呼叫");
		}
    	isCall = !isCall;
    }
    
    private void phoneNotify() {
//    	final Set<String> selectedWifi = ApplicationSharedPreferences.getNoAlarmArea(getBaseContext());
//    	final String wifiInfo = NoAlarmAreaActivity.getConnectWifiSsid(getBaseContext());
//    	final boolean isOpen  =  ApplicationSharedPreferences.getIsOpenNoAlarmArea(getBaseContext());
//    	if (isOpen && selectedWifi != null && selectedWifi.contains(wifiInfo)) {
//    		Log.e(TAG, "is in no alarm area!");
//    		return; 
//    	}
//    	
    	AntiLostNotification notification = AntiLostNotification.getInstance(getBaseContext());
		if (null != notification) {
			notification.setFlag(true);
			notification.sendRemindNotification(true);
		}
    }
    private void stopPhoneNotify() {
    	AntiLostNotification notification = AntiLostNotification.getInstance(getBaseContext());
    	notification.stopNotification();
    }
	public static void gotoActivity(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, LostOnlyMainActivity.class);
		context.startActivity(intent);
	}
}