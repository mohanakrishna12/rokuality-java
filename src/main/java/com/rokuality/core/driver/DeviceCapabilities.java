package com.rokuality.core.driver;

import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class DeviceCapabilities {

    private JSONObject jsonCaps = null;

    public DeviceCapabilities() {
        jsonCaps = new JSONObject();
    }

    /**
	 * Adds a capability object that will be passed to the Driver session start. Please
     * see the README for a detailed descripton on what capabilities are required for
     * a session start and which capabilities are optional.
	 *
	 * @param name String - The name of the capability. See README for complete list.
	 * @param value Object - The capability value.
	 */
    public void addCapability(String name, Object value) {
        jsonCaps.put(name, value);
    }

    public JSONObject getCapabilitiesAsJSON() {
        return jsonCaps;
    }

    public void removeCapability(String name) {
        jsonCaps.remove(name);
    }

}
