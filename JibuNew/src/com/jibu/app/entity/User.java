package com.jibu.app.entity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import com.jibu.app.database.MoveDataService;

import android.text.format.Time;
import android.util.Log;

public class User {

	public String userId;
	public String userName;
	public String password;
	public int sex;
	public int year;
	public float weight;
	public float height;
	public float waist;
	public int step;
	public long updateTime;
	public long autoUpdateTime;
	public long firstTime;
	
	public String userHead;
	
	public User(String id){
		userId = id;
		userName = "";
		password = "";
		sex=-1;
		year=1984;
		weight=60;
		height=(float)1.70;
		waist=30;
		step=10000;
		updateTime=0;
		
		firstTime=0;
		autoUpdateTime=0;
		userHead = "";
	}
	
	public User(){
		userId = "";
		userName = "";
		password = "";
		sex=-1;
		year=1984;
		weight=60;
		height=(float)1.70;
		waist=30;
		step=10000;
		updateTime=0;
		
		firstTime=0;
		autoUpdateTime=0;
		userHead = "";
	}
	
	public void printUpdateTime(){
		
		if(updateTime==0){
			Log.i("log","无更新时间");
		}else{
			SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd HH:mm:ss");     
			Date    data    =   new    Date(updateTime);//获取当前时间     
			Log.i("log","updateTime:"+formatter.format(data));
		}
		
	}
	
	public void printFirstTime(){
		
		if(firstTime==0){
			Log.i("log","无首次时间");
		}else{
			SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd HH:mm:ss");     
			Date    data    =   new    Date(firstTime);//获取当前时间     
			Log.i("log","firstTime:"+formatter.format(data));
		}
		
	}
	
	public int getUpdateTimeForYear(){
		
		Time time = new Time();
		time.set(updateTime);
		return time.year;
		
	}
	
public int getUpdateTimeForMounth(){
		
		Time time = new Time();
		time.set(updateTime);
		return time.month;
		
	}

public int getUpdateTimeForDay(){
	
	Time time = new Time();
	time.set(updateTime);
	return time.monthDay;
	
}
	

public int getFirstTimeForYear(){
	
	Time time = new Time();
	time.set(firstTime);
	return time.year;
	
}

public int getFirstTimeForMounth(){
	
	Time time = new Time();
	time.set(firstTime);
	return time.month;
	
}

public int getFirstTimeForDay(){

Time time = new Time();
time.set(firstTime);
return time.monthDay;

}



public int getUpdateTimeForTotalDay(){
	
	Time time = new Time();
	time.set(updateTime);
		
		return time.year*10000+time.month*100+time.monthDay;
	
}



public int getFirstTimeForTotalDay(){
	
	Time time = new Time();
	time.set(firstTime);
		
		return time.year*10000+time.month*100+time.monthDay;
	
}


public static boolean isExsitLastMoveData(MoveData moveData,User user){
	
	int currTotalDay = moveData.getTotalDay();
	
	int userFirstTimeTotalDay = user.getFirstTimeForTotalDay();
	
	if(currTotalDay>userFirstTimeTotalDay){
		return true;
	}else{
		return false;
	}
	
	
}

public static boolean isExsitLastMonthMoveData(Vector<MoveData> currMonthMoveDatas,User user){
	
	int currTotalDay = currMonthMoveDatas.firstElement().getTotalDay();
	
	int userFirstTimeTotalDay = user.getFirstTimeForTotalDay();
	
	if(currTotalDay>userFirstTimeTotalDay){
		return true;
	}else{
		return false;
	}
	
	
}

public static boolean isExsitLastWeekMoveData(Vector<MoveData> currWeekMoveDatas,User user){
	
	int currTotalDay = currWeekMoveDatas.firstElement().getTotalDay();
	
	int userFirstTimeTotalDay = user.getFirstTimeForTotalDay();
	
	if(currTotalDay>userFirstTimeTotalDay){
		return true;
	}else{
		return false;
	}
	
	
}


public static MoveData getLastMoveData(MoveData moveData,MoveDataService moveDataService,User user){
	
	Calendar cl = Calendar.getInstance();
	cl.set(moveData.year, moveData.mounth, moveData.day);
	cl.set(Calendar.DATE, moveData.day-1);
	
	
	int year = cl.get(Calendar.YEAR);
	int mounth = cl.get(Calendar.MONTH);
	int day = cl.get(Calendar.DATE);
	
	MoveData lastMoveData = moveDataService.queryMoveDataByUserSpecDay(user.userId, year, mounth, day);
	
	if(lastMoveData==null){
		lastMoveData = new MoveData(user.userId, year, mounth, day);
		moveDataService.insertMoveData(lastMoveData);
	}
	Log.i("log","lastMoveData.day="+lastMoveData.day);
	return lastMoveData;

}
/**
 * 获取距离当天N天前的数据
 * 
 */
public static MoveData getLastServalDayMoveData(MoveData moveData,MoveDataService moveDataService,User user, int lastDays){
	Calendar cl = Calendar.getInstance();
	cl.set(moveData.year, moveData.mounth, moveData.day);
	cl.set(Calendar.DATE, moveData.day - lastDays);
	
	
	int year = cl.get(Calendar.YEAR);
	int mounth = cl.get(Calendar.MONTH);
	int day = cl.get(Calendar.DATE);
	
	MoveData lastMoveData = moveDataService.queryMoveDataByUserSpecDay(user.userId, year, mounth, day);
	
	if(lastMoveData==null){
		lastMoveData = new MoveData(user.userId, year, mounth, day);
		moveDataService.insertMoveData(lastMoveData);
	}
	Log.i("log","lastMoveData.day="+lastMoveData.day);
	return lastMoveData;
}

public static Vector<MoveData> getLastMonthMoveData(Vector<MoveData> currMonthMoveDatas,MoveDataService moveDataService,User user){
	
	Calendar cl = Calendar.getInstance();
	MoveData firstMoveData = currMonthMoveDatas.firstElement();
	cl.set(firstMoveData.year, firstMoveData.mounth, firstMoveData.day);
	cl.set(Calendar.MONTH, firstMoveData.mounth-1);
	
	
	int year = cl.get(Calendar.YEAR);
	int month = cl.get(Calendar.MONTH);
	int day = cl.get(Calendar.DATE);
	int week = cl.get(Calendar.DAY_OF_WEEK);
	
	Vector<MoveData> lastMonthMoveData = moveDataService.queryMoveDataByUserSpecMonth(user.userId, year, month);
	
	if (lastMonthMoveData == null) {
		lastMonthMoveData = new Vector<MoveData>();
	}

	Log.i("log","lastMonthMoveData.size="+lastMonthMoveData.size());
	return lastMonthMoveData;

}


public static Vector<MoveData> getLastWeekMoveData(Vector<MoveData> currWeekMoveDatas,MoveDataService moveDataService,User user){
	
	Calendar cl = Calendar.getInstance();
	MoveData firstMoveData = currWeekMoveDatas.firstElement();
	cl.set(firstMoveData.year, firstMoveData.mounth, firstMoveData.day);
	cl.set(Calendar.DATE, firstMoveData.day-1);
	
	
	int year = cl.get(Calendar.YEAR);
	int month = cl.get(Calendar.MONTH);
	int day = cl.get(Calendar.DATE);
	int week = cl.get(Calendar.DAY_OF_WEEK);
	
	Vector<MoveData> lastWeekMoveData = moveDataService.queryMoveDataByUserSpecWeek(user.userId, year, month,day);
	
	if (lastWeekMoveData == null) {
		lastWeekMoveData = new Vector<MoveData>();
	}
	Log.i("log","lastWeekMoveData.size="+lastWeekMoveData.size());
	return lastWeekMoveData;

}



}
