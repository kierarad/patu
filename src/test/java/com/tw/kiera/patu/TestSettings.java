package com.tw.kiera.patu;

public class TestSettings {
    public final static String TEST_DOCROOT = "./src/test/data";

    public static void init() {
        Settings.getInstance().setDocRoot(TEST_DOCROOT);
        Settings.getInstance().setDirectoryBrowsingEnabled(false);
        Settings.getInstance().setHostname("test.host");
    }
}
