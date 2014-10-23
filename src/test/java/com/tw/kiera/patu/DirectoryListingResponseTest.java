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
        assertThat(response.getBody(), containsString("<a href=\"subfile.txt\">subfile.txt</a>"));
    }

}
