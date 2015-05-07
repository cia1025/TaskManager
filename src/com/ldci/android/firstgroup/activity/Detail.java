package com.ldci.android.firstgroup.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ldci.android.firstgroup.R;
import com.ldci.android.firstgroup.adapter.MenuAdapter;
import com.ldci.android.firstgroup.database.ScheduleExceptionAdapter;
import com.ldci.android.firstgroup.database.TaskAdapter;
import com.ldci.android.firstgroup.database.TaskParameterAdapter;
import com.ldci.android.firstgroup.database.TaskScheduleAdapter;
import com.ldci.android.firstgroup.entity.ScheduleExceptionEntity;
import com.ldci.android.firstgroup.entity.TaskEntity;
import com.ldci.android.firstgroup.entity.TaskParameterEntity;
import com.ldci.android.firstgroup.entity.TaskSEntity;
import com.ldci.android.firstgroup.service.MainService;

public class Detail extends Activity {
	
	public static TaskSEntity _taskSchedule; 
	private List<ScheduleExceptionEntity> statusList = null;
	private List<TaskParameterEntity> paraList = null;
	
	private int[] gv_array = { R.string.detail_back, R.string.detail_cancel, R.string.detail_save };
	private String[] gv_arraystr = new String[gv_array.length];
	private static String TAG = "DETAIL";
	
	private static final int DETAIL_NAME = 201;
	private static final int DETAIL_LOOP = 202;
	private static final int DETAIL_ALERT = 203;
	private static final int DETAIL_EXCEPT= 204;
	private static final int DETAIL_PHONE= 205;
	private static final int DETAIL_SMS = 206;
	
	// �����ؼ�
	private EditText hour_et;
	private EditText minute_et;
	private GridView gv;
	private TextView tvname;
	private TextView tvnamecontent;
	private TextView tvcyclestyle;
	private TextView tvcycle;
	private TextView tvremind;
	private TextView tvremindcontent;
	private TextView tvparameter;
	private TextView tvexcept;
	private ImageButton iButton;
	private TextView textparamter1;
	private TextView textparamter2;

	private TaskAdapter tAdapter;
	private TaskScheduleAdapter tsAdapter;
	private TaskParameterAdapter tpAdapter;
	private ScheduleExceptionAdapter seAdapter;
		
	private Boolean isCheck = false;
	private int CAT;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//setContentView(R.layout.detail);
		
		//ͨ������id����4�ֲ�ͬ�Ĳ���
		int taskId = Integer.parseInt(_taskSchedule.getTaskid());
		Log.d(TAG,"taskId="+taskId);
		
		//����������
		if((taskId)<=5){
			setContentView(R.layout.detail1);
		//����绰������
		}else if((taskId)==6){
			setContentView(R.layout.detail2);
		//���Ͷ���������
		}else if((taskId)==7){
			setContentView(R.layout.detail3);
		//ָ���ļ�������
		}else if((taskId)==8){
			setContentView(R.layout.detail4);
		//�޲�������
		}else{
			setContentView(R.layout.detail0);
		}
		
		// ����ID���XML�����й����Ŀؼ�
		findViews();
	
		// ���ù����ؼ��ļ����¼�
		setListeners();
		
		//ͨ������Id��������Ŀؼ�������������¼�
		//����������
		if((taskId)<=5){
			findViews1();
			setListeners1();
			
			// ����ImageButtonͼƬ�Ŀ������ر�
			iButton.setImageResource(R.drawable.bton);
			isCheck = true;
			
		//����绰������
		}else if((taskId)==6){
			findView2();
			setListeners2();
			
		//���Ͷ���������
		}else if((taskId)==7){
			findView3();
			setListeners3();
			
		//ָ���ļ�������
		}else if((taskId)==8){
			
		//�޲�������
		}else{
			
		}
		
		//�����ͼ��������
		Intent it = this.getIntent();
		CAT = it.getExtras().getInt("CAT");
		Log.d(TAG,"CAT = "+CAT);
		
		//�����ݿ�
		tsAdapter = new TaskScheduleAdapter(this);
		tsAdapter.open();
		
		seAdapter = new ScheduleExceptionAdapter(Detail.this);
		seAdapter.open();
		
		tpAdapter = new TaskParameterAdapter(Detail.this);
		tpAdapter.open();

		if (CAT == Main.REQ_ADD){ // ���������
			
			// ����ʱ��Ϊ��ǰʱ��
			Calendar calendar  = Calendar.getInstance();
			Date time= calendar.getTime();
			String year = String.valueOf(1900 + time.getYear());
			String month = String.valueOf(1 + time.getMonth());
			String day = String.valueOf(time.getDate());
			String hour = String.valueOf(time.getHours());
			String min = String.valueOf(time.getMinutes());
			hour_et.setText(hour);
			minute_et.setText(min);
			_taskSchedule.setStarttime(hour + ":" + min);
			
			//���ÿ�ʼ����Ϊ��ǰ����
			_taskSchedule.setStartdate(year+"-"+month+"-"+day);
			
			// ��������ƻ���ΪĬ��������
			tAdapter = new TaskAdapter(Detail.this);
			tAdapter.open();
			TaskEntity te = tAdapter.getItem(_taskSchedule.getTaskid());
			tAdapter.close();
			
			tvnamecontent.setText(te.getTaskname());
			_taskSchedule.setSname(te.getTaskname());
			
			//����ѭ����ʽΪĬ����ѭ��
			tvcyclestyle.setText(getString(R.string.csweek) + ":" +
					_taskSchedule.getStartdate());
			
			_taskSchedule.setLoop("0"); 
			_taskSchedule.setLoopinfo("0000000");
			
			// �������ѷ�ʽΪĬ�ϲ�����
			_taskSchedule.setAlerter("0");
			tvremindcontent.setText(getString(R.string.noRemind));
			
			// ���ÿ���Ϊ��״̬
			_taskSchedule.setSwitcher("true");
			
			// ��������״̬Ϊ��
			statusList = new ArrayList<ScheduleExceptionEntity>();
			
			// ���ò����б�Ϊ��
			paraList = new ArrayList<TaskParameterEntity>();
		}
		else if (CAT == Main.REQ_EDIT) // �༭��������
		{
			//���ÿ�ʼʱ��Ϊ�󶨵�TaskSEntity�Ŀ�ʼʱ��
			String starttime = _taskSchedule.getStarttime();
			String[] fields = starttime.split("\\:");
			hour_et.setText(fields[0]);
			minute_et.setText(fields[1]);
			
			//���üƻ�������ΪTaskSEntity��������
			tvnamecontent.setText(_taskSchedule.getSname());
			
			//����ѭ����ʽΪTaskSEntity��ѭ����ʽ
			String loopinfo = _taskSchedule.getLoopinfo();
			int loop = Integer.parseInt(_taskSchedule.getLoop());
			if (loop == 0){ //��ѭ��
				tvcyclestyle.setText(
						getString(R.string.csweek) + ":" + 
								_taskSchedule.getStartdate() + " : " +
								getSelectedWeekDays(_taskSchedule.getLoopinfo()));
			}
			else if (loop == 1){ //һ����
				tvcyclestyle.setText(getString(R.string.csonce) + ":" +
						_taskSchedule.getStartdate());
			}
			else if (loop == 2){//ÿ��ָ������
				tvcyclestyle.setText(getString(R.string.csday) + ":" +
						_taskSchedule.getStartdate() + " : " +
						_taskSchedule.getLoopinfo());
			}
			else if (loop == 3){//ÿ��ָ������
				tvcyclestyle.setText(getString(R.string.csminute) + ":" +
						_taskSchedule.getStartdate() + " : " +
						_taskSchedule.getLoopinfo());
			}
			
			//�������ѷ�ʽΪTaskSEntity�����ѷ�ʽ
			String alerter = _taskSchedule.getAlerter();
			if (alerter.equals("0"))
				tvremindcontent.setText(getString(R.string.noRemind));
			else if (alerter.equals("1"))
				tvremindcontent.setText(getString(R.string.auto));
			else if (alerter.equals("2"))
				tvremindcontent.setText(getString(R.string.vibration));
			else if (alerter.equals("3"))
				tvremindcontent.setText(getString(R.string.voice));
			
			//��������״̬ΪTaskSEntity������״̬
			//ͨ���ƻ�Id��ѯ�ƻ�������õ����������״̬
			statusList = seAdapter.getItemsBySId(_taskSchedule.getScheduleid());
			
			//��ò����������������Ӧ�����ļ��Ŀؼ���
			paraList = tpAdapter.getItemsBySId(_taskSchedule.getScheduleid());
			
			// ���ݲ�ͬ������ID��������б�:
			//����������
			if((taskId)<=5){
				if (paraList.get(0).getValue().equals("true"))
				{
					isCheck = true;
					iButton.setImageResource(R.drawable.bton);
				}
				else
				{
					isCheck = false;
					iButton.setImageResource(R.drawable.btoff);
				}
			//����绰������
			}else if((taskId)==6){
				textparamter1.setText(paraList.get(0).getValue());
			//���Ͷ���������
			}else if((taskId)==7){
				textparamter1.setText(paraList.get(0).getValue());
				textparamter2.setText(paraList.get(1).getValue());
			//ָ���ļ�������
			}else if((taskId)==8){
				
			//�޲�������
			}else{
				
			}
		}
		
		// ����GridView
		for (int i = 0; i < gv_array.length; i++) {
			gv_arraystr[i] = this.getResources().getString(gv_array[i]);
		}

		MenuAdapter madapter;
		madapter = new MenuAdapter(this);
		
		gv = (GridView) findViewById(R.id.gv);
		gv.setSelector(R.drawable.select);
		// gv.setBackgroundResource();// ���ñ���
		gv.setNumColumns(3);// ����ÿ������
		gv.setGravity(Gravity.CENTER);// λ�þ���
		gv.setVerticalSpacing(10);// ��ֱ���
		gv.setHorizontalSpacing(10);// ˮƽ���
		gv.setAdapter(madapter.getMenuAdapter(gv_arraystr));// ���ò˵�Adapter
	}
	
	private String getSelectedWeekDays(String str)
	{
		String weekdays="";
		
		if (str.charAt(0) == '1'){
			weekdays += getString(R.string.info_sunday);
		}
		if (str.charAt(1) == '1'){
			weekdays += getString(R.string.info_monday);
		}
		if (str.charAt(2) == '1'){
			weekdays += getString(R.string.info_tuesday);
		}
		if (str.charAt(3) == '1'){
			weekdays += getString(R.string.info_wednesday);
		}
		if (str.charAt(4) == '1'){
			weekdays += getString(R.string.info_thursday);
		}
		if (str.charAt(5) == '1'){
			weekdays += getString(R.string.info_friday);
		}
		if (str.charAt(6) == '1'){
			weekdays += getString(R.string.info_saturday);
		}
		
		return weekdays;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == Main.RESULT_USER_CANCEL){
			setResult(Main.RESULT_USER_CANCEL);
			finish();
			
		}else if (resultCode == RESULT_OK){
			switch (requestCode)
			{
			case DETAIL_NAME:
				tvnamecontent.setText(data.getExtras().getString("taskname"));
				_taskSchedule.setSname(data.getExtras().getString("taskname"));
				break;
				
			case DETAIL_LOOP:
				String loop = data.getExtras().getString("loop");
				String loopinfo = data.getExtras().getString("loopinfo");
				String startdate = data.getExtras().getString("startdate");
				
				if (loop.equals("0")){ //��ѭ��
					tvcyclestyle.setText(
							getString(R.string.csweek) + " : " + 
							startdate + " : " + getSelectedWeekDays(loopinfo));
				}
				else if (loop.equals("1")){ //һ����
					tvcyclestyle.setText(getString(R.string.csonce) + " : " +
							startdate);
				}
				else if (loop.equals("2")){//ÿ��ָ������
					tvcyclestyle.setText(getString(R.string.csday) + " : " +
							startdate + " : " +
							loopinfo);
				}
				else if (loop.equals("3")){//ÿ��ָ������
					tvcyclestyle.setText(getString(R.string.csminute) + " : " +
							startdate + " : " +
							loopinfo);
				}
				
				_taskSchedule.setLoop(loop);
				_taskSchedule.setLoopinfo(loopinfo);
				_taskSchedule.setStartdate(startdate);				
				break;
				
			case DETAIL_ALERT:
				String alerter = data.getExtras().getString("alerter");
				
				if (alerter.equals("0"))
					tvremindcontent.setText(getString(R.string.noRemind));
				else if (alerter.equals("1"))
					tvremindcontent.setText(getString(R.string.auto));
				else if (alerter.equals("2"))
					tvremindcontent.setText(getString(R.string.vibration));
				else if (alerter.equals("3"))
					tvremindcontent.setText(getString(R.string.voice));
				
				_taskSchedule.setAlerter(alerter);
				break;
				
			case DETAIL_EXCEPT:
				break;
				
			case DETAIL_PHONE:
				String phonenum = data.getExtras().getString("phoneparam");
				textparamter1.setText(phonenum);
			break;
			case DETAIL_SMS:
				String sms = data.getExtras().getString("phoneparam");
				textparamter2.setText(sms);
				break;
			}
		}
	}

	// ���ø�����ļ����¼�
	private void setListeners() {
		// ��פ�˵��ļ����¼�
		
		gv.setOnItemClickListener(new GridView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				/* �ж�Adapter���Ԫ�ظ������жϱ�������ǵڼ���Ԫ������ */
				switch (arg2) {
				case 0: //���ذ�ť
					finish();
					break;
					
				case 1: //ȡ����ť
					setResult(Main.RESULT_USER_CANCEL);
					finish();
					break;

				case 2: //���水ť
					//��TaskSEntity���󱣴�����ݿ�
					
					//����ʼʱ�䱣���_taskSchedule
					String starttime = hour_et.getText() + ":" + minute_et.getText();
					_taskSchedule.setStarttime(starttime);
					int taskId = Integer.valueOf(_taskSchedule.getTaskid());
					
					String scheduleId = "";
					if (CAT == Main.REQ_ADD){
						tsAdapter.create(_taskSchedule);
						
						//��ѯ�ƻ�������������ɵļƻ�Id
						scheduleId = tsAdapter.geLatestItem().getScheduleid();
					}
					else if (CAT == Main.REQ_EDIT){
						tsAdapter.update(_taskSchedule);
						
						//��õ�ǰ�󶨵�task schedule�ļƻ�Id
						scheduleId = _taskSchedule.getScheduleid();
					}
					
					//��������״̬�б�
					
					// ���ݲ�ͬ������ID��������б�
					//����������
					if((taskId)<=5){
						
						if (CAT == Main.REQ_ADD){
							TaskParameterEntity tpe = new TaskParameterEntity();
							tpe.setSid(scheduleId);
							tpe.setPid("1");
							tpe.setValue(isCheck.toString());
							paraList.add(tpe);
							tpAdapter.create(paraList.get(0));
						}
						else if (CAT == Main.REQ_EDIT){
							paraList.get(0).setValue(isCheck.toString());
							tpAdapter.update(paraList.get(0));
						}
		
					//����绰������
					}else if((taskId)==6){
						if (CAT == Main.REQ_ADD){
							TaskParameterEntity tpe = new TaskParameterEntity();
							tpe.setSid(scheduleId);
							tpe.setPid("2");
							tpe.setValue(textparamter1.getText().toString());
							paraList.add(tpe);
							tpAdapter.create(paraList.get(0));
						}
						else if (CAT == Main.REQ_EDIT){
							paraList.get(0).setValue(textparamter1.getText().toString());
							tpAdapter.update(paraList.get(0));
						}
					//���Ͷ���������
					}else if((taskId)==7){
						if (CAT == Main.REQ_ADD){
							TaskParameterEntity Entity1 = new TaskParameterEntity();
							Entity1.setSid(scheduleId);
							Entity1.setPid("2");
							Entity1.setValue(textparamter1.getText().toString());
							
							TaskParameterEntity Entity2 = new TaskParameterEntity();
							Entity2.setSid(scheduleId);
							Entity2.setPid("3");
							Entity2.setValue(textparamter2.getText().toString());
							
							paraList.add(Entity1);
							paraList.add(Entity2);
							tpAdapter.create(paraList.get(0));
							tpAdapter.create(paraList.get(1));
						}
						else if (CAT == Main.REQ_EDIT){
							paraList.get(0).setValue(textparamter1.getText().toString());
							paraList.get(1).setValue(textparamter2.getText().toString());
							tpAdapter.update(paraList.get(0));
							tpAdapter.update(paraList.get(1));
						}
						
					//ָ���ļ�������
					}else if((taskId)==8){
						
					//�޲�������
					}else{
						
					}
					
					//**********************************************
					//������Ϣ��StartupReceiver����������ע������task
					//**********************************************
					Intent intent = new Intent(MainService.ACTION_SERVICE_RESTART);
					sendBroadcast(intent);
					
					setResult(RESULT_OK);
					finish();
					break;
				}
			}
		});
		
		//�������Ƶ���ת
		tvname.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent in = new Intent();
				in.putExtra("CAT", CAT);
				in.putExtra("taskname", _taskSchedule.getSname());
				in.setClass(Detail.this, TaskName.class);
				startActivityForResult(in, DETAIL_NAME);
			}
		});		

		// ѭ������ת
		tvcycle.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent in = new Intent();
				in.putExtra("CAT", CAT);
				in.putExtra("startdate", _taskSchedule.getStartdate());
				in.putExtra("loop", _taskSchedule.getLoop());
				in.putExtra("loopinfo", _taskSchedule.getLoopinfo());
				in.setClass(Detail.this, CycleStyle.class);
				startActivityForResult(in, DETAIL_LOOP);
			}
		});

//		// ѭ������ת
//		tvcyclestyle.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				Intent in = new Intent();
//				in.putExtra("CAT", CAT);
//				in.putExtra("startdate", _taskSchedule.getStartdate());
//				in.putExtra("loop", _taskSchedule.getLoop());
//				in.putExtra("loopinfo", _taskSchedule.getLoopinfo());
//				in.setClass(Detail.this, CycleStyle.class);
//				startActivityForResult(in, DETAIL_LOOP);
//			}
//		});
		
		// �������͵���ת
		tvremind.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent in = new Intent();
				in.putExtra("CAT", CAT);
				in.putExtra("alerter", _taskSchedule.getAlerter());
				in.setClass(Detail.this, RemindType.class);
				startActivityForResult(in, DETAIL_ALERT);
			}
		});

		// �������ת
		tvexcept.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent in = new Intent();
				in.setClass(Detail.this, Except.class);
				//in.putExtra("exceptstatus", statusList);
				startActivityForResult(in, DETAIL_EXCEPT);
			}
		});
	}
	
	// ������������������¼�
	private void setListeners1()
	{
		// ���ÿ���ImageButton�����¼�
		iButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (v.getId() == iButton.getId()) {
					if (!isCheck) {
						iButton.setImageResource(R.drawable.bton);
						isCheck = true;
					} else if (isCheck) {
						iButton.setImageResource(R.drawable.btoff);
						isCheck = false;
					}
				}
			}
		});
	}
	
	//���ò���绰��������¼�
	private void setListeners2(){
		textparamter1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				//����绰����
				intent.putExtra("CAT", CAT);
				intent.putExtra("phoneparam", textparamter1.getText().toString());
				intent.setClass(Detail.this, EditPhone.class);
				startActivityForResult(intent, DETAIL_PHONE);
			}
		});
	}
	
	//���÷��Ͷ��ż����¼�
	void setListeners3(){
		textparamter1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				//����绰����
				intent.putExtra("CAT", CAT);
				intent.putExtra("phoneparam", textparamter1.getText().toString());
				intent.setClass(Detail.this, EditPhone.class);
				startActivityForResult(intent, DETAIL_PHONE);
			}
		});
		
		textparamter2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
				//�����������
				it.putExtra("CAT", CAT);
				it.putExtra("phoneparam", textparamter2.getText().toString());
				it.setClass(Detail.this, EditSMS.class);
				startActivityForResult(it,DETAIL_SMS);
			}
		});
	}

	// ����ID��ò����еĿؼ���ͨ�ã�
	private void findViews() {
		hour_et = (EditText) findViewById(R.id.hour_et);
		minute_et = (EditText) findViewById(R.id.minute_et);
		gv = (GridView) findViewById(R.id.gv);
		tvcycle = (TextView) findViewById(R.id.tv_cycle);
		tvcyclestyle = (TextView) findViewById(R.id.tv_cyclestyle);
		tvname = (TextView) findViewById(R.id.tv_tskname_title);
		tvnamecontent = (TextView) findViewById(R.id.tv_taskname);
		tvexcept = (TextView) findViewById(R.id.tv_except);
		tvremind = (TextView) findViewById(R.id.tv_remind);
		tvremindcontent = (TextView) findViewById(R.id.tv_remindContent);
		tvparameter = (TextView) findViewById(R.id.tv_parameter);
	}
	
	// ������������ؼ�
	private void findViews1(){
		iButton = (ImageButton) findViewById(R.id.detail_ib);
	}
	
	//����绰��������ؼ�
	private void findView2(){
		textparamter1 = (TextView) findViewById(R.id.tv_phoneNum);
	}

	//���Ͷ�����������ؼ�
	private void findView3(){
		textparamter1 = (TextView) findViewById(R.id.tv_phoneNum);
		textparamter2 =  (TextView) findViewById(R.id.tv_SMScontent);
	}

	// ���ݿ�Ĺر�
	@Override
	protected void onDestroy() {
		super.onDestroy();
		tsAdapter.close();
		seAdapter.close();
		tpAdapter.close();		
	}

}