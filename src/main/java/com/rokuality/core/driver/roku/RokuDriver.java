package com.rokuality.core.driver.roku;

import java.io.File;
import java.util.Iterator;

import com.rokuality.core.driver.BaseDriver;
import com.rokuality.core.driver.DeviceCapabilities;
import com.rokuality.core.driver.Finder;
import com.rokuality.core.driver.Options;
import com.rokuality.core.driver.Screen;
import com.rokuality.core.driver.ServerPostHandler;
import com.rokuality.core.exceptions.ServerFailureException;
import com.rokuality.core.exceptions.SessionNotStartedException;
import com.rokuality.core.httpexecutor.HttpClient;
import com.rokuality.core.utils.FileToStringUtils;

import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class RokuDriver extends BaseDriver {

	private ServerPostHandler serverPostHandler = null;
	private HttpClient httpClient = null;

	/**
	 * Starts a new Roku driver session.
	 *
	 * @param serverURL String - The url your server is listening at, i.e. http://localhost:port
	 * @param capabilities DeviceCapabilities - The capabilities for your driver session.
	 * 
	 * @throws SessionNotStartedException If a session could not be initiated.
	 */
	public RokuDriver(String serverURL, DeviceCapabilities capabilities) {
		httpClient = new HttpClient(serverURL);
		serverPostHandler = new ServerPostHandler(httpClient);
		super.setServerURL(serverURL);
		JSONObject capJSON = capabilities.getCapabilitiesAsJSON();
		capJSON = prepareCapJSON(capJSON);
		capJSON.put("action", "start");
		super.setSession(
				serverPostHandler.postToServerWithHandling("session", capJSON, SessionNotStartedException.class));
	}

	/**
	 * Stops the Roku driver session and releases all assets. Should be called as the last command of every session.
	 * 
	 * @throws ServerFailureException If a session could not be properly torn down for any reason.
	 */
	public void stop() {
		JSONObject json = super.getSession();
		json.put("action", "stop");
		if (super.getSession() != null) {
			serverPostHandler.postToServerWithHandling("session", json, ServerFailureException.class);
		}
	}

	/**
	 * Initiates the Finder for finding elements.
	 */
	public Finder finder() {
		return new Finder(httpClient, super.getSession());
	}

	/**
	 * Initiates the Roku Remote for sending remote control commands.
	 */
	public RokuRemote remote() {
		return new RokuRemote(httpClient, super.getSession());
	}

	/**
	 * Gets information about the device under test.
	 */
	public RokuInfo info() {
		return new RokuInfo(httpClient, super.getSession());
	}

	/**
	 * Initiates the Screen for getting information and artifacts from the device screen.
	 */
	public Screen screen() {
		return new Screen(httpClient, super.getSession());
	}

	/**
	 * Initiates Options for various driver and finder settings.
	 */
	public Options options() {
		return new Options(httpClient, super.getSession());
	}

	/**
	 * Gets the session id of the device under test.
	 * 
	 * @return String
	 */
	public String getSessionID() {
		return super.getSession().get("session_id").toString();
	}

	private JSONObject prepareCapJSON(JSONObject capabilities) {
		JSONObject preparedJSON = new JSONObject();
		Iterator<String> keys = capabilities.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			Object value = capabilities.get(key);

			File file = new File(String.valueOf(value));
			if (file.exists() && file.isFile()) {
				String encodedData = new FileToStringUtils().convertFileToBase64String(file);
				preparedJSON.put(key, encodedData);
			} else {
				preparedJSON.put(key, value);
			}
		}

		return preparedJSON;
	}

}
