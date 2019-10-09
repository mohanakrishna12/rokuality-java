package com.rokuality.core.driver.roku;

import org.json.simple.JSONObject;

public class RokuDeviceInfo {

    private String advertisingId;
    private String secureDevice;
    private String notificationsEnabled;
    private String modelNumber;
    private String modelName;
    private String networkType;
    private String supportsFindRemote;
    private String udn;
    private String developerEnabled;
    private String supportsWakeOnWlan;
    private String softwareVersion;
    private String vendorName;
    private String supportsEcsMicrophone;
    private String softwareBuild;
    private String country;
    private String isTv;
    private String powerMode;
    private String timezoneOffset;
    private String userDeviceName;
    private String searchEnabled;
    private String friendlyDeviceName;
    private String language;
    private String timeZoneTz;
    private String timeZone;
    private String hasPlayOnRoku;
    private String supportsAudioGuide;
    private String modelRegion;
    private String locale;
    private String timeZoneName;
    private String supportsEcsTextEdit;
    private String timeZoneAuto;
    private String hasMobileScreensaver;
    private String wifiMac;
    private String notificationsFirstUse;
    private String supportsSuspend;
    private String searchChannelsEnabled;
    private String serialNumber;
    private String supportsPrivateListening;
    private String friendlyModelName;
    private String wifiDriver;
    private String uptime;
    private String voiceSearchEnabled;
    private String supportsEthernet;
    private String deviceId;
    private String isStick;
    private String clockFormat;
    private String keyedDeveloperId;
    private String defaultDeviceName;
    private String networkName;
    private String headphonesConnected;
    private String supportUrl;

    // TODO - serialize with jackson to remove the castings
    public RokuDeviceInfo(JSONObject json) {
        JSONObject deviceInfoObj = (JSONObject) json.get("device-info");
        advertisingId = deviceInfoObj.get("advertising-id").toString();
        secureDevice = deviceInfoObj.get("secure-device").toString();
        notificationsEnabled = deviceInfoObj.get("notifications-enabled").toString();
        modelNumber = deviceInfoObj.get("model-number").toString();
        modelName = deviceInfoObj.get("model-name").toString();
        networkType = deviceInfoObj.get("network-type").toString();
        supportsFindRemote = deviceInfoObj.get("supports-find-remote").toString();
        udn = deviceInfoObj.get("udn").toString();
        developerEnabled = deviceInfoObj.get("developer-enabled").toString();
        supportsWakeOnWlan = deviceInfoObj.get("supports-wake-on-wlan").toString();
        softwareVersion = deviceInfoObj.get("software-version").toString();
        vendorName = deviceInfoObj.get("vendor-name").toString();
        supportsEcsMicrophone = deviceInfoObj.get("supports-ecs-microphone").toString();
        softwareBuild = deviceInfoObj.get("software-build").toString();
        country = deviceInfoObj.get("country").toString();
        isTv = deviceInfoObj.get("is-tv").toString();
        powerMode = deviceInfoObj.get("power-mode").toString();
        timezoneOffset = deviceInfoObj.get("time-zone-offset").toString();
        userDeviceName = deviceInfoObj.get("user-device-name").toString();
        searchEnabled = deviceInfoObj.get("search-enabled").toString();
        friendlyDeviceName = deviceInfoObj.get("friendly-device-name").toString();
        language = deviceInfoObj.get("language").toString();
        timeZoneTz = deviceInfoObj.get("time-zone-tz").toString();
        timeZone = deviceInfoObj.get("time-zone").toString();
        hasPlayOnRoku = deviceInfoObj.get("has-play-on-roku").toString();
        supportsAudioGuide = deviceInfoObj.get("supports-audio-guide").toString();
        modelRegion = deviceInfoObj.get("model-region").toString();
        locale = deviceInfoObj.get("locale").toString();
        timeZoneName = deviceInfoObj.get("time-zone-name").toString();
        supportsEcsTextEdit = deviceInfoObj.get("supports-ecs-textedit").toString();
        timeZoneAuto = deviceInfoObj.get("time-zone-auto").toString();
        hasMobileScreensaver = deviceInfoObj.get("has-mobile-screensaver").toString();
        wifiMac = deviceInfoObj.get("wifi-mac").toString();
        notificationsFirstUse = deviceInfoObj.get("notifications-first-use").toString();
        supportsSuspend = deviceInfoObj.get("supports-suspend").toString();
        searchChannelsEnabled = deviceInfoObj.get("search-channels-enabled").toString();
        serialNumber = deviceInfoObj.get("serial-number").toString();
        supportsPrivateListening = deviceInfoObj.get("supports-private-listening").toString();
        friendlyModelName = deviceInfoObj.get("friendly-model-name").toString();
        wifiDriver = deviceInfoObj.get("wifi-driver").toString();
        uptime = deviceInfoObj.get("uptime").toString();
        voiceSearchEnabled = deviceInfoObj.get("voice-search-enabled").toString();
        supportsEthernet = deviceInfoObj.get("supports-ethernet").toString();
        deviceId = deviceInfoObj.get("device-id").toString();
        isStick = deviceInfoObj.get("is-stick").toString();
        clockFormat = deviceInfoObj.get("clock-format").toString();
        keyedDeveloperId = deviceInfoObj.get("keyed-developer-id").toString();
        defaultDeviceName = deviceInfoObj.get("default-device-name").toString();
        networkName = deviceInfoObj.get("network-name").toString();
        headphonesConnected = deviceInfoObj.get("headphones-connected").toString();
        supportUrl = deviceInfoObj.get("support-url").toString();
    }

    public String getAdvertisingId() {
        return advertisingId;
    }

    public String getSecureDevice() {
        return secureDevice;
    }

    public String getNotificationsEnabled() {
        return notificationsEnabled;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public String getModelName() {
        return modelName;
    }

    public String getNetworkType() {
        return networkType;
    }

    public String getSupportsFindRemote() {
        return supportsFindRemote;
    }

    public String getUdn() {
        return udn;
    }

    public String getDeveloperEnabled() {
        return developerEnabled;
    }

    public String getSupportsWakeOnWlan() {
        return supportsWakeOnWlan;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public String getVendorName() {
        return vendorName;
    }

    public String getSupportsEcsMicrophone() {
        return supportsEcsMicrophone;
    }

    public String getSoftwareBuild() {
        return softwareBuild;
    }

    public String getCountry() {
        return country;
    }

    public String getIsTv() {
        return isTv;
    }

    public String getPowerMode() {
        return powerMode;
    }

    public String getTimezoneOffset() {
        return timezoneOffset;
    }

    public String getUserDeviceName() {
        return userDeviceName;
    }

    public String getSearchEnabled() {
        return searchEnabled;
    }

    public String getFriendlyDeviceName() {
        return friendlyDeviceName;
    }

    public String getLanguage() {
        return language;
    }

    public String getTimeZoneTz() {
        return timeZoneTz;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public String getHasPlayOnRoku() {
        return hasPlayOnRoku;
    }

    public String getSupportsAudioGuide() {
        return supportsAudioGuide;
    }

    public String getModelRegion() {
        return modelRegion;
    }

    public String getLocale() {
        return locale;
    }

    public String getTimeZoneName() {
        return timeZoneName;
    }

    public String getSupportsEcsTextEdit() {
        return supportsEcsTextEdit;
    }

    public String getTimeZoneAuto() {
        return timeZoneAuto;
    }

    public String getHasMobileScreensaver() {
        return hasMobileScreensaver;
    }

    public String getWifiMac() {
        return wifiMac;
    }

    public String getNotificationsFirstUse() {
        return notificationsFirstUse;
    }

    public String getSupportsSuspend() {
        return supportsSuspend;
    }

    public String getSearchChannelsEnabled() {
        return searchChannelsEnabled;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getSupportsPrivateListening() {
        return supportsPrivateListening;
    }

    public String getFriendlyModelName() {
        return friendlyModelName;
    }

    public String getWifiDriver() {
        return wifiDriver;
    }

    public String getUptime() {
        return uptime;
    }

    public String getVoiceSearchEnabled() {
        return voiceSearchEnabled;
    }

    public String getSupportsEthernet() {
        return supportsEthernet;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getIsStick() {
        return isStick;
    }

    public String getClockFormat() {
        return clockFormat;
    }

    public String getKeyedDeveloperId() {
        return keyedDeveloperId;
    }

    public String getDefaultDeviceName() {
        return defaultDeviceName;
    }

    public String getNetworkName() {
        return networkName;
    }

    public String getHeadphonesConnected() {
        return headphonesConnected;
    }

    public String getSupportUrl() {
        return supportUrl;
    }

}
