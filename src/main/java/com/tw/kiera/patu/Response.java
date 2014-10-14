package com.tw.kiera.patu;

/**
 * Created by ThoughtWorker on 9/23/14.
 */
public class Response {

    public static final Response NOT_FOUND = new Response(404, "Not Found");
    public static final Response BAD_REQUEST = new Response(400, "Bad Request");
    private String statusLine;

    private int statusCode;
    private String body;

    public Response() {}

    public Response(int statusCode, String statusLine) {
        this.statusCode = statusCode;
        this.statusLine = statusLine;
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
        return String.format("HTTP/1.1 %d %s\n%s", statusCode, statusLine, body);
    }
}
