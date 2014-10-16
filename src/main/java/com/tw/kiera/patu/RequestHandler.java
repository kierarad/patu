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
        System.out.println(requestedFilePath);
        File requestedFile = new File(docRoot + "/" + requestedFilePath);
        if (!requestedFile.exists() || outsideOfDocRoot(requestedFile)) {
            return Response.NOT_FOUND;
        }

        Response response = new Response(200, "OK");
        response.setBody(respondWithResource(requestedFile));
        return response;
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
            String requestedFilePath = "index.html";
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
}

