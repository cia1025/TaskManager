package com.ldci.android.firstgroup.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.ldci.android.firstgroup.entity.ParameterEntity;

public class ParameterAdapter {
		//���ݿ���
		public static final String DB_NAME = "manager.db";
		public static final String TB_NAME = "tbl_Parameter";
		//���ݿ��ֶ���
		public static final String T_ID = "id";
		public static final String T_NAME = "name";
		private Context ctx;
		private DbHelper dbHelper;
		private SQLiteDatabase db;
		
		public ParameterAdapter(Context ctx){
			this.ctx = ctx;
		}
		
		//�������ݿ�
		public ParameterAdapter open(){
			dbHelper = new DbHelper(ctx);
			db = dbHelper.getWritableDatabase();
			return this;
		}
		
		//�ر����ݿ�
		public void close(){
			dbHelper.close();
		}
		
		//���Ӳ�������
		public long create(String name){
				ContentValues cValues = new ContentValues();
				cValues.put(T_NAME, name);
				
			return db.insert(TB_NAME, null, cValues);
		}
		
		//��ѯ��������
		public List<ParameterEntity> getAll(){
			String[] col = {T_ID,T_NAME};
			Cursor cursor = db.query(TB_NAME, col, null, null, null, null, null);
			
			ParameterEntity entity = null;
			List<ParameterEntity> result = new ArrayList<ParameterEntity>();
			if (cursor.moveToFirst()) {
				do{
					entity = new ParameterEntity();
					entity.setId(cursor.getString(0));
					entity.setName(cursor.getString(1));
					result.add(entity);
				}while(cursor.moveToNext());				
			}
			
			return result;
		}
		
		//�������ݿ�
		public void update(ParameterEntity entity){
			ContentValues cValues = new ContentValues();
			cValues.put(T_NAME, entity.getName());
			db.update(TB_NAME, cValues, T_ID+"="+entity.getId(), null);
		}
		
		//ɾ����¼
		public void delete(String id){
				db.delete(TB_NAME, T_ID+"="+id, null);
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
				String CREATE_TABLE = "create table " +TB_NAME +
					"( " +
					T_ID+" integer primary key AUTOINCREMENT," +
					T_NAME+" text not null," +
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
