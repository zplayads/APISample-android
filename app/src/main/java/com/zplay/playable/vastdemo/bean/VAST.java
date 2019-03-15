package com.zplay.playable.vastdemo.bean;

import java.io.Serializable;

public class VAST implements Serializable {
    private String VASTVersion;
    private AdBean ad;

    @Override
    public String toString() {
        return "VAST{" +
                "VASTVersion='" + VASTVersion + '\'' +
                ", ad=" + ad +
                '}';
    }

    public String getVASTVersion() {
        return VASTVersion;
    }

    public void setVASTVersion(String VASTVersion) {
        this.VASTVersion = VASTVersion;
    }

    public AdBean getAd() {
        return ad;
    }

    public void setAd(AdBean ad) {
        this.ad = ad;
    }

}
