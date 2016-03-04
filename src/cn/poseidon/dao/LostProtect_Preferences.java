package cn.poseidon.dao;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class LostProtect_Preferences {
	public static final String PFNAME = "config";//SharePreference�ļ���
	
	//ʹ��SharePreference��������
	public static void save(Context context, String key, Object value){
		//��ȡSharePreference����
		SharedPreferences sp = context.getSharedPreferences(PFNAME, 0);
		//��ñ༭��
		Editor editor = sp.edit();
		//�ж��������Ͳ�����
		if(value instanceof String){
			editor.putString(key, (String)value);
		}
		else if (value instanceof Boolean){
			editor.putBoolean(key,(Boolean)value);
		}
		//�ύ�޸�
		editor.commit();
	}
	
	//ʹ��SharePreference��ȡ����
	public static String getString(Context context, String key){
		SharedPreferences sp = context.getSharedPreferences(PFNAME, 0);
		return sp.getString(key, "");
	}
	
	public static Boolean getBoolean(Context context, String key){
		SharedPreferences sp = context.getSharedPreferences(PFNAME, 0);
		return sp.getBoolean(key, false);
	}
	
}