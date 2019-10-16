package com.rokuality.core.driver;

import java.io.File;
import java.util.Iterator;

import com.rokuality.core.utils.FileToStringUtils;
import com.rokuality.core.utils.JsonUtils;

import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class BaseDriver {

	private JSONObject session = null;
	private String serverURL = null;

	public BaseDriver() {

	}

	protected void setSession(JSONObject session) {
		this.session = session;
	}

	public JSONObject getSession() {
		return JsonUtils.deepCopy(session);
	}

	protected void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}

	protected String getServerURL() {
		return serverURL;
	}

	protected JSONObject prepareCapJSON(JSONObject capabilities) {
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
