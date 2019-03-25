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
//    private static final String API_SERVER = "https://pa-engine.zplayads.com/v1/api/ads";
    private static final String API_SERVER = "http://101.201.78.229:8999/v1/api/ads";

    private static final String FAKE_POST_BODY = "{\n" +
        "  \"version\": \"1.0\",\n" +
        "  \"developer_token\": \"989BBE25-9621-1181-1239-0FBB68F7B5FF\",\n" +
        "  \"need_https\": 0,\n" +
        "  \"support_function\":1,\n" +
        "  \"app\": {\n" +
        "    \"app_id\": \"8AEEE8AE-6FD6-F67A-7F3E-8FA241756A74\",\n" +
        "    \"app_name\": \"愤怒的小鸟2\",\n" +
        "    \"bundle_id\": \"com.rovio.baba\",\n" +
        "    \"version\": \"5.1.8\",\n" +
        "    \"cat\": \"\"\n" +
        "  },\n" +
        "  \"device\": {\n" +
        "    \"model\": \"iPhone8,4\",\n" +
        "    \"manufacturer\": \"Apple\",\n" +
        "    \"brand\": \"apple\",\n" +
        "    \"plmn\": \"46001\",\n" +
        "    \"device_type\": \"phone\",\n" +
        "    \"dnt\": 0,\n" +
        "    \"connection_type\": \"wifi\",\n" +
        "    \"carrier\": \"\",\n" +
        "    \"orientation\": 1,\n" +
        "    \"mac\": \"d41d8cd98f00b204e9800998ecf8427e\",\n" +
        "    \"imei\": \"\",\n" +
        "    \"android_id\": \"\",\n" +
        "    \"android_adid\": \"\",\n" +
        "    \"idfa\": \"B7904707-AEFC-495D-B262-10C912F7C6DC\",\n" +
        "    \"idfv\": \"5F361B99-EC21-42A8-BA8F-1554B6B6CAD0\",\n" +
        "    \"openudid\": \"\",\n" +
        "    \"language\": \"zh-Hans-CN\",\n" +
        "    \"os_type\": \"iOS\",\n" +
        "    \"os_version\": \"12.0\",\n" +
        "    \"screen\": {\n" +
        "      \"width\": 1242,\n" +
        "      \"height\": 2208,\n" +
        "      \"dpi\": 0,\n" +
        "      \"pxratio\": 0\n" +
        "    },\n" +
        "    \"geo\": {\n" +
        "      \"lat\": 0,\n" +
        "      \"lon\": 0,\n" +
        "      \"horizontal_accu\": 0,\n" +
        "      \"vertical_accu\": 0\n" +
        "    }\n" +
        "  },\n" +
        "  \"ads\": [{\n" +
        "    \"video\":{\n" +
        "    \"mimes\":[\"mp4\"],\n" +
        "    \"minduration\":[\"1\"],\n" +
        "    \"maxduration\":[\"10000\"],\n" +
        "    \"protocols\":[3],\n" +
        "    \"startdelay\":1,\n" +
        "    \"linearity\":1,\n" +
        "    \"minbitrate\":1,\n" +
        "    \"maxbitrate\":10000,\n" +
        "    \"pos\":1\n" +
        "  },\n" +
        "    \"unit_type\": 4,\n" +
        "    \"ad_unit_id\": \"4B8AE0D5-D508-7348-7F40-B3566305DA3A\"\n" +
        "  }]\n" +
        "}";

    public static void fetchAdData(@NonNull final AdResult result) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(API_SERVER);

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
                    writer.write(FAKE_POST_BODY);
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
                    result.onResult(getHTMLData(response.toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                    result.onResult(new AdModel(""));
                }
            }
        }).start();

    }

    private static AdModel getHTMLData(String apiData) {
        if (TextUtils.isEmpty(apiData)) {
            return new AdModel("");
        }

        try {
            Log.e(TAG, "apiData : " + apiData);
            JSONObject rootJson = new JSONObject(apiData);
            JSONArray adsJson = rootJson.getJSONArray("ads");
            JSONObject ad = adsJson.getJSONObject(0);

//            String htmlData = ad.getString("playable_ads_html");
//            Log.i(TAG, "playable_ads_html : " + htmlData);
//            String appMarketPackage = ad.getString("store_bundle");
//            Log.i(TAG, "appMarketPackage : " + appMarketPackage);
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
