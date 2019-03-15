package com.zplay.playable.vastdemo;


public class AdModel {
    private String mAppMarketPackage;
    private String mTargetPackage;
    private String mHtmlData;
    private String mAdm;

    AdModel(String htmlData) {
        mHtmlData = htmlData;
    }

    String getAppMarketPackage() {
        return mAppMarketPackage;
    }

    void setAppMarketPackage(String appMarketPackage) {
        mAppMarketPackage = appMarketPackage;
    }

    String getTargetPackage() {
        return mTargetPackage;
    }

    void setTargetPackage(String targetPackage) {
        mTargetPackage = targetPackage;
    }

    String getHtmlData() {
        return mHtmlData;
    }

    public void setAdm(String adm) {
        mAdm = adm;
    }

    public String getAdm() {
        return mAdm;
    }
}
