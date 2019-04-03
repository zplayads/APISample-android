package com.zplay.playable.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zplay.playable.panosdk.R;
import com.zplay.playable.panosdk.WebViewController;
import com.zplay.playable.utils.UserConfig;
import com.zplay.playable.vastdemo.APIDataFetcher;
import com.zplay.playable.vastdemo.AdModel;

import org.json.JSONObject;

import butterknife.BindView;

import static com.zplay.playable.activity.AdWebViewFunctionActivity2.REQUEST_CODE_ACTIVITY2;
import static com.zplay.playable.panosdk.WebViewController.FUNCTION2;

public class SupportFunctionActivity2 extends ToolBarActivity {

    private String TAG = "SupportFunctionActivity2";

    EditText dataEditText;

    @BindView(R.id.requestButton)
    Button requestButton;

    @BindView(R.id.presentButton)
    Button presentButton;

    TextView logView;

    UserConfig mConfig;

    private String apiRequestDate = "{\n" +
            "  \"version\": \"1.0\",\n" +
            "  \"developer_token\": \"D0C85086-DC66-433E-9304-49EAB171D008\",\n" +
            "  \"need_https\": 1,\n" +
            "  \"support_function\":2,\n" +
            "  \"app\": {\n" +
            "    \"app_id\": \"5C5419C7-A2DE-88BC-A311-C3E7A646F6AF\",\n" +
            "    \"app_name\": \"Android-demo\",\n" +
            "    \"bundle_id\": \"com.playableads.demo\",\n" +
            "    \"version\": \"1.0\",\n" +
            "    \"cat\": \"\"\n" +
            "  },\n" +
            "  \"device\": {\n" +
            "    \"model\": \"Android\",\n" +
            "    \"manufacturer\": \"Samsung\",\n" +
            "    \"brand\": \"Samsung\",\n" +
            "    \"plmn\": \"46001\",\n" +
            "    \"device_type\": \"phone\",\n" +
            "    \"dnt\": 1,\n" +
            "    \"connection_type\": \"wifi\",\n" +
            "    \"carrier\": \"China Mobile\",\n" +
            "    \"orientation\": 0,\n" +
            "    \"mac\": \"28b92b0dde49cff7190841686960c6bc\",\n" +
            "    \"imei\": \"6cd5c276d3f6ce4205dde5b1bf913361\",\n" +
            "    \"android_id\": \"adf3b4675742e11a73db503d1273d860\",\n" +
            "    \"android_adid\": \"600c8c48-afca-42b8-89e5-4612c086c3cc\",\n" +
            "    \"idfa\": \"\",\n" +
            "    \"idfv\": \"\",\n" +
            "    \"openudid\": \"\",\n" +
            "    \"language\": \"zh-Hans-CN\",\n" +
            "    \"os_type\": \"Android\",\n" +
            "    \"os_version\": \"8.0.0\",\n" +
            "    \"screen\": {\n" +
            "      \"width\": 667,\n" +
            "      \"height\": 375,\n" +
            "      \"dpi\": 219,\n" +
            "      \"pxratio\": 2\n" +
            "    },\n" +
            "    \"geo\": {\n" +
            "      \"lat\": 34.567,\n" +
            "      \"lon\": 107.67,\n" +
            "      \"horizontal_accu\": 45,\n" +
            "      \"vertical_accu\": 56\n" +
            "    }\n" +
            "  },\n" +
            "\"user\": {\n" +
            "    \"id\": \"34ddd\",\n" +
            "    \"gender\": \"M\",\n" +
            "    \"age\": 34,\n" +
            "    \"keywords\": [\"auto\", \"cosmetics\", \"perfume\"]\n" +
            "  },\n" +
            "  \"ads\": [\n" +
            "    {\n" +
            "      \"unit_type\": 1,\n" +
            "      \"ad_unit_id\": \"19393189-C4EB-3886-60B9-13B39407064E\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_function1);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        showSettingsButton();
        setSettingName(ToolBarActivity.SUPPORTFUNCTIONSETTING2);
        showUpAction();

        WebView.setWebContentsDebuggingEnabled(true);

        mConfig = UserConfig.getInstance(this);
        dataEditText = (EditText) findViewById(R.id.dataEditText);
        dataEditText.setText(apiRequestDate);
        logView = (TextView) findViewById(R.id.logView);
        requestButton = (Button) findViewById(R.id.requestButton);
        presentButton = (Button) findViewById(R.id.presentButton);

    }

    public void request(View view) {
        if (isRequestDetaNoEmpty(dataEditText.getText().toString().trim())) {
            if (mConfig.isLoadHTMLorURL2()) {
                setInfo("load html ad");
                if (mConfig.isPreRender()) {
                    preRenderHtml(dataEditText.getText().toString(), null);
                } else {
                    show(dataEditText.getText().toString().trim(), null);
                }
                return;
            }

            if (!isRequestDetaValid(dataEditText.getText().toString().trim())) {
                return;
            }

            APIDataFetcher.fetchAPIAdData(dataEditText.getText().toString().trim(), mConfig.isTestModule(), new APIDataFetcher.AdResult() {

                @Override
                public void onResult(final AdModel adModel) {
                    SupportFunctionActivity2.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (TextUtils.isEmpty(adModel.getHtmlData()) || adModel.getHtmlData().equals("")) {
                                setInfo("request failed, html data is null");
                                return;
                            }
                            setInfo("request SUCCESS");
                            if (!mConfig.isPreRender2()) {
                                setInfo("present");
                                show(adModel.getHtmlData(), adModel.getTargetUrl());
                            } else {
                                setInfo("pre loading");
                                preRenderHtml(adModel.getHtmlData(), adModel.getTargetUrl());
                            }
                        }
                    });
                }
            });
        } else {
            if (mConfig.isLoadHTMLorURL2()) {
                setInfo("load html error, html is null");
            } else {
                setInfo("request Ad error, request data is null");
            }
        }
    }


    public void show(final String html, String targetUrl) {
        WebViewController webViewController = new WebViewController(this, 2);
        webViewController.setHtmlData(getReformatData(html));
        webViewController.setTargetUrl(targetUrl);
        WebViewController.storeWebViewController(FUNCTION2, webViewController);

        AdWebViewFunctionActivity2.launch(this);

    }

    public void present(View view) {
        WebViewController webViewController = WebViewController.getWebViewController(FUNCTION2);
        if (webViewController == null) {
            setInfo("present failed, WebView not initiated yet");
            return;
        }
        if (mConfig.isPreRender2() && !webViewController.isCachedHtmlData()) {
            setInfo("present failed, WebView not prepared yet");
            return;
        }

        setInfo("present");
        AdWebViewFunctionActivity2.launch(this);
    }

    public void preRenderHtml(String html, String targetUrl) {

        WebViewController webViewController = new WebViewController(this, 2);
        webViewController.setTargetUrl(targetUrl);
        webViewController.preRenderHtml(getReformatData(html), new WebViewController.WebViewListener() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d(TAG, "onPageFinished: " + url);
                setInfo("prepared");
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Log.d(TAG, "onReceivedError: " + request.getUrl());
                setInfo("preparedFailed");
            }
        });
        WebViewController.storeWebViewController(FUNCTION2, webViewController);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_ACTIVITY2:
                int isClose = data.getIntExtra("close", 0);
                if (isClose == 1) {
                    setInfo("close");
                }
                break;
            default:
                break;
        }
    }


    public static void launch(Context ctx) {
        Intent i = new Intent(ctx, SupportFunctionActivity2.class);
        ctx.startActivity(i);
    }

    private boolean isRequestDetaNoEmpty(String data) {
        return !TextUtils.isEmpty(data) && !data.equals("");
    }

    private boolean isRequestDetaValid(String data) {

        try {
            JSONObject requestData = new JSONObject(data);
            int support_function = requestData.getInt("support_function");
            if (support_function == 2) {
                return true;
            }
            setInfo("request error : function != 2");
            return false;
        } catch (Exception e) {
            setInfo("request error : " + e.toString());
            return false;
        }
    }

    private String getReformatData(String raw) {
        raw = raw.replaceAll("\\\\\"", "\"");
        raw = raw.replaceAll("\\\n", " ");
        raw = raw.replaceAll("\\\t", " ");
        raw = raw.replaceAll("\\\\r\\\\n", " ");
        return raw;
    }

    private void setInfo(final String msg) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (logView != null) {
                    logView.append(msg + "\n");
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mConfig.isLoadHTMLorURL2()) {
            requestButton.setText("load");
        } else {
            requestButton.setText("request");
        }
    }
}
