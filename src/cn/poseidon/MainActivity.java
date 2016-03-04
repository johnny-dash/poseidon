package cn.poseidon;

import com.example.poseidon.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class MainActivity extends Activity {
	//�������
	private ImageView ImageView1 = null;
	//���̹���
	private ImageView ImageView2 = null;
	//��ر���
	private ImageView ImageView3 = null;
	//ɧ������
	private ImageView ImageView4 = null;
	//��ȫɨ��
	private ImageView ImageView5 = null;
	//�ֻ�����
	private ImageView ImageView6 = null;	
	//�������
	private ImageView ImageView7 = null;	
	//��������	
	private ImageView ImageView8 = null;		
	//��ȫ����
	private ImageView ImageView9 = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// ���ر�����
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ����״̬��
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//�������ť
		ImageView1 = (ImageView)findViewById(android.R.id.ImageView1);
		ImageView1.setOnClickListener(new ImageView1Listener());
		//���̹���ť
		ImageView2 = (ImageView)findViewById(R.id.ImageView5);
		ImageView2.setOnClickListener(new ImageView2Listener());
		//��ر�����ť
		ImageView3 = (ImageView)findViewById(R.id.ImageView3);
		ImageView3.setOnClickListener(new ImageView3Listener());
		//ɧ�����ذ�ť
		ImageView4 = (ImageView)findViewById(R.id.ImageView4);
		ImageView4.setOnClickListener(new ImageView4Listener());
		//��ȫɨ�谴ť
		ImageView5 = (ImageView)findViewById(R.id.ImageView5);
		ImageView5.setOnClickListener(new ImageView5Listener());
		//�ֻ�������ť
		ImageView6 = (ImageView)findViewById(R.id.ImageView6);
		ImageView6.setOnClickListener(new ImageView6Listener());
		//������ذ�ť
		ImageView7 = (ImageView)findViewById(R.id.ImageView7);
		ImageView7.setOnClickListener(new ImageView7Listener());
		//�������ǰ�ť
		ImageView8 = (ImageView)findViewById(R.id.ImageView8);
		ImageView8.setOnClickListener(new ImageView8Listener());
		//��ȫ���ݰ�ť
		ImageView9 = (ImageView)findViewById(R.id.ImageView9);
		ImageView9.setOnClickListener(new ImageView9Listener());

		//					BlackListDAO dao = new BlackListDAO(this);
		//					ArrayList<String> strings = dao.getallNo();
		//					for (int i = 0;i<strings.size();i++) {
		//	                    Log.i("main",strings.get(i));    
		//					}




	}

	//�������ť������
	class ImageView1Listener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			//����һ��Intent����
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, AppManagerActivity.class);
			MainActivity.this.startActivity(intent);
		}
	}

	//���̹���ť������
	class ImageView2Listener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			//����һ��Intent����
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, ProManageActivity.class);
			MainActivity.this.startActivity(intent);
		}
	}

	//���ű������ȡ��
	class ImageView3Listener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			//����һ��Intent����
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, BackUpSmsActivity.class);
			MainActivity.this.startActivity(intent);
		}
	}
	//ɧ�����ذ�ť������
	class ImageView4Listener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			//����һ��Intent����
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, BlackListActivity.class);
			MainActivity.this.startActivity(intent);
		}
	}
	//��ȫɨ�谴ť������
	class ImageView5Listener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			//����һ��Intent����
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, SafeScanActivity.class);
			MainActivity.this.startActivity(intent);
		}
	}

	//�ֻ�������ť������
	class ImageView6Listener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			//����һ��Intent����
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, LostProtectActivity.class);
			MainActivity.this.startActivity(intent);
		}
	}

	//������ذ�ť������
	class ImageView7Listener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			//����һ��Intent����
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, NetMonitorActivity.class);
			MainActivity.this.startActivity(intent);
		}
	}

	//�������ǰ�ť������
	class ImageView8Listener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			//����һ��Intent����
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, AboutUsActivity.class);
			MainActivity.this.startActivity(intent);
		}
	}

	//��ȫ���ݰ�ť������
	class ImageView9Listener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			//����һ��Intent����
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, SafeBackupActivity.class);
			MainActivity.this.startActivity(intent);
		}
	}



	//		@Override
	//		public boolean onCreateOptionsMenu(Menu menu) {
	//			MenuInflater mflater=new MenuInflater(this);
	//			mflater.inflate(R.menu.mainactivity_optionmenu_layout,menu);
	//			return super.onCreateOptionsMenu(menu);
	//		}
	//		
	//		//������˵����˳���ť��5��������
	//		class MainActivity_OptionMenu_5 implements OnMenuItemClickListener{
	//			@Override
	//			public boolean onMenuItemClick(MenuItem item){
	//				finish();
	//				return true;
	//			}
	//		}
}
