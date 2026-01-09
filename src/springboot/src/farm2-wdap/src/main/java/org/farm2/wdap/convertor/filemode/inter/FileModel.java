package org.farm2.wdap.convertor.filemode.inter;

import org.farm2.files.domain.ResourceFile;
import org.farm2.files.service.ResourceFileServiceInter;

import java.io.File;
import java.util.List;

/**
 * 文件模型
 */
public interface FileModel {

    public String getKey();

    public String getTitle();

    public String getNote();

    /**
     * 返回模型文件列表
     *
     * @param resourceFile
     * @param resourceFileServiceImpl
     * @return
     */
    public List<File> getFiles(ResourceFile resourceFile, ResourceFileServiceInter resourceFileServiceImpl);

    /**
     * 返回模型文件夹
     *
     * @param resourceFile
     * @param resourceFileServiceImpl
     * @return
     */
    public File getDir(ResourceFile resourceFile, ResourceFileServiceInter resourceFileServiceImpl);


    /**
     * 返回单个模型文件
     *
     * @param resourceFile
     * @param resourceFileServiceImpl
     * @return
     */
    public File getFile(ResourceFile resourceFile, ResourceFileServiceInter resourceFileServiceImpl);
}
