package com.farm2.lucene.query.rule;

import com.farm2.lucene.query.DocumentRule;
import com.farm2.lucene.utils.FarmLuceneUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;

import java.util.List;

/**
 * 多字段关键字查询（分词后查询）
 */
public class DocumentRuleByMultiField implements DocumentRule {

    private List<String> fields;
    private String queryStr;

    public DocumentRuleByMultiField(List<String> fields, String queryStr) {
        this.fields = fields;
        this.queryStr = FarmLuceneUtils.escapeSpecialCharacters(queryStr);
    }

    @Override
    public Query getQuery(Analyzer analyzer) {
        // 创建 MultiFieldQueryParser
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields.toArray(new String[fields.size()]), analyzer);
        try {
            return parser.parse(queryStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.LUCENE;
    }
}
