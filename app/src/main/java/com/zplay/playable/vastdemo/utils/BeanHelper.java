package com.zplay.playable.vastdemo.utils;

import com.zplay.playable.vastdemo.bean.AdBean;
import com.zplay.playable.vastdemo.bean.CreativeBean;
import com.zplay.playable.vastdemo.bean.CreativesBean;
import com.zplay.playable.vastdemo.bean.InLineBean;
import com.zplay.playable.vastdemo.bean.LinearBean;
import com.zplay.playable.vastdemo.bean.MediaFilesBean;
import com.zplay.playable.vastdemo.bean.TrackingEventsBean;
import com.zplay.playable.vastdemo.bean.VAST;
import com.zplay.playable.vastdemo.bean.VideoClicksBean;

public class BeanHelper {

    public static MediaFilesBean getMediaFiles(VAST vast){
        LinearBean linear = getLinear(vast);
        return linear.getMediaFiles();
    }

    public static VideoClicksBean getVideoClicks(VAST vast) {
        LinearBean linear = getLinear(vast);
        return linear.getVideoClicks();
    }

    public static TrackingEventsBean getTrackingEvents(VAST vast) {
        LinearBean linear = getLinear(vast);
        return linear.getTrackingEvents();
    }

    public static LinearBean getLinear(VAST vast) {
        CreativeBean fristCreative = getFristCreative(vast);
        return fristCreative.getLinear();
    }

    public static CreativeBean getFristCreative(VAST vast) {
        CreativesBean creatives = getCreatives(vast);
        return creatives.getCreativeList().get(0);
    }

    public static CreativesBean getCreatives(VAST vast) {
        InLineBean inLine = getInLine(vast);
        return inLine.getCreatives();
    }

    public static InLineBean getInLine(VAST vast) {
        AdBean ad = getAdBean(vast);
        return ad.getInLine();
    }

    public static AdBean getAdBean(VAST vast) {
        return vast.getAd();
    }
}
