package org.farm2.tools.files;

import org.farm2.tools.files.domain.FilesNum;

import java.io.File;

/**
 * 单个文件处理类
 */
public interface Farm2FileHandle {
    /**
     * 单个文件处理方法
     *
     * @param file    文件
     * @param fileNum 文件数量
     * @return 是否计数（当前文件是否算入总文件数量）
     */
    public void handle(File file, FilesNum fileNum);

}
