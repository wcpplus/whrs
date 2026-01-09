package org.farm2.wdap.convertor.filemode.impl;

import org.farm2.files.domain.ResourceFile;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.wdap.convertor.filemode.enums.ThumbnalEnum;
import org.farm2.wdap.convertor.filemode.inter.FileModel;
import org.farm2.wdap.utils.FileModelUtils;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 缩略图文件类型
 */
public class ThumbnailFileMode implements FileModel {
    static {
        FileModelUtils.registFileModel(new ThumbnailFileMode());
    }
    @Override
    public String getKey() {
        return "THUMBNAIL_FILE";
    }

    @Override
    public String getTitle() {
        return "缩略图文件夹";
    }

    @Override
    public String getNote() {
        return "缩略图文件夹，存放MAX，MIDDLE ，MIN，三种尺寸规格";
    }


    @Override
    public List<File> getFiles(ResourceFile resourceFile, ResourceFileServiceInter resourceFileServiceImpl) {
        List<File> files = new ArrayList<>();
        File viewPath = resourceFileServiceImpl.getExDirPath(resourceFile);
        files.add(new File(Paths.get(viewPath.toURI()).resolve("thumbnail").resolve(ThumbnalEnum.MIN.name()) + "." + resourceFile.getExname()));
        files.add(new File(Paths.get(viewPath.toURI()).resolve("thumbnail").resolve(ThumbnalEnum.MIDDLE.name()) + "." + resourceFile.getExname()));
        files.add(new File(Paths.get(viewPath.toURI()).resolve("thumbnail").resolve(ThumbnalEnum.MAX.name()) + "." + resourceFile.getExname()));
        return files;
    }

    @Override
    public File getDir(ResourceFile resourceFile, ResourceFileServiceInter resourceFileServiceImpl) {
        List<File> files = new ArrayList<>();
        File viewPath = resourceFileServiceImpl.getExDirPath(resourceFile);
        return Paths.get(viewPath.toURI()).resolve("thumbnail").toFile();
    }

    @Override
    public File getFile(ResourceFile resourceFile, ResourceFileServiceInter resourceFileServiceImpl) {
        //无此类型
        throw new RuntimeException("没有该类型的文件可以读取");
    }
}
