package com.farm2.lucene.utils;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.farm2.auth.face.FarmParameter;
import org.farm2.auth.service.ParameterServiceInter;
import org.farm2.base.parameter.FarmParameterInter;
import org.farm2.base.web.Farm2BeanFactory;

import java.io.IOException;
import java.nio.file.Paths;

public class FarmLuceneUtils {
    // Lucene查询中的特殊字符集合
    private static final String SPECIAL_CHARACTERS = "+-&|!(){}[]^\"~*?:\\/";

    /**
     * 获得文本索引目录
     *
     * @return
     */
    public static String getLuceneIndexFilePath() {
        FarmParameterInter paramService = (FarmParameterInter) Farm2BeanFactory.getBean(FarmParameter.class);
        return paramService.getStringParameter("farm2.config.lucene.index.dir");
    }

    /**
     * 获得标签索引目录
     *
     * @return
     */
    public static String getTagIndexFilePath() {
        FarmParameterInter paramService = (FarmParameterInter) Farm2BeanFactory.getBean(FarmParameter.class);
        return paramService.getStringParameter("farm2.config.lucene.tag.dir");
    }

    /**
     * 获得答题题库索引目录
     *
     * @return
     */
    public static String getWtsRoomSubjectIndexFilePath() {
        FarmParameterInter paramService = (FarmParameterInter) Farm2BeanFactory.getBean(FarmParameter.class);
        return paramService.getStringParameter("farm2.config.lucene.wts.room.subject.dir");
    }

    /**
     * 获得向量索引目录
     *
     * @return
     */
    public static String getLuceneEmbeddingFilePath() {
        FarmParameterInter paramService = (FarmParameterInter) Farm2BeanFactory.getBean(FarmParameter.class);
        return paramService.getStringParameter("farm2.config.lucene.embedding.dir");
    }

    /**
     * 转义Lucene查询中的特殊字符
     *
     * @param input 要检查的字符
     * @return 转义Lucene查询中的特殊字符
     */
    public static String escapeSpecialCharacters(String input) {
        if (input == null) {
            return null;
        }
        StringBuilder escaped = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (isSpecialCharacter(c)) {
                escaped.append('\\');
            }
            escaped.append(c);
        }
        return escaped.toString();
    }

    /**
     * 检查字符是否是Lucene查询中的特殊字符
     *
     * @param c 要检查的字符
     * @return 如果是特殊字符，则返回true；否则返回false
     */
    private static boolean isSpecialCharacter(char c) {
        return SPECIAL_CHARACTERS.indexOf(c) != -1;
    }

    /**
     * 获得索引大小
     *
     * @param path
     * @return
     */
    public static long getIndexSize(String path) {
        Directory directory = null;
        IndexReader reader = null;
        if (!Paths.get(path).toFile().exists() || (Paths.get(path).toFile().listFiles().length == 0)) {
            return 0;
        }
        try {
            directory = FSDirectory.open(Paths.get(path));
            reader = DirectoryReader.open(directory);
            int numDocs = reader.numDocs();
            return numDocs;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
