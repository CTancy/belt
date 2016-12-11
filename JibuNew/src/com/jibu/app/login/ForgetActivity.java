package com.jibu.app.login;

import org.json.JSONException;
import org.json.JSONObject;

import com.jibu.app.R;
import com.jibu.app.database.UserService;
import com.jibu.app.entity.User;
import com.jibu.app.main.MainApplication;
import com.jibu.app.main.ToastUtil;
import com.jibu.app.main.WaitingActivity;
import com.jibu.app.view.CountTimer;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView.FixedViewInfo;
import android.widget.TextView;

public class ForgetActivity extends WaitingActivity implements OnClickListener {

	
	
	MainApplication mainApplication;
	
	EditText editTextPhone,editTextPsw,editTextCode;
	TextView textViewGetCode;
	CountTimer countTimer;
	
	ImageView imageViewEye;
	boolean isOpenEye = false;
	public static ForgetActivity activity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_forget);
		mainApplication = (MainApplication) this.getApplication();
		activity = this;
		editTextPhone = (EditText) findViewById(R.id.id_edittext_phone);
		editTextPsw = (EditText) findViewById(R.id.id_edittext_pwd);
		editTextCode = (EditText) findViewById(R.id.id_edittext_code);
		
		textViewGetCode = (TextView) findViewById(R.id.id_textview_getcode);
		
		imageViewEye = (ImageView) findViewById(R.id.id_imageview_eye);
		
		countTimer = new CountTimer(textViewGetCode, R.string.get_code, getString(R.string.reacquire_code));
		
		if(isOpenEye){
			
			imageViewEye.setBackgroundResource(R.drawable.open_eye);
			editTextPsw.setTransformationMethod(HideReturnsTransformationMethod
					.getInstance());
		}else{
			imageViewEye.setBackgroundResource(R.drawable.close_eye);
			editTextPsw.setTransformationMethod(PasswordTransformationMethod
					.getInstance());
		}
		
		findViewById(R.id.id_imageview_eye).setOnClickListener(this);
		textViewGetCode.setOnClickListener(this);
		findViewById(R.id.id_textview_ok).setOnClickListener(this);
		findViewById(R.id.id_textview_login).setOnClickListener(this);
		findViewById(R.id.id_textview_phone_reg).setOnClickListener(this);
		
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	
		activity = null;
		

	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.id_textview_ok:{
			String name = editTextPhone.getText().toString();
			String pwd = editTextPsw.getText().toString();
			String code = editTextCode.getText().toString();
			if (null == name || name.length() == 0||null == pwd || pwd.length() == 0||null == code || code.length() == 0) {
				ToastUtil.toast(R.string.phone_number_password_code_null);
			}else{
				showWait();
				new ForgetAsyncTask().execute(new String[]{name,pwd,code});
			}
			
			
		}
			break;
		case R.id.id_textview_getcode:{
			String name = editTextPhone.getText().toString();
			
			if (null == name || name.length() == 0) {
				ToastUtil.toast(R.string.phone_number_null);
			}else{
				countTimer.start();
				showWait();
				new GetCodeAsyncTask().execute(new String[]{name});
			}

			
		}
			break;
		case R.id.id_textview_login:{
			LoginActivity.gotoActivity(this);
		}
		break;
		case R.id.id_textview_phone_reg:{
			
		}
		break;
		case R.id.id_imageview_eye:{
			if(isOpenEye){
				isOpenEye = false;
				imageViewEye.setBackgroundResource(R.drawable.close_eye);
				editTextPsw.setTransformationMethod(PasswordTransformationMethod
						.getInstance());
			}else{
				isOpenEye = true;
				imageViewEye.setBackgroundResource(R.drawable.open_eye);
				editTextPsw.setTransformationMethod(HideReturnsTransformationMethod
						.getInstance());
			}
		}
		break;
		}
	}

	private class ForgetAsyncTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			String url = "http://112.74.27.169:8001/ebelt/login/forget_pass?phone="+params[0]+"&newpass="+params[1]+"&sms_code="+params[2];
			return HttpUtils.getJson(url, null, null, HttpUtils.METHOD_GET);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			System.out.print("rrrrrrrrrrrrrr "+result);
			
			waitClose();
			
			
			parseRegRspInfo(result);
		}
	}
	
	private class GetCodeAsyncTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			String url = "http://112.74.27.169:8001/ebelt/login/forget_pass?phone="+params[0];
			return HttpUtils.getJson(url, null, null, HttpUtils.METHOD_GET);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			System.out.print("rrrrrrrrrrrrrr "+result);
			
			waitClose();
			
			
			parseGetCodeRspInfo(result);
		}
	}
	
	
	private void parseRegRspInfo(String info){
		
		
		
		try {
			JSONObject jsonObject;
			jsonObject = new JSONObject(info);
			int ret = jsonObject.getInt("ret");
			
			if(ret==0){
				
				ToastUtil.toast(R.string.fix_password_success);
				LoginActivity.gotoActivity(this);
				this.finish();
				
			}else{
				String msg = jsonObject.getString("msg");
				ToastUtil.toast(msg);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		
		
		
		
	}
	
	
	private void parseGetCodeRspInfo(String info){
		
		
		
		try {
			JSONObject jsonObject;
			jsonObject = new JSONObject(info);
			int ret = jsonObject.getInt("ret");
			
			if(ret==0){
				
				ToastUtil.toast(R.string.code_has_send);
				
				
				
			}else{
				String msg = jsonObject.getString("msg");
				ToastUtil.toast(msg);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		
		
		
		
	}
	
	public static void gotoActivity(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, ForgetActivity.class);
		context.startActivity(intent);
	} 
}
