package org.farm2.wdap.convertor.filemode.impl;

import org.farm2.files.domain.ResourceFile;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.wdap.convertor.filemode.enums.ThumbnalEnum;
import org.farm2.wdap.convertor.filemode.inter.FileModel;
import org.farm2.wdap.utils.FileModelUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 文本文件类型
 */
public class TextFileMode implements FileModel {
    static {
        FileModelUtils.registFileModel(new TextFileMode());
    }

    @Override
    public String getKey() {
        return "TEXT_FILE";
    }

    @Override
    public String getTitle() {
        return "文本文件";
    }

    @Override
    public String getNote() {
        return "可以直接读取展示的文件类型";
    }

    @Override
    public List<File> getFiles(ResourceFile resourceFile, ResourceFileServiceInter resourceFileServiceImpl) {
        List<File> files = new ArrayList<>();
        File viewPath = resourceFileServiceImpl.getExDirPath(resourceFile);
        Path path = Paths.get(viewPath.toURI()).resolve("text");
        return Arrays.stream(path.toFile().listFiles()).toList();
    }

    @Override
    public File getDir(ResourceFile resourceFile, ResourceFileServiceInter resourceFileServiceImpl) {
        List<File> files = new ArrayList<>();
        File viewPath = resourceFileServiceImpl.getExDirPath(resourceFile);
        Path path = Paths.get(viewPath.toURI()).resolve("text");
        return path.toFile();
    }

    @Override
    public File getFile(ResourceFile resourceFile, ResourceFileServiceInter resourceFileServiceImpl) {
        List<File> files = new ArrayList<>();
        File viewPath = resourceFileServiceImpl.getExDirPath(resourceFile);
        Path path = Paths.get(viewPath.toURI()).resolve("text");
        if (!path.toFile().exists()|| path.toFile().listFiles().length <= 0) {
            File textDir = getDir(resourceFile, resourceFileServiceImpl);
            textDir.mkdirs();
            File textFile = Paths.get(textDir.toURI()).resolve("text.txt").toFile();
            return textFile;
        }
        if (path.toFile().listFiles().length > 0) {
            return path.toFile().listFiles()[0];
        } else {
            return null;
        }
    }
}
