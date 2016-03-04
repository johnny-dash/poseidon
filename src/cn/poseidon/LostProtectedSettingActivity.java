package cn.poseidon;


import cn.poseidon.receiver.MyAdmin;

import com.example.poseidon.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LostProtectedSettingActivity extends Activity implements OnClickListener{

	private static final int MENU_CHANGE_NAME_ID = 0;
	private TextView tv_lost_protected_setting_safe_number;
	private CheckBox cb_lost_protected_setting_protecting;
	private TextView tv_lost_protected_setting_reset;
	private SharedPreferences sp;
	private DevicePolicyManager devicePolicyManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//����ϵͳ����Ա��Ȩ��
    	devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
    	
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		setContentView(R.layout.lost_protected_setting);
		
		tv_lost_protected_setting_safe_number = (TextView) findViewById(R.id.tv_lost_protected_setting_safe_number);
		String safe_number = sp.getString("safe_number", "");
		tv_lost_protected_setting_safe_number.setText(safe_number);
		
		cb_lost_protected_setting_protecting = (CheckBox) findViewById(R.id.cb_lost_protected_setting_protecting);
		boolean isprotected = sp.getBoolean("isprotected", false);
		if(isprotected){
			cb_lost_protected_setting_protecting.setChecked(true);
			cb_lost_protected_setting_protecting.setText("���������Ѿ�����");
		}else{
			cb_lost_protected_setting_protecting.setChecked(false);
			cb_lost_protected_setting_protecting.setText("��������û�п���");
		}
		cb_lost_protected_setting_protecting.setOnClickListener(this);
		
		tv_lost_protected_setting_reset = (TextView) findViewById(R.id.tv_lost_protected_setting_reset);
		tv_lost_protected_setting_reset.setOnClickListener(this);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.tv_lost_protected_setting_reset:
			Intent intent = new Intent(this,Setup1ConfigActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.cb_lost_protected_setting_protecting:
			boolean isprotected = sp.getBoolean("isprotected", false);
			if(isprotected){
				cb_lost_protected_setting_protecting.setChecked(false);
				cb_lost_protected_setting_protecting.setText("��������û�п���");
				Editor editor = sp.edit();
				editor.putBoolean("isprotected", false);
				editor.commit();
			}else{
				cb_lost_protected_setting_protecting.setChecked(true);
				cb_lost_protected_setting_protecting.setText("���������Ѿ�����");
				Editor editor = sp.edit();
				editor.putBoolean("isprotected", true);
				editor.commit();
				
				activeAdmin();
			}
			break;
		default:
			break;
		}
	}
	
	//����ϵͳ����Ա��Ȩ��
	private void activeAdmin() {
		//����Ȩ��
		ComponentName componentName = new ComponentName(this, MyAdmin.class);
		//�жϸ�����Ƿ���ϵͳ����Ա��Ȩ��
		boolean isAdminActive = devicePolicyManager.isAdminActive(componentName);
		if(!isAdminActive){
			//����ϵͳ��Ȩ���
			Intent intent = new Intent();
			//ָ������
			intent.setAction(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			//ָ�����Ǹ������Ȩ
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
			startActivity(intent);
		}
	}
}
