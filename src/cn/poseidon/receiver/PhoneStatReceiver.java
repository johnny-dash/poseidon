package cn.poseidon.receiver;

import java.lang.reflect.Method;

import cn.poseidon.BlackListActivity;
import cn.poseidon.dao.BlackListDAO;

import com.android.internal.telephony.ITelephony;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;


public class PhoneStatReceiver extends BroadcastReceiver {
	Context context;
	private BlackListDAO dao = new BlackListDAO(context);
	

    String TAG = "tag";  
    TelephonyManager telMgr;
    
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
        telMgr = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);  
        switch (telMgr.getCallState()) {  
                //来电
            case TelephonyManager.CALL_STATE_RINGING:  
            	//得到当前来电号码
                String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                Log.i(TAG,"来电号码:"+number); 
                if (dao == null) {
                    Log.i(TAG,"dao为空");                 	
                }
                else {
                    Log.i(TAG,"dao非空");                   	
                }
                
                if (dao.getallNo() == null) {
                    Log.i(TAG,"dao.getallNo()为空");                 	
                }
                else {
                    Log.i(TAG,"dao.getallNo()非空");                   	
                }  
                
                //检测当前联系人中是否存在该号码                
                if (dao.getallNo().contains(number)) {  
                    endCall();  
                }  
                break;  
                //挂断
            case TelephonyManager.CALL_STATE_OFFHOOK:                                 
                break;  
                //通话中
            case TelephonyManager.CALL_STATE_IDLE:                                 
                break;  
        }  
	}
    /** 
     * 挂断电话 
     */  
    private void endCall()  
    {  
        Class<TelephonyManager> c = TelephonyManager.class;           
        try  
        {              
        	Log.i(TAG, "调用endCall()."); 
            Method getITelephonyMethod = c.getDeclaredMethod("getITelephony", (Class[]) null);  
            getITelephonyMethod.setAccessible(true);  
            ITelephony iTelephony = null;   
            iTelephony = (ITelephony) getITelephonyMethod.invoke(telMgr, (Object[]) null);  
        	Log.i(TAG, "调用iTelephony.endCall()."); 
            iTelephony.endCall();             
        }  
        catch (Exception e)  
        {  
            Log.i(TAG, "Fail to answer ring call.");  
        }          
    }   
}
