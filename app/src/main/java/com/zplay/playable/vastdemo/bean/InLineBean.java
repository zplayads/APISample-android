package com.zplay.playable.vastdemo.bean;

import java.io.Serializable;

public class InLineBean implements Serializable {
    private AdSystemBean adSystem;
    private String adTilte;
    private String impression;
    private CreativesBean creatives;

    public AdSystemBean getAdSystem() {
        return adSystem;
    }

    public void setAdSystem(AdSystemBean adSystem) {
        this.adSystem = adSystem;
    }


    public String getAdTilte() {
        return adTilte;
    }

    public void setAdTilte(String adTilte) {
        this.adTilte = adTilte;
    }

    public String getImpression() {
        return impression;
    }

    public void setImpression(String impression) {
        this.impression = impression;
    }

    public CreativesBean getCreatives() {
        return creatives;
    }

    public void setCreatives(CreativesBean creatives) {
        this.creatives = creatives;
    }

    @Override
    public String toString() {
        return "InLine{" +
                "impression='" + impression + '\'' +
                ", creatives=" + creatives +
                '}';
    }
}
