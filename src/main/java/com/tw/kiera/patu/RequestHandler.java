package com.tw.kiera.patu;

import java.io.*;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ThoughtWorker on 8/26/14.
 */
public class RequestHandler {

    private final String docRoot;

    public RequestHandler(String docRoot) {
        this.docRoot = docRoot;
    }

    public void handleRequest(final Socket client) {
        System.out.println("Connection came in from: " + client.getRemoteSocketAddress());
        new Thread(new Runnable() {


            public void run() {
                System.out.println("Started new thread to handle connection: " + Thread.currentThread().getName());
                try {
                    String requestedFilePath = determineResourceRequested(client);
                    if (requestedFilePath == null) {
                        respondWith400(client);
                    }
                    File requestedFile = new File(docRoot + "/" + requestedFilePath);
                    System.out.println(requestedFile);
                    if (!requestedFile.exists()) {
                        respondWith404(client);
                    } else {
                        respondWithResource(requestedFile, client);
                    }
                    System.out.println("done handling client");
                    client.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private void respondWith400(Socket client) throws IOException {
        OutputStream output = client.getOutputStream();
        System.out.println("Output is: " + output);

        output.write("HTTP/1.1 400 Bad Request\n\n".getBytes());
    }

    private void respondWith404(Socket client) throws IOException {
        OutputStream output = client.getOutputStream();
        System.out.println("Output is: " + output);

        output.write("HTTP/1.1 404 Not Found\n\n".getBytes());
    }

    private void respondWithResource(File requestedFile, Socket client) throws IOException {
        OutputStream output = client.getOutputStream();
        System.out.println("Output is: " + output);

        output.write("HTTP/1.1 200 OK\n\n".getBytes());
        InputStream fileContents = new FileInputStream(requestedFile);
        byte[] buffer;
        buffer = new byte[124];
        int currentReadLength = 0;
        int bufferPosition = 0;

        while(true) {
            if (bufferPosition >= buffer.length) {
                buffer = new byte[124];
                bufferPosition = 0;
                currentReadLength = 0;
            }

            currentReadLength = fileContents.read(buffer, bufferPosition, buffer.length - bufferPosition);
            if (currentReadLength == -1) {
                break;
            }
            output.write(buffer, bufferPosition, currentReadLength);
            bufferPosition += currentReadLength;
        }
    }

    private String determineResourceRequested(Socket client) throws IOException {
        String request = readRequest(client);
        String clientMessage = parseRequest(request);

        Pattern pattern = Pattern.compile("GET /([\\w\\.]*) HTTP.*");
        Matcher matcher = pattern.matcher(clientMessage);
        if (matcher.matches()) {
            System.out.println("match! " + matcher);
            String requestedFilePath = "index.html";
            System.out.println(String.format("Match: '%s'", matcher.group(1)));
            if (!matcher.group(1).equals("")) {
                requestedFilePath = matcher.group(1);
            }
            return requestedFilePath;
        }
        return null;
    }

    private String parseRequest(String request) {
        String[] lines = request.split("\n");
        String clientMessage = lines[0].trim();
        System.out.println(String.format("Received from client: (Length: %d) '%s'", clientMessage.length(), clientMessage));
        return clientMessage;
    }

    private String readRequest(Socket client) throws IOException {
        InputStream input = client.getInputStream();
        System.out.println("Input Stream received" + input);
        StringBuilder sb = new StringBuilder();
        byte[] buffer = new byte[8];
        int bytesRead = 0;
        int totalRead = 0;
        boolean endOfMessage = false;
        while(bytesRead != -1 && !endOfMessage) {
            bytesRead = input.read(buffer, totalRead, buffer.length - totalRead);
            totalRead += bytesRead;
            System.out.println("Read " + bytesRead + "bytes, read " + totalRead + " so far");
            endOfMessage = buffer[totalRead-1] == "\n".getBytes()[0];
            if (totalRead >= buffer.length || endOfMessage) {
                sb.append(new String(buffer));
                buffer = new byte[8];
                totalRead = 0;
                bytesRead = 0;
            }

        }
        return sb.toString();
    }

}

