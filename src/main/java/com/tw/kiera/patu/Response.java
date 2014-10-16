package com.tw.kiera.patu;

/**
 * Created by ThoughtWorker on 9/23/14.
 */
public class Response {

    public static final Response NOT_FOUND = new Response(404, "Not Found");
    private String statusLine;

    private int statusCode;
    private String body;

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
        return String.format("HTTP/1.1 %d %s\n%s", statusCode, statusLine, body);
    }
}
