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
import java.util.regex.*;

class Main {

    private static final String DEFAULT_DOC_ROOT = System.getenv("HOME") + "/Sites";
    private static final int DEFAULT_PORT = 8080;

    @Option(name="-d", usage="specify html docroot, e.g. where GET / will be relative to")
    private String docRoot = DEFAULT_DOC_ROOT;

    @Option(name="-p",usage="port to listen on")
    private int port = DEFAULT_PORT;

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
        this();
        System.out.println(Arrays.asList(args));
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
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
				System.out.println("Listening again for a client");
				final Socket client = serverSocket.accept();
                String request = readRequest(client);
                new RequestHandler(docRoot).handleRequest(request);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

    private String readRequest(Socket client) throws IOException {
        InputStream input = client.getInputStream();
        System.out.println("Input Stream received " + input);
        return buildRequest(input);
    }


    private void listenOnPort() throws IOException {
        System.out.println("Starting patu web server");
        this.serverSocket = new ServerSocket(port);
        System.out.println("patu listening on " + port + "...");
    }

    public boolean isRunning() {
		return isRunning;
	}

	public static void main (String... args) throws java.lang.Exception {
        System.out.println("Main received: " + Arrays.asList(args));
        new Main(args).startAsync();
	}

    public void stop() {
     this.mainThread.stop();
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getPort() {
        return this.port;
    }

    static String buildRequest(InputStream input) throws IOException {
        LineIterator lineIterator = IOUtils.lineIterator(input, "UTF-8");
        List<String> lines = new ArrayList<String>();
        while (lineIterator.hasNext()) {
            System.out.println("has next");
            String line = lineIterator.nextLine();
            System.out.println("line => " + line);
            if (line.equals("\n")) {
                break;
            }
            lines.add(line);
        }
        return Joiner.on("\n").join(lines);
    }
}
