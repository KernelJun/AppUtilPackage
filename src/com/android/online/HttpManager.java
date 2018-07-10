package com.android.online;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.android.common.log.AndroidLog;
import com.android.online.okhttp.OkHttpClientUtil;

public class HttpManager {

	private static final String TAG = HttpManager.class.getSimpleName();
	
	private static HttpInterface mHttpInterface;
	
	private static Handler mHandler;
	
	private static final long DELAY_RESTART = 3*1000;
	private static final int MSG_DEFAULT      		= 0; 
	private static final int MSG_GET_RESTART  		= MSG_DEFAULT + 1; 
	private static final int MSG_POST_BYTE_RESTART  = MSG_DEFAULT + 2; 
	private static final int MSG_DOWNLOAD_RESTART   = MSG_DEFAULT + 3; 
	
	public interface GetCallback{
		void success(byte[] data);
		void failed(String reason);
	}
	
	public interface ProgressCallback{
		void publishProgress(int value);
	}
	
	public void init(){
		mHandler = new Handler(Looper.getMainLooper()){

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch(msg.what){
				case MSG_GET_RESTART:
					GetItem restartItem = (GetItem)msg.obj;
					AndroidLog.v(TAG, "restartItem:"+restartItem.toString());
					get(restartItem);
					break;
				case MSG_POST_BYTE_RESTART:
					PostItem restartPItem = (PostItem)msg.obj;
					AndroidLog.v(TAG, "restartItem:"+restartPItem.toString());
					postByte(restartPItem);
					break;
				case MSG_DOWNLOAD_RESTART:
					GetItem restartDItem = (GetItem)msg.obj;
					AndroidLog.v(TAG, "restartItem:"+restartDItem.toString());
					downloadFile(restartDItem);
					break;
				}
			}
			
		};
		mHttpInterface = getHttpClient();
		mHttpInterface.init();
	}
	
	private static HttpInterface getHttpClient(){
		return new OkHttpClientUtil();
	}


	public static void get(GetItem getItem){
		if(HttpUtils.hasNetwork() || getItem.isCallAgain()){
			mHttpInterface.get(getItem);
		} else {
			AndroidLog.v(TAG, "no network");
			if(getItem != null){
				getItem.setCallAgain(true);
				Message msg = new Message();
				msg.what = MSG_GET_RESTART;
				msg.obj = getItem;
				if(!mHandler.sendMessageDelayed(msg, DELAY_RESTART)){
					getItem.getCallback().failed("handler is die, I so badly");
				}
			}
		}
	}
	
	public static void downloadFile(GetItem getItem){
		if(HttpUtils.hasNetwork() || getItem.isCallAgain()){
			mHttpInterface.downloadFile(getItem);
		} else {
			AndroidLog.v(TAG, "no network");
			if(getItem != null){
				getItem.setCallAgain(true);
				Message msg = new Message();
				msg.what = MSG_GET_RESTART;
				msg.obj = getItem;
				if(!mHandler.sendMessageDelayed(msg, DELAY_RESTART)){
					getItem.getCallback().failed("handler is die, I so badly");
				}
			}
		}
	}
	
	public static void postByte(PostItem item){
		if(HttpUtils.hasNetwork() || item.isCallAgain()){
			mHttpInterface.postByte(item);
		} else {
			AndroidLog.v(TAG, "post byte no network");
			if(item != null){
				item.setCallAgain(true);
				Message msg = new Message();
				msg.what = MSG_POST_BYTE_RESTART;
				msg.obj = item;
				if(!mHandler.sendMessageDelayed(msg, DELAY_RESTART)){
					item.getCallback().failed("handler is die, I so badly");
				}
			}
		}
	}
}
