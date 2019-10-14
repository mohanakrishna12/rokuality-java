package com.rokuality.core.driver.xbox;

import org.json.simple.JSONObject;

public class XBoxDeviceInfo {

    private String osVersion;
    private String devMode;
    private String osEdition;
    private String consoleType;
    private String consoleID;
    private String deviceID;
    private String serialNumber;

    public XBoxDeviceInfo(JSONObject json) {
        osVersion = json.get("OsVersion").toString();
        devMode = json.get("DevMode").toString();
        osEdition = json.get("OsEdition").toString();
        consoleType = json.get("ConsoleType").toString();
        consoleID = json.get("ConsoleId").toString();
        deviceID = json.get("DeviceId").toString();
        serialNumber = json.get("SerialNumber").toString();
    }

    public String getOSVersion() {
        return osVersion;
    }

    public String getDevMode() {
        return devMode;
    }

    public String getOSEdition() {
        return osEdition;
    }

    public String getConsoleType() {
        return consoleType;
    }

    public String getConsoleID() {
        return consoleID;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

}
