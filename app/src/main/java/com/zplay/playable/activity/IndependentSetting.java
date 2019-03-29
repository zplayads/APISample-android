package com.zplay.playable.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.LinearLayout;

import com.zplay.playable.panosdk.R;
import com.zplay.playable.utils.UserConfig;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description:
 * <p>
 * Created by lgd on 2018/6/14.
 */

public class IndependentSetting extends ToolBarActivity {

    private static final String EXTRA_TAG = "code";

    @BindView(R.id.load_html)
    SwitchCompat mLoadHtmlSwitch;

    @BindView(R.id.pre_render)
    SwitchCompat mPreRenderSwitch;

    @BindView(R.id.support_mraid)
    SwitchCompat mSupportMraidSwitch;

    @BindView(R.id.support_a_tag)
    SwitchCompat mSupportTagSwitch;

    @BindView(R.id.support_mraid_layout)
    LinearLayout mSupportMraidLayout;

    @BindView(R.id.support_a_tag_layout)
    LinearLayout mSupportATagLayout;

    @BindView(R.id.line)
    View mLineView;

    UserConfig mConfig;

    private int mFunctionCode = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_independent_setting);
        ButterKnife.bind(this);
        showUpAction();
        mConfig = UserConfig.getInstance(this);
        Intent intent = getIntent();
        mFunctionCode = intent.getIntExtra(EXTRA_TAG, -1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mFunctionCode == 2) {
            mLoadHtmlSwitch.setChecked(mConfig.isLoadHTMLorURL2());
            mPreRenderSwitch.setChecked(mConfig.isPreRender2());
            mSupportMraidLayout.setVisibility(View.INVISIBLE);
            mSupportATagLayout.setVisibility(View.INVISIBLE);
            mLineView.setVisibility(View.INVISIBLE);
        }

        if (mFunctionCode == 1) {
            mLoadHtmlSwitch.setChecked(mConfig.isLoadHTMLorURL());
            mPreRenderSwitch.setChecked(mConfig.isPreRender());
            mSupportMraidSwitch.setChecked(mConfig.isSupportMraid());
            mSupportTagSwitch.setChecked(mConfig.isSupportTag());
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mFunctionCode == 1) {
            mConfig.setPreRender(mPreRenderSwitch.isChecked());
            mConfig.setSupportMraid(mSupportMraidSwitch.isChecked());
            mConfig.setSupportTag(mSupportTagSwitch.isChecked());
            mConfig.setLoadHTMLorURL(mLoadHtmlSwitch.isChecked());
        } else {
            mConfig.setPreRender2(mPreRenderSwitch.isChecked());
            mConfig.setLoadHTMLorURL2(mLoadHtmlSwitch.isChecked());
        }
    }


    public static void launch(Context ctx, int code) {
        Intent i = new Intent(ctx, IndependentSetting.class);
        i.putExtra(EXTRA_TAG, code);
        ctx.startActivity(i);
    }
}
