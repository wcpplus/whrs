package org.farm2.tools.web;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.validator.routines.UrlValidator;
import org.farm2.tools.web.domain.WebDoc;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Farm2HttpUtils {
    /**
     * 对url中的中文编码
     *
     * @param url
     * @param chartSet
     * @return
     */
    public static String encodeChinese(String url, String chartSet) {
        try {
            Matcher matcher = Pattern.compile("[\\u4e00-\\u9fa5]").matcher(url);
            while (matcher.find()) {
                String tmp = matcher.group();
                url = url.replaceAll(tmp, java.net.URLEncoder.encode(tmp, chartSet));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }
    public static boolean isHasIpOrPort(String url) {
        {
            String ip = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
            Pattern pattern = Pattern.compile(ip);
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                return true;
            }
        }
        {
            String port = ":([0-9]|[1-9]\\d{1,3}|[1-5]\\d{4}|6[0-4]\\d{3}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5])";
            Pattern pattern = Pattern.compile(port);
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                return true;
            }
        }
        return false;
    }


    public static boolean isUrl(String url) {
        return UrlValidator.getInstance().isValid(url);
    }

    /**
     * 获得经过过滤的html
     *
     * @param url
     * @return
     */
    public static WebDoc getWebPageByUrl(String url) {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();
        Request request = new Request.Builder().url(url)
                // 设置User-Agent来模拟浏览器
                .header("User-Agent", getAgent()).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            String html = response.body().string();
            //替换标签
            html = replaceTag(html, "section", "p");
            //删除空白段落
            Document doc = Jsoup.parse(Farm2HtmlUtils.removeEmptyPTags(cleanHtml(html)));
            //替换标签
            WebDoc wdoc = new WebDoc();
            //格式化pre中得内容
            Farm2HtmlUtils.formatPre(doc);
            wdoc.setBaseUrl(getBaseUrl(new URL(url), Jsoup.parse(html)));
            wdoc.setHtml(doc.html());
            return wdoc;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 獲得頁面的基準url
     *
     * @return
     * @throws MalformedURLException
     */
    private static URL getBaseUrl(URL url, Element document) throws MalformedURLException {
        String baseurl = null;
        Elements base = document.getElementsByTag("base");
        {
            if (base.size() > 0) {
                if (base.hasAttr("href")) {
                    baseurl = base.attr("href");
                }
            }
            if (baseurl == null) {
                baseurl = url.toString().substring(0, url.toString().indexOf(url.getPath()));
            }
            if (baseurl == null) {
                url.toString().substring(0, url.toString().lastIndexOf("/") + 1);
            }
            if (baseurl.lastIndexOf("/") == (baseurl.length() - 1)) {
                baseurl = baseurl.substring(0, baseurl.length() - 1);
            }
        }
        return new URL(baseurl);
    }

    public static String cleanHtml(String html) {
        Safelist safelist = Safelist.none()
                .addTags("span", "img", "h1", "h2", "h3", "ul", "li", "title", "strong", "p","pre","code") // 允许的标签
                .addAttributes("img", "src", "width", "height", "data-src") // 允许的 img 标签属性
                .addAttributes("span", "src") // 允许的 span 标签属性（可选）
                .preserveRelativeLinks(true); // 保留相对链接（可选）
        return Jsoup.clean(html, safelist);
    }


    /**
     * 替换标签
     *
     * @param html
     * @param oTagName
     * @param tTagName
     * @return
     */
    public static String replaceTag(String html, String oTagName, String tTagName) {
        Document document = Jsoup.parse(html);
        // 递归调用以确保所有层级的oTagName都被替换
        document = replaceTagRecursively(document, oTagName, tTagName);
        return document.html();
    }

    private static Document replaceTagRecursively(Document document, String oTagName, String tTagName) {
        boolean found = false;
        Elements sections = document.getElementsByTag(oTagName);
        for (Element section : sections) {
            // 创建新的目标标签元素
            Element newElement = document.createElement(tTagName);
            // 将原标签的内容移动到新标签中
            newElement.html(section.html());
            // 替换原标签为新标签
            section.replaceWith(newElement);
            found = true;
        }
        // 如果找到了并替换了标签，则递归调用自身，确保嵌套的标签也被替换
        if (found) {
            return replaceTagRecursively(document, oTagName, tTagName);
        }
        // 如果没有找到更多的oTagName标签，则返回修改后的document对象
        return document;
    }




    public static void main(String[] args) {
        System.out.println(getWebPageByUrl("https://mp.weixin.qq.com/s/wp0_XBCk2s6-mv2yQrIR8g").getHtml());
    }

    public static String getImgUrl(String src, URL baseUrl) {
        if (src.indexOf("//") == 0) {
            src = baseUrl.getProtocol() + ":" + src;
        }
        if (src.toUpperCase().indexOf("HTTP") < 0) {
            if (src.indexOf("/") == 0) {
                src = baseUrl + src;
            } else {
                src = baseUrl + "/" + src;
            }
        }
        // url訪問不到資源或返回404就直接返回空
        if (!isLiveUrl(src)) {
            return null;
        }
        return src;
    }

    public static boolean isLiveUrl(String url) {
        try {
            URL remoteUrl = new URL(url);
            HttpURLConnection uConnection = (HttpURLConnection) remoteUrl.openConnection();
            uConnection.setConnectTimeout(1000);
            uConnection.setReadTimeout(10000);
            uConnection.connect();
            if (uConnection.getResponseCode() == 404) {
                return false;
            }
            return true;
        } catch (Exception e1) {
            return false;
        }
    }

    public static String getAgent() {
        return "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3";
    }

    /**
     * 获得知识库中的图片ur
     *
     * @param id
     * @return
     */
    public static String getSkcImgUrl(String id) {
        return "[FARM2_BASEPATH_FLAG]api/files/download/" + id;
    }
}
