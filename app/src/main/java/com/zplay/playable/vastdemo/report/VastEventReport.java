package com.zplay.playable.vastdemo.report;

import android.os.AsyncTask;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;

public class VastEventReport extends AsyncTask<String, Void, String> {
    private String TAG = "VastEventReport";

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection httpUrlCon = null;
        try {
            // new  a  url connection
            URL httpUrl = new URL(strings[0]);
            httpUrlCon = (HttpURLConnection) httpUrl.openConnection();
            // set  http  configure
            httpUrlCon.setConnectTimeout(2000);// 建立连接超时时间
            httpUrlCon.setReadTimeout(1000);//数据传输超时时间，很重要，必须设置。
            httpUrlCon.setDoInput(true); // 向连接中写入数据
            httpUrlCon.setUseCaches(false); // 禁止缓存
            httpUrlCon.setInstanceFollowRedirects(true);
            httpUrlCon.setRequestProperty("Charset", "UTF-8");

            httpUrlCon.setDoOutput(false); // 从连接中读取数据
            httpUrlCon.setRequestMethod("GET");// 设置请求类型为

            httpUrlCon.connect();
            //check the result of connection
            Log.i(TAG, "httpUrlCon.getResponseCode() : " + httpUrlCon.getResponseCode());
        } catch (Exception e) {
            Log.e(TAG, "doInBackground error: " + e);
            //如果需要处理超时，可以在这里写
        } finally {
            if (httpUrlCon != null) {
                httpUrlCon.disconnect(); // 断开连接
            }
        }
        return null;
    }
}
