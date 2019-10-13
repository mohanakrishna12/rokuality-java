package com.rokuality.core.driver.xbox;

import com.rokuality.core.driver.BaseDriver;
import com.rokuality.core.driver.DeviceCapabilities;
import com.rokuality.core.driver.Finder;
import com.rokuality.core.driver.Options;
import com.rokuality.core.driver.Screen;
import com.rokuality.core.driver.ServerPostHandler;
import com.rokuality.core.exceptions.ServerFailureException;
import com.rokuality.core.exceptions.SessionNotStartedException;
import com.rokuality.core.httpexecutor.HttpClient;

import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class XBoxDriver extends BaseDriver {

	private ServerPostHandler serverPostHandler = null;
	private HttpClient httpClient = null;

	/**
	 * Starts a new XBox driver session.
	 *
	 * @param serverURL String - The url your server is listening at, i.e. http://localhost:port
	 * @param capabilities DeviceCapabilities - The capabilities for your driver session.
	 * 
	 * @throws SessionNotStartedException If a session could not be initiated.
	 */
	public XBoxDriver(String serverURL, DeviceCapabilities capabilities) {
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
	 * Stops the XBox driver session and releases all assets. Should be called as the last command of every session.
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
	 * 
	 * @return Finder
	 */
	public Finder finder() {
		return new Finder(httpClient, super.getSession());
	}

	/**
	 * Initiates the XBox Remote for sending remote control commands.
	 * 
	 * @return XBoxRemote
	 */
	public XBoxRemote remote() {
		return new XBoxRemote(httpClient, super.getSession());
	}

	/**
	 * Gets information about the device under test.
	 * 
	 * @return XBoxInfo
	 */
	public XBoxInfo info() {
		return new XBoxInfo(httpClient, super.getSession());
	}

	/**
	 * Initiates the Screen for getting information and artifacts from the device screen.
	 * 
	 * @return Screen
	 */
	public Screen screen() {
		return new Screen(httpClient, super.getSession());
	}

	/**
	 * Initiates Options for various driver and finder settings.
	 * 
	 * @return Options
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

}
