package com.tw.kiera.patu;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class DirectoryListingResponse extends Response {
    private final File path;

    public DirectoryListingResponse(String path) {
        super(200, "OK");
        this.path = new File(Settings.getInstance().getDocRoot() + path);
        Preconditions.checkArgument(this.path.exists(), "Cannot list contents of " + this.path + " because it does not exist");
    }

    public String getBody() {
        HtmlBuilder builder = new HtmlBuilder();
        String title = "Index of /" + path;
        builder.withTitle(title);
        builder.withHeader(title);

        Collection<String> fileLinks = Collections2.transform(Arrays.asList(this.path.list()), new Function<String,String>(){
            public String apply(String file) {
                return HtmlBuilder.a(file, file);
            }
        });



        builder.withBody(HtmlBuilder.toUnorderedList(fileLinks));

        return builder.build();
    }
}
