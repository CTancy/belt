package com.jibu.app.login;

import java.util.Vector;
import org.json.JSONException;
import org.json.JSONObject;

import com.jibu.app.R;
import com.jibu.app.database.MoveDataService;
import com.jibu.app.database.PersonalInfoService;
import com.jibu.app.database.UserService;
import com.jibu.app.entity.MoveData;
import com.jibu.app.entity.User;
import com.jibu.app.entity.UserPersonalInfo;
import com.jibu.app.main.ApplicationSharedPreferences;
import com.jibu.app.main.MainActivity;
import com.jibu.app.main.MainApplication;
import com.jibu.app.main.ScanActivity;
import com.jibu.app.main.ToastUtil;
import com.jibu.app.main.WaitingActivity;
import com.jibu.app.user.PersonalinforActivity;
import com.umeng.analytics.MobclickAgent;
import com.veclink.hw.bleservice.VLBleServiceManager;
import com.veclink.hw.bleservice.util.Keeper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

public class LoginActivity extends WaitingActivity implements OnClickListener {

	//电话号码长度不超过13.
	private final int PHONE_NUM_LENGTH = 13;
	
	MainApplication mainApplication;
	
	EditText editTextPhone,editTextPsw;
	public static LoginActivity activity;
	ImageView imageViewEye;
	boolean isOpenEye = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		((MainApplication) this.getApplication()).addActivity(this);

		activity = this;
		
		mainApplication = (MainApplication) this.getApplication();

		findViewById(R.id.id_imageview_eye).setOnClickListener(this);
		findViewById(R.id.id_textview_forget).setOnClickListener(this);
		findViewById(R.id.id_textview_login).setOnClickListener(this);
		findViewById(R.id.id_textview_phone_reg).setOnClickListener(this);
//		findViewById(R.id.id_textview_email_reg).setOnClickListener(this);
		
		editTextPhone = (EditText)findViewById(R.id.id_edittext_phone);
		editTextPsw = (EditText)findViewById(R.id.id_edittext_pwd);
		
		UserService userService = new UserService(this);
		Vector<User> users=null;
		try {
			users = userService.queryAllUser();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		User firstUser = null;
		if(users!=null&&users.size()>0){
			firstUser = users.firstElement();
			String lastUser = ApplicationSharedPreferences.getLastUser(this);
			for (User user : users) {
				if (user.userId.equals(lastUser)) {
					firstUser = user;
					break;
				}
			}
			if (firstUser.userId.length() < PHONE_NUM_LENGTH) {
			editTextPhone.setText(firstUser.userId);
			}
		}
		
		
		imageViewEye = (ImageView) findViewById(R.id.id_imageview_eye);
		
		if(isOpenEye){
			
			imageViewEye.setBackgroundResource(R.drawable.open_eye);
			editTextPsw.setTransformationMethod(HideReturnsTransformationMethod
					.getInstance());
		}else{
			imageViewEye.setBackgroundResource(R.drawable.close_eye);
			editTextPsw.setTransformationMethod(PasswordTransformationMethod
					.getInstance());
		}
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
		case R.id.id_textview_login: {

			String phone = editTextPhone.getText().toString();
			String psw = editTextPsw.getText().toString();
			
			if(phone!=null&&phone.length()>0&&psw!=null&&psw.length()>0){
				showWait();
				new LoginAsyncTask().execute(new String[]{phone,psw});
			}else{
				ToastUtil.toast(R.string.phone_number_password_null);
			}
			
			
		}
			break;
		case R.id.id_textview_phone_reg: {
			RegActivity.gotoActivity(this);
			this.finish();
		}
			break;
//		case R.id.id_textview_email_reg:{
//			
//		}
//		break;
		case R.id.id_textview_forget:{
			ForgetActivity.gotoActivity(this);
			this.finish();
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

	private class LoginAsyncTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			String loginUrl = "http://api.ebelt.cn:8001/ebelt/login/login1?phone="+params[0]+"&password="+params[1];
			return HttpUtils.getJson(loginUrl, null, null, HttpUtils.METHOD_GET);
			
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			waitClose();
			
			
			parseRspInfo(result);
			
			Log.i("log","result="+result);
		}

	}
	
	private void parseRspInfo(String info){
		
		
		
		try {
			JSONObject jsonObject;
			jsonObject = new JSONObject(info);
			int ret = jsonObject.getInt("ret");
			
			if(ret==0){
				
				ToastUtil.toast(R.string.login_success);
				mainApplication.scode = jsonObject.getString("scode");
				JSONObject jsonObjectUser = jsonObject.getJSONObject("uinfo");
				String userId = jsonObjectUser.getString("u_name");
				
				mainApplication.user = new User(userId);
				UserService userService = new UserService(this);
				if(userService.checkUserIsExist(mainApplication.user.userId)){
					
					Log.i("sino",mainApplication.user.userId+" 存在");
					User user = userService.queryUserInfo(mainApplication.user.userId);
					user.password =editTextPsw.getText().toString();
					userService.updateUser(user);
					mainApplication.user = user;
				}else{
					Log.i("sino",mainApplication.user.userId+" 不存在");
					mainApplication.user.firstTime = System.currentTimeMillis();
					mainApplication.user.updateTime = mainApplication.user.firstTime;
					mainApplication.user.password = editTextPsw.getText().toString();
					userService.insertItem(mainApplication.user);
					
					MoveDataService  moveDataService = new MoveDataService(this);
					MoveData moveData = new MoveData(mainApplication.user.userId,mainApplication.user.updateTime);
					moveDataService.insertMoveData(moveData);
				}
				PersonalInfoService personalInfoService = new PersonalInfoService(this);
				UserPersonalInfo userPersonalInfo = new UserPersonalInfo(mainApplication.user.userId);
				if (personalInfoService.queryPersonalInfoByUser(mainApplication.user.userId) == null) {
					personalInfoService.insertPersonalInforItem(userPersonalInfo);
				} 
				
				ApplicationSharedPreferences.setLastUser(this, userId);
				if(mainApplication.user.sex==-1){
					
					PersonalinforActivity.gotoActivity(this,PersonalinforActivity.ENTRY_MODE_INIT);
					this.finish();
				}else{
					if(Keeper.getUserHasBindBand(this)){
						VLBleServiceManager.getInstance().bindService(getApplication());
						MainActivity.gotoActivity(this);
					}else{
						ScanActivity.gotoActivity(this);
						
						//MainActivity.gotoActivity(this);
						
					}
				}
				this.finish();
				
			}else{
				
				ToastUtil.toast(R.string.login_fail_phone_number_password_wrong);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ToastUtil.toast(R.string.login_fail_phone_number_password_wrong);
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		
		
		
		
	}

	public static void gotoActivity(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, LoginActivity.class);
		context.startActivity(intent);
	} 
}
