package com.tw.kiera.patu;

import org.apache.commons.io.IOUtils;

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
        try {
            this.docRoot = new File(docRoot).getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Response handleRequest(final String request) throws IOException {
        String requestedFilePath = determineResourceRequested(request);
        if (requestedFilePath == null) {
            return Response.BAD_REQUEST;
        }
        File requestedFile = new File(docRoot + "/" + requestedFilePath);
        if (!requestedFile.exists() || outsideOfDocRoot(requestedFile)) {
            return Response.NOT_FOUND;
        }

        Response response = new Response();
        response.setBody(respondWithResource(requestedFile));
        response.setStatusCode(200);
        return response;
//        System.out.println("Connection came in from: " + client.getRemoteSocketAddress());
//        new Thread(new Runnable() {
//
//
//            public void run() {
//                System.out.println("Started new thread to handle connection: " + Thread.currentThread().getName());
//                try {
//                    String requestedFilePath = determineResourceRequested(client);
//                    if (requestedFilePath == null) {
//                        respondWith400(client);
//                    }
//                    File requestedFile = new File(docRoot + "/" + requestedFilePath);
//                    System.out.println(requestedFile);
//                    if (!requestedFile.exists() || outsideOfDocRoot(requestedFile)) {
//                        respondWith404(client);
//                    } else {
//                        respondWithResource(requestedFile, client);
//                    }
//                    System.out.println("done handling client");
//                    client.close();
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }).start();
    }

    private boolean outsideOfDocRoot(File requestedFile) {
        try {
            return !requestedFile.getCanonicalPath().startsWith(docRoot);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String respondWithResource(File requestedFile) throws IOException {
        return IOUtils.toString(new FileInputStream(requestedFile));
    }

    private String determineResourceRequested(String request) throws IOException {
        String clientMessage = parseRequest(request);
        Pattern pattern = Pattern.compile("GET /([^\b]*) HTTP.*");
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

