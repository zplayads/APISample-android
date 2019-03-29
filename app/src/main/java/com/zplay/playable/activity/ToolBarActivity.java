package com.zplay.playable.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.zplay.playable.panosdk.R;

/**
 * Description:
 * <p>
 * Created by lgd on 2018/9/26.
 */

public abstract class ToolBarActivity extends AppCompatActivity {
    private static String TAG = "ToolBarActivity";

    public static int DEMO_SETTING = 0;
    public static int SUPPORTFUNCTIONSETTING1 = 1;
    public static int SUPPORTFUNCTIONSETTING2 = 2;
    public static int SUPPORTVASTSETTING = 3;

    private int settingName = -1;


    private FrameLayout mContentView;
    private Toolbar mToolbar;
    private boolean showSettingButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_toolbar);
        mContentView = (FrameLayout) findViewById(R.id.content_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setElevation(10);
        }
        setSupportActionBar(mToolbar);

    }

    protected void showUpAction() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }


    protected void showSettingsButton() {
        showSettingButton = true;
    }

    protected void setSettingName(int settingName) {
        this.settingName = settingName;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (showSettingButton) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Log.i(TAG, "onOptionsItemSelected: " + settingName);
                if (settingName == DEMO_SETTING) {
                    SettingsActivity.launch(this);
                } else if (settingName == SUPPORTFUNCTIONSETTING1) {
                    IndependentSetting.launch(this, 1);
                } else if (settingName == SUPPORTFUNCTIONSETTING2) {
                    IndependentSetting.launch(this, 2);
                } else if (settingName == SUPPORTVASTSETTING) {

                }

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }


    }

    @Override
    public void setContentView(int layoutResID) {
        mContentView.addView(LayoutInflater.from(this).inflate(layoutResID, mContentView, false));
    }

    @Override
    public void setContentView(View view) {
        mContentView.addView(view);
    }
}
