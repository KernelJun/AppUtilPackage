package com.android.common.log;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.util.Log;


/**
 * 此类是为了统一通过一个tag来查看日记，目的是为了方便查看日志
 * 
 * @author KernelJun
 *
 */
public class AndroidLog {
	
	private static final String TAG  = WriteLog.TAG;
	
	private static final boolean  DEBUG = false;
	
	private static final boolean  DEBUG_INFO = false;
	
	public static void v(final String tag, final String str){
		if(!DEBUG) {
			Log.v(TAG, "tag,"+str);
			return;
		}
		WriteLog.writeLog(tag+",v:"+str);
	}
	
	public static void e(final String tag, final String str){
		if(!DEBUG) {
			Log.e(TAG, "tag,"+str);
			return;
		}
		WriteLog.writeLog(tag+",e:"+str);
	}
	
	public static void e(final String tag, final Throwable e){
		if(!DEBUG) {
			Log.e(TAG, "tag,"+getStackTrackLog(e));
			return;
		}
		WriteLog.writeLog(tag+",e:"+getStackTrackLog(e));
	}
	
	
	public static void d(final String tag, final String str){
		if(DEBUG_INFO){
			if(!DEBUG) {
				Log.d(TAG, "tag,"+str);
				return;
			}
			WriteLog.writeLog(tag+",e:"+str);
		}
	}
	
	public static void stop(){
		WriteLog.stopWriteLog();
	}
	
	private static String getStackTrackLog(Throwable e){
		StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.flush();
        return sw.toString();
	}
}
