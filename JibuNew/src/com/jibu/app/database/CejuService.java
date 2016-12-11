package com.jibu.app.database;

/**
 * @author ctc
 * 测距历史记录相关数据库操作
 * */

import java.util.Vector;

import com.jibu.app.entity.CejuData;
import com.jibu.app.entity.MoveData;
import com.jibu.app.main.ApplicationSharedPreferences;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CejuService {
	
	private SQLiteHelper mdbHelper;
	
	private final String userId = "userId";
	
	public static final String TABLE_NAME ="ceju";

	private final String DATABASE_NAME = "jibu.db";

	private final String step = "step";
	private final String duration = "duration";
	private final String info = "info";
	private final String timestamp = "timestamp";
	
	
	private final String TABLE_CREATED = "create table " + TABLE_NAME + " ("
			+ "userId varchar" +
			",timestamp long, step int, duration long, info varchar"+
			",primary key(userId, timestamp));";
	
	public CejuService(Context context) {
		super();

		int version = ApplicationSharedPreferences.getDataBaseVersion(context);

		this.mdbHelper = new SQLiteHelper(context, DATABASE_NAME, version,
				TABLE_NAME, TABLE_CREATED);
		
	}
	
	
	public synchronized Vector<CejuData> queryAllCejuData(String id){
		
		SQLiteDatabase db = null;
		
		Vector<CejuData> ret = null;
		
		if(this.mdbHelper != null) {
			db  = mdbHelper.getWritableDatabase();
		}
		String orderBy = timestamp + " desc";
		
		Cursor cursor = db.query(TABLE_NAME, 
				null, userId + "=?",  new String[] {id}, null, null, orderBy);
		
		if (cursor != null && cursor.moveToFirst()) {
			ret = new Vector<CejuData>();
			
			CejuData cejuData = new CejuData();
			cejuData.userId   = cursor.getString(cursor.getColumnIndex(userId));
			cejuData.duration = cursor.getLong(cursor.getColumnIndex(duration));
			cejuData.info	  = cursor.getString(cursor.getColumnIndex(info));
			cejuData.step	  = cursor.getInt(cursor.getColumnIndex(step));
			cejuData.timestamp= cursor.getLong(cursor.getColumnIndex(timestamp));
			
			ret.addElement(cejuData);
			
			while (cursor.moveToNext()) {
				CejuData cejuData2 = new CejuData();
				cejuData2.userId   = cursor.getString(cursor.getColumnIndex(userId));
				cejuData2.duration = cursor.getLong(cursor.getColumnIndex(duration));
				cejuData2.info	  = cursor.getString(cursor.getColumnIndex(info));
				cejuData2.step	  = cursor.getInt(cursor.getColumnIndex(step));
				cejuData2.timestamp= cursor.getLong(cursor.getColumnIndex(timestamp));
				
				ret.addElement(cejuData2);
			}
		} 
		cursor.close();
		
		db.close();
		
		return ret;
	}
	
	public synchronized CejuData queryCejuDataByTimestamp(String id, long stamp) {
		CejuData cejuData = null;
		
		SQLiteDatabase db = null;
		
		
		if(this.mdbHelper != null) {
			db  = mdbHelper.getWritableDatabase();
		}

		Cursor cursor = db.query(TABLE_NAME, 
				null, userId + "=? and " + timestamp +"=?",  new String[] {id, String.valueOf(stamp)}, null, null, null);
		
		if (cursor != null && cursor.moveToFirst()) {
			cejuData = new CejuData();
			cejuData.userId   = cursor.getString(cursor.getColumnIndex(userId));
			cejuData.duration = cursor.getLong(cursor.getColumnIndex(duration));
			cejuData.info	  = cursor.getString(cursor.getColumnIndex(info));
			cejuData.step	  = cursor.getInt(cursor.getColumnIndex(step));
			cejuData.timestamp= cursor.getLong(cursor.getColumnIndex(timestamp));
		}
		
		cursor.close();
		
		db.close();
		
		return cejuData;
	}
	
	public synchronized long insertCejuData(CejuData g){
		long result = -1;
		if(g==null){
			return result;
		}
		
		if(queryCejuDataByTimestamp(g.userId, g.timestamp)==null){
			
			SQLiteDatabase db = null;

			if (this.mdbHelper != null) {
				db = mdbHelper.getWritableDatabase();
			}
			
			ContentValues values = new ContentValues();

			values.put(userId, g.userId);
			values.put(timestamp, g.timestamp);
			values.put(duration, g.duration);
			values.put(info, g.info);
			values.put(step, g.step);
			
			result = db.insert(TABLE_NAME, null, values);

			db.close();

		} else {

			
			SQLiteDatabase db = null;

			if (this.mdbHelper != null) {
				db = mdbHelper.getWritableDatabase();

			}

			ContentValues values = new ContentValues();

			values.put(userId, g.userId);
			values.put(timestamp, g.timestamp);
			values.put(duration, g.duration);
			values.put(info, g.info);
			values.put(step, g.step);
			
			result = db.update(TABLE_NAME, values, userId+"=? and "+timestamp+"=? ",
					new String[] { g.userId,String.valueOf(g.timestamp)});

			db.close();

		}
		
		
	

		return result;
	}
	
	public long deletedCejuData(String id, long stamp) {
		SQLiteDatabase db = null;

		if(this.mdbHelper != null)
		{
			db = mdbHelper.getWritableDatabase();
		}

		long result = db.delete(TABLE_NAME, userId+"=? and " + timestamp + "=?",
				new String[]{id, String.valueOf(stamp)});

		db.close(); 

		return result;	
	}
	public long deletedAllCejuData(String id) {
		SQLiteDatabase db = null;

		if(this.mdbHelper != null)
		{
			db = mdbHelper.getWritableDatabase();
		}

		long result = db.delete(TABLE_NAME, userId+"=?",new String[]{id});

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

	public synchronized void initCeju() {

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
