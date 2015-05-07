package com.ldci.android.firstgroup.activity;

import com.ldci.android.firstgroup.adapter.MenuAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.ldci.android.firstgroup.R;

public class CycleStyle extends Activity {
	private GridView gv;
	// �����ؼ�
	private RadioButton rb1;
	private RadioButton rb2;
	private RadioButton rb3;
	private RadioButton rb4;
	private int[] gv_array = { R.string.detail_back, R.string.detail_cancel, R.string.detail_nextstep };
	private String[] gv_arraystr = new String[gv_array.length];
	
	private String startdate;
	private String loop;
	private String loopinfo;
	private int CAT;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.cyclestyle);
		
		// ��ò���ID
		findViews();
		
		Intent it = this.getIntent();
		CAT = it.getExtras().getInt("CAT");
		startdate = it.getExtras().getString("startdate");
		loop = it.getExtras().getString("loop");
		loopinfo = it.getExtras().getString("loopinfo");
		
		if (loop.equals("0")) {
			rb2.setChecked(true);
		} else if (loop.equals("1")) {
			rb1.setChecked(true);
		} else if (loop.equals("2")) {
			rb3.setChecked(true);
		} else if (loop.equals("3")) {
			rb4.setChecked(true);
		}

		// ���ü���
		setListensers();
		
		//����GridView
		for (int i = 0; i < gv_array.length; i++) {
			gv_arraystr[i] = this.getResources().getString(gv_array[i]);
		}

		MenuAdapter madapter;
		madapter = new MenuAdapter(this);
		// gv.setSelector(R.drawable.icon);
		// gv.setBackgroundResource();// ���ñ���
		gv.setNumColumns(3);// ����ÿ������
		gv.setGravity(Gravity.CENTER);// λ�þ���
		gv.setVerticalSpacing(10);// ��ֱ���
		gv.setHorizontalSpacing(10);// ˮƽ���
		gv.setAdapter(madapter.getMenuAdapter(gv_arraystr));// ���ò˵�Adapter
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == Main.RESULT_USER_CANCEL){
			
			setResult(Main.RESULT_USER_CANCEL);
			finish();
		}
		else if (resultCode == RESULT_OK){
		
			// ѭ����ʽ
			if (rb2.isChecked())
				loop = "0";
			else if (rb1.isChecked())
				loop = "1";
			else if (rb3.isChecked())
				loop = "2";
			else if (rb4.isChecked())
				loop = "3";
			
			data.putExtra("loop", loop);
			setResult(RESULT_OK, data); //��ѭ����ʽ����Data���������(���⻹��loopinfo��startdate)
			finish();
		}
	}

	private void setListensers() {
		gv.setOnItemClickListener(new GridView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0: //����
					finish();
					break;
				case 1: //ȡ��
					setResult(Main.RESULT_USER_CANCEL);
					finish();
					break;
				case 2:  //��һ��
					
					Intent intent = new Intent();
					intent.putExtra("CAT", CAT);
					intent.putExtra("startdate", startdate);
					intent.putExtra("loop", loop);
					intent.putExtra("loopinfo", loopinfo);
					
					if (rb1.isChecked()) {
						intent.setClass(CycleStyle.this, OneTime.class);
						startActivityForResult(intent, CAT);
						
					} else if (rb2.isChecked()) {
						intent.setClass(CycleStyle.this, WeekCycle.class);
						startActivityForResult(intent, CAT);
						
					} else if (rb3.isChecked()) {
						intent.setClass(CycleStyle.this, EverySeveralDays.class);
						startActivityForResult(intent, CAT);
						
					} else if (rb4.isChecked()) {
						intent.setClass(CycleStyle.this, EverySeveralMinutes.class);
						startActivityForResult(intent, CAT);
					}
					break;
				}
			}
		});
	}

	private void findViews() {
		
		rb1 = (RadioButton) findViewById(R.id.rb2);
		rb2 = (RadioButton) findViewById(R.id.rb3);
		rb3 = (RadioButton) findViewById(R.id.rb4);
		rb4 = (RadioButton) findViewById(R.id.rb5);

		rb1.setOnCheckedChangeListener((OnCheckedChangeListener) new listener());
		rb2.setOnCheckedChangeListener((OnCheckedChangeListener) new listener());
		rb3.setOnCheckedChangeListener((OnCheckedChangeListener) new listener());
		rb4.setOnCheckedChangeListener((OnCheckedChangeListener) new listener());

		gv = (GridView) findViewById(R.id.csgv);
	}

	class listener implements OnCheckedChangeListener {
		public void onCheckedChanged(CompoundButton v, boolean isChecked) {
			if (v.getId() == rb1.getId()) {
				if (isChecked) {
					rb2.setChecked(false);
					rb3.setChecked(false);
					rb4.setChecked(false);
				}
			} else if (v.getId() == rb2.getId()) {
				if (isChecked) {
					rb1.setChecked(false);
					rb3.setChecked(false);
					rb4.setChecked(false);
				}
			} else if (v.getId() == rb3.getId()) {
				if (isChecked) {
					rb1.setChecked(false);
					rb2.setChecked(false);
					rb4.setChecked(false);
				}
			} else if (v.getId() == rb4.getId()) {
				if (isChecked) {
					rb1.setChecked(false);
					rb3.setChecked(false);
					rb2.setChecked(false);
				}
			}
		}
	}
}