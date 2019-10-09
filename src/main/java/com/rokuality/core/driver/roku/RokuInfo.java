package com.rokuality.core.driver.roku;

import com.rokuality.core.driver.ServerPostHandler;
import com.rokuality.core.exceptions.ServerFailureException;
import com.rokuality.core.httpexecutor.HttpClient;

import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class RokuInfo {

	JSONObject session = null;
	private ServerPostHandler serverPostHandler = null;
	
	public RokuInfo(HttpClient httpClient, JSONObject session) {
		this.session = session;
		serverPostHandler = new ServerPostHandler(httpClient);
	}

	/**
	 * Gets info about the Roku device under test such as version, power mode, etc.
	 * 
	 * @return RokuDeviceInfo - Various information about the device under test.
	 */
	public RokuDeviceInfo getDeviceInfo() {
		session.put("action", "device_info");
		return new RokuDeviceInfo(serverPostHandler.postToServerWithHandling("info", session, ServerFailureException.class));
	}

}
