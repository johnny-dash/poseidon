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
	//软件管理
	private ImageView ImageView1 = null;
	//进程管理
	private ImageView ImageView2 = null;
	//电池保养
	private ImageView ImageView3 = null;
	//骚扰拦截
	private ImageView ImageView4 = null;
	//安全扫描
	private ImageView ImageView5 = null;
	//手机防盗
	private ImageView ImageView6 = null;	
	//流量监控
	private ImageView ImageView7 = null;	
	//关于我们	
	private ImageView ImageView8 = null;		
	//安全备份
	private ImageView ImageView9 = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// 隐藏标题栏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 隐藏状态栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//软件管理按钮
		ImageView1 = (ImageView)findViewById(android.R.id.ImageView1);
		ImageView1.setOnClickListener(new ImageView1Listener());
		//进程管理按钮
		ImageView2 = (ImageView)findViewById(R.id.ImageView5);
		ImageView2.setOnClickListener(new ImageView2Listener());
		//电池保养按钮
		ImageView3 = (ImageView)findViewById(R.id.ImageView3);
		ImageView3.setOnClickListener(new ImageView3Listener());
		//骚扰拦截按钮
		ImageView4 = (ImageView)findViewById(R.id.ImageView4);
		ImageView4.setOnClickListener(new ImageView4Listener());
		//安全扫描按钮
		ImageView5 = (ImageView)findViewById(R.id.ImageView5);
		ImageView5.setOnClickListener(new ImageView5Listener());
		//手机防盗按钮
		ImageView6 = (ImageView)findViewById(R.id.ImageView6);
		ImageView6.setOnClickListener(new ImageView6Listener());
		//流量监控按钮
		ImageView7 = (ImageView)findViewById(R.id.ImageView7);
		ImageView7.setOnClickListener(new ImageView7Listener());
		//关于我们按钮
		ImageView8 = (ImageView)findViewById(R.id.ImageView8);
		ImageView8.setOnClickListener(new ImageView8Listener());
		//安全备份按钮
		ImageView9 = (ImageView)findViewById(R.id.ImageView9);
		ImageView9.setOnClickListener(new ImageView9Listener());

		//					BlackListDAO dao = new BlackListDAO(this);
		//					ArrayList<String> strings = dao.getallNo();
		//					for (int i = 0;i<strings.size();i++) {
		//	                    Log.i("main",strings.get(i));    
		//					}




	}

	//软件管理按钮监听器
	class ImageView1Listener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			//生成一个Intent对象
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, AppManagerActivity.class);
			MainActivity.this.startActivity(intent);
		}
	}

	//进程管理按钮监听器
	class ImageView2Listener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			//生成一个Intent对象
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, ProManageActivity.class);
			MainActivity.this.startActivity(intent);
		}
	}

	//短信备份与读取　
	class ImageView3Listener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			//生成一个Intent对象
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, BackUpSmsActivity.class);
			MainActivity.this.startActivity(intent);
		}
	}
	//骚扰拦截按钮监听器
	class ImageView4Listener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			//生成一个Intent对象
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, BlackListActivity.class);
			MainActivity.this.startActivity(intent);
		}
	}
	//安全扫描按钮监听器
	class ImageView5Listener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			//生成一个Intent对象
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, SafeScanActivity.class);
			MainActivity.this.startActivity(intent);
		}
	}

	//手机防盗按钮监听器
	class ImageView6Listener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			//生成一个Intent对象
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, LostProtectActivity.class);
			MainActivity.this.startActivity(intent);
		}
	}

	//流量监控按钮监听器
	class ImageView7Listener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			//生成一个Intent对象
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, NetMonitorActivity.class);
			MainActivity.this.startActivity(intent);
		}
	}

	//关于我们按钮监听器
	class ImageView8Listener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			//生成一个Intent对象
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, AboutUsActivity.class);
			MainActivity.this.startActivity(intent);
		}
	}

	//安全备份按钮监听器
	class ImageView9Listener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			//生成一个Intent对象
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
	//		//主程序菜单项退出按钮（5）监听器
	//		class MainActivity_OptionMenu_5 implements OnMenuItemClickListener{
	//			@Override
	//			public boolean onMenuItemClick(MenuItem item){
	//				finish();
	//				return true;
	//			}
	//		}
}
