package com.tw.kiera.patu;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileRequestHandler implements RequestHandler {

    private final File docRoot;

    public FileRequestHandler() {
        docRoot = Settings.getInstance().getDocRoot();
    }

    @Override
    public Response handleRequest(Request request) {
        File requestedFile = new File(docRoot, request.getPath());
        if (!requestedFile.exists() || outsideOfDocRoot(requestedFile)) {
            return Response.NOT_FOUND;
        }

        if (requestedFile.isDirectory()) {
            return new DirectoryRequestHandler().handleRequest(request);
        }

        try {
            return new Response(200, "OK", respondWithResource(requestedFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private String respondWithResource(File requestedFile) throws IOException {
        return IOUtils.toString(new FileInputStream(requestedFile), Settings.getInstance().getCharset().toString());
    }


    private boolean outsideOfDocRoot(File requestedFile) {
        try {
            return !requestedFile.getCanonicalPath().startsWith(docRoot.getCanonicalPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
