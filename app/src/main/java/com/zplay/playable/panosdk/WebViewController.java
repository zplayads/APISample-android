package com.zplay.playable.panosdk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * <p>
 * Created by lgd on 2019/2/15.
 */
class WebViewController {
    private static final String TAG = "WebViewController";
    static final String TEMP_KEY = "tk";

    private static Map<String, WebViewController> sWebViewControllerMap = new HashMap<>();

    private WebView mWebView;
    private String mHtmlData;

    private WebViewListener mWebViewListener;
    private boolean isCachedHtmlData;

    WebViewController(@NonNull Context context) {
        mWebView = new WebView(context);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                isCachedHtmlData = true;
                if (mWebViewListener != null) {
                    mWebViewListener.onPageFinished(view, url);
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (mWebViewListener != null) {
                    mWebViewListener.onReceivedError(view, request, error);
                }
            }
        });
    }


    void preRenderHtml(@NonNull String htmlData, @Nullable WebViewListener listener) {
        if (mWebView == null) {
            Log.d(TAG, "preRenderHtml: webview already destroy, recreated it again.");
            return;
        }

        mWebViewListener = listener;
        if (htmlData.startsWith("http")) {
            mWebView.loadUrl(htmlData);
        } else {
            mWebView.loadDataWithBaseURL(null, htmlData, "txt/html", "UTF-8", null);
        }
    }

    void setHtmlData(String htmlData) {
        mHtmlData = htmlData;
    }

    String getHtmlData() {
        return TextUtils.isEmpty(mHtmlData) ? "" : mHtmlData;
    }

    boolean isCachedHtmlData() {
        return isCachedHtmlData;
    }

    @Nullable
    WebView getWebView() {
        return mWebView;
    }

    void destroy() {
        isCachedHtmlData = false;
        if (mWebView == null) {
            Log.d(TAG, "destroy: webivew is null");
            return;
        }
        if (mWebView.getParent() != null && mWebView.getParent() instanceof ViewGroup) {
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
        }
        mWebView.destroy();

    }

    interface WebViewListener {
        void onPageFinished(WebView view, String url);

        void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error);
    }

    static void storeWebViewController(@NonNull String id, WebViewController webViewController) {
        sWebViewControllerMap.put(id, webViewController);
    }

    static WebViewController getWebViewController(@NonNull String id) {
        if (!sWebViewControllerMap.containsKey(id)) {
            Log.i(TAG, "getWebViewController: can not found value of the id: " + id);
            return null;
        }
        return sWebViewControllerMap.get(id);
    }

    static void removeWebViewController(@NonNull String id) {
        sWebViewControllerMap.remove(id);
    }
}
