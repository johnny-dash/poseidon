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
     
     //�ж�ĳһ��Ӧ�ó����ǲ���ϵͳ��Ӧ�ó�������Ƿ���true�����򷵻�false
     public boolean filterApp(ApplicationInfo info)
     {
             //��ЩϵͳӦ���ǿ��Ը��µģ�����û��Լ�������һ��ϵͳ��Ӧ����������ԭ���ģ�
             //������ϵͳӦ�ã���������ж����������
             if((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0)
             {
                     return true;
             }
             else if((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0)//�ж��ǲ���ϵͳӦ��
             {
                     return true;
             }
             return false;
     }
}

