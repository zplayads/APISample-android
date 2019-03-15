package com.zplay.playable.panosdk;

import android.content.Intent;
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

import com.zplay.playable.vastdemo.APIDataFetcher;
import com.zplay.playable.vastdemo.AdModel;
import com.zplay.playable.vastdemo.activity.VastVideoFullScreenActivity;
import com.zplay.playable.vastdemo.bean.MediaFilesBean;
import com.zplay.playable.vastdemo.bean.VAST;
import com.zplay.playable.vastdemo.utils.BeanHelper;
import com.zplay.playable.vastdemo.utils.VASTHelper;
import com.zplay.playable.vastdemo.file.MediaDownload;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

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
    public VAST vastDate;
    public String videoPath;
    private boolean vastIsLoding;
    public void loadVastAd(View view){
        try {
             if(vastIsLoding){
                 showToast("VAST 广告正在加载，请稍后");
                 return;
             }
            vastIsLoding = true;
            APIDataFetcher.fetchAdData(new APIDataFetcher.AdResult() {
                @Override
                public void onResult(AdModel adModel) {
                    if(adModel != null && !adModel.equals("") && adModel.getAdm() != null){
                        try {
                            VASTHelper.getInstance().parseVAST(new ByteArrayInputStream(adModel.getAdm().getBytes("UTF-8")), new VASTHelper.ParseVastCallback() {
                                @Override
                                public void onResult(VAST vast) {
                                    vastDate = vast;
                                    showToast("VAST 开始加载视频");
                                    MediaFilesBean.MediaFile mediaFile = BeanHelper.getMediaFiles(vast).getMediaFiles().get(0);
                                    Log.i(TAG, "vast video start download videoUrl ：" + mediaFile.getVideoUrl());
                                    new MediaDownload(MainActivity.this) {
                                        @Override
                                        protected void onPostExecute(String dataPath) {
                                            super.onPostExecute(dataPath);
                                            vastIsLoding = false;
                                            if (dataPath != null) {
                                                Log.i(TAG, "vast video loaded dataPath：" + dataPath);
                                                videoPath = dataPath;
                                                showToast("VAST 视频加载完成");
                                            } else {
                                                Log.i(TAG, "vast video load failed dataPath：");
                                                videoPath = dataPath;
                                                showToast("VAST 视频加载失败");
                                            }
                                        }
                                    }.execute(mediaFile.getVideoUrl());
                                }
                            });
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            vastIsLoding = false;
                        }
                    }else{
                        showToast("服务端返回VAST数据异常");
                        vastIsLoding = false;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            vastIsLoding = false;
        }
    }

    public void showVastAd(View view){
        if(vastDate != null && videoPath != null){
            showToast("开始展示VAST视频");
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, VastVideoFullScreenActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("vast", vastDate);
            bundle.putString("path", videoPath);
            intent.putExtras(bundle);
            MainActivity.this.startActivity(intent);
        }else{
            Log.e(TAG, "show VAST ad file vastDate is null ？ ：" + (vastDate != null) + ",videoPath : " + videoPath);
            showToast("展示VAST失败");
        }
    }

    public void showToast(final String msg){
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getReformatData(String raw) {
        raw = raw.replaceAll("\\\\\"", "\"");
        raw = raw.replaceAll("\\\n", " ");
        raw = raw.replaceAll("\\\t", " ");
        raw = raw.replaceAll("\\\\r\\\\n", " ");
        return raw;
    }
}
