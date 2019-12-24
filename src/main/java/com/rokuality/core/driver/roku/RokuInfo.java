package com.rokuality.core.driver.roku;

import com.rokuality.core.driver.ServerPostHandler;
import com.rokuality.core.exceptions.ServerFailureException;
import com.rokuality.core.httpexecutor.HttpClient;
import com.rokuality.core.utils.JsonUtils;

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
		JSONObject readySession = JsonUtils.deepCopy(session);
		readySession.put("action", "device_info");
		return new RokuDeviceInfo(serverPostHandler.postToServerWithHandling("info", readySession, ServerFailureException.class));
	}

	/**
	 * Gets info about the Roku media player under test including buffering information, the player state, player errors, etc.
	 * 
	 * @return RokuMediaPlayerInfo - Various information about the device media player under test.
	 */
	public RokuMediaPlayerInfo getMediaPlayerInfo() {
		JSONObject readySession = JsonUtils.deepCopy(session);
		readySession.put("action", "media_player_info");
		return new RokuMediaPlayerInfo(serverPostHandler.postToServerWithHandling("info", readySession, ServerFailureException.class));
	}

}
