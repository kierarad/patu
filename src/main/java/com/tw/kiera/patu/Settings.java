package com.tw.kiera.patu;

import java.io.File;


public class Settings {

    private File docRoot;


    private static final Settings INSTANCE = new Settings();

    public static Settings getInstance() {
        return INSTANCE;
    }

    public File getDocRoot() {
        return docRoot;
    }

    public void setDocRoot(File docRoot) {
        if (!docRoot.exists()) {
            throw new IllegalArgumentException(String.format("The docRoot [%s] must already exist.", docRoot));
        }
        this.docRoot = docRoot;
    }

    public void setDocRoot(String docRoot) {
        setDocRoot(new File(docRoot));
    }
}
