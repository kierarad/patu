package com.tw.kiera.patu;

import java.util.Map;

public class Request {
    private final Map<String, String> headers;
    private final String path;

    public Request(Map<String, String> headers, String path) {
        this.headers = headers;
        this.path = path;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getPath() {
        return path;
    }
}
