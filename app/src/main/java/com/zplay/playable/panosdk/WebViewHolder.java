package com.zplay.playable.panosdk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.lang.ref.WeakReference;

/**
 * Description:
 * <p>
 * Created by lgd on 2019/2/15.
 */
class WebViewHolder {
    private static WebViewHolder sWebViewHolder;
    private WeakReference<WebView> mWebViewRef;
    private String mHtmlDoc;
    private WebViewListener mLitener;

    private WebViewHolder(Context context) {
        mWebViewRef = new WeakReference<>(newWebView(context));
    }

    static WebViewHolder getInstance(Context context) {
        if (sWebViewHolder == null) {
            sWebViewHolder = new WebViewHolder(context);
        }
        return sWebViewHolder;
    }

    WebView getWebView(Context context) {
        if (mWebViewRef.get() == null) {
            mWebViewRef = new WeakReference<>(newWebView(context));
            if (!TextUtils.isEmpty(mHtmlDoc)) {
                mWebViewRef.get().loadDataWithBaseURL(null, mHtmlDoc, "text/html", "UTF-8", null);
            }
        }
        return mWebViewRef.get();
    }

    void loadHtmlDoc(Context context, String htmlDoc, WebViewListener listener) {
        mLitener = listener;
        mHtmlDoc = htmlDoc;
        WebView webView = getWebView(context);
        webView.loadDataWithBaseURL(null, htmlDoc, "text/html", "UTF-8", null);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private WebView newWebView(Context context) {
        WebView result = new WebView(context);
        result.getSettings().setJavaScriptEnabled(true);
        result.getSettings().setMediaPlaybackRequiresUserGesture(false);
        result.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (mLitener != null && mWebViewRef.get() != null && !TextUtils.isEmpty(mHtmlDoc)) {
                    mLitener.onPageFinished(view, url);
                }
            }
        });
        return result;
    }

    void clearWebViewData() {
        mHtmlDoc = null;
        if (mWebViewRef.get() != null) {
            WebView webView = mWebViewRef.get();
            if (webView.getParent() != null) {
                ((ViewGroup) webView.getParent()).removeView(webView);
            }
            mWebViewRef.clear();
            webView.destroy();
        }
    }

    interface WebViewListener {
        void onPageFinished(WebView view, String url);
    }
}
