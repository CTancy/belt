package com.jibu.app.main;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.jibu.app.R;
import com.umeng.analytics.MobclickAgent;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebBackForwardList;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WaitingActivity extends FragmentActivity {

	private Dialog dialog;

	private AlertDialog alertDialog;
	
	private TextView tipTextView;

	public boolean isActive = true;
	
	private boolean isTimeOut = false;
	
	private Timer timer = null;
	private MyTimerTask timerTaskPatternUnLocker = null;;
	
	public  Dialog createLoadingDialog(Context context, String msg) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.linearlayout_loading_dialog, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v
				.findViewById(R.id.id_linearlayout_loading);// 加载布局
		// main.xml中的ImageView
		ImageView spaceshipImage = (ImageView) v
				.findViewById(R.id.id_imageview_loading);
		tipTextView = (TextView) v
				.findViewById(R.id.id_textview_message);// 提示文字
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.loading_animation);
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		if (msg != null) {
			tipTextView.setText(msg);// 设置加载信息
		} else {
			tipTextView.setVisibility(View.GONE);
		}
		Dialog loadingDialog = new Dialog(context, R.style.LoadingDialog);// 创建自定义样式dialog

		loadingDialog.setCancelable(false);// 不可以用“返回键”取�?
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
		
		
		return loadingDialog;

	}
	
	public Dialog createCanelableLoadingDialog(Context context, String msg) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.linearlayout_loading_dialog, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v
				.findViewById(R.id.id_linearlayout_loading);// 加载布局
		// main.xml中的ImageView
		ImageView spaceshipImage = (ImageView) v
				.findViewById(R.id.id_imageview_loading);
		tipTextView = (TextView) v
				.findViewById(R.id.id_textview_message);// 提示文字
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.loading_animation);
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		if (msg != null) {
			tipTextView.setText(msg);// 设置加载信息
		} else {
			tipTextView.setVisibility(View.GONE);
		}
		Dialog loadingDialog = new Dialog(context, R.style.LoadingDialog);// 创建自定义样式dialog

		
		
		loadingDialog.setCancelable(true);// 可以用�?返回键�?取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));// 设置布局
		
		
		

		
		return loadingDialog;

	}

	public void changeWaitMsg(final String msg){
		Log.i("sion","changeWaitMsg:msg="+msg);
		if(dialog != null && dialog.isShowing()){
			if (msg != null) {
				tipTextView.setText(msg);// 设置加载信息
			} else {
				tipTextView.setVisibility(View.GONE);
			}
		}
	}
	
	/**
	 * 带等待图标的等待�?参数message：需要显示给用户的字符串
	 * */
	public void showWaitCanelable(final String message) {
		WaitingActivity.this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				dialog = createCanelableLoadingDialog(WaitingActivity.this, message);

				dialog.show();
			
			}
		});

	}

	public void showWaitCanelable() {
		WaitingActivity.this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				dialog = createCanelableLoadingDialog(WaitingActivity.this, null);

				dialog.show();
			
			}
		});

	}
	

	/**
	 * 带等待图标的等待�?参数message：需要显示给用户的字符串
	 * */
	public void showWait(final String message) {
		WaitingActivity.this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				dialog = createLoadingDialog(WaitingActivity.this, message);

				dialog.show();
			
			}
		});

	}
	
	public void setWaitMessage(String message){
		if(tipTextView!=null){
			tipTextView.setText(message);
		}
		
	}

	public void showWait() {
		WaitingActivity.this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				dialog = createLoadingDialog(WaitingActivity.this, null);

				dialog.show();
			
			}
		});

	}

	/**
	 * 关闭等待�?
	 * */
	public void waitClose() {
		WaitingActivity.this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
					
				}
			}
		});

	}
	
	public boolean waitIsShowing(){
		
		if(dialog != null && dialog.isShowing()){
			return true;
		}else{
			return false;
		}
			
		
	}

	public void showErrorInfoDialog(final String title, final String content) {

		WaitingActivity.this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// LayoutInflater layoutInflater = (LayoutInflater)
				// getSystemService(LAYOUT_INFLATER_SERVICE);
				// View view = layoutInflater.inflate(R.layout.popup_error_info,
				// null);

				alertDialog = new AlertDialog.Builder(WaitingActivity.this)
						.create();
				Window view = alertDialog.getWindow();
				alertDialog.show();
				alertDialog.setContentView(R.layout.popup_info);

				TextView button_ok = (TextView) view
						.findViewById(R.id.id_textview_popup_ok);

				((TextView) view.findViewById(R.id.id_textview_popup_title))
						.setText(title);

				((TextView) view.findViewById(R.id.id_textview_popup_info))
						.setText(content);

				// alertDialog = new
				// AlertDialog.Builder(WaitingActivity.this).create();

				// alertDialog.setView(view);
				// alertDialog.show();

				class PopupClickListener implements OnClickListener {

					AlertDialog alertDialog;

					public PopupClickListener(AlertDialog alertDialog) {
						this.alertDialog = alertDialog;
					}

					@Override
					public void onClick(View v) {
						switch (v.getId()) {

						case R.id.id_textview_popup_ok:

							if (alertDialog != null) {
								alertDialog.dismiss();
							}
							alertDialog = null;
							break;
						}
					}
				}

				PopupClickListener clicklistener = new PopupClickListener(
						alertDialog);

				button_ok.setOnClickListener(clicklistener);

			}
		});

	}

	public void showErrorInfoDialog(final String title, final String content,final PopUpSelListener popUpSelListener) {

		WaitingActivity.this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// LayoutInflater layoutInflater = (LayoutInflater)
				// getSystemService(LAYOUT_INFLATER_SERVICE);
				// View view = layoutInflater.inflate(R.layout.popup_error_info,
				// null);

				alertDialog = new AlertDialog.Builder(WaitingActivity.this)
						.create();
				Window view = alertDialog.getWindow();
				alertDialog.show();
				alertDialog.setContentView(R.layout.popup_info);

				TextView button_ok = (TextView) view
						.findViewById(R.id.id_textview_popup_ok);

				((TextView) view.findViewById(R.id.id_textview_popup_title))
						.setText(title);

				((TextView) view.findViewById(R.id.id_textview_popup_info))
						.setText(content);

				// alertDialog = new
				// AlertDialog.Builder(WaitingActivity.this).create();

				// alertDialog.setView(view);
				// alertDialog.show();

				class PopupClickListener implements OnClickListener {

					AlertDialog alertDialog;

					public PopupClickListener(AlertDialog alertDialog) {
						this.alertDialog = alertDialog;
					}

					@Override
					public void onClick(View v) {
						switch (v.getId()) {

						case R.id.id_textview_popup_ok:

							if (alertDialog != null) {
								alertDialog.dismiss();
							}
							alertDialog = null;
							popUpSelListener.okButtonPress();
							break;
						}
					}
				}

				PopupClickListener clicklistener = new PopupClickListener(
						alertDialog);

				button_ok.setOnClickListener(clicklistener);

			}
		});

	}
	
	public void showInfoDialog(final String title, final String content) {

		WaitingActivity.this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// LayoutInflater layoutInflater = (LayoutInflater)
				// getSystemService(LAYOUT_INFLATER_SERVICE);
				// View view = layoutInflater.inflate(R.layout.popup_error_info,
				// null);

				alertDialog = new AlertDialog.Builder(WaitingActivity.this)
						.create();
				Window view = alertDialog.getWindow();
				alertDialog.show();
				alertDialog.setContentView(R.layout.popup_info);

				TextView button_ok = (TextView) view
						.findViewById(R.id.id_textview_popup_ok);

				((TextView) view.findViewById(R.id.id_textview_popup_title))
						.setText(title);

				((TextView) view.findViewById(R.id.id_textview_popup_info))
						.setText(content);

				// alertDialog = new
				// AlertDialog.Builder(WaitingActivity.this).create();

				// alertDialog.setView(view);
				// alertDialog.show();

				class PopupClickListener implements OnClickListener {

					AlertDialog alertDialog;

					public PopupClickListener(AlertDialog alertDialog) {
						this.alertDialog = alertDialog;
					}

					@Override
					public void onClick(View v) {
						switch (v.getId()) {

						case R.id.id_textview_popup_ok:

							if (alertDialog != null) {
								alertDialog.dismiss();
							}
							alertDialog = null;
							break;
						}
					}
				}

				PopupClickListener clicklistener = new PopupClickListener(
						alertDialog);

				button_ok.setOnClickListener(clicklistener);

			}
		});

	}

	public void showSelDialog(final String title, final String content,final String textOk,final String textCancel,
			final PopUpSelListener popUpSelListener) {

		WaitingActivity.this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// LayoutInflater layoutInflater = (LayoutInflater)
				// getSystemService(LAYOUT_INFLATER_SERVICE);
				// View view = layoutInflater.inflate(R.layout.popup_error_info,
				// null);

				alertDialog = new AlertDialog.Builder(WaitingActivity.this)
						.create();
				Window view = alertDialog.getWindow();
				alertDialog.show();
				alertDialog.setContentView(R.layout.popup_sel);

				TextView button_ok = (TextView) view
						.findViewById(R.id.id_textview_popup_ok);
				if(textOk!=null){
					button_ok.setText(textOk);
				}
				
				TextView button_cannel = (TextView) view
						.findViewById(R.id.id_textview_popup_cannel);
				if(textCancel!=null){
					button_cannel.setText(textCancel);
				}
				

				((TextView) view.findViewById(R.id.id_textview_popup_title))
						.setText(title);

				((TextView) view.findViewById(R.id.id_textview_popup_info))
						.setText(content);

				// alertDialog = new
				// AlertDialog.Builder(WaitingActivity.this).create();

				// alertDialog.setView(view);
				// alertDialog.show();

				class PopupClickListener implements OnClickListener {

					AlertDialog alertDialog;

					public PopupClickListener(AlertDialog alertDialog) {
						this.alertDialog = alertDialog;
					}

					@Override
					public void onClick(View v) {
						switch (v.getId()) {

						case R.id.id_textview_popup_ok:

							if (alertDialog != null) {
								alertDialog.dismiss();
							}
							alertDialog = null;
							popUpSelListener.okButtonPress();
							break;
						case R.id.id_textview_popup_cannel:

							if (alertDialog != null) {
								alertDialog.dismiss();
							}
							alertDialog = null;
							popUpSelListener.canncelButtonPress();
							break;
						}
					}
				}

				PopupClickListener clicklistener = new PopupClickListener(
						alertDialog);

				button_ok.setOnClickListener(clicklistener);
				button_cannel.setOnClickListener(clicklistener);

			}
		});

	}

	
	@Override
	protected void onStart() {

		super.onStart();

	}

	@Override
	protected void onDestroy() {

		super.onDestroy();

		if(timer!=null){
			timer.cancel();  
            timer.purge(); 
            timer = null;
        }
		if(timerTaskPatternUnLocker!=null){
        	timerTaskPatternUnLocker.cancel();
        	timerTaskPatternUnLocker=null;
        }
	}

	@Override
	protected void onPause() {

		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onStop() {

		super.onStop();
		
		if (!isAppOnForeground()) {  
            //app 进入后台  
               
            isActive = false;// 记录当前已经进入后台  
            isTimeOut = false;
            
            
            if(timer!=null){
				timer.cancel();  
                timer.purge(); 
                timer = null;
            }
			if(timerTaskPatternUnLocker!=null){
            	timerTaskPatternUnLocker.cancel();
            	timerTaskPatternUnLocker=null;
            }
            
			timer = new Timer(); 
        	timerTaskPatternUnLocker = new MyTimerTask();
        	timer.schedule(timerTaskPatternUnLocker, 30*1000);
            
            
            
    }  

	}


	 class MyTimerTask extends TimerTask{  
        @Override  
        public void run() {  
        	
        	isTimeOut = true;
        	if(timer==null){
        		timer.cancel();  
                timer.purge(); 
                timer = null;
        	}
        	
              
        }  
    };  
	@Override
	protected void onResume() {


		super.onResume();
	
		MobclickAgent.onResume(this);
	}

	
	public boolean isAppOnForeground() {  
        // Returns a list of application processes that are running on the  
        // device  
           
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);  
        String packageName = getApplicationContext().getPackageName();  

        List<RunningAppProcessInfo> appProcesses = activityManager  
                        .getRunningAppProcesses();  
        if (appProcesses == null)  
                return false;  

        for (RunningAppProcessInfo appProcess : appProcesses) {  
                // The name of the process that this object is associated with.  
                if (appProcess.processName.equals(packageName)  
                                && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {  
                        return true;  
                }  
        }  

        return false;  
	}  
	
	
	

	
}
