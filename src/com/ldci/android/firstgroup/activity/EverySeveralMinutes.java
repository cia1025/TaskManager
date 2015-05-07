package com.ldci.android.firstgroup.activity;

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
import com.ldci.android.firstgroup.R;

public class EverySeveralMinutes extends Activity {

	private EditText etYear,etMonth,etDay, etSeveralMinute;
	private GridView  gv;
	private MenuAdapter madapter;
	
	private String startdate;
	private String loop;
	private String loopinfo;
	
	private int[] gv_array = { R.string.detail_back, R.string.detail_cancel, R.string.detail_ok };
	private String[] gv_arraystr = new String[gv_array.length];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.everyseveralminutes);
		 
		//ƥ��XML�ļ��ؼ�
		etYear= (EditText) findViewById(R.id.etYear);
		etMonth =(EditText) findViewById(R.id.etMonthr);
		etDay =(EditText) findViewById(R.id.etDay);
		etSeveralMinute =(EditText) findViewById(R.id.etEverySeveralMinutes);
		
		//��ô��͵���ͼ
		Intent it=this.getIntent();
		startdate = it.getExtras().getString("startdate");
		loop = it.getExtras().getString("loop");
		loopinfo = it.getExtras().getString("loopinfo");
		
		 //���ؼ���������Ϊ����Ŀ�ʼ����
		String[] fields = startdate.split("\\-");
		etYear.setText(fields[0]);
		etMonth.setText(fields[1]);
		etDay.setText(fields[2]);
		
		//���ؼ���������Ϊ����ļ������
		if (loop.equals("3"))// ֻ�е�ѭ������Ϊָ��������ѭ��ʱ����Ҫת��ѭ������
		{
			etSeveralMinute.setText(loopinfo);
		}
		else
		{
			etSeveralMinute.setText("1");
		}
	 
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
				case 1://ȡ��
					setResult(Main.RESULT_USER_CANCEL);
					finish();
					break;
				case 2://ȷ��
					startdate = etYear.getText() + "-" + etMonth.getText() + "-" + etDay.getText();
					loopinfo = etSeveralMinute.getText().toString();
					
					Intent it = new Intent();
					it.putExtra("startdate", startdate);
					it.putExtra("loopinfo", loopinfo);
					setResult(RESULT_OK, it);
					finish();
					break;
				}
		}});
	}
}
