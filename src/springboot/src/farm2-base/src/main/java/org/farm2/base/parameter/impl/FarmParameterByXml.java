package org.farm2.base.parameter.impl;

import org.farm2.base.parameter.ParameterDomain;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FarmParameterByXml {
    private static List<ParameterDomain> paras = null;
    private static Map<String, String> valMap = new HashMap<>();
    private static Map<String, String> typeMap = new HashMap<>();
    public List<ParameterDomain> getParas() {
        return paras;
    }

    public Map<String, String> getValMap() {
        return valMap;
    }

    public Map<String, String> getTypeMap() {
        return typeMap;
    }

    public List<ParameterDomain> readConfigFromXml(String xmlFileName) {
        if (paras != null) {
            return paras;
        }
        List<ParameterDomain> farmParameters = new ArrayList<>();
        String resourcePath = "config/" + xmlFileName + ".xml";
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Resource not found: " + resourcePath);
            }
            // 创建 DocumentBuilderFactory 和 DocumentBuilder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            // 解析 XML 输入流
            Document document = builder.parse(inputStream);
            document.getDocumentElement().normalize();
            // 获取所有 group 元素
            NodeList groupNodes = document.getElementsByTagName("group");

            // 遍历每个 group 元素
            for (int i = 0; i < groupNodes.getLength(); i++) {
                Node groupNode = groupNodes.item(i);
                if (groupNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element groupElement = (Element) groupNode;

                    // 获取 group 的 name 和 describe 属性
                    String groupName = groupElement.getAttribute("name");
                    String groupKey = groupElement.getAttribute("key");
                    // 获取 group 下的所有 parameter 元素
                    NodeList parameterNodes = groupElement.getElementsByTagName("parameter");
                    // 遍历每个 parameter 元素
                    for (int j = 0; j < parameterNodes.getLength(); j++) {
                        Node parameterNode = parameterNodes.item(j);
                        if (parameterNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element parameterElement = (Element) parameterNode;

                            // 获取 parameter 的 name 和 version 属性
                            String parameterName = parameterElement.getAttribute("name");
                            String parameterKey = parameterElement.getAttribute("key");
                            String parameterVersion = parameterElement.getAttribute("version");
                            String parameterVtype = parameterElement.getAttribute("vtype");
                            Boolean parameterUserable = parameterElement.getAttribute("userable").toLowerCase().equals("true");

                            // 获取 val 和 describe 子元素的内容
                            String parameterVal = "";
                            String parameterDescribe = "";

                            NodeList childNodes = parameterElement.getChildNodes();
                            for (int k = 0; k < childNodes.getLength(); k++) {
                                Node childNode = childNodes.item(k);

                                if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                                    Element childElement = (Element) childNode;

                                    if ("val".equals(childElement.getTagName())) {
                                        parameterVal = childElement.getTextContent().trim();
                                    } else if ("describe".equals(childElement.getTagName())) {
                                        parameterDescribe = childElement.getTextContent().trim();
                                    }
                                }
                            }
                            // 创建 FarmParameter 对象并添加到列表中
                            ParameterDomain farmParameter = new ParameterDomain(groupName, groupKey, parameterName, parameterKey, parameterVal, parameterDescribe, parameterVersion, parameterVtype, parameterUserable);
                            farmParameters.add(farmParameter);
                            valMap.put(farmParameter.getParameterKey(), farmParameter.getParameterVal());
                            typeMap.put(farmParameter.getParameterKey(),farmParameter.getParameterType());
                        }
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
        paras = farmParameters;
        return paras;
    }
}