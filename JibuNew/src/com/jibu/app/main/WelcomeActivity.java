package com.jibu.app.main;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import mybleservice.E3AKeeper;

import org.json.JSONException;
import org.json.JSONObject;

import com.jibu.app.R;
import com.jibu.app.NetworkDetection.NetworkDetection;
import com.jibu.app.database.CejuService;
import com.jibu.app.database.MoveDataService;
import com.jibu.app.database.PersonalInfoService;
import com.jibu.app.database.UserService;
import com.jibu.app.entity.MoveData;
import com.jibu.app.entity.User;
import com.jibu.app.entity.UserPersonalInfo;
import com.jibu.app.login.HttpUtils;
import com.jibu.app.login.LoginActivity;
import com.jibu.app.login.LoginAndRegActivity;
import com.jibu.app.user.PersonalinforActivity;
import com.szants.hw.bleservice.util.Keeper;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.veclink.hw.bleservice.VLBleServiceManager;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WelcomeActivity extends Activity  {
	
	public static final String TAG = "WelcomeActivity";
	public static WelcomeActivity activity = null;


	MainApplication mainApplication;
	
//	private boolean test_value = false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);
		((MainApplication) this.getApplication()).addActivity(this);

		
		activity = this;
		mainApplication = (MainApplication) this.getApplication();
		
		
	}

	@Override
	protected void onStart() {

		super.onStart();

		

	}

	@Override
	protected void onPause() {

		super.onPause();
		
		MobclickAgent.onPause(this);
		
	}

	@Override
	protected void onStop() {

		super.onStop();
	
	}

	@Override
	protected void onResume() {

		super.onResume();
		
		//initGloal();
		if(ApplicationSharedPreferences.getSystemFirstRun(this)){
			Log.e("log","dddddddddddd");
			new InitTask().execute();
		}else{
			Log.e("log","eeeeeeeeeeee");
			new InitTask2().execute();
			
			
			
		}
		
		MobclickAgent.onResume(this);
		/*
		
		UserService userService = new UserService(this);
		if(userService.checkUserIsExist(mainApplication.user.userId)){
			
			User user = userService.queryUserInfo(mainApplication.user.userId);
			
			mainApplication.user = user;
		}else{
			  
			mainApplication.user.firstTime = System.currentTimeMillis();
			userService.insertItem(mainApplication.user);
		}
		
		if(mainApplication.user.sex==0){
			
			
		}
		
		
		if(Keeper.getUserHasBindBand(this)){
			MainActivity.gotoActivity(this);
		}else{
			ScanActivity.gotoActivity(this);
			
			//MainActivity.gotoActivity(this);
			
		}

*/
		
		
	}

	private void initGloal(){
		
		
			
		
			UserService userService = new UserService(this);
			if(!userService.isTableExist()){
				int version = ApplicationSharedPreferences.getDataBaseVersion(this);
				ApplicationSharedPreferences.setDataBaseVersion(this, version+1);
				UserService userService2 = new UserService(this);
				userService2.initUser();
			}
			MoveDataService moveDataService = new MoveDataService(this);
			if(!moveDataService.isTableExist()){
				int version = ApplicationSharedPreferences.getDataBaseVersion(this);
				ApplicationSharedPreferences.setDataBaseVersion(this, version+1);
				MoveDataService moveDataService2 = new MoveDataService(this);
				moveDataService2.init();
			}
			
			PersonalInfoService personalInfoService = new PersonalInfoService(this);
			if (!personalInfoService.isTableExist()) {
				int version = ApplicationSharedPreferences.getDataBaseVersion(this);
				ApplicationSharedPreferences.setDataBaseVersion(this, version+1);
				PersonalInfoService personalInfoService2 = new PersonalInfoService(this);
				personalInfoService2.initPersonalInfo();
			}
			
			//更新User
			if (ApplicationSharedPreferences.getDataBaseVersion(this) <= UserService.USER_DB_VESTION) {
				int version = ApplicationSharedPreferences.getDataBaseVersion(this);
				ApplicationSharedPreferences.setDataBaseVersion(this, version+1);
				UserService userService2 = new UserService(this);
				userService2.initUser();
			}
			
			//更新MoveData
			if (ApplicationSharedPreferences.getDataBaseVersion(this) <= MoveDataService.MOVE_DATA_DB_VERSION) {
				int version = ApplicationSharedPreferences.getDataBaseVersion(this);
				ApplicationSharedPreferences.setDataBaseVersion(this, version+1);
				MoveDataService moveDataService2 = new MoveDataService(this);
				moveDataService2.init();
			}
			
			CejuService cejuService = new CejuService(this);
			if( !cejuService.isTableExist()) {
				int version = ApplicationSharedPreferences.getDataBaseVersion(this);
				ApplicationSharedPreferences.setDataBaseVersion(this, version+1);
				CejuService cejuService2 = new CejuService(this);
				cejuService2.initCeju();
			}
			//更新Personal
//			if (ApplicationSharedPreferences.getDataBaseVersion(this) < PersonalInfoService.PERSONALINFO_DB_VERSION) {
//				int version = ApplicationSharedPreferences.getDataBaseVersion(this);
//				ApplicationSharedPreferences.setDataBaseVersion(this, version+1);
//				PersonalInfoService personalInfoService2 = new PersonalInfoService(this);
//				personalInfoService2.initPersonalInfo();
//				personalInfoService2.deleteAllUserPersonalInfo();
//			}
		
		
		//User user = new User("yangchao");
				
		//mainApplication.user = user;
	}
	
	
	private class InitTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			initGloal();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
//			LoginAndRegActivity.gotoActivity(WelcomeActivity.this);
			new InitTask2().execute();
//			WelcomeActivity.this.finish();
		}

	}
	
	private class InitTask2 extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			UserService userService = new UserService(WelcomeActivity.this);

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
				/**获取最新登陆的user*/
				String lastUser = ApplicationSharedPreferences.getLastUser(WelcomeActivity.this);
				for (User user : users) {
					if (user.userId.equals(lastUser)) {
						firstUser = user;
						break;
					}
				}
				Log.i("log","firstUser.userId="+firstUser.userId+" firstUser.password="+firstUser.password);
				
				//fix by ctc 
				/**
				if (NetworkDetection.isConnect(this)) {
					Log.e(TAG, "Network is Ok");
					new LoginAsyncTask().execute(new String[]{firstUser.userId,firstUser.password});
				} else*/
				{
//					Log.e(TAG, "Network is wrong");
					if (firstUser.password != "" && !firstUser.password.equals("")) {
						offlineLogin(firstUser.userId);
					} else {
						LoginAndRegActivity.gotoActivity(WelcomeActivity.this);
					}
					
				}
			
			}else{
				LoginAndRegActivity.gotoActivity(WelcomeActivity.this);
			}
			
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
			
			
			
			parseRspInfo(result);
			
			Log.i("log","result="+result);
		}

	}
	
	
	private void parseRspInfo(String info){
		
		Log.i("log","parseRspInfo:info="+info);
		
		try {
			JSONObject jsonObject;
			jsonObject = new JSONObject(info);
			int ret = jsonObject.getInt("ret");
			
			if(ret==0){
				
				//ToastUtil.toast("登录成功");
				
				mainApplication.scode = jsonObject.getString("scode");
				
				JSONObject jsonObjectUser = jsonObject.getJSONObject("uinfo");
				String userId = jsonObjectUser.getString("u_name");
				
				mainApplication.user = new User(userId);
				UserService userService = new UserService(this);
				if(userService.checkUserIsExist(mainApplication.user.userId)){
					
					Log.i("sino",mainApplication.user.userId+" 存在");
					User user = userService.queryUserInfo(mainApplication.user.userId);
					
					mainApplication.user = user;
				}else{
					Log.i("sino",mainApplication.user.userId+" 不存在");
					mainApplication.user.firstTime = System.currentTimeMillis();
					mainApplication.user.updateTime = mainApplication.user.firstTime;
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
					
				if(mainApplication.user.sex==-1){
					
					PersonalinforActivity.gotoActivity(this,PersonalinforActivity.ENTRY_MODE_INIT);
					this.finish();
				}else{
					if(Keeper.getUserHasBindBand(this)){
						VLBleServiceManager.getInstance().bindService(getApplication());
						MainActivity.gotoActivity(this);
					}else if (E3AKeeper.getInstance().hasBindDevice(this)){
						
						//MainActivity.gotoActivity(this);
						LostOnlyMainActivity.gotoActivity(this);
						
					}else{
						ScanActivity.gotoActivity(this);
						
						//MainActivity.gotoActivity(this);
						
					}
				}
				/*
				if(Keeper.getUserHasBindBand(this)){
					VLBleServiceManager.getInstance().bindService(getApplication());
					MainActivity.gotoActivity(this);
				}else{
					ScanActivity.gotoActivity(this);
					
					//MainActivity.gotoActivity(this);
					
				}
				*/
				
			}else{
				LoginAndRegActivity.gotoActivity(WelcomeActivity.this);
				//ToastUtil.toast("登录失败，用户名或密码错误。");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LoginAndRegActivity.gotoActivity(WelcomeActivity.this);
//			MainActivity.gotoActivity(activity);
		}

		this.finish();
		
		
		
		
	}
	
	
	@Override
	protected void onDestroy() {

		super.onDestroy();
	
		activity = null;
		

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			((MainApplication) this.getApplication()).exit();
		}
		return false;
	}

	/**
	 * 离线登录
	 * add by ctc  
	 */
	public void offlineLogin(String userId)
	{
		mainApplication.user = new User(userId);
		UserService userService = new UserService(this);
		if(userService.checkUserIsExist(mainApplication.user.userId)){
			
			Log.i("sino",mainApplication.user.userId+" 存在");
			User user = userService.queryUserInfo(mainApplication.user.userId);
			
			mainApplication.user = user;
		}else{
			Log.i("sino",mainApplication.user.userId+" 不存在");
			mainApplication.user.firstTime = System.currentTimeMillis();
			mainApplication.user.updateTime = mainApplication.user.firstTime;
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
		if(mainApplication.user.sex==-1){
			
			PersonalinforActivity.gotoActivity(this,PersonalinforActivity.ENTRY_MODE_INIT);
			this.finish();
		}else{
			initUmengUpdate();
			if(Keeper.getUserHasBindBand(this)){
				VLBleServiceManager.getInstance().bindService(getApplication());
				MainActivity.gotoActivity(this);
			}else if (E3AKeeper.getInstance().hasBindDevice(this)){
				
				//MainActivity.gotoActivity(this);
				LostOnlyMainActivity.gotoActivity(this);
				
			}else{
				ScanActivity.gotoActivity(this);
			}
		}
	}
	
	private void initUmengUpdate() {
		UmengUpdateAgent.setUpdateAutoPopup(true);
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.update(this);
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			
			@Override
			public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
				switch (updateStatus) {
				case 0:
					UmengUpdateAgent.showUpdateDialog(WelcomeActivity.this, updateInfo);
				case 1:
					ApplicationSharedPreferences.setAPPVersion(WelcomeActivity.this, updateStatus);
					break;
				}
			}
		});
	}
	public static void gotoActivity(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, WelcomeActivity.class);
		context.startActivity(intent);
	}
	
}
