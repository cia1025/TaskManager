package com.ldci.android.firstgroup.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import com.ldci.android.firstgroup.R;
import android.content.Context;
import android.widget.SimpleAdapter;

public class MenuAdapter {
    	
    	private Context context;
    	
    	public MenuAdapter(Context context) {
    		super();
    		this.context = context;
    	}
    	
    	public  SimpleAdapter getMenuAdapter(String[] menuNameArray) 
    	{
    		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
    		for (int i = 0; i < menuNameArray.length; i++) {
    			HashMap<String, Object> map = new HashMap<String, Object>();
    		//	map.put("itemImage", imageResourceArray[i]);
    			map.put("itemText", menuNameArray[i]);
    			data.add(map);
    		}
    		SimpleAdapter simpleAdapter = new SimpleAdapter(context, data,
    				R.layout.cyclestyle_menu, new String[] { "itemText" },
    				new int[] { R.id.item_text });
    		return simpleAdapter;
    	}
    }