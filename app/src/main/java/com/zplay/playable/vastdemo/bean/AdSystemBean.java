package com.zplay.playable.vastdemo.bean;

import java.io.Serializable;

public class AdSystemBean implements Serializable {
    private String version;
    private String adTitle;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAdTitle() {
        return adTitle;
    }

    public void setAdTitle(String adTitle) {
        this.adTitle = adTitle;
    }

    @Override
    public String toString() {
        return "AdSystem{" +
                "version='" + version + '\'' +
                ", adTitle='" + adTitle + '\'' +
                '}';
    }
}
