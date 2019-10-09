package com.rokuality.core.driver.roku;

import com.rokuality.core.driver.ServerPostHandler;
import com.rokuality.core.enums.RokuButton;
import com.rokuality.core.exceptions.RemoteInteractException;
import com.rokuality.core.httpexecutor.HttpClient;

import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class RokuRemote {

	JSONObject session = null;
	private ServerPostHandler serverPostHandler = null;
	
	public RokuRemote(HttpClient httpClient, JSONObject session) {
		this.session = session;
		serverPostHandler = new ServerPostHandler(httpClient);
	}

	/**
	 * Sends a remote control command to the device.
	 *
	 * @param RokuButton RokuButton - The button you wish to press on the device.
	 * 
	 * @throws RemoteInteractException If the remote button could not be pressed.
	 */
	public void pressButton(RokuButton button) {
		session.put("action", "press_button");
		session.put("remote_button", button.value());
		serverPostHandler.postToServerWithHandling("remote", session, RemoteInteractException.class);
	}

	/**
	 * Sends a literal string of text to the device. Only relevant if you are interacting with a Roku text input.
	 *
	 * @param textToType String - The text you wish to type into the input field.
	 * 
	 * @throws RemoteInteractException If the input fails to be sent.
	 */
	public void sendKeys(String textToType) {
		session.put("action", "send_keys");
		session.put("text", textToType);
		serverPostHandler.postToServerWithHandling("remote", session, RemoteInteractException.class);
	}

}
