package com.jibu.app.server;

import java.util.Random;

import com.jibu.app.R;
import com.jibu.app.main.ApplicationSharedPreferences;
import com.jibu.app.main.WelcomeActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;


public class AntiLostNotification {
	
	private final String TAG = "AntiLostNotification";
	public final static String NOTIFICATION_DELETED_ACTION = "notification_deleted_action";
	private static AntiLostNotification instance;
	//防止短时间重复发送防丢提醒
	private boolean flag = true;
	Context mContext = null;
	NotificationManager notificationManager = null;
	String ringFilePath = "";
	Thread thread;
	
	private AntiLostNotification(Context context) {
		mContext = context;
		instance = this;
	}
	

	public void sendRemindNotification(){
		if (null == mContext || !flag) return ;
		
		flag = false;
		ringFilePath = ApplicationSharedPreferences.getRingPath(mContext);
		notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification();
		Notification notification_sound_shock = new Notification();
		PendingIntent pendingIntent = goAPPIntent(mContext);
		notification.icon = R.drawable.ic_launcher;
		notification.tickerText = mContext.getString(R.string.anti_lost);
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		
		Log.e(TAG , "ringFilePath" + ringFilePath);
		String[] strs =  mContext.getResources().getStringArray(R.array.anti_lost_phone_remind_message);
		
		notification.setLatestEventInfo(mContext,
				mContext.getString(R.string.anti_lost_remind),  strs[get_random(strs.length)], pendingIntent);
		
		//声音和震动单独提取
		
		notification_sound_shock.defaults |= Notification.DEFAULT_VIBRATE;// 震动提醒
		if (ringFilePath.equals("") || null == ringFilePath) {
			notification_sound_shock.defaults |= Notification.DEFAULT_SOUND ;//默认系统铃声
		} else {
			notification_sound_shock.sound = Uri.parse(ringFilePath);
		}
		
		notification_sound_shock.flags |= Notification.FLAG_INSISTENT; // 声音一直响到用户相应，就是通知会一直响起，直到你触碰通知栏的时间就会停止
		notification_sound_shock.contentIntent = null;
		notification.deleteIntent = PendingIntent.getBroadcast(mContext, 0, new Intent()
		.setAction(NOTIFICATION_DELETED_ACTION), PendingIntent.FLAG_CANCEL_CURRENT);
		
		notificationManager.notify(0, notification);
		notificationManager.notify(1, notification_sound_shock);
		
		thread = new Thread(new MyThread());
		thread.start();
	}
	
	private PendingIntent goAPPIntent(Context context){
		
		
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setClass(mContext, WelcomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		PendingIntent pendingIntent = PendingIntent.getActivity(
				context, 0, intent, 0);
		
		return pendingIntent;
	}
	
	/**
	 * 
	 * @author ctc
	 * 开个线程20秒后自动取消铃声
	 */
    public class MyThread implements Runnable {  
        @Override  
        public void run() {  
            // TODO Auto-generated method stub  
        		try{
                    Thread.sleep(20000);// 线程暂停20秒，单位毫秒  
                   Log.e("TAG", "cancel by Thread");
                } catch (InterruptedException e) {  
                    // TODO Auto-generated catch block  
                    e.printStackTrace();  
                } finally {
                    if (notificationManager != null) {
                    	notificationManager.cancel(1);
                    }
                    flag = true;
                }
            
        }  
    }  
    public void setFlag(boolean value) {
    	this.flag = value;
    	if(null != thread) {
    		thread.interrupt();
    	}
    }
    
	private int get_random(int max) {
		Random random = new Random();
		return (Math.abs(random.nextInt()) % max);
	}
	
	
    public static synchronized AntiLostNotification getInstance(Context context) {
    	if (null == instance) {
    		return new AntiLostNotification(context);
    	}
    	return instance;
    }

}
