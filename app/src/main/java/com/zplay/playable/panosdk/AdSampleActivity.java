package com.zplay.playable.panosdk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static com.zplay.playable.panosdk.WebViewController.TEMP_KEY;

/**
 * Description:
 * <p>
 * mraid 注入过程参考资料：
 * https://www.programcreek.com/java-api-examples/index.php?source_dir=MobFox-Android-SDK-master/src/main/java/com/adsdk/sdk/mraid/MraidView.java
 * <p>
 * 原理为，使用 <head><script>mraid.js</script> 替换 HTML 中的 <head> 标签
 * <p>
 * <p>
 * 还有一种注入方法，是直接使用 webview.evaluateJavascript(js, new ValueCallback<String>(..)) 来注入
 * 如：https://github.com/nexage/sourcekit-mraid-android/blob/master/src/src/org/nexage/sourcekit/mraid/MRAIDView.java
 * <p>
 * 实践过程中，直接使用 webview.evaluateJavascript 注入，在执行 webview.load(htmlDocUrl) 后，在 htmlDocUrl
 * 里无法找到 window.mraid 对象；先执行 webview.load(htmlDocUrl) ，然后在 onPageFinished 回调中执行 webview.evaluateJavascript，
 * 可以在 htmlDocUrl 中找到 window.mraid 对象，但此对象应在加载完成之前添入到 htmlDocUrl，所以没有使用这个方法。
 * <p>
 * <p>
 * Created by lgd on 2018/10/11.
 */

public class AdSampleActivity extends Activity {
    private static final String TAG = "AdSampleActivity";

    WebView mWebView;
    WebViewController mWebViewController;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView.setWebContentsDebuggingEnabled(true);

        mWebViewController = WebViewController.getWebViewController(TEMP_KEY);
        if (mWebViewController == null) {
            Log.e(TAG, "onCreate: Cannot found WebView. Activity finished.");
            return;
        }
        mWebView = mWebViewController.getWebView();

        mWebView.addJavascriptInterface(new ZPLAYAdsJavascriptInterface(), "ZPLAYAds");
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (TextUtils.equals(request.getUrl().getScheme(), "mraid")) {
                    handleMraidOpen(request.getUrl().toString());
                    return true;
                }
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, request.getUrl());
                    startActivity(browserIntent);
                } catch (Exception e) {
                    Log.d(TAG, "shouldOverrideUrlLoading: " + request.getUrl(), e);
                }
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("mraid")) {
                    handleMraidOpen(url);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        if (!mWebViewController.isCachedHtmlData()) {
            if (mWebViewController.getHtmlData().startsWith("http")) {
                loadUrl(mWebViewController.getHtmlData());
            } else if (!TextUtils.isEmpty(mWebViewController.getHtmlData())) {
                loadHtmlData(mWebViewController.getHtmlData());
            } else {
                Toast.makeText(this, "Html data is empty.", Toast.LENGTH_SHORT).show();
            }
        }
        setContentView(mWebView);
    }

    private void handleMraidOpen(String url) {
        try {
            Log.d(TAG, "handleMraidOpen: " + url);
            String targetUrl = url.replace("mraid://open?url=", "");
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URLDecoder.decode(targetUrl, "UTF-8")));
            startActivity(browserIntent);
        } catch (Exception e) {
            Log.d(TAG, "handleMraidOpen: ", e);
        }
    }

    public void loadHtmlData(String data) {
        if (data == null) {
            return;
        }

        // If the string data lacks the HTML boilerplate, add it.
        if (!data.contains("<html>")) {
            data = "<html><head></head><body style='margin:0;padding:0;'>" + data +
                    "</body></html>";
        }

        // Inject the MRAID JavaScript bridge.
        data = data.replace("<head>", "<head><script>" + Assets.MRAID_JS + "</script>");

        mWebView.loadDataWithBaseURL(null, data, "text/html", "UTF-8", null);
    }

    public void loadUrl(String url) {
        if (url == null) {
            return;
        }

        if (url.startsWith("javascript:")) {
            mWebView.loadUrl(url);
            return;
        }
        fetchHtmlAndLoad(url);
    }

    private void fetchHtmlAndLoad(@NonNull final String urlStr) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream inStream;
                try {
                    URL url = new URL(urlStr);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    inStream = conn.getInputStream();
                    if (inStream == null) {
                        throw new IOException("http request input stream is null.");
                    }
                } catch (IOException e) {
                    Log.d(TAG, e.getMessage());
                    return;
                }
                final String html;
                try {
                    byte[] data = readInputStream(inStream);
                    html = new String(data, StandardCharsets.UTF_8);
                    if (mWebView == null || TextUtils.isEmpty(html)) {
                        Log.d(TAG, "fetch html doc is null.");
                        return;
                    }
                    mWebView.post(new Runnable() {
                        @Override
                        public void run() {
                            loadHtmlData(html);
                        }
                    });
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            }
        }).start();
    }

    private static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebViewController != null) {
            mWebViewController.destroy();
        }
    }

    private class ZPLAYAdsJavascriptInterface {

        @JavascriptInterface
        public void onCloseSelected() {
            // 可玩广告点击关闭按钮时，触发该方法
            Toast.makeText(AdSampleActivity.this, "got onCloseSelected event", Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void onInstallSelected() {
            // 当点击"安装"按钮时，触发该方法
            Toast.makeText(AdSampleActivity.this, "got onInstallSelected event", Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void onVideoEndLoading() {
            Toast.makeText(AdSampleActivity.this, "onVideoEndLoading", Toast.LENGTH_SHORT).show();
        }

    }

    public static void launch(Context ctx) {
        Intent i = new Intent(ctx, AdSampleActivity.class);
        ctx.startActivity(i);
    }

}
