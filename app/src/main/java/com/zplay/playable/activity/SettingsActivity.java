package com.zplay.playable.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;

import com.zplay.playable.panosdk.R;
import com.zplay.playable.utils.UserConfig;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description:
 * <p>
 * Created by lgd on 2018/6/14.
 */

public class SettingsActivity extends ToolBarActivity {
    @BindView(R.id.testmodule)
    SwitchCompat mTestModuleSwitch;

    UserConfig mConfig;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        showUpAction();
        mConfig = UserConfig.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTestModuleSwitch.setChecked(mConfig.isTestModule());
    }


    @Override
    protected void onPause() {
        super.onPause();
        mConfig.setTestModule(mTestModuleSwitch.isChecked());
    }

    public static void launch(Context ctx) {
        Intent i = new Intent(ctx, SettingsActivity.class);
        ctx.startActivity(i);
    }
}
