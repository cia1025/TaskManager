package com.ldci.android.firstgroup.adapter;

import java.util.List;

import com.ldci.android.firstgroup.R;
import com.ldci.android.firstgroup.entity.TaskEntity;
import android.R.anim;
import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class NewTaskAdapter extends BaseAdapter{
	//���ɹ����б�tasklist
//	private RadioGroup rGroup;
	private List<TaskEntity> tasklist;
	private Context ctx;
	public NewTaskAdapter(Context ctx,List<TaskEntity> tasklist){
		this.ctx = ctx;
		this.tasklist = tasklist;
//		this.rGroup = rGroup;
	}
	//ֱ�����ù����б�
	public void setlist(List<TaskEntity> tasklist){
		this.tasklist = tasklist;
	}
	//����
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return tasklist.size();
	}
	//����
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return tasklist.get(arg0);
	}
	//����
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	//����б��й�����Ϣ�����ù�����
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		LayoutInflater layoutInflater = (LayoutInflater) ctx
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout l = (LinearLayout) layoutInflater.inflate(R.layout.newtaskitemview, null);
		
		TaskEntity taskentity = (TaskEntity) getItem(arg0);
		TextView name = (TextView) l.findViewById(R.id.newtask_itemname);
		name.setText(taskentity.getTaskname());
//		RadioButton rButton = (RadioButton) l.findViewById(R.id.newtask_rb);
//		rGroup.addView(rButton);
		return l;
	}
	
}
