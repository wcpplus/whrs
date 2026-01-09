package com.farm2.lucene.query.rule;

import com.farm2.lucene.query.DocumentRule;
import com.farm2.lucene.utils.FarmLuceneUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;

import java.util.List;

/**
 * 精确匹配(范围匹配)，用于分类id的检索和标签key的检索
 */
public class DocumentRuleByAccurate implements DocumentRule {
    private String field;
    private List<String> vals;

    /**
     * 完全匹配到字段上，需要字段是StringField而非TextField
     *
     * @param field
     * @param vals
     */
    public DocumentRuleByAccurate(String field, List<String> vals) {
        this.field = field;
        this.vals = vals;
    }


    @Override
    public Query getQuery(Analyzer defaultAnalyzer) {
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        for (String val : vals) {
            Term term = new Term(field, val);
            builder.add(new TermQuery(term), BooleanClause.Occur.SHOULD);
        }
        // 可选：如果需要至少匹配一个 SHOULD 子句
        builder.setMinimumNumberShouldMatch(1);
        return builder.build();
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.LUCENE;
    }
}
