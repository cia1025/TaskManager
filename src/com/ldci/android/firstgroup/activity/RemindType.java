package com.ldci.android.firstgroup.activity;

import com.ldci.android.firstgroup.adapter.MenuAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.AdapterView.OnItemClickListener;
import com.ldci.android.firstgroup.R;

public class RemindType extends Activity {
	
	private GridView  gv;
	private MenuAdapter madapter;
	private ImageButton ibt1,ibt2,ibt3;
	private Boolean isCheck01 = false;
	private Boolean isCheck02 = false;
	private Boolean isCheck03 = false;
	
	private String alerter;
	private int CAT;
	
	private int[] gv_array = { R.string.detail_back, R.string.detail_cancel, R.string.detail_ok };
	private String[] gv_arraystr = new String[gv_array.length];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.remindtype);
		
		Intent it = this.getIntent();
		CAT = it.getExtras().getInt("CAT");
		alerter = it.getExtras().getString("alerter");
		
		findView();
		bottomMenu();
	
		//设置ImageButton图片的开启，关闭		
		ibt1.setImageResource(R.drawable.btoff);
		ibt2.setImageResource(R.drawable.btoff);
		ibt3.setImageResource(R.drawable.btoff);
		
		//设置ImageButton监听事件
		ibt1.setOnClickListener(new ClickEvent());
		ibt2.setOnClickListener(new ClickEvent());     
		ibt3.setOnClickListener(new ClickEvent());
		
		if (alerter.equals("1"))
		{
			ibt1.setImageResource(R.drawable.bton);
			isCheck01 = true;
		}
		else if  (alerter.equals("2"))
		{
			ibt2.setImageResource(R.drawable.bton);
			isCheck02 = true;
		}
		else if  (alerter.equals("3"))
		{
			ibt3.setImageResource(R.drawable.bton);
			isCheck03 = true;
		}
	}
	
	class ClickEvent implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getId() == ibt1.getId()) {
				if (!isCheck01) {
					ibt1.setImageResource(R.drawable.bton);
					isCheck01 = true;
					ibt2.setImageResource(R.drawable.btoff);
					ibt3.setImageResource(R.drawable.btoff);
					isCheck02 = false;
					isCheck03 = false;
				} else if (isCheck01) {
					ibt1.setImageResource(R.drawable.btoff);
					isCheck01 = false;
				}
			}
			if (v.getId() == ibt2.getId()) {
				if (!isCheck02) {
					ibt2.setImageResource(R.drawable.bton);
					isCheck02 = true;
					ibt1.setImageResource(R.drawable.btoff);
					ibt3.setImageResource(R.drawable.btoff);
					isCheck01 = false;
					isCheck03 = false;
				} else if (isCheck02) {
					ibt2.setImageResource(R.drawable.btoff);
					isCheck02 = false;
				}
			}
			if (v.getId() == ibt3.getId()) {
				if (!isCheck03) {
					ibt3.setImageResource(R.drawable.bton);
					isCheck03 = true;
					ibt1.setImageResource(R.drawable.btoff);
					ibt2.setImageResource(R.drawable.btoff);
					isCheck01 = false;
					isCheck02 = false;
				} else if (isCheck03) {
					ibt3.setImageResource(R.drawable.btoff);
					isCheck03 = false;
				}
			}	
		}
	}
	
	public void findView(){
		ibt1 = (ImageButton)findViewById(R.id.ImageButton01);
		ibt2 = (ImageButton)findViewById(R.id.ImageButton02);
		ibt3 = (ImageButton)findViewById(R.id.ImageButton03);
	}
	
	public void bottomMenu(){
		
		for (int i = 0; i < gv_array.length; i++) {
			gv_arraystr[i] = this.getResources().getString(gv_array[i]);
		}        
		
        gv=(GridView)findViewById(R.id.gv);     	
        madapter = new MenuAdapter(this);

//	        gv.setSelector(R.drawable.icon);
//	        gv.setBackgroundResource();// 设置背景
//	        gv.setBackgroundColor(Color.LTGRAY);
        gv.setNumColumns(3);// 设置每行列数
        gv.setGravity(Gravity.CENTER);// 位置居中
        gv.setVerticalSpacing(10);// 垂直间隔
        gv.setHorizontalSpacing(10);// 水平间隔
        
        // 设置菜单Adapter
        gv.setAdapter(madapter.getMenuAdapter(gv_arraystr));

        //监听菜单点击事件  
        gv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
			switch(arg2){
				case 0:
					finish();
					break;
				case 1:
					setResult(Main.RESULT_USER_CANCEL);
					finish();
					break;
				case 2:
					alerter = "0";
					if (isCheck01)
						alerter = "1";
					else if (isCheck02)
						alerter = "2";
					else if (isCheck03)
						alerter = "3";
					
					Intent it = new Intent();
					it.putExtra("alerter", alerter);
					setResult(RESULT_OK, it);
					finish();
					break;
				}
		}});
	}
}
