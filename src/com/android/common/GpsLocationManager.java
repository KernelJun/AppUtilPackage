package com.android.common;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

import com.android.common.log.AndroidLog;

/**
 * 调用时，注意如下顺序：
 * 1.setContext
 * 2.bootGpsLocation
 * 
 * 其他：
 * addGpsLocationListener
 * removeGpsLocationListener
 * 为了其他对象加入到此监听器中，避免重复造轮子
 * 
 * @author KernelJun
 *
 */
public class GpsLocationManager implements LocationListener {

	private static final String TAG = "GpsLocationManager";
	private static boolean DEBUG = true;

	public interface GpsLocationListener {
		void onLocationChanged(Location location);
	}

	private static GpsLocationManager mInstance;

	private LocationManager mLocationManager;
	private Context mContext;
	private ArrayList<GpsLocationListener> mArrayListListener;
	private boolean GPS_BOOT_STATUS = false;
	private boolean GPS_STATUS = true;

	private GpsLocationManager() {
		mArrayListListener = new ArrayList<GpsLocationListener>();
	}

	public static GpsLocationManager getInstance() {
		if (mInstance == null) {
			mInstance = new GpsLocationManager();
		}
		return mInstance;
	}

	/**
	 * 在bootGpsLocation之前一定要先setContext
	 * @param context
	 */
	public void setContext(Context context) {
		mContext = context.getApplicationContext();
	}

	public void bootGpsLocation() {
		if(mContext == null){
			throw new NullPointerException("context is null");
		}
		if (GPS_BOOT_STATUS) {
			AndroidLog.v(TAG, "boot gps status:" + GPS_BOOT_STATUS);
			return;
		}
		try {
			if (mLocationManager == null) {
				mLocationManager = (LocationManager) mContext
						.getSystemService(Context.LOCATION_SERVICE);
			}
			mLocationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 0, 0, this);
			GPS_BOOT_STATUS = true;
			AndroidLog.v(TAG, "boot success");
		} catch (Exception e) {
			AndroidLog.e(TAG, e);
		}
	}

	public void closeGpsLocation() {
		if (!GPS_BOOT_STATUS) {
			AndroidLog.v(TAG, "close gps status:" + GPS_BOOT_STATUS);
			return;
		}
		if (mLocationManager != null) {
			mLocationManager.removeUpdates(this);
			mLocationManager = null;
		}
	}

	public void addGpsLocationListener(GpsLocationListener listener) {
		if (mArrayListListener != null) {
			mArrayListListener.add(listener);
		}
	}

	public void removeGpsLocationListener(GpsLocationListener listener) {
		if (mArrayListListener != null) {
			mArrayListListener.remove(listener);
		}
	}

	private void notifyAllObject(Location location) {
		if (mArrayListListener != null) {
			for (int i = 0; i < mArrayListListener.size(); i++) {
				mArrayListListener.get(i).onLocationChanged(location);
			}
		}
	}

	public boolean getGpsStatus() {
		return GPS_STATUS;
	}

	@Override
	public void onLocationChanged(Location location) {
		if (DEBUG) {
			DEBUG = false;
			AndroidLog.v(TAG, "onLocationChanged >>> gps OK");
		}
		notifyAllObject(location);
	}

	@Override
	public void onProviderDisabled(String provider) {
		AndroidLog.v(TAG, "onProviderDisabled:" + provider);
		GPS_STATUS = false;
	}

	@Override
	public void onProviderEnabled(String provider) {
		AndroidLog.v(TAG, "onProviderEnabled:" + provider);
		GPS_STATUS = true;
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		AndroidLog.d(TAG, "onStatusChanged:" + provider + ",status:" + status
				+ ",extras:" + extras.toString());
		switch (status) {
		case LocationProvider.OUT_OF_SERVICE:
		case LocationProvider.TEMPORARILY_UNAVAILABLE:
			GPS_STATUS = false;
			break;
		case LocationProvider.AVAILABLE:
			GPS_STATUS = true;
			break;
		}
	}

}
