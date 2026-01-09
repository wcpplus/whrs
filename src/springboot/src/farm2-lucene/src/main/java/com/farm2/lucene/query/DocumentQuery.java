package com.farm2.lucene.query;

import com.farm2.lucene.inter.Farm2LuceneFilterInter;
import com.farm2.lucene.query.rule.DocumentRuleByVector;
import com.farm2.lucene.result.DocumentResult;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DocumentQuery {
    private int pageSize = 20;//查询总页数
    private int page = 1;//当前查询页
    private int allSize = 1000;//从索引中取回数据总数
    private boolean isHighlight = true;//是否高亮
    private List<String> highlightWords;
    private int highlightLen = 256;//高亮内容长度
    private List<DocumentRule> rules = new ArrayList<>();
    private DocumentSort sort;
    private String tagField = null;//如果设置此属性，则按照该字段进行标签统计
    private Farm2LuceneFilterInter filter;
    /**
     * 设置标签统计字段，配置后，结果集中的标签将自动改进型统计存入tags中
     *
     * @param tagField
     */
    public DocumentQuery countTags(String tagField) {
        this.tagField = tagField;
        return this;
    }

    public DocumentQuery setHighlight(boolean isHighLight) {
        return setHighlight(isHighLight, null);
    }

    public DocumentQuery setHighlight(boolean isHighLight, Integer size, List<String> words) {
        highlightWords = words;
        return setHighlight(isHighLight, null);
    }

    public DocumentQuery setHighlight(boolean isHighLight, Integer size) {
        this.isHighlight = isHighLight;
        if (size != null) {
            this.highlightLen = size;
        }
        return this;
    }



    public void setAllSize(int allSize) {
        this.allSize = allSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }


    public void setPage(int page) {
        this.page = page;
    }


    public DocumentQuery addRule(DocumentRule rule) {
        if (rule instanceof DocumentRuleByVector) {
            //向量查询不做高亮
            this.setHighlight(false);
        }
        this.rules.add(rule);
        return this;
    }


    public DocumentQuery setSort(DocumentSort sort) {
        this.sort = sort;
        return this;
    }
}
