package cn.poseidon;

import com.example.poseidon.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Setup3ConfigActivity extends Activity {

	private EditText et_safe_number;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.setup3config);
		
		et_safe_number = (EditText) findViewById(R.id.et_safe_number);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		
		String safe_number = sp.getString("safe_number", "");
		if("".equals(safe_number)){
			
		}else{
			et_safe_number.setText(safe_number);
		}
	}
	
	public void select_contact(View v){
		Intent intent = new Intent(this,SelectContactActivity.class);
		startActivityForResult(intent, 0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 0){
			if(data != null){
				String number = data.getStringExtra("number");
				et_safe_number.setText(number);
			}
		}
	}
	
	public void pre(View v){
		Intent intent = new Intent(this,Setup2ConfigActivity.class);
		startActivity(intent);
		finish();
	}
	
	public void next(View v){
		
		//判断安全号码是否为空
		String safe_number = et_safe_number.getText().toString();
		if("".equals(safe_number)){
			Toast.makeText(this, "安全号码不能为空", 1).show();
		}else{
			//保存安全号码到sp
			Editor editor = sp.edit();
			editor.putString("safe_number", safe_number);
			editor.commit();
			
			Intent intent = new Intent(this,Setup4ConfigActivity.class);
			startActivity(intent);
			finish();
		}
	}
}
