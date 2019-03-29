package com.zplay.playable.vastdemo;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

public class APIDataFetcher {
    private static String TAG = "APIDataFetcher";
    private static final String API_SERVER_TEST = "http://101.201.78.229:8999/v1/api/ads";
    private static final String API_SERVER = "http://pa-engine.zplayads.com/v1/api/ads";


    public static void fetchAPIAdData(@NonNull final String data, final boolean isDebug, @NonNull final AdResult result) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    URL url = new URL(isDebug ? API_SERVER_TEST : API_SERVER);

//                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.addRequestProperty("X-Forwarded-For", "223.104.147.164");

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, StandardCharsets.UTF_8));
                    writer.write(data);
                    writer.flush();
                    writer.close();
                    os.close();

                    conn.connect();

                    int responseCode = conn.getResponseCode();
                    StringBuilder response = new StringBuilder();
                    Log.e(TAG, "responseCode : " + responseCode);
                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        String line;
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        while ((line = br.readLine()) != null) {
                            response.append(line);
                        }
                    }
                    result.onResult(getAPIData(response.toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                    result.onResult(new AdModel(""));
                }
            }
        }).start();

    }


    private static AdModel getAPIData(String apiData) {
        if (TextUtils.isEmpty(apiData)) {
            return new AdModel("");
        }

        try {
            Log.e(TAG, "apiData : " + apiData);
            JSONObject rootJson = new JSONObject(apiData);
            JSONArray adsJson = rootJson.getJSONArray("ads");
            JSONObject ad = adsJson.getJSONObject(0);

            String htmlData = ad.getString("playable_ads_html");
            Log.i(TAG, "playable_ads_html : " + htmlData);
            String targetUrl = ad.getString("target_url");
            Log.i(TAG, "targetUrl : " + targetUrl);


            AdModel result = new AdModel(htmlData);
            result.setTargetUrl(targetUrl);

            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            return new AdModel("");
        }
    }


    public static void fetchVASTAdData(@NonNull final String data,final boolean isDebug,final AdResult result) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    URL url = new URL(isDebug ? API_SERVER_TEST : API_SERVER);

//                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.addRequestProperty("X-Forwarded-For", "223.104.147.164");

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, StandardCharsets.UTF_8));
                    writer.write(data);
                    writer.flush();
                    writer.close();
                    os.close();

                    conn.connect();

                    int responseCode = conn.getResponseCode();
                    StringBuilder response = new StringBuilder();
                    Log.e(TAG, "responseCode : " + responseCode);
                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        String line;
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        while ((line = br.readLine()) != null) {
                            response.append(line);
                        }
                    }
                    result.onResult(getVASTData(response.toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                    result.onResult(new AdModel(""));
                }
            }
        }).start();

    }

    private static AdModel getVASTData(String apiData) {
        if (TextUtils.isEmpty(apiData)) {
            return new AdModel("");
        }

        try {
            Log.e(TAG, "apiVastData : " + apiData);
            JSONObject rootJson = new JSONObject(apiData);
            JSONArray adsJson = rootJson.getJSONArray("ads");
            JSONObject ad = adsJson.getJSONObject(0);

            String targetPackage = ad.getString("app_bundle");
            Log.i(TAG, "targetPackage : " + targetPackage);
            String adm = ad.getString("adm");
            Log.i(TAG, "adm : " + adm);

            AdModel result = new AdModel(null);
            result.setAppMarketPackage(null);
            result.setTargetPackage(targetPackage);
            result.setAdm(adm);
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            return new AdModel("");
        }
    }

    public interface AdResult {
        void onResult(AdModel adModel);
    }
}
