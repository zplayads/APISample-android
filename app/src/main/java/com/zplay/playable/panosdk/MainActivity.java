package com.zplay.playable.panosdk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import static com.zplay.playable.panosdk.WebViewController.TEMP_KEY;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivityTag";

    EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText = findViewById(R.id.edit_text);
        mEditText.setText(DataHolder.HTML_DATA);
        WebView.setWebContentsDebuggingEnabled(true);
    }

    public void show(View view) {
        String trimContent = mEditText.getText().toString().trim();
        if (TextUtils.isEmpty(trimContent)) {
            Toast.makeText(this, "No be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        WebViewController webViewController = new WebViewController(this);
        webViewController.setHtmlData(getReformatData(trimContent));
        WebViewController.storeWebViewController(TEMP_KEY, webViewController);

        AdSampleActivity.launch(this);
    }

    public void showPreRenderedView(View view) {
        WebViewController webViewController = WebViewController.getWebViewController(TEMP_KEY);
        if (webViewController == null || !webViewController.isCachedHtmlData()) {
            Toast.makeText(this, "WebView not hit onPageFinished yet.", Toast.LENGTH_SHORT).show();
            return;
        }
        AdSampleActivity.launch(this);
    }

    public void preRenderHtml(View view) {
        String rawData = mEditText.getText().toString().trim();
        if (TextUtils.isEmpty(rawData)) {
            Toast.makeText(this, "input data is empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        WebViewController webViewController = new WebViewController(this);
        webViewController.preRenderHtml(getReformatData(rawData), new WebViewController.WebViewListener() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d(TAG, "onPageFinished: " + url);
                Toast.makeText(MainActivity.this, "onPageFinished", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Log.d(TAG, "onReceivedError: " + request.getUrl());
            }
        });
        WebViewController.storeWebViewController(TEMP_KEY, webViewController);
    }


    private String getReformatData(String raw) {
        raw = raw.replaceAll("\\\\\"", "\"");
        raw = raw.replaceAll("\\\n", " ");
        raw = raw.replaceAll("\\\t", " ");
        raw = raw.replaceAll("\\\\r\\\\n", " ");
        return raw;
    }
}
