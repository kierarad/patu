package com.tw.kiera.patu;

import org.apache.commons.lang.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class HtmlBuilder {

    private String title = "Untitled";
    private String header;
    private String body;

    public HtmlBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public HtmlBuilder withHeader(String header) {
        this.header = header;
        return this;
    }

    public HtmlBuilder withBody(String body) {
        this.body = body;
        return this;
    }

    public String build() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        pw.println("<!doctype html>");
        pw.println("<html>");
        pw.println("\t<head>");
        pw.println("\t\t<title>" + title + "</title>");
        pw.println("\t</head>");

        pw.println("\t<body>");
        if (StringUtils.isNotBlank(header)) {
            pw.println("\t\t<header><h1>" + header + "</h1></header>");
        }

        if (StringUtils.isNotBlank(body)) {
            pw.println("\t\t<main>" + body + "</main>");
        }

        pw.println("\t\t<footer><hr/><address>" + Settings.getInstance().getSignature() + "</address></footer>\n");
        pw.println("\t</body>");
        pw.println("</html>");
        pw.close();
        return sw.toString();
    }

}
