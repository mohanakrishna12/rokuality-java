package com.rokuality.core.driver;

import java.io.File;
import java.util.Iterator;

import com.rokuality.core.exceptions.ServerFailureException;
import com.rokuality.core.utils.FileToStringUtils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
		JSONObject deepCopySession = null;
		try {
			deepCopySession = (JSONObject) new JSONParser().parse(session.toJSONString());
		} catch (ParseException e) {
			throw new ServerFailureException("Failed to retrieve session details!");
		}
		
		return deepCopySession;
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
