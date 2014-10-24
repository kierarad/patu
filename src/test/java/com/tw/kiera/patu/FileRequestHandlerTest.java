package com.tw.kiera.patu;

import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class FileRequestHandlerTest {

    private FileRequestHandler requestHandler;

    @Before
    public void createHandler() {
       TestSettings.init();
       requestHandler = new FileRequestHandler();
    }

    @Test
    public void shouldDeliverBinaryFilesWithProperEncoding() throws UnsupportedEncodingException {
        Request req = new TestRequest("/clear.gif");
        Response response = requestHandler.handleRequest(req);
        assertThat(Base64.encodeBase64String(response.getBody().getBytes("ISO-8859-1")), equalTo("R0lGODlhAQABAIABAAAAAP///yH5BAEAAAEALAAAAAABAAEAAAICTAEAOw=="));
    }

    @Test
    public void shouldRedirectToIndexWhenRequestRoot() throws Exception {
        Request req = new TestRequest("");
        Response response = requestHandler.handleRequest(req);
        assertEquals(302, response.getStatusCode());
    }

    @Test
    public void shouldRespondWithFileRequested() throws Exception {
        Request req = new TestRequest("/link.html");
        Response response = requestHandler.handleRequest(req);
        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().contains("<title>link</title>"));
    }

    @Test
    public void shouldRespondWith404IfResourceForFileDoesNotExist() throws Exception {
        Request req = new TestRequest("/nonexistent.html");
        Response response = requestHandler.handleRequest(req);
        assertEquals(404, response.getStatusCode());
    }

    @Test
    public void shouldRespondWithResourceIfInSubfolder() throws Exception {
        Request req = new TestRequest("/subfolder/subfile.txt");
        Response response = requestHandler.handleRequest(req);
        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().contains("hello"));
    }

    @Test
    public void shouldRespondWithResourceRequestedInParentFolder() throws Exception {
        Request req = new TestRequest("/subfolder/../link.html");
        Response response = requestHandler.handleRequest(req);
        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().contains("<title>link</title>"));
    }

    @Test
    public void shouldRespondWith404IfResourceForFileOutsideOfDocFolder() throws Exception {
        Request req = new TestRequest("/../kiera-secret-file.txt");
        Response response = requestHandler.handleRequest(req);
        assertEquals(404, response.getStatusCode());
        assertFalse(response.getBody().contains("PASSWORDS"));
    }

}
