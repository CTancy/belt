package com.jibu.app.database;

/**
 * @author ctc
 * 统计个人信息的数据库相关操作，暂时没有使用
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.jibu.app.entity.UserPersonalInfo;
import com.jibu.app.main.ApplicationSharedPreferences;

public class PersonalInfoService {
	 
	
	
	public static final int PERSONALINFO_DB_VERSION = 7;
	private SQLiteHelper mdbHelper;
	
	private final String userId = "userId";
	
	private final String lastDate = "lastDate";
	private final String longestSitDay = "longestSitDay";
	private final String longestSportDay = "longestSportDay";
	private final String sportDistanceTotal = "sportDistanceTotal";
	private final String sitTimeTotal = "sitTimeTotal";
	private final String longestSitDayTime = "longestSitDayTime";
	private final String longestSportDayStep = "longestSportDayStep";
	
	public static final String TABLE_NAME = "personalInfo";

	private final String DATABASE_NAME = "jibu.db";

	private final String TABLE_CREATED = "create table " + TABLE_NAME + " ("
			+ "userId varchar" +
			",lastDate long, longestSitDay long, longestSportDay long, sportDistanceTotal int, sitTimeTotal float "+
			",longestSitDayTime float, longestSportDayStep int"+
			",primary key(userId));";
	
	public PersonalInfoService(Context context) {
		super();

		int version = ApplicationSharedPreferences.getDataBaseVersion(context);

		this.mdbHelper = new SQLiteHelper(context, DATABASE_NAME, version,
				TABLE_NAME, TABLE_CREATED);

	}
	
	public synchronized boolean checkPersonalInfoIsExist(String id){
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
	
	/**获取User 个人详情*/
	public synchronized UserPersonalInfo queryPersonalInfoByUser(String id) {
		SQLiteDatabase db = null;

		UserPersonalInfo ret = null;

		if (this.mdbHelper != null) {
			db = mdbHelper.getWritableDatabase();
		}
		
		Cursor cursor = db.query(TABLE_NAME, null, userId+"=?",
				new String[] { id }, null, null, null);
		
		if (cursor != null && cursor.moveToFirst()) {
			ret = new UserPersonalInfo(id);
			
			ret.lastDate = cursor.getLong(cursor.getColumnIndex(lastDate));
			ret.longestSitDay = cursor.getLong(cursor.getColumnIndex(longestSitDay));
			ret.longestSportDay = cursor.getLong(cursor.getColumnIndex(longestSportDay));
			ret.sitTimeTotal = cursor.getFloat(cursor.getColumnIndex(sitTimeTotal));
			ret.sportDistanceTotal = cursor.getInt(cursor.getColumnIndex(sportDistanceTotal));
			ret.longestSportDayStep =cursor.getInt(cursor.getColumnIndex(longestSportDayStep));
			ret.longestSitDayTime  = cursor.getFloat(cursor.getColumnIndex(longestSitDayTime));
		}
		
		cursor.close();

		db.close();
		
		return ret;
	}
	
	public long UpdateUserPersonalInfo (UserPersonalInfo info) {
		
		if (info == null) {
			return -1;
		}
		
		SQLiteDatabase db = null;


		if (this.mdbHelper != null) {
			db = mdbHelper.getWritableDatabase();
		}

		ContentValues values = new ContentValues();
		values.put(longestSitDay, info.longestSitDay);
		values.put(longestSportDay, info.longestSportDay);
		values.put(sitTimeTotal, info.sitTimeTotal);
		values.put(sportDistanceTotal, info.sportDistanceTotal);
		values.put(lastDate, info.lastDate);
		values.put(longestSitDayTime, info.longestSitDayTime);
		values.put(longestSportDayStep, info.longestSportDayStep);
		
		long result = db.update(TABLE_NAME, values, userId+"=?",new String[]{info.userId});
		
		return result;
	}
	
	public long insertPersonalInforItem(UserPersonalInfo info) {
		long result = -1;
		if(info==null){
			return result;
		}
		
		if ( queryPersonalInfoByUser(info.userId) == null) {
			SQLiteDatabase db = null;

			if (this.mdbHelper != null) {
				db = mdbHelper.getWritableDatabase();

			}

			ContentValues values = new ContentValues();
			
			values.put(userId, info.userId);
			values.put(longestSitDay, info.longestSitDay);
			values.put(longestSportDay, info.longestSportDay);
			values.put(sitTimeTotal, info.sitTimeTotal);
			values.put(sportDistanceTotal, info.sportDistanceTotal);
			values.put(lastDate, info.lastDate);
			values.put(longestSitDayTime, info.longestSitDayTime);
			values.put(longestSportDayStep, info.longestSportDayStep);
			
			result = db.insert(TABLE_NAME, null, values);

			db.close();
		} 
		return result;
	}
	
	public synchronized long deleteUserPersonalInfoByUser(String id){

		SQLiteDatabase db = null;

		if(this.mdbHelper != null)
		{
			db = mdbHelper.getWritableDatabase();
		}

		long result = db.delete(TABLE_NAME, userId+"=?",new String[]{id});

		db.close(); 

		return result;		
	}
	
	public synchronized long deleteAllUserPersonalInfo(){

		SQLiteDatabase db = null;

		if(this.mdbHelper != null)
		{
			db = mdbHelper.getWritableDatabase();
		}

		long result = db.delete(TABLE_NAME, null,null);

		db.close(); 

		return result;		
	}
	
	public synchronized boolean isTableExist(){
		
		boolean value = false;
		
		try {
			
//			queryPersonalInfoByUser("");
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

	public synchronized void initPersonalInfo() {

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
