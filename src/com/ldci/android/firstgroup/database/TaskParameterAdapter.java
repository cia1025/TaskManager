package com.ldci.android.firstgroup.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ldci.android.firstgroup.entity.TaskParameterEntity;

public class TaskParameterAdapter {
		public static final String DB_NAME = "manager.db";
		public static final String TB_NAME = "tbl_TaskParameter";
		//数据库字段名
		public static final String T_SID = "_id";
		public static final String T_PID = "pid";
		public static final String T_VALUE = "value";
		private Context ctx;
		private DbHelper dbHelper;
		private SQLiteDatabase db;
		
		public TaskParameterAdapter(Context ctx){
			this.ctx = ctx;
		}
		
		//创建数据库
		public TaskParameterAdapter open(){
			dbHelper = new DbHelper(ctx);
			db = dbHelper.getWritableDatabase();
			return this;
		}
		
		//关闭数据库
		public void close(){
			dbHelper.close();
		}
		
		public long create(TaskParameterEntity tpe){
			ContentValues cValues = new ContentValues();
			cValues.put(T_SID, tpe.getSid());
			cValues.put(T_PID, tpe.getPid());
			cValues.put(T_VALUE, tpe.getValue());
			
		return db.insert(TB_NAME, null, cValues);
	}
		
		//查询所有数据
		public List<TaskParameterEntity> getAll(){
			String[] col = {T_SID, T_PID, T_VALUE};
			Cursor cursor = db.query(TB_NAME, col, null, null, null, null, null);
			
			List<TaskParameterEntity> result = new ArrayList<TaskParameterEntity>(); 
			if (cursor.moveToFirst()) {
				do {
					TaskParameterEntity entity = new TaskParameterEntity();
					entity.setSid(cursor.getString(0));
					entity.setPid(cursor.getString(1));
					entity.setValue(cursor.getString(2));
					result.add(entity);
				} while(cursor.moveToNext());
			}
			
			return result;
		}
		
		//根据计划ID查询所有参数
		public List<TaskParameterEntity> getItemsBySId(String sId){
			String[] col = {T_SID, T_PID, T_VALUE};
			Cursor cursor = db.query(TB_NAME, col, T_SID + "=" + sId, null, null, null, null);
			
			List<TaskParameterEntity> result =  new ArrayList<TaskParameterEntity>();
			if (cursor.moveToFirst()) {
				do {
					TaskParameterEntity entity = new TaskParameterEntity();
					entity.setSid(cursor.getString(0));
					entity.setPid(cursor.getString(1));
					entity.setValue(cursor.getString(2));
					result.add(entity);
				} while(cursor.moveToNext());
			}
			
			return result;
		}
		
		//更新数据库
		public void update(TaskParameterEntity entity){
			ContentValues cValues = new ContentValues();
			cValues.put(T_SID, entity.getSid());
			cValues.put(T_PID, entity.getPid());
			cValues.put(T_VALUE, entity.getValue());
			db.update(TB_NAME, cValues, T_SID+"="+entity.getSid() + 
					" and " + T_PID +"="+entity.getPid(), null);
		}
		
		//删除记录
		public void delete(String sid, String pid){
				db.delete(TB_NAME, T_SID+"="+sid + " and " + T_PID + "="+pid, null);
		}
		
		//删除记录
		public void delete(String sid){
				db.delete(TB_NAME, T_SID+"="+sid, null);
		}
		
		//创建数据库帮助类
		private static class DbHelper extends SQLiteOpenHelper{

			public DbHelper(Context context) {
				super(context, DB_NAME, null, 1);
				// TODO Auto-generated constructor stub
			}

			@Override
			public void onCreate(SQLiteDatabase arg0) {
				// TODO Auto-generated method stub
				String CREATE_TABLE = "create table " +TB_NAME +
					"( " +
					T_SID+" integer," +
					T_PID+" integer," +
					T_VALUE+" text not null," +
					")";
				arg0.execSQL(CREATE_TABLE);
			}

			@Override
			public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub
				arg0.execSQL("drop table if exists "+TB_NAME);
				onCreate(arg0);
			}			
		}
}
