package com.android.online;

import android.content.Context;
import android.net.ConnectivityManager;

public class HttpUtils {

	private static Context mContext;
	private static ConnectivityManager mConnManager;
	
	public static void init(Context context){
		mContext = context.getApplicationContext();
		mConnManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
	}
	
	public static boolean hasNetwork() {
		if (mConnManager.getActiveNetworkInfo() != null) {
			return mConnManager.getActiveNetworkInfo().isAvailable();
		}
		return false;
	}
	
	
	
}
