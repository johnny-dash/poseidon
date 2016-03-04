package cn.poseidon;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.poseidon.util.MD5Encoder;

import com.example.poseidon.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;


public class SafeScanActivity extends Activity {
    protected static final int SCANING = 0;
	protected static final int FINISH_SCAN = 1;
	private ImageView iv_scan;
	private ScrollView scrollview;
	private ProgressBar pb;
	private Button bt_kill;
	private LinearLayout ll_info;
	private AnimationDrawable ad;
	private List<String> allvirus;
	private List<String> existvirus;
	private File file;
	private PackageManager pm;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SCANING:
				View view = (View) msg.obj;
				ll_info.addView(view);
				scrollview.scrollBy(0, 60);//
				break;
			case FINISH_SCAN:
				int size = (Integer) msg.obj;
				TextView tv = new TextView(getApplicationContext());
				if(existvirus.size() > 0){
					tv.setText("扫描了" + size + "个应用程序,发现了" +  existvirus.size() + "个病毒");
					bt_kill.setEnabled(true);
				}else{
					tv.setText("扫描了" + size + "个应用程序,发现了" +  existvirus.size() + "个病毒");
				}
				ll_info.addView(tv);
				ad.stop();
				break;
			default:
				break;
			}
		};
	};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.safescan);
        
        iv_scan = (ImageView) findViewById(R.id.iv_scan);
        scrollview = (ScrollView) findViewById(R.id.scrollview);
        pb = (ProgressBar) findViewById(R.id.pb);
        ll_info = (LinearLayout) findViewById(R.id.ll_info);
        bt_kill = (Button) findViewById(R.id.bt_kill);
        pm = getPackageManager();
        
        //读病毒库
        try {
        	file = new File(getFilesDir(), "antivirus.db");
			InputStream is = getAssets().open("antivirus.db");
			FileOutputStream fos = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int len = 0;
			while((len = is.read(buffer)) != -1){
				fos.write(buffer, 0, len);
			}
			is.close();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        allvirus = new ArrayList<String>();
        existvirus = new ArrayList<String>();
    }
    
    public void kill_virus(View v){
    	ad = (AnimationDrawable) iv_scan.getBackground();
    	ad.start();
    	
    	//杀毒
    	new Thread(){
    		public void run() {
    			
    			Message msg = new Message();
    			msg.what = SCANING;//开始扫描
    			TextView tv = new TextView(getApplicationContext());
    			tv.setText("开始扫描...");
    			msg.obj = tv;
    			mHandler.sendMessage(msg);
    			
    			//得到病毒库信息
    			SQLiteDatabase db = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
    			if(db.isOpen()){
    				Cursor c = db.query("datable", new String[]{"md5"}, null, null, null, null, null);
    				while(c.moveToNext()){
    					String md5 = c.getString(0);
    					allvirus.add(md5);
    				}
    				c.close();
    				db.close();
    			}
    			
    			//得到所有的安装程序
    			List<PackageInfo> packageInfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES|PackageManager.GET_SIGNATURES);
    			pb.setMax(packageInfos.size());//progress_bar设置
    			for(PackageInfo packageInfo:packageInfos){
    				String packageName = packageInfo.packageName;//得到包名
    				
    				ApplicationInfo appInfo = packageInfo.applicationInfo;
    				Drawable icon = appInfo.loadIcon(pm);
    				String name = appInfo.loadLabel(pm).toString();
    				
    				//设置线性布局
    				LinearLayout ll = new LinearLayout(getApplicationContext());
    				ll.setOrientation(LinearLayout.HORIZONTAL);
    				ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    				ll.setLayoutParams(params);
    				ImageView iv = new ImageView(getApplicationContext());
    				params = new ViewGroup.LayoutParams(25, 25);
    				iv.setLayoutParams(params);//图标尺寸设置
    				iv.setImageDrawable(icon);
    				ll.addView(iv);
    				TextView tv1 = new TextView(getApplicationContext());
    				params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    				tv1.setLayoutParams(params);
    				tv1.setText(name);
    				ll.addView(tv1);
    				
    				Message msg2 = new Message();
    				msg2.what = SCANING;
    				msg2.obj = ll;
    				mHandler.sendMessage(msg2);
    				
    				Signature[] signatures = packageInfo.signatures;//得到签名
    				StringBuilder sb = new StringBuilder();
    				for(Signature signature:signatures){
    					sb.append(signature.toCharsString());
    				}
    				
    				//MD5加密
    				String signature = MD5Encoder.getData(sb.toString());
    				if(allvirus.contains(signature)){
    					existvirus.add(packageName);
    				}
    				pb.incrementProgressBy(1);//progress_bar每次加1
    				SystemClock.sleep(200);//设置扫描时间
    			}
    			
    			//扫描完毕，发送消息
    			Message msg3 = new Message();
    			msg3.what = FINISH_SCAN;
    			msg3.obj = packageInfos.size();
    			mHandler.sendMessage(msg3);
    			
    		};
    	}.start();
    }
 
    
    public void kill(View v){
    	for(String packageName:existvirus){
    		Intent intent = new Intent();
    		intent.setAction(Intent.ACTION_DELETE);
    		intent.setData(Uri.parse("package:" + packageName));
    		startActivity(intent);
    	}
    }
}