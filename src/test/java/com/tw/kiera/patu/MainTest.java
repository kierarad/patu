package com.tw.kiera.patu;

import com.google.common.io.ByteSource;
import org.junit.*;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;

public class MainTest {

    public static final int DEFAULT_PORT = 8080;
    private Main server;

    @Before
    public void startServer() throws Exception {
        server = new Main();
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
        assertTrue(canConnectTo(DEFAULT_PORT));
    }

    @Test
    public void allowAllowPortToBeSpecifiedWithCmdLineArgs() throws InterruptedException {
        server.stop();
        server = new Main("-p", "7777");
        server.startAsync();
        while(!server.isRunning()) {
            Thread.sleep(100);
        }
        assertEquals(7777, server.getPort());

    }

    @Test
    public void pieceTogetherARequest() throws Exception {
        final String request = "GET / HTTP/1.0\n";
        InputStream neverEndingInput = new InputStream() {

            private int currentPosition = 0;

            @Override
            public int read() throws IOException {
                if (currentPosition < request.length()) {
                    return request.getBytes()[currentPosition++];
                } else {
                    try {
                        synchronized (this) {
                            this.wait();
                        }

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                throw new RuntimeException();
            }
        };
        String requestString = Main.buildRequest(neverEndingInput);
        assertEquals(requestString, request.trim());
    }

    private boolean canConnectTo(int port) throws Exception {
        Socket s = new Socket(InetAddress.getLocalHost(), port);
        return s.isConnected();
    }
}
