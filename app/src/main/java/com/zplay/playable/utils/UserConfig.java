package com.zplay.playable.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Description:
 * <p>
 * Created by lgd on 2018/6/14.
 */

public class UserConfig {
    private static final String SP_NAME = "zp.apiuser.config";
    private static final String USEWEBVIEW = "USEWEBVIEW";
    private static final String TESTMODULE = "TEXTMODULE";
    private static final String LOADHTMLORURL= "LOADHTMLORURL";
    private static final String PRERENDER = "PRERENDER";
    private static final String SUPPORTMRAID = "SUPPORTMRAID";
    private static final String SUPPORTTAG = "SUPPORTTAG";

    private static final String LOADHTMLORURL2= "LOADHTMLORURL2";
    private static final String PRERENDER2 = "PRERENDER2";

    private SharedPreferences.Editor editor;
    private SharedPreferences sp;
    private static UserConfig adConfig;

    public synchronized static UserConfig getInstance(Context ctx) {
        if (adConfig == null) {
            adConfig = new UserConfig(ctx);
        }
        return adConfig;
    }

    @SuppressLint("CommitPrefEdits")
    private UserConfig(Context ctx) {
        sp = ctx.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public boolean setUseWebview(boolean useWebView) {
        return editor.putBoolean(USEWEBVIEW, useWebView).commit();
    }

    public boolean isUseWebview() {
        return sp.getBoolean(USEWEBVIEW, false);
    }

    public boolean setPreRender(boolean preRender) {
        return editor.putBoolean(PRERENDER, preRender).commit();
    }

    public boolean isPreRender() {
        return sp.getBoolean(PRERENDER, false);
    }

    public boolean setPreRender2(boolean preRender) {
        return editor.putBoolean(PRERENDER2, preRender).commit();
    }

    public boolean isPreRender2() {
        return sp.getBoolean(PRERENDER2, false);
    }

    public boolean setTestModule(boolean testModule) {
        return editor.putBoolean(TESTMODULE, testModule).commit();
    }

    public boolean isTestModule() {
        return sp.getBoolean(TESTMODULE, false);
    }

    public boolean isSupportMraid() {
        return sp.getBoolean(SUPPORTMRAID, false);
    }

    public boolean setSupportMraid(boolean supportMraid) {
        return editor.putBoolean(SUPPORTMRAID, supportMraid).commit();
    }

    public boolean isSupportTag() {
        return sp.getBoolean(SUPPORTTAG, false);
    }

    public boolean setSupportTag(boolean supporTag) {
        return editor.putBoolean(SUPPORTTAG, supporTag).commit();
    }

    public boolean isLoadHTMLorURL() {
        return sp.getBoolean(LOADHTMLORURL, false);
    }

    public boolean setLoadHTMLorURL(boolean loadHTMLorURL) {
        return editor.putBoolean(LOADHTMLORURL, loadHTMLorURL).commit();
    }

    public boolean isLoadHTMLorURL2() {
        return sp.getBoolean(LOADHTMLORURL2, false);
    }

    public boolean setLoadHTMLorURL2(boolean loadHTMLorURL2) {
        return editor.putBoolean(LOADHTMLORURL2, loadHTMLorURL2).commit();
    }
}
