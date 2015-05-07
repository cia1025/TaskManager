package com.ldci.android.firstgroup.service;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.ldci.android.firstgroup.database.TaskAdapter;
import com.ldci.android.firstgroup.database.TaskParameterAdapter;
import com.ldci.android.firstgroup.database.TaskScheduleAdapter;
import com.ldci.android.firstgroup.entity.TaskParameterEntity;
import com.ldci.android.firstgroup.entity.TaskSEntity;
import com.ldci.android.firstgroup.tool.TaskNotify;
import com.ldci.android.firstgroup.tool.WifaManage;

public class MainService extends Service {
	
	private static Timer timer = new Timer();
	
	public static final String SETTING = "SETTING";
	public static final String SETTING_LANGUAGE = "SETTING_LANGUAGE";
	public static final String SETTING_SOUNDFILE = "SETTING_SOUNDFILE";
	public static final String SETTING_SWITCHER = "SETTING_SWITCHER";
	
	public static final String ACTION_SERVICE_RESTART = "com.ldci.android.firstgroup.SERVICE_RESTART";
	public static final String ACTION_SERVICE_START = "com.ldci.android.firstgroup.SERVICE_START";
	public static final String ACTION_SERVICE_STOP = "com.ldci.android.firstgroup.SERVICE_STOP";
	public static final String ACTION_BOOT_COMPLETED1 = "com.ldci.android.firstgroup.BOOT_COMPLETED";
	
	private static final String TAG = "MAINSERVICE";
	
	private TaskAdapter ta;
	private TaskScheduleAdapter tsa;
	private TaskParameterAdapter tpa;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d(TAG, "OnCreate");
		//Toast.makeText(this, "MainService : OnCreate...", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(TAG, "onDestroy");
		
		timer.cancel();
		timer = new Timer();
		
		Log.d(TAG, "onDestroy: timer canceled and created again.");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		Log.d(TAG, "onStart");
		//Toast.makeText(this, "MainService : onStart...", Toast.LENGTH_SHORT).show();
		
		SharedPreferences settings = getSharedPreferences(SETTING, MODE_PRIVATE);
		boolean  switcher= settings.getBoolean(SETTING_SWITCHER, false);
		
		//����Ϊtrue˵��������������
		if (switcher == true)
		{
			Log.d(TAG, "The switcher is closed, nothing to execute.");
			return;
		}
		
		// open the databases
        ta = new TaskAdapter(this); 
        ta.open();
        tsa = new TaskScheduleAdapter(this);
        tsa.open();
        tpa = new TaskParameterAdapter(this);
        tpa.open();
        
		//��ѯTbl_TaskSchedule���õ����е�����ƻ�
        List<TaskSEntity> taskScheduleList = tsa.getAll();
        for (int i=0; i<taskScheduleList.size(); i++){
        	ProcessSchedule(taskScheduleList.get(i));
        }		
        
        // Close the databases
        ta.close();
        tsa.close();
        tpa.close();
	}
	
	// ����ÿ���ƻ�����
	private void ProcessSchedule(TaskSEntity taskSc){
		//1. �жϸüƻ�����Ŀ��أ����ΪTrue��������� ���ΪFalse���򷵻�
//		if (taskSc.getSwitcher().equals("true")){
//			Log.d(TAG, "task schdedule:" + taskSc.getScheduleid() + " quit due to the switcher is off");
//			return;
//		}
		
		//2. ��ѯ�����Tbl_Task������������״̬�����״̬Ϊtrue���������
		//    ���Ϊfalse�� �򷵻�

		//3. ���ݸüƻ������ѭ����ʽ����ȡ��ͬ�Ĵ����߼�
		switch (Integer.parseInt(taskSc.getLoop()))	{
		
			case 0: // ��ѭ��
				ProcessWeeksExec(taskSc);
				break;
			
			case 1: // һ����ִ��
				ProcessOnceExec(taskSc);		
				break;
				
			case 2: // ÿ��ָ������ѭ��
				ProcessDaysExec(taskSc);
				break;
				
			case 3: // ÿ��ָ������ѭ��
				ProcessMinutesExec(taskSc);
				break;
		}		
		
	}
	
	private void ProcessWeeksExec(TaskSEntity taskSc){
		
		Log.d(TAG, "Task:" + taskSc.getTaskid() + " - schdedule:" + taskSc.getScheduleid() + " WEEK loop...");
		
		// 1. �жϵ�ǰ���ܼ����Ƿ��������ѭ����ѭ�������У���������ڣ��򷵻�
		int weekday = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
		String loopparam = taskSc.getLoopinfo();
		
		if (loopparam.charAt(weekday) == '0'){
			Log.d(TAG, "task schdedule:" + taskSc.getScheduleid() + " failed due to the current week day is not scheduled.");
			return;
		}

		//2. ����üƻ�����Ŀ�ʼʱ�����ڵ�ǰʱ�䣬�򷵻�
		if (compareTime(taskSc)==false)
			return;
		
		// 3. ���ݲ�ͬ�������ID�����ò�ͬ��TimerTask
		//     ��������ʱ��Ϊ��ʼʱ��
		ExecuteByTask(taskSc.getTaskid(), 
				taskSc.getScheduleid(),
				getStartTime(taskSc), 
				0,
				taskSc.getAlerter(),
				taskSc.getSname());
	}
	
	private void ProcessOnceExec(TaskSEntity taskSc){
		
		Log.d(TAG, "Task:" + taskSc.getTaskid() + " - schdedule:" + taskSc.getScheduleid() + " ONETIME ...");
		
		// 1. �жϵ�ǰ�����Ƿ���ڸüƻ�����Ŀ�ʼ���ڣ��������ȣ��򷵻�
		String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
		String month = String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1);
		String day = String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		String curDate = year + "-" + month + "-" + day;
		
		if (! curDate.equals(taskSc.getStartdate())){
			Log.d(TAG, "task schdedule:" + taskSc.getScheduleid() + " failed due to the startdate is not current day.");
			return;
		}
		
		//2. ����üƻ�����Ŀ�ʼʱ�����ڵ�ǰʱ�䣬�򷵻�
		if (compareTime(taskSc)==false)
			return;
		
		// 3. ���ݲ�ͬ�������ID�����ò�ͬ��TimerTask
		//     ��������ʱ��Ϊ��ʼʱ��
		ExecuteByTask(taskSc.getTaskid(),
				taskSc.getScheduleid(),
				getStartTime(taskSc), 
				0, 
				taskSc.getAlerter(),
				taskSc.getSname());
	}
	
	private void ProcessDaysExec(TaskSEntity taskSc){
		
		Log.d(TAG, "Task:" + taskSc.getTaskid() + " - schdedule:" + taskSc.getScheduleid() + " DAY loop...");
		
		// 1. �����ǰ�������ڸüƻ�����Ŀ�ʼ���ڣ��򷵻�
		// 2. ������ڻ���ڣ�������жϣ��ƻ�����Ŀ�ʼ���� + ��������ı������Ƿ�
		//     ���ڵ�ǰ���ڣ���������ڣ��򷵻�
		if (compareDateForDayLoop(taskSc) == false)
			return;
				
		//2. ����üƻ�����Ŀ�ʼʱ�����ڵ�ǰʱ�䣬�򷵻�
		if (compareTime(taskSc)==false)
			return;
  	    
		// 3. ���ݲ�ͬ�������ID�����ò�ͬ��TimerTask
		//     ��������ʱ��Ϊ��ʼʱ��
  	    ExecuteByTask(taskSc.getTaskid(),
  	    		taskSc.getScheduleid(),
  	    		getStartTime(taskSc), 
  	    		0,
  	    		taskSc.getAlerter(),
  	    		taskSc.getSname());
	}
	
	private void ProcessMinutesExec(TaskSEntity taskSc){
		
		Log.d(TAG, "Task:" + taskSc.getTaskid() + " - schdedule:" + taskSc.getScheduleid() + " MINUTES loop...");
		
		//1. �����ѭ���Ŀ�ʼ�������ڵ�ǰ���ڣ��򷵻�
		if (compareDate(taskSc) == false)
			return;
		
		// 2. ���ݲ�ͬ�������ID�����ò�ͬ��TimerTask
		//     ��������ʱ��Ϊ��ʼʱ�䣬���ʱ���ѭ�������л�á�
		long period = (Long.parseLong(taskSc.getLoopinfo())) * 60 * 1000; // 1���ӱ�ʾ���1���ӣ�60�룩
  	    ExecuteByTask(taskSc.getTaskid(),
				  	    		taskSc.getScheduleid(),
				  	    		getStartTime(taskSc), 
				  	    		period,
				  	    		taskSc.getAlerter(),
				  	    		taskSc.getSname());
	}

	// �Ƚϵ�ǰʱ��Ϳ�ʼʱ�䣬�����ʼʱ�����ڵ�ǰʱ�䣬�򷵻�false�����򷵻�true
	private boolean compareTime(TaskSEntity taskSc){
		
		//����üƻ�����Ŀ�ʼʱ�����ڵ�ǰʱ�䣬�򷵻�
		Calendar currenttime = Calendar.getInstance();
		Calendar starttime = Calendar.getInstance();
		String hour = taskSc.getStarttime().substring(0, taskSc.getStarttime().indexOf(":"));
		String minute = taskSc.getStarttime().substring(taskSc.getStarttime().indexOf(":") + 1);
		starttime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
		starttime.set(Calendar.MINUTE, Integer.parseInt(minute));
		
		//����ͺ��������Ϊ0�������Ƚϲž�ȷ
		currenttime.set(Calendar.SECOND, 0);
		currenttime.set(Calendar.MILLISECOND, 0);
		starttime.set(Calendar.SECOND, 0);
		starttime.set(Calendar.MILLISECOND, 0);
		
		if (starttime.before(currenttime))
		{
			Log.d(TAG, "task schdedule:" + taskSc.getScheduleid() + " failed due to the starttime is earlier than current time.");
			return false;
		}
		
		return true;
	}

	//�жϸ�ѭ���Ŀ�ʼ�����Ƿ����ڻ���ڵ�ǰ���ڣ�������򷵻�
	private boolean compareDate(TaskSEntity taskSc){

		// get start date
		String startdate = taskSc.getStartdate();
		String year =startdate.substring(0, startdate.indexOf("-"));
		String month =startdate.substring(startdate.indexOf("-") + 1, startdate.lastIndexOf("-"));
		String day = startdate.substring(startdate.lastIndexOf("-") + 1);
		
		Calendar startDate = Calendar.getInstance();
		startDate.set(Integer.parseInt(year), 
							Integer.parseInt(month) - 1,  //��0��11
							Integer.parseInt(day), 
							0,0,0);
		startDate.set(Calendar.MILLISECOND, 0);
				
		// get current date
		Calendar currentDate = Calendar.getInstance();
		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		currentDate.set(Calendar.MILLISECOND, 0);
				
		if (startDate.after(currentDate)) {
			Log.d(TAG, "task schdedule:" + taskSc.getScheduleid() + " failed due to the current date is earlier than the startdate.");
			return false;
		}
		
		return true;
	}
		
	private boolean compareDateForDayLoop(TaskSEntity taskSc){

		//1. �жϸ�ѭ���Ŀ�ʼ�����Ƿ����ڻ���ڵ�ǰ���ڣ�������򷵻�
		String startdate = taskSc.getStartdate();
		String year =startdate.substring(0, startdate.indexOf("-"));
		String month =startdate.substring(startdate.indexOf("-") + 1, startdate.lastIndexOf("-"));
		String day = startdate.substring(startdate.lastIndexOf("-") + 1);
		
		Calendar startDate = Calendar.getInstance();
		startDate.set(Integer.parseInt(year), 
							Integer.parseInt(month) - 1,  //��0��11
							Integer.parseInt(day), 
							0,0,0);
		startDate.set(Calendar.MILLISECOND, 0);
				
		// get current date
		Calendar currentDate = Calendar.getInstance();
		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		currentDate.set(Calendar.MILLISECOND, 0);
				
		if (startDate.after(currentDate)) {
			Log.d(TAG, "task schdedule:" + taskSc.getScheduleid() + " failed due to the current date is earlier than the startdate.");
			return false;
		}
		
		// 2. ������ڻ���ڣ�������жϣ��ƻ�����Ŀ�ʼ���� + ��������ı������Ƿ�
		//     ���ڵ�ǰ���ڣ���������ڣ��򷵻�
		if (currentDate.compareTo(startDate) != 0) // ��������ڣ�����Ҫ�����ж�
		{
	        boolean isEqual = false;
	        while (startDate.before(currentDate)){
	  	    	startDate.add(Calendar.DATE, 
	  	    			Integer.parseInt(taskSc.getLoopinfo())); //���1�죺ÿ��ִ�У����2�죺2��ִ��һ�Σ��Դ�����
	  	    	if (startDate.compareTo(currentDate) == 0){
	  	    		isEqual = true;
	  	    		break;
	  	    	}
	  	    }
	  	    
	  	    if (! isEqual){
	  	    	Log.d(TAG, "task schdedule:" + taskSc.getScheduleid() + " failed due to the current date is not the day need to run.");
				return false;
	  	    }
		}
		
		return true;
	}
	
	// Get the start time...
	private Date getStartTime(TaskSEntity taskSc){

		String starttime = taskSc.getStarttime();
		String hour = starttime.substring(0, starttime.indexOf(":"));
		String minute = starttime.substring(starttime.indexOf(":") + 1);
		
		Date execTime = new Date();
		execTime.setHours(Integer.parseInt(hour));
		execTime.setMinutes(Integer.parseInt(minute));
		execTime.setSeconds(0);
		
		return execTime;
	}
	
	// Execute by task
	private void ExecuteByTask(String taskId, String scheduleId, Date execTime, long period, String alerter, String scheduleName){
		
		//1. �жϵ�ǰ������״̬�Ƿ����ִ�и�����
		if (! isExecutable(Integer.parseInt(scheduleId))){
			Log.d(TAG, "task schdedule:" + scheduleId + " failed due to the current state is not permitted" );
			return;
		}
		
		//2. ��ѯ�����������øüƻ���������в�����
		tpa = new TaskParameterAdapter(this);
		tpa.open();
		List<TaskParameterEntity> paraList = tpa.getItemsBySId(scheduleId);
				
		Log.d(TAG, "Task:" + taskId + " - schdedule:" + scheduleId + 
				" was scheduled on " + execTime.toString() + " and period is " + period +" ms");
		
		int alertstyle = Integer.valueOf(alerter);
				
		//3. ����TaskIdѡ��ͬ��TimerTaskִ�и�����
		switch (Integer.parseInt(taskId)){
		
//		case 1: 
//			MyTask1 t1 = new MyTask1();
//			if (period == 0)
//				timer.schedule(t1, execTime);
//			else
//				timer.schedule(t1, execTime, period);
//			break;
//			
//		case 2: 
//			MyTask2 t2 = new MyTask2();
//			if (period == 0)
//				timer.schedule(t2, execTime);
//			else
//				timer.schedule(t2, execTime, period);
//			break;
		
			case 1: //����
				Task1 t1 = new Task1( paraList, alertstyle, scheduleName);
				if (period == 0)
					timer.schedule(t1, execTime);
				else
					timer.schedule(t1, execTime, period);
				break;
				
			case 2: //��
				Task2 t2 = new Task2(paraList, alertstyle, scheduleName);
				if (period == 0)
					timer.schedule(t2, execTime);
				else
					timer.schedule(t2, execTime, period);
				break;
				
			case 3: //WIFI
				Task3 t3 = new Task3(paraList, alertstyle, scheduleName);
				if (period == 0)
					timer.schedule(t3, execTime);
				else
					timer.schedule(t3, execTime, period);
				break;
				
			case 4: //����
//				Task4 t4 = new Task4(paraList, alertstyle, scheduleName);
//				if (period == 0)
//					timer.schedule(t4, execTime);
//				else
//					timer.schedule(t4, execTime, period);
//				break;
				
			case 5://����ģʽ
				Task5 t5 = new Task5(paraList, alertstyle, scheduleName);
				if (period == 0)
					timer.schedule(t5, execTime);
				else
					timer.schedule(t5, execTime, period);
				break;
				
			case 6://����绰
				Task6 t6 = new Task6(paraList, alertstyle, scheduleName);
				if (period == 0)
					timer.schedule(t6, execTime);
				else
					timer.schedule(t6, execTime, period);
				break;
				
			case 7://���Ͷ���
				Task7 t7 = new Task7(paraList, alertstyle, scheduleName);
				if (period == 0)
					timer.schedule(t7, execTime);
				else
					timer.schedule(t7, execTime, period);
				break;
				
			case 8://��������
				Task8 t8 = new Task8(paraList, alertstyle, scheduleName);
				if (period == 0)
					timer.schedule(t8, execTime);
				else
					timer.schedule(t8, execTime, period);
				break;
		}
	}
	
	private boolean isExecutable(int ScheduleId){
		boolean isExecutable = true;
		
		//1. ��õ�ǰ������״̬��Ϣ�����硰����ͨ������������ʹ��GPRS����������ʹ��WIFI����
		//    ����״̬��Tbl_Status����ѯ�õ�״̬Id��
		
		//2. ��ѯ�ƻ�������������Щ״̬��״̬Id���¸üƻ�����ִ�С�
		
		//3. ��1��2�в�ѯ�õ���״̬Id���бȽϣ����1��״̬Id��2��״̬Id�д��ڣ���ô���ز�ִ��
		
		return isExecutable;
	}
	
	public class MyTask1 extends TimerTask{
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.d(TAG, "MyTask1 is running...");
			//Toast.makeText(ctx, "MainService : MyTask1 is running...", Toast.LENGTH_SHORT).show();
		}	
	}
	
	public class MyTask2 extends TimerTask{
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.d(TAG, "MyTask2 is running...");
			//Toast.makeText(ctx, "MainService : MyTask2 is running...", Toast.LENGTH_SHORT).show();
		}	
	}
	
	//��������/�أ�
	public class Task1 extends TimerTask{
		
		//private Context ctx;
		private List<TaskParameterEntity> paraList;
		private int alerter;
		private String scheduleName;
		
		//���幹�췽��
		public Task1(List<TaskParameterEntity> paraList, int alerter, String scheduleName){
			this.paraList = paraList;
			this.alerter = alerter;
			this.scheduleName = scheduleName;
		}	
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			TaskNotify tn =  new TaskNotify(MainService.this);
			tn.Notify(alerter, scheduleName);
			
			String switchParam = null;
			for(int i=0; i<paraList.size(); i++){
				switch (Integer.valueOf(paraList.get(i).getPid()))
				{
				case 1: // ������Ĳ���1
					switchParam = paraList.get(i).getValue();
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
					break;
				}
			}
			
			if(switchParam.equals("true")){
				//������ز���Ϊ��true�����򿪾���
				//���AudioManager����
				AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
				//����Ϊ����ģʽ
				am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
			}else if(switchParam.equals("false")){
				//������ز���Ϊ��false�����رվ���
				//���AudioManager����
				AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
				//����Ϊ��ͨģʽ
				am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
			}
			
			Log.d(TAG, "Task1 is running...");
		}	
	}
	
	//�񶯿���
	public class Task2 extends TimerTask{
			
		private List<TaskParameterEntity> paraList ;
		private int alerter;
		private String scheduleName;
		
		//���幹�췽��
		public Task2(List<TaskParameterEntity> paraList, int alerter, String scheduleName){
			this.paraList = paraList;
			this.alerter = alerter;
			this.scheduleName = scheduleName;
		}	
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//Toast.makeText(ctx, "Task2 started...", Toast.LENGTH_SHORT);
			
			TaskNotify tn =  new TaskNotify(MainService.this);
			tn.Notify(alerter, scheduleName);
			
			String switchParam = null;
			for(int i=0; i<paraList.size(); i++){
				switch (Integer.valueOf(paraList.get(i).getPid()))
				{
				case 1: // ������Ĳ���1
					switchParam = paraList.get(i).getValue();
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
					break;
				}
			}
			
			if(switchParam.equals("true")){
				//������ز���Ϊ��true��������
				//���AudioManager����
				AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
				//����Ϊ��ģʽ
				am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
			}else if(switchParam.equals("false")){
				//������ز���Ϊ��false�����ر���
				//���AudioManager����
				AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
				//����Ϊ��ͨģʽ
				am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
			}
			
			Log.d(TAG, "Task2 is running...");
		}	
	}
	
	//��������
	public class Task8 extends TimerTask{
		
		private List<TaskParameterEntity> paraList ;
		private int alerter;
		private String scheduleName;
		
		//���幹�췽��
		public Task8(List<TaskParameterEntity> paraList, int alerter, String scheduleName){
			this.paraList = paraList;
			this.alerter = alerter;
			this.scheduleName = scheduleName;
		}	
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			TaskNotify tn =  new TaskNotify(MainService.this);
			tn.Notify(alerter, scheduleName);
			
			//ȡ��SD��״̬
			String state = Environment.getExternalStorageState();
			Log.d(TAG, "sd card state: " + state);
			
			//ȡ��sdCard·��
			String path = Environment.getExternalStorageDirectory().getPath() +"/wallpapper" ;
			
			//ע�⣺sdCard��Ŀ¼�ŵ��ļ�������ͼƬ��ʽ������BitmapFactoryת��ʱ����
			//����sdCard��Ŀ¼�µ��ļ����������ļ���
			Log.d(TAG, "path=" + path);
			File file = new File(path);							
			StringBuilder sb = new StringBuilder();

			for (File f : file.listFiles()) {
				if (f.isDirectory() == false && 
						f.getName().substring(f.getName().lastIndexOf(".") + 1).equals("jpg")) {
					sb.append(path + "/" + f.getName() + ",");
				}
			}
			
			Log.d(TAG, sb.toString());
			
			String allWallpapper[] = sb.toString().split(",");
			Random rd = new Random();
			
			int random = rd.nextInt(allWallpapper.length - 1);
			
			Log.d(TAG, "random number: " + random);
			
			Bitmap bitWallpaper = BitmapFactory.decodeFile(allWallpapper[random]);
			try {
				setWallpaper(bitWallpaper);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Log.d(TAG, "Task8 is running...");
		}	
	}
		
	//Wifi
	public class Task3 extends TimerTask{
		
		private List<TaskParameterEntity> paraList;
		private int alerter;
		private String scheduleName;
		
		//���幹�췽��
		public Task3(List<TaskParameterEntity> paraList, int alerter, String scheduleName){
			this.paraList = paraList;
			this.alerter = alerter;
			this.scheduleName = scheduleName;
		}	
	  
		@Override
		public void run() {
			
			TaskNotify tn =  new TaskNotify(MainService.this);
			tn.Notify(alerter, scheduleName);
			
			String onoroff= paraList.get(0).getValue();
			WifaManage wm = new WifaManage(MainService.this);

			if (Boolean.valueOf(onoroff)) {
				wm.OpenWifi();
			}else{
				wm.CloseWifi();
			}
			
			Log.d(TAG, "Task3 is running...");
		}	
	}
		
//	//Bluetooth
//	public class Task4 extends TimerTask{
//		
//		private List<TaskParameterEntity> paraList;
//		private int alerter;
//		private String scheduleName;
//		
//		//���幹�췽��
//		public Task4(List<TaskParameterEntity> paraList, int alerter, String scheduleName){
//			this.paraList = paraList;
//			this.alerter = alerter;
//			this.scheduleName = scheduleName;
//		}	
//		
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			Log.d(TAG, "Task4 is running...");
//			
//			TaskNotify tn =  new TaskNotify(ctx);
//			tn.Notify(alerter, scheduleName);
//			
//			String onoroff= paraList.get(0).getValue();
//			BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();
//			
//			if (!bta.isEnabled())
//			{
//				Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				
//				ctx.startActivity(intent);
//			}
//			
////			if (Boolean.valueOf(onoroff)) {
////				//bluetoothAdapter.enable();
////			}
////			else{
////				//bluetoothAdapter.disable();
////			}
//		}	
//	}
	
	//���Ͷ���
	public class Task7 extends TimerTask {

		private List<TaskParameterEntity> paraList;
		private int alerter;
		private String scheduleName;
		
		//���幹�췽��
		public Task7(List<TaskParameterEntity> paraList, int alerter, String scheduleName){
			this.paraList = paraList;
			this.alerter = alerter;
			this.scheduleName = scheduleName;
		}	

		@Override
		public void run() {
			// TODO Auto-generated method stub			
			TaskNotify tn =  new TaskNotify(MainService.this);
			tn.Notify(alerter, scheduleName);
			
			Log.d(TAG, "Task7 is running...");
			
			String note = null;
//			String[] text = null;
			String phonenum = null;
			// ��ò�ѯ�Ĳ���ֵ
//			for (int i = 0; i < list.size()+1; i++) {
//				TaskParameterEntity tpEntity = list.get(0);
//				if (tpEntity.getPid().equals("1")) {
//					phonenum = tpEntity.getValue(); // ��ò���ID =1�еĵ绰����
//				} else {
//					note = tpEntity.getValue(); // ��ò���ID�� =2�Ķ�������
//				}
//				Log.d("mainserviece","Mytask_sendsms   " +"phonenum"+phonenum +"  note"+note);
//			}
			phonenum =paraList.get(0).getValue();
			note = paraList.get(1).getValue();
			Log.d("mainserviece","Mytask_sendsms" +3+note+"--"+phonenum);
//			if (note.length() > 70) {
//				for (int i = 0; i < (note.length() / 70) + 1; i++) {
//					text[i] = note.substring(i * 70, (i + 1) * 70);// ����������ݳ���70���ֽ�
//					// �����ŷ�Ϊ��������
//				}
//			} else {
//				text[0] = note;
//			}
//			Log.d("mainserviece","Mytask_sendsms" +4+"text[0]"+text[0]);
			// ��ö��ŷ��͵Ķ���
			PendingIntent pIntent = PendingIntent.getBroadcast(MainService.this, 0,
					new Intent(), 0);
			SmsManager sManager = SmsManager.getDefault();
//			for (int i = 0; i < text.length; i++) {
//				sManager.sendTextMessage(phonenum, null, text[i], pIntent, null);
				sManager.sendTextMessage(phonenum, null, note, pIntent, null);
				Log.d("mainserviece","Mytask_sendsms" +5);
//			}
		}
	}

	//����绰
	public class Task6 extends TimerTask {
		private List<TaskParameterEntity> paraList;
		private int alerter;
		private String scheduleName;
		
		//���幹�췽��
		public Task6(List<TaskParameterEntity> paraList, int alerter, String scheduleName){
			this.paraList = paraList;
			this.alerter = alerter;
			this.scheduleName = scheduleName;
		}	

		@Override
		public void run() {
			// TODO Auto-generated method stub
			TaskNotify tn =  new TaskNotify(MainService.this);
			tn.Notify(alerter, scheduleName);
			
			Log.d(TAG, "Task6 is running...");
//
//			new AlertDialog.Builder(ctx).setTitle("1111").setMessage(
//					"�Ƿ񲦴��Ѷ�����绰").setPositiveButton("OK",
//					new DialogInterface.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Log.d("MAINSERVICE", "Mytask_call" +2+"");
							String num = "";
							Log.d("MAINSERVICE", "Mytask_call" +3+"");
							for (int i = 0; i < paraList.size(); i++) {
								TaskParameterEntity tpEntity = paraList.get(0);
								if (tpEntity.getPid().equals("2")) {
									num = tpEntity.getValue();
									// ��ò���ID =1�еĵ绰����
								}
							}
							Log.d("MAINSERVICE", "Mytask_call" +4+"The phoneNum is "+num);
							Intent callintent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + num));
							callintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(callintent);
							Log.d("MAINSERVICE", "Mytask_call" +5+"");
							Log.d("MAINSERVICE", "Mytask_call" +6+"");
						}
//					}).setNegativeButton("ȡ��",
//					new DialogInterface.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							// TODO Auto-generated method stub
//
//						}
//					}).show();
//		}
	}
	
	//����ģʽ
	public class Task5 extends TimerTask {
		private List<TaskParameterEntity> paraList;
		private int alerter;
		private String scheduleName;
		
		//���幹�췽��
		public Task5(List<TaskParameterEntity> paraList, int alerter, String scheduleName){
			this.paraList = paraList;
			this.alerter = alerter;
			this.scheduleName = scheduleName;
		}	

		@Override
		public void run() {
			// TODO Auto-generated method stub
			TaskNotify tn =  new TaskNotify(MainService.this);
			tn.Notify(alerter, scheduleName);
			
			Log.d(TAG, "Task5 is running...");
			//true �ǿ�����ģʽ  false�ǹط���ģʽ
			 boolean isEnabled = Boolean.valueOf(paraList.get(0).getValue());
	        	// toggle airplane mode
	        	Settings.System.putInt(
	        			MainService.this.getContentResolver(),
	        	      Settings.System.AIRPLANE_MODE_ON, isEnabled ? 1 : 0);
	        	// Post an intent to reload
	        	Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
	        	intent.putExtra("state", isEnabled);
	        	sendBroadcast(intent);
		}
	}
	
}
