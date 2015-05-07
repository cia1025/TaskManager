package com.ldci.android.firstgroup.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ldci.android.firstgroup.entity.TaskSEntity;

public class TaskScheduleAdapter {
	public static final String DB_NAME = "manager.db";
	//表名
	public static final String TBNAME= "Tbl_TaskSchedule";
	//数据库字段名
	public static final String SCHEDULEID = "_id";
	public static final String SCHEDULENAME = "sname";
	public static final String TASKID = "taskid";
	public static final String STARTDATE = "startdate";
	public static final String STARTTIME = "starttime";
	public static final String LOOP = "loop";
	public static final String LOOPINFO= "loopinfo";
	public static final String SWITHCHER = "switcher";
	public static final String ALERTER = "alerter";
	
	private Context ctx;
	private TaskScheduleHelper tsHelper;
	private SQLiteDatabase db;
	
	public TaskScheduleAdapter(Context ctx){
		this.ctx = ctx;
	}
	//创建数据库
	public TaskScheduleAdapter open(){
		tsHelper = new TaskScheduleHelper(ctx);
		db =  tsHelper.getWritableDatabase();
		return this;
	}
	//关闭数据库
	public void close(){
		tsHelper.close();
	}
	//插入数据
	public long create(TaskSEntity tse){
		ContentValues cv = new ContentValues();
		cv.put(SCHEDULENAME, tse.getSname());
		cv.put(TASKID, tse.getTaskid());
		cv.put(STARTDATE, tse.getStartdate());
		cv.put(STARTTIME, tse.getStarttime());
		cv.put(LOOP, tse.getLoop());
		cv.put(LOOPINFO, tse.getLoopinfo());
		cv.put(SWITHCHER, tse.getSwitcher());
		cv.put(ALERTER, tse.getAlerter());
		
		return db.insert(TBNAME, null, cv);
	}
	
	//删除数据
	public void delete(String sId){
			db.delete(TBNAME, SCHEDULEID+"="+sId, null);
	}
	
	//更新数据
	public void update(TaskSEntity tse){
		ContentValues cv = new ContentValues();
		cv.put(SCHEDULENAME, tse.getSname());
		cv.put(TASKID, tse.getTaskid());
		cv.put(STARTDATE, tse.getStartdate());
		cv.put(STARTTIME, tse.getStarttime());
		cv.put(LOOP, tse.getLoop());
		cv.put(LOOPINFO, tse.getLoopinfo());
		cv.put(SWITHCHER, tse.getSwitcher());
		cv.put(ALERTER, tse.getAlerter());
		
		db.update(TBNAME, cv, SCHEDULEID+"="+tse.getScheduleid(), null);
	}
	
	//查询全部
	public List<TaskSEntity> getAll(){
		
		String[] col={SCHEDULEID, SCHEDULENAME, TASKID,STARTDATE, STARTTIME,LOOP,LOOPINFO,SWITHCHER,ALERTER};
		Cursor cursor = db.query(TBNAME, col, null, null, null, null, null);
		List<TaskSEntity> list = new ArrayList<TaskSEntity>();
		
		if (cursor.moveToFirst()) {
			do {
				TaskSEntity tSEntity = new TaskSEntity();
				tSEntity.setScheduleid(String.valueOf(cursor.getInt(0)));
				tSEntity.setSname(cursor.getString(1));
				tSEntity.setTaskid(String.valueOf(cursor.getInt(2)));
				tSEntity.setStartdate(cursor.getString(3));
				tSEntity.setStarttime(cursor.getString(4));
				tSEntity.setLoop(String.valueOf(cursor.getInt(5)));
				tSEntity.setLoopinfo(cursor.getString(6));
				tSEntity.setSwitcher(String.valueOf(cursor.getString(7)));
				tSEntity.setAlerter(String.valueOf(cursor.getInt(8)));
				
				list.add(tSEntity);				
			} while (cursor.moveToNext()); 
		}
		
		return list;
	}
	
	//查询单个
	public TaskSEntity getItem(int id){
		String[] col={SCHEDULEID, SCHEDULENAME, TASKID,STARTDATE, STARTTIME,LOOP,LOOPINFO,SWITHCHER,ALERTER};
		Cursor cursor = db.query(TBNAME, col, SCHEDULEID+"="+id, null, null, null, null);
		TaskSEntity tSEntity = new TaskSEntity();
		if (cursor.moveToFirst()) {
			
			tSEntity.setScheduleid(String.valueOf(cursor.getInt(0)));
			tSEntity.setSname(cursor.getString(1));
			tSEntity.setTaskid(String.valueOf(cursor.getInt(2)));
			tSEntity.setStartdate(cursor.getString(3));
			tSEntity.setStarttime(cursor.getString(4));
			tSEntity.setLoop(String.valueOf(cursor.getInt(5)));
			tSEntity.setLoopinfo(cursor.getString(6));
			tSEntity.setSwitcher(String.valueOf(cursor.getString(7)));
			tSEntity.setAlerter(String.valueOf(cursor.getInt(8)));
		}
		
		return tSEntity;
	}
	
	public TaskSEntity geLatestItem(){
		String[] col={SCHEDULEID, SCHEDULENAME, TASKID,STARTDATE, STARTTIME,LOOP,LOOPINFO,SWITHCHER,ALERTER};
		Cursor cursor = db.query(TBNAME, col, null, null, null, null, SCHEDULEID + " desc");
		TaskSEntity tSEntity = new TaskSEntity();
		if (cursor.moveToFirst()) {
			
			tSEntity.setScheduleid(String.valueOf(cursor.getInt(0)));
			tSEntity.setSname(cursor.getString(1));
			tSEntity.setTaskid(String.valueOf(cursor.getInt(2)));
			tSEntity.setStartdate(cursor.getString(3));
			tSEntity.setStarttime(cursor.getString(4));
			tSEntity.setLoop(String.valueOf(cursor.getInt(5)));
			tSEntity.setLoopinfo(cursor.getString(6));
			tSEntity.setSwitcher(String.valueOf(cursor.getString(7)));
			tSEntity.setAlerter(String.valueOf(cursor.getInt(8)));
		}
		
		return tSEntity;
	}
	
	//创建数据库操作类
	public static class TaskScheduleHelper extends SQLiteOpenHelper{

		public TaskScheduleHelper(Context context) {
			super(context,DB_NAME, null, 1);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			String CREATE_TABLE ="create table " +TBNAME +
			"( " +
			SCHEDULEID+ " integer primary key AUTOINCREMENT,"+
			SCHEDULENAME+" text not null," +
			TASKID+ " integer not null,"+
			STARTDATE+" text not null," +
			STARTTIME+" text not null," +
			LOOP+" integer not null," +
			LOOPINFO+" text not null," +
			SWITHCHER+"text not null," +
			ALERTER+"integer" +
			")";
			db.execSQL(CREATE_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("drop table if exists "+TBNAME);
			onCreate(db);
		}
			
	}
} 
