package com.zplay.playable.vastdemo.bean;

import java.io.Serializable;
import java.util.List;

public class TrackingEventsBean implements Serializable {
    private List<Tracking> mTrackingList;

    public List<Tracking> getTrackingList() {
        return mTrackingList;
    }

    public void setTrackingList(List<Tracking> trackingList) {
        mTrackingList = trackingList;
    }

    @Override
    public String toString() {
        return "TrackingEvents{" +
                "mTrackingList=" + mTrackingList +
                '}';
    }

    public static class Tracking implements Serializable {
        private String event;
        private String url;

        public String getEvent() {
            return event;
        }

        public void setEvent(String event) {
            this.event = event;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "Tracking{" +
                    "event='" + event + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }
}