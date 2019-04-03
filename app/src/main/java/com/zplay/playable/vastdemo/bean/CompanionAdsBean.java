package com.zplay.playable.vastdemo.bean;

import java.io.Serializable;
import java.util.List;

public class CompanionAdsBean implements Serializable {
    private Companion companion;

    public Companion getCompanion() {
        return companion;
    }

    public void setCompanion(Companion companion) {
        this.companion = companion;
    }

    @Override
    public String toString() {
        return "CompanionAds{" +
                "companion=" + companion +
                '}';
    }

    public static class Companion implements Serializable {
        private String width;
        private String height;
        private String HTMLResource;
        private TrackingEvents mTrackingEvents;
        private String CompanionClickTracking;

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getHTMLResource() {
            return HTMLResource;
        }

        public void setHTMLResource(String HTMLResource) {
            this.HTMLResource = HTMLResource;
        }

        public TrackingEvents getTrackingEvents() {
            return mTrackingEvents;
        }

        public void setTrackingEvents(TrackingEvents trackingEvents) {
            mTrackingEvents = trackingEvents;
        }

        public String getCompanionClickTracking() {
            return CompanionClickTracking;
        }

        public void setCompanionClickTracking(String companionClickTracking) {
            CompanionClickTracking = companionClickTracking;
        }

        @Override
        public String toString() {
            return "Companion{" +
                    "width='" + width + '\'' +
                    ", height='" + height + '\'' +
                    ", HTMLResource='" + HTMLResource + '\'' +
                    ", mTrackingEvents=" + mTrackingEvents +
                    ", CompanionClickTracking='" + CompanionClickTracking + '\'' +
                    '}';
        }

        public static class TrackingEvents {
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

            public static class Tracking {
                private String event;
                private String url;

                public void setEvent(String event) {
                    this.event = event;
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
    }
}
