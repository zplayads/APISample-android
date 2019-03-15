package com.zplay.playable.vastdemo.bean;

import java.io.Serializable;
import java.util.List;

public class LinearBean implements Serializable {
    private String Duration;
    private TrackingEventsBean mTrackingEvents;
    private VideoClicksBean mVideoClicks;
    private MediaFilesBean mMediaFiles;


    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public TrackingEventsBean getTrackingEvents() {
        return mTrackingEvents;
    }

    public void setTrackingEvents(TrackingEventsBean trackingEvents) {
        mTrackingEvents = trackingEvents;
    }

    public VideoClicksBean getVideoClicks() {
        return mVideoClicks;
    }

    public void setVideoClicks(VideoClicksBean videoClicks) {
        mVideoClicks = videoClicks;
    }

    public MediaFilesBean getMediaFiles() {
        return mMediaFiles;
    }

    public void setMediaFiles(MediaFilesBean mediaFiles) {
        mMediaFiles = mediaFiles;
    }

    @Override
    public String toString() {
        return "Linear{" +
                "Duration=" + Duration +
                ", mTrackingEvents=" + mTrackingEvents +
                ", mVideoClicks=" + mVideoClicks +
                ", mMediaFiles=" + mMediaFiles +
                '}';
    }

}
