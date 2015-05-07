package com.ldci.android.firstgroup.activity;

import java.util.Calendar;
import java.util.Date;

import com.ldci.android.firstgroup.R;
import com.ldci.android.firstgroup.adapter.MenuAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class WeekCycle extends Activity {

	private Button btSelectAll, btSelectNull, btReverseSelect;
	private CheckBox cb00, cb01, cb02, cb03, cb04, cb05, cb06;
	private GridView  gv;
	private MenuAdapter madapter;
	
	private int CAT;
	private String startdate;
	private String loop;
	private String loopinfo;
	
	private int[] gv_array = { R.string.detail_back, R.string.detail_cancel, R.string.detail_ok };
	private String[] gv_arraystr = new String[gv_array.length];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.weekcycle);
	
		findView();
		
		//获得传送的意图
		Intent it=this.getIntent();
		CAT = it.getExtras().getInt("CAT");
		startdate = it.getExtras().getString("startdate");
		loop = it.getExtras().getString("loop");
		loopinfo = it.getExtras().getString("loopinfo");
		
		if (loop.equals("0")) // 只有当循环类型为周循环时才需要转换循环参数
		{
			int iloopinfo = Integer.valueOf(loopinfo);
			if (iloopinfo / 1000000 == 1) {
				cb00.setChecked(true);
				iloopinfo = iloopinfo % 1000000;
			}
			if (iloopinfo / 100000 == 1) {
				cb01.setChecked(true);
				iloopinfo = iloopinfo % 100000;
			}
			if (iloopinfo / 10000 == 1) {
				cb02.setChecked(true);
				iloopinfo = iloopinfo % 10000;
			}
			if (iloopinfo / 1000 == 1) {
				cb03.setChecked(true);
				iloopinfo = iloopinfo % 1000;
			}
			if (iloopinfo / 100 == 1) {
				cb04.setChecked(true);
				iloopinfo = iloopinfo % 100;
			}
			if (iloopinfo / 10 == 1) {
				cb05.setChecked(true);
				iloopinfo = iloopinfo % 10;
			}
			if (iloopinfo / 1 == 1) {
				cb06.setChecked(true);
			}
		}
		 
		//设置GridView
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
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
			{
				switch(arg2){
				
				case 0: //返回
					finish();
					break;
				case 1://取消
					setResult(Main.RESULT_USER_CANCEL);
					finish();
					break;
				case 2:// 确定
					String infoString = "";
					if (cb00.isChecked()) {
						infoString = infoString+"1";
					}else {
						infoString = infoString +"0";
					}
					if (cb01.isChecked()) {
						infoString = infoString+"1";
					}else {
						infoString = infoString +"0";
					}
					if (cb02.isChecked()) {
						infoString = infoString+"1";
					}else {
						infoString = infoString +"0";
					}
					if (cb03.isChecked()) {
						infoString = infoString+"1";
					}else {
						infoString = infoString +"0";
					}
					if (cb04.isChecked()) {
						infoString = infoString+"1";
					}else {
						infoString = infoString +"0";
					}
					if (cb05.isChecked()) {
						infoString = infoString+"1";
					}else {
						infoString = infoString +"0";
					}
					if (cb06.isChecked()) {
						infoString = infoString+"1";
					}else {
						infoString = infoString +"0";
					}
					
					Intent intent = new Intent();
					intent.putExtra("startdate", startdate);
					intent.putExtra("loopinfo", infoString);
										
					setResult(RESULT_OK, intent);
					finish();
					break;
				}
			}});
		
		btSelectAll.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cb00.setChecked(true);
				cb01.setChecked(true);
				cb02.setChecked(true);
				cb03.setChecked(true);
				cb04.setChecked(true);
				cb05.setChecked(true);
				cb06.setChecked(true);
				
			}});
		
		btSelectNull.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cb00.setChecked(false);
				cb01.setChecked(false);
				cb02.setChecked(false);
				cb03.setChecked(false);
				cb04.setChecked(false);
				cb05.setChecked(false);
				cb06.setChecked(false);
				
			}});
		
		btReverseSelect.setOnClickListener(new OnClickListener(){

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
				if(cb04.isChecked()){
					cb04.setChecked(false);	
				}else{
					cb04.setChecked(true);
				}
				if(cb05.isChecked()){
					cb05.setChecked(false);	
				}else{
					cb05.setChecked(true);
				}
				if(cb06.isChecked()){
					cb06.setChecked(false);	
				}else{
					cb06.setChecked(true);
				}
			}});
	}	
	
	public void findView(){
		btSelectAll = (Button)findViewById(R.id.btSelectAll);	
		btSelectNull = (Button)findViewById(R.id.btSelectNull);	
		btReverseSelect = (Button)findViewById(R.id.btReverseSelect);
		cb00 = (CheckBox)findViewById(R.id.cb00);
		cb01 = (CheckBox)findViewById(R.id.cb01);
		cb02 = (CheckBox)findViewById(R.id.cb02);
		cb03 = (CheckBox)findViewById(R.id.cb03);
		cb04 = (CheckBox)findViewById(R.id.cb04);
		cb05 = (CheckBox)findViewById(R.id.cb05);
		cb06 = (CheckBox)findViewById(R.id.cb06);
	}	
}
