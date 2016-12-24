package com.jibu.app.main;

import com.jibu.app.R;
import com.szants.bracelet.bletask.BleCallBack;
import com.szants.sdk.AntsBeltSDK;
import com.szants.sdk.FindPhoneObserver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;

public class AntiLostNewActivity extends Activity implements OnClickListener {

	private AntsBeltSDK sdk;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_lost_new);
		findViewById(R.id.id_linearlayout_title_left).setOnClickListener(this);
		findViewById(R.id.id_textview_call).setOnClickListener(this);
		findViewById(R.id.id_textview_setup).setOnClickListener(this);
		
		sdk = AntsBeltSDK.getInstance();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	


	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.id_linearlayout_title_left:
			this.finish();
			break;
		case R.id.id_textview_call:
			sdk.findDevice(new BleCallBack() {
				
				@Override
				public void onStart(Object startObject) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onFinish(Object result) {
					
				}
				
				@Override
				public void onFailed(Object error) {
					ToastUtil.toast("查找不到设备");
				}
			});
			break;
		case R.id.id_textview_setup:
			AntiLostActivity.gotoAntiLostActivity(AntiLostNewActivity.this);
			break;
		}
	}

	public static void gotoActivity(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, AntiLostNewActivity.class);
		context.startActivity(intent);
	}
}
