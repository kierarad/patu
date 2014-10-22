package com.tw.kiera.patu;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by ThoughtWorker on 8/26/14.
 */
public class RequestParserTest {

    private Main server;
    private RequestParser requestHandler;

    @Before
    public void createRequestHandler() throws Exception {
        Settings.getInstance().setDocRoot(TestSettings.TEST_DOCROOT);
        this.requestHandler = new RequestParser();
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
