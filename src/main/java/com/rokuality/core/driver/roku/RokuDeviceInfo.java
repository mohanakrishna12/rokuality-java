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
     * @return JSONObject - The complete Roku device info JSONObject of device info name/values.
     */
    public JSONObject getDeviceInfoJSON() {
        return deviceInfoObj;
    }

    /**
     * Gets the device info Object value of the provided attribute name.
     * i.e. getDeviceInfoAttribute("friendly-device-name");
     * 
     * @param attributeName - The attribute name of the value to be returned.
     * @return Object - The Roku device info attribute value.
     */
    public Object getDeviceInfoAttribute(String attributeName) {
        return (Object) deviceInfoObj.get(attributeName);
    }

}
