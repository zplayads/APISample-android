package com.zplay.playable.panosdk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
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
public class WebViewController {
    private static final String TAG = "WebViewController";
    public static final String FUNCTION1 = "function1";
    public static final String FUNCTION2 = "function2";

    private static Map<String, WebViewController> sWebViewControllerMap = new HashMap<>();

    private WebView mWebView;
    private String mHtmlData;
    private String mTargetUrl;

    private WebViewListener mWebViewListener;
    private WebViewJSListener mWebViewJSListener;
    private boolean isCachedHtmlData;

    public WebViewController(@NonNull Context context, int supportFunctionCode) {

        mWebView = new WebView(context);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);

        if(supportFunctionCode == 2){
            mWebView.addJavascriptInterface(new ZPLAYAdsJavascriptInterface(), "ZPLAYAds");
        }


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


    public void preRenderHtml(@NonNull String htmlData, @Nullable WebViewListener listener) {
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

    public void setHtmlData(String htmlData) {
        mHtmlData = htmlData;
    }

    public String getHtmlData() {
        return TextUtils.isEmpty(mHtmlData) ? "" : mHtmlData;
    }

    public void setTargetUrl(String targetUrl) {
        mTargetUrl = targetUrl;
    }

    public String getTargetUrl() {
        return TextUtils.isEmpty(mTargetUrl) ? "" : mTargetUrl;
    }

    public void setWebViewJSListener(WebViewJSListener webViewJSListener) {
        this.mWebViewJSListener = webViewJSListener;
    }

    public boolean isCachedHtmlData() {
        return isCachedHtmlData;
    }

    @Nullable
    public WebView getWebView() {
        return mWebView;
    }

    public void destroy() {
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

    public interface WebViewListener {
        void onPageFinished(WebView view, String url);

        void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error);
    }

    public static void storeWebViewController(@NonNull String id, WebViewController webViewController) {
        sWebViewControllerMap.put(id, webViewController);
    }

    public static WebViewController getWebViewController(@NonNull String id) {
        if (!sWebViewControllerMap.containsKey(id)) {
            Log.i(TAG, "getWebViewController: can not found value of the id: " + id);
            return null;
        }
        return sWebViewControllerMap.get(id);
    }

    static void removeWebViewController(@NonNull String id) {
        sWebViewControllerMap.remove(id);
    }


    public interface WebViewJSListener {
        void onCloseSelected();

        void onInstallSelected();

        void onVideoEndLoading();

    }

    public class ZPLAYAdsJavascriptInterface {

        @JavascriptInterface
        public void onCloseSelected() {
            // 可玩广告点击关闭按钮时，触发该方法
            Log.d(TAG, "onCloseSelected: ");
            if (mWebViewJSListener != null) {
                mWebViewJSListener.onCloseSelected();
            }
        }

        @JavascriptInterface
        public void onInstallSelected() {
            // 当点击"安装"按钮时，触发该方法
            Log.d(TAG, "onInstallSelected: ");
            if (mWebViewJSListener != null) {
                mWebViewJSListener.onInstallSelected();
            }
        }

        @JavascriptInterface
        public void onVideoEndLoading() {
            Log.d(TAG, "onVideoEndLoading: ");
            if (mWebViewJSListener != null) {
                mWebViewJSListener.onVideoEndLoading();
            }
        }

    }

}
