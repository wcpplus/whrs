package org.farm2.wdap.convertor.filemode.impl;

import org.farm2.files.domain.ResourceFile;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.wdap.convertor.filemode.inter.FileModel;
import org.farm2.wdap.utils.FileModelUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PdfFileMode implements FileModel {
    static {
        FileModelUtils.registFileModel(new PdfFileMode());
    }

    @Override
    public String getKey() {
        return "PDF_FILE";
    }

    @Override
    public String getTitle() {
        return "pdf文件";
    }

    @Override
    public String getNote() {
        return "可以直接预览或下载";
    }

    @Override
    public List<File> getFiles(ResourceFile resourceFile, ResourceFileServiceInter resourceFileServiceImpl) {
        List<File> files = new ArrayList<>();
        File viewPath = resourceFileServiceImpl.getExDirPath(resourceFile);
        Path path = Paths.get(viewPath.toURI()).resolve("pdf");
        return Arrays.stream(path.toFile().listFiles()).toList();
    }

    @Override
    public File getDir(ResourceFile resourceFile, ResourceFileServiceInter resourceFileServiceImpl) {
        List<File> files = new ArrayList<>();
        File viewPath = resourceFileServiceImpl.getExDirPath(resourceFile);
        Path path = Paths.get(viewPath.toURI()).resolve("pdf");
        return path.toFile();
    }

    @Override
    public File getFile(ResourceFile resourceFile, ResourceFileServiceInter resourceFileServiceImpl) {
        List<File> files = new ArrayList<>();
        File viewPath = resourceFileServiceImpl.getExDirPath(resourceFile);
        Path path = Paths.get(viewPath.toURI()).resolve("pdf");
        if (!path.toFile().exists() || path.toFile().listFiles().length <= 0) {
            File pdfDir = getDir(resourceFile, resourceFileServiceImpl);
            pdfDir.mkdirs();
            File pdfFile = Paths.get(pdfDir.toURI()).resolve("file.pdf").toFile();
            return pdfFile;
        }
        if (path.toFile().listFiles().length > 0) {
            return path.toFile().listFiles()[0];
        }
        return null;
    }
}
