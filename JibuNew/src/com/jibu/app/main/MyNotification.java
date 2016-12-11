package com.jibu.app.main;

/**
 * @author ctc
 * @function ���;�������
 * 
 */
import java.util.Random;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.jibu.app.R;

public class MyNotification {
	Context mContext = null;
	NotificationManager notificationManager = null;
	public MyNotification(Context context) {
		mContext = context;
	}
	

	public void sendRemindNotification(){
		if (null == mContext) return ;
		boolean hasOpenRing = ApplicationSharedPreferences.getHasOpenRing(mContext);
		boolean hasOpenShock= ApplicationSharedPreferences.getHasOpenShock(mContext);
		notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification();
		
		Notification notification_sound_shock = new Notification();
		
		PendingIntent pendingIntent = goAPPIntent(mContext);
		
		notification.icon = R.drawable.ic_launcher;
		notification.tickerText = mContext.getString(R.string.long_sit_remind);
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		if (hasOpenRing) {
			notification_sound_shock.defaults |= Notification.DEFAULT_SOUND ;//Ĭ��ϵͳ����
		}
		if (hasOpenShock) {
			notification_sound_shock.defaults |= Notification.DEFAULT_VIBRATE;// ������
		}
		String[] strs =  mContext.getResources().getStringArray(R.array.long_sit_remind_message);
		
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification_sound_shock.flags |= Notification.FLAG_INSISTENT; // ����һֱ�쵽�û���Ӧ������֪ͨ��һֱ����ֱ���㴥��֪ͨ����ʱ��ͻ�ֹͣ
		notification.setLatestEventInfo(mContext,
				mContext.getString(R.string.long_sit_remind), strs[get_random(strs.length)], pendingIntent);
	
		notificationManager.notify(0, notification);
		notificationManager.notify(1, notification_sound_shock);
		
	    new Thread(new MyThread()).start(); 
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
	
	private int get_random(int max) {
		Random random = new Random();
		return (Math.abs(random.nextInt()) % max);
	}
	
    public class MyThread implements Runnable {  
        @Override  
        public void run() {  
            // TODO Auto-generated method stub  
        		try{
                    Thread.sleep(10000);// �߳���ͣ10�룬��λ����  
                   Log.e("TAG", "cancel by Thread");
                } catch (InterruptedException e) {  
                    // TODO Auto-generated catch block  
                    e.printStackTrace();  
                } finally {
                    if (notificationManager != null) {
                    	notificationManager.cancel(1);
                    }
                }
            
        }  
    }  
}
