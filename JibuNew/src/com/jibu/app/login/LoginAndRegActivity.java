package com.jibu.app.login;


import java.util.Map;
import java.util.Set;

import mybleservice.E3AKeeper;

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
import com.jibu.app.main.LostOnlyMainActivity;
import com.jibu.app.main.MainActivity;
import com.jibu.app.main.MainApplication;
import com.jibu.app.main.ScanActivity;
import com.jibu.app.main.ToastUtil;
import com.jibu.app.main.WaitingActivity;
import com.jibu.app.user.PersonalinforActivity;
import com.szants.hw.bleservice.util.Keeper;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;

public class LoginAndRegActivity extends WaitingActivity implements OnClickListener {

	private final int LOGIN_ERROR = 7 ;
	
	MainApplication mainApplication;
	
	public static LoginAndRegActivity activity;
	
	final UMSocialService mController  = UMServiceFactory.getUMSocialService("com.umeng.login");
	
	final static String password = "1234567";
	
	//QQ（微信）登录后获取的相关信息KEY值
	Map<String, Object> mInfo;
	//微信
	final String USER_ID_WX = "openid";
	final String USER_NAME_WX = "nickname";
	final String USER_SEX_WX  = "sex";
	final String USER_HEADIMG_WX  = "headimgurl";
	
	//QQ
	final String USER_ID_QQ = "openid";
	final String USER_NAME_QQ = "screen_name";
	final String USER_SEX_QQ  = "gender";
	final String USER_HEADIMG_QQ = "profile_image_url";
	
	//区分是微信登录还是QQ登录
	int login_method = -1;
	final int LOGIN_METHOD_QQ = 1;
	final int LOGIN_METHOD_WX = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login_and_reg);
		//((MainApplication) this.getApplication()).addActivity(this);

		activity = this;
		
		mainApplication = (MainApplication) this.getApplication();

		findViewById(R.id.id_textview_wx_login).setOnClickListener(this);
		findViewById(R.id.id_textview_qq_login).setOnClickListener(this);
		findViewById(R.id.id_textview_reg).setOnClickListener(this);
		findViewById(R.id.id_textview_login).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {


		case R.id.id_textview_wx_login: {
//			ToastUtil.toast(R.string.open_soon);
//			weixinLogin();
			weixinLogin();
		}
			break;
		case R.id.id_textview_qq_login: {
//			ToastUtil.toast(R.string.open_soon);
			qqOauthVerify();
		}

			break;
		case R.id.id_textview_reg:{
			RegActivity.gotoActivity(this);
			this.finish();
		}
		break;
		case R.id.id_textview_login:{
			LoginActivity.gotoActivity(this);
			this.finish();
		}
		break;
		} 
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	
		activity = null;
		

	}
	
	private void qqOauthVerify() {
		
		login_method = LOGIN_METHOD_QQ;
		
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "1104885232",
                "c7394704798a158208a74ab60104f0ba");
		qqSsoHandler.addToSocialSDK();
		
		mController.doOauthVerify(this, SHARE_MEDIA.QQ, new UMAuthListener() {



			@Override
			public void onComplete(Bundle arg0, SHARE_MEDIA arg1) {
				// TODO Auto-generated method stub
//				ToastUtil.toast("授权完成");
				if(arg0 != null) {
					 Log.d("TestData","openid = " + arg0.get("openid"));
				}
				final Bundle bundle = arg0;
				mController.getPlatformInfo(LoginAndRegActivity.this, SHARE_MEDIA.QQ, new SocializeListeners.UMDataListener() {

				    @Override
			        public void onComplete(int status, Map<String, Object> info) {
			            if(status == 200 && info != null){
			            	
			            	mInfo = info;
			            	mInfo.put(USER_ID_QQ, bundle.get(USER_ID_QQ).toString());
			            	new LoginAsyncTask().execute(mInfo.get(USER_ID_QQ).toString(), password);
			          
			            }else{
//			            	ToastUtil.toast("发生错误："+status);
			            	changeWaitMsg(getString(R.string.auth_error));
			           }
			        }

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
//						ToastUtil.toast("授权开始");
						changeWaitMsg(getString(R.string.logining));
					}
					

				});
			}

			@Override
			public void onError(SocializeException arg0, SHARE_MEDIA arg1) {
				// TODO Auto-generated method stub
//				ToastUtil.toast("授权错误");
	            changeWaitMsg(getString(R.string.auth_error));
	            waitClose();
			}

			@Override
			public void onStart(SHARE_MEDIA arg0) {
				// TODO Auto-generated method stub
//				ToastUtil.toast("授权开始");
				showWaitCanelable(getString(R.string.auth_begin));
			}
			
			@Override
			public void onCancel(SHARE_MEDIA arg0) {
				// TODO Auto-generated method stub
    	    	changeWaitMsg(getString(R.string.auth_cancel));
    	    	waitClose();
			}
		}); 
	}
	
	private void weixinLogin() {
		
		
		login_method = LOGIN_METHOD_WX;
		
    	String appId = "wx68f0f9afdb191980";
    	String appSecret = "b7d05eb4cb2d3c158a1b00f90bc9f9a0";
    	
    	// 添加微信平台
    	UMWXHandler wxHandler = new UMWXHandler(this, appId, appSecret);
    	wxHandler.addToSocialSDK();
    	
    	mController.doOauthVerify(this, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
    	    @Override
    	    public void onStart(SHARE_MEDIA platform) {
//    	    	ToastUtil.toast("授权开始");
    	    	showWaitCanelable(getString(R.string.auth_begin));
    	    }
    	    @Override
    	    public void onError(SocializeException e, SHARE_MEDIA platform) {
//    	    	ToastUtil.toast("授权错误");
	            changeWaitMsg(getString(R.string.auth_error));
	            waitClose();
    	    }
    	    @Override
    	    public void onComplete(Bundle value, SHARE_MEDIA platform) {
//    	    	ToastUtil.toast("授权完成");
    	    	changeWaitMsg(getString(R.string.auth_complete));
    	        //获取相关授权信息
    	        mController.getPlatformInfo(LoginAndRegActivity.this, SHARE_MEDIA.WEIXIN, new UMDataListener() {
    	        @Override
    	        public void onStart() {
    	        	// Toast.makeText(MainActivity.this, "获取平台数据开始...", Toast.LENGTH_SHORT).show();
    	        }                                              
    	        @Override
    	        public void onComplete(int status, Map<String, Object> info) {
    	            if(status == 200 && info != null){
    	            	
    	            	changeWaitMsg(getString(R.string.logining));
    	            	mInfo = info;
//    	                StringBuilder sb = new StringBuilder();
//    	                Set<String> keys = info.keySet();
//    	                for(String key : keys){
//    	                   sb.append(key+"="+info.get(key).toString()+"\r\n");
//    	                }
//    	                Log.d("TestData",sb.toString());
//    	                ToastUtil.toast(sb.toString());
    	            	new LoginAsyncTask().execute(info.get(USER_ID_WX).toString(), password);
    	            	
    	            }else{
//    	               Log.d("TestData","发生错误："+status);
    	               changeWaitMsg(getString(R.string.auth_error));
    	               waitClose();
    	           }
    	        }
    	        });
    	    }
    	    @Override
    	    public void onCancel(SHARE_MEDIA platform) {
    	    	changeWaitMsg(getString(R.string.auth_cancel));
    	    	waitClose();
    	    }
    	} );
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
	
	private class ForceRegistAsyncTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String loginUrl =  "http://api.ebelt.cn:8001/ebelt/login/registe_pp?phone="+params[0]+"&password="+params[1];
			return HttpUtils.getJson(loginUrl, null, null, HttpUtils.METHOD_GET);
			
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			waitClose();
			
			parseRegisterResult(result);
			
			Log.i("log","result="+result);
		}

	}
	
	private void parseRegisterResult(String info) {
		try {
			JSONObject jsonObject;
			jsonObject = new JSONObject(info);
			int ret = jsonObject.getInt("ret");
			
			String userId = "";
			
			if(ret==0){
				if (login_method == LOGIN_METHOD_QQ) {
					userId   = mInfo.get(USER_ID_QQ).toString();

				} else if (login_method == LOGIN_METHOD_WX) {
					userId   = mInfo.get(USER_ID_WX).toString();
				}
				new LoginAsyncTask().execute(userId, password);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			ToastUtil.toast(R.string.login_fail_phone_number_password_wrong);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void parseRspInfo(String info){
		
		String userId = "";
		String userName = "";
		String userHead = "";
		int    sex    = -1;
		
		try {
			JSONObject jsonObject;
			jsonObject = new JSONObject(info);
			int ret = jsonObject.getInt("ret");
			
			//初始化拉取的第三方消息
			if (login_method == LOGIN_METHOD_QQ) {
				userId   = mInfo.get(USER_ID_QQ).toString();
				userName = mInfo.get(USER_NAME_QQ).toString();
				userHead = mInfo.get(USER_HEADIMG_QQ).toString();
				
				if (mInfo.get(USER_SEX_QQ).toString().equals("男")) {
					sex = 0;
				} else {
					sex = 1;
				}
			} else if (login_method == LOGIN_METHOD_WX) {
				userId   = mInfo.get(USER_ID_WX).toString();
				userName = mInfo.get(USER_NAME_WX).toString();
				userHead = mInfo.get(USER_HEADIMG_WX).toString();
				//微信定义1 为男，0为女，自定义女为1， 男为0 转换一下
				sex		 = 1 - Integer.valueOf(mInfo.get(USER_SEX_WX).toString());
			}
			
			if(ret==0){
				
				ToastUtil.toast(R.string.login_success);
//				mainApplication.scode = jsonObject.getString("scode");
//				JSONObject jsonObjectUser = jsonObject.getJSONObject("uinfo");
//				String userId = jsonObjectUser.getString("u_name");
				
				mainApplication.user = new User(userId);
				UserService userService = new UserService(this);
				if(userService.checkUserIsExist(mainApplication.user.userId)){
					
					Log.i("sino",mainApplication.user.userId+" 存在");
					User user = userService.queryUserInfo(mainApplication.user.userId);
					user.password = password;
					user.userName = userName;
					user.userHead = userHead;
					user.sex      = sex;
					userService.updateUser(user);
					mainApplication.user = user;
				}else{
					Log.i("sino",mainApplication.user.userId+" 不存在");
					mainApplication.user.firstTime = System.currentTimeMillis();
					mainApplication.user.updateTime = mainApplication.user.firstTime;
					mainApplication.user.password = password;
					mainApplication.user.userName = userName;
					mainApplication.user.userHead = userHead;
					mainApplication.user.sex      = sex;
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
				
				//关闭
				waitClose();
				
				if(mainApplication.user.sex==-1){
					
					PersonalinforActivity.gotoActivity(this,PersonalinforActivity.ENTRY_MODE_INIT);
					this.finish();
				}else{
					if(Keeper.getUserHasBindBand(this)){
//						VLBleServiceManager.getInstance().bindService(getApplication());
						MainActivity.gotoActivity(this);
					}else if (E3AKeeper.getInstance().hasBindDevice(this)){
						
						//MainActivity.gotoActivity(this);
						LostOnlyMainActivity.gotoActivity(this);
						
					}else{
						ScanActivity.gotoActivity(this);
						
						//MainActivity.gotoActivity(this);
						
					}
				}
				this.finish();
				
			}else if (ret == LOGIN_ERROR){
				
//				ToastUtil.toast(R.string.login_fail_phone_number_password_wrong);
				new ForceRegistAsyncTask().execute(userId, password);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ToastUtil.toast(R.string.login_fail_phone_number_password_wrong);
			waitClose();
		} catch (Exception e) {
			e.printStackTrace();
			waitClose();
		}
		
		
		
	}
	
	public static void gotoActivity(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, LoginAndRegActivity.class);
		context.startActivity(intent);
	} 
}
