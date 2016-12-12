package mybleservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import com.jibu.app.main.ToastUtil;

public class E3AKeeper {
	
	private static E3AKeeper instance = new E3AKeeper();
	private static String TAG = "E3AKeeper";
	
	private Context context;
	public String name = "";
	public String address = "";
	
	
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private boolean mConnected = false;
    private BluetoothGattCharacteristic mNotifyCharacteristic;
    private BluetoothLeService mBluetoothLeService;

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";
    
    private BluetoothGattCharacteristic mAuthNotifyCharacteristic;
    private BluetoothGattCharacteristic mKeyEnableNotifyCharacteristic;
    private BluetoothGattCharacteristic mAlertCharacteristic;
    
    private BluetoothGattService mAuthBluetoothLeService;
    
	
	public static E3AKeeper getInstance() {
		return instance;
	}
	
	public void setRemoteDevice(String name, String address) {
		this.name = name;
		this.address = address;
	}
	
	public void binderDevice(Context context) {
		if ("".equals(this.name)) {
			this.name = getBinderName(context);
		}
		if ("".equals(this.address)) {
			this.address = getBinderAddress(context);
		}
		
		this.context = context;
        Intent gattServiceIntent = new Intent(context, BluetoothLeService.class);
        context.bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        
        context.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
	}
	public void unBinderDevice(Context context) {
		if (this.context != null) {
			this.context.unbindService(mServiceConnection);
		}
	}
	
	 // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(address);
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
                savedContentedDevice();
                
//                updateConnectionState(R.string.connected);
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
//                updateConnectionState(R.string.disconnected);
                Log.e(TAG, "断开了设备连接");
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
//		            	stopPhoneNotify();
		        	} else if (data.contains("FD E8")) {
		            	ToastUtil.toast("双击了设备");
//		            	phoneNotify();
		        	}
            	}
            }
        }
    };
    
    
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
    
    protected void savedContentedDevice() {
        setBinderAddress(context, this.address);
        setBinderName(context, this.name);
	}

	public void callDevice() {
		if (mAlertCharacteristic != null && mBluetoothLeService != null) {
			mAlertCharacteristic.setValue(new byte[]{0x01});
			mBluetoothLeService.writeCharateristic(mAlertCharacteristic);
		}
    }
    
    public void stopDevice() {
		if (mAlertCharacteristic != null && mBluetoothLeService != null) {
			mAlertCharacteristic.setValue(new byte[]{0x00});
			mBluetoothLeService.writeCharateristic(mAlertCharacteristic);
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
    
	//保存最近一次的登录帐号
	private final static String PREF_BINDER_ADDRESS = "binder_address";
	private final static String PREF_BINDER_ADDRESS_DEFFAULT_VALUE = "";
	
	public static String getBinderAddress(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getString(PREF_BINDER_ADDRESS, PREF_BINDER_ADDRESS_DEFFAULT_VALUE);
	}
	public static void setBinderAddress(Context context, String value) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(PREF_BINDER_ADDRESS, value);
		editor.commit();
	}
	
	//保存最近一次的登录帐号
	private final static String PREF_BINDER_NAME = "binder_name";
	private final static String PREF_BINDER_NAME_DEFALUT_VALUE = "";
	
	public String getBinderName(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getString(PREF_BINDER_NAME, PREF_BINDER_NAME_DEFALUT_VALUE);
	}
	public void setBinderName(Context context, String value) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(PREF_BINDER_NAME, value);
		editor.commit();
	}
	
	//判断是否成功绑定过设备
	public boolean hasBindDevice(Context context) {
		return !PREF_BINDER_NAME_DEFALUT_VALUE.equals(getBinderName(context)) 
				&& !PREF_BINDER_ADDRESS_DEFFAULT_VALUE.equals(getBinderAddress(context));
	}
	
	public boolean hasContectedDevice() {
		return mConnected;
	}
	
	public void clearBindDeviceMessage(Context context) {
		setBinderName(context, PREF_BINDER_NAME_DEFALUT_VALUE);
		setBinderAddress(context, PREF_BINDER_ADDRESS_DEFFAULT_VALUE);
	}
}
