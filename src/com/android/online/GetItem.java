package com.android.online;

import com.android.online.HttpManager.GetCallback;
import com.android.online.HttpManager.ProgressCallback;

public class GetItem {

	private String url;
	private GetCallback callback;
	private boolean callAgain = false;
	private String fileName;
	private ProgressCallback progressCallback;
	
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

	public boolean isCallAgain() {
		return callAgain;
	}

	public void setCallAgain(boolean callAgain) {
		this.callAgain = callAgain;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public ProgressCallback getProgressCallback() {
		return progressCallback;
	}

	public void setProgressCallback(ProgressCallback progressCallback) {
		this.progressCallback = progressCallback;
	}

	@Override
	public String toString() {
		return "url="+url+",callAgain="+callAgain;
	}
	
}
