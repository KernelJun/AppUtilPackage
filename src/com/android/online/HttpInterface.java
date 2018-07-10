package com.android.online;

public interface HttpInterface {
	int CONN_TIMEOUT = 60*1000;
	int READ_TIMEOUT = 60*1000;
	void init();
	void postByte(PostItem item);
	void get(GetItem item);
	void downloadFile(GetItem item);
}
