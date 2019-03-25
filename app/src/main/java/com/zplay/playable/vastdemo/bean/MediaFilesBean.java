package com.zplay.playable.vastdemo.bean;

import java.io.Serializable;
import java.util.List;

public class MediaFilesBean implements Serializable {
    private List<MediaFile> mMediaFiles;

    public List<MediaFile> getMediaFiles() {
        return mMediaFiles;
    }

    public void setMediaFiles(List<MediaFile> mediaFiles) {
        mMediaFiles = mediaFiles;
    }

    @Override
    public String toString() {
        return "MediaFiles{" +
                "mMediaFiles=" + mMediaFiles +
                '}';
    }

    public static class MediaFile implements Serializable{
        private String bitrate;
        private String delivery;
        private String height;
        private String width;
        private String id;
        private String type;
        private String videoUrl;

        public String getBitrate() {
            return bitrate;
        }

        public void setBitrate(String bitrate) {
            this.bitrate = bitrate;
        }

        public String getDelivery() {
            return delivery;
        }

        public void setDelivery(String delivery) {
            this.delivery = delivery;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        @Override
        public String toString() {
            return "MediaFile{" +
                    "bitrate='" + bitrate + '\'' +
                    ", delivery='" + delivery + '\'' +
                    ", height='" + height + '\'' +
                    ", width='" + width + '\'' +
                    ", id='" + id + '\'' +
                    ", type='" + type + '\'' +
                    ", videoUrl='" + videoUrl + '\'' +
                    '}';
        }
    }
}
