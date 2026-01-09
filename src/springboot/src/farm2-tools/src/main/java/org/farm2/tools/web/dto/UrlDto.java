package org.farm2.tools.web.dto;

/**
 * 封装id
 */
public class UrlDto {
    private String url;

    private String baseUrl;

    private String key;

    //------------------------------------------------------------------
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
