package org.farm2.tools.config;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Farm2ConfigUtils {
    private String PROPERTY_FILE;

    /**
     * @param fileName 如jdbc.properties
     */
    public Farm2ConfigUtils(Farm2ConfigEnum fileName) {
        try {
            // 获取资源文件的输入流
            InputStream inputStream = Farm2ConfigUtils.class.getClassLoader()
                    .getResourceAsStream(fileName.getFilename());
            if (inputStream == null) {
                throw new IllegalArgumentException("Resource file '" + fileName.getFilename() + "' not found in classpath.");
            }

            // 判断当前是否运行在 JAR 文件中
            String filePath = extractFilePath(fileName.getFilename(), inputStream);

            PROPERTY_FILE = filePath; // 设置文件路径
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Farm2ConfigUtils with file: " + fileName.getFilename(), e);
        }
    }


    /**
     * 提取资源文件的实际路径
     *
     * @param fileName    资源文件名
     * @param inputStream 资源文件的输入流
     * @return 文件的实际路径
     * @throws Exception 如果文件无法提取
     */
    private static String extractFilePath(String fileName, InputStream inputStream) throws Exception {
        // 检查是否可以获取文件的直接路径（适用于 IDE 环境）
        String resourcePath = Farm2ConfigUtils.class.getResource("/" + fileName).getPath();
        File resourceFile = new File(resourcePath);

        // 如果文件存在且可访问，则直接返回路径
        if (resourceFile.exists() && resourceFile.canRead()) {
            return resourceFile.getAbsolutePath();
        }

        // 否则将文件复制到临时目录（适用于 JAR 文件环境）
        File tempFile = Files.createTempFile(fileName, null).toFile();
        tempFile.deleteOnExit(); // JVM 退出时删除临时文件

        // 将输入流内容写入临时文件
        try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        return tempFile.getAbsolutePath(); // 返回临时文件路径
    }
    /**
     * 获取当前服务器ID:farm2.conf.server.id
     *
     * @return
     */
    public static String getServiceId() {
        return new Farm2ConfigUtils(Farm2ConfigEnum.FARM2_PROPERTIES).getData("farm2.conf.server.id");
    }

    public static Farm2ConfigUtils getInstance(Farm2ConfigEnum fileName) {
        return new Farm2ConfigUtils(fileName);
    }

    /**
     * 根据Key 读取Value
     */
    public String getData(String key) {
        Properties props = new Properties();
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(
                    PROPERTY_FILE));
            props.load(in);
            String value = props.getProperty(key);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<String> getDatas(String key) {
        String data = getData(key);
        return Arrays.stream(data.split(",")).toList();
    }



    /**
     * 修改或添加键值对 如果key存在，修改 反之，添加。
     */
    public void setData(String key, String value) {
        Properties prop = new Properties();
        OutputStream fos = null;
        InputStream fis = null;
        try {
            File file = new File(PROPERTY_FILE);
            if (!file.exists())
                file.createNewFile();
            fis = new FileInputStream(file);
            prop.load(fis);
        } catch (IOException e) {
            System.err.println("Visit " + PROPERTY_FILE + " for updating "
                    + value + " value error");
        } finally {
            try {
                fis.close();// 一定要在修改值之前关闭fis
            } catch (IOException e) {
            }
        }
        try {
            fos = new FileOutputStream(PROPERTY_FILE);
            prop.setProperty(key, value);
            prop.store(fos, "Update '" + key + "' value");
            prop.store(fos, "just for test");
        } catch (IOException e) {
            System.err.println("Visit " + PROPERTY_FILE + " for updating "
                    + value + " value error");
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
            }
        }
    }
}
