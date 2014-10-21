package com.tw.kiera.patu;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;

import java.util.*;

/**
 * Created by ThoughtWorker on 9/23/14.
 */
public class Response {

    public static final Response NOT_FOUND = new Response(404, "Not Found");
    private String statusLine;

    private int statusCode;
    private String body;

    private Map <String, String> headers = new HashMap<String, String>();
    {
        headers.put("Connection", "close");
    }

    public Response() {}

    public Response(int statusCode, String statusLine) {
        this(statusCode, statusLine, null);
    }

    public Response(int statusCode, String statusLine, String body) {
        this.statusCode = statusCode;
        this.statusLine = statusLine;
        this.body = body;
    }

    public static Response badRequest(String body) {
        return new Response(400, "Bad Request", body);
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getBody() {
        return this.body;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getStatusLine() {
        return statusLine;
    }

    public String toString() {
        List<String> lines = new ArrayList<String>(Arrays.asList(String.format("HTTP/1.1 %d %s", statusCode, statusLine),
                                                          toHTTPHeaderString(),
                                                          ""));
        if (body != null) {
            lines.add(body);
        }
        return Joiner.on("\r\n").join(lines);
    }

    public String toHTTPHeaderString() {
        Collection<String> headerLines = Collections2.transform(headers.entrySet(), new Function<Map.Entry<String, String>, String>() {
            public String apply(Map.Entry<String, String> nameAndValuePair) {
                return nameAndValuePair.getKey() + ": " + nameAndValuePair.getValue();
            }
        });
        return Joiner.on("\n").join(headerLines);
    }

    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public static Response redirectTo(String location) {
        Response response = new Response(302, "Found");
        response.setHeader("Location", location);
        return response;
    }
}
