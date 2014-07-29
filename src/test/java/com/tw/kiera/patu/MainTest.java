package com.tw.kiera.patu;

import org.junit.*;
import static org.junit.Assert.*;
import java.net.*;

public class MainTest {


  @Test
  public void shouldListenOn8080() throws Exception {
    Main server = startServer();
    assertTrue(canConnectTo(8080));
  }

  private Main startServer() throws Exception {
    Main server = new Main();
    server.startAsync();
    while(!server.isRunning()) {
        Thread.sleep(100);
    }
    return server;
  }


  private boolean canConnectTo(int port) throws Exception {
    Socket s = new Socket(InetAddress.getLocalHost(), 8080);
    return s.isConnected();
  }

}
