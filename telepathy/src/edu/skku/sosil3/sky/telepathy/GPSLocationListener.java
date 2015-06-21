package edu.skku.sosil3.sky.telepathy;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class GPSLocationListener  implements LocationListener {
	double latitude,longitude; // 위도, 경도
	
	Location location;
	LocationManager locationManager;
	
	boolean GPS_Enabled; // gps 사용 가능 여부
	boolean Network_Enabled; // network 상태
	
	private static final float UPDATE_DISTANCE_INTERVAL = 5.0f; // 최소 GPS 정보 업데이트 거리(m). 거리가 이만큼 변하면 업데이트
	private static final long UPDATE_TIME_INTERVAL = 1000 * 30; // 최소 GPS 정보 업데이트 시간(ms). 시간이 이만큼 지나면 업데이트
	
	protected static final double DATA_GET_FAILED = 1000000.0;
	
	public GPSLocationListener(LocationManager locationManager) {
		this.locationManager=locationManager;
        getLocation(); // class가 construct되면 바로 위치 받기 시작
    }
	
	public Location getLocation() { // 위치 정보 받기 시작
		// gps로 받아오기
		GPS_Enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		
		if (GPS_Enabled) { // gps 연결 가능
			
			Log.v("comment","GPS enabled");
			
		    locationManager.requestLocationUpdates(
		            LocationManager.NETWORK_PROVIDER, UPDATE_TIME_INTERVAL, UPDATE_DISTANCE_INTERVAL, this);
		    
		    if (locationManager != null) {
		    	
		    	Log.v("comment","locationManager updated");
		    	
		        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		        
		        if (location != null) { // location이 확인되었으면
		        	Log.v("comment","location updated");
		            latitude = location.getLatitude(); // 위도 get 
		            longitude = location.getLongitude(); // 경도 get
		        }
		    }
		    
		    return location;
		}
		
		// 네트워크로 받아오기 (gps로 받아오기 실패했으면)
		Network_Enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			
	    if (Network_Enabled) { // 네트워크 연결 가능
	    	
			Log.v("comment","Network enabled");
			
	        locationManager.requestLocationUpdates(
	                LocationManager.GPS_PROVIDER, UPDATE_TIME_INTERVAL, UPDATE_DISTANCE_INTERVAL, this);
	        
	        if (locationManager != null) {
	        	
		    	Log.v("comment","locationManager updated");
		    	
	            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	            
	            if (location != null) { // location이 확인되었으면
		        	Log.v("comment","location updated");
	                latitude = location.getLatitude(); // 위도 get
	                longitude = location.getLongitude(); // 경도 get
	            }
	        }
	        
	        return location;
	    }
	    
	    return null;
	}
	
	public boolean checkGPSEnable() {
		GPS_Enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		return GPS_Enabled;
	}
	
	public boolean checkNetworkEnable() {
		Network_Enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		return Network_Enabled;
	}
	
	public double getLatitude() { // 위도 데이터 받기
		if (location!=null) {
			latitude = location.getLatitude();
			return latitude;
		}
		else return DATA_GET_FAILED;
	}
	
	public double getLongitude() { // 경도 데이터 받기
		if (location!=null) {
			longitude = location.getLongitude();
			return longitude;
		}
		else return DATA_GET_FAILED;
	}
	
	public void stopGettingLocation() { // 위치 정보를 받는 것을 중단하기. 안하면 계속 받아옴
		if (locationManager!=null)
			locationManager.removeUpdates(this);
	}

	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}
}
