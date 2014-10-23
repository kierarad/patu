package com.tw.kiera.patu;

import org.apache.commons.lang.StringUtils;

import java.io.File;

public class DirectoryRequestHandler implements RequestHandler {

    @Override
    public Response handleRequest(Request request) {
        if (Settings.getInstance().isDirectoryBrowsingEnabled()) {
            return new DirectoryListingResponse(request.getPath());
        }

        File pathToIndex = new File(request.getPath(), "/index.html");
        File indexFile = new File(Settings.getInstance().getDocRoot(), "/" + pathToIndex);
        if (indexFile.exists()) {
            return Response.redirectTo(pathToIndex.getPath());
        }

        return Response.NOT_FOUND;
    }
}
