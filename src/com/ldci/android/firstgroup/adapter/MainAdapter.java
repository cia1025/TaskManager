package com.ldci.android.firstgroup.adapter;


import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ldci.android.firstgroup.R;
import com.ldci.android.firstgroup.entity.TaskSEntity;

public class MainAdapter extends BaseAdapter{
	//生成功能列表tasklist
	private List<TaskSEntity> tasklist;
	private Context ctx;
	public MainAdapter(Context ctx,List<TaskSEntity> tasklist){
		this.ctx = ctx;
		this.tasklist = tasklist;
	}
	//直接设置功能列表
	public void setlist(List<TaskSEntity> tasklist){
		this.tasklist = tasklist;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return tasklist.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return tasklist.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return Long.valueOf(tasklist.get(arg0).getScheduleid());
	}
	
	//获得列表中功能信息并设置功能名
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		
		// TODO Auto-generated method stub
		LayoutInflater layoutInflater = (LayoutInflater) ctx
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout l = (RelativeLayout) layoutInflater.inflate(
				R.layout.mainitemview, null);
		
		TaskSEntity tSEntity = tasklist.get(arg0);
		
		//补零显示
		String starttime = tSEntity.getStarttime();
		String[] fields = starttime.split("\\:");
		if (fields[0].length() == 1)
			fields[0] = "0" + fields[0];
		if (fields[1].length() == 1)
			fields[1] = "0" + fields[1];
		
		TextView tvtime = (TextView) l.findViewById(R.id.Menu_itemtime);
		//tvtime.setText(tSEntity.getStarttime());
		tvtime.setText(fields[0] + ":" + fields[1]);
		
		TextView tvname = (TextView) l.findViewById(R.id.Menu_itemname);
		tvname.setText(tSEntity.getSname());
		
		TextView tvloop = (TextView) l.findViewById(R.id.Menu_itemloop);
		int loop = Integer.valueOf(tSEntity.getLoop());
		
		if (loop == 0){ //周循环
			tvloop.setText(
					ctx.getString(R.string.csweek) + " : " + 
							tSEntity.getStartdate() + " : " +
							getSelectedWeekDays(tSEntity.getLoopinfo()));
		}
		else if (loop == 1){ //一次性
			tvloop.setText(ctx.getString(R.string.csonce) + " : " +
					tSEntity.getStartdate());
		}
		else if (loop == 2){//每隔指定天数
			tvloop.setText(ctx.getString(R.string.csday) + " : " +
					tSEntity.getStartdate() + " : " +
					tSEntity.getLoopinfo());
		}
		else if (loop == 3){//每隔指定分钟
			tvloop.setText(ctx.getString(R.string.csminute) + " : " +
					tSEntity.getStartdate() + " : " +
					tSEntity.getLoopinfo());
		}
		
		return l;
	}
	
	private String getSelectedWeekDays(String str)
	{
		String weekdays="";
		
		if (str.charAt(0) == '1'){
			weekdays += ctx.getString(R.string.info_sunday);
		}
		if (str.charAt(1) == '1'){
			weekdays += ctx.getString(R.string.info_monday);
		}
		if (str.charAt(2) == '1'){
			weekdays += ctx.getString(R.string.info_tuesday);
		}
		if (str.charAt(3) == '1'){
			weekdays += ctx.getString(R.string.info_wednesday);
		}
		if (str.charAt(4) == '1'){
			weekdays += ctx.getString(R.string.info_thursday);
		}
		if (str.charAt(5) == '1'){
			weekdays += ctx.getString(R.string.info_friday);
		}
		if (str.charAt(6) == '1'){
			weekdays += ctx.getString(R.string.info_saturday);
		}
		
		return weekdays;
	}
	
}
