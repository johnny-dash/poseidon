package cn.poseidon.engine;

import cn.poseidon.domain.ProcessInfo;
import android.R.string;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;

import java.util.List;
import java.util.ArrayList;

public class TaskInfoProvider {
      private PackageManager packageManager;
      private ActivityManager activityManager;
      
      public TaskInfoProvider(Context context){
    	    packageManager = context.getPackageManager();
            activityManager = (ActivityManager) context
                            .getSystemService(Context.ACTIVITY_SERVICE);
      }
      
      public List<ProcessInfo> getAllTask(List<RunningAppProcessInfo> runningAppProcessInfos) {
		   List<ProcessInfo> processInfos = new ArrayList<ProcessInfo>();
		   //List<PackageInfo> packinfos = packageManager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		   for(RunningAppProcessInfo runningAppProcessInfo:runningAppProcessInfos)
		   {
			   ProcessInfo processInfo = new ProcessInfo();
			   int id =runningAppProcessInfo.pid;
			   processInfo.setPid(id);
			   String packageName = runningAppProcessInfo.processName;
			   processInfo.setPkgname(packageName);
			   try {
				ApplicationInfo applicationInfo = packageManager.getPackageInfo(packageName, 0).applicationInfo;
				Drawable icon = applicationInfo.loadIcon(packageManager);
				processInfo.setIcon(icon);
				String name=applicationInfo.loadLabel(packageManager).toString();
				processInfo.setAppname(name);
				
				MemoryInfo[] memoryInfos= activityManager.getProcessMemoryInfo(new int[] {id});  
				
                int memory = memoryInfos[0].getTotalPrivateDirty();
                processInfo.setmemSize(memory);
                processInfos.add(processInfo);
                processInfo = null;
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		   }
		   return processInfos;
	}
}
