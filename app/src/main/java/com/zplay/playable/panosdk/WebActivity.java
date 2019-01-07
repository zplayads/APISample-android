package com.zplay.playable.panosdk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Description:
 * <p>
 * Created by lgd on 2018/10/11.
 */

public class WebActivity extends Activity {
    private static final String TAG = "PA_no_SDK";

    WebView mWebView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activity);
        WebView.setWebContentsDebuggingEnabled(true);
        mWebView = findViewById(R.id.web_view);
        String url = getIntent().getStringExtra("url");
        String data = getIntent().getStringExtra("data");
        String mimeType = getIntent().getStringExtra("mimeType");
        String encoding = getIntent().getStringExtra("encoding");


        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        mWebView.addJavascriptInterface(new ZPLAYAdsJavascriptInterface(), "ZPLAYAds");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, request.getUrl());
                    startActivity(browserIntent);
                } catch (Exception e) {
                    Log.d(TAG, "shouldOverrideUrlLoading: " + request.getUrl(), e);
                }
                return true;
            }
        });

        if (!TextUtils.isEmpty(url)) {
            mWebView.loadUrl(url);
        } else if (!TextUtils.isEmpty(data)) {
            mWebView.loadDataWithBaseURL(null, data, mimeType, encoding, null);
        } else {
            Toast.makeText(this, "oops~ not content to show.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.destroy();
        mWebView = null;
    }

    private class ZPLAYAdsJavascriptInterface {

        @JavascriptInterface
        public void onCloseSelected() {
            // 可玩广告点击关闭按钮时，触发该方法
            finish();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @JavascriptInterface
        public void onInstallSelected() {
            // 当点击"安装"按钮时，触发该方法
            Log.d(TAG, "onInstallSelected: no nothing.");
        }

    }

    public static void launch(Context ctx, String url) {
        Intent i = new Intent(ctx, WebActivity.class);
        i.putExtra("url", url);
        ctx.startActivity(i);
    }

    public static void launch(Context ctx, String data, String mimeType, String encoding) {
        Intent i = new Intent(ctx, WebActivity.class);
        i.putExtra("data", data);
        i.putExtra("mimeType", mimeType);
        i.putExtra("encoding", encoding);
        ctx.startActivity(i);
    }

}
