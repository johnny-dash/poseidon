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
	                //��ѯraw_contact��õ���ϵ�˵�_id
	                Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
	                //��ѯdata��	            
	                Uri dataUri = Uri.parse("content://com.android.contacts/data");
	                Cursor cursor = contentResolver.query(uri, null, null, null, null);
	                while(cursor.moveToNext())
	                {
	                        info = new ContactInfo();
	                        String id = cursor.getString(cursor.getColumnIndex("_id"));
	                        String name = cursor.getString(cursor.getColumnIndex("display_name"));
	                        info.setName(name);
	                        //ͨ��raw_contacts�����id�õ�data�����Ӧ������
	                        Cursor dataCursor = contentResolver.query(dataUri, null, "raw_contact_id = ? ", new String[] {id}, null);
	                        while(dataCursor.moveToNext())
	                        {
	                                String type = dataCursor.getString(dataCursor.getColumnIndex("mimetype"));
	                                //�������ͣ�ֻҪ�绰�������͵����ݣ�mimetype=5ʱ��vnd.android.cursor.item/phone_v2��ʾ���룩
	                                if(type.equals("vnd.android.cursor.item/phone_v2"))
	                                {
	                                        String number = dataCursor.getString(dataCursor.getColumnIndex("data1"));//�õ�����
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

