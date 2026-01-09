package org.farm2.wdap.convertor.impl;

import org.farm2.base.process.FarmProcessTypeEnum;
import org.farm2.base.process.FarmProcessUtils;
import org.farm2.files.domain.ResourceFile;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.tools.files.Farm2FileUtils;
import org.farm2.tools.web.Farm2HtmlUtils;
import org.farm2.wdap.convertor.filemode.impl.*;
import org.farm2.wdap.convertor.filemode.inter.FileModel;
import org.farm2.wdap.convertor.inter.FileConvertorInter;
import org.farm2.wdap.convertor.utils.ConvertResult;
import org.farm2.wdap.convertor.utils.ConvertUtils;
import org.farm2.wdap.convertor.utils.ConvertorParam;
import org.farm2.wdap.domain.WdapConvertor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HtmlToTextConvertor implements FileConvertorInter {
    static {
        //注册转换器实现类
        ConvertUtils.registImpl((new HtmlToTextConvertor()).getClass());
    }

    @Override
    public List<ConvertorParam> getParams() {
        return List.of();
    }

    @Override
    public void valideParams(List<ConvertorParam> params) {

    }

    @Override
    public String getTitle() {
        return "Html转文本";
    }

    @Override
    public String getNote() {
        return "Html转文本,删除html中所有的标签和样式js等，只返回可见的文本内容";
    }

    @Override
    public ConvertResult runConvert(ResourceFile resourcefile, FileModel filemodel, WdapConvertor convertor, Map<String, ConvertorParam> params, Map<String, Object> flowContext, ResourceFileServiceInter resourceFileServiceImpl) {
        File file = filemodel.getFile(resourcefile, resourceFileServiceImpl);
        try {
            FarmProcessUtils.setProcess(resourcefile.getId(), FarmProcessTypeEnum.CONVERT_EXFILE, getTitle() + "...");
            Document document = Jsoup.parse(file, null);
            String text = document.html();
            text = Farm2HtmlUtils.getTextByHtml(text);
            TextFileMode textFileMode = new TextFileMode();
            File textDir = textFileMode.getFile(resourcefile, resourceFileServiceImpl);
            Farm2FileUtils.writeToFile(text, textDir);
            ConvertResult result = new ConvertResult(true);
            result.addFileModel(new TextFileMode().getKey());
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<FileModel> getSourceFileModel() {
        List<FileModel> list = new ArrayList<>();
        list.add(new ResourceFileMode());
        list.add(new HtmlFileMode());
        return list;
    }

    @Override
    public FileModel getTargetFileModel() {
        return new TextFileMode();
    }
}
