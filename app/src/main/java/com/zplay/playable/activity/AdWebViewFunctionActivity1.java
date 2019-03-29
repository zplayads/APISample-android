package com.zplay.playable.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
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

import com.zplay.playable.panosdk.WebViewController;
import com.zplay.playable.utils.UserConfig;
import com.zplay.playable.vastdemo.utils.ResFactory;
import com.zplay.playable.vastdemo.utils.WindowSizeUtils;

import java.net.URLDecoder;

import static com.zplay.playable.panosdk.WebViewController.FUNCTION1;
import static com.zplay.playable.panosdk.WebViewController.loadHtmlData;
import static com.zplay.playable.panosdk.WebViewController.loadUrl;

public class AdWebViewFunctionActivity1 extends Activity {
    private static final String TAG = "WBFunctionActivity1";

    WebView mWebView;
    WebViewController mWebViewController;
    UserConfig mConfig;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebView.setWebContentsDebuggingEnabled(true);
        mConfig = UserConfig.getInstance(this);

        FrameLayout content = new FrameLayout(AdWebViewFunctionActivity1.this);
        FrameLayout.LayoutParams param_content = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        param_content.gravity = Gravity.CENTER;
        content.setLayoutParams(param_content);

        mWebViewController = WebViewController.getWebViewController(FUNCTION1);
        if (mWebViewController == null) {
            Log.e(TAG, "onCreate: Cannot found WebView. Activity finished.");
            return;
        }
        mWebView = mWebViewController.getWebView();

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.d(TAG, "shouldOverrideUrlLoading: " + request.getUrl().toString());
                if (TextUtils.equals(request.getUrl().getScheme(), "mraid") && mConfig.isSupportMraid()) {
                    handleMraidOpen(request.getUrl().toString());
                    return true;
                }

                if (mConfig.isSupportTag()) {
                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, request.getUrl());
                        startActivity(browserIntent);
                    } catch (Exception e) {
                        Log.d(TAG, "shouldOverrideUrlLoading: " + request.getUrl(), e);
                    }
                    return true;
                } else {
                    return super.shouldOverrideUrlLoading(view, request.getUrl().toString());
                }

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, final String url) {
                Log.d(TAG, "shouldOverrideUrlLoading: " + url);
                if (url.startsWith("mraid") && mConfig.isSupportMraid()) {
                    handleMraidOpen(url);
                    return true;
                }

                if (mConfig.isSupportTag()) {
                    try {
                        Uri uri = Uri.parse(url);
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(browserIntent);
                    } catch (Exception e) {
                        Log.d(TAG, "shouldOverrideUrlLoading: " + url, e);
                    }
                    return true;
                } else {
                    return super.shouldOverrideUrlLoading(view, url);
                }
            }
        });

        if (!mWebViewController.isCachedHtmlData()) {
            if (mWebViewController.getHtmlData().startsWith("http")) {
                loadUrl(mWebView, mWebViewController.getHtmlData());
            } else if (!TextUtils.isEmpty(mWebViewController.getHtmlData())) {
                loadHtmlData(mWebView, mWebViewController.getHtmlData());
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
                setResult(20, intent);
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebViewController != null) {
            mWebViewController.destroy();
        }
    }

    public static void launch(Activity ctx) {
        Intent i = new Intent(ctx, AdWebViewFunctionActivity1.class);
        ctx.startActivityForResult(i, 20);
    }

    public void onBackPressed() {
    }
}
