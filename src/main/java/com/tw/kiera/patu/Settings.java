package com.tw.kiera.patu;

import com.google.common.escape.Escaper;
import com.google.common.net.UrlEscapers;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang.StringUtils;

import java.io.File;


public class Settings {

    private File docRoot;

    private static final Settings INSTANCE = new Settings();
    private boolean directoryBrowsingEnabled;
    private String hostname;
    private final URLCodec urlCodec = new URLCodec();

    public static Settings getInstance() {
        return INSTANCE;
    }

    public String getSignature() {
        return String.format("<strong><a href='%s'>Patu</a> %s</strong> [%s - java %s]", "https://github.com/kierarad/patu", getVersion(), System.getProperty("os.name"), Runtime.class.getPackage().getImplementationVersion());
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

    public String getVersion() {
        String version = getClass().getPackage().getImplementationVersion();
        if (StringUtils.isBlank(version)) {
            version = "(unreleased version)";
        }
        return version;
    }

    public boolean isDirectoryBrowsingEnabled() {
        return directoryBrowsingEnabled;
    }

    public void setDirectoryBrowsingEnabled(boolean directoryBrowsingEnabled) {
        this.directoryBrowsingEnabled = directoryBrowsingEnabled;
    }

    public URLCodec getUrlEncoder() {
        return urlCodec;
    }

    public URLCodec getUrlDecoder() {
        return urlCodec;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getHostname() {
        return hostname;
    }
}
