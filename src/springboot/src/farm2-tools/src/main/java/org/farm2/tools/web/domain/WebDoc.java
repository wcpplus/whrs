package org.farm2.tools.web.domain;


import java.net.URL;

public class WebDoc {
    private String html;
    private URL url;

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public URL getBaseUrl() {
        return url;
    }

    public void setBaseUrl(URL baseUrl) {
        this.url = baseUrl;
    }
}
