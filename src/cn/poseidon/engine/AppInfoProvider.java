package cn.poseidon.engine;

import cn.poseidon.domain.Appinfos;

import java.util.List;
import java.util.ArrayList;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;


public class AppInfoProvider {
	private PackageManager packageManager;
	
     public AppInfoProvider(Context context){
         packageManager = context.getPackageManager();
         
     }
     
     public List<Appinfos> getAllAPP(){
    	List<Appinfos> Applist = new ArrayList<Appinfos>();
    	Appinfos myAppinfo;
    	List<PackageInfo> packageInfos =packageManager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
    	for(PackageInfo info:packageInfos)
    	{
    		myAppinfo = new Appinfos();
    		String packageName = info.packageName;
    		ApplicationInfo appinfo= info.applicationInfo;
    		Drawable icon = appinfo.loadIcon(packageManager);
    		String appname = appinfo.loadLabel(packageManager).toString();
    		
    		myAppinfo.setAppname(appname);
    		myAppinfo.setIcon(icon);
    		myAppinfo.setPackageName(packageName);
    		if (filterApp(appinfo)) {
				myAppinfo.setSysApp(true);
			}
    		else {
				myAppinfo.setSysApp(false);
			}
    		Applist.add(myAppinfo);
    	}
    	return Applist;
     }
     
     //判断某一个应用程序是不是系统的应用程序，如果是返回true，否则返回false
     public boolean filterApp(ApplicationInfo info)
     {
             //有些系统应用是可以更新的，如果用户自己下载了一个系统的应用来更新了原来的，
             //它还是系统应用，这个就是判断这种情况的
             if((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0)
             {
                     return true;
             }
             else if((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0)//判断是不是系统应用
             {
                     return true;
             }
             return false;
     }
}

