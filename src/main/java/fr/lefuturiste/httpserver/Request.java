package fr.lefuturiste.httpserver;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Request {
    private String method;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> params = new HashMap<>();
    private Map<String, String> queryParams = new HashMap<>();
    private String body = null;
    private String protocol;
    private String url;

    public void setMethod(String method) {
        this.method = method;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean hasHeader() {
        return this.body != null;
    }

    public String getBody() {
        return body;
    }

    public String getProtocol() {
        return protocol;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public Map<String, String> getQueryParams() {
        return this.queryParams;
    }

    public Map<String, String> getParams() {
        return this.params;
    }

    public String getParam(String key) {
        return this.params.get(key);
    }

    public boolean hasQueryParam(String key) {
        return this.queryParams.containsKey(key);
    }

    public String getQueryParam(String key) {
        return this.queryParams.get(key);
    }

    public void setQueryParams(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }

    public void addParam(String key, String value) {
        this.params.put(key, value);
    }

    public JSONObject getJsonParsedBody() {
        if (!this.body.isEmpty()) {
            return new JSONObject(this.body);
        }
        return null;
    }
}
