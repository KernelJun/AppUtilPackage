package com.android.common;

import com.android.common.log.ApplicationCrashHandler;

import android.app.Application;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		listenerAppException();
	}

	/**
	 * 监听app的异常报错机制
	 */
	private void listenerAppException() {
		ApplicationCrashHandler.getInstance().init(this);
	}
}
