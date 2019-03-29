package com.zplay.playable.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
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
    private static String TAG = "IndependentSetting";


    @BindView(R.id.loadhtml)
    SwitchCompat loadhtml;

    @BindView(R.id.prerender)
    SwitchCompat prerender;

    @BindView(R.id.supportmraid)
    SwitchCompat supportMraid;

    @BindView(R.id.supporttag)
    SwitchCompat supportTag;

    @BindView(R.id.supportmraid_layout)
    LinearLayout supportmraid_layout;

    @BindView(R.id.supporttag_layout)
    LinearLayout supporttag_layout;

    @BindView(R.id.line)
    View line;

    UserConfig mConfig;

    private int functionCode = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_independent_setting);
        ButterKnife.bind(this);
        showUpAction();
        mConfig = UserConfig.getInstance(this);
        Intent intent = getIntent();
        functionCode = intent.getIntExtra("code", -1);
        if (functionCode == 2) {
            loadhtml.setChecked(mConfig.isLoadHTMLorURL2());
            prerender.setChecked(mConfig.isPreRender2());
            supportmraid_layout.setVisibility(View.INVISIBLE);
            supporttag_layout.setVisibility(View.INVISIBLE);
            line.setVisibility(View.INVISIBLE);
        }

        if (functionCode == 1) {
            loadhtml.setChecked(mConfig.isLoadHTMLorURL());
            prerender.setChecked(mConfig.isPreRender());
            supportMraid.setChecked(mConfig.isSupportMraid());
            supportTag.setChecked(mConfig.isSupportTag());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "prerender: " + prerender.isChecked());
        Log.i(TAG, "supportMraid: " + supportMraid.isChecked());
        Log.i(TAG, "supportTag: " + supportTag.isChecked());
        if (functionCode == 1) {
            mConfig.setPreRender(prerender.isChecked());
            mConfig.setSupportMraid(supportMraid.isChecked());
            mConfig.setSupportTag(supportTag.isChecked());
            mConfig.setLoadHTMLorURL(loadhtml.isChecked());
        } else {
            mConfig.setPreRender2(prerender.isChecked());
            mConfig.setLoadHTMLorURL2(loadhtml.isChecked());
        }
    }


    public static void launch(Context ctx, int code) {
        Intent i = new Intent(ctx, IndependentSetting.class);
        i.putExtra("code", code);
        ctx.startActivity(i);
    }
}
