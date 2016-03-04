package cn.poseidon.dao;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class LostProtect_Preferences {
	public static final String PFNAME = "config";//SharePreference文件名
	
	//使用SharePreference保存数据
	public static void save(Context context, String key, Object value){
		//获取SharePreference对象
		SharedPreferences sp = context.getSharedPreferences(PFNAME, 0);
		//获得编辑器
		Editor editor = sp.edit();
		//判断数据类型并保存
		if(value instanceof String){
			editor.putString(key, (String)value);
		}
		else if (value instanceof Boolean){
			editor.putBoolean(key,(Boolean)value);
		}
		//提交修改
		editor.commit();
	}
	
	//使用SharePreference读取数据
	public static String getString(Context context, String key){
		SharedPreferences sp = context.getSharedPreferences(PFNAME, 0);
		return sp.getString(key, "");
	}
	
	public static Boolean getBoolean(Context context, String key){
		SharedPreferences sp = context.getSharedPreferences(PFNAME, 0);
		return sp.getBoolean(key, false);
	}
	
}