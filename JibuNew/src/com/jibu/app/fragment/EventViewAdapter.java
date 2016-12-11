package com.jibu.app.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.jibu.app.R;
import com.jibu.app.entity.HuanSuanUtil;
import com.jibu.app.entity.MoveData;
import com.jibu.app.entity.SportEvent;


import android.app.LauncherActivity.ListItem;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EventViewAdapter extends BaseAdapter {
	
	public ArrayList<SportEvent> SportEventItems  = new ArrayList<SportEvent>();
	private Context mContext;
	
	
	public EventViewAdapter(Context context) {
		mContext = context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return SportEventItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return SportEventItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		final SportEvent sportEvent = SportEventItems.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_events, null);
			holder.event_textview = (TextView) convertView.findViewById(R.id.id_textview_sport_info);
			holder.event_time_perid_textview =(TextView) convertView.findViewById(R.id.id_textview_sport_time_perid);
//			holder.event_time_total_textview =(TextView) convertView.findViewById(R.id.id_textview_sport_time_total);
//			holder.event_time_unit = (TextView) convertView.findViewById(R.id.id_textview_sport_time_unit);
			holder.event_imageview = (ImageView) convertView.findViewById(R.id.id_imageview_event_icon);
//			holder.move_step = (TextView) convertView.findViewById(R.id.id_textview_move_step);
//			holder.move_length = (TextView) convertView.findViewById(R.id.id_textview_move_length);
//			holder.move_cal = (TextView) convertView.findViewById(R.id.id_textview_move_cal);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
//			convertView.findViewById(R.id.id_linearlayout_bottom).setVisibility(View.GONE);
		}
		
		
		if (position == 0) {
			(convertView.findViewById(R.id.id_textview_shang_xian)).setVisibility(View.INVISIBLE);
		} else {
			(convertView.findViewById(R.id.id_textview_shang_xian)).setVisibility(View.VISIBLE);
		}
		
		if (position == SportEventItems.size() - 1) {
			(convertView.findViewById(R.id.id_textview_xia_xian)).setVisibility(View.INVISIBLE);
		} else {
			(convertView.findViewById(R.id.id_textview_xia_xian)).setVisibility(View.VISIBLE);
		}
		String str = "";
		if (sportEvent.getType() == MoveData.NO_MOVE) { 
			float time = sportEvent.getSportTotalTime();
			int type  = sportEvent.getSportType();
		    str = String.format(mContext.getString(R.string.sit_event_stastic_value), 
		    		time, mContext.getString(type));
		} else {
			int step = sportEvent.getSteps();
		    str = String.format(mContext.getResources().getString(R.string.sport_event_stastic_value), 
		    		step, HuanSuanUtil.getLengthByStep(step), HuanSuanUtil.getCalByStep(step));
		}
		
	
		holder.event_imageview.setImageResource(sportEvent.getSportIcon());
		holder.event_time_perid_textview.setText(sportEvent.getSportPeriod());
		holder.event_textview.setText(str);
//		holder.event_time_total_textview.setText(""+sportEvent.getSportTotalTime());
//		holder.event_time_unit.setText(sportEvent.getSportTimeUnit());
//		holder.move_step.setText("" + sportEvent.getSteps());
//		holder.move_length.setText("" + HuanSuanUtil.getLengthByStep(sportEvent.getSteps()));
//		holder.move_cal.setText("" + HuanSuanUtil.getCalByStep(sportEvent.getSteps()));
//		Log.e("TAG", "getView position = " + position + "currentTime = " + System.currentTimeMillis());
		return convertView;
	}
	
	
	public final class ViewHolder{
		public TextView event_textview;
		public TextView event_time_perid_textview;
//		public TextView event_time_total_textview;
//		public TextView event_time_unit;
		public ImageView event_imageview;
//		public TextView  move_step;
//		public TextView  move_length;
//		public TextView  move_cal;
	}
	public synchronized void addItem(final SportEvent sportEvent){
		this.SportEventItems.add(sportEvent);
		this.notifyDataSetChanged();
	}
	public void addAllItem(Vector<SportEvent> sportEvents) {
		for(int i = 0; i < sportEvents.size(); i++) {
			this.SportEventItems.add(sportEvents.elementAt(i));
//			addItem(sportEvents.elementAt(i));
		}
		this.notifyDataSetChanged();
	}
	public void clearAllDevieceItem(){
		this.SportEventItems.clear();
//		this.notifyDataSetChanged();
	}
	
}
