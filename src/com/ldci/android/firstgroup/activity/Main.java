package com.ldci.android.firstgroup.activity;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ldci.android.firstgroup.R;
import com.ldci.android.firstgroup.adapter.MainAdapter;
import com.ldci.android.firstgroup.database.ScheduleExceptionAdapter;
import com.ldci.android.firstgroup.database.TaskAdapter;
import com.ldci.android.firstgroup.database.TaskParameterAdapter;
import com.ldci.android.firstgroup.database.TaskScheduleAdapter;
import com.ldci.android.firstgroup.entity.TaskSEntity;
import com.ldci.android.firstgroup.service.MainService;

public class Main extends ListActivity {

	public static final int REQ_ADD = 100;
	public static final int REQ_EDIT = 101;
	
	public static final int RESULT_USER_CANCEL = 0x00000002;
	
	// 返回 ： RESULT_CANCELED
	// 取消：  RESULT_USER_CANCEL 
	// 确定：  RESULT_OK
	
	private TaskAdapter taskAdapter;
	private TaskScheduleAdapter scheduleAdapter;
	private ScheduleExceptionAdapter scheduleexcAdapter;
	private TaskParameterAdapter taskparaAdapter;

	// 创建用户定义的功能列表
	private List<TaskSEntity> menuList;
	private ListView lView;
	private MainAdapter mAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		// 主界面ListView
		lView = (ListView) findViewById(android.R.id.list);

		// 开启数据库
		taskAdapter = new TaskAdapter(this);
		taskAdapter.open();
		scheduleAdapter = new TaskScheduleAdapter(this);
		scheduleAdapter.open();
		scheduleexcAdapter = new ScheduleExceptionAdapter(this);
		scheduleexcAdapter.open();
		taskparaAdapter = new TaskParameterAdapter(this);
		taskparaAdapter.open();		
		
		menuList = scheduleAdapter.getAll();

		mAdapter = new MainAdapter(this, menuList);
		lView.setAdapter(mAdapter);
		
		registerForContextMenu(getListView());
		
		////////////////////////////////////////////////////////////////
		// 系统启动模拟，当且仅当设置中开关为关闭状态时发送
		////////////////////////////////////////////////////////////////
		SharedPreferences settings = getSharedPreferences(MainService.SETTING, MODE_PRIVATE);
		boolean  switcher= settings.getBoolean(MainService.SETTING_SWITCHER, true);
		
		if (switcher == false)
		{
			Intent intent = new Intent(MainService.ACTION_BOOT_COMPLETED1);
			sendBroadcast(intent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		// 新增任务
		menu.add(0, 0, 0, R.string.Menu_menuitem1);
		// 编辑任务
		menu.add(0, 1, 1, R.string.Menu_menuitem2);
//		// 删除任务
//		menu.add(0, 2, 3, R.string.Menu_menuitem4);
		// 设置
		menu.add(0, 3, 4, R.string.Menu_menuitem5);
		// 退出
		menu.add(0, 4, 5, R.string.Menu_menuitem3);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {

		case 0: // 添加新任务
			Intent i0 = new Intent();
			i0.setClass(Main.this, AddNewTask.class);
			i0.putExtra("CAT", REQ_ADD);
			
			startActivityForResult(i0, REQ_ADD);
			break;

		case 1: // 编辑任务
			int position = lView.getSelectedItemPosition();
			if ( position == -1){
				Toast.makeText(Main.this, getString(R.string.alert_noselecteditem), Toast.LENGTH_LONG).show();
				return super.onOptionsItemSelected(item);
			}
			
			TaskSEntity entity = menuList.get(position);
			Intent i1 = new Intent();
			i1.setClass(Main.this, Detail.class);
			i1.putExtra("CAT", REQ_EDIT);
			
			//绑定计划任务
			Detail._taskSchedule = entity;
			
			startActivityForResult(i1, REQ_EDIT);
			break;
//			
//		case 2: // 删除任务
//			position = lView.getSelectedItemPosition();
//			
//			if ( position == -1){
//				Toast.makeText(Main.this, getString(R.string.alert_noselecteditem), Toast.LENGTH_LONG).show();
//				return super.onOptionsItemSelected(item);
//			}
//			
//			entity = menuList.get(position);
//			//  删除计划任务表\任务参数表\状态例外表
//			scheduleAdapter.delete(entity.getScheduleid());
//			taskparaAdapter.delete(entity.getScheduleid());
//			scheduleexcAdapter.delete(entity.getScheduleid());
//			
//			refresh();
//			
//			//**********************************************
//			//发送消息给StartupReceiver，重启服务，注册所有task
//			//**********************************************
//			Intent intent = new Intent(MainService.ACTION_SERVICE_RESTART);
//			sendBroadcast(intent);
//			break;
			
		case 3: // 设置
			Intent i3 = new Intent(Main.this, Settings.class);
			startActivity(i3);
			break;

		case 4: //退出
			finish();
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterView.AdapterContextMenuInfo info;
		info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		
		switch (item.getItemId()) 
		{
		case 0:
			scheduleAdapter.delete(String.valueOf(info.id));
			taskparaAdapter.delete(String.valueOf(info.id));
			scheduleexcAdapter.delete(String.valueOf(info.id));
			
			refresh();
			
			//**********************************************
			//发送消息给StartupReceiver，重启服务，注册所有task
			//**********************************************
			Intent intent = new Intent(MainService.ACTION_SERVICE_RESTART);
			sendBroadcast(intent);
			break;
		}
		
		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		
		menu.add(0, 0, 0, R.string.Menu_menuitem4);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);

		// 根据listview点击行数 获得menulist中具体信息对象
		TaskSEntity entity = menuList.get(position);

		Intent it = new Intent();
		it.setClass(Main.this, Detail.class);

		// 通过Detail的静态成员变量绑定计划任务
		it.putExtra("CAT", REQ_EDIT);
		Detail._taskSchedule = entity;		
		startActivityForResult(it, REQ_EDIT);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if(resultCode == RESULT_OK){
			refresh();
			
		}else if (resultCode == RESULT_USER_CANCEL){
			// do nothing
		}
	}
	
	private void refresh(){
		
		menuList = scheduleAdapter.getAll();
		mAdapter = new MainAdapter(this, menuList);
		lView.setAdapter(mAdapter);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		scheduleAdapter.close();
		taskAdapter.close();
		scheduleexcAdapter.close();
		taskparaAdapter.close();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

	}

}