package org.farm2.wdap.convertor.impl;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.farm2.base.process.FarmProcessTypeEnum;
import org.farm2.base.process.FarmProcessUtils;
import org.farm2.files.domain.ResourceFile;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.tools.files.Farm2FileUtils;
import org.farm2.wdap.convertor.filemode.impl.PdfFileMode;
import org.farm2.wdap.convertor.filemode.impl.ResourceFileMode;
import org.farm2.wdap.convertor.filemode.impl.TextFileMode;
import org.farm2.wdap.convertor.filemode.inter.FileModel;
import org.farm2.wdap.convertor.inter.FileConvertorInter;
import org.farm2.wdap.convertor.utils.ConvertResult;
import org.farm2.wdap.convertor.utils.ConvertUtils;
import org.farm2.wdap.convertor.utils.ConvertorParam;
import org.farm2.wdap.domain.WdapConvertor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class PdfToTextConvertor implements FileConvertorInter {
    static {
        //注册转换器实现类
        ConvertUtils.registImpl((new PdfToTextConvertor()).getClass());
    }

    @Override
    public List<ConvertorParam> getParams() {
        return List.of();
    }

    @Override
    public String getTitle() {
        return "PDF文件转文本";
    }

    @Override
    public String getNote() {
        return "将pdf文件转换为文本文件";
    }

    @Override
    public ConvertResult runConvert(ResourceFile resourcefile, FileModel filemodel, WdapConvertor convertor, Map<String, ConvertorParam> params, Map<String, Object> flowContext, ResourceFileServiceInter resourceFileServiceImpl) {
        File pdfFile = filemodel.getFile(resourcefile, resourceFileServiceImpl);
        FarmProcessUtils.setProcess(resourcefile.getId(), FarmProcessTypeEnum.CONVERT_EXFILE, getTitle() + "...");
        //----------------------------------------------------
        TextFileMode textFileMode = new TextFileMode();
        File textdir = textFileMode.getDir(resourcefile, resourceFileServiceImpl);
        File textFile = Paths.get(textdir.toURI()).resolve("text.txt").toFile();
        String text = null;
        try (PDDocument document = PDDocument.load(pdfFile)) { // 修正了这里的语法错误
            PDFTextStripper pdfStripper = new PDFTextStripper();
            text = pdfStripper.getText(document); // 直接返回结果
            Farm2FileUtils.writeToFile(text, textFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to extract text from PDF: " + e.getMessage(), e);
        }
        //-----------------------------------------------------
        ConvertResult result = new ConvertResult(true);
        result.addFileModel(new TextFileMode().getKey());
        return result;
    }

    @Override
    public List<FileModel> getSourceFileModel() {
        return List.of(new PdfFileMode(), new ResourceFileMode());
    }

    @Override
    public FileModel getTargetFileModel() {
        return new TextFileMode();
    }

    @Override
    public void valideParams(List<ConvertorParam> params) {

    }
}
