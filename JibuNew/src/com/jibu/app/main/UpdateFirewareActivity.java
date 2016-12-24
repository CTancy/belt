package com.jibu.app.main;

import java.io.IOException;

import com.jibu.app.R;
import com.jibu.app.view.RoundProgressBar2;
//import com.veclink.bracelet.bletask.BleCallBack;
//import com.veclink.bracelet.bletask.UpdateFirmwareUtil;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateFirewareActivity extends Activity {

	private RoundProgressBar2 roundProgressBar;
	private TextView textview_complete_bfb;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_update_fireware);

		initView();
//		try {
//			updateHwVersion();
//		} catch (IOException e){
//			e.printStackTrace();
//			setResult(RESULT_CANCELED);
//			UpdateFirewareActivity.this.finish();
//		}
	}
	private void initView() {
		roundProgressBar = (RoundProgressBar2) findViewById(R.id.id_roundprogressbar_update_fireware);
		roundProgressBar.setMax(100);
		roundProgressBar.setBitmap(R.drawable.update_jiantou, 104, 104);
		textview_complete_bfb = (TextView) findViewById(R.id.id_textview_complete_bfb);
		String CompleteBfb = getString(R.string.update_progress) + " 0%";
		textview_complete_bfb.setText(CompleteBfb);
	}
//	Handler updateHandler = new Handler(){
//
//		@Override
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case BleCallBack.TASK_START:
//				break;
//
//			case BleCallBack.TASK_PROGRESS:
//				
//				if(msg.obj!=null){
//					int progress = (Integer)msg.obj;
//					String CompleteBfb = getString(R.string.update_progress) + progress + "%";
//					roundProgressBar.setProgress(progress);
//					textview_complete_bfb.setText(CompleteBfb);
//				}
//				break;
//
//			case BleCallBack.TASK_FINISH:
//				setResult(RESULT_OK);
//				UpdateFirewareActivity.this.finish();
//				break;
//
//			case BleCallBack.TASK_FAILED:
//				setResult(RESULT_CANCELED);
//				UpdateFirewareActivity.this.finish();
//				break;
//			}
//		}
//		
//	};
//	
//	BleCallBack updateCallBack = new BleCallBack(updateHandler);
//	
//	private void updateHwVersion() throws IOException{
//
//		String filePath = Environment.getExternalStorageDirectory().getPath()+"/"+YaodaiActivity.FIREWARE_NAME;
//
//		YaodaiActivity.copyBigDataToSD(filePath);
//
//		UpdateFirmwareUtil.update(this, updateCallBack, filePath);
//		
//	}
}
