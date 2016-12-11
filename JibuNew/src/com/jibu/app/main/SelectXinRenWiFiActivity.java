package com.jibu.app.main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import com.jibu.app.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SelectXinRenWiFiActivity extends Activity {

	
	ListView listViewWiFi;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_select_wifi);
		
		initView();
	}
	

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	private void initView() {
		listViewWiFi = (ListView) findViewById(R.id.id_listview_wifi);
		
		WifiListViewAdapter Adapter = new WifiListViewAdapter(SelectXinRenWiFiActivity.this);
		
		listViewWiFi.setAdapter(Adapter);
		
		findViewById(R.id.id_linearlayout_title_left).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SelectXinRenWiFiActivity.this.finish();
			}
		});
	}
	
	public class WifiListViewAdapter extends BaseAdapter {
		
		public List<String> WiFis =  new ArrayList<String>();
		/***
		 * selectedWifi是从shareperference获取的set实例，根据API说明其实例无法改变
		 * 故另外克隆一个selectedWifi2实例满足修改操作
		 */
		public Set<String>  selectedWifi, selectedWifi2;
		
		Context mContext;
		
		public WifiListViewAdapter(Context context) {
			mContext = context;
			WiFis = getWifiInfo();
			selectedWifi = ApplicationSharedPreferences.getNoAlarmArea(context);

			selectedWifi2 = new HashSet<String>();
			
			if (selectedWifi == null) {
				return;
			}
			//test
			Iterator it = selectedWifi.iterator();
			while(it.hasNext()) {
				Object o = it.next();
//				Log.e("TAG saved wifi = ", o.toString()+"\n");
				selectedWifi2.add(o.toString());
			}
		}
		
		@Override
		public int getCount() {
			return WiFis.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			final String WiFiName = WiFis.get(position);
			
//			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_wifi, null);
				holder.textView = (TextView) convertView.findViewById(R.id.id_textview_wifi_name);
				holder.checkBox = (CheckBox) convertView.findViewById(R.id.id_checkBox1);
				convertView.setTag(holder);
//			} else {
//				holder = (ViewHolder) convertView.getTag();
//			}

			if (holder != null) {
				
				holder.textView.setText(WiFiName);
				if (selectedWifi2 != null && selectedWifi2.contains(WiFiName.toString())) {
					holder.checkBox.setChecked(true);
				} else {
					holder.checkBox.setChecked(false);
				}
				
				holder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						String name = WiFiName;
						if (isChecked) {
							selectedWifi2.add(name);
						} else {
							selectedWifi2.remove(name);
						}
						ApplicationSharedPreferences.setNoAlarmArea(SelectXinRenWiFiActivity.this,
								selectedWifi2);
					}
				});
			}
			return convertView;
		}
		
	}
	public final class ViewHolder{
		public TextView textView;
		public CheckBox checkBox;
		public ImageView imageView;

	}
    private List<String> getWifiInfo() {
    	WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);  
        List<WifiConfiguration> wifiConfigurations = wifiManager.getConfiguredNetworks();//搜索到的Wifi列表  
        List<String> WiFis = new ArrayList<String>();
        try {
        	for (WifiConfiguration scanResult : wifiConfigurations) {
//        		Log.e(TAG, "scanResult.SSID = " +  scanResult.SSID);
        		WiFis.add(scanResult.SSID);
        	}
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        return WiFis;
    }
    
    public static void gotoActivity(Context context) {
    	Intent intent = new Intent(context, SelectXinRenWiFiActivity.class);
    	context.startActivity(intent);
    }
}
