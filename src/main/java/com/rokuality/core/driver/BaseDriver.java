package com.rokuality.core.driver;

import com.rokuality.core.exceptions.ServerFailureException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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

}
