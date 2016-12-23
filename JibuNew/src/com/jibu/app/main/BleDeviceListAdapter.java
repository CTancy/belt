package com.jibu.app.main;

/**  
* @Title: BleDeviceListAdapter.java
* @Package com.veclink.bledemo
* @Description: TODO(用一句话描述该文件做�?�?)
* @author Allen 
* @date 2014-9-6 上午11:26:44
* @version V1.0  
*/ 

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jibu.app.R;
import com.szants.bracelet.bean.BluetoothDeviceBean;

/**
 * @author Allen
 *
 */
public class BleDeviceListAdapter  extends BaseAdapter{
	
	public List<BluetoothDeviceBean> listItems = new ArrayList<BluetoothDeviceBean>();
	private Context mContext;
	public BleDeviceListAdapter(Context mContext){
		this.mContext = mContext;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listItems.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	
	public final class ViewHolder{
		public TextView name_view;
		public TextView address_view;
		public TextView rssi_view;
		public ImageView signal_view;
	}
	@Override
	public View getView(int position, View contentView, ViewGroup arg2) {
		ViewHolder holder;
		final BluetoothDeviceBean device = listItems.get(position);
		if(contentView==null){
			holder = new ViewHolder();
			contentView = LayoutInflater.from(mContext).inflate(R.layout.linearlayout_device, null);
			holder.name_view = (TextView) contentView.findViewById(R.id.id_textview_device_name);
			holder.address_view = (TextView) contentView.findViewById(R.id.id_textview_device_address);
			holder.rssi_view = (TextView) contentView.findViewById(R.id.id_textview_device_xinhao);
			holder.signal_view = (ImageView) contentView.findViewById(R.id.id_imageview_device_xinhao);
			contentView.setTag(holder);
		}else{
			holder = (ViewHolder) contentView.getTag();
		}
		
		holder.name_view.setText(device.getName());
		holder.address_view.setText(device.getAddress());
		holder.rssi_view.setText(String.valueOf(device.getRssi()));
	
		int xinhao = device.getRssi();
		
		if(xinhao<-90){
			holder.signal_view.setBackgroundResource(R.drawable.xinhao_1);
		}else if(xinhao>=-90&&xinhao<-70){
			holder.signal_view.setBackgroundResource(R.drawable.xinhao_2);
		}else{
			holder.signal_view.setBackgroundResource(R.drawable.xinhao_3);
		}
		return contentView;
	}
	
	public void addDeviceItem(BluetoothDeviceBean device){
		this.listItems.add(device);
		Collections.sort(this.listItems);
		this.notifyDataSetChanged();
	}
	
	public void clearAllDevieceItem(){
		this.listItems.clear();
		this.notifyDataSetChanged();
	}

}
