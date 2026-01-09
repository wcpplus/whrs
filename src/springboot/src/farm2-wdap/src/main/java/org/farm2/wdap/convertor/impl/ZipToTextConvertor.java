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

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipToTextConvertor implements FileConvertorInter {
    static {
        //注册转换器实现类
        ConvertUtils.registImpl((new ZipToTextConvertor()).getClass());
    }

    @Override
    public List<ConvertorParam> getParams() {
        return List.of(new ConvertorParam("ENCODE", "编码", "GBK或UTF-8", "GBK"));
    }

    @Override
    public void valideParams(List<ConvertorParam> params) {
        if (params.size() <= 0) {
            throw new RuntimeException("未找到ENCODE参数");
        }

        if (!params.get(0).getValue().toUpperCase().equals("GBK") && !params.get(0).getValue().toUpperCase().equals("UTF-8")) {
            throw new RuntimeException("编码请使用GBK或UTF-8");
        }
    }

    @Override
    public String getTitle() {
        return "Zip转目录结构文本";
    }

    @Override
    public String getNote() {
        return "实现将zip内容读取为文本，内容为所有目录结构和文件名称";
    }

    @Override
    public ConvertResult runConvert(ResourceFile resourcefile, FileModel filemodel, WdapConvertor convertor, Map<String, ConvertorParam> params, Map<String, Object> flowContext, ResourceFileServiceInter resourceFileServiceImpl) {
        File file = filemodel.getFile(resourcefile, resourceFileServiceImpl);
        FarmProcessUtils.setProcess(resourcefile.getId(), FarmProcessTypeEnum.CONVERT_EXFILE, getTitle() + "...");
        StringBuffer fileText = new StringBuffer();
        try (FileInputStream fis = new FileInputStream(file)) {
            try (ZipInputStream zis = new ZipInputStream(fis, Charset.forName(params.get("ENCODE").getValue()))) {
                ZipEntry zipEntry;
                while ((zipEntry = zis.getNextEntry()) != null) {
                    String entryName = zipEntry.getName();
                    // 假设不需要处理编码问题，这里直接使用entryName
                    if (!zipEntry.isDirectory()) {
                        fileText.append("文件: ").append(entryName).append("\n");
                    } else {
                        fileText.append("目录: ").append(entryName).append("\n");
                    }
                    zis.closeEntry();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //--------------------------------------
        TextFileMode textFileMode = new TextFileMode();
        File textDir = textFileMode.getFile(resourcefile, resourceFileServiceImpl);
        Farm2FileUtils.writeToFile(fileText.toString(), textDir);
        ConvertResult result = new ConvertResult(true);
        result.addFileModel(new TextFileMode().getKey());
        return result;
    }

    @Override
    public List<FileModel> getSourceFileModel() {
        return List.of(new ResourceFileMode());
    }

    @Override
    public FileModel getTargetFileModel() {
        return new TextFileMode();
    }


    /**
     * 在getEntryNameWithCorrectEncoding方法中，首先尝试使用UTF-8解码ZIP条目名称。如果UTF-8解码失败，则尝试使用ISO-8859-1或其他编码方式。
     *
     * @param zipEntry
     * @return
     */
    private String getEntryNameWithCorrectEncoding(ZipEntry zipEntry) {
        // 尝试使用UTF-8解码
        byte[] bytes = zipEntry.getName().getBytes(StandardCharsets.UTF_8);
        try {
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            // 如果UTF-8解码失败，尝试使用ISO-8859-1或其他编码
            return new String(bytes, StandardCharsets.ISO_8859_1);
        }
    }
}
