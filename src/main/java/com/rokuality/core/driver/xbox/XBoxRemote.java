package com.rokuality.core.driver.xbox;

import com.rokuality.core.driver.ServerPostHandler;
import com.rokuality.core.enums.XBoxButton;
import com.rokuality.core.exceptions.RemoteInteractException;
import com.rokuality.core.httpexecutor.HttpClient;
import com.rokuality.core.utils.JsonUtils;

import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class XBoxRemote {

	JSONObject session = null;
	private ServerPostHandler serverPostHandler = null;
	
	public XBoxRemote(HttpClient httpClient, JSONObject session) {
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
	public void pressButton(XBoxButton button) {
		JSONObject readySession = JsonUtils.deepCopy(session);
		readySession.put("action", "press_button");
		readySession.put("remote_button", button.value());
		serverPostHandler.postToServerWithHandling("remote", readySession, RemoteInteractException.class);
	}

	/**
	 * Sends a literal string of text to the device. Only relevant if you are interacting with an XBox text input
	 * and the XBox virtual keyboard is present on the screen.
	 *
	 * @param textToType String - The text you wish to type into the input field.
	 * 
	 * @throws RemoteInteractException If the input fails to be sent.
	 */
	public void sendKeys(String textToType) {
		JSONObject readySession = JsonUtils.deepCopy(session);
		readySession.put("action", "send_keys");
		readySession.put("text", textToType);
		serverPostHandler.postToServerWithHandling("remote", readySession, RemoteInteractException.class);
	}

}
