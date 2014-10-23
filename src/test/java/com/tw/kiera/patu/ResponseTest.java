package com.tw.kiera.patu;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Created by ThoughtWorker on 10/14/14.
 */
public class ResponseTest {

    @Before
    public void initTestSettings() {
        TestSettings.init();
    }

    @Test
    public void shouldBeAbleToConvertToHTTPProtocolString() throws Exception {
        Response response = new Response(200, "OK");
        response.setBody("This is the body");
        String HTTPString = response.toString();
        String expected = "HTTP/1.1 200 OK\r\nConnection: close\r\n\r\nThis is the body";
        assertEquals(expected, HTTPString);
    }

    @Test
    public void redirectShouldHaveFullLocationHeader() {
        Response res = Response.redirectTo("/somepath");
        assertThat(res.getHeader("Location"), equalTo("http://test.host/somepath"));
    }

    @Test
    public void shouldBeAbleToChangeTheHeaderForResponse() throws Exception {
        Response response = new Response(200, "OK");
        response.setHeader("Connection", "keep-alive");
        response.setHeader("foo", "bar");
        assertEquals("keep-alive", response.getHeader("Connection"));
        assertEquals("bar", response.getHeader("foo"));
        assertEquals("foo: bar\nConnection: keep-alive", response.toHTTPHeaderString());
    }
}
