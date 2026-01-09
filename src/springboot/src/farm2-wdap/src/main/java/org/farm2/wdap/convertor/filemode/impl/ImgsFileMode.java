package org.farm2.wdap.convertor.filemode.impl;

import lombok.extern.slf4j.Slf4j;
import org.farm2.files.domain.ResourceFile;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.tools.files.Farm2FileUtils;
import org.farm2.wdap.convertor.filemode.inter.FileModel;
import org.farm2.wdap.utils.FileModelUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.farm2.wdap.convertor.impl.PdfToImgConvertor.MAX_DIR_NAME;
import static org.farm2.wdap.convertor.impl.PdfToImgConvertor.MIN_DIR_NAME;

/**
 * 图片册模型
 */
@Slf4j
public class ImgsFileMode implements FileModel {
    static {
        FileModelUtils.registFileModel(new ImgsFileMode());
    }

    @Override
    public String getKey() {
        return "IMGS_FILE";
    }

    @Override
    public String getTitle() {
        return "图片册文件夹";
    }

    @Override
    public String getNote() {
        return "有多个图片组成展示一个附件";
    }

    @Override
    public List<File> getFiles(ResourceFile resourceFile, ResourceFileServiceInter resourceFileServiceImpl) {
        List<File> files = new ArrayList<>();
        File viewPath = resourceFileServiceImpl.getExDirPath(resourceFile);
        Path path = Paths.get(viewPath.toURI()).resolve("imgs");
        return Farm2FileUtils.getSortedFiles(path);
    }

    @Override
    public File getDir(ResourceFile resourceFile, ResourceFileServiceInter resourceFileServiceImpl) {
        List<File> files = new ArrayList<>();
        File viewPath = resourceFileServiceImpl.getExDirPath(resourceFile);
        Path path = Paths.get(viewPath.toURI()).resolve("imgs");
        return path.toFile();
    }

    @Override
    public File getFile(ResourceFile resourceFile, ResourceFileServiceInter resourceFileServiceImpl) {
        List<File> files = new ArrayList<>();
        File viewPath = resourceFileServiceImpl.getExDirPath(resourceFile);
        Path path = Paths.get(viewPath.toURI()).resolve("imgs");
        if (path.toFile().listFiles().length > 0) {
            return path.toFile().listFiles()[0];
        } else {
            return path.resolve("img." + resourceFile.getExname()).toFile();
        }
    }

    /**
     * 获得缩略图
     *
     * @param resourceFile
     * @param resourceFileServiceImpl
     * @param type                    缩略图类型   0:小,2:大
     * @return
     */
    public List<File> getFiles(ResourceFile resourceFile, ResourceFileServiceInter resourceFileServiceImpl, String type) {
        try {
        ImgsFileMode imgsFileMode = new ImgsFileMode();
        //----------------------------------------------------
        File outputDir = imgsFileMode.getDir(resourceFile, resourceFileServiceImpl);
        if (type.equals("0")) {
            //小缩略图
            List<File> files = new ArrayList<>();
            File viewPath = resourceFileServiceImpl.getExDirPath(resourceFile);
            Path path = Paths.get(viewPath.toURI()).resolve("imgs");
            return Farm2FileUtils.getSortedFiles(path);
        }
        if (type.equals("2")) {
            //大缩略图
            List<File> files = new ArrayList<>();
            File viewPath = resourceFileServiceImpl.getExDirPath(resourceFile);
            Path path = Paths.get(viewPath.toURI()).resolve("imgs");
            return Farm2FileUtils.getSortedFiles(path);
        }
        return getFiles(resourceFile, resourceFileServiceImpl);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }
}
