package com.tw.kiera.patu;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.Socket;
import java.util.*;
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

    public Response handleRequest(String lines) throws IOException {
        return handleRequest(Arrays.asList(lines.split("\n")));
    }

    public Response handleRequest(List<String> requestLines) throws IOException {
        Response response;
        try{
            Map<String, String> headers = parseHeaders(requestLines);

            ValidationResult result = validate(headers);
            if (result.isInvalid()) {
               return Response.badRequest(result.getErrorMessage());
            }

            String requestedFilePath = determineResourceRequested(requestLines.get(0));
            if (requestedFilePath == null) {
                return Response.badRequest("Malformed resource");
            }
            if (requestedFilePath.equals("")) {
                return Response.redirectTo("index.html");
            }
            System.out.println(requestedFilePath);
            File requestedFile = new File(docRoot + "/" + requestedFilePath);
            if (!requestedFile.exists() || outsideOfDocRoot(requestedFile)) {
                return Response.NOT_FOUND;
            }

            response = new Response(200, "OK");
            response.setBody(respondWithResource(requestedFile));
        } catch(Exception e) {
          e.printStackTrace();
          response = new Response(500, "Internal Server Error","<html><body><img src=\"https://c4.staticflickr.com/8/7001/6509400855_aaaf915871_n.jpg\"></body></html>");
        }
        return response;
    }

    private ValidationResult validate(Map<String, String> headers) {
        if (!headers.containsKey("Host")) {
            return new ValidationResult(false, "Missing required header: Host");
        }
        return ValidationResult.VALID;
    }

    private Map<String, String> parseHeaders(List<String> lines) {
        if (lines.size() < 2) {
            return Collections.EMPTY_MAP;
        }


        Map<String, String> headers = new HashMap<String, String>();

        for(int i=1; i<lines.size();i++) {
            String line = lines.get(i);
            if ("".equals(line)) {
                break;
            }
            String[] nameAndValue = line.split(":");
            headers.put(nameAndValue[0].trim(), nameAndValue[1].trim());
        }

        return Collections.unmodifiableMap(headers);
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
            return matcher.group(1);
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

