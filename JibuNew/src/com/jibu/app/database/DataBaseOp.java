package com.jibu.app.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


public class DataBaseOp {

	private final static String DATABASE_NAME = "sinoeb.db";

	public SQLiteHelper mdbHelper;

	public DataBaseOp(Context context) {

	//	int version = ApplicationSharedPreferences.getDatabaseVersion(context);

	//	mdbHelper = new SQLiteHelper(context, DATABASE_NAME, 1);

	}

	public void createAllTable() {
		SQLiteDatabase db = null;

		if (this.mdbHelper != null) {
			db = mdbHelper.getWritableDatabase();

			db.beginTransaction();// �?��事务
			try {

				// 把建表都放在�?

				db.setTransactionSuccessful();// 调用此方法会在执行到endTransaction()
												// 时提交当前事务，如果不调用此方法会回滚事�?
			} finally {
				db.endTransaction();// 由事务的标志决定是提交事务，还是回滚事务
			}

			db.close();
		}
	}

	public void updateTable() {
		SQLiteDatabase db = null;

		if (this.mdbHelper != null) {
			db = mdbHelper.getWritableDatabase();

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

	public void initData(Context context) {

	}

}
