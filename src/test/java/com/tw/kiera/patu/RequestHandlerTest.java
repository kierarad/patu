package com.tw.kiera.patu;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by ThoughtWorker on 8/26/14.
 */
public class RequestHandlerTest {

    private final static String TEST_DOCROOT = "./src/test/data";
    private Main server;
    private RequestHandler requestHandler;

    @Before
    public void createRequestHandler() throws Exception {
        this.requestHandler = new RequestHandler(TEST_DOCROOT);
    }

    @Test
    public void shouldRedirectToIndexWhenRequestRoot() throws Exception {
        String request = validGetRequest("/");
        Response response = requestHandler.handleRequest(request);
        assertEquals(302, response.getStatusCode());
    }

    @Test
    public void shouldRespondWith400IfHostHeaderNotIncluded() throws Exception {
        String requestWithoutHostHeader = "GET / HTTP/1.1\n\n";
        Response response = requestHandler.handleRequest(requestWithoutHostHeader);
        assertEquals(400, response.getStatusCode());
        assertTrue(response.getBody().contains("Missing required header: Host"));
    }

    @Test
    public void shouldRespondWithFileRequested() throws Exception {
        String request = validGetRequest("/link.html");
        Response response = requestHandler.handleRequest(request);
        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().contains("<title>link</title>"));
    }

    @Test
    public void shouldRespondWith404IfResourceForFileDoesNotExist() throws Exception {
        String request = validGetRequest("/nofile.html");
        Response response = requestHandler.handleRequest(request);
        assertEquals(404, response.getStatusCode());
    }

    @Test
    public void shouldRespondToBadRequest() throws Exception {
        String request = "this-is-bad.txt";
        Response response = requestHandler.handleRequest(request);
        assertEquals(400, response.getStatusCode());
        assertEquals("Bad Request", response.getStatusLine());
    }

    @Test
    public void shouldRespondWithResourceIfInSubfolder() throws Exception {
        String request = validGetRequest("/subfolder/subfile.txt");
        Response response = requestHandler.handleRequest(request);
        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().contains("hello"));
    }

    @Test
    public void shouldRespondWithResourceRequestedInParentFolder() throws Exception {
        String request = validGetRequest("/subfolder/../link.html");
        Response response = requestHandler.handleRequest(request);
        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().contains("<title>link</title>"));
    }

    @Test
    public void shouldRespondWith404IfResourceForFileOutsideOfDocFolder() throws Exception {
        String request = validGetRequest("/../kiera-secret-file.txt");
        Response response = requestHandler.handleRequest(request);
        assertEquals(404, response.getStatusCode());
    }

    private String validGetRequest(String path) {

        return String.format("GET %s HTTP/1.1\nHost: example.com\n\n", path);
    }
}
