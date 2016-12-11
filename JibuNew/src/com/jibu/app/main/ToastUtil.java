package com.jibu.app.main;


import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

	public static void toast(Context context, String msg) {
		
		if(msg!=null){
			Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
		}
		
		
	}

	public static void toast(Context context, int ResId) {
		Toast.makeText(context, ResId, Toast.LENGTH_SHORT).show();
	}
	
	public static void toast(String msg) {
		
		if(msg!=null){
			Toast.makeText(MainApplication.context, msg, Toast.LENGTH_SHORT).show();
		}
		
		
	}
	
	public static void toast(int ResId) {
		
			Toast.makeText(MainApplication.context, ResId, Toast.LENGTH_SHORT).show();
	
	}
}
