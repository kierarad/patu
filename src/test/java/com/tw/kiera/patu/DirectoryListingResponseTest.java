package com.tw.kiera.patu;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class DirectoryListingResponseTest{

    @Before
    public void resetSettings() {
         TestSettings.init();
    }

    @Test
    public void shouldHaveABodyWithLinks() {
        Response response = new DirectoryListingResponse("/subfolder");
        assertThat(response.getBody(), containsString("<a href=\"/subfolder/subfile.txt\">subfile.txt</a>"));
    }

    @Test
    public void shouldIncludeLinksRelativeToDocroot() {
        Response response = new DirectoryListingResponse("/subfolder/subsubfolder");
        assertThat(response.getBody(), containsString("href=\"/subfolder/subsubfolder/subsubfile.html\""));
    }

    @Test
    public void shouldProperlyListDocroot() {
        Response response = new DirectoryListingResponse("/");
        assertThat(response.getBody(), containsString("<a href=\"/subfolder\">subfolder</a>"));
    }

    @Test
    public void shouldEscapeSpacesInHrefs() {
        Response response = new DirectoryListingResponse("/");
        assertThat(response.getBody(), containsString("<a href=\"/file+with+spaces.html\">file with spaces.html</a>"));
    }
}
