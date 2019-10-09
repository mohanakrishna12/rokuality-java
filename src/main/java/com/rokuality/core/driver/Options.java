package com.rokuality.core.driver;

import com.rokuality.core.exceptions.ServerFailureException;
import com.rokuality.core.httpexecutor.HttpClient;

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
		session.put("action", "image_match_similarity");
		session.put("image_match_similarity", String.valueOf(imageMatchSimilarity));
		serverPostHandler.postToServerWithHandling("settings", session, ServerFailureException.class);
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
		session.put("action", "element_find_timeout");
		session.put("element_find_timeout", String.valueOf(timeoutInMilliseconds));
		serverPostHandler.postToServerWithHandling("settings", session, ServerFailureException.class);
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
		session.put("action", "element_polling_interval");
		session.put("element_polling_interval", String.valueOf(pollIntervalInMilliseconds));
		serverPostHandler.postToServerWithHandling("settings", session, ServerFailureException.class);
	}

}
