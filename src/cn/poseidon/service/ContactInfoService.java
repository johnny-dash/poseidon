package cn.poseidon.service;

import java.util.ArrayList;
import java.util.List;

import cn.poseidon.domain.ContactInfo;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class ContactInfoService {
	        private Context context;
	        
	        public ContactInfoService(Context context)
	        {
	                this.context = context;
	        }
	        
	        public List<ContactInfo> getContactInfos()
	        {
	                List<ContactInfo> infos = new ArrayList<ContactInfo>();
	                ContactInfo info;
	                
	                ContentResolver contentResolver = context.getContentResolver();
	                //查询raw_contact表得到联系人的_id
	                Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
	                //查询data表	            
	                Uri dataUri = Uri.parse("content://com.android.contacts/data");
	                Cursor cursor = contentResolver.query(uri, null, null, null, null);
	                while(cursor.moveToNext())
	                {
	                        info = new ContactInfo();
	                        String id = cursor.getString(cursor.getColumnIndex("_id"));
	                        String name = cursor.getString(cursor.getColumnIndex("display_name"));
	                        info.setName(name);
	                        //通过raw_contacts里面的id拿到data里面对应的数据
	                        Cursor dataCursor = contentResolver.query(dataUri, null, "raw_contact_id = ? ", new String[] {id}, null);
	                        while(dataCursor.moveToNext())
	                        {
	                                String type = dataCursor.getString(dataCursor.getColumnIndex("mimetype"));
	                                //根据类型，只要电话这种类型的数据（mimetype=5时，vnd.android.cursor.item/phone_v2表示号码）
	                                if(type.equals("vnd.android.cursor.item/phone_v2"))
	                                {
	                                        String number = dataCursor.getString(dataCursor.getColumnIndex("data1"));//拿到数据
	                                        info.setPhone(number);
	            	                        infos.add(info);
	                                } 
	                        }
	                        dataCursor.close();
	                }
	                cursor.close();
	                return infos;
	        }
	}

