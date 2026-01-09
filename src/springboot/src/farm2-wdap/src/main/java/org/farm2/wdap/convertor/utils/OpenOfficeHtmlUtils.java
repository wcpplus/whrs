package org.farm2.wdap.convertor.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class OpenOfficeHtmlUtils {
    public static String initExcelHtml(String msg) {
        Document document = Jsoup.parse(msg);
        Element element = document.getElementsByTag("body").get(0);
        Elements centerElements = element.getElementsByTag("center");
        centerElements.addClass("fixed-excel-top");
        for (Element center : centerElements) {
            center.getElementsByTag("br").remove();
        }
        return document.getElementsByTag("body").get(0).html();
    }
}
