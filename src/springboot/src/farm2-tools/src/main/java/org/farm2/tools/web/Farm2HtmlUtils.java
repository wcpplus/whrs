package org.farm2.tools.web;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Farm2HtmlUtils {
    /**
     * 删除文本中的xml标签
     *
     * @param text
     * @return
     */
    public static String removeXmlTag(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        // 正则表达式用于匹配XML标签
        String xmlTagRegex = "<[^>]+>";
        Pattern pattern = Pattern.compile(xmlTagRegex);
        Matcher matcher = pattern.matcher(text);
        // 使用StringBuilder来构建结果字符串
        StringBuilder result = new StringBuilder();
        int lastEnd = 0;
        while (matcher.find()) {
            // 将非XML标签部分添加到结果中
            result.append(text, lastEnd, matcher.start());
            lastEnd = matcher.end();
        }
        // 添加最后一段非XML标签部分
        result.append(text.substring(lastEnd));
        return result.toString();
    }

    /**
     * 删除html中所有的标签和样式js等，只返回可见的文本内容
     *
     * @param html
     * @return
     */
    public static String getTextByHtml(String html) {
        if (html == null || html.isEmpty()) {
            return html;
        }
        // 使用Jsoup的clean方法去除所有标签和样式，只保留纯文本
        String cleanText = Jsoup.clean(html, Safelist.none());
        // 解析清理后的文本以进一步提取可见文本
        Document document = Jsoup.parse(cleanText);
        // 使用text()方法获取文档中的可见文本
        return document.text();
    }


    public static String getTextByHtml(String html, int maxnum) {
        String text = getTextByHtml(html);
        if (text.length() > maxnum) {
            return text.substring(0, maxnum - 1);
        } else {
            return text;
        }
    }

    public static String getSrc(Element img) {
        String src = img.attr("src");
        if (StringUtils.isBlank(src)) {
            src = img.attr("data-original");
        }
        img.removeAttr("data-original");
        if (StringUtils.isBlank(src)) {
            src = img.attr("data-src");
        }
        img.removeAttr("data-src");
        return src;
    }

    /**
     * 删除空白段落
     *
     * @param html
     * @return
     */
    public static String removeEmptyPTags(String html) {
        Document document = Jsoup.parse(html);
        // 获取所有<p>标签
        Elements ps = document.getElementsByTag("p");
        Elements lis = document.getElementsByTag("li");
        Elements uls = document.getElementsByTag("ul");
        List<Element> paragraphs = new ArrayList<>();
        paragraphs.addAll(ps);
        paragraphs.addAll(lis);
        paragraphs.addAll(uls);
        // 创建一个迭代器以安全地遍历并移除元素
        for (Iterator<Element> it = paragraphs.iterator(); it.hasNext(); ) {
            Element paragraph = it.next();
            // 清理段落中的空白字符
            paragraph.children().removeIf(el -> el.tagName().equals("br"));
            String text = paragraph.text().trim(); // 获取<p>标签内的纯文本内容，并去除首尾空白
            // 如果<p>标签内没有文本内容，并且没有子元素，则移除这个<p>标签
            if (text.isEmpty() && paragraph.children().isEmpty()) {
                paragraph.remove();
            }
        }
        return document.html();
    }

    /**
     * 合并pre标签内得<code>并且code间插入换行
     * 在 HTML 中，将 <pre> 标签内的多个 <code> 标签合并为一个，并且在每个原本的 <code> 内容之间插入换行符（\n 或 HTML 中的 <br>）。
     *
     * 使用了 Jsoup 这个强大的 HTML 解析库。
     * 合并 <code> 的内容时，用 \n 分隔不同 <code> 的内容。
     * 原来的多个 <code> 被移除，替换为一个新的 <code> 标签。
     * 如果你在网页中展示，记得 HTML 中换行要用 <br> 或者 white-space: pre-line/pre-wrap 来显示换行。
     *
     * @param img
     */
    public static void formatPre(Element img) {
        // 遍历所有 <pre> 标签
        for (Element pre : img.select("pre")) {
            StringBuilder mergedCode = new StringBuilder();

            // 收集并移除所有 <code> 子节点的内容
            for (Element code : pre.getElementsByTag("code")) {
                if (mergedCode.length() > 0) {
                    mergedCode.append("\n"); // 插入换行符
                }
                mergedCode.append(code.text());
                code.remove(); // 删除旧的 <code>
            }

            // 创建新的 <code> 标签并插入合并后的内容
            if (mergedCode.length() > 0) {
                Element newCode = new Element("code");
                newCode.text(mergedCode.toString());
                pre.appendChild(newCode);
            }
        }
    }





    /**
     * 从html中提取属性
     *
     * @param html
     * @param tag
     * @param attr
     * @return
     */
    public static void getAttrs(Element html, String tag, String attr, Farm2HtmlUtilsHandleInter handle) {
        for (Element node : html.getElementsByTag(tag)) {
            handle.handle(node, node.attr(attr));
        }
    }
}
