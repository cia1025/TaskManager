package com.ldci.android.firstgroup.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.AdapterView.OnItemClickListener;

import com.ldci.android.firstgroup.R;
import com.ldci.android.firstgroup.adapter.MenuAdapter;
import com.ldci.android.firstgroup.service.MainService;

public class Settings   extends Activity  {
	private ImageButton settingsib;
	private Boolean isCheck = true;
	private GridView gv;
	//菜单数组
	private int[] gv_array = { R.string.detail_back, R.string.detail_about };
	private String[] gv_arraystr = new String[gv_array.length];
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.settings);
		
		for (int i = 0; i < gv_array.length; i++) {
			gv_arraystr[i] = this.getResources().getString(gv_array[i]);
		}
		
		//根据ID得到控件
		gv = (GridView) findViewById(R.id.gv);
		settingsib=(ImageButton) findViewById(R.id.settings_ib);
		isCheck = false;
		
		//得到当前的系统设置
		restoreSettings();
		
		//设置菜单选项
		MenuAdapter madapter;
		madapter = new MenuAdapter(this);

		// gv.setSelector(R.drawable.icon);
		// gv.setBackgroundResource();// 设置背景
		gv.setNumColumns(2);// 设置每行列数
		gv.setGravity(Gravity.CENTER);// 位置居中
		gv.setVerticalSpacing(10);// 垂直间隔
		gv.setHorizontalSpacing(10);// 水平间隔
		gv.setAdapter(madapter.getMenuAdapter(gv_arraystr));// 设置菜单Adapter
		
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
			case 1: // 关于
				new AlertDialog.Builder(Settings.this)
    			.setTitle(R.string.detail_about)
    			.setMessage(R.string.app_name)
    			.setPositiveButton("OK", 
    					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
					}
				})
    			.show();
				break;
			}			
		}});
		
		// 设置开关ImageButton监听事件
		settingsib.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				if (v.getId() == settingsib.getId()) {
					SharedPreferences settings = getSharedPreferences(MainService.SETTING, MODE_WORLD_READABLE);
					
					if (!isCheck) {
						settingsib.setImageResource(R.drawable.bton);
						isCheck = true;
						
						settings.edit()
									.putBoolean(MainService.SETTING_SWITCHER, isCheck)
									.commit();
						
						// 启动服务
						Intent intent = new Intent(MainService.ACTION_SERVICE_STOP);
						sendBroadcast(intent);
					}else{
						settingsib.setImageResource(R.drawable.btoff);
						isCheck = false;
						
						settings.edit()
									.putBoolean(MainService.SETTING_SWITCHER, isCheck)
									.commit();
						
						//终止服务
						Intent intent = new Intent(MainService.ACTION_SERVICE_START);
						sendBroadcast(intent);
					}
				}
			}
		});
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
//		SharedPreferences settings = getSharedPreferences(SETTING, MODE_WORLD_READABLE);
//		settings.edit()
//					.putBoolean(SETTING_SWITCHER, isCheck)
//					.commit();
	}	
	
	private void restoreSettings()
	{
		SharedPreferences settings = getSharedPreferences(MainService.SETTING, MODE_PRIVATE);
		
		isCheck = settings.getBoolean(MainService.SETTING_SWITCHER, false);
		if (isCheck)
			settingsib.setImageResource(R.drawable.bton);
		else
			settingsib.setImageResource(R.drawable.btoff);
	}
}