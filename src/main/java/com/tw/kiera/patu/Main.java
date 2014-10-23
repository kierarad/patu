package com.tw.kiera.patu;


import com.google.common.base.Joiner;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Main {

    private static final String DEFAULT_DOC_ROOT = System.getenv("HOME") + "/Sites";
    private static final int DEFAULT_PORT = 8080;

    @Option(name="-d", usage="specify html docroot, e.g. where GET / will be relative to")
    private String docRoot = DEFAULT_DOC_ROOT;

    @Option(name="-p",usage="port to listen on")
    private int port = DEFAULT_PORT;

    @Option(name="--dir-browsing", usage="enable directory browsing")
    private boolean directoryBrowsingEnabled = false;

    @Option(name="-h", usage="external hostname")
    private String hostname = System.getenv("HOSTNAME");

    private boolean isRunning;
    private Thread mainThread;
    private ServerSocket serverSocket;

    public Main() {
        this(DEFAULT_PORT);
    }

    public Main(int port) {
        this(port, DEFAULT_DOC_ROOT);
    }

    public Main(int port, String docRoot) {
        this.port = port;
        this.docRoot = docRoot;
    }

    public Main(String... args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
            Settings.getInstance().setDocRoot(docRoot);
            Settings.getInstance().setDirectoryBrowsingEnabled(directoryBrowsingEnabled);
            Settings.getInstance().setHostname(hostname);
        } catch (CmdLineException e) {
            parser.printUsage(System.err);
            throw new RuntimeException(e);
        }
    }

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
                Socket client = null;
                try {
                    client = serverSocket.accept();
                    String request = readRequest(client.getInputStream());
                    Response response = new RequestParser().parseAndHandleRequest(request);
                    OutputStream output = client.getOutputStream();
                    output.write(response.toString().getBytes());
                    System.out.println(String.format("[HTTP %d %s] - %s", response.getStatusCode(), response.getStatusLine(), client.getRemoteSocketAddress()));
                } catch (Exception e) {
                    System.out.println(String.format("error handling %s: %s", client.getRemoteSocketAddress(), e.getMessage()));
                } finally {
                    try {
                        client.close();
                    } catch(Exception e) {
                        System.out.println("Error closing client: " + e.getMessage());
                    }
                }
            }
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

    private void listenOnPort() throws IOException {
        System.out.println("Starting patu web server");
        this.serverSocket = new ServerSocket(port);
        System.out.println("patu listening on " + port + "...");
        System.out.println("docroot: " + docRoot);
    }

    public boolean isRunning() {
		return isRunning;
	}

	public static void main (String... args) throws java.lang.Exception {
        System.out.println("Main received: " + Arrays.asList(args));
        new Main(args).startAsync();
	}

    static String readRequest(InputStream input) throws IOException {
        LineIterator lineIterator = IOUtils.lineIterator(input, "UTF-8");
        List<String> lines = new ArrayList<String>();

        while (true) {
            if (!lineIterator.hasNext()) {
                System.out.println("no more");
                break;
            }
            String line = lineIterator.nextLine();
            if ("".equals(line.trim())) {
                break;
            }
            lines.add(line);
        }
        return Joiner.on("\n").join(lines);
    }
}
