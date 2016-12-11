package com.jibu.app.database;

import java.util.Vector;

import com.jibu.app.entity.User;
import com.jibu.app.main.ApplicationSharedPreferences;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class UserService {

	
	public final static int USER_DB_VESTION = 3;
	
	private SQLiteHelper mdbHelper;

	private final String userId = "userId";
	private final String userName = "userName";
	private final String password = "password";
	private final String sex = "sex";
	private final String year= "year";
	private final String height= "height";
	private final String weight= "weight";
	private final String waist= "waist";
	private final String step= "step";
	private final String updateTime= "updateTime";
	private final String firstTime= "firstTime";
	private final String userHead="userHead";

	public static final String TABLE_NAME = "user";

	private final String DABABASE_NAME = "jibu.db";
	private final String TABLE_CREATED = "create table " + TABLE_NAME + " ("
			+ "userId varchar primary key,userName varchar" +
			",password varchar,sex int,year int,height float,weight float" +
			",waist float,step int,updateTime long,firstTime long);";

	public UserService(Context context) {
		super();

		int version = ApplicationSharedPreferences.getDataBaseVersion(context);
		
		this.mdbHelper = new SQLiteHelper(context, DABABASE_NAME, version,
				TABLE_NAME, TABLE_CREATED);

	}

	public synchronized long insertItem(User g) {

		SQLiteDatabase db = null;

		if (this.mdbHelper != null) {
			db = mdbHelper.getWritableDatabase();

		}

		ContentValues values = new ContentValues();

		values.put(userId, g.userId);
		values.put(userName, g.userName);
		
		values.put(password, g.password);
		values.put(sex, g.sex);
		values.put(year, g.year);
		
		values.put(height, g.height);
		values.put(weight, g.weight);
		values.put(waist, g.waist);
		values.put(step, g.step);
		values.put(updateTime, g.updateTime);
		values.put(firstTime, g.firstTime);
		values.put(userHead, g.userHead);
		
		long result = db.insert(TABLE_NAME, null, values);

		db.close();

		return result;
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
	
	public synchronized Vector<User> queryAllUser() throws Exception{

		SQLiteDatabase db = null;

		Vector<User> ret = null;

		if (this.mdbHelper != null) {
			db = mdbHelper.getWritableDatabase();
		}

		
		
		Cursor cursor = db.query(TABLE_NAME, null, null,
			null, null, null, null);

		if (cursor != null && cursor.moveToFirst()) {
			
			ret = new Vector<User>();
			
			User user = new User();
			
			user.userId = cursor.getString(cursor.getColumnIndex(userId));
			user.userName = cursor.getString(cursor.getColumnIndex(userName));
			user.password = cursor.getString(cursor.getColumnIndex(password));
			user.sex = cursor.getInt(cursor.getColumnIndex(sex));
			user.year = cursor.getInt(cursor.getColumnIndex(year));
			user.height = cursor.getFloat(cursor.getColumnIndex(height));
			user.weight = cursor.getFloat(cursor.getColumnIndex(weight));
			user.waist = cursor.getFloat(cursor.getColumnIndex(waist));
			user.step = cursor.getInt(cursor.getColumnIndex(step));
			user.updateTime = cursor.getLong(cursor.getColumnIndex(updateTime));
			user.firstTime = cursor.getLong(cursor.getColumnIndex(firstTime));
			user.userHead  = cursor.getString(cursor.getColumnIndex(userHead));
			ret.addElement(user);
			
			while(cursor.moveToNext()){
				
				if(ret.size()>=10){
					break;
				}
				
				user = new User();
				
				user.userId = cursor.getString(cursor.getColumnIndex(userId));
				user.userName = cursor.getString(cursor.getColumnIndex(userName));
				user.password = cursor.getString(cursor.getColumnIndex(password));
				user.sex = cursor.getInt(cursor.getColumnIndex(sex));
				user.year = cursor.getInt(cursor.getColumnIndex(year));
				user.height = cursor.getFloat(cursor.getColumnIndex(height));
				user.weight = cursor.getFloat(cursor.getColumnIndex(weight));
				user.waist = cursor.getFloat(cursor.getColumnIndex(waist));
				user.step = cursor.getInt(cursor.getColumnIndex(step));
				user.updateTime = cursor.getLong(cursor.getColumnIndex(updateTime));
				user.firstTime = cursor.getLong(cursor.getColumnIndex(firstTime));
				user.userHead  = cursor.getString(cursor.getColumnIndex(userHead));
				ret.addElement(user);
			}
		}

		cursor.close();

		db.close();

		return ret;
	}

	public synchronized long deleteUser(String id){

		SQLiteDatabase db = null;

		if(this.mdbHelper != null)
		{
			db = mdbHelper.getWritableDatabase();
		}

		long result = db.delete(TABLE_NAME, userId+"=?",new String[]{id});

		db.close(); 

		return result;		
	}
	
	
	public synchronized long updateUser(User user){
		
		if(user==null){
			return -1;
		}
		
		SQLiteDatabase db = null;


		if (this.mdbHelper != null) {
			db = mdbHelper.getWritableDatabase();
		}

		ContentValues values=new ContentValues();
		
		values.put(userName,user.userName);
		values.put(password,user.password);
		values.put(sex,user.sex);
		values.put(year,user.year);
		values.put(height,user.height);
		values.put(weight,user.weight);
		values.put(waist,user.waist);
		values.put(step,user.step);
		values.put(updateTime,user.updateTime);
		values.put(firstTime,user.firstTime);
		values.put(userHead, user.userHead);
		long result = db.update(TABLE_NAME, values, userId+"=?",new String[]{user.userId});


		db.close();

		return result;
	}
	
	public synchronized User queryUserInfo(String id) {

		SQLiteDatabase db = null;

		User user = null;

		if (this.mdbHelper != null) {
			db = mdbHelper.getWritableDatabase();
		}

		Cursor cursor = db.query(TABLE_NAME, null, userId+"=?",
				new String[] { id }, null, null, null);

		if (cursor != null && cursor.moveToFirst()) {
			user = new User();
			
			user.userId = cursor.getString(cursor.getColumnIndex(userId));
			user.userName = cursor.getString(cursor.getColumnIndex(userName));
			user.password = cursor.getString(cursor.getColumnIndex(password));
			user.sex = cursor.getInt(cursor.getColumnIndex(sex));
			user.year = cursor.getInt(cursor.getColumnIndex(year));
			user.height = cursor.getFloat(cursor.getColumnIndex(height));
			user.weight = cursor.getFloat(cursor.getColumnIndex(weight));
			user.waist = cursor.getFloat(cursor.getColumnIndex(waist));
			user.step = cursor.getInt(cursor.getColumnIndex(step));
			user.updateTime = cursor.getLong(cursor.getColumnIndex(updateTime));
			user.firstTime = cursor.getLong(cursor.getColumnIndex(firstTime));
			user.userHead  = cursor.getString(cursor.getColumnIndex(userHead));
		}

		cursor.close();

		db.close();

		return user;
	}

	public synchronized void close() {
		this.mdbHelper.close();
	}
	
	
public synchronized boolean isTableExist(){
		
		boolean value = false;
		
		try {
//			queryAllUser();
			
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

	public synchronized void initUser() {

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
