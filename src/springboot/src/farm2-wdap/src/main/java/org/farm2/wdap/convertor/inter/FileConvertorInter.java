package org.farm2.wdap.convertor.inter;

import org.farm2.files.domain.ResourceFile;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.wdap.convertor.utils.ConvertResult;
import org.farm2.wdap.convertor.filemode.inter.FileModel;
import org.farm2.wdap.convertor.utils.ConvertorParam;
import org.farm2.wdap.domain.WdapConvertor;

import java.util.List;
import java.util.Map;

public interface FileConvertorInter {

    /**
     * 获得实现类参数描述
     *
     * @return
     */
    public List<ConvertorParam> getParams();

    /**
     * 校验转换器参数
     *
     * @param params
     */
    public void valideParams(List<ConvertorParam> params);
    /**
     * 获得转换器的名称
     *
     * @return
     */
    public String getTitle();

    /**
     * 获得转换器描述
     *
     * @return
     */
    public String getNote();


    /**执行转换器
     * @param resourcefile 资源文件
     * @param convertor 转换器信息
     * @param params 转换器参数
     * @param flowContext 流程上下文
     * @return
     */
    public ConvertResult runConvert(ResourceFile resourcefile, FileModel filemodel, WdapConvertor convertor, Map<String, ConvertorParam> params, Map<String, Object> flowContext, ResourceFileServiceInter resourceFileServiceImpl);

    /**
     * 获得转换器源文件类型(支持的所有原文件模型)
     *
     * @return
     */
    public List<FileModel> getSourceFileModel();

    /**
     * 获得转换器目标文件类型
     *
     * @return
     */
    public FileModel getTargetFileModel();


}
