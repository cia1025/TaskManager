package com.ldci.android.firstgroup.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ldci.android.firstgroup.entity.ScheduleExceptionEntity;

public class ScheduleExceptionAdapter {
		public static final String DB_NAME = "manager.db";
		public static final String TBL_SCHEDULEEXCEPTION = "tbl_ScheduleException";
		//���ݿ��ֶ���
		public static final String T_SID = "_id";
		public static final String T_STID = "stateid";
		private Context ctx;
		private DbHelper dbHelper;
		private SQLiteDatabase db;
		
		public ScheduleExceptionAdapter(Context ctx){
			this.ctx = ctx;
		}
		
		//�������ݿ�
		public ScheduleExceptionAdapter open(){
			dbHelper = new DbHelper(ctx);
			db = dbHelper.getWritableDatabase();
			return this;
		}
		
		//�ر����ݿ�
		public void close(){
			dbHelper.close();
		}
		
		//���Ӳ�������
		public long create(String sid, String stid){
				ContentValues cValues = new ContentValues();
				cValues.put(T_SID, sid);
				cValues.put(T_STID, stid);
				
			return db.insert(TBL_SCHEDULEEXCEPTION, null, cValues);
		}
		
		//��ѯ��������
		public List<ScheduleExceptionEntity> getAll(){
			String[] col = {T_SID, T_STID};
			Cursor cursor = db.query(TBL_SCHEDULEEXCEPTION, col, null, null, null, null, null);
			
			List<ScheduleExceptionEntity> result = new ArrayList<ScheduleExceptionEntity>();
			if (cursor.moveToFirst()) {
				do {
					ScheduleExceptionEntity entity = new ScheduleExceptionEntity();
					entity.setSid(cursor.getString(0));
					entity.setStid(cursor.getString(1));
					result.add(entity);
				}while (cursor.moveToNext());				
			}
			
			return result;
		}
		
		//ͨ��ScheduleId��ѯ
		public List<ScheduleExceptionEntity> getItemsBySId(String sId){
			String[] col = {T_SID, T_STID};
			Cursor cursor = db.query(TBL_SCHEDULEEXCEPTION, col, T_SID + "=" + sId, null, null, null, null);
			
			List<ScheduleExceptionEntity> result = new ArrayList<ScheduleExceptionEntity>();
			if (cursor.moveToFirst()) {
				do {
					ScheduleExceptionEntity entity = new ScheduleExceptionEntity();
					entity.setSid(cursor.getString(0));
					entity.setStid(cursor.getString(1));
					result.add(entity);
				}while (cursor.moveToNext());				
			}
			
			return result;
		}
		
		//ɾ����¼
		public void delete(String sid, String stid){
				db.delete(TBL_SCHEDULEEXCEPTION, T_SID+"="+sid + " and " + T_STID + "="+stid, null);
		}
		
		//ɾ����¼
		public void delete(String sid){
				db.delete(TBL_SCHEDULEEXCEPTION, T_SID+"="+sid, null);
		}
		
		//�������ݿ������
		private static class DbHelper extends SQLiteOpenHelper{

			public DbHelper(Context context) {
				super(context, DB_NAME, null, 1);
				// TODO Auto-generated constructor stub
			}

			@Override
			public void onCreate(SQLiteDatabase arg0) {
				// TODO Auto-generated method stub
				String CREATE_TABLE = "create table " +TBL_SCHEDULEEXCEPTION +
					"( " +
					T_SID+" intege," +
					T_STID+" integer" +
					")";
				arg0.execSQL(CREATE_TABLE);
			}

			@Override
			public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub
				arg0.execSQL("drop table if exists "+TBL_SCHEDULEEXCEPTION);
				onCreate(arg0);
			}			
		}
}
