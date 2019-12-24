package com.rokuality.core.driver.roku;

import com.rokuality.core.driver.BaseDriver;
import com.rokuality.core.driver.DeviceCapabilities;
import com.rokuality.core.driver.Finder;
import com.rokuality.core.driver.Options;
import com.rokuality.core.driver.ServerPostHandler;
import com.rokuality.core.exceptions.ServerFailureException;
import com.rokuality.core.exceptions.SessionNotStartedException;
import com.rokuality.core.httpexecutor.HttpClient;
import com.rokuality.core.utils.JsonUtils;

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
		JSONObject readySession = JsonUtils.deepCopy(super.getSession());
		readySession.put("action", "stop");
		if (super.getSession() != null) {
			serverPostHandler.postToServerWithHandling("session", readySession, ServerFailureException.class);
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
	 * Initiates the Roku Remote for sending remote control commands.
	 * 
	 * @return RokuRemote
	 */
	public RokuRemote remote() {
		return new RokuRemote(httpClient, super.getSession());
	}

	/**
	 * Gets information about the device under test and the Roku media player.
	 * 
	 * @return RokuInfo
	 */
	public RokuInfo info() {
		return new RokuInfo(httpClient, super.getSession());
	}

	/**
	 * Initiates the Screen for getting information and artifacts from the device screen.
	 * 
	 * @return RokuScreen
	 */
	public RokuScreen screen() {
		return new RokuScreen(httpClient, super.getSession());
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
