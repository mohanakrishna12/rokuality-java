package com.rokuality.core.httpexecutor;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.rokuality.core.exceptions.ServerFailureException;

public class HttpClient {

    private static final int DEFAULT_TIMEOUT = 90;

    private String serverURL = null;

    public HttpClient(String serverURL) {
        this.serverURL = serverURL;
    }

    public JSONObject postToServer(String servletName, JSONObject jsonRequest) {

        int socketTimeout = DEFAULT_TIMEOUT;
        int connectTimeout = DEFAULT_TIMEOUT;

        String responseContent = null;

        int responseCode = 500;

        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) new URL(serverURL + "/" + servletName).openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            con.setConnectTimeout((connectTimeout * 1000));
            con.setReadTimeout((socketTimeout * 1000));

            try (OutputStream outputStream = con.getOutputStream()) {
                byte[] input = jsonRequest.toJSONString().getBytes("utf-8");
                outputStream.write(input, 0, input.length);
            } catch (Exception e) {
                e.printStackTrace();
            }

            responseCode = con.getResponseCode();

            InputStreamReader inputStreamReader = null;
            if (responseCode == 200) {
                inputStreamReader = new InputStreamReader(con.getInputStream(), "utf-8");
            } else {
                inputStreamReader = new InputStreamReader(con.getErrorStream(), "utf-8");
            }

            try (BufferedReader br = new BufferedReader(inputStreamReader)) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                responseContent = response.toString();
                br.close();
            }

            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (con != null) {
                con.disconnect();
            }
        }

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonResponseObj = null;

        if (responseContent != null) {
            try {
                jsonResponseObj = (JSONObject) jsonParser.parse(responseContent);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (responseCode != 200 && jsonResponseObj == null) {
            throw new ServerFailureException(String.format(
                    "The server failed to respond at %s. Is the server listening at this location?", String.valueOf(serverURL)));
        }

        if (responseCode != 200 && jsonResponseObj != null && !jsonResponseObj.containsKey("results")) {
            throw new ServerFailureException(String.format(
                    "The server responded at %s with an unknown error!", String.valueOf(serverURL)));
        }

        return jsonResponseObj;
    }

}