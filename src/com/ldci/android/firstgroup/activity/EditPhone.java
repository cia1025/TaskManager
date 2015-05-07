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

public class EditPhone extends Activity {
	private int CAT;
	private EditText etphoneparam;
	private TextView tvParameter;
	private String phoneparam;
	private Button btContact;
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
		// 得到意图
		Intent it = this.getIntent();
		CAT = it.getExtras().getInt("CAT");
		Log.d(TAG,"CAT = "+CAT);
			phoneparam = it.getExtras().getString("phoneparam");
	
		// 设置布局
		setContentView(R.layout.editphone);
		// 找到控件
		etphoneparam = (EditText) findViewById(R.id.editPhoneParam);
		btContact = (Button)findViewById(R.id.btContact);
		tvParameter = (TextView)findViewById(R.id.tvPhoneParam);
		
		// 如果intent中带有数据则显示到editview中
		if(CAT == Main.REQ_EDIT){
			if(phoneparam.equals("电话号码")){
				etphoneparam.setHint("电话号码");
			}
			etphoneparam.setText(phoneparam);
		}else if(CAT == Main.REQ_ADD){
			etphoneparam.setHint("电话号码");
		}	
		gv = (GridView) findViewById(R.id.gv);
		for (int i = 0; i < gv_array.length; i++) {
			gv_arraystr[i] = this.getResources().getString(gv_array[i]);
		}
		madapter = new MenuAdapter(this);
		gv.setAdapter(madapter.getMenuAdapter(gv_arraystr));
		gv.setNumColumns(3);// 设置每行列数
		gv.setGravity(Gravity.CENTER);// 位置居中
		gv.setVerticalSpacing(10);// 垂直间隔
		gv.setHorizontalSpacing(10);// 水平间隔

		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				switch (arg2) {

				case 0: // 返回
					finish();
					break;
				case 1: // 取消
					setResult(Main.RESULT_USER_CANCEL);
					finish();
					break;
				case 2:// 确定
					Intent it = new Intent();
					String phoneparam = etphoneparam.getText().toString();
					it.putExtra("phoneparam", phoneparam);
					setResult(RESULT_OK, it);
					finish();
					break;
				}
			}
		});
		//点击联系人按钮监听事件
		btContact.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			 Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);   
             startActivityForResult(intent, PICK_CONTACT); 
			}});
		}

    @Override  
    public void onActivityResult(int reqCode, int resultCode, Intent data){   
        super.onActivityResult(reqCode, resultCode, data);   
        Log.d(TAG, "onActivityResult");   
        switch(reqCode){   
           case (PICK_CONTACT):   
               Log.d(TAG, "onActivityResult PICK_CONTACT");   
             if (resultCode == Activity.RESULT_OK){   
                                 Uri contactData = data.getData();   
                                 ContentResolver  c = getContentResolver();   
                 Cursor cursor= c.query(contactData, null, null, null, null);   
                                 if (cursor.moveToFirst()){   
                                            
                      String[] PHONES_PROJECTION = new String[] { "_id","display_name","data1","data3"};  
//                      String[] PHONES_PROJECTION = new String[] { "_id","has_phone_number","data1","data3"}; 
                      String contactId = cursor.getString(cursor.getColumnIndex(PhoneLookup._ID));   
                         
                    Cursor phone = c.query(   
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,   
                            PHONES_PROJECTION,   
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID   
                                    + "=" + contactId, null, null);   
                    // name type ..   
                    while (phone.moveToNext()) {   
                        int i = phone.getInt(0);   
                           
                        String str = phone.getString(1);   
                                                str += " ";   
                        phoneNum = phone.getString(2);   
                                                str += " ";   
                        str += phone.getString(3);  
                        Log.d("contact",""+str);
                                            }   

                     // other data is available for the Contact.  I have decided   
                     //    to only get the name of the Contact.   
//                     String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                     Log.d(TAG, "onActivityResult PICK_CONTACT RESULT_OK 444");   
                 //    Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();   
                     etphoneparam.setText(phoneNum);   
                 }   
             }   
        }   
    }  
}
