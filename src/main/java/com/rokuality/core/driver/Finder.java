package com.rokuality.core.driver;

import com.rokuality.core.exceptions.NoSuchElementException;
import com.rokuality.core.httpexecutor.HttpClient;
import com.rokuality.core.utils.FileToStringUtils;
import com.rokuality.core.utils.JsonUtils;

import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class Finder {

	JSONObject session = null;
	private ServerPostHandler serverPostHandler = null;
	
	public Finder(HttpClient httpClient, JSONObject session) {
		this.session = session;
		serverPostHandler = new ServerPostHandler(httpClient);
	}

	/**
	 * Searches for the existence of a locator within the device screen.
	 *
	 * @param by By - The locator to search for.
	 * @return Element - An Element object containing details about the elements location and contents.
	 * @throws NoSuchElementException If the locator can not be found on the screen.
	 */
	public Element findElement(By by) {
		by = new FileToStringUtils().prepareLocator(by);
		JSONObject readySession = JsonUtils.deepCopy(session);
		readySession.put("action", "find");
		readySession.put("element_locator", by.toString());
		return new Element(serverPostHandler.postToServerWithHandling("element", readySession, NoSuchElementException.class));
	}

	/**
	 * Searches for the existence of a locator within the device sub screen starting at
	 * subScreenX, subScreenY, and with subScreenWidth and subScreenHeight.
	 *
	 * @param by By - The locator to search for in the device subscreen.
	 * @param subScreenX int - The x coordinate starting point of the subscreen.
	 * @param subScreenY int - The y coordinate starting point of the subscreen.
	 * @param subScreenWidth int - The subscreen width ending point.
	 * @param subScreenHeight int - The subscreen height ending point.
	 * 
	 * @return Element - An Element object containing details about the elements location and contents.
	 * @throws NoSuchElementException If the locator can not be found on the screen.
	 */
	public Element findElement(By by, int subScreenX, int subScreenY, int subScreenWidth, int subScreenHeight) {
		by = new FileToStringUtils().prepareLocator(by);
		JSONObject readySession = JsonUtils.deepCopy(session);
		readySession.put("action", "find");
		readySession.put("element_locator", by.toString());
		readySession.put("sub_screen_x", subScreenX);
		readySession.put("sub_screen_y", subScreenY);
		readySession.put("sub_screen_width", subScreenWidth);
		readySession.put("sub_screen_height", subScreenHeight);
		return new Element(serverPostHandler.postToServerWithHandling("element", readySession, NoSuchElementException.class));
	}

}
