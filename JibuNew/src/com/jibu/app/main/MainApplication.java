package com.jibu.app.main;

import java.util.Vector;
import com.jibu.app.entity.User;
import com.jibu.app.login.LoginAndRegActivity;



import android.app.Activity;
import android.app.Application;

public class MainApplication extends Application {

	public static int serviceVersion = 0;

	public static MainApplication context;


	public User user = null;
	
	public boolean isConnected = false;

	private Vector<Activity> listActivity;
	
	public String scode="";
	
	public int filler_index = 0;
	
	@Override
	public void onCreate() {
		context = this;

		listActivity = new Vector<Activity>();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		
		
	}



	public void addActivity(Activity activity) {
		listActivity.add(activity);
	}

	private void stopServer() {
	}

	public void exit(){
		

		stopServer();
		
	
		
		try {
			if(LoginAndRegActivity.activity!=null){
				LoginAndRegActivity.activity.finish();
			}
			
			closeAllActivity();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}

	
	}
	
	public void exitToLogin(){
		

		stopServer();
		
	
		
		try {
			
			
			closeAllActivity();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//System.exit(0);
		}

	
	}
	
	
	public void closeAllActivity(){
		for (int i = 0; i < listActivity.size(); i++) {
			if (listActivity.elementAt(i) != null)
				listActivity.elementAt(i).finish();
		}
		
	}


}
