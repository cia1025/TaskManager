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
	//�˵�����
	private int[] gv_array = { R.string.detail_back, R.string.detail_about };
	private String[] gv_arraystr = new String[gv_array.length];
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.settings);
		
		for (int i = 0; i < gv_array.length; i++) {
			gv_arraystr[i] = this.getResources().getString(gv_array[i]);
		}
		
		//����ID�õ��ؼ�
		gv = (GridView) findViewById(R.id.gv);
		settingsib=(ImageButton) findViewById(R.id.settings_ib);
		isCheck = false;
		
		//�õ���ǰ��ϵͳ����
		restoreSettings();
		
		//���ò˵�ѡ��
		MenuAdapter madapter;
		madapter = new MenuAdapter(this);

		// gv.setSelector(R.drawable.icon);
		// gv.setBackgroundResource();// ���ñ���
		gv.setNumColumns(2);// ����ÿ������
		gv.setGravity(Gravity.CENTER);// λ�þ���
		gv.setVerticalSpacing(10);// ��ֱ���
		gv.setHorizontalSpacing(10);// ˮƽ���
		gv.setAdapter(madapter.getMenuAdapter(gv_arraystr));// ���ò˵�Adapter
		
		//�����˵�����¼�  
	    gv.setOnItemClickListener(new OnItemClickListener(){
	
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
			switch(arg2){
			
			case 0: //����
				finish();
				break;
			case 1: // ����
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
		
		// ���ÿ���ImageButton�����¼�
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
						
						// ��������
						Intent intent = new Intent(MainService.ACTION_SERVICE_STOP);
						sendBroadcast(intent);
					}else{
						settingsib.setImageResource(R.drawable.btoff);
						isCheck = false;
						
						settings.edit()
									.putBoolean(MainService.SETTING_SWITCHER, isCheck)
									.commit();
						
						//��ֹ����
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