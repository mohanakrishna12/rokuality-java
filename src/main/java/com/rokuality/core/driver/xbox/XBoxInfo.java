package com.rokuality.core.driver.xbox;

import com.rokuality.core.driver.ServerPostHandler;
import com.rokuality.core.exceptions.ServerFailureException;
import com.rokuality.core.httpexecutor.HttpClient;

import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class XBoxInfo {

	JSONObject session = null;
	private ServerPostHandler serverPostHandler = null;
	
	public XBoxInfo(HttpClient httpClient, JSONObject session) {
		this.session = session;
		serverPostHandler = new ServerPostHandler(httpClient);
	}

	/**
	 * Gets info about the XBox device under test such as os version, device id, etc.
	 * 
	 * @return XBoxDeviceInfo - Various information about the device under test.
	 */
	public XBoxDeviceInfo getDeviceInfo() {
		session.put("action", "device_info");
		return new XBoxDeviceInfo(serverPostHandler.postToServerWithHandling("info", session, ServerFailureException.class));
	}

}
