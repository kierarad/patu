package com.tw.kiera.patu;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FileRequestHandlerTest {

    private FileRequestHandler requestHandler;

    private final static String TEST_DOCROOT = "./src/test/data";

    @Before
    public void createHandler() {
        Settings.getInstance().setDocRoot(TEST_DOCROOT);
       requestHandler = new FileRequestHandler();
    }

    @Test
    public void shouldRedirectToIndexWhenRequestRoot() throws Exception {
        Request req = new Request(validHeaders(), "");
        Response response = requestHandler.handleRequest(req);
        assertEquals(302, response.getStatusCode());
    }

    @Test
    public void shouldRespondWithFileRequested() throws Exception {
        Request req = new Request(validHeaders(), "link.html");
        Response response = requestHandler.handleRequest(req);
        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().contains("<title>link</title>"));
    }

    @Test
    public void shouldRespondWith404IfResourceForFileDoesNotExist() throws Exception {
        Request req = new Request(validHeaders(), "nonexistent.html");
        Response response = requestHandler.handleRequest(req);
        assertEquals(404, response.getStatusCode());
    }

    @Test
    public void shouldRespondWithResourceIfInSubfolder() throws Exception {
        Request req = new Request(validHeaders(), "subfolder/subfile.txt");
        Response response = requestHandler.handleRequest(req);
        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().contains("hello"));
    }

    @Test
    public void shouldRespondWithResourceRequestedInParentFolder() throws Exception {
        Request req = new Request(validHeaders(), "subfolder/../link.html");
        Response response = requestHandler.handleRequest(req);
        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().contains("<title>link</title>"));
    }

    @Test
    public void shouldRespondWith404IfResourceForFileOutsideOfDocFolder() throws Exception {
        Request req = new Request(validHeaders(), "../kiera-secret-file.txt");
        Response response = requestHandler.handleRequest(req);
        assertEquals(404, response.getStatusCode());
        assertFalse(response.getBody().contains("PASSWORDS"));
    }


    private Map<String, String> validHeaders() {
        return Collections.singletonMap("Host", "localhost");
    }
}
