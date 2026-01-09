package org.farm2.wdap.convertor.filemode.impl;

import com.alibaba.fastjson2.JSONObject;
import org.farm2.files.domain.ResourceFile;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.tools.files.Farm2FileUtils;
import org.farm2.tools.json.FarmJsons;
import org.farm2.tools.web.domain.TextParagraph;
import org.farm2.wdap.convertor.filemode.inter.FileModel;
import org.farm2.wdap.utils.FileModelUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 段落
 */
public class SegmentFileMode implements FileModel {
    static {
        FileModelUtils.registFileModel(new SegmentFileMode());
    }

    public static void main(String[] args) {
        String path = "D:\\temp\\farm2files\\wdap\\tempdir\\2025\\02\\14\\16\\b8599f8ae5c141448558df25d8fd9ee2\\segment\\segment.json";

        List<TextParagraph> paras = getTextParagraphs(new File(path));
        System.out.println(paras.size());
    }


    /**
     * 从文件读取所有分段文本
     *
     * @param jsonFile
     * @return
     */
    public static List<TextParagraph> getTextParagraphs(File jsonFile) {
        try {
            String json = Farm2FileUtils.readText(jsonFile);
            List<TextParagraph> paragraphs = new ArrayList<>();
            List<JSONObject> list = FarmJsons.toBean(json, List.class);
            for (JSONObject obj : list) {
                paragraphs.add(FarmJsons.toBean(obj.toString(), TextParagraph.class));
            }
            return paragraphs;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getKey() {
        return "SEGMENT_FILE";
    }

    @Override
    public String getTitle() {
        return "段落文件";
    }

    @Override
    public String getNote() {
        return "分段为JSON数组格式[{sort:number,text:string}]";
    }

    @Override
    public List<File> getFiles(ResourceFile resourceFile, ResourceFileServiceInter resourceFileServiceImpl) {
        List<File> files = new ArrayList<>();
        File viewPath = resourceFileServiceImpl.getExDirPath(resourceFile);
        Path path = Paths.get(viewPath.toURI()).resolve("segment");
        return Arrays.stream(path.toFile().listFiles()).toList();
    }

    @Override
    public File getDir(ResourceFile resourceFile, ResourceFileServiceInter resourceFileServiceImpl) {
        List<File> files = new ArrayList<>();
        File viewPath = resourceFileServiceImpl.getExDirPath(resourceFile);
        Path path = Paths.get(viewPath.toURI()).resolve("segment");
        return path.toFile();
    }

    @Override
    public File getFile(ResourceFile resourceFile, ResourceFileServiceInter resourceFileServiceImpl) {
        List<File> files = new ArrayList<>();
        File viewPath = resourceFileServiceImpl.getExDirPath(resourceFile);
        Path path = Paths.get(viewPath.toURI()).resolve("segment");
        if (!path.toFile().exists()|| path.toFile().listFiles().length <= 0) {
            File textDir = getDir(resourceFile, resourceFileServiceImpl);
            textDir.mkdirs();
            File textFile = Paths.get(textDir.toURI()).resolve("segment.json").toFile();
            return textFile;
        }
        if (path.toFile().listFiles().length > 0) {
            return path.toFile().listFiles()[0];
        } else {
            return null;
        }
    }
}
