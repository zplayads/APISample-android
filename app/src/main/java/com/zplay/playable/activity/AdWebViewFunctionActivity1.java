package com.zplay.playable.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.zplay.playable.panosdk.WebViewController;
import com.zplay.playable.utils.UserConfig;
import com.zplay.playable.vastdemo.utils.ResFactory;
import com.zplay.playable.vastdemo.utils.WindowSizeUtils;

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

        if (!mConfig.isPreRender()) {
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

        mWebViewController.setWebViewPageClosedListener(new WebViewController.WebViewPageClosedListener() {
            @Override
            public void onPageClosed() {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mConfig.isSupportMraid()) {
            mWebViewController.setWebViewPageFinishedListener(new WebViewController.WebViewPageFinishedListener() {
                @Override
                public void onPageFinished() {
                    Log.d(TAG, "fire fireViewableChangeEvent(true)");
                    mWebView.loadUrl("javascript:mraid.fireViewableChangeEvent(true)");
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mConfig.isSupportMraid()) {
            Log.d(TAG, "fire fireViewableChangeEvent(false)");
            mWebView.loadUrl("javascript:mraid.fireViewableChangeEvent(false)");
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
