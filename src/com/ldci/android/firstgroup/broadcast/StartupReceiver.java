package com.ldci.android.firstgroup.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.ldci.android.firstgroup.service.MainService;

public class StartupReceiver extends BroadcastReceiver {
	private static final String TAG = "StartupReceiver";
	private static Intent global_serviceIntent;
	
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		String action = arg1.getAction();
		System.out.println("StartupReceiver.onReceive(), global_serviceIntent == "+global_serviceIntent);
		//当系统启动完成后，启动一个全局service，负责注册所有的计划任务
		if (action.equals(Intent.ACTION_BOOT_COMPLETED))
		{ 
			Toast.makeText(arg0, "The main service started!", Toast.LENGTH_SHORT)
			.show();
			
			global_serviceIntent = new Intent(arg0, MainService.class);	
			Log.d(TAG,"BOOT COMPLETED!");
			arg0.startService(global_serviceIntent);
		}
		
		// 这是内部模拟的系统启动事件，因为不可能经常重启系统来调试
		if (action.equals(MainService.ACTION_BOOT_COMPLETED1))
		{
			Toast.makeText(arg0, "MainService started!", Toast.LENGTH_SHORT)
			.show();
			
			global_serviceIntent = new Intent(arg0, MainService.class);	
			Log.d(TAG,"BOOT COMPLETED 1 !");
			arg0.startService(global_serviceIntent);
		}
		
		// 当计划任务被保存的时候，触发该消息并重启全局的Service，
		// 从而取消掉所有已注册的计划任务，然后重新注册
		if (action.equals(MainService.ACTION_SERVICE_RESTART))
		{
			Toast.makeText(arg0, "MainService restarted!", Toast.LENGTH_LONG)
			.show();
			
			arg0.stopService(global_serviceIntent);
			arg0.startService(global_serviceIntent);
		}
		
		if (action.equals(MainService.ACTION_SERVICE_START))
		{
			Toast.makeText(arg0, "MainService started!", Toast.LENGTH_LONG)
			.show();
			
			arg0.startService(global_serviceIntent);
		}
		
		if (action.equals(MainService.ACTION_SERVICE_STOP))
		{
			Toast.makeText(arg0, "MainService stopped!", Toast.LENGTH_LONG)
			.show();
			
			arg0.stopService(global_serviceIntent);
		}
	}
}
