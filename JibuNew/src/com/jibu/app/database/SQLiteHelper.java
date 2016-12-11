package com.jibu.app.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 该类是一个数据库的帮助类，用于创建数据库
 * 
 * @author lhl
 * 
 */
public class SQLiteHelper extends SQLiteOpenHelper {


	private String tableName;
	private String createTableSql;
	
	public SQLiteHelper(Context context, String dbName,int version,String tableName,String createTableSql) {

		super(context, dbName, null, version); 
		Log.e("SQLiteHelper", "version = " + version);
		this.tableName = tableName;
		this.createTableSql = createTableSql;   
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {		
		Log.i("test","onCreate:"+createTableSql);
		db.execSQL(createTableSql);		
	}
	
	

	

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i("test","onUpgrade:"+tableName+" "+oldVersion+" "+newVersion);
		String sql ="";
		Log.e("SQLiteHelper", "tableExist ?=" + doesTableExist(db, tableName));
		
		/*UserService数据库更新*/
		if (tableName.equals(UserService.TABLE_NAME)
				 && doesTableExist(db, tableName)) {
			if (!isFieldExist(db, tableName, "userHead")) {
				sql = "ALTER TABLE " + tableName +" ADD COLUMN userHead varchar"; 
				db.execSQL(sql);
			}
			return;
		}  
		
		/*MoveData数据库更新*/
		if (tableName.equals(MoveDataService.TABLE_NAME)
					&& doesTableExist(db, tableName)) {
			
			if (!isFieldExist(db, tableName, "todayTarget")) {
				sql = "ALTER TABLE " + tableName +" ADD COLUMN todayTarget int";
				db.execSQL(sql);
			}
			return;
		}
		
		/*数据库更新5-6*/
//		if (oldVersion < PersonalInfoService.PERSONALINFO_DB_VERSION
//				&& tableName.equals(PersonalInfoService.TABLE_NAME)
//					&& doesTableExist(db, tableName)) {
//			sql = "ALTER TABLE " + tableName +" ADD COLUMN dabiaotianshu int";
//			db.execSQL(sql);
//			sql = "ALTER TABLE " + tableName +" ADD COLUMN lianxudabiaotianshu int";
//			db.execSQL(sql);
//			return;
//		}
		
		sql = "DROP TABLE IF EXISTS "+tableName;
		db.execSQL(sql);
		onCreate(db);

	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "DROP TABLE IF EXISTS "+tableName;
		db.execSQL(sql);
		onCreate(db);
	}
	
	 public static boolean doesTableExist(SQLiteDatabase db, String tableName) {
	        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);

	    if (cursor != null) {
	        if (cursor.getCount() > 0) {
	            cursor.close();
	            return true;
	        }
	        cursor.close();
	    }
	    return false;
	}
	 
		/**
		 * 判断某表里某字段是否存在
		 * 
		 * @param db
		 * @param tableName
		 * @param fieldName
		 * @return
		 */
		public static boolean isFieldExist(SQLiteDatabase db, String tableName, String fieldName) {
			String queryStr = "select sql from sqlite_master where type = 'table' and name = '%s'";
			queryStr = String.format(queryStr, tableName);
			Cursor c = db.rawQuery(queryStr, null);
			String tableCreateSql = null;
			try {
				if (c != null && c.moveToFirst()) {
					tableCreateSql = c.getString(c.getColumnIndex("sql"));
				}
			} finally {
				if (c != null)
					c.close();
			}
			if (tableCreateSql != null && tableCreateSql.contains(fieldName))
				return true;
			return false;
		}
}

