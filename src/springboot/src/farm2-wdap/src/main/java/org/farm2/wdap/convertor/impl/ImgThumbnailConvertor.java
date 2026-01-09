package org.farm2.wdap.convertor.impl;

import org.farm2.base.process.FarmProcessTypeEnum;
import org.farm2.base.process.FarmProcessUtils;
import org.farm2.base.validation.Farm2ValidUtils;
import org.farm2.base.validation.ValidType;
import org.farm2.files.domain.ResourceFile;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.tools.files.Farm2ImgUtils;
import org.farm2.wdap.convertor.filemode.impl.ResourceFileMode;
import org.farm2.wdap.convertor.filemode.impl.ThumbnailFileMode;
import org.farm2.wdap.convertor.inter.FileConvertorInter;
import org.farm2.wdap.convertor.filemode.inter.FileModel;
import org.farm2.wdap.convertor.utils.ConvertResult;
import org.farm2.wdap.convertor.utils.ConvertUtils;
import org.farm2.wdap.convertor.utils.ConvertorParam;
import org.farm2.wdap.domain.WdapConvertor;
import org.farm2.wdap.utils.ThumbnailUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 缩略图转换器
 */
public class ImgThumbnailConvertor implements FileConvertorInter {

    static {
        //注册转换器实现类
        ConvertUtils.registImpl((new ImgThumbnailConvertor()).getClass());
    }

    @Override
    public List<ConvertorParam> getParams() {
        List<ConvertorParam> paras = new ArrayList<>();
        paras.add(new ConvertorParam("max_width", "大图宽度", "大图最大宽度，单位px", "1500"));
        paras.add(new ConvertorParam("middle_width", "中图宽度", "中图最大宽度，单位px", "900"));
        paras.add(new ConvertorParam("min_width", "小图宽度", "小图最大宽度，单位px", "200"));
        return paras;
    }

    @Override
    public void valideParams(List<ConvertorParam> params) {
        for (ConvertorParam param : params) {
            Farm2ValidUtils.valid(param.getValue(), ValidType.pInteger, false, true, param.getTitle());
        }
    }

    @Override
    public String getTitle() {
        return "生成缩略图";
    }

    @Override
    public String getNote() {
        return "支持图片类型，如png,jpg,jpeg,gif";
    }

    @Override
    public ConvertResult runConvert(ResourceFile resourcefile, FileModel filemodel, WdapConvertor convertor, Map<String, ConvertorParam> params, Map<String, Object> context, ResourceFileServiceInter resourceFileServiceImpl) {
        ConvertResult result = new ConvertResult(true);
        FarmProcessUtils.setProcess(resourcefile.getId(), FarmProcessTypeEnum.CONVERT_EXFILE, getTitle() + "...");
        //图片文件
        File imgFile = filemodel.getFile(resourcefile, resourceFileServiceImpl);
        int fileWidth = 0;
        try {
            fileWidth = Farm2ImgUtils.getImageWidth(imgFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ThumbnailFileMode fileMode = new ThumbnailFileMode();
        //大图文件
        File thumbnailMaxFile = fileMode.getFiles(resourcefile, resourceFileServiceImpl).get(2);
        thumbnailMaxFile.getParentFile().mkdirs();
        //中图文件
        File thumbnailMiddleFile = fileMode.getFiles(resourcefile, resourceFileServiceImpl).get(1);
        thumbnailMiddleFile.getParentFile().mkdirs();
        //小图文件
        File thumbnailMinFile = fileMode.getFiles(resourcefile, resourceFileServiceImpl).get(0);
        thumbnailMinFile.getParentFile().mkdirs();
        if (fileWidth > Integer.valueOf(params.get("max_width").getValue())) {
            ThumbnailUtils.creatThumbnail(Integer.valueOf(params.get("max_width").getValue()), imgFile, thumbnailMaxFile);
        }
        if (fileWidth > Integer.valueOf(params.get("middle_width").getValue())) {
            ThumbnailUtils.creatThumbnail(Integer.valueOf(params.get("middle_width").getValue()), imgFile, thumbnailMiddleFile);
        }
        if (fileWidth > Integer.valueOf(params.get("min_width").getValue())) {
            ThumbnailUtils.creatThumbnail(Integer.valueOf(params.get("min_width").getValue()), imgFile, thumbnailMinFile);
        }
        result.addFileModel(new ThumbnailFileMode().getKey());
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
        return new ThumbnailFileMode();
    }


}
