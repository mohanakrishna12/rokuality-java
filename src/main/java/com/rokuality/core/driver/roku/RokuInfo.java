package com.rokuality.core.driver.roku;

import java.io.File;

import com.rokuality.core.driver.ServerPostHandler;
import com.rokuality.core.exceptions.ServerFailureException;
import com.rokuality.core.httpexecutor.HttpClient;
import com.rokuality.core.utils.FileToStringUtils;
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

	/**
	 * Gets the Roku performance profile (.bsprof) file from the device which can be used to monitor the apps CPU and memory behavior.
	 * 
	 * NOTE - you must pass the 'EnablePerformanceProfiling' capability on session start with a value of true to enable this capture.
	 * The returned .bsprof file can then be loaded into the Roku brightscript profile visualizer tool at http://devtools.web.roku.com/profiler/viewer/
	 * and will showcase the apps CPU and memory utilizations and help diagnose any performance issues on the device.
	 * 
	 * Note - calling this method will reset the performance profile capture on the device and will relaunch the app.
	 * 
	 * @return File - The Roku brightscript profile which can be loaded into http://devtools.web.roku.com/profiler/viewer/
	 * 
	 * @throws ServerFailurException - If the user did NOT start the test session with the 'EnablePerformanceProfiling' capability set to true, 
	 * Or an error occurred during the collection of the performance profile data.
	 */
	public File getPerformanceProfile() {
		JSONObject readySession = JsonUtils.deepCopy(session);
		readySession.put("action", "performance_profile");
		JSONObject resultObj = serverPostHandler.postToServerWithHandling("info", readySession, ServerFailureException.class);
		String profileContent = String.valueOf(resultObj.get("performance_profiling_data"));
		String profileExt = String.valueOf(resultObj.get("performance_profile_file_ext"));
		return new FileToStringUtils().convertToFile(profileContent, profileExt);
	}

}
