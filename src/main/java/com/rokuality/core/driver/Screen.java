package com.rokuality.core.driver;

import java.io.File;
import java.util.List;

import com.rokuality.core.exceptions.ScreenException;
import com.rokuality.core.httpexecutor.HttpClient;
import com.rokuality.core.utils.FileToStringUtils;
import com.rokuality.core.utils.JsonUtils;

import java.awt.Dimension;

import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class Screen {

	JSONObject session = null;
	private ServerPostHandler serverPostHandler = null;
	
	public Screen(HttpClient httpClient, JSONObject session) {
		this.session = session;
		serverPostHandler = new ServerPostHandler(httpClient);
	}

	/**
	 * Gets the device screen image and saves it to a temporary file on your machine.
	 *
	 * @return File - The device screen image at the time of capture.
	 * @throws ScreenException If the device screen fails to capture.
	 */
	public File getImage() {
		session.put("action", "get_screen_image");
		JSONObject jsonResults = serverPostHandler.postToServerWithHandling("screen", session, ScreenException.class);
		return new FileToStringUtils().convertToFile(String.valueOf(jsonResults.get("screen_image")),
				String.valueOf(jsonResults.get("screen_image_extension")));
	}

	/**
	 * Gets the device sub screen image and saves it to a temporary file on your machine.
	 *
	 * @param subScreenX int - The x coordinate starting point of the subscreen to capture.
	 * @param subScreenY int - The y coordinate starting point of the subscreen to capture.
	 * @param subScreenWidth int - The subscreen width ending point to capture.
	 * @param subScreenHeight - int The subscreen height ending point to capture.
	 * 
	 * @return File - The device sub screen image at the time of capture.
	 * @throws ScreenException If the device sub screen fails to capture.
	 */
	public File getImage(int subScreenX, int subScreenY, int subScreenWidth, int subScreenHeight) {
		session.put("action", "get_screen_image");
		session.put("sub_screen_x", subScreenX);
		session.put("sub_screen_y", subScreenY);
		session.put("sub_screen_width", subScreenWidth);
		session.put("sub_screen_height", subScreenHeight);
		JSONObject jsonResults = serverPostHandler.postToServerWithHandling("screen", session, ScreenException.class);
		return new FileToStringUtils().convertToFile(String.valueOf(jsonResults.get("screen_image")),
				String.valueOf(jsonResults.get("screen_image_extension")));
	}

	/**
	 * Gets the device screen text as a ScreenText collection with details about each found word on the screen.
	 *
	 * @return com.rokuality.core.driver.ScreenText - A list of ScreenText objects containing details of every found word on the device screen.
	 * @throws ScreenException If the device screen text fails to capture.
	 */
	public List<ScreenText> getText() {
		session.put("action", "get_screen_text");
		JSONObject jsonResults = serverPostHandler.postToServerWithHandling("screen", session, ScreenException.class);
		return JsonUtils.fromJsonToList(jsonResults.get("screen_text").toString(), ScreenText.class);
	}

	/**
	 * Gets the device screen text from the identified device sub screen as a ScreenText 
	 * collection with details about each found word on the screen.
	 *
	 * @param subScreenX int - The x coordinate starting point of the subscreen to capture.
	 * @param subScreenY int - The y coordinate starting point of the subscreen to capture.
	 * @param subScreenWidth int - The subscreen width ending point to capture.
	 * @param subScreenHeight - int The subscreen height ending point to capture.
	 * 
	 * @return com.rokuality.core.driver.ScreenText - A list of ScreenText objects containing details of every found word on the device sub screen.
	 * @throws ScreenException If the device sub screen text fails to capture.
	 */
	public List<ScreenText> getText(int subScreenX, int subScreenY, int subScreenWidth, int subScreenHeight) {
		session.put("action", "get_screen_text");
		session.put("sub_screen_x", subScreenX);
		session.put("sub_screen_y", subScreenY);
		session.put("sub_screen_width", subScreenWidth);
		session.put("sub_screen_height", subScreenHeight);
		JSONObject jsonResults = serverPostHandler.postToServerWithHandling("screen", session, ScreenException.class);
		return JsonUtils.fromJsonToList(jsonResults.get("screen_text").toString(), ScreenText.class);
	}

	/**
	 * Gets the device screen text as a complete String.
	 *
	 * @return String - A complete string of every word found on the screen.
	 * @throws ScreenException If the device screen text fails to capture.
	 */
	public String getTextAsString() {
		List<ScreenText> screenText = getText();
		String constructedTxt = "";
		for (ScreenText text : screenText) {
			constructedTxt += " " + text.getText();
		}
		return constructedTxt;
	}

	/**
	 * Gets the device sub screen text as a complete String.
	 *
	 * @param subScreenX int - The x coordinate starting point of the subscreen to capture.
	 * @param subScreenY int - The y coordinate starting point of the subscreen to capture.
	 * @param subScreenWidth int - The subscreen width ending point to capture.
	 * @param subScreenHeight - int The subscreen height ending point to capture.
	 * 
	 * @return String - A complete string of every word found on the screen.
	 * @throws ScreenException If the device screen text fails to capture.
	 */
	public String getTextAsString(int subScreenX, int subScreenY, int subScreenWidth, int subScreenHeight) {
		List<ScreenText> screenText = getText(subScreenX, subScreenY, subScreenWidth, subScreenHeight);
		String constructedTxt = "";
		for (ScreenText text : screenText) {
			constructedTxt += " " + text.getText();
		}
		return constructedTxt;
	}

	/**
	 * Gets the device screen size.
	 *
	 * @return Dimension - The size of the device under test.
	 * @throws ScreenException If the device screen size is not determined.
	 */
	public Dimension getSize() {
		session.put("action", "get_screen_size");
		JSONObject jsonResults = serverPostHandler.postToServerWithHandling("screen", session, ScreenException.class);
		Dimension dimension = new Dimension(Integer.parseInt(jsonResults.get("screen_width").toString()),
				Integer.parseInt(jsonResults.get("screen_height").toString()));
		return dimension;
	}

	/**
	 * Gets the device screen recording from the driver start to current. Note the recording is generated
	 * in .mp4 format but is done through stitching the collected device screenshots together from the
	 * start of the driver seesion - and the quality of the capture won't be the best. But very useful
	 * for reporting and debugging.
	 *
	 * @return File - An .mp4 video of the driver session from start until current.
	 * @throws ScreenException If the video recording cannot be captured.
	 */
	public File getRecording() {
		session.put("action", "get_screen_recording");
		JSONObject jsonResults = serverPostHandler.postToServerWithHandling("screen", session, ScreenException.class);
		return new FileToStringUtils().convertToFile(String.valueOf(jsonResults.get("screen_video")),
				String.valueOf(jsonResults.get("screen_video_extension")));
	}

}
