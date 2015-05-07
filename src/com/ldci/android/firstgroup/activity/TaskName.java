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
		
		//ƥ��XML�ļ��ؼ�
		etName= (EditText) findViewById(R.id.etTaskname);

		//�����ͼ����������
		Intent it=this.getIntent();
		CAT = it.getExtras().getInt("CAT");
		taskname = it.getExtras().getString("taskname");
		
		 //���ؼ���������Ϊ����ļƻ�����
		etName.setText(taskname);
		
		for (int i = 0; i < gv_array.length; i++) {
			gv_arraystr[i] = this.getResources().getString(gv_array[i]);
		}
		
	    gv=(GridView)findViewById(R.id.gv);     	
	    madapter = new MenuAdapter(this);
	
	//		        gv.setSelector(R.drawable.icon);
	//		        gv.setBackgroundResource();// ���ñ���
	//		        gv.setBackgroundColor(Color.LTGRAY);
	    gv.setNumColumns(3);// ����ÿ������
	    gv.setGravity(Gravity.CENTER);// λ�þ���
	    gv.setVerticalSpacing(10);// ��ֱ���
	    gv.setHorizontalSpacing(10);// ˮƽ���
	    
	 // ���ò˵�Adapter
	    gv.setAdapter(madapter.getMenuAdapter(gv_arraystr));
	
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
			case 1: //ȡ��
				setResult(Main.RESULT_USER_CANCEL);
				finish();
				break;
			case 2: // ȷ��
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
