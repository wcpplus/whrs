package org.farm2.wdap.convertor.impl;

import org.farm2.base.process.FarmProcessTypeEnum;
import org.farm2.base.process.FarmProcessUtils;
import org.farm2.files.domain.ResourceFile;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.tools.files.Farm2FileUtils;
import org.farm2.tools.files.Farm2ProcessHandle;
import org.farm2.wdap.convertor.filemode.impl.NoneFileMode;
import org.farm2.wdap.convertor.filemode.impl.ResourceFileMode;
import org.farm2.wdap.convertor.filemode.inter.FileModel;
import org.farm2.wdap.convertor.inter.FileConvertorInter;
import org.farm2.wdap.convertor.utils.ConvertResult;
import org.farm2.wdap.convertor.utils.ConvertUtils;
import org.farm2.wdap.convertor.utils.ConvertorParam;
import org.farm2.wdap.domain.WdapConvertor;
import org.farm2.wdap.utils.FileModelUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CopyToFileModelConvertor implements FileConvertorInter {

    static {
        //注册转换器实现类
        ConvertUtils.registImpl((new CopyToFileModelConvertor()).getClass());
    }

    @Override
    public List<ConvertorParam> getParams() {
        List<ConvertorParam> paras = new ArrayList<>();
        String modekeys = "";
        for (FileModel mode : FileModelUtils.getModels()) {
            modekeys = modekeys + "," + mode.getTitle() + ":" + mode.getKey();
        }
        paras.add(new ConvertorParam("key", "目标文件类型", "将文件拷贝到目标模型:" + modekeys, new NoneFileMode().getKey()));
        return paras;
    }

    @Override
    public void valideParams(List<ConvertorParam> params) {
        FileModel fileModel = FileModelUtils.getModel(params.get(0).getValue());
        if (fileModel == null) {
            throw new RuntimeException("文件模型key不存在：" + params.get(0).getValue());
        }
    }

    @Override
    public String getTitle() {
        return "拷贝文件到目标模型";
    }

    @Override
    public String getNote() {
        return "拷贝文件到目标模型";
    }

    @Override
    public ConvertResult runConvert(ResourceFile resourcefile, FileModel filemodel, WdapConvertor convertor, Map<String, ConvertorParam> params, Map<String, Object> flowContext, ResourceFileServiceInter resourceFileServiceImpl) {
        FarmProcessUtils.setProcess(resourcefile.getId(), FarmProcessTypeEnum.CONVERT_EXFILE, getTitle() + "...");
        File file = filemodel.getFile(resourcefile, resourceFileServiceImpl);
        //----------------------------------------------------
        FileModel fileModel = FileModelUtils.getModel(params.get("key").getValue());
        File tofile = fileModel.getFile(resourcefile, resourceFileServiceImpl);
        Farm2FileUtils.copyTo(file, tofile, new Farm2ProcessHandle() {
            @Override
            public void handle(int process) {
                FarmProcessUtils.setProcess(resourcefile.getId(), FarmProcessTypeEnum.CONVERT_EXFILE, getTitle() + process + "%...");
            }
        });
        //-----------------------------------------------------
        ConvertResult result = new ConvertResult(true);
        result.addFileModel(fileModel.getKey());
        return result;
    }

    @Override
    public List<FileModel> getSourceFileModel() {
        return List.of(new ResourceFileMode());
    }

    @Override
    public FileModel getTargetFileModel() {
        return new NoneFileMode();
    }
}
