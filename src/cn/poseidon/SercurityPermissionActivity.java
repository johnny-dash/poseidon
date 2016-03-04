package cn.poseidon;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SercurityPermissionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	Class spClass;
	try {
		//����asp��
		spClass = getClass().getClassLoader().loadClass("android.widget.AppSecurityPermissions");
		// ��ȡasp�� ���췽�� ����ִ�����е� getPermissionView����(����ϵͳ�����ó����Դ�����֪���캯������������һ���������� һ���ǰ���)
		Constructor constructor =  spClass.getConstructor(Context.class,String.class);
		//ʵ����һ������
		//Object aspObject = constructor.newInstance(this,"cn.phoniex.ssg");
		// ��ȡ������Activity��intent���洫�ݹ����İ���
		String pkgname = getIntent().getStringExtra("packagename");
		Object aspObject = constructor.newInstance(new Object[]{SercurityPermissionActivity.this,pkgname});
		//��ȡ���еķ���   �ú����ǲ���Ҫ������
		Method aspMethod = spClass.getDeclaredMethod("getPermissionsView", new Class[]{});
		//���øú��� ����Ϊasp��һ������  �� �ú�����Ҫ�Ĳ���
		View view = (View) aspMethod.invoke(aspObject, new Object[]{});
		setContentView(view);
				
				
	} catch (Exception e) {
		e.printStackTrace();
		Toast.makeText(SercurityPermissionActivity.this, "��packageδ�ҵ�", 0).show();
	}

	
	}

	
	
}
