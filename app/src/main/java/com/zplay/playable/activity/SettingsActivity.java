package com.zplay.playable.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;

import com.zplay.playable.panosdk.R;
import com.zplay.playable.utils.UserConfig;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description:
 * <p>
 * Created by lgd on 2018/6/14.
 */

public class SettingsActivity extends ToolBarActivity {
    private static String TAG = "SettingsActivity";

    @BindView(R.id.useruiwebview)
    SwitchCompat useruiwebview;

    @BindView(R.id.testmodule)
    SwitchCompat testmodule;

    UserConfig mConfig;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        showUpAction();
        mConfig = UserConfig.getInstance(this);

        useruiwebview.setChecked(mConfig.isUseWebview());
        testmodule.setChecked(mConfig.isTestModule());

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "user ui webview: " + useruiwebview.isChecked());
        Log.i(TAG, "test module: " + testmodule.isChecked());
        mConfig.setUseWebview(useruiwebview.isChecked());
        mConfig.setTestModule(testmodule.isChecked());
    }


    private boolean myDeleteFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return true;
        }
        if (!file.isDirectory()) {
            return file.delete();
        }

        File[] files = file.listFiles();
        for (File f : files) {
            myDeleteFile(f.getPath());
        }
        return file.delete();
    }

    public static void launch(Context ctx) {
        Intent i = new Intent(ctx, SettingsActivity.class);
        ctx.startActivity(i);
    }
}
