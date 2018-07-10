package com.android.online;

import android.net.Uri;

public class TestDemo {

	/**
	 * 注意格式的調用
	 * @param url
	 */
	private void downloadQrcode(String url) {
		String specialString = "~!@#$%^&*()_+:|\\=-,./?><;'][{}\"";
		url = Uri.encode(url, specialString);
		HttpManager.GetCallback callback = new com.android.online.HttpManager.GetCallback() {

			@Override
			public void success(final byte[] data) {
			}

			@Override
			public void failed(String reason) {
			}
		};

		GetItem item = new GetItem();
		item.setUrl(url);
		item.setCallback(callback);
		HttpManager.get(item);
	}
}
