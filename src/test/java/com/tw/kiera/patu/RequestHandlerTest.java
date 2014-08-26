package com.tw.kiera.patu;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by ThoughtWorker on 8/26/14.
 */
public class RequestHandlerTest {

    private Main server;

    @Before
    public void startServer() throws Exception {
        this.server = new Main();
        server.startAsync();
        while(!server.isRunning()) {
            Thread.sleep(100);
        }
    }

    @After
    public void shutdownServer() throws Exception {
        this.server.stop();
    }

    @Test
    public void shouldBeAbleToHandleARequest()throws Exception {
        HttpResponse request = getRequest("/");
        assertNotNull(request);
        assertEquals(200, request.getStatusLine().getStatusCode());
        ByteArrayOutputStream body = new ByteArrayOutputStream();
        request.getEntity().writeTo(body);
        assertTrue(String.valueOf(body).contains("<h1>"));
    }

    @Test
    public void shouldRespondWithASpecificFile() throws Exception {
        HttpResponse request = getRequest("/link.html");
        assertNotNull(request);
        assertEquals(200, request.getStatusLine().getStatusCode());
        ByteArrayOutputStream body = new ByteArrayOutputStream();
        request.getEntity().writeTo(body);
        assertTrue(String.valueOf(body).contains("<h1>Link"));
    }

    @Test
    public void shouldRespondWith404IfResourceForFileDoesNotExist() throws Exception {
        HttpResponse request = getRequest("/nofile.html");
        assertNotNull(request);
        assertEquals(404, request.getStatusLine().getStatusCode());
    }

    @Test
    public void shouldRespondToBadRequest() throws Exception {
        String requestLine = "GET\n\n";
        Socket client = new Socket("localhost", MainTest.DEFAULT_PORT);
        OutputStream webServer = client.getOutputStream();
        webServer.write(requestLine.getBytes());
        String response = IOUtils.toString(client.getInputStream());
        System.out.println("Body: " + response);

    }

    private HttpResponse getRequest(String resourceRequested) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://localhost:" + MainTest.DEFAULT_PORT + resourceRequested);
        CloseableHttpResponse response = httpclient.execute(httpget);
        return response;
    }
}
