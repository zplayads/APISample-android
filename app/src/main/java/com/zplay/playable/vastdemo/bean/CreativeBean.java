package com.zplay.playable.vastdemo.bean;

import java.io.Serializable;
import java.util.List;

public class CreativeBean implements Serializable {
    private LinearBean linear;
    private String AdID;
    private String sequence;
    private CompanionAdsBean mCompanionAds;

    public LinearBean getLinear() {
        return linear;
    }

    public void setLinear(LinearBean linear) {
        this.linear = linear;
    }

    public String getAdID() {
        return AdID;
    }

    public void setAdID(String adID) {
        AdID = adID;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public CompanionAdsBean getCompanionAds() {
        return mCompanionAds;
    }

    public void setCompanionAds(CompanionAdsBean companionAds) {
        mCompanionAds = companionAds;
    }

    @Override
    public String toString() {
        return "Creative{" +
                "linear=" + linear +
                ", AdID='" + AdID + '\'' +
                ", sequence='" + sequence + '\'' +
                ", mCompanionAds=" + mCompanionAds +
                '}';
    }


}
