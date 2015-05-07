package com.ldci.android.firstgroup.tool;

import java.io.IOException;
import com.ldci.android.firstgroup.R;
import android.app.Service;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

public class TaskNotify {
	//得到吐丝里显示的内容
	private int  alert_start= R.string.alert_start;
	private static String tag="TaskNotify";
	private String str;
	private Context ctx;
	private Vibrator vibrator; 
	private MediaPlayer myplayer;

	//构造方法
	public  TaskNotify(Context ctx){
		this.ctx = ctx;
	}
   
	public  void Notify(int type,String content){
	  Log.d(tag, type + content);
		 
		//TOAST提醒
	  if(type==1){
		  Log.d(tag,"toast1");
		  str = ctx.getResources().getString(alert_start);
		  Log.d(tag,"toast2");
		  Log.d(tag,ctx.getClass().getName());
		  Toast.makeText(ctx,content + str, Toast.LENGTH_LONG).show();
		  //震动提醒
	  }else if(type==2){
		   Log.d(tag,"shake");
		   vibrator = (Vibrator) ctx.getSystemService(Service.VIBRATOR_SERVICE);   
		   vibrator.vibrate( new long[]{1000,50,1000,50,2000},-1); 
		   //响铃提醒
	  }else if(type==3){
		  Log.d(tag,"ring");
		  myplayer = new MediaPlayer();
		  try {
			myplayer.setDataSource("/SDcard/myring.mp3");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			myplayer.release();
		}
		
		  try {
			myplayer.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			myplayer.release();
		}
		  myplayer.start();
		  myplayer.release();		  
	  }
	}	
}

