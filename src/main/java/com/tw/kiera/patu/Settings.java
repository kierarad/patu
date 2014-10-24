package com.tw.kiera.patu;

import com.google.common.base.Preconditions;
import com.google.common.escape.Escaper;
import com.google.common.net.UrlEscapers;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.nio.charset.Charset;


public class Settings {

    private File docRoot;

    private static final Settings INSTANCE = new Settings();
    private boolean directoryBrowsingEnabled;
    private String hostname;
    private final URLCodec urlCodec = new URLCodec();
    private int port;
    private final Charset charset = Charset.forName("ISO-8859-1");
    private boolean basicAuthOn;

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
        Preconditions.checkNotNull(hostname);
        this.hostname = hostname;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setBasicAuthOn(boolean basicAuthOn) {
        this.basicAuthOn = basicAuthOn;
    }

    public boolean isBasicAuthOn() {
        return basicAuthOn;
    }
}
