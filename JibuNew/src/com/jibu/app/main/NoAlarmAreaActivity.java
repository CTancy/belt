package com.jibu.app.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.jibu.app.R;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class NoAlarmAreaActivity extends Activity implements OnClickListener{

	private final String TAG = "NoAlarmAreaActivity";
	
	TextView textViewSwitch; 
	ImageView imageViewSwitchIcon;
	
	private boolean isOpenNoAlarmArea;
	private boolean isSetNoAlarmWiFi;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_no_alarm_area);
		initView();
		updateView();
//		getWifiInfo();
//		getConnectWifiSsid(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateView();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	private void initView() {
		findViewById(R.id.id_linearlayout_title_left).setOnClickListener(this);
		findViewById(R.id.id_relativelayout_no_alert_select_wifi).setOnClickListener(this);
		findViewById(R.id.id_relativelayout_no_alarm).setOnClickListener(this);
		textViewSwitch = (TextView) findViewById(R.id.id_textview_no_alert_switch);
		imageViewSwitchIcon = (ImageView) findViewById(R.id.id_imageview_no_alarm_switch);
		
	}
	
	private void updateView() {
//		ApplicationSharedPreferences.setIsOpenNoAlarmArea(NoAlarmAreaActivity.this, true);
		isOpenNoAlarmArea = ApplicationSharedPreferences.getIsOpenNoAlarmArea(NoAlarmAreaActivity.this);
		Set<String> set = ApplicationSharedPreferences.getNoAlarmArea(NoAlarmAreaActivity.this);
		if (isOpenNoAlarmArea) {
			imageViewSwitchIcon.setImageResource(R.drawable.open_remind);
		} else {
			imageViewSwitchIcon.setImageResource(R.drawable.close_remind);
		}
		if (null != set && set.size() > 0) {
			isSetNoAlarmWiFi = true;
			textViewSwitch.setText(R.string.setup_already);
		} else {
			isSetNoAlarmWiFi = false;
			textViewSwitch.setText(R.string.click_to_add_wi_fi);
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.id_linearlayout_title_left:
			this.finish();
			break;
			
		case R.id.id_relativelayout_no_alarm:
			setOpenCloseNoAlarmArea();
			break;
		
		case R.id.id_relativelayout_no_alert_select_wifi:
			SelectXinRenWiFiActivity.gotoActivity(NoAlarmAreaActivity.this);
			break;
		}
		
	}
	
	private void setOpenCloseNoAlarmArea() {
		isOpenNoAlarmArea = ApplicationSharedPreferences.getIsOpenNoAlarmArea(NoAlarmAreaActivity.this);
		/*close*/
		if (isOpenNoAlarmArea) {
			isOpenNoAlarmArea = false;
			imageViewSwitchIcon.setImageResource(R.drawable.close_remind);
			ApplicationSharedPreferences.setIsOpenNoAlarmArea(NoAlarmAreaActivity.this, isOpenNoAlarmArea);
		} else {
			isOpenNoAlarmArea = true;
			imageViewSwitchIcon.setImageResource(R.drawable.open_remind);
			ApplicationSharedPreferences.setIsOpenNoAlarmArea(NoAlarmAreaActivity.this, isOpenNoAlarmArea);
		}
		
	}
	
    public static String getConnectWifiSsid(Context context){  
    	WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);  
    	WifiInfo wifiInfo = wifiManager.getConnectionInfo();  
    	Log.d("wifiInfo", wifiInfo.toString());  
    	Log.d("SSID",wifiInfo.getSSID());  
    	return wifiInfo.getSSID();  
    }  

 	
}
