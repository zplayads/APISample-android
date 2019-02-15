package com.zplay.playable.panosdk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "cccB";

    EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText = findViewById(R.id.edit_text);
    }

    public void showWeb(View view) {

        String trimContent = mEditText.getText().toString().trim();
        if (TextUtils.isEmpty(trimContent)) {
            Toast.makeText(this, "No be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (trimContent.startsWith("http")) {
            WebActivity.launch(this, trimContent);
        } else {
            trimContent = getReformatData(trimContent);
            Log.d(TAG, "showWeb: " + trimContent);
            Log.d(TAG, "showWeb: " + "hello: \"");
            WebActivity.launch(this, trimContent, null, null);
        }

    }

    private String getReformatData(String raw) {
        raw = raw.replaceAll("\\\\\"", "\"");
        raw = raw.replaceAll("\\\n", " ");
        raw = raw.replaceAll("\\\t", " ");
        raw = raw.replaceAll("\\\\r\\\\n", " ");
        return raw;
    }

    public void loadDoc2WebView(View view) {
        String rawData = mEditText.getText().toString().trim();
        if (TextUtils.isEmpty(rawData)) {
            Toast.makeText(this, "input data is empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        WebViewHolder webViewHolder = WebViewHolder.getInstance(this);
        webViewHolder.loadHtmlDoc(this, getReformatData(rawData), new WebViewHolder.WebViewListener() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d(TAG, "onPageFinished: " + url);
            }
        });
    }

    public void showWeb2(View view) {
        Intent i = new Intent(this, WebAttachActivity.class);
        startActivity(i);
    }
}
