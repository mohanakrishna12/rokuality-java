package com.rokuality.core.driver;

import com.rokuality.core.enums.SessionStatus;
import com.rokuality.core.exceptions.ServerFailureException;
import com.rokuality.core.httpexecutor.HttpClient;
import com.rokuality.core.utils.JsonUtils;

import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class Options {

	JSONObject session = null;
	private ServerPostHandler serverPostHandler = null;
	
	public Options(HttpClient httpClient, JSONObject session) {
		this.session = session;
		serverPostHandler = new ServerPostHandler(httpClient);
	}

	/**
	 * Overrides the default Image Match Similarity value for all Image based elements. It will last for the duration
	 * of the driver session, or until a new value is set. A lower value 
	 * will increase the likelihood that your image locator will find a match, but too low a value
	 * and you can introduce false positives.
	 *
	 * @param imageMatchSimilarity double - the image match similarity to apply to all image elements i.e. '0.95'
	 * @throws ServerFailureException If the image match similarity cannot be applied.
	 */
	public void setImageMatchSimilarity(double imageMatchSimilarity) {
		JSONObject readySession = JsonUtils.deepCopy(session);
		readySession.put("action", "image_match_similarity");
		readySession.put("image_match_similarity", String.valueOf(imageMatchSimilarity));
		serverPostHandler.postToServerWithHandling("settings", readySession, ServerFailureException.class);
	}

	/**
	 * Sets an implicit wait for all elements in milliseconds. It will last for the duration
	 * of the driver session, or until a new value is set. By default, when performing a finder().findElement
	 * command, the locator find will be evaluated immediately and throw an exception if the element is not immediately found.
	 * By setting this value, the server will search for the element repeatedly until the element is found, or will throw
	 * the NoSuchElementException if the element is not found after the duration expires. Setting this timeout is recommended
	 * but setting too high a value can result in increased test time.
	 *
	 * @param timeoutInMilliseconds long - the timeout in milliseconds to wait for an element before throwing an exception.
	 * @throws ServerFailureException If the timeout cannot be applied.
	 */
	public void setElementTimeout(long timeoutInMilliseconds) {
		JSONObject readySession = JsonUtils.deepCopy(session);
		readySession.put("action", "element_find_timeout");
		readySession.put("element_find_timeout", String.valueOf(timeoutInMilliseconds));
		serverPostHandler.postToServerWithHandling("settings", readySession, ServerFailureException.class);
	}

	/**
	 * Sets a poll interval for all elements in milliseconds. It will last for the duration
	 * of the driver session, or until a new value is set. Only applicable if an element timeout has been applied. By
	 * default the element poll interval is 250 milliseconds.
	 *
	 * @param pollIntervalInMilliseconds long - the poll interval in milliseconds.
	 * @throws ServerFailureException If the poll interval cannot be applied.
	 */
	public void setElementPollInterval(long pollIntervalInMilliseconds) {
		JSONObject readySession = JsonUtils.deepCopy(session);
		readySession.put("action", "element_polling_interval");
		readySession.put("element_polling_interval", String.valueOf(pollIntervalInMilliseconds));
		serverPostHandler.postToServerWithHandling("settings", readySession, ServerFailureException.class);
	}

	/**
	 * Sets a dely in milliseconds for remote control interactions. By default there is no pause between 
	 * remote control commands so remote interactions can happen very fast and may lead to test flake 
	 * depending on the test scenario. This option allows you to throttle those remote control commands.
	 * It will last for the duration of the driver session, or until a new value is set.
	 *
	 * @param delayInMilliseconds long - the pause between remote commands in milliseconds. i.e. '1000'
	 * will pause for 1 second between every remote control button press sent to the server. Defaults to 0.
	 * @throws ServerFailureException If the remote control delay cannot be applied.
	 */
	public void setRemoteInteractDelay(long delayInMilliseconds) {
		JSONObject readySession = JsonUtils.deepCopy(session);
		readySession.put("action", "remote_interact_delay");
		readySession.put("remote_interact_delay", String.valueOf(delayInMilliseconds));
		serverPostHandler.postToServerWithHandling("settings", readySession, ServerFailureException.class);
	}

	/**
	 * Sets a session status that can be later retrieved during the course of a session. By default the session status is 'In Progress'.
	 * Useful if you want to set a pass/fail/broken status during the course of a test run and then later retrieve the status
	 * for communicating with a 3rd party service. The status will last only so long as the session is active and will be lost
	 * once the user stops the session.
	 *
	 * @param status SessionStatus - the session status.
	 * @throws ServerFailureException If the session status cannot be applied.
	 */
	public void setSessionStatus(SessionStatus status) {
		JSONObject readySession = JsonUtils.deepCopy(session);
		readySession.put("action", "set_session_status");
		readySession.put("session_status", status.value());
		serverPostHandler.postToServerWithHandling("settings", readySession, ServerFailureException.class);
	}

	/**
	 * Gets the session status as set by calling the setSessionStatus() method.
	 *
	 * @return SessionStatus - the session status as set by the user during the course of the session.
	 * @throws ServerFailureException If the session status cannot be retrieved.
	 */
	public SessionStatus getSessionStatus() {
		JSONObject readySession = JsonUtils.deepCopy(session);
		readySession.put("action", "get_session_status");
		JSONObject results = serverPostHandler.postToServerWithHandling("settings", readySession, ServerFailureException.class);
		return SessionStatus.getEnumByString((String) results.get("session_status"));
	}

}
