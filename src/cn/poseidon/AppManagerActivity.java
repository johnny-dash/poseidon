package cn.poseidon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;





import cn.poseidon.domain.Appinfos;
import cn.poseidon.engine.AppInfoProvider;
import cn.poseidon.util.GetStrValue;
import cn.poseidon.util.MyToast;

import com.example.poseidon.R;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.IPackageStatsObserver;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AppManagerActivity extends Activity implements OnItemClickListener, OnItemLongClickListener {
	
	private String TAG = "AppManagerActivity";
	private List<Appinfos> allappinfos;
	private List<Appinfos> sysappinfos;
	private List<Appinfos> userappinfos;
	private List<Appinfos> filterinfos;
	private Context context;
	private int listmode = 0;
	private int sysmode = 0;
	private final int LIST = 1;
	private final int GRID = 0;
	private final int ALL = 0;
	private final int SYS = 1;
	private final int USER = 2;
	private ImageView imv;
	private TextView tv_name;
	private TextView tv_pagname = null;
	private ListView lv;
	private GridView gv;
	private AppManagerAdapter adapter;
	private AppInfoProvider appInfoProvider;
	private ImageButton imb_chg_info;
	private ImageButton imb_chg_list;
	private static final int SWITCH = 0;
	private static final int DELAPP = 1;
	private static final int FIND = 2;
	private ProgressDialog pd;
	private Appinfos delinfo;
	private int icut = 0;
	private TextView tv_title;
	private Button bt_find;
	private EditText et_find;
	private String findStr;
	private SharedPreferences sp;
	private long datasize  = 0;
	private long cachesize = 0;
	private long codesize = 0;
	private AppSizes appSizes;
	private boolean bdone = false;
	private Map<String, AppSizes> maps;
	private Handler handler = new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == SWITCH) {
				
				reflashView();
				pd.dismiss();
				setProgressBarIndeterminateVisibility(false);
			}else if (msg.what == DELAPP) {
				String appname = ((Appinfos)gv.getItemAtPosition(msg.arg1)).getAppname();
				Toast.makeText(AppManagerActivity.this, "Remove"+ appname+"successfull!", 0).show();
				reflashView();
			}else if (msg.what == FIND){
				adapter = new AppManagerAdapter(filterinfos);
				int  iret = filterinfos.size();
				lv.setAdapter(adapter);
				gv.setAdapter(adapter);
				pd.dismiss();
				tv_title.setText("���ҽ��"+",һ��:"+String.valueOf(iret)+"��");
			}
		}

	};
	
	private void reflashView() {
		String strtitle;
		
		if (sysmode == ALL) {
			adapter = new AppManagerAdapter(allappinfos);
			System.out.println("allappinfos"+allappinfos.size());
			strtitle = "��ʾȫ��APP";
			icut = allappinfos.size();
		}else if (sysmode == USER) {
			adapter = new AppManagerAdapter(userappinfos);
			System.out.println("userappinfos"+userappinfos.size());
			strtitle = "��ʾ�û�APP";
			icut = userappinfos.size();
		}else {
			adapter = new AppManagerAdapter(sysappinfos);
			System.out.println("sysappinfos"+sysappinfos.size());
			strtitle = "��ʾϵͳAPP";
			icut = sysappinfos.size();
		}
		
		if (listmode == LIST) {

			lv.setVisibility(View.VISIBLE);
			gv.setVisibility(View.GONE);

		}
		else {

			gv.setVisibility(View.VISIBLE);
			lv.setVisibility(View.GONE);

		}
		lv.setAdapter(adapter);
		gv.setAdapter(adapter);
		tv_title.setText(strtitle+",һ��:"+String.valueOf(icut)+"��");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.app_manager_main);
		maps = new HashMap<String,AppSizes>();
		sp =  getSharedPreferences("config", Context.MODE_PRIVATE);
		imb_chg_info = (ImageButton) findViewById(R.id.imb_change_category);
		imb_chg_list = (ImageButton) findViewById(R.id.imb_change_view);
		tv_title = (TextView) findViewById(R.id.tv_app_title);
		bt_find = (Button) findViewById(R.id.bt_app_find);
		et_find = (EditText) findViewById(R.id.et_app_find);
		bt_find.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				findStr	= et_find.getText().toString().trim();
				if (findStr.length() > 0) {
					pd = ProgressDialog.show(AppManagerActivity.this, "���Ժ�...", "��������������װ��Ӧ�ó���...",true,false);
					new Thread(){

						@Override
						public void run() {
							super.run();
							filterAppInfo(findStr);
							
						}
						
					}.start();
				}else {
					handler.sendEmptyMessage(SWITCH);
					Toast.makeText(AppManagerActivity.this, "����Ϊ��", 0).show();
				}
			}
		});
		
		imb_chg_info.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Message msg = handler.obtainMessage();
				msg.what = SWITCH;
				if (sysmode == ALL) {
					sysmode = SYS;
					MyToast.showToast(AppManagerActivity.this, R.drawable.android_icon, "�л���ϵͳ���ģʽ", 0);
					System.out.println("�л���ϵͳ���ģʽ"+sysmode);
				}else if (sysmode == USER) {
					sysmode = ALL;
					MyToast.showToast(AppManagerActivity.this, R.drawable.android_icon, "�л���ȫ�����ģʽ", 0);
					System.out.println("�л���ȫ�����ģʽ"+sysmode);
				}else if (sysmode == SYS) {
					sysmode = USER;
					MyToast.showToast(AppManagerActivity.this, R.drawable.android_icon, "�л����û����ģʽ", 0);
					System.out.println("�л����û����ģʽ"+sysmode);
				}
				handler.sendMessage(msg);
			}
		});
		
		imb_chg_list.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Message msg = handler.obtainMessage();
				msg.what = SWITCH;
				if(listmode == LIST)
				{
					listmode = GRID;
					MyToast.showToast(AppManagerActivity.this, R.drawable.grids, "�л�������ģʽ��ʾ", 0);
				}else {
					listmode = LIST;
					MyToast.showToast(AppManagerActivity.this, R.drawable.list, "�л����б�ģʽ��ʾ", 0);
				}
				handler.sendMessage(msg);
			}
		});
		gv = (GridView) findViewById(R.id.gv_apps);
		lv = (ListView) findViewById(R.id.lv_apps);
		setProgressBarIndeterminateVisibility(true);
		// ����ʹ��Animation ����һЩ����Ч��
		lv.setCacheColorHint(0);
		gv.setOnItemClickListener(this);
		lv.setOnItemClickListener(this);
		gv.setOnItemLongClickListener(this);
		lv.setOnItemLongClickListener(this);
		lv.setVisibility(View.GONE);
		gv.setVisibility(View.VISIBLE);
		pd = ProgressDialog.show(this, "���Ժ�...", "��������������װ��Ӧ�ó���...",true,false);
		new Thread(){

			@Override
			public void run() {
				super.run();
				initAppInfo();
				
				//appInfoProvider = new AppInfoProvider(AppManagerActivity.this);
				//allappinfos = appInfoProvider.getAllAPP();
				//adapter = new AppManagerAdapter(allappinfos);
				handler.sendEmptyMessage(SWITCH);
			}
			
		}.start();
		
	}


	public class AppManagerAdapter extends BaseAdapter
	{
		private List<Appinfos> appinfos;
		
		public AppManagerAdapter(List<Appinfos> appinfos) {
			this.appinfos = appinfos;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return appinfos.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return appinfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Appinfos appinfo;

			View view = null;
			// ����֮ǰ��convertView �Ż���ʾ�ٶ�
			if (convertView == null) {
				if (listmode == LIST) {
					view = View.inflate(AppManagerActivity.this, R.layout.app_manager_item, null);
				}
				else if (listmode == GRID) {
					 view  = View.inflate(AppManagerActivity.this, R.layout.app_manager_item2, null);	
				}
			}else {
				view = convertView;
			}
			if (listmode == LIST) {
				//view = View.inflate(AppManagerActivity.this, R.layout.lv_item, null);
				  imv  = (ImageView) view.findViewById(R.id.imv_lv_item_icon);
				  tv_name = (TextView) view.findViewById(R.id.tv_lv_item_appname);
				 tv_pagname = (TextView) view.findViewById(R.id.tv_lv_item_packageame);
			}
			else if (listmode == GRID) {
				 //view  = View.inflate(AppManagerActivity.this, R.layout.gv_item, null);	
				  imv  = (ImageView) view.findViewById(R.id.imv_gv_item_icon);
				tv_name = (TextView) view.findViewById(R.id.tv_gv_item_appname);
			}
			
			if (tv_pagname != null) {
				tv_pagname.setText(appinfos.get(position).getPackageName());
			}
			appinfo = appinfos.get(position);
			imv.setImageDrawable(appinfo.getIcon());
			tv_name.setText(appinfo.getAppname());
			
			return  view;
		}
		
		
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {

		final Appinfos appinfo;
		if (listmode == LIST) {
			ListView listView = (ListView)parent;
			 appinfo  = (Appinfos)(listView.getItemAtPosition(position));
		}else {
			GridView gridView = (GridView) parent;
			 appinfo  = (Appinfos)(gridView.getItemAtPosition(position));
		}
		 String pkgname = appinfo.getPackageName();
		final PackageInfo pkginfo;
		PackageInfo info = null;
		try {
			  info  = getPackageManager().getPackageInfo(pkgname, PackageManager.GET_UNINSTALLED_PACKAGES
					| PackageManager.GET_ACTIVITIES);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		pkginfo = info;
		
		AlertDialog.Builder builder = new Builder(this);
		String[] items = new String[]{"����","��ϸ��Ϣ","ж��","����","Ȩ���б�","����APK","����"};
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					if (pkginfo != null) {
						//ĳЩ�����������Զ���Ȩ�� ����������Ȩ�޾ͻᰲȫ�쳣
						//java.lang.SecurityException: Permission Denial
						ActivityInfo actinfo = pkginfo.activities[0];
						if (actinfo != null) {
							Intent intent = new Intent();
							intent.setClassName(pkginfo.packageName, actinfo.name);
							startActivity(intent);
						}else {
							Toast.makeText(AppManagerActivity.this, "��ǰ���򲻴��ڿ�������Activity", 0).show();
						}
					}else {
						Toast.makeText(AppManagerActivity.this, "����δ�ҵ�", 0).show();
					}
					break;
				case 1:
					if (pkginfo != null) {
						AlertDialog.Builder builder = new AlertDialog.Builder(AppManagerActivity.this);
						builder.setTitle("��ϸ��Ϣ");
						StringBuffer strBuf = new StringBuffer();
						AppSizes sizes = maps.get(pkginfo.packageName);
						strBuf.append("��������:"+pkginfo.applicationInfo.loadLabel(getPackageManager()));
						strBuf.append("\n ����:" + pkginfo.packageName);
						strBuf.append("\n �汾��:" + pkginfo.versionCode);
						strBuf.append("\n �汾��:" + pkginfo.versionName);
						if (sizes != null) {
							strBuf.append("\n �����С:" + sizes.appsize);
							strBuf.append("\n ���ݴ�С:" + sizes.datasize);
							strBuf.append("\n �����С:" + sizes.cachesize);
						}
						builder.setMessage(strBuf);
						builder.setIcon(appinfo.getIcon());
						builder.setPositiveButton("ȷ��", null);
						builder.create().show();
						
					}
					break;
				case 2:
					delinfo = appinfo;
					Uri delUri = Uri.parse("package:"+appinfo.getPackageName());
					Intent deliIntent = new Intent();
					deliIntent.setAction(Intent.ACTION_UNINSTALL_PACKAGE);
					deliIntent.setData(delUri);
					startActivityForResult(deliIntent, 0);
					break;
				case 3:
					delinfo = appinfo;
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_SEND);
					intent.setType("text/plain");
					intent.putExtra(Intent.EXTRA_TITLE, "����Ӧ�÷���");
					intent.putExtra(Intent.EXTRA_TEXT, "����");
					intent.putExtra(Intent.EXTRA_SUBJECT, "share this App");
					Intent shareIntent  = Intent.createChooser(intent, "����Ӧ��by����;��");
					if (shareIntent != null) {
						try {
							startActivity(shareIntent);
						}catch (android.content.ActivityNotFoundException ex) {
							Toast.makeText(AppManagerActivity.this, "δ�ҵ���������", 0).show();
						}
					}
					break;
				case 4:
					Intent perIntent = new Intent(AppManagerActivity.this,SercurityPermissionActivity.class);
					if (pkginfo !=null) {
						perIntent.putExtra("packagename", pkginfo.packageName);
					}
					startActivity(perIntent);
					break;
				case 5:
					extractApkFlie(pkginfo.packageName);
					break;
				case 6:
					ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
					Intent appIntent = new Intent();
					appIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
					Uri uri = Uri.fromParts("package", appinfo.getPackageName(), null);
					appIntent.setData(uri);
					startActivity(appIntent);
					break;
					

				default:
					break;
				}
				
			}
		});
		//�������false��ôonItemClick��Ȼ�ᱻ���á��������ȵ���onItemLongClick��Ȼ�����onItemClick�� 
		//�������true��ôclick�ͻᱻ�Ե���onItemClick�Ͳ����ٱ������ˡ�
		builder.create().show();
		return true;
	}
    
	public void getAppSizes(final String pkgname){
		PackageManager pm = getPackageManager();
		try {
			Method method = PackageManager.class.getMethod("getPackageSizeInfo", new Class[]{String.class,IPackageStatsObserver.class});
			method.invoke(pm, new Object[]{pkgname,new Object(){//IPackageStatsObserver.Stub() {
				//��ȡ��С�ĺ����ǻص����������첽ִ�е�
				//@Override
				public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
						throws RemoteException {
					
						codesize = pStats.codeSize;
						cachesize = pStats.cacheSize;
						datasize = pStats.dataSize;
						appSizes = new AppSizes();
						appSizes.appsize = GetStrValue.getValueOf(codesize);
						appSizes.cachesize = GetStrValue.getValueOf(cachesize);
						appSizes.datasize = GetStrValue.getValueOf(datasize);
						//Log.d(TAG, appSizes.appsize + appSizes.datasize+appSizes.cachesize);
						maps.put(pkgname, appSizes);
						appSizes = null;
						
					
				}
			}});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public class AppSizes{
		public String  appsize;
		public String  datasize;
		public String  cachesize;
		
	}
	
	  public static class ApkInfo
	  {
	    public String appName;
	    public String appSize;
	    public Drawable icon;
	    public String pkgName;
	    public long size;
	    public String srcDir;
	    public int versionCode = 0;
	    public String versionName;
	  }
	
	  public static String toFileSizeString(long paramLong)
	  {
	    int i = 0;
	    while (true)
	    {
	      if (paramLong <= 1048576L)
	      {
	        if (paramLong > 1024L)
	          i++;
	        Object[] arrayOfObject = new Object[2];
	        arrayOfObject[0] = Float.valueOf((float)paramLong / 1024.0F);
	        arrayOfObject[1] = Character.valueOf(" KMGTPE".charAt(i));
	        return String.format("%.1f %cB", arrayOfObject);
	      }
	      i++;
	      paramLong >>= 10;
	    }
	  }
	  
	  public static String getFileName(String paramString)
	  {
	    int i = paramString.lastIndexOf("/");
	    if (i != -1)
	      paramString = paramString.substring(i + 1, paramString.length());
	    return paramString;
	  }
	  
	  public  void copyFile(File paramFile, String paramString) throws Exception
			  {
			    String str = getFileName(paramFile.getAbsolutePath());
			    
			    File dir = new File(paramString);
			    if (!dir.exists())
			    {
			      boolean bok = dir.mkdirs();
			      if (!bok) {
					Toast.makeText(AppManagerActivity.this, "����Ŀ¼ʧ��", 0).show();
				}
			    }
			    File outFile = new File(paramString, str);
			    outFile.createNewFile();
			    FileInputStream fIs = new FileInputStream(paramFile);
			    FileOutputStream fOs = new FileOutputStream(outFile);
			    byte[] arrayOfByte = new byte[4096];
			    
			    while (true)
			    {
			      int i = fIs.read(arrayOfByte);
			      if (i<=0) {
				        fOs.flush();
				        fIs.close();
				        fOs.close();
					break;
				}
			      fOs.write(arrayOfByte, 0, i);
			      fOs.flush();
			    }
					

			  }
	  
	protected void extractApkFlie(String packageName) {
		
		try {
			PackageInfo apkpkginfo = getPackageManager().getPackageInfo(packageName, 0);
			ApkInfo apkInfo = new ApkInfo();
			
			
			apkInfo.appName = apkpkginfo.applicationInfo.loadLabel(getPackageManager()).toString();
			apkInfo.pkgName = apkpkginfo.packageName;
			apkInfo.versionName = apkpkginfo.versionName;
			apkInfo.versionCode = apkpkginfo.versionCode;
			apkInfo.icon = apkpkginfo.applicationInfo.loadIcon(getPackageManager());
			apkInfo.srcDir = apkpkginfo.applicationInfo.sourceDir;
			apkInfo.size = new File(apkpkginfo.applicationInfo.sourceDir).length();
			apkInfo.appSize = toFileSizeString(apkInfo.size);
			String outPath = Environment.getExternalStorageDirectory().toString()+"/appks/";
			copyFile(new File(apkInfo.srcDir), outPath);
			Toast.makeText(this, "����Ŀ¼:"+outPath, 0).show();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		
		final Appinfos appinfo;
		final PackageInfo pkginfo;
		PackageInfo info = getPkginfo(parent, position);
		pkginfo = info;
		
		if (pkginfo != null) {
			ActivityInfo actinfo = pkginfo.activities[0];
			if (actinfo != null) {
				Intent intent = new Intent();
				//���µ�����ջ������Activity
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setClassName(pkginfo.packageName, actinfo.name);
				startActivity(intent);
			}else {
				Toast.makeText(AppManagerActivity.this, "��ǰ���򲻴��ڿ�������Activity", 0).show();
			}
		}else {
			Toast.makeText(AppManagerActivity.this, "����δ�ҵ�", 0).show();
		}
		
	}

	private PackageInfo getPkginfo(AdapterView<?> parent, int position) {
		final Appinfos appinfo;
		if (listmode == LIST) {
			ListView listView = (ListView)parent;
			 appinfo  = (Appinfos)(listView.getItemAtPosition(position));
		}else {
			GridView gridView = (GridView) parent;
			 appinfo  = (Appinfos)(gridView.getItemAtPosition(position));
		}
		 String pkgname = appinfo.getPackageName();

		PackageInfo info = null;
		try {
			  info  = getPackageManager().getPackageInfo(pkgname, PackageManager.GET_UNINSTALLED_PACKAGES
					| PackageManager.GET_ACTIVITIES);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return info;
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		initAppInfo();//���³�ʼ��
		// �Ƴ�ɾ�������
		//allappinfos.remove(delinfo);
		//userappinfos.remove(delinfo);
		handler.sendEmptyMessage(DELAPP);
		
	}


	public void filterAppInfo(String filter)
	{
		filterinfos = new ArrayList<Appinfos>();
		for (Appinfos info  :allappinfos ) {
			//ȫ��ʹ��Сд�������Сд��һ�µ��µ��������Ϊ��
			if (info.getAppname().trim().toLowerCase().contains(filter.toLowerCase().trim())) {
				
				filterinfos.add(info);
			}
			
		}
		handler.sendEmptyMessage(FIND);
		
	}
	// ��ʱδȥʵ�� ������������ʾ ֻ�Ǳ���������
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//��ʶ�������ظ�ʹ��  
		// ���������� groupId(����Ϊ0) ��ʶ��  ˳�� �� ����
		SubMenu subMenu = menu.addSubMenu(0, 1, 1, "ѡ����ʾģʽ");
		subMenu.add(0, 11, 1, "�û��������");//�Ӳ˵���id���ܺ��ϲ�˵�ʹ�õ�idһ�·�����Ч���͵����Ӧid���ϼ��˵�Ч��һ��
		subMenu.add(0, 12, 1, "ϵͳ�������");
		subMenu.add(0, 13, 1, "������ʾģʽ");
		//menu.setGroupVisible(1, false);//����ĳ����Ĳ˵�
		//menu.setGroupCheckable(1, false, false);
		//menu.setGroupEnabled(1, false);//����
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
			Toast.makeText(AppManagerActivity.this, "id = "+item.getItemId(), 0).show();
			Editor editor = sp.edit();
			editor.putInt("appmanagermode", 0);
		return super.onContextItemSelected(item);
	}

	private void initAppInfo() {
		//PackageManager.GET_ACTIVITIES ��������ʼ�� packageinfo�����activity�����
		List<PackageInfo> allpkgInfos = getPackageManager().getInstalledPackages(
	           PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_ACTIVITIES);
		userappinfos = new ArrayList<Appinfos>();
		sysappinfos = new ArrayList<Appinfos>();
		allappinfos = new ArrayList<Appinfos>();
		//PackageManager packageManager;
		//packageManager =context.getPackageManager();
		
		try {
			for (PackageInfo packageInfo : allpkgInfos) {
				//getAppSizes(packageInfo.packageName);
				Appinfos temp = new Appinfos();
				//ApplicationInfo applicationInfo = packageManager.getPackageInfo(packageInfo.packageName, 0).applicationInfo;
				
				ApplicationInfo applicationInfo =  packageInfo.applicationInfo;
				temp.setAppname(applicationInfo.loadLabel(getPackageManager()).toString());
				temp.setPackageName(packageInfo.packageName);
				temp.setIcon(applicationInfo.loadIcon(getPackageManager()));
				
				if((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
					userappinfos.add(temp);
					temp.setSysApp(true);
				} else if((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
					userappinfos.add(temp);
					temp.setSysApp(true);
				}else {
					sysappinfos.add(temp);
					temp.setSysApp(false);
				}
				allappinfos.add(temp);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
			
			//allpkgInfos.remove(temp);
		
		
	}

	
	

}
