package com.android.common;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


public class MainActivity extends Activity {

	private static String[] appExplains = new String[]{
		"此app主要把常用的功能结合在一起，方便后续使用时，方便结合使用",
		"1.統一tag日記打印功能(可以通过Log打印出来，也可以写入到本地)，封装类为AndroidLog",
		"2.监听app报错机制，让错误信息输出到本地文件中，方便查阅。注：此功能无法捕捉so导致的异常",
		"3.导入gps监听器，注：此为纯gps定位"
	};
	
	
	private TextView mAppFunctionExplains;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAppFunctionExplains = (TextView) findViewById(R.id.app_explain);
        mAppFunctionExplains.setText(getAppFunctionExplains());
    }
    
    
    private String getAppFunctionExplains(){
    	StringBuilder builder = new StringBuilder();
    	for(int i=0; i<appExplains.length; i++){
    		builder.append(appExplains[i]);
    		builder.append("\n");
    	}
    	return builder.toString();
    }
}
