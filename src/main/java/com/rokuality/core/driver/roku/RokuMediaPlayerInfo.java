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

    /**
     * Gets the current playback speed (in bps).
     * 
     * @return String
     */
    public String getSpeed() {
        return speed;
    }

    /**
     * Gets the runtime of the video being played (in seconds).
     * 
     * @return String
     */
    public String getRuntime() {
        return runtime;
    }

    /**
     * Gets the container format ("hls", for example).
     * 
     * @return String
     */
    public String getContainer() {
        return container;
    }

    /**
     * Gets the closed caption format ("608_708", for example). This value is set to
     * "none" if there are no captions.
     * 
     * @return String
     */
    public String getCaptions() {
        return captions;
    }

    /**
     * Gets the format of the currently playing video stream ("mpeg4-15", for
     * example).
     * 
     * @return String
     */
    public String getVideo() {
        return video;
    }

    /**
     * Gets the audio compression method ("aac", "aac_adts", and so on.).
     * 
     * @return String
     */
    public String getAudio() {
        return audio;
    }

    /**
     * Gets the encoding type. If no encoding is used, this us set to "none".
     * 
     * @return String
     */
    public String getDRM() {
        return drm;
    }

    /**
     * Gets the resolution of the currently playing video stream ("1280X720", for
     * example).
     * 
     * @return String
     */
    public String getVideoResolution() {
        return videoRes;
    }

    /**
     * Gets the time of the current position in the stream, expressed as the elapsed
     * time (in ms) since the start of stream or UTC time, depending on the content.
     * 
     * @return String
     */
    public String getPosition() {
        return position;
    }

    /**
     * Gets the type of data in the segment, which may be one of the following
     * values: "audio", "video", "captions", "mux".
     * 
     * @return String
     */
    public String getSegmentType() {
        return segmentType;
    }

    /**
     * Gets the HLS media sequence ID of the segment in the video.
     * 
     * @return String
     */
    public String getMediaSequence() {
        return mediaSequence;
    }

    /**
     * Gets the chunk start time.
     * 
     * @return String
     */
    public String getTime() {
        return time;
    }

    /**
     * Gets the bitrate of the video segment (in bps).
     * 
     * @return String
     */
    public String getBitrate() {
        return bitrate;
    }

    /**
     * Gets the current playback state ("play", "pause", "resume", and so on).
     * 
     * @return String
     */
    public String getState() {
        return state;
    }

    /**
     * Gets whether there was a playback error. If no error occurred, this is set to
     * "false".
     * 
     * @return boolean
     */
    public boolean isError() {
        return Boolean.parseBoolean(error);
    }

    /**
     * Gets the duration of the video being played (in seconds). This becomes valid
     * when playback begins and may change if the video is dynamic content, such as
     * a live event.
     * 
     * @return String
     */
    public String getDuration() {
        return duration;
    }

    /**
     * Gets whether the video being played is a live stream.
     * 
     * @return boolean
     */
    public boolean isLive() {
        return Boolean.parseBoolean(isLive);
    }

    /**
     * Gets the target buffering speed (in kbps).
     * 
     * @return String
     */
    public String getTarget() {
        return target;
    }

    /**
     * Gets the maximum possible buffering speed (in kbps).
     * 
     * @return String
     */
    public String getMax() {
        return max;
    }

    /**
     * Gets the current buffering speed (in kbps).
     * 
     * @return String
     */
    public String getCurrent() {
        return current;
    }

}
