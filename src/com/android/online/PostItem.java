package com.android.online;

import com.android.online.HttpManager.GetCallback;

public class PostItem {

	private String url;
	private GetCallback callback;
	private byte[] data;
	private boolean callAgain = false;
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public GetCallback getCallback() {
		return callback;
	}
	
	public void setCallback(GetCallback callback) {
		this.callback = callback;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
	public boolean isCallAgain() {
		return callAgain;
	}
	
	public void setCallAgain(boolean callAgain) {
		this.callAgain = callAgain;
	}

	@Override
	public String toString() {
		return "url="+url+",callAgain="+callAgain;
	}

}
