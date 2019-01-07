package com.zplay.playable.panosdk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
            trimContent = trimContent.replaceAll("\\\\\"", "\"");
            trimContent = trimContent.replaceAll("\\\n", " ");
            trimContent = trimContent.replaceAll("\\\t", " ");
            trimContent = trimContent.replaceAll("\\\\r\\\\n", " ");
            Log.d(TAG, "showWeb: " + trimContent);
            Log.d(TAG, "showWeb: " + "hello: \"");
            WebActivity.launch(this, trimContent, null, null);
        }

    }
}
