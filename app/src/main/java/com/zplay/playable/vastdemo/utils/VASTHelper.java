package com.zplay.playable.vastdemo.utils;

import android.util.Xml;

import com.zplay.playable.vastdemo.bean.AdBean;
import com.zplay.playable.vastdemo.bean.AdSystemBean;
import com.zplay.playable.vastdemo.bean.CompanionAdsBean;
import com.zplay.playable.vastdemo.bean.CreativeBean;
import com.zplay.playable.vastdemo.bean.CreativesBean;
import com.zplay.playable.vastdemo.bean.InLineBean;
import com.zplay.playable.vastdemo.bean.LinearBean;
import com.zplay.playable.vastdemo.bean.MediaFilesBean;
import com.zplay.playable.vastdemo.bean.TrackingEventsBean;
import com.zplay.playable.vastdemo.bean.VAST;
import com.zplay.playable.vastdemo.bean.VideoClicksBean;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class VASTHelper {

    private static VASTHelper sVastHelper;
    private VAST mVAST;

    private VASTHelper() {
    }

    public static VASTHelper getInstance() {
        if (sVastHelper == null) {
            sVastHelper = new VASTHelper();
        }
        return sVastHelper;
    }

    public VAST getVAST() {
        return mVAST;
    }

    public void parseVAST(final InputStream open, final ParseVastCallback parseVastCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    XmlPullParser xmlPullParser = Xml.newPullParser();
                    xmlPullParser.setInput(open, "utf-8");
                    VAST vast = null;
                    List<CreativeBean> creativeList = null;
                    int count = 0;
                    int type = xmlPullParser.getEventType();
                    while (type != XmlPullParser.END_DOCUMENT) {
                        switch (type) {
                            case XmlPullParser.START_DOCUMENT:
                                vast = new VAST();
                                count = 0;
                                creativeList = new ArrayList<>();
                                break;
                            case XmlPullParser.START_TAG:
                                //获取开始标签的名字
                                String starttgname = xmlPullParser.getName();
                                creativeList = parseVAST(xmlPullParser, vast, creativeList, count, starttgname);
                                break;
                            case XmlPullParser.END_TAG:
                                if ("vast".equalsIgnoreCase(xmlPullParser.getName())) {
                                    mVAST = vast;
                                    parseVastCallback.onResult(mVAST);
                                } else if (equalsName("TrackingEvents", xmlPullParser.getName())) {
                                    count++;
                                }
                                break;
                        }//细节：
                        type = xmlPullParser.next();
                    }
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                    parseVastCallback.onResult(null);
                } catch (IOException e) {
                    e.printStackTrace();
                    parseVastCallback.onResult(null);
                }
            }
        }).start();
    }

    private List<CreativeBean> parseVAST(XmlPullParser xmlPullParser, VAST vast, List<CreativeBean> creativeList, int count, String starttgname) throws IOException, XmlPullParserException {
        if (equalsName(starttgname, "vast")) {
            vast.setVASTVersion(xmlPullParser.getAttributeValue(0));
        } else if (equalsName(starttgname, "ad")) {
            AdBean adBean = new AdBean();
            adBean.setId(xmlPullParser.getAttributeValue(0));
            vast.setAd(adBean);
        } else if (equalsName(starttgname, "AdSystem")) {
            InLineBean inLine = getInLine(vast);
            AdSystemBean adSystem = new AdSystemBean();
            adSystem.setVersion(xmlPullParser.getAttributeValue(0));
            adSystem.setAdTitle(xmlPullParser.nextText());
            inLine.setAdSystem(adSystem);
        } else if (equalsName(starttgname, "AdTitle")) {
            InLineBean inLine = getInLine(vast);
            inLine.setAdTilte(xmlPullParser.nextText());
        } else if (equalsName(starttgname, "InLine")) {
            AdBean ad = getAdBean(vast);
            ad.setInLine(new InLineBean());
        } else if (equalsName(starttgname, "Impression")) {
            InLineBean inLine = getInLine(vast);
            inLine.setImpression(xmlPullParser.nextText());
        } else if (equalsName(starttgname, "Creatives")) {
            InLineBean inLine = getInLine(vast);
            inLine.setCreatives(new CreativesBean());
        } else if (equalsName(starttgname, "Creative")) {
            CreativesBean creatives = getCreatives(vast);
            CreativeBean creative = new CreativeBean();

            for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
                if (xmlPullParser.getAttributeName(i).equals("id")) {
                    creative.setAdID(xmlPullParser.getAttributeValue(i));
                } else if (xmlPullParser.getAttributeName(i).equals("sequence")) {
                    creative.setSequence(xmlPullParser.getAttributeValue(i));
                }
            }
            creativeList.add(creative);
            creatives.setCreativeList(creativeList);
        } else if (equalsName(starttgname, "Linear")) {
            CreativeBean creative = getFristCreative(vast);
            LinearBean linear = new LinearBean();
            creative.setLinear(linear);
        } else if (equalsName(starttgname, "Duration")) {
            LinearBean linear = getLinear(vast);
            linear.setDuration(xmlPullParser.nextText());
        } else if (equalsName(starttgname, "TrackingEvents")) {
            if (count == 0) {
                LinearBean linear = getLinear(vast);
                TrackingEventsBean trackingEvents = new TrackingEventsBean();
                trackingEvents.setTrackingList(new ArrayList<TrackingEventsBean.Tracking>());
                linear.setTrackingEvents(trackingEvents);
            } else {
                CreativesBean creatives = getCreatives(vast);
                CompanionAdsBean.Companion.TrackingEvents trackingEvents = new CompanionAdsBean.Companion.TrackingEvents();
                creativeList = creatives.getCreativeList();
                CreativeBean creative = creativeList.get(count);
                CompanionAdsBean companionAds = creative.getCompanionAds();
                CompanionAdsBean.Companion companion = companionAds.getCompanion();

                trackingEvents.setTrackingList(new ArrayList<CompanionAdsBean.Companion.TrackingEvents.Tracking>());
                companion.setTrackingEvents(trackingEvents);
            }

        } else if (equalsName(starttgname, "Tracking")) {
            if (count == 0) {
                LinearBean linear = getLinear(vast);
                TrackingEventsBean trackingEvents = linear.getTrackingEvents();
                List<TrackingEventsBean.Tracking> trackingList = trackingEvents.getTrackingList();
                TrackingEventsBean.Tracking tracking = new TrackingEventsBean.Tracking();
                tracking.setEvent(xmlPullParser.getAttributeValue(0));
                tracking.setUrl(xmlPullParser.nextText());
                trackingList.add(tracking);
            } else {
                CreativesBean creatives = getCreatives(vast);
                creativeList = creatives.getCreativeList();
                CreativeBean creative = creativeList.get(count);
                CompanionAdsBean.Companion.TrackingEvents trackingEvents = creative.getCompanionAds().getCompanion().getTrackingEvents();
                List<CompanionAdsBean.Companion.TrackingEvents.Tracking> trackingList = trackingEvents.getTrackingList();
                CompanionAdsBean.Companion.TrackingEvents.Tracking tracking = new CompanionAdsBean.Companion.TrackingEvents.Tracking();
                tracking.setEvent(xmlPullParser.getAttributeValue(0));
                tracking.setUrl(xmlPullParser.nextText());
                trackingList.add(tracking);
            }

        } else if (equalsName(starttgname, "VideoClicks")) {
            LinearBean linear = getLinear(vast);
            VideoClicksBean videoClicks = new VideoClicksBean();
            videoClicks.setClickTrackings(new ArrayList<VideoClicksBean.ClickTracking>());
            linear.setVideoClicks(videoClicks);
        } else if (equalsName(starttgname, "ClickThrough")) {
            VideoClicksBean videoClicks = getVideoClicks(vast);
            videoClicks.setClickThrough(xmlPullParser.nextText());
        } else if (equalsName(starttgname, "ClickTracking")) {
            VideoClicksBean videoClicks = getVideoClicks(vast);
            List<VideoClicksBean.ClickTracking> clickTrackings = videoClicks.getClickTrackings();
            VideoClicksBean.ClickTracking clickTracking = new VideoClicksBean.ClickTracking();
            try {
                clickTracking.setId(xmlPullParser.getAttributeValue(0));
            } catch (Exception e) {
                e.printStackTrace();
            }
            clickTracking.setUrl(xmlPullParser.nextText());
            clickTrackings.add(clickTracking);
        } else if (equalsName(starttgname, "MediaFiles")) {
            LinearBean linear = getLinear(vast);
            MediaFilesBean mediaFiles = new MediaFilesBean();
            List<MediaFilesBean.MediaFile> mediaFilesList = new ArrayList<>();
            mediaFilesList.add(new MediaFilesBean.MediaFile());
            mediaFiles.setMediaFiles(mediaFilesList);
            linear.setMediaFiles(mediaFiles);
        } else if (equalsName(starttgname, "MediaFile")) {
            LinearBean linear = getLinear(vast);
            MediaFilesBean mediaFiles = linear.getMediaFiles();
            List<MediaFilesBean.MediaFile> mediaFileList = mediaFiles.getMediaFiles();
            MediaFilesBean.MediaFile mediaFile = mediaFileList.get(0);
            for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
                if (xmlPullParser.getAttributeName(i).equals("bitrate")) {
                    mediaFile.setBitrate(xmlPullParser.getAttributeValue(i));
                } else if (xmlPullParser.getAttributeName(i).equals("delivery")) {
                    mediaFile.setDelivery(xmlPullParser.getAttributeValue(i));
                } else if (xmlPullParser.getAttributeName(i).equals("height")) {
                    mediaFile.setHeight(xmlPullParser.getAttributeValue(i));
                } else if (xmlPullParser.getAttributeName(i).equals("id")) {
                    mediaFile.setId(xmlPullParser.getAttributeValue(i));
                } else if (xmlPullParser.getAttributeName(i).equals("type")) {
                    mediaFile.setType(xmlPullParser.getAttributeValue(i));
                } else if (xmlPullParser.getAttributeName(i).equals("width")) {
                    mediaFile.setWidth(xmlPullParser.getAttributeValue(i));
                }
            }
            mediaFile.setVideoUrl(xmlPullParser.nextText());
        } else if (equalsName(starttgname, "CompanionAds")) {
            CreativesBean creatives = getCreatives(vast);
            creativeList = creatives.getCreativeList();
            CreativeBean creative = creativeList.get(1);
            CompanionAdsBean companionAds = new CompanionAdsBean();
            creative.setCompanionAds(companionAds);
        } else if (equalsName(starttgname, "Companion")) {
            CreativesBean creatives = getCreatives(vast);
            creativeList = creatives.getCreativeList();
            CreativeBean creative = creativeList.get(1);
            CompanionAdsBean companionAds = creative.getCompanionAds();
            CompanionAdsBean.Companion companion = new CompanionAdsBean.Companion();
            companion.setHeight(xmlPullParser.getAttributeValue(0));
            companion.setWidth(xmlPullParser.getAttributeValue(1));
            companionAds.setCompanion(companion);
        } else if (equalsName(starttgname, "HTMLResource")) {
            CreativesBean creatives = getCreatives(vast);
            creativeList = creatives.getCreativeList();
            CreativeBean creative = creativeList.get(1);
            CompanionAdsBean companionAds = creative.getCompanionAds();
            CompanionAdsBean.Companion companion = companionAds.getCompanion();
            companion.setHTMLResource(xmlPullParser.nextText());
        } else if (equalsName(starttgname, "CompanionClickTracking")) {
            CreativesBean creatives = getCreatives(vast);
            creativeList = creatives.getCreativeList();
            CreativeBean creative = creativeList.get(1);
            CompanionAdsBean companionAds = creative.getCompanionAds();
            CompanionAdsBean.Companion companion = companionAds.getCompanion();
            companion.setCompanionClickTracking(xmlPullParser.nextText());
        }

        return creativeList;
    }

    private VideoClicksBean getVideoClicks(VAST vast) {
        LinearBean linear = getLinear(vast);
        return linear.getVideoClicks();
    }

    private LinearBean getLinear(VAST vast) {
        CreativeBean fristCreative = getFristCreative(vast);
        return fristCreative.getLinear();
    }

    private CreativeBean getFristCreative(VAST vast) {
        CreativesBean creatives = getCreatives(vast);
        return creatives.getCreativeList().get(0);
    }

    private CreativesBean getCreatives(VAST vast) {
        InLineBean inLine = getInLine(vast);
        return inLine.getCreatives();
    }

    private InLineBean getInLine(VAST vast) {
        AdBean ad = getAdBean(vast);
        return ad.getInLine();
    }

    private AdBean getAdBean(VAST vast) {
        return vast.getAd();
    }

    private boolean equalsName(String starttgname, String name) {
        return starttgname.equalsIgnoreCase(name);
    }

    public interface ParseVastCallback {
        void onResult(VAST vast);
    }
}
