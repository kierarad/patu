package com.tw.kiera.patu;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.*;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.net.*;

public class MainTest {

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
  public void shouldListenOn8080() throws Exception {
    assertTrue(canConnectTo(8080));
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

  private HttpResponse getRequest(String resourceRequested) throws Exception {
    CloseableHttpClient httpclient = HttpClients.createDefault();
    HttpGet httpget = new HttpGet("http://localhost:8080" + resourceRequested);
    CloseableHttpResponse response = httpclient.execute(httpget);
    return response;
  }

  private boolean canConnectTo(int port) throws Exception {
    Socket s = new Socket(InetAddress.getLocalHost(), 8080);
    return s.isConnected();
  }
}
