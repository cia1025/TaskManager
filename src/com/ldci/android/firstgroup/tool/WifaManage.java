package com.ldci.android.firstgroup.tool;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifaManage {
	//声明WifiManager对象
	private WifiManager wifiManager;	
	//构造器 
    public  WifaManage(Context context) 
    { 
        //取得WifiManager对象 
    	wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);        
    } 

    //打开WIFI 
    public void OpenWifi() {
	    if (wifiManager.isWifiEnabled()) 
	    { 
	    	//若已打开则不执行此操作
	     }else{
    		 wifiManager.setWifiEnabled(true);
	     }
     }
    
    //关闭WIFI
    public void CloseWifi(){
    	if(wifiManager.isWifiEnabled()){
    		
    	wifiManager.setWifiEnabled(false); 
    	}else{
    		//若已关闭，不执行此操作
    	}
    }    
}
	          
		





