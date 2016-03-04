package cn.poseidon.dao;

import java.util.ArrayList;

import cn.poseidon.db.BlackListDBHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BlackListDAO {

	private BlackListDBHelper blackdbhelper;
	private SQLiteDatabase blackdb;
	private Context context;
	
	public BlackListDAO(Context context) {
		this.context = context;
		blackdbhelper = new BlackListDBHelper(context);
	}
	
	//��ӵ�������
	public boolean addNo(String number) {
		blackdb  = blackdbhelper.getWritableDatabase();
		boolean bRet = false;
		if (blackdb.isOpen()) {
			ContentValues values = new ContentValues();
			values.put("number", number);
			long lret = blackdb.insert("blacklist", "number", values);
			if (lret != -1) {
				bRet = true;
			}else {
				System.err.println("addNo()���ִ���");
			}
			blackdb.close();
		}
		//bRet = true;
		return bRet;		
	}
	
	//�õ����к���
	public ArrayList<String> getallNo() {
		// TODO Auto-generated method stub
		blackdb  = blackdbhelper.getReadableDatabase();
		String tmpstr;
		ArrayList<String> numbers = new ArrayList<String>();
		if (blackdb.isOpen()) {
			Cursor cursor = blackdb.rawQuery("select number from blacklist ", null);			
			while (cursor.moveToNext()) {
				tmpstr = cursor.getString(cursor.getColumnIndex("number"));
				numbers.add(tmpstr);				
			}			
			cursor.close();
			blackdb.close();
		}
		return numbers;
	}
	
	//ɾ����������
	public boolean delNo(String number) {
		blackdb  = blackdbhelper.getWritableDatabase();
		boolean bRet = false;
		if (blackdb.isOpen()) {
			int iret = blackdb.delete("blacklist", "number = ?", new String[]{number});
			if (iret != 0) {
				bRet = true;
			}
			blackdb.close();
		}		
		return bRet;
	}
	
	//������к���
	public void removeAll() {
		blackdb = blackdbhelper.getWritableDatabase();
		if (blackdb.isOpen()) {
			blackdb.execSQL("delete from blacklist");
		}		
	}
	
	//�޸ĵ�ǰ����
	public boolean updateNo(String oldone, String newone) {
		// TODO Auto-generated method stub
		blackdb  = blackdbhelper.getWritableDatabase();
		boolean bRet = false;
		//����º���
		ContentValues values = new ContentValues();
		values.put("number", newone);
		//ɾ���ɺ���
		if (blackdb.isOpen()) {
			int iret =blackdb.update("blacklist", values, "where number =?", new String[]{oldone});
			if (iret != 0) {
				bRet =true;
			}
			blackdb.close();
		}
		return bRet;
	}
	
	//����delNo(),����ɾ��
	public int delNos(ArrayList<String> numbers) {
		// TODO Auto-generated method stub
		boolean bret = true;
		int iRet = 0;
		int icut = numbers.size();
		for (int i = 0; i < icut; i++) {
			bret = delNo(numbers.get(i));
			if (bret) {
				iRet++;
			}			
		}
		return iRet;
	}

}
