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

	/**
	 * Gets a JSON object containing information about all installed apps on the device.
	 * 
	 * @return JSONObject - A JSON object containing info about all installed apps on the device under test.
	 */
	public JSONObject getInstalledApps() {
		JSONObject readySession = JsonUtils.deepCopy(session);
		readySession.put("action", "get_installed_apps");
		return serverPostHandler.postToServerWithHandling("info", readySession, ServerFailureException.class);
	}

	/**
	 * Gets a JSON object containing information about the currently focused app on the device.
	 * 
	 * @return JSONObject - A JSON object containing info about the currently focused app on the device under test.
	 */
	public JSONObject getActiveApp() {
		JSONObject readySession = JsonUtils.deepCopy(session);
		readySession.put("action", "get_active_app");
		return serverPostHandler.postToServerWithHandling("info", readySession, ServerFailureException.class);
	}

	/**
	 * Gets the Roku debugger logs from session start to now.
	 * 
	 * @return String - The Roku debugger logs.
	 */
	public String getDebugLogs() {
		JSONObject readySession = JsonUtils.deepCopy(session);
		readySession.put("action", "get_debug_logs");
		JSONObject resultObj = serverPostHandler.postToServerWithHandling("info", readySession, ServerFailureException.class);
		return String.valueOf(resultObj.get("log_content"));
	}

}
