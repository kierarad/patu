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
        Settings.getInstance().setDocRoot(TestSettings.TEST_DOCROOT);
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

    private String validGetRequest(String path) {

        return String.format("GET %s HTTP/1.1\nHost: example.com\n\n", path);
    }
}
