package com.tw.kiera.patu;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Routing class that determines what type of request it is and which specific handler should handle it.
 */
public class RequestParser {

    public Response parseAndHandleRequest(String lines) throws IOException {
        return parseAndHandleRequest(Arrays.asList(lines.split("\n")));
    }

    public Response parseAndHandleRequest(List<String> requestLines) throws IOException {

        Map<String, String> headers = parseHeaders(requestLines);

//        if (!headers.containsKey("Authorization")){
//            Response response = new Response(401, "Not Authorized");
//            response.setHeader("WWW-Authenticate", "Basic realm='patu'");
//            return response;
//        }

        try {
            ValidationResult result = validate(headers);
            if (result.isInvalid()) {
               return Response.badRequest(result.getErrorMessage());
            }

            String requestedFilePath = determineResourceRequested(requestLines.get(0));
            if (requestedFilePath == null) {
                return Response.badRequest("Malformed resource");
            }

            Request request = new Request(headers, requestedFilePath);
            return new FileRequestHandler().handleRequest(request);

        } catch(Exception e) {
          e.printStackTrace();
          String html = new HtmlBuilder().withTitle("Internal Server Error").withBody("<img src=\"https://c4.staticflickr.com/8/7001/6509400855_aaaf915871_n.jpg\">").build();
          return new Response(500, "Internal Server Error", html);
        }

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
        return clientMessage;
    }
}

