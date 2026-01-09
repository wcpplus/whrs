package org.farm2.tools.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class I18nConfigReader {
    private static Map<String, String> config = null;

    /**
     * 翻译
     *
     * @param msg
     * @return
     */
    public static String getMsg(String msg) {
        try {
            String backMsg = readXmlToMap("cn").get(msg);
            if (StringUtils.isNotBlank(backMsg)) {
                return backMsg;
            }
            return msg;
        } catch (Exception e) {
            e.printStackTrace();
            return msg;
        }
    }


    /**
     * 读取国际化配置
     *
     * @param domain 语言類型 "cn"）
     * @return 包含配置项的Map
     */
    public static Map<String, String> readXmlToMap(String domain) {
        if (config == null) {
            config = new HashMap<>();
            String resourcePath = "config/i18n/i18n_" + domain + ".xml";
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            try (InputStream inputStream = classLoader.getResourceAsStream(resourcePath)) {
                if (inputStream == null) {
                    throw new IOException("Resource not found: " + resourcePath);
                }

                // 创建一个新的DocumentBuilderFactory
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                // 使用工厂获取一个DocumentBuilder
                DocumentBuilder builder = factory.newDocumentBuilder();
                // 解析XML输入流为Document
                Document document = builder.parse(inputStream);
                // 可选: 使用下列代码规范化文档的结构
                document.getDocumentElement().normalize();

                // 获取所有entry节点
                NodeList nodeList = document.getElementsByTagName("entry");

                // 遍历所有entry节点
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Element element = (Element) nodeList.item(i);
                    String key = element.getAttribute("key");
                    String value = element.getTextContent();
                    config.put(key, value);
                }
            } catch (ParserConfigurationException | SAXException | IOException e) {
                e.printStackTrace();
            }
        }
        return config;
    }


}
