package com.rokuality.core.driver.roku;

import org.json.simple.JSONObject;

public class RokuMediaPlayerInfo {

    private String speed;
    private String runtime;
    private String container;
    private String captions;
    private String video;
    private String audio;
    private String drm;
    private String videoRes;
    private String position;
    private String segmentType;
    private String mediaSequence;
    private String time;
    private String bitrate;
    private String state;
    private String error;
    private String duration;
    private String isLive;
    private String target;
    private String max;
    private String current;
    

    public RokuMediaPlayerInfo(JSONObject json) {
        JSONObject valueObj = (JSONObject) json.get("value");
        JSONObject newStreamObj = (JSONObject) valueObj.get("NewStream");
        JSONObject formatObj = (JSONObject) valueObj.get("Format");
        JSONObject streamSegmentObj = (JSONObject) valueObj.get("StreamSegment");
        JSONObject bufferingObj = (JSONObject) valueObj.get("Buffering");
        
        speed = (String) newStreamObj.get("Speed");
        runtime = (String) valueObj.get("Runtime");
        container = (String) formatObj.get("Container");
        captions = (String) formatObj.get("Captions");
        video = (String) formatObj.get("Video");
        audio = (String) formatObj.get("Audio");
        drm = (String) formatObj.get("Drm");
        videoRes = (String) formatObj.get("VideoRes");
        position = (String) valueObj.get("Position");
        segmentType = (String) streamSegmentObj.get("SegmentType");
        mediaSequence = (String) streamSegmentObj.get("MediaSequence");
        time = (String) streamSegmentObj.get("Time");
        bitrate = (String) streamSegmentObj.get("Bitrate");
        state = (String) valueObj.get("State");
        error = (String) valueObj.get("Error");
        duration = (String) valueObj.get("Duration");
        isLive = (String) valueObj.get("IsLive");
        target = (String) bufferingObj.get("Target");
        max = (String) bufferingObj.get("Max");
        current = (String) bufferingObj.get("Current");
    }

    public String getSpeed() {
        return speed;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getContainer() {
        return container;
    }

    public String getCaptions() {
        return captions;
    }

    public String getVideo() {
        return video;
    }

    public String getAudio() {
        return audio;
    }

    public String getDRM() {
        return drm;
    }

    public String getVideoResolution() {
        return videoRes;
    }

    public String getPosition() {
        return position;
    }

    public String getSegmentType() {
        return segmentType;
    }

    public String getMediaSequence() {
        return mediaSequence;
    }

    public String getTime() {
        return time;
    }

    public String getBitrate() {
        return bitrate;
    }

    public String getState() {
        return state;
    }

    public boolean isError() {
        return Boolean.parseBoolean(error);
    }

    public String getDuration() {
        return duration;
    }

    public boolean isLive() {
        return Boolean.parseBoolean(isLive);
    }

    public String getTarget() {
        return target;
    }

    public String getMax() {
        return max;
    }

    public String getCurrent() {
        return current;
    }

}
