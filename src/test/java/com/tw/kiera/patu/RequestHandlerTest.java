package com.tw.kiera.patu;

import com.sun.xml.internal.bind.v2.TODO;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

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
    public void shouldBeAbleToHandleARequestFromString() throws Exception {
        String request = "GET / HTTP/1.1\n\n";
        Response response = requestHandler.handleRequest(request);
        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().contains("<title>home</title>"));
    }

    @Test
    public void shouldRespondWithFileRequested() throws Exception {
        String request = "GET /link.html HTTP/1.1\n\n";
        Response response = requestHandler.handleRequest(request);
        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().contains("<title>link</title>"));
    }

    @Test
    public void shouldRespondWith404IfResourceForFileDoesNotExist() throws Exception {
        String request = getRequest("/nofile.html");
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
        String request = getRequest("/subfolder/subfile.txt");
        Response response = requestHandler.handleRequest(request);
        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().contains("hello"));
    }

    @Test
    public void shouldRespondWithResourceRequestedInParentFolder() throws Exception {
        String request = getRequest("/subfolder/../link.html");
        Response response = requestHandler.handleRequest(request);
        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().contains("<title>link</title>"));
    }

    @Test
    public void shouldRespondWith404IfResourceForFileOutsideOfDocFolder() throws Exception {
        String request = getRequest("/../kiera-secret-file.txt");
        Response response = requestHandler.handleRequest(request);
        assertEquals(404, response.getStatusCode());
    }

    private String getRequest(String path) {
        return String.format("GET %s HTTP/1.1\n\n", path);
    }
}
