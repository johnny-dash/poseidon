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
                //����
            case TelephonyManager.CALL_STATE_RINGING:  
            	//�õ���ǰ�������
                String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                Log.i(TAG,"�������:"+number); 
                if (dao == null) {
                    Log.i(TAG,"daoΪ��");                 	
                }
                else {
                    Log.i(TAG,"dao�ǿ�");                   	
                }
                
                if (dao.getallNo() == null) {
                    Log.i(TAG,"dao.getallNo()Ϊ��");                 	
                }
                else {
                    Log.i(TAG,"dao.getallNo()�ǿ�");                   	
                }  
                
                //��⵱ǰ��ϵ�����Ƿ���ڸú���                
                if (dao.getallNo().contains(number)) {  
                    endCall();  
                }  
                break;  
                //�Ҷ�
            case TelephonyManager.CALL_STATE_OFFHOOK:                                 
                break;  
                //ͨ����
            case TelephonyManager.CALL_STATE_IDLE:                                 
                break;  
        }  
	}
    /** 
     * �Ҷϵ绰 
     */  
    private void endCall()  
    {  
        Class<TelephonyManager> c = TelephonyManager.class;           
        try  
        {              
        	Log.i(TAG, "����endCall()."); 
            Method getITelephonyMethod = c.getDeclaredMethod("getITelephony", (Class[]) null);  
            getITelephonyMethod.setAccessible(true);  
            ITelephony iTelephony = null;   
            iTelephony = (ITelephony) getITelephonyMethod.invoke(telMgr, (Object[]) null);  
        	Log.i(TAG, "����iTelephony.endCall()."); 
            iTelephony.endCall();             
        }  
        catch (Exception e)  
        {  
            Log.i(TAG, "Fail to answer ring call.");  
        }          
    }   
}
