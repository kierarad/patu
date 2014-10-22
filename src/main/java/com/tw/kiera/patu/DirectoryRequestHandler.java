package com.tw.kiera.patu;

import java.io.File;

public class DirectoryRequestHandler implements RequestHandler {
    private final File dir;

    public DirectoryRequestHandler(File dir) {
        this.dir = dir;
    }

    @Override
    public Response handleRequest(Request request) {
        return null;
    }
}
