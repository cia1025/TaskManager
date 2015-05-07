package com.ldci.android.firstgroup.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.ldci.android.firstgroup.R;
import com.ldci.android.firstgroup.adapter.MenuAdapter;

public class EditSMS extends Activity {
	private int CAT;
	private EditText etSMS;
	private String phoneparam;
	private String phoneNum = null;
    private static final int PICK_CONTACT = 3;  
    private String TAG = "EditPhone";
	
	private MenuAdapter madapter;
	private GridView gv;
	private int[] gv_array = { R.string.detail_back, R.string.detail_cancel,
			R.string.detail_ok };
	private String[] gv_arraystr = new String[gv_array.length];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// �õ���ͼ
		Intent it = this.getIntent();
		CAT = it.getExtras().getInt("CAT");
		Log.d(TAG,"CAT = "+CAT);
			phoneparam = it.getExtras().getString("phoneparam");
	
		// ���ò���
		setContentView(R.layout.editsms);
		// �ҵ��ؼ�
		etSMS = (EditText) findViewById(R.id.editSMS);
		
		// ���intent�д�����������ʾ��editview��
		if(CAT == Main.REQ_EDIT){
			etSMS.setText(phoneparam);
		}else if(CAT == Main.REQ_ADD){
			etSMS.setHint("��������");
		}	
		gv = (GridView) findViewById(R.id.gv);
		for (int i = 0; i < gv_array.length; i++) {
			gv_arraystr[i] = this.getResources().getString(gv_array[i]);
		}
		madapter = new MenuAdapter(this);
		gv.setAdapter(madapter.getMenuAdapter(gv_arraystr));
		gv.setNumColumns(3);// ����ÿ������
		gv.setGravity(Gravity.CENTER);// λ�þ���
		gv.setVerticalSpacing(10);// ��ֱ���
		gv.setHorizontalSpacing(10);// ˮƽ���

		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				switch (arg2) {

				case 0: // ����
					finish();
					break;
				case 1: // ȡ��
					setResult(Main.RESULT_USER_CANCEL);
					finish();
					break;
				case 2:// ȷ��
					Intent it = new Intent();
					String phoneparam = etSMS.getText().toString();
					it.putExtra("phoneparam", phoneparam);
					setResult(RESULT_OK, it);
					finish();
					break;
				}
			}
		});

	}
}
