package mybleservice;

import java.util.Set;

import android.content.Context;
import android.util.Log;

import com.jibu.app.main.ApplicationSharedPreferences;
import com.jibu.app.main.NoAlarmAreaActivity;
import com.jibu.app.server.AntiLostNotification;

public class FindPhoneNotify {
   private static FindPhoneNotify instance = new FindPhoneNotify();
   
   public static FindPhoneNotify getInstance() {
	   return instance;
   }
   public void phoneNotify(Context context) {
    	AntiLostNotification notification = AntiLostNotification.getInstance(context);
		if (null != notification) {
			notification.setFlag(true);
			notification.sendRemindNotification(true);
		}
    }
   public void stopPhoneNotify(Context context) {
    	AntiLostNotification notification = AntiLostNotification.getInstance(context);
    	notification.stopNotification();
    }

   
   public void phoneIfNotify(Context context) {
   	final Set<String> selectedWifi = ApplicationSharedPreferences.getNoAlarmArea(context);
   	final String wifiInfo = NoAlarmAreaActivity.getConnectWifiSsid(context);
   	final boolean isOpen  =  ApplicationSharedPreferences.getIsOpenNoAlarmArea(context);
   	final boolean isOpenPhoneAlert = ApplicationSharedPreferences.getHasOpenAntiLostRemind(context);
   	if (isOpen && selectedWifi != null && selectedWifi.contains(wifiInfo) || !isOpenPhoneAlert) {
   		Log.e("TAG", "is in no alarm area!");
   		return; 
   	}
   
   	
   	AntiLostNotification notification = AntiLostNotification.getInstance(context);
		if (null != notification) {
			notification.setFlag(true);
			notification.sendRemindNotification(true);
		}
   }
}
