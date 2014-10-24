package com.tw.kiera.patu;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class RequestParserTest {

    private Main server;
    private RequestParser requestHandler;

    @Before
    public void createRequestHandler() throws Exception {
        TestSettings.init();
        this.requestHandler = new RequestParser();
    }

    @Test
    public void shouldProperlyHandleUrlEscapedPaths() throws IOException {
        String request = validGetRequest("/file+with+spaces.html");
        Response response = requestHandler.parseAndHandleRequest(request);
        assertThat(response.getStatusCode(), equalTo(200));
    }

    @Test
    public void shouldRespondWith400IfHostHeaderNotIncluded() throws Exception {
        String requestWithoutHostHeader = "GET / HTTP/1.1\n\n";
        Response response = requestHandler.parseAndHandleRequest(requestWithoutHostHeader);
        assertEquals(400, response.getStatusCode());
        assertTrue(response.getBody().contains("Missing required header: Host"));
    }

    @Test
    public void shouldRespondToBadRequest() throws Exception {
        String request = "this-is-bad.txt";
        Response response = requestHandler.parseAndHandleRequest(request);
        assertEquals(400, response.getStatusCode());
        assertEquals("Bad Request", response.getStatusLine());
    }

    @Test
    public void shouldPromptUserForAuthCredentials() throws Exception {
        Settings.getInstance().setBasicAuthOn(true);
        String requestWithoutAuth = "GET / HTTP/1.1\nHost: example.com\n\n";
        Response response = requestHandler.parseAndHandleRequest(requestWithoutAuth);
        assertEquals(401, response.getStatusCode());
        assertEquals("Basic realm='patu'", response.getHeader("WWW-Authenticate"));
    }

    private String validGetRequest(String path) {
        return String.format("GET %s HTTP/1.1\nHost: example.com\n\n", path);
    }
}
