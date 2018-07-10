package com.android.common.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

public class ApplicationCrashHandler implements UncaughtExceptionHandler{

	private static final String TAG = ApplicationCrashHandler.class.getSimpleName();
	
	private static ApplicationCrashHandler mInstance = new ApplicationCrashHandler();
	
	private Context mContext;
	
	public static ApplicationCrashHandler getInstance(){
		return mInstance;
	}
	
	private ApplicationCrashHandler(){
		
	}
	
	/**
	 * 初始化的地方必须放在Application生命周期的OnCreate中
	 * @param context
	 */
	public void init(Context context){
		mContext = context.getApplicationContext();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	
	@Override
	public void uncaughtException(Thread thread, Throwable exception) {
		Log.e(TAG, "uncaughtException:"+exception.getMessage());
		SystemClock.sleep(100);
		WriteLog.writeLog(mContext,(exception == null)? "exception is null" : getStackTrackLog(exception));
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	private String getStackTrackLog(Throwable e){
		StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.flush();
        String stackTrace = sw.toString();
        return stackTrace;
	}

}
