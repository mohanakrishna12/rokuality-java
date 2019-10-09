package com.rokuality.core.driver;

import java.lang.reflect.InvocationTargetException;

import com.rokuality.core.driver.BaseDriver;
import com.rokuality.core.httpexecutor.HttpClient;

import org.json.simple.JSONObject;

public class ServerPostHandler extends BaseDriver {

	private HttpClient httpClient = null;

	public ServerPostHandler(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public <T extends Throwable> JSONObject postToServerWithHandling(String servletName, JSONObject requestJSON,
			Class<T> exceptionType) throws T {
		JSONObject resultJSON = httpClient.postToServer(servletName, requestJSON);
		
		String result = (String) resultJSON.get("results");
		if (!result.equals("success")) {
			try {
				throw exceptionType.getConstructor(String.class).newInstance(result);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}

		return resultJSON;
	}

}
