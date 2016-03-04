package cn.poseidon.receiver;

import com.example.poseidon.R;

import cn.poseidon.service.GPSInfoService;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsRecevier extends BroadcastReceiver {

	private SharedPreferences sp;
	private DevicePolicyManager devicePolicyManager;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		Log.i("i", "�Ѿ����ص��˶���");
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		//�жϱ����Ƿ���
		boolean isprotected = sp.getBoolean("isprotected", false);
		if(isprotected){
			devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
			Object[] pdus = (Object[]) intent.getExtras().get("pdus");
			for(Object pdu:pdus){
				//�õ���������
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[])pdu);
				String body = smsMessage.getDisplayMessageBody();
				Log.i("�������ݣ�", body);				
				//�õ�ʵ���趨�İ�ȫ����
				String safe_number = sp.getString("safe_number", "");	
				Log.i("��ȫ���룺", safe_number);
				if("#*location*#".equals(body)){					
					//��ȡ�ֻ���λ��
					//��λ�÷��͸���ȫ����
					//�жϹ㲥
					GPSInfoService service = GPSInfoService.getInstance(context);
					service.registenerLocationChangeListener();
					String last_location = service.getLastLocation();
					
					SmsManager smsManager = SmsManager.getDefault();
					//�����ݷ��͸�safe_number
					smsManager.sendTextMessage(safe_number, null, "location:" + last_location, null, null);
					
					abortBroadcast();
				}
				else if("#*lockscreen*#".equals(body)){
					//����
					devicePolicyManager.lockNow();
					//��������
					devicePolicyManager.resetPassword("20111942", 0);
					abortBroadcast();
				}
				else if("#*delete*#".equals(body)){
					//�ָ���������
					devicePolicyManager.wipeData(0);
					abortBroadcast();
				}
				else if("#*alarm*#".equals(body)){
					//������������
					MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.alarm);
					mediaPlayer.setVolume(1.0f, 1.0f);//����Ƶ����Ƶȫ������Ϊ�������
					mediaPlayer.start();
					abortBroadcast();
				}
			}
		}
	}

}
