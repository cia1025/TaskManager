package com.ldci.android.firstgroup.activity;

import java.util.List;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.ldci.android.firstgroup.R;
import com.ldci.android.firstgroup.adapter.NewTaskAdapter;
import com.ldci.android.firstgroup.database.TaskAdapter;
import com.ldci.android.firstgroup.entity.TaskEntity;
import com.ldci.android.firstgroup.entity.TaskSEntity;

public class AddNewTask extends ListActivity{

	// 本软件提供的所有功能列表
	private List<TaskEntity> tasklist;
// testing again
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.newtask);	
		
		// 生成添加任务的任务列表
		ListView lView = (ListView) findViewById(android.R.id.list);
		
		TaskAdapter taskAdapter = new TaskAdapter(this);
		taskAdapter.open();
		tasklist = taskAdapter.getAll();
		taskAdapter.close();
		
		// 创建适配器，并绑定到listview
		NewTaskAdapter ntAdapter = new NewTaskAdapter(this, tasklist);
		ntAdapter.setlist(tasklist);	
		lView.setAdapter(ntAdapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		Intent it = new Intent();
		it.setClass(AddNewTask.this, Detail.class);
		
		//此CAT = REQ_ADD表示在DETAIL里面是新增任务
		it.putExtra("CAT", Main.REQ_ADD);
		Detail._taskSchedule = new TaskSEntity();
		Detail._taskSchedule.setTaskid(String.valueOf(position + 1));
		
		startActivityForResult(it, Main.REQ_ADD);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == RESULT_OK){
			setResult(RESULT_OK);
			finish();
		}
		else if (resultCode == Main.RESULT_USER_CANCEL){
			setResult(Main.RESULT_USER_CANCEL);
			finish();
		}
	}	
}
