package org.farm2.wdap.convertor.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.farm2.base.process.FarmProcessTypeEnum;
import org.farm2.base.process.FarmProcessUtils;
import org.farm2.base.validation.Farm2ValidUtils;
import org.farm2.base.validation.ValidType;
import org.farm2.files.domain.ResourceFile;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.tools.files.Farm2FileUtils;
import org.farm2.tools.web.domain.TextParagraph;
import org.farm2.wdap.convertor.filemode.impl.SegmentFileMode;
import org.farm2.wdap.convertor.filemode.impl.TextFileMode;
import org.farm2.wdap.convertor.filemode.inter.FileModel;
import org.farm2.wdap.convertor.inter.FileConvertorInter;
import org.farm2.wdap.convertor.utils.ConvertResult;
import org.farm2.wdap.convertor.utils.ConvertUtils;
import org.farm2.wdap.convertor.utils.ConvertorParam;
import org.farm2.wdap.domain.WdapConvertor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TextSegmentConvertor implements FileConvertorInter {
    static {
        //注册转换器实现类
        ConvertUtils.registImpl((new TextSegmentConvertor()).getClass());
    }

    @Override
    public List<ConvertorParam> getParams() {
        return List.of(new ConvertorParam("size", "段落长度", "建议200左右", "200"));
    }

    @Override
    public void valideParams(List<ConvertorParam> params) {
        Farm2ValidUtils.valid(params.get(0).getValue(), ValidType.pInteger, false, true, params.get(0).getTitle());
    }

    @Override
    public String getTitle() {
        return "文本分段";
    }

    @Override
    public String getNote() {
        return "方便做分段索引";
    }

    @Override
    public ConvertResult runConvert(ResourceFile resourcefile, FileModel filemodel, WdapConvertor convertor, Map<String, ConvertorParam> params, Map<String, Object> flowContext, ResourceFileServiceInter resourceFileServiceImpl) {
        File file = filemodel.getFile(resourcefile, resourceFileServiceImpl);
        FarmProcessUtils.setProcess(resourcefile.getId(), FarmProcessTypeEnum.CONVERT_EXFILE, getTitle() + "...");
        SegmentFileMode segmentFileMode = new SegmentFileMode();
        File jsonfile = segmentFileMode.getFile(resourcefile, resourceFileServiceImpl);
        try {
            String text = Farm2FileUtils.readText(file);
            List<TextParagraph> paragraphs = convertToParagraphs(text, Integer.valueOf(params.get("size").getValue()));
            String jsonString = toJson(paragraphs);
            Farm2FileUtils.writeToFile(jsonString, jsonfile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ConvertResult result = new ConvertResult(true);
        result.addFileModel(new SegmentFileMode().getKey());
        return result;
    }

    @Override
    public List<FileModel> getSourceFileModel() {
        return List.of(new TextFileMode());
    }

    @Override
    public FileModel getTargetFileModel() {
        return new SegmentFileMode();
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


    /**
     * 按句子分隔文本。
     *
     * @param text 输入文本
     * @return 句子列表
     */
    private static List<String> splitSentences(String text) {
        List<String> sentences = new ArrayList<>();
        String[] rawSentences = text.split("[.!?。！？\\n]\\s*");
        for (String sentence : rawSentences) {
            if (!sentence.trim().isEmpty()) {
                sentences.add(sentence.trim());
            }
        }
        return sentences;
    }

    /**
     * 将文本按句子分隔并组合成段落，每个段落字符数约为 300。
     *
     * @param text 输入文本
     * @return 段落列表
     */
    private static List<TextParagraph> convertToParagraphs(String text, int size) {
        List<String> sentences = splitSentences(text); // 按句子分隔
        List<TextParagraph> paragraphs = new ArrayList<>();
        StringBuilder currentParagraph = new StringBuilder();
        int sort = 1;

        for (String sentence : sentences) {
            // 如果当前段落加上新句子的长度超过 300，则保存当前段落并开始新的段落
            if (currentParagraph.length() + sentence.length() > size && currentParagraph.length() > 0) {
                paragraphs.add(new TextParagraph(sort++, sentences.size(), currentParagraph.toString().trim()));
                currentParagraph.setLength(0); // 清空当前段落
            }
            currentParagraph.append(sentence).append(" "); // 添加句子到当前段落
        }

        // 添加最后一个段落（如果存在）
        if (currentParagraph.length() > 0) {
            paragraphs.add(new TextParagraph(sort, sentences.size(), currentParagraph.toString().trim()));
        }
        //设置总段落数
        for (TextParagraph node : paragraphs) {
            node.setSize(sort);
        }
        return paragraphs;
    }


}
