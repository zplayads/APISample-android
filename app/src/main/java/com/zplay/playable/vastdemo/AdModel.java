package com.zplay.playable.vastdemo;


public class AdModel {
    private String mAppMarketPackage;
    private String mTargetPackage;
    private String mTargetUrl;
    private String mHtmlData;
    private String mAdm;

    AdModel(String htmlData) {
        mHtmlData = htmlData;
    }

    public String getAppMarketPackage() {
        return mAppMarketPackage;
    }

    public void setAppMarketPackage(String appMarketPackage) {
        mAppMarketPackage = appMarketPackage;
    }

    public String getTargetPackage() {
        return mTargetPackage;
    }

    public void setTargetPackage(String targetPackage) {
        mTargetPackage = targetPackage;
    }

    public String getTargetUrl() {
        return mTargetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        mTargetUrl = targetUrl;
    }

    public String getHtmlData() {
        return mHtmlData;
    }

    public void setAdm(String adm) {
        mAdm = adm;
    }

    public String getAdm() {
        return mAdm;
    }
}
