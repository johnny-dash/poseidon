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

//单例模式
public class GPSInfoService {

	private static GPSInfoService mInstance;
	private LocationManager locationManager;//定位服务
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
	
	
	//定位监听
	public void registenerLocationChangeListener(){
		//查询条件
		Criteria criteria = new Criteria();
		//定位的精准度
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		//海拔信息是否关注
		criteria.setAltitudeRequired(false);
		//对周围的事情是否进行关心
		criteria.setBearingRequired(false);
		//是否支持收费的查询
		criteria.setCostAllowed(true);
		//是否耗电
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		//对速度是否关注
		criteria.setSpeedRequired(false);
		
		//得到最好的定位方式（passive定位：综合所有的定位方式，得出最准确的定位）
		String provider = locationManager.getBestProvider(criteria, true);
		
		//注册监听（provider名称,1分钟,0m,listener）
		locationManager.requestLocationUpdates(provider, 60000, 0, getListener());
	}
	
	//取消监听
	public void unRegisterLocationChangeListener(){
		locationManager.removeUpdates(getListener());
	}
	
	private MyLocationListener listener;
	
	//得到定位的监听器
	private MyLocationListener getListener(){
		if(listener == null){
			listener = new MyLocationListener();
		}
		return listener;
	}
	
	//得到上个地理位置
	public String getLastLocation(){
		return sp.getString("last_location", "");
	}
	
	private final class MyLocationListener implements LocationListener{

		//位置的改变
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			double latitude = location.getLatitude();//维度
			double longitude = location.getLongitude();//经度
			
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
