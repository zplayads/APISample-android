package com.zplay.playable.vastdemo.bean;

import java.io.Serializable;
import java.util.List;

public class VideoClicksBean implements Serializable {
    private String ClickThrough;
    private List<ClickTracking> mClickTrackings;

    public String getClickThrough() {
        return ClickThrough;
    }

    public void setClickThrough(String clickThrough) {
        ClickThrough = clickThrough;
    }

    public List<ClickTracking> getClickTrackings() {
        return mClickTrackings;
    }

    public void setClickTrackings(List<ClickTracking> clickTrackings) {
        mClickTrackings = clickTrackings;
    }

    @Override
    public String toString() {
        return "VideoClicks{" +
                "ClickThrough='" + ClickThrough + '\'' +
                ", mClickTrackings=" + mClickTrackings +
                '}';
    }

    public static class ClickTracking implements Serializable {
        private String id;
        private String url;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "ClickTracking{" +
                    "id='" + id + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }

    }
}
