package com.android.common;

import com.android.common.log.ApplicationCrashHandler;
import com.android.online.HttpManager;
import com.android.online.HttpUtils;

import android.app.Application;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		listenerAppException();
		initHttpClient();
	}

	/**
	 * 监听app的异常报错机制
	 */
	private void listenerAppException() {
		ApplicationCrashHandler.getInstance().init(this);
	}
	
	/**
	 * 初始化网络请求对象
	 * 注：目前只封装了okHttp
	 */
	private void initHttpClient() {
		HttpUtils.init(this);
		HttpManager mHttpManager = new HttpManager();
		mHttpManager.init();
	}
}
