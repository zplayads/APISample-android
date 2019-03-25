package com.zplay.playable.vastdemo.bean;

import java.io.Serializable;
import java.util.List;

public class CreativesBean implements Serializable {
    private List<CreativeBean> creativeList;

    public List<CreativeBean> getCreativeList() {
        return creativeList;
    }

    public void setCreativeList(List<CreativeBean> creativeList) {
        this.creativeList = creativeList;
    }

    @Override
    public String toString() {
        return "Creatives{" +
                "creativeList=" + creativeList +
                '}';
    }


}
