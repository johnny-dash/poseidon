package cn.poseidon.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

//����ģʽ
public class GPSInfoService {

	private static GPSInfoService mInstance;
	private LocationManager locationManager;//��λ����
	private SharedPreferences sp;
	
	private  GPSInfoService(Context context) {
		// TODO Auto-generated constructor stub
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
	}
	
	public static GPSInfoService getInstance(Context context){
		if(mInstance == null){
			mInstance = new GPSInfoService(context);
		}
		return mInstance;
	}
	
	
	//��λ����
	public void registenerLocationChangeListener(){
		//��ѯ����
		Criteria criteria = new Criteria();
		//��λ�ľ�׼��
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		//������Ϣ�Ƿ��ע
		criteria.setAltitudeRequired(false);
		//����Χ�������Ƿ���й���
		criteria.setBearingRequired(false);
		//�Ƿ�֧���շѵĲ�ѯ
		criteria.setCostAllowed(true);
		//�Ƿ�ĵ�
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		//���ٶ��Ƿ��ע
		criteria.setSpeedRequired(false);
		
		//�õ���õĶ�λ��ʽ��passive��λ���ۺ����еĶ�λ��ʽ���ó���׼ȷ�Ķ�λ��
		String provider = locationManager.getBestProvider(criteria, true);
		
		//ע�������provider����,1����,0m,listener��
		locationManager.requestLocationUpdates(provider, 60000, 0, getListener());
	}
	
	//ȡ������
	public void unRegisterLocationChangeListener(){
		locationManager.removeUpdates(getListener());
	}
	
	private MyLocationListener listener;
	
	//�õ���λ�ļ�����
	private MyLocationListener getListener(){
		if(listener == null){
			listener = new MyLocationListener();
		}
		return listener;
	}
	
	//�õ��ϸ�����λ��
	public String getLastLocation(){
		return sp.getString("last_location", "");
	}
	
	private final class MyLocationListener implements LocationListener{

		//λ�õĸı�
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			double latitude = location.getLatitude();//ά��
			double longitude = location.getLongitude();//����
			
			String last_location = "longitude: " + longitude + ",latitude:" + latitude;
			Log.i("location", last_location);
			Editor editor = sp.edit();
			editor.putString("last_location", last_location);
			editor.commit();
			
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
