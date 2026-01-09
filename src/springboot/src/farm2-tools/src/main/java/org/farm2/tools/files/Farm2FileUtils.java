package org.farm2.tools.files;

import lombok.extern.slf4j.Slf4j;
import org.farm2.tools.db.commons.FarmUUID;
import org.farm2.tools.files.domain.FilesNum;
import org.mozilla.universalchardet.UniversalDetector;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class Farm2FileUtils {

    /**
     * @param path 递归删除指定目录及其所有子目录和文件
     * @throws IOException
     */
    public static void deleteFolderRecursively(Path path) throws IOException {
        if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
            try (DirectoryStream<Path> entries = Files.newDirectoryStream(path)) {
                for (Path entry : entries) {
                    deleteFolderRecursively(entry);
                }
            }
        }
        Files.delete(path);
    }

    /**
     * 拷贝文件
     *
     * @param file   原文件
     * @param toFile 目标文件
     * @param handle 拷贝进度回调方法
     */
    public static void copyTo(File file, File toFile, Farm2ProcessHandle handle) {
        // 确保父目录存在
        File parentDir = toFile.getParentFile();
        if (!parentDir.exists() && !parentDir.mkdirs()) {
            throw new RuntimeException("无法创建目标文件的父目录: " + parentDir.getAbsolutePath());
        }
        try (InputStream is = new FileInputStream(file); OutputStream os = new FileOutputStream(toFile)) {
            long fileSize = file.length();
            byte[] buffer = new byte[1024];
            int length;
            long totalRead = 0;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
                totalRead += length;
                // 计算并设置进度百分比
                int percent = (int) ((totalRead * 100) / fileSize);
                handle.handle(percent);
            }
        } catch (IOException e) {
            // 处理异常情况
            throw new RuntimeException("文件拷贝失败: " + e.getMessage(), e);
        }
    }

    /**
     * 读取文本文件内容，支持指定编码方式。
     *
     * @param textFile    文本文件对象
     * @param charsetName 编码方式名称，默认为 "UTF-8"
     * @return 文件内容字符串
     * @throws IOException 如果发生 I/O 错误
     */
    public static String readText(File textFile, String charsetName) throws IOException {
        // 如果未指定编码方式，则使用默认的 UTF-8 编码
        if (charsetName == null || charsetName.isEmpty()) {
            charsetName = "UTF-8";
        }

        // 使用 Files.readString 方法读取文件内容
        try {
            return new String(Files.readAllBytes(textFile.toPath()), Charset.forName(charsetName));
        } catch (IOException e) {
            log.error("无法读取文件: [文件路径已隐藏]" + e.getMessage(), e);
            return "无法读取文件: [文件路径已隐藏]" + textFile.getName();
        }
    }

    // 重载方法，提供默认的 UTF-8 编码方式
    public static String readText(File textFile) throws IOException {
        return readText(textFile, "UTF-8");
    }

    public static String readFile(String filePath) {
        try {
            File file = new File(filePath);
            // 读取文件内容到字节数组
            byte[] fileContentBytes;
            try (FileInputStream fis = new FileInputStream(file)) {
                fileContentBytes = fis.readAllBytes();
            }
            // 使用 UniversalDetector 检测编码
            UniversalDetector detector = new UniversalDetector(null);
            detector.handleData(fileContentBytes, 0, fileContentBytes.length);
            detector.dataEnd();
            String detectedEncoding = detector.getDetectedCharset();
            detector.reset();
            // 如果没有检测到编码或者检测结果为 windows-1252（可能误报）
            if (detectedEncoding == null || "windows-1252".equals(detectedEncoding)) {
                // 尝试判断是否为 GBK 编码
                Charset gbk = Charset.forName("GBK");
                try {
                    String content = new String(fileContentBytes, gbk);
                    // 简单验证是否是有效的 GBK 字符串
                    if (content.getBytes(gbk).length == fileContentBytes.length) {
                        return content;
                    }
                } catch (Exception e) {
                    // 如果不是有效的 GBK 编码，继续尝试其他编码
                }
                // 如果没有找到合适的编码，使用默认编码
                detectedEncoding = Charset.defaultCharset().name();
            }
            // 根据检测到的编码读取文件内容
            return new String(fileContentBytes, detectedEncoding);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeToFile(String text, File textFile) {
        // 确保父目录存在，如果不存在则创建
        if (!textFile.getParentFile().exists() && !textFile.getParentFile().mkdirs()) {
            System.out.println("Failed to create directory for file: " + textFile.getPath());
            return;
        }

        BufferedWriter writer = null;
        try {
            // 创建FileOutputStream对象
            FileOutputStream fos = new FileOutputStream(textFile, false); // 'false'参数表示覆盖模式打开文件
            // 使用OutputStreamWriter和BufferedWriter指定UTF-8编码
            writer = new BufferedWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8));

            // 写入文本
            writer.write(text);
            // 换行符，便于下次写入时换行
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭writer
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    public static void writeToFile(String text, File textFile) {
//        // 确保父目录存在，如果不存在则创建
//        if (!textFile.getParentFile().exists() && !textFile.getParentFile().mkdirs()) {
//            System.out.println("Failed to create directory for file." + textFile.getPath());
//            return;
//        }
//        BufferedWriter writer = null;
//        try {
//            // 创建FileWriter对象
//            writer = new BufferedWriter(new FileWriter(textFile, false)); // 'true'参数表示以追加模式打开文件
//            // 写入文本
//            writer.write(text);
//            // 换行符，便于下次写入时换行
//            writer.newLine();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            // 关闭writer
//            if (writer != null) {
//                try {
//                    writer.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }


    /**
     * 按文件名排序
     *
     * @param path
     * @return
     */
    public static List<File> getSortedFiles(Path path) {
        // 确保目录存在
        if (path == null || !path.toFile().isDirectory()) {
            throw new IllegalArgumentException("Path must be a valid directory.");
        }

        // 获取文件列表并按名称排序
        return Arrays.stream(path.toFile().listFiles())
                .sorted((f1, f2) -> f1.getName().compareTo(f2.getName())) // 按文件名排序
                .collect(Collectors.toList());
    }

    public static String getExName(File targetFile) {
        String fileName = targetFile.getName();
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        int lastIndexOfDot = fileName.lastIndexOf('.');
        // 如果找不到 '.' 或者 '.' 是最后一个字符，则认为没有扩展名
        if (lastIndexOfDot == -1 || lastIndexOfDot == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(lastIndexOfDot + 1);
    }

    /**
     * 是否为图片
     *
     * @param exname
     * @return
     */
    public static boolean isImg(String exname) {
//        exname是附件扩展名
        // 检查输入是否为空或无效
        if (exname == null || exname.isEmpty()) {
            return false;
        }
        // 定义常见的图片扩展名
        String[] imgExts = {"jpg", "jpeg", "png", "gif", "webp",
                //    "tiff","bmp", "svg"
        };
        // 转换为小写以便统一比较
        exname = exname.toLowerCase();
        // 判断扩展名是否在图片扩展名列表中
        for (String ext : imgExts) {
            if (exname.equals(ext)) {
                return true;
            }
        }
        // 如果不在列表中，返回 false
        return false;
    }

    /**
     * 判断一个图片的长宽比是否满足1:1.6到1:1.2之间
     *
     * @param file   文件
     * @param exname 扩展名（用于简单校验）
     * @return 如果长宽比在范围内返回true，否则返回false
     */
    public static boolean viewImg(File file, String exname) {
        // 检查文件是否存在或扩展名是否为空
        if (file == null || !file.exists() || exname == null || exname.isEmpty()) {
            return false;
        }

        // 检查文件大小是否超过1MB
        long maxSize = 1024 * 1024; // 1MB
        if (file.length() > maxSize) {
            return false; // 文件过大，不处理
        }

        try {
            // 根据扩展名校验图片格式（可选）
            String lowerExname = exname.toLowerCase();
            if (!lowerExname.equals("jpg") && !lowerExname.equals("jpeg") && !lowerExname.equals("png") && !lowerExname.equals("gif")) {
                return false; // 不支持的图片格式
            }

            // 读取图片文件
            BufferedImage image = ImageIO.read(file);

            if (image == null) {
                return false; // 无法读取图片
            }
            // 获取图片的宽度和高度
            int width = image.getWidth();
            int height = image.getHeight();
            if (width <= 256 || height <= 0) {
                return false; // 图片尺寸无效
            }
            // 计算长宽比
            double ratio = (double) width / height;
            double ratioB = (double) height / width;
            // 判断长宽比是否在1:1.2到1:1.6之间
            return (ratio >= 1 && ratio <= 2) || (ratioB >= 1 && ratioB <= 2);
        } catch (Exception e) {
            // 捕获异常，例如文件损坏或格式错误
            return false;
        }
    }

    /**
     * 获取当前项目所在文件目录地址
     *
     * @return 当前classpath对应的文件目录路径
     */
    public static File getDefaultFileBasePath(String path) {
        try {
            File basePath = new File(System.getProperty("user.dir")).getAbsoluteFile();
            if (basePath != null) {
                return basePath.toPath().getRoot().toFile().toPath().resolve(Path.of("skcFiles").resolve(path)).toFile(); // 获取磁盘根目录
            }
            throw new FileNotFoundException("无法获得默认目录");
        } catch (Exception e) {
            e.printStackTrace();
            return new File(System.getProperty("user.home")); // 默认返回用户主目录
        }
    }

    /**
     * 迭代dir目录中的所有子目录和文件，并由handle处理
     *
     * @param dir
     * @param handle
     */
    public static int runFileHandleByDir(File dir, Farm2FileHandle handle) {
        if (dir == null || !dir.isDirectory()) {
            throw new IllegalArgumentException("The provided directory must be a valid directory.");
        }
        FilesNum num = new FilesNum();
        // 处理文件
        processFiles(dir, handle, num);
        return num.getNum();
    }

    /**
     * 处理目录及其子目录中的文件
     *
     * @param dir    目录
     * @param handle 处理器
     */
    private static void processFiles(File dir, Farm2FileHandle handle, FilesNum fileNum) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    handle.handle(file, fileNum);
                } else if (file.isDirectory()) {
                    handle.handle(file, fileNum);
                    processFiles(file, handle, fileNum);
                }
            }
        }
    }

    /**
     * 由base64字符串获取文件名称
     *
     * @param base64
     * @return
     */
    public static String getFileNameByBase64(String base64) {
        // 默认生成 UUID 作为文件名
        String filename = FarmUUID.getUUID32();

        // 如果需要解析 MIME 类型前缀中的文件扩展名
        if (base64 != null && base64.startsWith("data:")) {
            String mimeType = base64.split(";")[0].split(":")[1];
            String extension = getExtensionFromMimeType(mimeType);
            if (extension != null) {
                filename += "." + extension;
            }
        }

        return filename;
    }

    /**
     * 根据 MIME 类型获取文件扩展名
     *
     * @param mimeType MIME 类型
     * @return 文件扩展名
     */
    private static String getExtensionFromMimeType(String mimeType) {
        switch (mimeType) {
            case "image/jpeg":
                return "jpg";
            case "image/png":
                return "png";
            case "application/pdf":
                return "pdf";
            default:
                return "file"; // 不支持的类型
        }
    }

    /**
     * 由base64字符串获取byte数组
     *
     * @param base64Data
     * @return
     */
    public static byte[] getBytesByBase64(String base64Data) {
        if (base64Data == null || base64Data.isEmpty()) {
            throw new IllegalArgumentException("Base64 数据为空");
        }
        // 去除 MIME 类型前缀（如果存在）
        if (base64Data.contains(",")) {
            base64Data = base64Data.split(",")[1]; // 取逗号后面的内容
        }
        // 解码 Base64 数据
        return Base64.getDecoder().decode(base64Data);
    }
}
