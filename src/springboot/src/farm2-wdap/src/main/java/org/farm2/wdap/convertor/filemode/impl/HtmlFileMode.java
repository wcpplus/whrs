package org.farm2.wdap.convertor.filemode.impl;

import org.farm2.files.domain.ResourceFile;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.wdap.convertor.filemode.inter.FileModel;
import org.farm2.wdap.utils.FileModelUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class HtmlFileMode implements FileModel {
    static {
        FileModelUtils.registFileModel(new HtmlFileMode());
    }

    @Override
    public String getKey() {
        return "HTML_FILE";
    }

    @Override
    public String getTitle() {
        return "HTML文件";
    }

    @Override
    public String getNote() {
        return "HTML文件";
    }

    @Override
    public List<File> getFiles(ResourceFile resourceFile, ResourceFileServiceInter resourceFileServiceImpl) {
        List<File> files = new ArrayList<>();
        files.add(getFile(resourceFile, resourceFileServiceImpl));
        return files;
    }

    @Override
    public File getDir(ResourceFile resourceFile, ResourceFileServiceInter resourceFileServiceImpl) {
        List<File> files = new ArrayList<>();
        File viewPath = resourceFileServiceImpl.getExDirPath(resourceFile);
        Path path = Paths.get(viewPath.toURI()).resolve("html");
        return path.toFile();
    }

    @Override
    public File getFile(ResourceFile resourceFile, ResourceFileServiceInter resourceFileServiceImpl) {
        List<File> files = new ArrayList<>();
        File viewPath = resourceFileServiceImpl.getExDirPath(resourceFile);
        Path path = Paths.get(viewPath.toURI()).resolve("html");
        if (!path.toFile().exists() || path.toFile().listFiles().length <= 0) {
            File textDir = getDir(resourceFile, resourceFileServiceImpl);
            textDir.mkdirs();
            File textFile = Paths.get(textDir.toURI()).resolve("file.html").toFile();
            return textFile;
        }
        if (path.toFile().listFiles().length > 0) {
            return path.toFile().listFiles()[0];
        } else {
            return null;
        }
    }
}
