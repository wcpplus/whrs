package org.farm2.wdap.convertor.impl;

import org.farm2.base.process.FarmProcessTypeEnum;
import org.farm2.base.process.FarmProcessUtils;
import org.farm2.files.domain.ResourceFile;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.tools.files.Farm2FileUtils;
import org.farm2.wdap.convertor.filemode.impl.ResourceFileMode;
import org.farm2.wdap.convertor.filemode.impl.TextFileMode;
import org.farm2.wdap.convertor.filemode.inter.FileModel;
import org.farm2.wdap.convertor.inter.FileConvertorInter;
import org.farm2.wdap.convertor.utils.ConvertResult;
import org.farm2.wdap.convertor.utils.ConvertUtils;
import org.farm2.wdap.convertor.utils.ConvertorParam;
import org.farm2.wdap.domain.WdapConvertor;
import org.mozilla.universalchardet.UniversalDetector;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TextEncodeConvertor implements FileConvertorInter {

    static {
        //注册转换器实现类
        ConvertUtils.registImpl((new TextEncodeConvertor()).getClass());
    }

    @Override
    public List<ConvertorParam> getParams() {
        return new ArrayList<>();
    }

    @Override
    public String getTitle() {
        return "编码文本文件";
    }

    @Override
    public String getNote() {
        return "拷贝到模型目录下，同时编码为utf-8";
    }

    @Override
    public ConvertResult runConvert(ResourceFile resourcefile, FileModel filemodel, WdapConvertor convertor, Map<String, ConvertorParam> params, Map<String, Object> flowContext, ResourceFileServiceInter resourceFileServiceImpl) {
        File file = filemodel.getFile(resourcefile, resourceFileServiceImpl);
        FarmProcessUtils.setProcess(resourcefile.getId(), FarmProcessTypeEnum.CONVERT_EXFILE, getTitle() + "...");
        ConvertResult result = new ConvertResult(true);
        TextFileMode textFileMode = new TextFileMode();
        File textdir = textFileMode.getDir(resourcefile, resourceFileServiceImpl);
        File textFile = Paths.get(textdir.toURI()).resolve("text.txt").toFile();
        try {
            Path sourcePath = file.toPath();
            // 🔍 自动检测源文件编码
            Charset detectedCharset = detectEncoding(sourcePath);
            // 📖 用检测到的编码读取内容
            String text = Files.readString(sourcePath, detectedCharset);
            // ✅ 直接传原始 text 字符串即可！
            // Farm2FileUtils 内部已用 UTF-8 写入，无需任何转换
            Farm2FileUtils.writeToFile(text, textFile);
            result.addFileModel(textFileMode.getKey());
            return result;
        } catch (IOException e) {
            throw new RuntimeException("编码检测或转换失败: " + e.getMessage());
        }
    }

    @Override
    public List<FileModel> getSourceFileModel() {
        return List.of(new ResourceFileMode());
    }

    @Override
    public FileModel getTargetFileModel() {
        return new TextFileMode();
    }

    @Override
    public void valideParams(List<ConvertorParam> params) {
    }


    /**
     * 自动检测文件编码
     *
     * @param filePath 文件路径
     * @return 推荐的 Charset，若无法识别则返回 UTF-8
     * @throws IOException
     */
    public static Charset detectEncoding(Path filePath) {

        try {
            byte[] buf = new byte[4096];
            try (FileInputStream fis = new FileInputStream(filePath.toFile())) {
                UniversalDetector detector = new UniversalDetector(null);

                int nread;
                while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
                    detector.handleData(buf, 0, nread);
                }
                detector.dataEnd();

                String encoding = detector.getDetectedCharset();
                detector.reset();

                if (encoding == null) {
                    // 无法检测，默认使用 UTF-8（也可根据业务改为 GBK）
                    return StandardCharsets.UTF_8;
                }

                // 将检测到的编码名映射为 Java 支持的 Charset
                return normalizeCharset(encoding);
            }
        } catch (IOException e) {
            return StandardCharsets.UTF_8;
        }
    }

    /**
     * 标准化编码名称（解决 juniversalchardet 返回名与 Java 不一致的问题）
     */
    private static Charset normalizeCharset(String encoding) {
        String enc = encoding.toLowerCase().trim();

        switch (enc) {
            case "utf-8":
            case "utf8":
                return StandardCharsets.UTF_8;
            case "gb2312":
            case "gbk":
            case "gb18030":
                return Charset.forName("GBK"); // Java 中 GBK 兼容 GB2312/GB18030
            case "big5":
                return Charset.forName("Big5");
            case "shift_jis":
            case "sjis":
                return Charset.forName("Shift_JIS");
            case "euc-kr":
                return Charset.forName("EUC-KR");
            case "iso-8859-1":
                return StandardCharsets.ISO_8859_1;
            case "windows-1252":
                return Charset.forName("Cp1252");
            default:
                // 尝试直接使用（部分编码如 UTF-16 可能直接支持）
                try {
                    return Charset.forName(encoding);
                } catch (Exception e) {
                    return StandardCharsets.UTF_8; // 降级
                }
        }
    }
}
