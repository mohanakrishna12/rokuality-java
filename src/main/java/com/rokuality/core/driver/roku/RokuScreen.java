package com.rokuality.core.driver.roku;

import com.rokuality.core.driver.Element;
import com.rokuality.core.driver.Screen;
import com.rokuality.core.exceptions.ScreenException;
import com.rokuality.core.httpexecutor.HttpClient;
import com.rokuality.core.utils.JsonUtils;

import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class RokuScreen extends Screen {

	public RokuScreen(HttpClient httpClient, JSONObject session) {
		super(httpClient, session);
	}

	/**
	 * Gets the xml page source of the Roku device under test.
	 * 
	 * @return String - The application page source.
	 */
	public String getPageSource() {
		JSONObject readySession = JsonUtils.deepCopy(session);
		readySession.put("action", "get_screen_source");
		JSONObject jsonResults = serverPostHandler.postToServerWithHandling("screen", readySession, ScreenException.class);
		return (String) jsonResults.get("source");
	}

	/**
	 * Gets the current focused element within the app under test.
	 * 
	 * @return Element - The currently focused element.
	 */
	public Element getActiveElement() {
		JSONObject readySession = JsonUtils.deepCopy(session);
		readySession.put("action", "get_active_element");
		JSONObject jsonResults = serverPostHandler.postToServerWithHandling("screen", readySession, ScreenException.class);
		return new Element(jsonResults);
	}

}
