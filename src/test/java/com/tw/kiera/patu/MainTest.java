package com.tw.kiera.patu;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

public class MainTest {

    @Before
    public void startServer() throws InterruptedException {
      Main server = new Main("-p", "8888", "-d", "src/test/data");
      server.startAsync();
      while(!server.isRunning()) {
          Thread.sleep(100);
      }
    }

    @Test
    public void getRootShouldRespondWith200() throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet getRoot = new HttpGet("http://localhost:8888/index.html");
        CloseableHttpResponse response = client.execute(getRoot);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }


}
