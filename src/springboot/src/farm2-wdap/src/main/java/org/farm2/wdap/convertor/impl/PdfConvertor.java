package org.farm2.wdap.convertor.impl;

import org.farm2.base.process.FarmProcessTypeEnum;
import org.farm2.base.process.FarmProcessUtils;
import org.farm2.files.domain.ResourceFile;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.tools.files.Farm2FileUtils;
import org.farm2.tools.files.Farm2ProcessHandle;
import org.farm2.wdap.convertor.filemode.impl.PdfFileMode;
import org.farm2.wdap.convertor.filemode.impl.ResourceFileMode;
import org.farm2.wdap.convertor.filemode.inter.FileModel;
import org.farm2.wdap.convertor.inter.FileConvertorInter;
import org.farm2.wdap.convertor.utils.ConvertResult;
import org.farm2.wdap.convertor.utils.ConvertUtils;
import org.farm2.wdap.convertor.utils.ConvertorParam;
import org.farm2.wdap.domain.WdapConvertor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PdfConvertor implements FileConvertorInter {

    static {
        //注册转换器实现类
        ConvertUtils.registImpl((new PdfConvertor()).getClass());
    }

    @Override
    public List<ConvertorParam> getParams() {
        List<ConvertorParam> paras = new ArrayList<>();
        return paras;
    }

    @Override
    public String getTitle() {
        return "PDF复制预览";
    }

    @Override
    public String getNote() {
        return "PDF复制到PDF预览文件";
    }

    @Override
    public ConvertResult runConvert(ResourceFile resourcefile, FileModel filemodel, WdapConvertor convertor, Map<String, ConvertorParam> params, Map<String, Object> flowContext, ResourceFileServiceInter resourceFileServiceImpl) {
        File file = filemodel.getFile(resourcefile, resourceFileServiceImpl);
        FarmProcessUtils.setProcess(resourcefile.getId(), FarmProcessTypeEnum.CONVERT_EXFILE, getTitle() + "...");
        PdfFileMode pdfFileMode = new PdfFileMode();
        //----------------------------------------------------
        File pdffile = pdfFileMode.getFile(resourcefile, resourceFileServiceImpl);
        Farm2FileUtils.copyTo(file, pdffile, new Farm2ProcessHandle() {
            @Override
            public void handle(int process) {
                FarmProcessUtils.setProcess(resourcefile.getId(), FarmProcessTypeEnum.CONVERT_EXFILE, getTitle() + (process) + "%...");
            }
        });
        //-----------------------------------------------------
        ConvertResult result = new ConvertResult(true);
        result.addFileModel(pdfFileMode.getKey());
        return result;
    }


    @Override
    public List<FileModel> getSourceFileModel() {
        List<FileModel> list = new ArrayList<>();
        list.add(new ResourceFileMode());
        return list;
    }

    @Override
    public FileModel getTargetFileModel() {
        return new PdfFileMode();
    }

    @Override
    public void valideParams(List<ConvertorParam> params) {
    }
}
