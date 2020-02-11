package com.rokuality.core.driver.roku;

import com.rokuality.core.exceptions.ServerFailureException;

import org.json.simple.JSONObject;

public class RokuDeviceInfo {

    JSONObject deviceInfoObj = null;

    public RokuDeviceInfo(JSONObject json) {
        deviceInfoObj = (JSONObject) json.get("device-info");
        if (deviceInfoObj == null) {
            throw new ServerFailureException("Failed to retrieve device info from server!");
        }
    }

    /**
     * Gets the device info JSONObject of key/value device info entries.
     * 
     * @return JSONObject
     */
    public JSONObject getDeviceInfoJSON() {
        return deviceInfoObj;
    }

    /**
     * Gets the device info Object value of the provided attribute name.
     * i.e. getDeviceInfoAttribute("friendly-device-name");
     * 
     * @return JSONObject
     */
    public Object getDeviceInfoAttribute(String attributeName) {
        return (Object) deviceInfoObj.get(attributeName);
    }

}
