package com.zplay.playable.panosdk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

/**
 * Description:
 * <p>
 * Created by lgd on 2019/2/15.
 */
public class WebAttachActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView webView = WebViewHolder.getInstance(this).getWebView(this);
        setContentView(webView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WebViewHolder.getInstance(this).clearWebViewData();
    }
}
