package cn.poseidon;

import com.example.poseidon.R;

import cn.poseidon.domain.Appinfos;
import cn.poseidon.domain.ProcessInfo;
import cn.poseidon.engine.TaskInfoProvider;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.security.PublicKey;
import java.util.List;
import java.util.ArrayList;



public class ProManageActivity extends Activity{
    private final static int LOAD_FINISH= 1;
	private ListView ls_process_list;
	private LinearLayout ll_pmm_process;
	private ActivityManager activityManager;
    
	private List<ProcessInfo> processInfos;
	private List<RunningAppProcessInfo> runningAppProcessInfos;
	private TaskInfoAdapter adapter;
	private TaskInfoProvider taskInfoProvider;
	
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOAD_FINISH:
				     ls_process_list.setVisibility(View.VISIBLE);
				     //ll_pmm_process.setVisibility(View.INVISIBLE);
				     adapter = new TaskInfoAdapter();
				     ls_process_list.setAdapter(adapter);
				break;

			default:
				break;
			}
		}
	};
	
	protected void onCreate(Bundle savedInstanceState) {
		// Òþ²Ø±êÌâÀ¸
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    // Òþ²Ø×´Ì¬À¸
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	    WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    
	    activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
	    
		super.onCreate(savedInstanceState);
		setContentView(R.layout.process_manager_main);
		
		ls_process_list = (ListView)findViewById(R.id.lv_process_list);
		
		initData();
	}
	
	private void initData() {
		ls_process_list.setVisibility(View.INVISIBLE);
		runningAppProcessInfos = activityManager.getRunningAppProcesses();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				taskInfoProvider = new TaskInfoProvider(ProManageActivity.this);
				processInfos = taskInfoProvider.getAllTask(runningAppProcessInfos);
				
				//ls_process_list.setVisibility(View.INVISIBLE);
				
				Message msg = new Message();
				msg.what=LOAD_FINISH;
				handler.sendMessage(msg);
			}
		}).start();
	}
	
	private class TaskInfoAdapter extends BaseAdapter{
		private List<ProcessInfo> taskInfo;
		
		public TaskInfoAdapter() {
			taskInfo = new ArrayList<ProcessInfo>(); 
						
			for(ProcessInfo processInfo:processInfos)
			{
				taskInfo.add(processInfo);
			}
		}
		

	
	@Override	
	public int getCount() {
		return processInfos.size();
	}
	
	@Override
	public Object getItem(int position) {
		return processInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ProcessInfo processInfo;
		
		if (convertView == null) {
			view = View.inflate(ProManageActivity.this,
					R.layout.process_manager_item, null);
		} else {
			view = convertView;
		}
		processInfo = taskInfo.get(position);
		
		ImageView iView= (ImageView)view.findViewById(R.id.imv_pmi_icon);
		TextView nameTextView=(TextView)view.findViewById(R.id.tv_pmi_appname);
		TextView memary =(TextView)view.findViewById(R.id.tv_pmi_memory);
		
		iView.setImageDrawable(processInfo.getIcon());
		nameTextView.setText(processInfo.getPkgname());
		memary.setText("Õ¼ÓÃÄÚ´æ£º"+processInfo.getmemSize()+"KB");
		
		// info = processInfos. 
		return view;
	}
	}
	

}
