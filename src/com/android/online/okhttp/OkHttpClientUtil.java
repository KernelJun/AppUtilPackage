package com.android.online.okhttp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.android.common.log.AndroidLog;
import com.android.common.log.WriteLog;
import com.android.online.GetItem;
import com.android.online.HttpInterface;
import com.android.online.PostItem;

public class OkHttpClientUtil implements HttpInterface {

	private static final String TAG = OkHttpClientUtil.class.getSimpleName();
	
	private OkHttpClient okHttpClient;
	
	@Override
	public void init() {
		AndroidLog.v(TAG, "init");
		okHttpClient = new OkHttpClient.Builder()
		.connectTimeout(CONN_TIMEOUT, TimeUnit.MILLISECONDS)
		.readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
		.addInterceptor(new LoggerInterceptor(WriteLog.TAG))
		.build();
	}

	@Override
	public void get(final GetItem item) {

		if(item == null){
			AndroidLog.e(TAG, "get, item is null!!!!,Stupid!!!!,please change!!!!");
			return;
		}
		
		Request getRequest = new Request.Builder()
        .url(item.getUrl())
        .build();
		
		Call getCall = okHttpClient.newCall(getRequest);
		getCall.enqueue(new Callback() {
			
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if(response != null && response.code() == 500){//特殊处理,针对二维码来处理
					AndroidLog.e(TAG, "special error error error!!!");
					item.getCallback().success(null);
				} else {
					byte[] data = (response != null && response.body() != null) ? response.body().bytes() : null;
					item.getCallback().success(data);
				}
				//RmtLog.e(TAG, "onResponse call:"+call.toString()+",response:"+((data == null) ? "exception" : new String(data)));
			}
			
			@Override
			public void onFailure(Call call, IOException ioException) {
				if(item.isCallAgain()){
					item.getCallback().failed(ioException.getMessage());
					AndroidLog.e(TAG, ioException);
				} else {
					item.setCallAgain(true);
					get(item);
				}
			}
		});
		
	}
	
	@Override
	public void downloadFile(final GetItem item) {
		if(item == null){
			AndroidLog.e(TAG, "get, item is null!!!!,Stupid!!!!,please change!!!!");
			return;
		}
		
		Request getRequest = new Request.Builder()
        .url(item.getUrl())
        .build();
		
		Call getCall = okHttpClient.newCall(getRequest);
		getCall.enqueue(new Callback() {
			
			@Override
			public void onResponse(Call call, final Response response) throws IOException {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						InputStream inputStream = null;
						long total = response.body().contentLength();
						OutputStream outputStream = null;
						try{
							inputStream = response.body().byteStream();
							inputStream = new BufferedInputStream(inputStream);
							
							byte[] buffer = new byte[1024];
							int len = 0;
							int count = 0;
							outputStream = new FileOutputStream(item.getFileName());
							outputStream = new BufferedOutputStream(outputStream);
							while( (len = inputStream.read(buffer)) != -1){
								count +=len;
								if(item.getProgressCallback() != null){
									item.getProgressCallback().publishProgress((int)((count*100/total)));
								}
								outputStream.write(buffer, 0, len);
							}
							outputStream.flush();
							outputStream.close();
							inputStream.close();
							item.getCallback().success(item.getFileName().getBytes());
						} catch (Exception e){
							AndroidLog.e(TAG, e);
						} finally {
							if(outputStream != null){
								try{
									outputStream.close();
								} catch (Exception e){
								}
							}
							
							if(inputStream != null){
								try{
									inputStream.close();
								} catch (Exception e){
									
								}
							}
						}
						
					}
				}).start();
			}
			
			@Override
			public void onFailure(Call call, IOException ioException) {
				if(item.isCallAgain()){
					item.getCallback().failed(ioException.getMessage());
					AndroidLog.e(TAG, ioException);
				} else {
					item.setCallAgain(true);
					get(item);
				}
			}
		});
	}

	@Override
	public void postByte(final PostItem item) {
		
		if(item == null){
			AndroidLog.e(TAG, "post, item is null!!!!,Stupid!!!!,please change!!!!");
			return;
		}
		
		RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), item.getData());

		Request postRequest = new Request.Builder()
		.url(item.getUrl())
		.post(body)
		.build();
		
		Call getCall = okHttpClient.newCall(postRequest);
		getCall.enqueue(new Callback() {
			
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				byte[] data = (response != null && response.body() != null) ? response.body().bytes() : null;
				item.getCallback().success(data);
				AndroidLog.e(TAG, "onResponse call:"+call.toString()+",response:"+((data == null) ? "exception" : new String(data)));
			}
			
			@Override
			public void onFailure(Call call, IOException ioException) {
				if(item.isCallAgain()){
					item.getCallback().failed(ioException.getMessage());
					AndroidLog.e(TAG, ioException);
				} else {
					item.setCallAgain(true);
					postByte(item);
				}
			}
		});
	}
	
}
