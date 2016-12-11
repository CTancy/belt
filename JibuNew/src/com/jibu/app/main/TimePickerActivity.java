package com.jibu.app.main;

import com.jibu.app.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

public class TimePickerActivity extends Activity implements android.view.View.OnClickListener{
	
	TimePicker timePicker;
	NumberPicker hourPicker, minPicker;
	LinearLayout numberPicker;
	
	TextView textView;
	int requestCode = -1;
	private final int HOUR_MAX_VALUE = 3;
	private final int MINUTE_MAX_VALUE = 59;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.time_picker_dialog);
		timePicker = (TimePicker) findViewById(R.id.id_timepicker);
		timePicker.setIs24HourView(true);
		textView = (TextView) findViewById(R.id.id_textView_title);
		
		numberPicker = (LinearLayout) findViewById(R.id.id_ll_number_picker);
		hourPicker   = (NumberPicker) findViewById(R.id.hourpicker);
		minPicker	 = (NumberPicker) findViewById(R.id.minuteicker);
		Bundle extras = getIntent().getExtras();
		
		if (null != extras) {
			requestCode = extras.getInt("requestCode");
		}
		
		switch(requestCode) {
		case SetupLongSitRemindActivity.REQUEST_CODE_INTERVAL_TIME:
			textView.setText(R.string.setup_longsit_time);
			timePicker.setVisibility(View.INVISIBLE);
			numberPicker.setVisibility(View.VISIBLE);
			hourPicker.setMaxValue(HOUR_MAX_VALUE);
			minPicker.setMaxValue(MINUTE_MAX_VALUE);
			break;
		case SetupLongSitRemindActivity.REQUEST_CODE_BEGIN_TIME:
			textView.setText(R.string.setup_begin_time);
			timePicker.setVisibility(View.VISIBLE);
			numberPicker.setVisibility(View.INVISIBLE);
			timePicker.setCurrentHour(9);
			timePicker.setCurrentMinute(0);
			break;
		case SetupLongSitRemindActivity.REQUEST_CODE_END_TIME:
			textView.setText(R.string.setup_end_time);
			timePicker.setVisibility(View.VISIBLE);
			numberPicker.setVisibility(View.INVISIBLE);
			timePicker.setCurrentHour(22);
			timePicker.setCurrentMinute(0);
			break;
		}
		
		findViewById(R.id.id_textview_resel).setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.id_textview_resel:
			long hour = -1;
			long min  = -1;
			if (requestCode == SetupLongSitRemindActivity.REQUEST_CODE_INTERVAL_TIME) {
				hour = hourPicker.getValue();
				min  = minPicker.getValue();
			} else {
				hour = timePicker.getCurrentHour();
				min  = timePicker.getCurrentMinute();
			}
			Intent intent = new Intent();
			intent.putExtra("hour", hour);
			intent.putExtra("min",  min);
			setResult(Activity.RESULT_OK, intent);
			this.finish();
			break;
		}
	}

	
	
}
