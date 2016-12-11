package com.jibu.app.database;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import com.jibu.app.entity.MoveData;
import com.jibu.app.main.ApplicationSharedPreferences;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.Time;
import android.util.Log;
import android.view.ViewDebug.IntToString;

public class MoveDataService {

	
	/*插入数据的模式*/
	private static final int INSERT_MODE_NO_UPDATE = 0x22;
	
	public static final int MOVE_DATA_DB_VERSION = 5;
	private SQLiteHelper mdbHelper;

	private final String userId = "userId";
	private final String year = "year";
	private final String mounth = "mounth";
	private final String day = "day";
	
	private final String time_0= "time_0";
	private final String time_0_5= "time_0_5";
	private final String time_1= "time_1";
	private final String time_1_5= "time_1_5";
	private final String time_2= "time_2";
	private final String time_2_5= "time_2_5";
	private final String time_3= "time_3";
	private final String time_3_5= "time_3_5";
	private final String time_4= "time_4";
	private final String time_4_5= "time_4_5";
	private final String time_5= "time_5";
	private final String time_5_5= "time_5_5";
	private final String time_6= "time_6";
	private final String time_6_5= "time_6_5";
	private final String time_7= "time_7";
	private final String time_7_5= "time_7_5";
	private final String time_8= "time_8";
	private final String time_8_5= "time_8_5";
	private final String time_9= "time_9";
	private final String time_9_5= "time_9_5";
	private final String time_10= "time_10";
	private final String time_10_5= "time_10_5";
	private final String time_11= "time_11";
	private final String time_11_5= "time_11_5";
	private final String time_12= "time_12";
	private final String time_12_5= "time_12_5";
	private final String time_13= "time_13";
	private final String time_13_5= "time_13_5";
	private final String time_14= "time_14";
	private final String time_14_5= "time_14_5";
	private final String time_15= "time_15";
	private final String time_15_5= "time_15_5";
	private final String time_16= "time_16";
	private final String time_16_5= "time_16_5";
	private final String time_17= "time_17";
	private final String time_17_5= "time_17_5";
	private final String time_18= "time_18";
	private final String time_18_5= "time_18_5";
	private final String time_19= "time_19";
	private final String time_19_5= "time_19_5";
	private final String time_20= "time_20";
	private final String time_20_5= "time_20_5";
	private final String time_21= "time_21";
	private final String time_21_5= "time_21_5";
	private final String time_22= "time_22";
	private final String time_22_5= "time_22_5";
	private final String time_23= "time_23";
	private final String time_23_5= "time_23_5";
	
	private final String todayTarget = "todayTarget";
	
	private final String totalday= "totalday";

	public static final String TABLE_NAME = "moveData";

	private final String DABABASE_NAME = "jibu.db";
	private final String TABLE_CREATED = "create table " + TABLE_NAME + " ("
			+ "userId varchar,year int,mounth int,day int" +
			",time_0 int,time_0_5 int,time_1 int,time_1_5 int,time_2 int,time_2_5 int" +
			",time_3 int,time_3_5 int,time_4 int,time_4_5 int,time_5 int,time_5_5 int" +
			",time_6 int,time_6_5 int,time_7 int,time_7_5 int,time_8 int,time_8_5 int" +
			",time_9 int,time_9_5 int,time_10 int,time_10_5 int,time_11 int,time_11_5 int" +
			",time_12 int,time_12_5 int,time_13 int,time_13_5 int,time_14 int,time_14_5 int" +
			",time_15 int,time_15_5 int,time_16 int,time_16_5 int,time_17 int,time_17_5 int" +
			",time_18 int,time_18_5 int,time_19 int,time_19_5 int,time_20 int,time_20_5 int" +
			",time_21 int,time_21_5 int,time_22 int,time_22_5 int,time_23 int,time_23_5 int" +
			",totalday int,primary key(userId,year,mounth,day));";

	public MoveDataService(Context context) {
		super();

		int version = ApplicationSharedPreferences.getDataBaseVersion(context);

		this.mdbHelper = new SQLiteHelper(context, DABABASE_NAME, version,
				TABLE_NAME, TABLE_CREATED);

	}


	public synchronized boolean checkUserIsExist(String id){
		SQLiteDatabase db = null;

		boolean ret = false;

		if (this.mdbHelper != null) {
			db = mdbHelper.getWritableDatabase();
		}
		
		Cursor cursor = db.query(TABLE_NAME, null, userId+"=?",
				new String[] { id }, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			ret = true;
		}else{
			ret = false;
		}
		
		return ret;
	}
	
	
	
	public synchronized int qureyAllMoveDataSizeByUser(String id) {
		SQLiteDatabase db = null;

		int ret ;

		if (this.mdbHelper != null) {
			db = mdbHelper.getWritableDatabase();
		}

		
		Cursor cursor = db.query(TABLE_NAME, null, userId+"=?",
				new String[] { id }, null, null, null);
		
		ret = cursor.getCount();
		
		cursor.close();

		db.close();

		return ret;
	}
	
	public synchronized int qureyAllDaysByUser(String id) {
		SQLiteDatabase db = null;


		if (this.mdbHelper != null) {
			db = mdbHelper.getWritableDatabase();
		}
		
		String orderBy = totalday + " asc";
		
		Cursor cursor = db.query(TABLE_NAME, null, userId+"=?",
				new String[] { id }, null, null, orderBy);
		int y1 = 0;
		int m1 = 0;
		int d1 = 0;
		int y2 = 0;
		int m2 = 0;
		int d2 = 0;
		
		if (cursor != null && cursor.moveToFirst()) {
			y1 = cursor.getInt(cursor.getColumnIndex(year));
			m1 = cursor.getInt(cursor.getColumnIndex(mounth));
			d1 = cursor.getInt(cursor.getColumnIndex(day));
		}
		
		if (cursor != null && cursor.moveToLast()) {
			y2 = cursor.getInt(cursor.getColumnIndex(year));
			m2 = cursor.getInt(cursor.getColumnIndex(mounth));
			d2 = cursor.getInt(cursor.getColumnIndex(day));
		}
		
		return daysBetween(y1, m1, d1, y2, m2, d2);
	}
	
	
	/**
	 * 
	 * @param y1 开始年份
	 * @param m1
	 * @param d1
	 * @param y2 结束年份
	 * @param m2
	 * @param d2
	 * @return  ymd2 - ymd1 的天数
	 */
	
	public int daysBetween(int y1, int m1, int d1, int y2, int m2, int d2) {
		Calendar cl = Calendar.getInstance();
		cl.set(y1, m1, d1);
		long time1 = cl.getTimeInMillis();
		cl.set(y2, m2, d2);
		long time2 = cl.getTimeInMillis();
		long days = (time2-time1)/(1000*3600*24) + 1;
		
		return (int)days;
	}
	
	public synchronized MoveData qureyEarliestMoveDataByUser(String id) {
		SQLiteDatabase db = null;

		MoveData moveData= null;

		if (this.mdbHelper != null) {
			db = mdbHelper.getWritableDatabase();
		}

		String orderBy = totalday+" asc";
		
		Cursor cursor = db.query(TABLE_NAME, null, userId+"=?",
				new String[] { id }, null, null, orderBy);
		
		if (cursor != null && cursor.moveToFirst()) {
			
			//ret = new Vector<MoveData>();
			
			moveData = new MoveData();
			
			moveData.userId = cursor.getString(cursor.getColumnIndex(userId));
			moveData.year = cursor.getInt(cursor.getColumnIndex(year));
			moveData.mounth = cursor.getInt(cursor.getColumnIndex(mounth));
			moveData.day = cursor.getInt(cursor.getColumnIndex(day));
			moveData.time_0 = cursor.getInt(cursor.getColumnIndex(time_0));
			moveData.time_0_5 = cursor.getInt(cursor.getColumnIndex(time_0_5));
			moveData.time_1 = cursor.getInt(cursor.getColumnIndex(time_1));
			moveData.time_1_5 = cursor.getInt(cursor.getColumnIndex(time_1_5));
			moveData.time_2 = cursor.getInt(cursor.getColumnIndex(time_2));
			moveData.time_2_5 = cursor.getInt(cursor.getColumnIndex(time_2_5));
			moveData.time_3 = cursor.getInt(cursor.getColumnIndex(time_3));
			moveData.time_3_5 = cursor.getInt(cursor.getColumnIndex(time_3_5));
			moveData.time_4 = cursor.getInt(cursor.getColumnIndex(time_4));
			moveData.time_4_5 = cursor.getInt(cursor.getColumnIndex(time_4_5));
			moveData.time_5 = cursor.getInt(cursor.getColumnIndex(time_5));
			moveData.time_5_5 = cursor.getInt(cursor.getColumnIndex(time_5_5));
			moveData.time_6 = cursor.getInt(cursor.getColumnIndex(time_6));
			moveData.time_6_5 = cursor.getInt(cursor.getColumnIndex(time_6_5));
			moveData.time_7 = cursor.getInt(cursor.getColumnIndex(time_7));
			moveData.time_7_5 = cursor.getInt(cursor.getColumnIndex(time_7_5));
			moveData.time_8 = cursor.getInt(cursor.getColumnIndex(time_8));
			moveData.time_8_5 = cursor.getInt(cursor.getColumnIndex(time_8_5));
			moveData.time_9 = cursor.getInt(cursor.getColumnIndex(time_9));
			moveData.time_9_5 = cursor.getInt(cursor.getColumnIndex(time_9_5));
			moveData.time_10 = cursor.getInt(cursor.getColumnIndex(time_10));
			moveData.time_10_5 = cursor.getInt(cursor.getColumnIndex(time_10_5));
			moveData.time_11 = cursor.getInt(cursor.getColumnIndex(time_11));
			moveData.time_11_5 = cursor.getInt(cursor.getColumnIndex(time_11_5));
			moveData.time_12 = cursor.getInt(cursor.getColumnIndex(time_12));
			moveData.time_12_5 = cursor.getInt(cursor.getColumnIndex(time_12_5));
			moveData.time_13 = cursor.getInt(cursor.getColumnIndex(time_13));
			moveData.time_13_5 = cursor.getInt(cursor.getColumnIndex(time_13_5));
			moveData.time_14 = cursor.getInt(cursor.getColumnIndex(time_14));
			moveData.time_14_5 = cursor.getInt(cursor.getColumnIndex(time_14_5));
			moveData.time_15 = cursor.getInt(cursor.getColumnIndex(time_15));
			moveData.time_15_5 = cursor.getInt(cursor.getColumnIndex(time_15_5));
			moveData.time_16 = cursor.getInt(cursor.getColumnIndex(time_16));
			moveData.time_16_5 = cursor.getInt(cursor.getColumnIndex(time_16_5));
			moveData.time_17 = cursor.getInt(cursor.getColumnIndex(time_17));
			moveData.time_17_5 = cursor.getInt(cursor.getColumnIndex(time_17_5));
			moveData.time_18 = cursor.getInt(cursor.getColumnIndex(time_18));
			moveData.time_18_5 = cursor.getInt(cursor.getColumnIndex(time_18_5));
			moveData.time_19 = cursor.getInt(cursor.getColumnIndex(time_19));
			moveData.time_19_5 = cursor.getInt(cursor.getColumnIndex(time_19_5));
			moveData.time_20 = cursor.getInt(cursor.getColumnIndex(time_20));
			moveData.time_20_5 = cursor.getInt(cursor.getColumnIndex(time_20_5));
			moveData.time_21 = cursor.getInt(cursor.getColumnIndex(time_21));
			moveData.time_21_5 = cursor.getInt(cursor.getColumnIndex(time_21_5));
			moveData.time_22 = cursor.getInt(cursor.getColumnIndex(time_22));
			moveData.time_22_5 = cursor.getInt(cursor.getColumnIndex(time_22_5));
			moveData.time_23 = cursor.getInt(cursor.getColumnIndex(time_23));
			moveData.time_23_5 = cursor.getInt(cursor.getColumnIndex(time_23_5));
			moveData.todayTarget = cursor.getInt(cursor.getColumnIndex(todayTarget));
			
			
		}

		cursor.close();

		db.close();

		return moveData;
	}
	
	public synchronized MoveData qureyLastestMoveDataByUser(String id) {
		SQLiteDatabase db = null;

		MoveData moveData= null;

		if (this.mdbHelper != null) {
			db = mdbHelper.getWritableDatabase();
		}

		String orderBy = totalday+" desc";
		
		Cursor cursor = db.query(TABLE_NAME, null, userId+"=?",
				new String[] { id }, null, null, orderBy);
		
		if (cursor != null && cursor.moveToFirst()) {
			
			//ret = new Vector<MoveData>();
			
			moveData = new MoveData();
			
			moveData.userId = cursor.getString(cursor.getColumnIndex(userId));
			moveData.year = cursor.getInt(cursor.getColumnIndex(year));
			moveData.mounth = cursor.getInt(cursor.getColumnIndex(mounth));
			moveData.day = cursor.getInt(cursor.getColumnIndex(day));
			moveData.time_0 = cursor.getInt(cursor.getColumnIndex(time_0));
			moveData.time_0_5 = cursor.getInt(cursor.getColumnIndex(time_0_5));
			moveData.time_1 = cursor.getInt(cursor.getColumnIndex(time_1));
			moveData.time_1_5 = cursor.getInt(cursor.getColumnIndex(time_1_5));
			moveData.time_2 = cursor.getInt(cursor.getColumnIndex(time_2));
			moveData.time_2_5 = cursor.getInt(cursor.getColumnIndex(time_2_5));
			moveData.time_3 = cursor.getInt(cursor.getColumnIndex(time_3));
			moveData.time_3_5 = cursor.getInt(cursor.getColumnIndex(time_3_5));
			moveData.time_4 = cursor.getInt(cursor.getColumnIndex(time_4));
			moveData.time_4_5 = cursor.getInt(cursor.getColumnIndex(time_4_5));
			moveData.time_5 = cursor.getInt(cursor.getColumnIndex(time_5));
			moveData.time_5_5 = cursor.getInt(cursor.getColumnIndex(time_5_5));
			moveData.time_6 = cursor.getInt(cursor.getColumnIndex(time_6));
			moveData.time_6_5 = cursor.getInt(cursor.getColumnIndex(time_6_5));
			moveData.time_7 = cursor.getInt(cursor.getColumnIndex(time_7));
			moveData.time_7_5 = cursor.getInt(cursor.getColumnIndex(time_7_5));
			moveData.time_8 = cursor.getInt(cursor.getColumnIndex(time_8));
			moveData.time_8_5 = cursor.getInt(cursor.getColumnIndex(time_8_5));
			moveData.time_9 = cursor.getInt(cursor.getColumnIndex(time_9));
			moveData.time_9_5 = cursor.getInt(cursor.getColumnIndex(time_9_5));
			moveData.time_10 = cursor.getInt(cursor.getColumnIndex(time_10));
			moveData.time_10_5 = cursor.getInt(cursor.getColumnIndex(time_10_5));
			moveData.time_11 = cursor.getInt(cursor.getColumnIndex(time_11));
			moveData.time_11_5 = cursor.getInt(cursor.getColumnIndex(time_11_5));
			moveData.time_12 = cursor.getInt(cursor.getColumnIndex(time_12));
			moveData.time_12_5 = cursor.getInt(cursor.getColumnIndex(time_12_5));
			moveData.time_13 = cursor.getInt(cursor.getColumnIndex(time_13));
			moveData.time_13_5 = cursor.getInt(cursor.getColumnIndex(time_13_5));
			moveData.time_14 = cursor.getInt(cursor.getColumnIndex(time_14));
			moveData.time_14_5 = cursor.getInt(cursor.getColumnIndex(time_14_5));
			moveData.time_15 = cursor.getInt(cursor.getColumnIndex(time_15));
			moveData.time_15_5 = cursor.getInt(cursor.getColumnIndex(time_15_5));
			moveData.time_16 = cursor.getInt(cursor.getColumnIndex(time_16));
			moveData.time_16_5 = cursor.getInt(cursor.getColumnIndex(time_16_5));
			moveData.time_17 = cursor.getInt(cursor.getColumnIndex(time_17));
			moveData.time_17_5 = cursor.getInt(cursor.getColumnIndex(time_17_5));
			moveData.time_18 = cursor.getInt(cursor.getColumnIndex(time_18));
			moveData.time_18_5 = cursor.getInt(cursor.getColumnIndex(time_18_5));
			moveData.time_19 = cursor.getInt(cursor.getColumnIndex(time_19));
			moveData.time_19_5 = cursor.getInt(cursor.getColumnIndex(time_19_5));
			moveData.time_20 = cursor.getInt(cursor.getColumnIndex(time_20));
			moveData.time_20_5 = cursor.getInt(cursor.getColumnIndex(time_20_5));
			moveData.time_21 = cursor.getInt(cursor.getColumnIndex(time_21));
			moveData.time_21_5 = cursor.getInt(cursor.getColumnIndex(time_21_5));
			moveData.time_22 = cursor.getInt(cursor.getColumnIndex(time_22));
			moveData.time_22_5 = cursor.getInt(cursor.getColumnIndex(time_22_5));
			moveData.time_23 = cursor.getInt(cursor.getColumnIndex(time_23));
			moveData.time_23_5 = cursor.getInt(cursor.getColumnIndex(time_23_5));
			moveData.todayTarget = cursor.getInt(cursor.getColumnIndex(todayTarget));
			
			
		}

		cursor.close();

		db.close();

		return moveData;
	}
	public synchronized Vector<MoveData> queryAllMoveDataByUser(String id) throws Exception{

		SQLiteDatabase db = null;

		Vector<MoveData> ret = null;

		if (this.mdbHelper != null) {
			db = mdbHelper.getWritableDatabase();
		}

		String orderBy = totalday+" desc";
		
		Cursor cursor = db.query(TABLE_NAME, null, userId+"=?",
				new String[] { id }, null, null, orderBy);
		
		if (cursor != null && cursor.moveToFirst()) {
			
			ret = new Vector<MoveData>();
			
			MoveData moveData = new MoveData();
			
			moveData.userId = cursor.getString(cursor.getColumnIndex(userId));
			moveData.year = cursor.getInt(cursor.getColumnIndex(year));
			moveData.mounth = cursor.getInt(cursor.getColumnIndex(mounth));
			moveData.day = cursor.getInt(cursor.getColumnIndex(day));
			moveData.time_0 = cursor.getInt(cursor.getColumnIndex(time_0));
			moveData.time_0_5 = cursor.getInt(cursor.getColumnIndex(time_0_5));
			moveData.time_1 = cursor.getInt(cursor.getColumnIndex(time_1));
			moveData.time_1_5 = cursor.getInt(cursor.getColumnIndex(time_1_5));
			moveData.time_2 = cursor.getInt(cursor.getColumnIndex(time_2));
			moveData.time_2_5 = cursor.getInt(cursor.getColumnIndex(time_2_5));
			moveData.time_3 = cursor.getInt(cursor.getColumnIndex(time_3));
			moveData.time_3_5 = cursor.getInt(cursor.getColumnIndex(time_3_5));
			moveData.time_4 = cursor.getInt(cursor.getColumnIndex(time_4));
			moveData.time_4_5 = cursor.getInt(cursor.getColumnIndex(time_4_5));
			moveData.time_5 = cursor.getInt(cursor.getColumnIndex(time_5));
			moveData.time_5_5 = cursor.getInt(cursor.getColumnIndex(time_5_5));
			moveData.time_6 = cursor.getInt(cursor.getColumnIndex(time_6));
			moveData.time_6_5 = cursor.getInt(cursor.getColumnIndex(time_6_5));
			moveData.time_7 = cursor.getInt(cursor.getColumnIndex(time_7));
			moveData.time_7_5 = cursor.getInt(cursor.getColumnIndex(time_7_5));
			moveData.time_8 = cursor.getInt(cursor.getColumnIndex(time_8));
			moveData.time_8_5 = cursor.getInt(cursor.getColumnIndex(time_8_5));
			moveData.time_9 = cursor.getInt(cursor.getColumnIndex(time_9));
			moveData.time_9_5 = cursor.getInt(cursor.getColumnIndex(time_9_5));
			moveData.time_10 = cursor.getInt(cursor.getColumnIndex(time_10));
			moveData.time_10_5 = cursor.getInt(cursor.getColumnIndex(time_10_5));
			moveData.time_11 = cursor.getInt(cursor.getColumnIndex(time_11));
			moveData.time_11_5 = cursor.getInt(cursor.getColumnIndex(time_11_5));
			moveData.time_12 = cursor.getInt(cursor.getColumnIndex(time_12));
			moveData.time_12_5 = cursor.getInt(cursor.getColumnIndex(time_12_5));
			moveData.time_13 = cursor.getInt(cursor.getColumnIndex(time_13));
			moveData.time_13_5 = cursor.getInt(cursor.getColumnIndex(time_13_5));
			moveData.time_14 = cursor.getInt(cursor.getColumnIndex(time_14));
			moveData.time_14_5 = cursor.getInt(cursor.getColumnIndex(time_14_5));
			moveData.time_15 = cursor.getInt(cursor.getColumnIndex(time_15));
			moveData.time_15_5 = cursor.getInt(cursor.getColumnIndex(time_15_5));
			moveData.time_16 = cursor.getInt(cursor.getColumnIndex(time_16));
			moveData.time_16_5 = cursor.getInt(cursor.getColumnIndex(time_16_5));
			moveData.time_17 = cursor.getInt(cursor.getColumnIndex(time_17));
			moveData.time_17_5 = cursor.getInt(cursor.getColumnIndex(time_17_5));
			moveData.time_18 = cursor.getInt(cursor.getColumnIndex(time_18));
			moveData.time_18_5 = cursor.getInt(cursor.getColumnIndex(time_18_5));
			moveData.time_19 = cursor.getInt(cursor.getColumnIndex(time_19));
			moveData.time_19_5 = cursor.getInt(cursor.getColumnIndex(time_19_5));
			moveData.time_20 = cursor.getInt(cursor.getColumnIndex(time_20));
			moveData.time_20_5 = cursor.getInt(cursor.getColumnIndex(time_20_5));
			moveData.time_21 = cursor.getInt(cursor.getColumnIndex(time_21));
			moveData.time_21_5 = cursor.getInt(cursor.getColumnIndex(time_21_5));
			moveData.time_22 = cursor.getInt(cursor.getColumnIndex(time_22));
			moveData.time_22_5 = cursor.getInt(cursor.getColumnIndex(time_22_5));
			moveData.time_23 = cursor.getInt(cursor.getColumnIndex(time_23));
			moveData.time_23_5 = cursor.getInt(cursor.getColumnIndex(time_23_5));
			moveData.todayTarget = cursor.getInt(cursor.getColumnIndex(todayTarget));
			
			ret.addElement(moveData);
			
			while(cursor.moveToNext()){
				
				
				
				
				MoveData moveData2 = new MoveData();
				
				moveData2.userId = cursor.getString(cursor.getColumnIndex(userId));
				moveData2.year = cursor.getInt(cursor.getColumnIndex(year));
				moveData2.mounth = cursor.getInt(cursor.getColumnIndex(mounth));
				moveData2.day = cursor.getInt(cursor.getColumnIndex(day));
				moveData2.time_0 = cursor.getInt(cursor.getColumnIndex(time_0));
				moveData2.time_0_5 = cursor.getInt(cursor.getColumnIndex(time_0_5));
				moveData2.time_1 = cursor.getInt(cursor.getColumnIndex(time_1));
				moveData2.time_1_5 = cursor.getInt(cursor.getColumnIndex(time_1_5));
				moveData2.time_2 = cursor.getInt(cursor.getColumnIndex(time_2));
				moveData2.time_2_5 = cursor.getInt(cursor.getColumnIndex(time_2_5));
				moveData2.time_3 = cursor.getInt(cursor.getColumnIndex(time_3));
				moveData2.time_3_5 = cursor.getInt(cursor.getColumnIndex(time_3_5));
				moveData2.time_4 = cursor.getInt(cursor.getColumnIndex(time_4));
				moveData2.time_4_5 = cursor.getInt(cursor.getColumnIndex(time_4_5));
				moveData2.time_5 = cursor.getInt(cursor.getColumnIndex(time_5));
				moveData2.time_5_5 = cursor.getInt(cursor.getColumnIndex(time_5_5));
				moveData2.time_6 = cursor.getInt(cursor.getColumnIndex(time_6));
				moveData2.time_6_5 = cursor.getInt(cursor.getColumnIndex(time_6_5));
				moveData2.time_7 = cursor.getInt(cursor.getColumnIndex(time_7));
				moveData2.time_7_5 = cursor.getInt(cursor.getColumnIndex(time_7_5));
				moveData2.time_8 = cursor.getInt(cursor.getColumnIndex(time_8));
				moveData2.time_8_5 = cursor.getInt(cursor.getColumnIndex(time_8_5));
				moveData2.time_9 = cursor.getInt(cursor.getColumnIndex(time_9));
				moveData2.time_9_5 = cursor.getInt(cursor.getColumnIndex(time_9_5));
				moveData2.time_10 = cursor.getInt(cursor.getColumnIndex(time_10));
				moveData2.time_10_5 = cursor.getInt(cursor.getColumnIndex(time_10_5));
				moveData2.time_11 = cursor.getInt(cursor.getColumnIndex(time_11));
				moveData2.time_11_5 = cursor.getInt(cursor.getColumnIndex(time_11_5));
				moveData2.time_12 = cursor.getInt(cursor.getColumnIndex(time_12));
				moveData2.time_12_5 = cursor.getInt(cursor.getColumnIndex(time_12_5));
				moveData2.time_13 = cursor.getInt(cursor.getColumnIndex(time_13));
				moveData2.time_13_5 = cursor.getInt(cursor.getColumnIndex(time_13_5));
				moveData2.time_14 = cursor.getInt(cursor.getColumnIndex(time_14));
				moveData2.time_14_5 = cursor.getInt(cursor.getColumnIndex(time_14_5));
				moveData2.time_15 = cursor.getInt(cursor.getColumnIndex(time_15));
				moveData2.time_15_5 = cursor.getInt(cursor.getColumnIndex(time_15_5));
				moveData2.time_16 = cursor.getInt(cursor.getColumnIndex(time_16));
				moveData2.time_16_5 = cursor.getInt(cursor.getColumnIndex(time_16_5));
				moveData2.time_17 = cursor.getInt(cursor.getColumnIndex(time_17));
				moveData2.time_17_5 = cursor.getInt(cursor.getColumnIndex(time_17_5));
				moveData2.time_18 = cursor.getInt(cursor.getColumnIndex(time_18));
				moveData2.time_18_5 = cursor.getInt(cursor.getColumnIndex(time_18_5));
				moveData2.time_19 = cursor.getInt(cursor.getColumnIndex(time_19));
				moveData2.time_19_5 = cursor.getInt(cursor.getColumnIndex(time_19_5));
				moveData2.time_20 = cursor.getInt(cursor.getColumnIndex(time_20));
				moveData2.time_20_5 = cursor.getInt(cursor.getColumnIndex(time_20_5));
				moveData2.time_21 = cursor.getInt(cursor.getColumnIndex(time_21));
				moveData2.time_21_5 = cursor.getInt(cursor.getColumnIndex(time_21_5));
				moveData2.time_22 = cursor.getInt(cursor.getColumnIndex(time_22));
				moveData2.time_22_5 = cursor.getInt(cursor.getColumnIndex(time_22_5));
				moveData2.time_23 = cursor.getInt(cursor.getColumnIndex(time_23));
				moveData2.time_23_5 = cursor.getInt(cursor.getColumnIndex(time_23_5));
				moveData2.todayTarget = cursor.getInt(cursor.getColumnIndex(todayTarget));
				ret.addElement(moveData2);
			}
		}

		cursor.close();

		db.close();

		return ret;
	}
	public synchronized Vector<MoveData> queryMoveDataByBeginEndDay(String id, long begin_time, long end_time) throws Exception {
		if (begin_time == 0) {
			return queryAllMoveDataByUser(id);
		}
		
		Calendar cl_begin = Calendar.getInstance();
		cl_begin.setTimeInMillis(begin_time);
		Calendar cl_end = Calendar.getInstance();
		cl_end.setTimeInMillis(end_time);
		
		Vector<MoveData> moveDatas = new Vector<MoveData>();
		MoveData data = new MoveData();
		for( ; CompareCalendarByDate(cl_begin, cl_end) <= 0; ) {
			int lastYear  = cl_begin.get(Calendar.YEAR);
			int lastMonth = cl_begin.get(Calendar.MONTH);
			int lastDay   = cl_begin.get(Calendar.DATE);
			
			data = queryMoveDataByUserSpecDay(id, lastYear, lastMonth, lastDay);
			
			moveDatas.add(data);
			
			cl_begin.set(lastYear, lastMonth, lastDay + 1);
		}
		
		return moveDatas;
	}
	/**
	 *@return -1 c1 < c2; 0 c1 ==c2 ; 1 c1 > c2
	 * 
	 */
	public static int CompareCalendarByDate(Calendar c1, Calendar c2) {
		int c1_year = c1.get(Calendar.YEAR);
		int c1_month = c1.get(Calendar.MONTH);
		int c1_day = c1.get(Calendar.DATE);
		int c2_year = c2.get(Calendar.YEAR);
		int c2_month = c2.get(Calendar.MONTH);
		int c2_day = c2.get(Calendar.DATE);
		if (c1_year ==  c2_year){
			if (c1_month == c2_month) {
				if (c1_day == c2_day) {
					return 0;
				} else if (c1_day < c2_day) {
					return -1;
				} else {
					return 1;
				}
			} else if( c1_month < c2_month) {
				return -1;
			} else {
				return 1;
			}
		} else if (c1_year < c2_year) {
			return -1;
		} else {
			return 1;
		}
	}
	public synchronized MoveData queryMoveDataByUserSpecDay(String id,long t) {

		Time time = new Time();
		time.set(t);
		int year = time.year;
		int mounth = time.month;
		int day = time.monthDay;
		 
		return queryMoveDataByUserSpecDay(id,year,mounth,day);
	}
	
	public synchronized Vector<MoveData> queryMoveDataByUserSpecMonth(String id,long t) {

		Time time = new Time();
		time.set(t);
		int year = time.year;
		int mounth = time.month;
		int day = time.monthDay;
		 
		return queryMoveDataByUserSpecMonth(id,year,mounth);
	}
	
	public synchronized Vector<MoveData> queryMoveDataByUserSpecWeek(String id,long t) {

	
		
		Calendar cl = Calendar.getInstance();
		cl.setTimeInMillis(t);
		int week = cl.get(Calendar.DAY_OF_WEEK);
		
		Vector<MoveData> moveDatas = new Vector<MoveData>();
		for(int i=1; i<=week; i++){
			cl.setTimeInMillis(t);
			int day = cl.get(Calendar.DATE);
			cl.set(Calendar.DATE, day-week+i);
			int lastYear = cl.get(Calendar.YEAR);
			int lastMonth = cl.get(Calendar.MONTH);
			int lastDay = cl.get(Calendar.DATE);
			
			MoveData moveData = queryMoveDataByUserSpecDay(id, lastYear, lastMonth, lastDay);
			
			if(moveData!=null){
				moveDatas.add(moveData);
			}
		}
		
		return moveDatas;
	}
	
	public synchronized Vector<MoveData> queryMoveDataByUserSpecWeek(String id,int y,int m,int d) {

	
		
		Calendar cl = Calendar.getInstance();
		cl.set(y, m, d);
		int week = cl.get(Calendar.DAY_OF_WEEK);
		Log.i("log","queryMoveDataByUserSpecWeek:week="+week);
		Vector<MoveData> moveDatas = new Vector<MoveData>();
		for(int i=1; i<=week; i++){
			cl.set(y, m, d);
			int day = cl.get(Calendar.DATE);
			cl.set(Calendar.DATE, day-week+i);
			int lastYear = cl.get(Calendar.YEAR);
			int lastMonth = cl.get(Calendar.MONTH);
			int lastDay = cl.get(Calendar.DATE);
			
			MoveData moveData = queryMoveDataByUserSpecDay(id, lastYear, lastMonth, lastDay);
			
			if(moveData!=null){
				moveDatas.add(moveData);
			}
		}
		
		return moveDatas;
	}
	
	
	
	public synchronized Vector<MoveData> queryMoveDataByUserSpecYear(String id,long t) {

		Time time = new Time();
		time.set(t);
		int year = time.year;
		int mounth = time.month;
		int day = time.monthDay;
		 
		return queryMoveDataByUserSpecYear(id,year);
	}
	
	
	
	public synchronized MoveData queryMoveDataByUserSpecDay(String id,int y,int m,int d) {

		SQLiteDatabase db = null;

		MoveData moveData= null;

		if (this.mdbHelper != null) {
			db = mdbHelper.getWritableDatabase();
		}

		String orderBy = totalday+" asc";
		
		Cursor cursor = db.query(TABLE_NAME, null, userId+"=? and "+year+"=? and "+mounth+"=? and "+day+"=?",
				new String[] { id,String.valueOf(y),String.valueOf(m),String.valueOf(d) }, null, null, orderBy);
		Log.i("log","queryMoveDataByUserSpecDay:y="+y+" m="+m+" d="+d+" cusor="+cursor+" cursor.moveToFirst()="+cursor.moveToFirst());
		if (cursor != null && cursor.moveToFirst()) {
			
			//ret = new Vector<MoveData>();
			
			moveData = new MoveData();
			
			moveData.userId = cursor.getString(cursor.getColumnIndex(userId));
			moveData.year = cursor.getInt(cursor.getColumnIndex(year));
			moveData.mounth = cursor.getInt(cursor.getColumnIndex(mounth));
			moveData.day = cursor.getInt(cursor.getColumnIndex(day));
			moveData.time_0 = cursor.getInt(cursor.getColumnIndex(time_0));
			moveData.time_0_5 = cursor.getInt(cursor.getColumnIndex(time_0_5));
			moveData.time_1 = cursor.getInt(cursor.getColumnIndex(time_1));
			moveData.time_1_5 = cursor.getInt(cursor.getColumnIndex(time_1_5));
			moveData.time_2 = cursor.getInt(cursor.getColumnIndex(time_2));
			moveData.time_2_5 = cursor.getInt(cursor.getColumnIndex(time_2_5));
			moveData.time_3 = cursor.getInt(cursor.getColumnIndex(time_3));
			moveData.time_3_5 = cursor.getInt(cursor.getColumnIndex(time_3_5));
			moveData.time_4 = cursor.getInt(cursor.getColumnIndex(time_4));
			moveData.time_4_5 = cursor.getInt(cursor.getColumnIndex(time_4_5));
			moveData.time_5 = cursor.getInt(cursor.getColumnIndex(time_5));
			moveData.time_5_5 = cursor.getInt(cursor.getColumnIndex(time_5_5));
			moveData.time_6 = cursor.getInt(cursor.getColumnIndex(time_6));
			moveData.time_6_5 = cursor.getInt(cursor.getColumnIndex(time_6_5));
			moveData.time_7 = cursor.getInt(cursor.getColumnIndex(time_7));
			moveData.time_7_5 = cursor.getInt(cursor.getColumnIndex(time_7_5));
			moveData.time_8 = cursor.getInt(cursor.getColumnIndex(time_8));
			moveData.time_8_5 = cursor.getInt(cursor.getColumnIndex(time_8_5));
			moveData.time_9 = cursor.getInt(cursor.getColumnIndex(time_9));
			moveData.time_9_5 = cursor.getInt(cursor.getColumnIndex(time_9_5));
			moveData.time_10 = cursor.getInt(cursor.getColumnIndex(time_10));
			moveData.time_10_5 = cursor.getInt(cursor.getColumnIndex(time_10_5));
			moveData.time_11 = cursor.getInt(cursor.getColumnIndex(time_11));
			moveData.time_11_5 = cursor.getInt(cursor.getColumnIndex(time_11_5));
			moveData.time_12 = cursor.getInt(cursor.getColumnIndex(time_12));
			moveData.time_12_5 = cursor.getInt(cursor.getColumnIndex(time_12_5));
			moveData.time_13 = cursor.getInt(cursor.getColumnIndex(time_13));
			moveData.time_13_5 = cursor.getInt(cursor.getColumnIndex(time_13_5));
			moveData.time_14 = cursor.getInt(cursor.getColumnIndex(time_14));
			moveData.time_14_5 = cursor.getInt(cursor.getColumnIndex(time_14_5));
			moveData.time_15 = cursor.getInt(cursor.getColumnIndex(time_15));
			moveData.time_15_5 = cursor.getInt(cursor.getColumnIndex(time_15_5));
			moveData.time_16 = cursor.getInt(cursor.getColumnIndex(time_16));
			moveData.time_16_5 = cursor.getInt(cursor.getColumnIndex(time_16_5));
			moveData.time_17 = cursor.getInt(cursor.getColumnIndex(time_17));
			moveData.time_17_5 = cursor.getInt(cursor.getColumnIndex(time_17_5));
			moveData.time_18 = cursor.getInt(cursor.getColumnIndex(time_18));
			moveData.time_18_5 = cursor.getInt(cursor.getColumnIndex(time_18_5));
			moveData.time_19 = cursor.getInt(cursor.getColumnIndex(time_19));
			moveData.time_19_5 = cursor.getInt(cursor.getColumnIndex(time_19_5));
			moveData.time_20 = cursor.getInt(cursor.getColumnIndex(time_20));
			moveData.time_20_5 = cursor.getInt(cursor.getColumnIndex(time_20_5));
			moveData.time_21 = cursor.getInt(cursor.getColumnIndex(time_21));
			moveData.time_21_5 = cursor.getInt(cursor.getColumnIndex(time_21_5));
			moveData.time_22 = cursor.getInt(cursor.getColumnIndex(time_22));
			moveData.time_22_5 = cursor.getInt(cursor.getColumnIndex(time_22_5));
			moveData.time_23 = cursor.getInt(cursor.getColumnIndex(time_23));
			moveData.time_23_5 = cursor.getInt(cursor.getColumnIndex(time_23_5));
			moveData.todayTarget = cursor.getInt(cursor.getColumnIndex(todayTarget));
		//	ret.addElement(moveData);
			
			
		}

		cursor.close();

		db.close();

		return moveData;
	}
	
	
	
	
	
	public synchronized Vector<MoveData> queryMoveDataByUserSpecMonth(String id,int y,int m) {

		SQLiteDatabase db = null;

		Vector<MoveData> ret = null;

		if (this.mdbHelper != null) {
			db = mdbHelper.getWritableDatabase();
		}

		String orderBy = totalday+" asc";
		
		Cursor cursor = db.query(TABLE_NAME, null, userId+"=? and "+year+"=? and "+mounth+"=?",
				new String[] { id,String.valueOf(y),String.valueOf(m) }, null, null, orderBy);

		if (cursor != null && cursor.moveToFirst()) {
			
			ret = new Vector<MoveData>();
			
			MoveData moveData = new MoveData();
			
			moveData.userId = cursor.getString(cursor.getColumnIndex(userId));
			moveData.year = cursor.getInt(cursor.getColumnIndex(year));
			moveData.mounth = cursor.getInt(cursor.getColumnIndex(mounth));
			moveData.day = cursor.getInt(cursor.getColumnIndex(day));
			moveData.time_0 = cursor.getInt(cursor.getColumnIndex(time_0));
			moveData.time_0_5 = cursor.getInt(cursor.getColumnIndex(time_0_5));
			moveData.time_1 = cursor.getInt(cursor.getColumnIndex(time_1));
			moveData.time_1_5 = cursor.getInt(cursor.getColumnIndex(time_1_5));
			moveData.time_2 = cursor.getInt(cursor.getColumnIndex(time_2));
			moveData.time_2_5 = cursor.getInt(cursor.getColumnIndex(time_2_5));
			moveData.time_3 = cursor.getInt(cursor.getColumnIndex(time_3));
			moveData.time_3_5 = cursor.getInt(cursor.getColumnIndex(time_3_5));
			moveData.time_4 = cursor.getInt(cursor.getColumnIndex(time_4));
			moveData.time_4_5 = cursor.getInt(cursor.getColumnIndex(time_4_5));
			moveData.time_5 = cursor.getInt(cursor.getColumnIndex(time_5));
			moveData.time_5_5 = cursor.getInt(cursor.getColumnIndex(time_5_5));
			moveData.time_6 = cursor.getInt(cursor.getColumnIndex(time_6));
			moveData.time_6_5 = cursor.getInt(cursor.getColumnIndex(time_6_5));
			moveData.time_7 = cursor.getInt(cursor.getColumnIndex(time_7));
			moveData.time_7_5 = cursor.getInt(cursor.getColumnIndex(time_7_5));
			moveData.time_8 = cursor.getInt(cursor.getColumnIndex(time_8));
			moveData.time_8_5 = cursor.getInt(cursor.getColumnIndex(time_8_5));
			moveData.time_9 = cursor.getInt(cursor.getColumnIndex(time_9));
			moveData.time_9_5 = cursor.getInt(cursor.getColumnIndex(time_9_5));
			moveData.time_10 = cursor.getInt(cursor.getColumnIndex(time_10));
			moveData.time_10_5 = cursor.getInt(cursor.getColumnIndex(time_10_5));
			moveData.time_11 = cursor.getInt(cursor.getColumnIndex(time_11));
			moveData.time_11_5 = cursor.getInt(cursor.getColumnIndex(time_11_5));
			moveData.time_12 = cursor.getInt(cursor.getColumnIndex(time_12));
			moveData.time_12_5 = cursor.getInt(cursor.getColumnIndex(time_12_5));
			moveData.time_13 = cursor.getInt(cursor.getColumnIndex(time_13));
			moveData.time_13_5 = cursor.getInt(cursor.getColumnIndex(time_13_5));
			moveData.time_14 = cursor.getInt(cursor.getColumnIndex(time_14));
			moveData.time_14_5 = cursor.getInt(cursor.getColumnIndex(time_14_5));
			moveData.time_15 = cursor.getInt(cursor.getColumnIndex(time_15));
			moveData.time_15_5 = cursor.getInt(cursor.getColumnIndex(time_15_5));
			moveData.time_16 = cursor.getInt(cursor.getColumnIndex(time_16));
			moveData.time_16_5 = cursor.getInt(cursor.getColumnIndex(time_16_5));
			moveData.time_17 = cursor.getInt(cursor.getColumnIndex(time_17));
			moveData.time_17_5 = cursor.getInt(cursor.getColumnIndex(time_17_5));
			moveData.time_18 = cursor.getInt(cursor.getColumnIndex(time_18));
			moveData.time_18_5 = cursor.getInt(cursor.getColumnIndex(time_18_5));
			moveData.time_19 = cursor.getInt(cursor.getColumnIndex(time_19));
			moveData.time_19_5 = cursor.getInt(cursor.getColumnIndex(time_19_5));
			moveData.time_20 = cursor.getInt(cursor.getColumnIndex(time_20));
			moveData.time_20_5 = cursor.getInt(cursor.getColumnIndex(time_20_5));
			moveData.time_21 = cursor.getInt(cursor.getColumnIndex(time_21));
			moveData.time_21_5 = cursor.getInt(cursor.getColumnIndex(time_21_5));
			moveData.time_22 = cursor.getInt(cursor.getColumnIndex(time_22));
			moveData.time_22_5 = cursor.getInt(cursor.getColumnIndex(time_22_5));
			moveData.time_23 = cursor.getInt(cursor.getColumnIndex(time_23));
			moveData.time_23_5 = cursor.getInt(cursor.getColumnIndex(time_23_5));
			moveData.todayTarget = cursor.getInt(cursor.getColumnIndex(todayTarget));
			ret.addElement(moveData);
			
			while(cursor.moveToNext()){
				
				
				
				
				MoveData moveData2 = new MoveData();
				
				moveData2.userId = cursor.getString(cursor.getColumnIndex(userId));
				moveData2.year = cursor.getInt(cursor.getColumnIndex(year));
				moveData2.mounth = cursor.getInt(cursor.getColumnIndex(mounth));
				moveData2.day = cursor.getInt(cursor.getColumnIndex(day));
				moveData2.time_0 = cursor.getInt(cursor.getColumnIndex(time_0));
				moveData2.time_0_5 = cursor.getInt(cursor.getColumnIndex(time_0_5));
				moveData2.time_1 = cursor.getInt(cursor.getColumnIndex(time_1));
				moveData2.time_1_5 = cursor.getInt(cursor.getColumnIndex(time_1_5));
				moveData2.time_2 = cursor.getInt(cursor.getColumnIndex(time_2));
				moveData2.time_2_5 = cursor.getInt(cursor.getColumnIndex(time_2_5));
				moveData2.time_3 = cursor.getInt(cursor.getColumnIndex(time_3));
				moveData2.time_3_5 = cursor.getInt(cursor.getColumnIndex(time_3_5));
				moveData2.time_4 = cursor.getInt(cursor.getColumnIndex(time_4));
				moveData2.time_4_5 = cursor.getInt(cursor.getColumnIndex(time_4_5));
				moveData2.time_5 = cursor.getInt(cursor.getColumnIndex(time_5));
				moveData2.time_5_5 = cursor.getInt(cursor.getColumnIndex(time_5_5));
				moveData2.time_6 = cursor.getInt(cursor.getColumnIndex(time_6));
				moveData2.time_6_5 = cursor.getInt(cursor.getColumnIndex(time_6_5));
				moveData2.time_7 = cursor.getInt(cursor.getColumnIndex(time_7));
				moveData2.time_7_5 = cursor.getInt(cursor.getColumnIndex(time_7_5));
				moveData2.time_8 = cursor.getInt(cursor.getColumnIndex(time_8));
				moveData2.time_8_5 = cursor.getInt(cursor.getColumnIndex(time_8_5));
				moveData2.time_9 = cursor.getInt(cursor.getColumnIndex(time_9));
				moveData2.time_9_5 = cursor.getInt(cursor.getColumnIndex(time_9_5));
				moveData2.time_10 = cursor.getInt(cursor.getColumnIndex(time_10));
				moveData2.time_10_5 = cursor.getInt(cursor.getColumnIndex(time_10_5));
				moveData2.time_11 = cursor.getInt(cursor.getColumnIndex(time_11));
				moveData2.time_11_5 = cursor.getInt(cursor.getColumnIndex(time_11_5));
				moveData2.time_12 = cursor.getInt(cursor.getColumnIndex(time_12));
				moveData2.time_12_5 = cursor.getInt(cursor.getColumnIndex(time_12_5));
				moveData2.time_13 = cursor.getInt(cursor.getColumnIndex(time_13));
				moveData2.time_13_5 = cursor.getInt(cursor.getColumnIndex(time_13_5));
				moveData2.time_14 = cursor.getInt(cursor.getColumnIndex(time_14));
				moveData2.time_14_5 = cursor.getInt(cursor.getColumnIndex(time_14_5));
				moveData2.time_15 = cursor.getInt(cursor.getColumnIndex(time_15));
				moveData2.time_15_5 = cursor.getInt(cursor.getColumnIndex(time_15_5));
				moveData2.time_16 = cursor.getInt(cursor.getColumnIndex(time_16));
				moveData2.time_16_5 = cursor.getInt(cursor.getColumnIndex(time_16_5));
				moveData2.time_17 = cursor.getInt(cursor.getColumnIndex(time_17));
				moveData2.time_17_5 = cursor.getInt(cursor.getColumnIndex(time_17_5));
				moveData2.time_18 = cursor.getInt(cursor.getColumnIndex(time_18));
				moveData2.time_18_5 = cursor.getInt(cursor.getColumnIndex(time_18_5));
				moveData2.time_19 = cursor.getInt(cursor.getColumnIndex(time_19));
				moveData2.time_19_5 = cursor.getInt(cursor.getColumnIndex(time_19_5));
				moveData2.time_20 = cursor.getInt(cursor.getColumnIndex(time_20));
				moveData2.time_20_5 = cursor.getInt(cursor.getColumnIndex(time_20_5));
				moveData2.time_21 = cursor.getInt(cursor.getColumnIndex(time_21));
				moveData2.time_21_5 = cursor.getInt(cursor.getColumnIndex(time_21_5));
				moveData2.time_22 = cursor.getInt(cursor.getColumnIndex(time_22));
				moveData2.time_22_5 = cursor.getInt(cursor.getColumnIndex(time_22_5));
				moveData2.time_23 = cursor.getInt(cursor.getColumnIndex(time_23));
				moveData2.time_23_5 = cursor.getInt(cursor.getColumnIndex(time_23_5));
				moveData2.todayTarget = cursor.getInt(cursor.getColumnIndex(todayTarget));
				ret.addElement(moveData2);
			}
		}

		cursor.close();

		db.close();

		return ret;
	}
	
	
	public synchronized Vector<MoveData> queryMoveDataByUserSpecYear(String id,int y) {

		SQLiteDatabase db = null;

		Vector<MoveData> ret = null;

		if (this.mdbHelper != null) {
			db = mdbHelper.getWritableDatabase();
		}

		String orderBy = totalday+" asc";
		
		Cursor cursor = db.query(TABLE_NAME, null, userId+"=? and "+year+"=?",
				new String[] { id,String.valueOf(y) }, null, null, orderBy);

		if (cursor != null && cursor.moveToFirst()) {
			
			ret = new Vector<MoveData>();
			
			MoveData moveData = new MoveData();
			
			moveData.userId = cursor.getString(cursor.getColumnIndex(userId));
			moveData.year = cursor.getInt(cursor.getColumnIndex(year));
			moveData.mounth = cursor.getInt(cursor.getColumnIndex(mounth));
			moveData.day = cursor.getInt(cursor.getColumnIndex(day));
			moveData.time_0 = cursor.getInt(cursor.getColumnIndex(time_0));
			moveData.time_0_5 = cursor.getInt(cursor.getColumnIndex(time_0_5));
			moveData.time_1 = cursor.getInt(cursor.getColumnIndex(time_1));
			moveData.time_1_5 = cursor.getInt(cursor.getColumnIndex(time_1_5));
			moveData.time_2 = cursor.getInt(cursor.getColumnIndex(time_2));
			moveData.time_2_5 = cursor.getInt(cursor.getColumnIndex(time_2_5));
			moveData.time_3 = cursor.getInt(cursor.getColumnIndex(time_3));
			moveData.time_3_5 = cursor.getInt(cursor.getColumnIndex(time_3_5));
			moveData.time_4 = cursor.getInt(cursor.getColumnIndex(time_4));
			moveData.time_4_5 = cursor.getInt(cursor.getColumnIndex(time_4_5));
			moveData.time_5 = cursor.getInt(cursor.getColumnIndex(time_5));
			moveData.time_5_5 = cursor.getInt(cursor.getColumnIndex(time_5_5));
			moveData.time_6 = cursor.getInt(cursor.getColumnIndex(time_6));
			moveData.time_6_5 = cursor.getInt(cursor.getColumnIndex(time_6_5));
			moveData.time_7 = cursor.getInt(cursor.getColumnIndex(time_7));
			moveData.time_7_5 = cursor.getInt(cursor.getColumnIndex(time_7_5));
			moveData.time_8 = cursor.getInt(cursor.getColumnIndex(time_8));
			moveData.time_8_5 = cursor.getInt(cursor.getColumnIndex(time_8_5));
			moveData.time_9 = cursor.getInt(cursor.getColumnIndex(time_9));
			moveData.time_9_5 = cursor.getInt(cursor.getColumnIndex(time_9_5));
			moveData.time_10 = cursor.getInt(cursor.getColumnIndex(time_10));
			moveData.time_10_5 = cursor.getInt(cursor.getColumnIndex(time_10_5));
			moveData.time_11 = cursor.getInt(cursor.getColumnIndex(time_11));
			moveData.time_11_5 = cursor.getInt(cursor.getColumnIndex(time_11_5));
			moveData.time_12 = cursor.getInt(cursor.getColumnIndex(time_12));
			moveData.time_12_5 = cursor.getInt(cursor.getColumnIndex(time_12_5));
			moveData.time_13 = cursor.getInt(cursor.getColumnIndex(time_13));
			moveData.time_13_5 = cursor.getInt(cursor.getColumnIndex(time_13_5));
			moveData.time_14 = cursor.getInt(cursor.getColumnIndex(time_14));
			moveData.time_14_5 = cursor.getInt(cursor.getColumnIndex(time_14_5));
			moveData.time_15 = cursor.getInt(cursor.getColumnIndex(time_15));
			moveData.time_15_5 = cursor.getInt(cursor.getColumnIndex(time_15_5));
			moveData.time_16 = cursor.getInt(cursor.getColumnIndex(time_16));
			moveData.time_16_5 = cursor.getInt(cursor.getColumnIndex(time_16_5));
			moveData.time_17 = cursor.getInt(cursor.getColumnIndex(time_17));
			moveData.time_17_5 = cursor.getInt(cursor.getColumnIndex(time_17_5));
			moveData.time_18 = cursor.getInt(cursor.getColumnIndex(time_18));
			moveData.time_18_5 = cursor.getInt(cursor.getColumnIndex(time_18_5));
			moveData.time_19 = cursor.getInt(cursor.getColumnIndex(time_19));
			moveData.time_19_5 = cursor.getInt(cursor.getColumnIndex(time_19_5));
			moveData.time_20 = cursor.getInt(cursor.getColumnIndex(time_20));
			moveData.time_20_5 = cursor.getInt(cursor.getColumnIndex(time_20_5));
			moveData.time_21 = cursor.getInt(cursor.getColumnIndex(time_21));
			moveData.time_21_5 = cursor.getInt(cursor.getColumnIndex(time_21_5));
			moveData.time_22 = cursor.getInt(cursor.getColumnIndex(time_22));
			moveData.time_22_5 = cursor.getInt(cursor.getColumnIndex(time_22_5));
			moveData.time_23 = cursor.getInt(cursor.getColumnIndex(time_23));
			moveData.time_23_5 = cursor.getInt(cursor.getColumnIndex(time_23_5));
			moveData.todayTarget = cursor.getInt(cursor.getColumnIndex(todayTarget));
			ret.addElement(moveData);
			
			while(cursor.moveToNext()){
				
				
				
				
				MoveData moveData2 = new MoveData();
				
				moveData2.userId = cursor.getString(cursor.getColumnIndex(userId));
				moveData2.year = cursor.getInt(cursor.getColumnIndex(year));
				moveData2.mounth = cursor.getInt(cursor.getColumnIndex(mounth));
				moveData2.day = cursor.getInt(cursor.getColumnIndex(day));
				moveData2.time_0 = cursor.getInt(cursor.getColumnIndex(time_0));
				moveData2.time_0_5 = cursor.getInt(cursor.getColumnIndex(time_0_5));
				moveData2.time_1 = cursor.getInt(cursor.getColumnIndex(time_1));
				moveData2.time_1_5 = cursor.getInt(cursor.getColumnIndex(time_1_5));
				moveData2.time_2 = cursor.getInt(cursor.getColumnIndex(time_2));
				moveData2.time_2_5 = cursor.getInt(cursor.getColumnIndex(time_2_5));
				moveData2.time_3 = cursor.getInt(cursor.getColumnIndex(time_3));
				moveData2.time_3_5 = cursor.getInt(cursor.getColumnIndex(time_3_5));
				moveData2.time_4 = cursor.getInt(cursor.getColumnIndex(time_4));
				moveData2.time_4_5 = cursor.getInt(cursor.getColumnIndex(time_4_5));
				moveData2.time_5 = cursor.getInt(cursor.getColumnIndex(time_5));
				moveData2.time_5_5 = cursor.getInt(cursor.getColumnIndex(time_5_5));
				moveData2.time_6 = cursor.getInt(cursor.getColumnIndex(time_6));
				moveData2.time_6_5 = cursor.getInt(cursor.getColumnIndex(time_6_5));
				moveData2.time_7 = cursor.getInt(cursor.getColumnIndex(time_7));
				moveData2.time_7_5 = cursor.getInt(cursor.getColumnIndex(time_7_5));
				moveData2.time_8 = cursor.getInt(cursor.getColumnIndex(time_8));
				moveData2.time_8_5 = cursor.getInt(cursor.getColumnIndex(time_8_5));
				moveData2.time_9 = cursor.getInt(cursor.getColumnIndex(time_9));
				moveData2.time_9_5 = cursor.getInt(cursor.getColumnIndex(time_9_5));
				moveData2.time_10 = cursor.getInt(cursor.getColumnIndex(time_10));
				moveData2.time_10_5 = cursor.getInt(cursor.getColumnIndex(time_10_5));
				moveData2.time_11 = cursor.getInt(cursor.getColumnIndex(time_11));
				moveData2.time_11_5 = cursor.getInt(cursor.getColumnIndex(time_11_5));
				moveData2.time_12 = cursor.getInt(cursor.getColumnIndex(time_12));
				moveData2.time_12_5 = cursor.getInt(cursor.getColumnIndex(time_12_5));
				moveData2.time_13 = cursor.getInt(cursor.getColumnIndex(time_13));
				moveData2.time_13_5 = cursor.getInt(cursor.getColumnIndex(time_13_5));
				moveData2.time_14 = cursor.getInt(cursor.getColumnIndex(time_14));
				moveData2.time_14_5 = cursor.getInt(cursor.getColumnIndex(time_14_5));
				moveData2.time_15 = cursor.getInt(cursor.getColumnIndex(time_15));
				moveData2.time_15_5 = cursor.getInt(cursor.getColumnIndex(time_15_5));
				moveData2.time_16 = cursor.getInt(cursor.getColumnIndex(time_16));
				moveData2.time_16_5 = cursor.getInt(cursor.getColumnIndex(time_16_5));
				moveData2.time_17 = cursor.getInt(cursor.getColumnIndex(time_17));
				moveData2.time_17_5 = cursor.getInt(cursor.getColumnIndex(time_17_5));
				moveData2.time_18 = cursor.getInt(cursor.getColumnIndex(time_18));
				moveData2.time_18_5 = cursor.getInt(cursor.getColumnIndex(time_18_5));
				moveData2.time_19 = cursor.getInt(cursor.getColumnIndex(time_19));
				moveData2.time_19_5 = cursor.getInt(cursor.getColumnIndex(time_19_5));
				moveData2.time_20 = cursor.getInt(cursor.getColumnIndex(time_20));
				moveData2.time_20_5 = cursor.getInt(cursor.getColumnIndex(time_20_5));
				moveData2.time_21 = cursor.getInt(cursor.getColumnIndex(time_21));
				moveData2.time_21_5 = cursor.getInt(cursor.getColumnIndex(time_21_5));
				moveData2.time_22 = cursor.getInt(cursor.getColumnIndex(time_22));
				moveData2.time_22_5 = cursor.getInt(cursor.getColumnIndex(time_22_5));
				moveData2.time_23 = cursor.getInt(cursor.getColumnIndex(time_23));
				moveData2.time_23_5 = cursor.getInt(cursor.getColumnIndex(time_23_5));
				moveData2.todayTarget = cursor.getInt(cursor.getColumnIndex(todayTarget));
				ret.addElement(moveData2);
			}
		}

		cursor.close();

		db.close();

		return ret;
	}
	

	public synchronized long deleteAllMoveDataByUser(String id){

		SQLiteDatabase db = null;

		if(this.mdbHelper != null)
		{
			db = mdbHelper.getWritableDatabase();
		}

		long result = db.delete(TABLE_NAME, userId+"=?",new String[]{id});

		db.close(); 

		return result;		
	}
	
	public synchronized long deleteAllMoveData(){

		SQLiteDatabase db = null;

		if(this.mdbHelper != null)
		{
			db = mdbHelper.getWritableDatabase();
		}

		long result = db.delete(TABLE_NAME, null,null);

		db.close(); 

		return result;		
	}
	
	
	public synchronized long insertMoveData(MoveData g, int ...args){
		long result = -1;
		if(g==null){
			return result;
		}
		
		if(queryMoveDataByUserSpecDay(g.userId,g.year,g.mounth,g.day)==null){
			
			SQLiteDatabase db = null;

			if (this.mdbHelper != null) {
				db = mdbHelper.getWritableDatabase();
			}
			
			ContentValues values = new ContentValues();

			values.put(userId, g.userId);
			values.put(year, g.year);
			values.put(mounth, g.mounth);
			values.put(day, g.day);
			
			values.put(time_0, g.time_0);
			values.put(time_0_5, g.time_0_5);
			values.put(time_1, g.time_1);
			values.put(time_1_5, g.time_1_5);
			values.put(time_2, g.time_2);
			values.put(time_2_5, g.time_2_5);
			values.put(time_3, g.time_3);
			values.put(time_3_5, g.time_3_5);
			values.put(time_4, g.time_4);
			values.put(time_4_5, g.time_4_5);
			values.put(time_5, g.time_5);
			values.put(time_5_5, g.time_5_5);
			values.put(time_6, g.time_6);
			values.put(time_6_5, g.time_6_5);
			values.put(time_7, g.time_7);
			values.put(time_7_5, g.time_7_5);
			values.put(time_8, g.time_8);
			values.put(time_8_5, g.time_8_5);
			values.put(time_9, g.time_9);
			values.put(time_9_5, g.time_9_5);
			values.put(time_10, g.time_10);
			values.put(time_10_5, g.time_10_5);
			values.put(time_11, g.time_11);
			values.put(time_11_5, g.time_11_5);
			values.put(time_12, g.time_12);
			values.put(time_12_5, g.time_12_5);
			values.put(time_13, g.time_13);
			values.put(time_13_5, g.time_13_5);
			values.put(time_14, g.time_14);
			values.put(time_14_5, g.time_14_5);
			values.put(time_15, g.time_15);
			values.put(time_15_5, g.time_15_5);
			values.put(time_16, g.time_16);
			values.put(time_16_5, g.time_16_5);
			values.put(time_17, g.time_17);
			values.put(time_17_5, g.time_17_5);
			values.put(time_18, g.time_18);
			values.put(time_18_5, g.time_18_5);
			values.put(time_19, g.time_19);
			values.put(time_19_5, g.time_19_5);
			values.put(time_20, g.time_20);
			values.put(time_20_5, g.time_20_5);
			values.put(time_21, g.time_21);
			values.put(time_21_5, g.time_21_5);
			values.put(time_22, g.time_22);
			values.put(time_22_5, g.time_22_5);
			values.put(time_23, g.time_23);
			values.put(time_23_5, g.time_23_5);
			values.put(totalday, g.year*10000+g.mounth*100+g.day);
			values.put(todayTarget, g.todayTarget);
			
			result = db.insert(TABLE_NAME, null, values);

			db.close();

		} else {

			
			SQLiteDatabase db = null;

			if (this.mdbHelper != null) {
				db = mdbHelper.getWritableDatabase();

			}

			ContentValues values = new ContentValues();

			
			values.put(time_0, g.time_0);
			values.put(time_0_5, g.time_0_5);
			values.put(time_1, g.time_1);
			values.put(time_1_5, g.time_1_5);
			values.put(time_2, g.time_2);
			values.put(time_2_5, g.time_2_5);
			values.put(time_3, g.time_3);
			values.put(time_3_5, g.time_3_5);
			values.put(time_4, g.time_4);
			values.put(time_4_5, g.time_4_5);
			values.put(time_5, g.time_5);
			values.put(time_5_5, g.time_5_5);
			values.put(time_6, g.time_6);
			values.put(time_6_5, g.time_6_5);
			values.put(time_7, g.time_7);
			values.put(time_7_5, g.time_7_5);
			values.put(time_8, g.time_8);
			values.put(time_8_5, g.time_8_5);
			values.put(time_9, g.time_9);
			values.put(time_9_5, g.time_9_5);
			values.put(time_10, g.time_10);
			values.put(time_10_5, g.time_10_5);
			values.put(time_11, g.time_11);
			values.put(time_11_5, g.time_11_5);
			values.put(time_12, g.time_12);
			values.put(time_12_5, g.time_12_5);
			values.put(time_13, g.time_13);
			values.put(time_13_5, g.time_13_5);
			values.put(time_14, g.time_14);
			values.put(time_14_5, g.time_14_5);
			values.put(time_15, g.time_15);
			values.put(time_15_5, g.time_15_5);
			values.put(time_16, g.time_16);
			values.put(time_16_5, g.time_16_5);
			values.put(time_17, g.time_17);
			values.put(time_17_5, g.time_17_5);
			values.put(time_18, g.time_18);
			values.put(time_18_5, g.time_18_5);
			values.put(time_19, g.time_19);
			values.put(time_19_5, g.time_19_5);
			values.put(time_20, g.time_20);
			values.put(time_20_5, g.time_20_5);
			values.put(time_21, g.time_21);
			values.put(time_21_5, g.time_21_5);
			values.put(time_22, g.time_22);
			values.put(time_22_5, g.time_22_5);
			values.put(time_23, g.time_23);
			values.put(time_23_5, g.time_23_5);
			values.put(todayTarget, g.todayTarget);
			
			result = db.update(TABLE_NAME, values, userId+"=? and "+year+"=? and "+mounth+"=? and "+day+"=?",
					new String[] { g.userId,String.valueOf(g.year),String.valueOf(g.mounth),String.valueOf(g.day) });
					
				

			db.close();

		}
		
		
	

		return result;
	}
	

	
	public synchronized boolean isTableExist(){
		
		boolean value = false;
		
		try {
//			queryAllMoveDataByUser("");
			
			SQLiteDatabase db = null;

			if (this.mdbHelper != null) {
				db = mdbHelper.getWritableDatabase();
			}
			
			value = SQLiteHelper.doesTableExist(db, TABLE_NAME);
			
			
		}catch (Exception e) {
			value = false;
		}
		
		return value;
		
	}
	
	
	public synchronized void init() {

		SQLiteDatabase db = null;

		if (this.mdbHelper != null) {
			db = mdbHelper.getWritableDatabase();

		}

		db.beginTransaction();// �?��事务
		try {

			

			db.setTransactionSuccessful();// 调用此方法会在执行到endTransaction()
											// 时提交当前事务，如果不调用此方法会回滚事�?
		} finally {
			db.endTransaction();// 由事务的标志决定是提交事务，还是回滚事务
		}

		db.close();

	}
}
