package com.rokuality.core.driver.roku;

import com.rokuality.core.driver.Options;
import com.rokuality.core.exceptions.ScreenException;
import com.rokuality.core.httpexecutor.HttpClient;
import com.rokuality.core.utils.JsonUtils;

import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class RokuOptions extends Options {

	public RokuOptions(HttpClient httpClient, JSONObject session) {
		super(httpClient, session);
	}

	/**
	 * Attempts to reboot the Roku device.
	 * 
	 */
	public void reboot() {
		JSONObject readySession = JsonUtils.deepCopy(session);
		readySession.put("action", "reboot_device");
		serverPostHandler.postToServerWithHandling("settings", readySession, ScreenException.class);
	}

}
