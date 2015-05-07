package com.ldci.android.firstgroup.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

import com.ldci.android.firstgroup.R;
import com.ldci.android.firstgroup.adapter.MenuAdapter;

public class Except extends Activity {

	private Button butSelectAll, butSelectNull, butReverseSelect;
	private CheckBox cb00, cb01, cb02, cb03;
	private GridView  gv;
	private MenuAdapter madapter;
	
	private String[] exceptstatus;
	private int CAT;
	
	private int[] gv_array = { R.string.detail_back, R.string.detail_cancel, R.string.detail_ok };
	private String[] gv_arraystr = new String[gv_array.length];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.except);
	
		findView();
		
		Intent in = getIntent();
		//exceptstatus = in.getExtras().getStringArray("exceptstatus");
		
		//根据传入的例外状态设置
//		for (int i=0; i<exceptstatus.length; i++)
//		{
//			if (exceptstatus[i].equals("0"))
//				cb00.setChecked(true);
//			else if (exceptstatus[i].equals("1"))
//				cb01.setChecked(true);
//			else if (exceptstatus[i].equals("2"))
//				cb02.setChecked(true);
//			else if (exceptstatus[i].equals("3"))
//				cb03.setChecked(true);
//		}
		 
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
				break;
			}
		}});
		
		butSelectAll.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cb00.setChecked(true);
				cb01.setChecked(true);
				cb02.setChecked(true);
				cb03.setChecked(true);
				
			}});
		
		butSelectNull.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cb00.setChecked(false);
				cb01.setChecked(false);
				cb02.setChecked(false);
				cb03.setChecked(false);
				
			}});
		
		butReverseSelect.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(cb00.isChecked()){
					cb00.setChecked(false);	
				}else{
					cb00.setChecked(true);
				}
				if(cb01.isChecked()){
					cb01.setChecked(false);	
				}else{
					cb01.setChecked(true);
				}
				if(cb02.isChecked()){
					cb02.setChecked(false);	
				}else{
					cb02.setChecked(true);
				}
				if(cb03.isChecked()){
					cb03.setChecked(false);	
				}else{
					cb03.setChecked(true);
				}
			}});
	}	
	
	public void findView(){
		butSelectAll = (Button)findViewById(R.id.butSelectAll);	
		butSelectNull = (Button)findViewById(R.id.butSelectNull);	
		butReverseSelect = (Button)findViewById(R.id.butReverseSelect);
		cb00 = (CheckBox)findViewById(R.id.cb00);
		cb01 = (CheckBox)findViewById(R.id.cb01);
		cb02 = (CheckBox)findViewById(R.id.cb02);
		cb03 = (CheckBox)findViewById(R.id.cb03);
	}

}
