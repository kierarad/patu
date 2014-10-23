package com.tw.kiera.patu;

import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class DirectoryRequestHandlerTest {

    @Before
    public void setDocroot() {
        TestSettings.init();
    }

    @Test
    public void byDefaultReturn404WhenNoIndexFilePresent() {
        Request req = new TestRequest("subfolder");
        Response res = new DirectoryRequestHandler().handleRequest(req);
        assertThat(res.getStatusCode(), equalTo(404));
    }

    @Test
    public void byDefaultReturn302WhenIndexFilePresent() {
        Request req = new TestRequest("subfolder-with-index");
        Response res = new DirectoryRequestHandler().handleRequest(req);
        assertThat(res.getStatusCode(), equalTo(302));
        assertThat(res.getHeader("Location"), equalTo("http://test.host/subfolder-with-index/index.html"));
    }


    @Test
    public void whenDirBrowsingEnabledShouldIncludeContentsOfDirectoryInBody() {
       Settings.getInstance().setDirectoryBrowsingEnabled(true);
       Request req = new TestRequest("subfolder");
       Response res = new DirectoryRequestHandler().handleRequest(req);
       assertThat(res.getBody(), containsString("subfile.txt"));
    }

}
