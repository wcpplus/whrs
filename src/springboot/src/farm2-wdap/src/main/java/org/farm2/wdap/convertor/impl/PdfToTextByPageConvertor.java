package org.farm2.wdap.convertor.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.farm2.base.process.FarmProcessTypeEnum;
import org.farm2.base.process.FarmProcessUtils;
import org.farm2.files.domain.ResourceFile;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.tools.files.Farm2FileUtils;
import org.farm2.tools.web.domain.TextParagraph;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PdfToTextByPageConvertor implements FileConvertorInter {
    static {
        //注册转换器实现类
        ConvertUtils.registImpl((new PdfToTextByPageConvertor()).getClass());
    }

    @Override
    public List<ConvertorParam> getParams() {
        return List.of();
    }

    @Override
    public String getTitle() {
        return "PDF文件转分页JSON";
    }

    @Override
    public String getNote() {
        return "将pdf文件转换为分页JSON文件";
    }

    @Override
    public ConvertResult runConvert(ResourceFile resourcefile, FileModel filemodel, WdapConvertor convertor, Map<String, ConvertorParam> params, Map<String, Object> flowContext, ResourceFileServiceInter resourceFileServiceImpl) {
        File pdfFile = filemodel.getFile(resourcefile, resourceFileServiceImpl);
        FarmProcessUtils.setProcess(resourcefile.getId(), FarmProcessTypeEnum.CONVERT_EXFILE, getTitle() + "...");
        //----------------------------------------------------
        TextFileMode textFileMode = new TextFileMode();
        File textDir = textFileMode.getDir(resourcefile, resourceFileServiceImpl);
        File textFile = Paths.get(textDir.toURI()).resolve("text.txt").toFile();
        List<TextParagraph> paragraphs = new ArrayList<>();
        try (PDDocument document = PDDocument.load(pdfFile)) { // 加载 PDF 文档
            int totalPages = document.getNumberOfPages(); // 获取总页数
            for (int pageNumber = 1; pageNumber <= totalPages; pageNumber++) { // 遍历每一页
                PDFTextStripper pdfStripper = new PDFTextStripper();
                pdfStripper.setStartPage(pageNumber); // 设置起始页
                pdfStripper.setEndPage(pageNumber);   // 设置结束页（与起始页相同，表示只提取一页）
                String pageText = pdfStripper.getText(document); // 提取当前页的文本
                // 将当前页的文本写入单独的文件
                paragraphs.add(new TextParagraph(pageNumber, pageText.length(), pageText));
            }
            String jsonString = toJson(paragraphs);
            Farm2FileUtils.writeToFile(jsonString, textFile);
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


    /**
     * 将段落列表转换为 JSON 字符串。
     *
     * @param paragraphs 段落列表
     * @return JSON 字符串
     * @throws JsonProcessingException 如果 JSON 转换失败
     */
    private static String toJson(List<TextParagraph> paragraphs) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(paragraphs);
    }

}
