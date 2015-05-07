package com.ldci.android.firstgroup.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ldci.android.firstgroup.R;
import com.ldci.android.firstgroup.entity.TaskEntity;

public class TaskAdapter {
	//数据库名
	public static final String DB_NAME = "manager.db";
	//表名
	public static final String TBL_TASK= "Tbl_Task";
	public static final String TBL_TASKSCHEDULE= "Tbl_TaskSchedule";
	public static final String TBL_TASKPARAMETER= "Tbl_TaskParameter";
	public static final String TBL_SCHEDULEEXCEPTION = "Tbl_ScheduleException";
	
	//Task fileds
	public static final String TASKID ="_id";
	public static final String TASKNAME = "name";
	public static final String TASKSTATE = "state" ;
	
	// TaskSchedule fileds
	public static final String SCHEDULEID = "_id";
	public static final String SCHEDULENAME = "sname";
	public static final String STASKID = "taskid";
	public static final String STARTDATE = "startdate";
	public static final String STARTTIME = "starttime";
	public static final String LOOP = "loop";
	public static final String LOOPINFO= "loopinfo";
	public static final String SWITHCHER = "switcher";
	public static final String ALERTER = "alerter";
	
	// TaskParameter fileds
	public static final String PARAMETERID= "pid";
	public static final String VALUE= "value";
	
	//ScheduleException fileds
	public static final String STATEID= "stateid";
	
//	public static int[] task = { 
//			R.string.function_item1, R.string.function_item2,
//			R.string.function_item3, R.string.function_item4,
//			R.string.function_item5, R.string.function_item6,
//			R.string.function_item7, R.string.function_item8,
//			R.string.function_item9, R.string.function_item10,
//			R.string.function_item11, R.string.function_item12,
//			R.string.function_item13, R.string.function_item14,
//			R.string.function_item15, R.string.function_item16,
//			R.string.function_item17, R.string.function_item18,
//			R.string.function_item19, R.string.function_item20 };
	public static int[] task = { 
		R.string.function_item1, R.string.function_item2,
		R.string.function_item3, R.string.function_item4,
		R.string.function_item5, R.string.function_item6,
		R.string.function_item7, R.string.function_item8 };
	public static String[] taskstr = new String[task.length];
	
	private static Context ctx;
	private TaskHelper tHelper;
	private SQLiteDatabase db;
	
	public TaskAdapter(Context context){
		ctx = context;
	}
	
	//创建数据库
	public TaskAdapter open(){
		tHelper = new TaskHelper(ctx);
		db =  tHelper.getWritableDatabase();
		return this;
	}
	//关闭数据库
	public void close(){
		tHelper.close();
	}
	
	//插入数据
	public long create(TaskEntity te){
		ContentValues cv = new ContentValues();
		cv.put(TASKID, te.getTaskid());
		cv.put(TASKNAME, te.getTaskname());
		cv.put(TASKSTATE, te.getIschecked());
		return db.insert(TBL_TASK, null, cv);
	}
	
	public long create(String name, String isChecked){
		ContentValues cv = new ContentValues();
		cv.put(TASKNAME, name);
		cv.put(TASKSTATE, isChecked);
		return db.insert(TBL_TASK, null, cv);
	}
	
	//删除数据
	public void delete(TaskEntity te){
		db.delete(TBL_TASK, TASKID +"="+te.getTaskid() , null);
	}
	
	//更新
	public void update(TaskEntity te){
		ContentValues cv = new ContentValues();
		cv.put(TASKID, te.getTaskid());
		cv.put(TASKNAME, te.getTaskname());
		cv.put(TASKSTATE , te.getIschecked());
		db.update(TBL_TASK, cv, TASKID+"="+te.getTaskid(), null);
	}
	
	//查询所有
	public List<TaskEntity> getAll(){
		String[] col = {TASKID, TASKNAME , TASKSTATE};
		Cursor cursor = db.query(TBL_TASK, col, null, null, null, null, null);
		List<TaskEntity> list = new ArrayList<TaskEntity>();
		
		if (cursor.moveToFirst()) {
			do {
				TaskEntity tEntity = new TaskEntity();
				tEntity.setTaskid(cursor.getString(0));
				tEntity.setTaskname(cursor.getString(1));
				tEntity.setIschecked(cursor.getString(2));
				list.add(tEntity);
			}while (cursor.moveToNext());
		}
		
		return list;
	}
	
	//查询单个
	public TaskEntity getItem(String id){
		TaskEntity tEntity = null ;
		String[] col = {TASKID, TASKNAME , TASKSTATE};
		Cursor cursor = db.query(TBL_TASK, col, TASKID+"="+id, null, null, null, null);
		
		if (cursor.moveToFirst()) {
			tEntity = new TaskEntity();
			tEntity.setTaskid(cursor.getString(0));
			tEntity.setTaskname(cursor.getString(1));
			tEntity.setIschecked(cursor.getString(2));
		}
		
		return tEntity;
	}
	
	//数据库助手类
	public static class TaskHelper extends SQLiteOpenHelper{

		public TaskHelper(Context context) {
			super(context, DB_NAME, null, 1);
			// TODO Auto-generated constructor stub
		}		
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			
			//创建表Task
			String CREATE_TABLE ="create table " +TBL_TASK +
			"( " +
			TASKID+ " integer primary key AUTOINCREMENT,"+
			TASKNAME+ " text not null,"+
			TASKSTATE+" text not null" +
			")";
			db.execSQL(CREATE_TABLE);
			
			//创建表TaskSchedule
			CREATE_TABLE ="create table " +TBL_TASKSCHEDULE +
			"( " +
			SCHEDULEID+ " integer primary key AUTOINCREMENT,"+
			SCHEDULENAME+" text not null," +
			STASKID+ " integer not null," + 
			STARTDATE+" text not null," +
			STARTTIME+" text not null," +
			LOOP+" integer not null," +
			LOOPINFO+" text not null," +
			SWITHCHER+" text not null," +
			ALERTER+" integer" +
			")";
			db.execSQL(CREATE_TABLE);
			
			//创建表tbl_TaskParameter
			 CREATE_TABLE = "create table " +TBL_TASKPARAMETER +
			"( " +
			SCHEDULEID+" integer," +
			PARAMETERID+" integer," +
			VALUE+" text not null" +
			")";
			 db.execSQL(CREATE_TABLE);
					
			//创建表Tbl_ScheduleException
			CREATE_TABLE = "create table "+ TBL_SCHEDULEEXCEPTION +" ( " +
			SCHEDULEID+ " integer not null, "+	
			STATEID + " integer not null"+
			")";
			db.execSQL(CREATE_TABLE);		
			
			// 初始化Tbl_Task表
			for (int i = 0; i < task.length; i++) {
				taskstr[i] = ctx.getResources().getString(task[i]);
			}
			
			for (int i = 0; i < taskstr.length; i++) {
				
				ContentValues cValues = new ContentValues();
				cValues.put(TASKNAME, taskstr[i]);
				cValues.put(TASKSTATE, "true");
				
				db.insert(TBL_TASK, null, cValues);
			}			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub			
			db.execSQL("drop table if exists "+TBL_TASK);
			db.execSQL("drop table if exists "+TBL_TASKSCHEDULE);
			db.execSQL("drop table if exists "+TBL_TASKPARAMETER);
			db.execSQL("drop table if exists "+TBL_SCHEDULEEXCEPTION);
			
			onCreate(db);
		}
		
	}
}
