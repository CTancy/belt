package com.jibu.app.main;


import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

public class ApplicationSharedPreferences {


	public static int getLocalVersion(Context context) {
		int version;
		try {
			PackageInfo packageInfo = context.getApplicationContext()
					.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			version = packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			version = 0;
		}

		return version;
	}
	
	
	public static boolean getSystemFirstRun(Context context) {
		
		int lastVersion = getLastVersion(context);
		
	//	AppEnvironment appEnvironment = AppEnvironment.getIntance(context);
		
		int currVersion = getLocalVersion(context);
		
		if(currVersion>lastVersion){
			setLastVersion(context,currVersion);
			return true;
		}else{
			return false;
		}
	}


	
	private final static String PREF_LAST_VERSION = "last_version";
	private final static int PREF_LAST_VERSION_DEFAULT_VALUE = 0;

	public static int getLastVersion(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getInt(PREF_LAST_VERSION, PREF_LAST_VERSION_DEFAULT_VALUE);
	}

	public static void setLastVersion(Context context, int value) {
		Log.i("test", "value=" + value);
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(PREF_LAST_VERSION, value);
		editor.commit();
	}
	

	private final static String PREF_DATABASE_VERSION = "database_version";
	private final static int PREF_DATABASE_VERSION_DEFAULT_VALUE = 1;

	public static int getDataBaseVersion(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getInt(PREF_DATABASE_VERSION, PREF_DATABASE_VERSION_DEFAULT_VALUE);
	}

	public static void setDataBaseVersion(Context context, int value) {
		Log.i("test", "value=" + value);
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(PREF_DATABASE_VERSION, value);
		editor.commit();
	}
	
	
	private final static String PREF_DC_VALUE = "dc_value";
	private final static int PREF_DC_VALUE_DEFAULT_VALUE = 100;

	public static int getDcValue(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getInt(PREF_DC_VALUE, PREF_DC_VALUE_DEFAULT_VALUE);
	}

	public static void setDcValue(Context context, int value) {
		Log.i("test", "value=" + value);
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(PREF_DC_VALUE, value);
		editor.commit();
	}
	
	
	private final static String PREF_REMAIND_STATUS = "remind_status";
	private final static boolean PREF_REMAIND_STATUS_DEFAULT_VALUE = true;

	public static boolean getRemindStatus(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getBoolean(PREF_REMAIND_STATUS, PREF_REMAIND_STATUS_DEFAULT_VALUE);
	}

	public static void setRemindStatus(Context context, boolean value) {
		Log.i("test", "value=" + value);
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(PREF_REMAIND_STATUS, value);
		editor.commit();
	}
	
	private final static String PREF_REMIND_HAS_OPEN_RING = "has_open_ring";
	private final static boolean PREF_REMAIND_HAS_OPEN_RING_DEFAULT_VALUE = false;
	
	public static boolean getHasOpenRing(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getBoolean(PREF_REMIND_HAS_OPEN_RING, PREF_REMAIND_HAS_OPEN_RING_DEFAULT_VALUE);
	}
	
	public static void setHasOpenRing(Context context, boolean value) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(PREF_REMIND_HAS_OPEN_RING, value);
		editor.commit();
	}
	
	private final static String PREF_REMIND_HAS_OPEN_SHOCK = "has_open_shock";
	private final static boolean PREF_REMAIND_HAS_OPEN_SHOCK_DEFAULT_VALUE = false;
	
	public static boolean getHasOpenShock(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getBoolean(PREF_REMIND_HAS_OPEN_SHOCK, PREF_REMAIND_HAS_OPEN_SHOCK_DEFAULT_VALUE);
	}
	
	public static void setHasOpenShock(Context context, boolean value) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(PREF_REMIND_HAS_OPEN_SHOCK, value);
		editor.commit();
	}
	
	private final static String PREF_REMIND_BEGIN_TIME = "begin_time";
	private final static long PREF_REMAIND_BEGIN_TIME_DEFAULT_VALUE = 9 * 3600;
	
	public static long getBeginTime(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getLong(PREF_REMIND_BEGIN_TIME, PREF_REMAIND_BEGIN_TIME_DEFAULT_VALUE);
	}
	
	public static void setBeginTime(Context context, long value) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putLong(PREF_REMIND_BEGIN_TIME, value);
		editor.commit();
	}
	
	private final static String PREF_REMIND_END_TIME = "end_time";
	private final static long PREF_REMAIND_END_TIME_DEFAULT_VALUE = 22 * 3600;
	
	public static long getEndTime(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getLong(PREF_REMIND_END_TIME, PREF_REMAIND_END_TIME_DEFAULT_VALUE);
	}
	
	public static void setEndTime(Context context, long value) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putLong(PREF_REMIND_END_TIME, value);
		editor.commit();
	}
	
	private final static String PREF_REMIND_TIME_INTERVAL = "time_interval";
	private final static long PREF_REMAIND_TIME_INTERVAL_DEFAULT_VALUE = 3600;
	
	public static long getTimeInterval(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getLong(PREF_REMIND_TIME_INTERVAL, PREF_REMAIND_TIME_INTERVAL_DEFAULT_VALUE);
	}
	
	public static void setTimeInterval(Context context, long value) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putLong(PREF_REMIND_TIME_INTERVAL, value);
		editor.commit();
	}
	
	//防丢提醒
	private final static String PREF_HAS_OPEN_ANTILOST_REMIND = "antilost_remind";
	private final static boolean PREF_HAS_OPEN_ANTILOST_REMIND_DEFAULT_VALUE = false;
	
	public static boolean getHasOpenAntiLostRemind(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getBoolean(PREF_HAS_OPEN_ANTILOST_REMIND, PREF_HAS_OPEN_ANTILOST_REMIND_DEFAULT_VALUE);
	}
	
	public static void setHasOpenAntiLostRemind(Context context, boolean value) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(PREF_HAS_OPEN_ANTILOST_REMIND, value);
		editor.commit();
	}
	
	//防丢皮带提醒
	private final static String PREF_HAS_OPEN_BELT_REMIND = "belt_remind";
	private final static boolean PREF_HAS_OPEN_BELT_REMIND_DEFAULT_VALUE = false;
	
	public static boolean getHasOpenBeltRemind(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getBoolean(PREF_HAS_OPEN_BELT_REMIND, PREF_HAS_OPEN_BELT_REMIND_DEFAULT_VALUE);
	}
	
	public static void setHasOpenBeltRemind(Context context, boolean value) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(PREF_HAS_OPEN_BELT_REMIND, value);
		editor.commit();
	}
	
	
	//防丢RSSI值保存
	private final static String PREF_RSSI_VALUE = "rssi_value";
	private final static int PREF_RSSI_VALUE_DEFAULT_VALUE = 90;

	public static int getRSSIValue(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getInt(PREF_RSSI_VALUE, PREF_RSSI_VALUE_DEFAULT_VALUE);
	}

	public static void setRSSIValue(Context context, int value) {
		Log.i("test", "value=" + value);
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(PREF_RSSI_VALUE, value);
		editor.commit();
	}
	
	//手机铃声地址保存
	private final static String PREF_RING_PATH = "ring_path";
	public final static String PREF_RING_PAHT_DEFAULT_PATH = "Defalt";
	
	public static String getRingPath(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getString(PREF_RING_PATH, PREF_RING_PAHT_DEFAULT_PATH);
	}
	public static void setRingPath(Context context, String value) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(PREF_RING_PATH, value);
		editor.commit();
	}
	
	//暂时保存头像数据
	private final static String PREF_USERHEAD_PATH = "userhead_path";
	private final static String PREF_USERHEAD_PATH_DEFFAULT_PATH = "";
	
	public static String getUserHeadPath(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getString(PREF_USERHEAD_PATH, PREF_USERHEAD_PATH_DEFFAULT_PATH);
	}
	public static void setUserHeadPath(Context context, String value) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(PREF_USERHEAD_PATH, value);
		editor.commit();
	}
	
	//保存版本信息, 0为有版本更新， 1为已是最新版
	private final static String PREF_APP_VERSION = "app_version";
	private final static int PREF_APP_VERSION_DEFALULT_VALUE = 1;
	
	public static int getAPPVersion(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getInt(PREF_APP_VERSION, PREF_APP_VERSION_DEFALULT_VALUE);
	}
	
	public static void setAPPVersion(Context context, int value) {
//		if (context == null) return; 
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(PREF_APP_VERSION, value);
		editor.commit();
	}
	
	//保存最近一次的登录帐号
	private final static String PREF_LAST_USER = "user_name";
	private final static String PREF_LAST_USER_DEFFAULT_USER = "";
	
	public static String getLastUser(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getString(PREF_LAST_USER, PREF_LAST_USER_DEFFAULT_USER);
	}
	public static void setLastUser(Context context, String value) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(PREF_LAST_USER, value);
		editor.commit();
	}
	
	private final static String PREF_IS_OPEN_NO_ALARM = "is_open_no_alarm";
	private final static boolean PREF_IS_OPEN_NO_ALARM_DEFFAULT_VALUE = false;
	
	public static boolean getIsOpenNoAlarmArea(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		
		return preferences.getBoolean(PREF_IS_OPEN_NO_ALARM, PREF_IS_OPEN_NO_ALARM_DEFFAULT_VALUE);
	}
	
	public static void setIsOpenNoAlarmArea(Context context, boolean value) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(PREF_IS_OPEN_NO_ALARM, value);
		editor.commit();
	}
	
	private final static String PREF_NO_ALARM_AREA = "no_alarm_area";
	private final static Set<String> PREF_NO_ALARM_AREA_DEFFAULT_VALUE = null;
	
	public static Set<String> getNoAlarmArea(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		
		return preferences.getStringSet(PREF_NO_ALARM_AREA, PREF_NO_ALARM_AREA_DEFFAULT_VALUE);
	}
	
	public static void setNoAlarmArea(Context context, Set<String> value) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putStringSet(PREF_NO_ALARM_AREA, value);
		editor.commit();
	}
	
	private final static String PREF_IS_CEJU_ING = "is_ceju_flag";
	private final static boolean PREF_IS_CEJU_ING_DEFALUT_VALUE = false;
	
	public static boolean getIsCejuIng(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getBoolean(PREF_IS_CEJU_ING, PREF_IS_CEJU_ING_DEFALUT_VALUE);
	}
	
	public static void setIsCejuIng(Context context, boolean value) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(PREF_IS_CEJU_ING, value);
		editor.commit();
	}
	
	private final static String PREF_CEJU_STEP = "ceju_step";
	private final static int PREF_CEJU_STEP_DEFALUT_VALUE = 0;
	
	public static int getCejuStep(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getInt(PREF_CEJU_STEP, PREF_CEJU_STEP_DEFALUT_VALUE);
	}
	
	public static void setCejuStep(Context context, int value) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(PREF_CEJU_STEP, value);
		editor.commit();
	}
	
	private final static String PREF_CEJU_TIME = "ceju_time";
	private final static long PREF_CEJU_TIME_DEFALUT_VALUE = 0;
	
	public static long getCejuTime(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getLong(PREF_CEJU_TIME, PREF_CEJU_TIME_DEFALUT_VALUE);
	}
	
	public static void setCejuTime(Context context, long value) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putLong(PREF_CEJU_TIME, value);
		editor.commit();
	}
	
	//保存最近一次的登录帐号
	private final static String PREF_LAST_UPDATE_TIME = "update_time";
	private final static String PREF_LAST_UPDATE_TIME_DEFFAULT_VALUE = "";
	
	public static String getLastUpdateTime(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getString(PREF_LAST_UPDATE_TIME, PREF_LAST_UPDATE_TIME_DEFFAULT_VALUE);
	}
	public static void setLastUpdateTime(Context context, String value) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(PREF_LAST_UPDATE_TIME, value);
		editor.commit();
	}
}