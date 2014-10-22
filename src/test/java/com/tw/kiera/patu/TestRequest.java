package com.tw.kiera.patu;

import java.util.Collections;
import java.util.Map;

public class TestRequest extends Request {
    public TestRequest(String path) {
        super(Collections.singletonMap("Host", "localhost"), path);
    }
}
