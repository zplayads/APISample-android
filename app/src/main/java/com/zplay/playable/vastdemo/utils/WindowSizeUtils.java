package com.zplay.playable.vastdemo.utils;

import android.content.Context;

public final class WindowSizeUtils {

    public static int px2dip(Context context, int px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return ((int) (px / scale + 0.5f));
    }

    public static int dip2px(Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return ((int) (dp * scale + 0.5f));
    }
}
