package com.rokuality.core.driver.hdmi;

import com.rokuality.core.driver.ServerPostHandler;
import com.rokuality.core.exceptions.RemoteInteractException;
import com.rokuality.core.httpexecutor.HttpClient;
import com.rokuality.core.utils.JsonUtils;

import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class HDMIRemote {

	JSONObject session = null;
	private ServerPostHandler serverPostHandler = null;
	
	public HDMIRemote(HttpClient httpClient, JSONObject session) {
		this.session = session;
		serverPostHandler = new ServerPostHandler(httpClient);
	}

	/**
	 * Sends a remote control command to the device.
	 *
	 * @param button - The button you wish to press on the device.
	 * 
	 * @throws RemoteInteractException If the remote button could not be pressed.
	 */
	public void pressButton(String button) {
		JSONObject readySession = JsonUtils.deepCopy(session);
		readySession.put("action", "press_button");
		readySession.put("remote_button", button);
		serverPostHandler.postToServerWithHandling("remote", readySession, RemoteInteractException.class);
	}

	public String getButtonOptions() {
		JSONObject readySession = JsonUtils.deepCopy(session);
		readySession.put("action", "get_buttons");
		JSONObject buttonObj = serverPostHandler.postToServerWithHandling("remote", readySession, RemoteInteractException.class);
		return String.valueOf(buttonObj.get("button_options"));
	}

}
