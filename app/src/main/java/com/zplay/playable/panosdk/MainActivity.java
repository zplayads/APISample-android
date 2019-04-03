package com.zplay.playable.panosdk;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zplay.playable.activity.SupportFunctionActivity1;
import com.zplay.playable.activity.SupportFunctionActivity2;
import com.zplay.playable.activity.SupportVAST;
import com.zplay.playable.activity.ToolBarActivity;
import com.zplay.playable.utils.UserConfig;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zplay.playable.panosdk.MainActivity.AdType.SUPPORTFUNCTION1;
import static com.zplay.playable.panosdk.MainActivity.AdType.SUPPORTFUNCTION2;
import static com.zplay.playable.panosdk.MainActivity.AdType.SUPPORTVAST3;

public class MainActivity extends ToolBarActivity {
    private static final String TAG = "MainActivityTag";
    private static Activity mActivity;

    enum AdType {
        SUPPORTFUNCTION1("Support Function=1", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SupportFunctionActivity1.launch(mActivity);
            }
        }),
        SUPPORTFUNCTION2("Support Function=2", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SupportFunctionActivity2.launch(mActivity);

            }
        }),
        SUPPORTVAST3("Support VAST 3.0", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SupportVAST.launch(mActivity);
            }
        });

        private String name;
        private View.OnClickListener clickListener;

        AdType(String name, View.OnClickListener clickListener) {
            this.name = name;
            this.clickListener = clickListener;
        }
    }

    private static List<AdType> sSupportArray =
            Collections.unmodifiableList(Arrays.asList(SUPPORTFUNCTION1, SUPPORTFUNCTION2, SUPPORTVAST3));

    @BindView(R.id.ad_list)
    RecyclerView mAdList;

    UserConfig mConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserConfig.getInstance(this);
        setContentView(R.layout.activity_main);
        mActivity = this;

        mConfig = UserConfig.getInstance(this);
        showSettingsButton();
        setSettingName(ToolBarActivity.DEMO_SETTING);
        ButterKnife.bind(this);
        mAdList.setAdapter(new AdListAdapter(sSupportArray));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(mAdList.getContext(), linearLayoutManager.getOrientation());
        mAdList.setLayoutManager(linearLayoutManager);
        mAdList.addItemDecoration(dividerItemDecoration);
        resetConfig();
    }

    static class AdListAdapter extends RecyclerView.Adapter<AdListVH> {

        List<AdType> mData;

        AdListAdapter(List<AdType> data) {
            mData = data;
        }


        @Override
        public AdListVH onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ad_list, parent, false);
            return new AdListVH(view);
        }

        @Override
        public void onBindViewHolder(AdListVH holder, int position) {
            holder.adName.setText(mData.get(position).name);
            holder.itemView.setOnClickListener(mData.get(position).clickListener);
        }

        @Override
        public int getItemCount() {
            if (mData != null) {
                return mData.size();
            }
            return 0;
        }
    }

    static class AdListVH extends RecyclerView.ViewHolder {
        @BindView(R.id.ad_name)
        TextView adName;

        AdListVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void resetConfig() {
        mConfig.setLoadHTMLorURL(false);
        mConfig.setTestModule(false);
        mConfig.setPreRender(false);
        mConfig.setUseWebview(false);
        mConfig.setSupportMraid(false);

        mConfig.setLoadHTMLorURL2(false);
        mConfig.setPreRender2(false);
        mConfig.setSupportMraid2(false);

        mConfig.setSupportTag(false);
    }

}
