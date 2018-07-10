package com.android.common.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;

public class WriteLog {
	
	public static final String TAG = "RmtWriteLog";
	
	private static final String SUFFIX =  ".txt";
	
	private static final String CrashLog = "AndroidCrashLog";
	
	private static File logFileDvr;
	private static FileOutputStream logFoStreamDvr;
	private static String lasttimeDvr;

	private static File logFile;
	private static FileOutputStream logFoStream;
	private static String lasttime;
	
	public static void writeLog(Context context, String log){
		if (log == null || "".equals(log)) return;
		
		String timeFormat = "yyyyMMdd";//"yyyyMMdd_HH";
		String time = new SimpleDateFormat(timeFormat).format(new Date());
		
		if (logFile == null){
			lasttime = time;
			logFile =  new File(getLogPath(CrashLog)+"/"+time+SUFFIX);
		}
		if(!time.equals(lasttime)){
			lasttime = time;
			logFile =  new File(getLogPath(CrashLog)+"/"+time+SUFFIX);
		}
		try {
			String timeString = DateFormat.format("HH-mm-ss", System.currentTimeMillis()).toString();
			logFoStream = new FileOutputStream(logFile, true);
			logFoStream.write((timeString + ": "+getAppInfo(context.getApplicationContext())+" Reason:  "+log + "\n").getBytes());
			logFoStream.flush();
			logFoStream.close();
		} catch (FileNotFoundException e) {
			Log.e(TAG, "FileNotFoundException failed:"+e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, "IOException failed:"+e.getMessage());
		} finally {
			 stopLog();
		}
	}
	
	private static void stopLog(){
		if (logFoStream != null){
			try {
				logFoStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			logFile = null;
			logFoStream = null;
		}
	}
	
	private static String getAppInfo(Context context){
		StringBuffer stringBuffer = new StringBuffer();
		try {
			PackageInfo mInfo = context.getPackageManager().
					getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
			stringBuffer.append("packageName:"+mInfo.packageName+" ");
			stringBuffer.append("versionCode:"+mInfo.versionCode+" ");
			stringBuffer.append("versionName:"+mInfo.versionName+"\n");
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			stringBuffer.append("NameNotFoundException :"+e.getMessage()+"\n");
			Log.e(TAG, "NameNotFoundException failed:"+e.getMessage());
		}
		return stringBuffer.toString();
	}
	
	private static String getLogPath(String fileName){
		String logPath = Environment.getExternalStorageDirectory().getPath()+"/"+fileName;
		File mFile = new File(logPath);
		if(mFile.exists()){
			if(mFile.isFile()){
				mFile.delete();
				if(mFile.mkdir()){
					return logPath;
				}
			}
		} else {
			if(mFile.mkdir()){
				return logPath;
			}
		}
		return logPath;
	}
	
	public static void writeLog(String log){
		if (log == null || "".equals(log)) return;
		Log.v(TAG, log);
		String timeFormat = "yyyyMMdd";//"yyyyMMdd_HH";
		String time = new SimpleDateFormat(timeFormat).format(new Date());
		String logPath = getLogPath(TAG);
		
		if (logFileDvr == null || !time.equals(lasttimeDvr)){
			lasttimeDvr = time;
			logFileDvr =  new File(logPath+"/"+time+SUFFIX);
		}
		
		try {
			String timeString = DateFormat.format("hh-mm-ss", System.currentTimeMillis()).toString();
			logFoStreamDvr = new FileOutputStream(logFileDvr, true);
			logFoStreamDvr.write((timeString + " "+log + "\n").getBytes());
			logFoStreamDvr.flush();
		} catch (FileNotFoundException e) {
			Log.e(TAG, "FileNotFoundException failed:"+e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, "IOException failed:"+e.getMessage());
		} 
	}
	
	public static void stopWriteLog(){
		if (logFoStreamDvr != null){
			try {
				logFoStreamDvr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			logFileDvr = null;
			logFoStreamDvr = null;
		}
	}
}
