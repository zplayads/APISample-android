package com.zplay.playable.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.zplay.playable.panosdk.Assets;
import com.zplay.playable.panosdk.WebViewController;
import com.zplay.playable.vastdemo.utils.ResFactory;
import com.zplay.playable.vastdemo.utils.WindowSizeUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static com.zplay.playable.panosdk.WebViewController.FUNCTION2;

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

public class AdWebViewFunctionActivity2 extends Activity {
    private static final String TAG = "WBFunctionActivity2";

    WebView mWebView;
    WebViewController mWebViewController;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView.setWebContentsDebuggingEnabled(true);

        FrameLayout content = new FrameLayout(AdWebViewFunctionActivity2.this);
        FrameLayout.LayoutParams param_content = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        param_content.gravity = Gravity.CENTER;
        content.setLayoutParams(param_content);

        mWebViewController = WebViewController.getWebViewController(FUNCTION2);
        if (mWebViewController == null) {
            Log.e(TAG, "onCreate: Cannot found WebView. Activity finished.");
            return;
        }
        mWebView = mWebViewController.getWebView();

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.d(TAG, "shouldOverrideUrlLoading: " + request.getUrl().toString());
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, final String url) {
                Log.d(TAG, "shouldOverrideUrlLoading: " + url);
                return true;
            }
        });

        mWebViewController.setWebViewJSListener(new WebViewController.WebViewJSListener() {
            @Override
            public void onCloseSelected() {
                Toast.makeText(AdWebViewFunctionActivity2.this, "got onCloseSelected event", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("close", 1);
                setResult(30, intent);
                finish();
            }

            @Override
            public void onInstallSelected() {
                Toast.makeText(AdWebViewFunctionActivity2.this, "got onInstallSelected event", Toast.LENGTH_SHORT).show();
                try {
                    if (mWebViewController.getTargetUrl().equals("")) {
                        return;
                    }

                    Uri uri = Uri.parse(mWebViewController.getTargetUrl());
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(browserIntent);
                } catch (Exception e) {
                    Log.d(TAG, "onInstallSelected: " + mWebViewController.getTargetUrl(), e);
                }
            }

            @Override
            public void onVideoEndLoading() {
                Toast.makeText(AdWebViewFunctionActivity2.this, "onVideoEndLoading", Toast.LENGTH_SHORT).show();
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

        ImageView iv_back = new ImageView(this);
        Drawable back = ResFactory.getDrawableByAssets("ic_back", this);
        iv_back.setBackgroundDrawable(back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("close", 1);
                setResult(30, intent);
                finish();
            }
        });

        FrameLayout.LayoutParams param_cancel = new FrameLayout.LayoutParams(WindowSizeUtils.dip2px(this, 35),
                WindowSizeUtils.dip2px(this, 35));
        param_cancel.rightMargin = WindowSizeUtils.dip2px(this, 6);
        param_cancel.topMargin = WindowSizeUtils.dip2px(this, 6);

        content.addView(mWebView);
        content.addView(iv_back, param_cancel);

        setContentView(content);
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


    public static void launch(Activity ctx) {
        Intent i = new Intent(ctx, AdWebViewFunctionActivity2.class);
        ctx.startActivityForResult(i, 30);
    }

}
