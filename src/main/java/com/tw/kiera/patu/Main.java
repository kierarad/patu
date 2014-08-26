package com.tw.kiera.patu;

import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.regex.*;

class Main {

	private boolean isRunning;
    private Thread mainThread;
    private ServerSocket serverSocket;

    public void startAsync() {
		this.mainThread = new Thread(new Runnable(){
			public void run() {
				startSync();
			}
		});
        this.mainThread.start();
    }



	public void startSync() {
		try {
			listenOnPort();

			isRunning = true;
			while(true) {
				System.out.println("Listening again for a client");
				final Socket client = serverSocket.accept();
                new RequestHandler().handleRequest(client);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}



    private void listenOnPort() throws IOException {
        System.out.println("Starting patu web server");
        final int port = 8080;
        this.serverSocket = new ServerSocket(port);
        System.out.println("patu listening on " + port + "...");
    }

    public boolean isRunning() {
		return isRunning;
	}

	public static void main (String... args) throws java.lang.Exception {
		new Main().startAsync();
	}

    public void stop() {
     this.mainThread.stop();
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
