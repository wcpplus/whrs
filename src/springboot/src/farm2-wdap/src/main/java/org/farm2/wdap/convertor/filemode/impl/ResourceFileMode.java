package org.farm2.wdap.convertor.filemode.impl;

import org.farm2.files.domain.ResourceFile;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.wdap.convertor.filemode.inter.FileModel;
import org.farm2.wdap.utils.FileModelUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 原始文件类型
 */
public class ResourceFileMode implements FileModel {
    static {
        FileModelUtils.registFileModel(new ResourceFileMode());
    }
    @Override
    public String getKey() {
        return "RESOURCE_FILE";
    }

    @Override
    public String getTitle() {
        return "原始文件";
    }

    @Override
    public String getNote() {
        return "原文件";
    }

    @Override
    public List<File> getFiles(ResourceFile resourceFile, ResourceFileServiceInter resourceFileServiceImpl) {
        List<File> files = new ArrayList<>();
        files.add(resourceFileServiceImpl.getFile(resourceFile));
        return files;
    }

    @Override
    public File getDir(ResourceFile resourceFile, ResourceFileServiceInter resourceFileServiceImpl) {
        //无此类型
        return null;
    }

    @Override
    public File getFile(ResourceFile resourceFile, ResourceFileServiceInter resourceFileServiceImpl) {
        return resourceFileServiceImpl.getFile(resourceFile);
    }
}
