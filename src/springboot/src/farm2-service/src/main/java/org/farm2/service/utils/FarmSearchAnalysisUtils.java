package org.farm2.service.utils;

import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.farm2.service.dto.SearchDto;
import org.farm2.service.dto.SearchLimitDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 检索词解析器
 */
@Component
public class FarmSearchAnalysisUtils {
//    @Autowired
//    private SkcTagServiceInter skcTagServiceImpl;
//
//    /**
//     * 从检索条件中查找检索精确度
//     *
//     * @param searchDto
//     * @return
//     */
//    public static String getAccurate(@Valid SearchDto searchDto) {
//        if (searchDto.getLimits() != null) {
//            for (SearchLimitDto node : searchDto.getLimits()) {
//                if (node.getModel().equals("ACCURATE") && node.getVal() != null && !node.getVal().isEmpty()) {
//                    return node.getVal().get(0);
//                }
//            }
//        }
//        return "0";
//    }
//
//    /**
//     * 按检索精确度处理检索词
//     *
//     * @param searchDto
//     * @return
//     */
//    public static String initByAccurate(@Valid SearchDto searchDto) {
//        String searchWord = searchDto.getWord();
//        String accurate = FarmSearchAnalysisUtils.getAccurate(searchDto);
//        if ("0".equals(accurate.trim())) {
//            //自动处理
//            // 当 accurate 模式为 "0"（模糊匹配）时，对 searchWord 做预处理：
//            // 条件：
//            //   1. searchWord 中包含超过 3 个连续空格（或总空格数 > 3，根据实际需求）
//            //   2. searchWord 中不包含关键词 "TAG-" 或 "TYPE-"
//            // 满足以上条件时，将所有空格替换为逗号，以便后续按多关键词解析
//            if (searchWord != null
//                    && searchWord.length() - searchWord.replace(" ", "").length() > 3  // 空格数量 > 3
//                    && !searchWord.contains(" TAG-")
//                    && !searchWord.contains(" TYPE-")) {
//
//                // 将所有空格替换为逗号（注意：使用 replaceAll 或 replace(char, char)）
//                searchWord = searchWord.replace(' ', ','); // 推荐用 char 版本，高效且替换全部
//            }
//        }
//        if (accurate.trim().equals("1")) {
//            //宽松
//            searchWord = searchWord.replace(" ", ",");
//        }
//        if (accurate.trim().equals("2")) {
//            //精确，后面initQueryLimit(query, searchDto);会处理
//        }
//        return searchWord;
//    }
//
//    /**
//     * 从检索词中解析出标签key，可能是key或标签名称
//     *
//     * @param word
//     * @return
//     */
//    public String getTagKey(String word) {
//        //  TAG-前端开发-XXX-
//        if (StringUtils.isBlank(word)) {
//            return "";
//        }
//        if (word.indexOf("TAG-") != 0) {
//            return "";
//        }
//        String[] words = word.split("-");
//        String tag = null;
//        if (words.length >= 2) {
//            tag = words[1];
//        }
////        if (StringUtils.isNotBlank(tag)) {
////            //查询是否有系统tag
////            SkcTag sysTag = skcTagServiceImpl.getTagsByName(tag);
////            if (sysTag != null) {
////                tag = sysTag.getTagkey();
////            }
////        }
//        return tag;
//    }
//
//    /**
//     * 从检索词中解析出标签key，可能是key或标签名称
//     *
//     * @param word
//     * @return
//     */
//    public String getTypeKey(String word) {
//        //  TAG-前端开发-XXX-
//        if (StringUtils.isBlank(word)) {
//            return "";
//        }
//        if (word.indexOf("TYPE-") != 0) {
//            return "";
//        }
//        String[] words = word.split("-");
//        String tag = null;
//        if (words.length >= 2) {
//            tag = words[1];
//        }
////        if (StringUtils.isNotBlank(tag)) {
////            //查询是否有系统tag
////            SkcTag sysTag = skcTagServiceImpl.getTagsByName(tag);
////            if (sysTag != null) {
////                tag = sysTag.getTagkey();
////            }
////        }
//        return tag;
//    }
}
