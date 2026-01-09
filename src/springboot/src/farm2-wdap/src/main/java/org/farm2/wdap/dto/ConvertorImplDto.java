package org.farm2.wdap.dto;

import lombok.Data;
import org.farm2.wdap.convertor.filemode.inter.FileModel;

import java.util.List;

/**
 * 封装转换器实现类信息
 */
@Data
public class ConvertorImplDto {

    /**
     * class
     */
    private String classKey;

    /**
     * 转换器类型
     */
    private String title;

    /**
     * 转换器描述
     */
    private String note;

    /**
     * 支持的文件模型
     */
    private List<FileModel> sFileModels;
}
