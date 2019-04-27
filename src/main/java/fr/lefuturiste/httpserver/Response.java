package fr.lefuturiste.httpserver;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Response {
    private int statusCode;
    private String statusText;
    private String protocol = "HTTP/1.1";
    private Map<String, String> headers = new HashMap<>();
    private String body;
    private Map<Integer, String> statuses = new HashMap<>();

    public Response() {
        this.statuses.put(100, "Continue");
        this.statuses.put(200, "OK");
        this.statuses.put(204, "No Content");
        this.statuses.put(206, "Partial Content");
        this.statuses.put(301, "Moved Permanently");
        this.statuses.put(302, "Found");
        this.statuses.put(304, "Not Modified");
        this.statuses.put(307, "Temporary Redirect");
        this.statuses.put(400, "Bad Request");
        this.statuses.put(401, "Unauthorized");
        this.statuses.put(403, "Forbidden");
        this.statuses.put(404, "Not Found");
        this.statuses.put(405, "Method Not Allowed");
        this.statuses.put(408, "Request Timeout");
        this.statuses.put(412, "Precondition Failed");
        this.statuses.put(413, "Request Entity Too Large");
        this.statuses.put(414, "Request-URI Too Large");
        this.statuses.put(416, "Requested Range Not Satisfiable");
        this.statuses.put(417, "Expectation Failed");
        this.statuses.put(418, "I'm a teapot");
        this.statuses.put(429, "Too many requests");
        this.statuses.put(500, "Internal Server Error");
        this.statuses.put(501, "Not Implemented");
        this.statuses.put(502, "Bad Gateway");
        this.statuses.put(503, "Service Unavailable");
        this.statuses.put(504, "Gateway Time-out");
    }

    public Response setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        if (this.statuses.containsKey(statusCode)) {
            this.statusText= this.statuses.get(statusCode);
        }
        return this;
    }

    public String getStatusText() {
        return this.statusText;
    }

    public Map<Integer, String> getStatuses() {
        return this.statuses;
    }

    public Response setProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    public Response putHeader(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    public Response putHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
        return this;
    }

    public Response setStatusText(String statusText) {
        this.statusText = statusText;
        return this;
    }

    public Response setBody(String body) {
        this.body = body;
        return this;
    }

    public boolean hasHeader(String key) {
        return this.headers.containsKey(key);
    }

    public ArrayList<String> toLines() {
        ArrayList<String> lines = new ArrayList<>();
        lines.add(this.protocol + " " + this.statusCode + " " + this.statusText);
        for (Map.Entry<String, String> header : this.headers.entrySet())
        {
            lines.add(header.getKey() + ": " + header.getValue());
        }
        lines.add("\r\n");
        lines.addAll(Arrays.asList(this.body.split("\n")));
        return lines;
    }

    public Response withJson(JSONObject jsonObject) {
        this.body = jsonObject.toString(0);
        this.headers.put("Content-Type", "application/json");
        return this;
    }
}