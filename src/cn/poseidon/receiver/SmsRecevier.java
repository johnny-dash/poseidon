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

		Log.i("i", "已经拦截到了短信");
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		//判断保护是否开启
		boolean isprotected = sp.getBoolean("isprotected", false);
		if(isprotected){
			devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
			Object[] pdus = (Object[]) intent.getExtras().get("pdus");
			for(Object pdu:pdus){
				//得到短信内容
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[])pdu);
				String body = smsMessage.getDisplayMessageBody();
				Log.i("短信内容：", body);				
				//得到实现设定的安全号码
				String safe_number = sp.getString("safe_number", "");	
				Log.i("安全号码：", safe_number);
				if("#*location*#".equals(body)){					
					//获取手机的位置
					//把位置发送给安全号码
					//中断广播
					GPSInfoService service = GPSInfoService.getInstance(context);
					service.registenerLocationChangeListener();
					String last_location = service.getLastLocation();
					
					SmsManager smsManager = SmsManager.getDefault();
					//将内容发送给safe_number
					smsManager.sendTextMessage(safe_number, null, "location:" + last_location, null, null);
					
					abortBroadcast();
				}
				else if("#*lockscreen*#".equals(body)){
					//锁屏
					devicePolicyManager.lockNow();
					//重设密码
					devicePolicyManager.resetPassword("20111942", 0);
					abortBroadcast();
				}
				else if("#*delete*#".equals(body)){
					//恢复出厂设置
					devicePolicyManager.wipeData(0);
					abortBroadcast();
				}
				else if("#*alarm*#".equals(body)){
					//发出报警音乐
					MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.alarm);
					mediaPlayer.setVolume(1.0f, 1.0f);//左音频右音频全都设置为最大音量
					mediaPlayer.start();
					abortBroadcast();
				}
			}
		}
	}

}
