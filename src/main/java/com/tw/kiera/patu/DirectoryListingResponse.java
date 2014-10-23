package com.tw.kiera.patu;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DirectoryListingResponse extends Response {
    private final File path;
    private final String requestPath;

    public DirectoryListingResponse(String path) {
        super(200, "OK");
        this.requestPath = path;
        this.path = new File(Settings.getInstance().getDocRoot() + path);
        Preconditions.checkArgument(this.path.exists(), "Cannot list contents of " + this.path + " because it does not exist");
    }

    public String getBody() {
        HtmlBuilder builder = new HtmlBuilder();
        String title = "Index of " + requestPath;
        builder.withTitle(title);
        builder.withHeader(title);

        List<String> fileLinks = new ArrayList<String>(Collections2.transform(Arrays.asList(this.path.list()), new Function<String,String>(){
            public String apply(String file) {
                return HtmlBuilder.a(relativize(file), file);
            }
        }));

        if (!isDocRootRequest()) {
            try {
                String parentPath =
                        path.getParentFile().equals(Settings.getInstance().getDocRoot()) ? "/" :
                        path.getParentFile().getCanonicalPath().replaceFirst(Settings.getInstance().getDocRoot().getCanonicalPath(), "");
                fileLinks.add(0, HtmlBuilder.a(parentPath, "[..]"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        builder.withBody(HtmlBuilder.toUnorderedList(fileLinks));

        return builder.build();
    }


    private String relativize(String filename) {
        try {
            filename = Settings.getInstance().getUrlEncoder().encode(filename);
        } catch (EncoderException e) {
            throw new RuntimeException(e);
        }
        String parent = requestPath;
        if (filename.startsWith("/")) {
            filename = filename.substring(1);
        }

        if (isDocRootRequest()) {
            parent = "";
        } else if (parent.endsWith("/")) {
            parent = parent.substring(0, parent.length() - 2);
        }

        return Joiner.on("/").join(Arrays.asList(parent, filename));
    }

    private boolean isDocRootRequest() {
        return "/".equals(requestPath);
    }
}
