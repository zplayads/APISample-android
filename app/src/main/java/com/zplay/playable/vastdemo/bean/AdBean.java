package com.zplay.playable.vastdemo.bean;

import java.io.Serializable;

public class AdBean implements Serializable {
    private String id;
    private InLineBean inLine;

    public InLineBean getInLine() {
        return inLine;
    }

    public void setInLine(InLineBean inLine) {
        this.inLine = inLine;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AdBean{" +
                "id='" + id + '\'' +
                ", inLine=" + inLine +
                '}';
    }

    public String getId() {
        return id;
    }

}
