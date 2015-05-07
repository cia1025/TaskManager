package com.ldci.android.firstgroup.activity;

import com.ldci.android.firstgroup.R;
import com.ldci.android.firstgroup.adapter.MenuAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class TaskName extends Activity {

	private EditText etName;
	private GridView  gv;
	private MenuAdapter madapter;
	
	private int CAT;
	private String taskname;
	
	private int[] gv_array = { R.string.detail_back, R.string.detail_cancel, R.string.detail_ok };
	private String[] gv_arraystr = new String[gv_array.length];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.taskname);
		
		//匹配XML文件控件
		etName= (EditText) findViewById(R.id.etTaskname);

		//获得意图所传送数据
		Intent it=this.getIntent();
		CAT = it.getExtras().getInt("CAT");
		taskname = it.getExtras().getString("taskname");
		
		 //将控件内容设置为传入的计划名称
		etName.setText(taskname);
		
		for (int i = 0; i < gv_array.length; i++) {
			gv_arraystr[i] = this.getResources().getString(gv_array[i]);
		}
		
	    gv=(GridView)findViewById(R.id.gv);     	
	    madapter = new MenuAdapter(this);
	
	//		        gv.setSelector(R.drawable.icon);
	//		        gv.setBackgroundResource();// 设置背景
	//		        gv.setBackgroundColor(Color.LTGRAY);
	    gv.setNumColumns(3);// 设置每行列数
	    gv.setGravity(Gravity.CENTER);// 位置居中
	    gv.setVerticalSpacing(10);// 垂直间隔
	    gv.setHorizontalSpacing(10);// 水平间隔
	    
	 // 设置菜单Adapter
	    gv.setAdapter(madapter.getMenuAdapter(gv_arraystr));
	
	  //监听菜单点击事件  
	    gv.setOnItemClickListener(new OnItemClickListener(){
	
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
			switch(arg2){
			
			case 0: //返回
				finish();
				break;
			case 1: //取消
				setResult(Main.RESULT_USER_CANCEL);
				finish();
				break;
			case 2: // 确定
				taskname = etName.getText().toString();
				
				Intent it = new Intent();
				it.putExtra("taskname", taskname);
				setResult(RESULT_OK, it);
				finish();
				break;
			}			
		}});
	}
	
	
	
}
