package com.ldci.android.firstgroup.tool;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifaManage {
	//����WifiManager����
	private WifiManager wifiManager;	
	//������ 
    public  WifaManage(Context context) 
    { 
        //ȡ��WifiManager���� 
    	wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);        
    } 

    //��WIFI 
    public void OpenWifi() {
	    if (wifiManager.isWifiEnabled()) 
	    { 
	    	//���Ѵ���ִ�д˲���
	     }else{
    		 wifiManager.setWifiEnabled(true);
	     }
     }
    
    //�ر�WIFI
    public void CloseWifi(){
    	if(wifiManager.isWifiEnabled()){
    		
    	wifiManager.setWifiEnabled(false); 
    	}else{
    		//���ѹرգ���ִ�д˲���
    	}
    }    
}
	          
		





