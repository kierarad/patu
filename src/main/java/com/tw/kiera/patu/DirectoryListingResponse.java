package com.tw.kiera.patu;

public class DirectoryListingResponse extends Response {
    private final String path;

    public DirectoryListingResponse(String path) {
        super(200, "OK");
        this.path = path;
    }

    public String getBody() {
        return "subfile.txt";
    }
}
