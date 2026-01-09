package com.farm2.lucene.query.rule;

import com.farm2.lucene.query.DocumentRule;
import com.farm2.lucene.utils.FarmLuceneUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermRangeQuery;

/**
 * 按大小比较范围的查询条件
 */
public class DocumentRuleByRange implements DocumentRule {
    private String field;
    private String date14StrStart = null;
    private String date14StrEnd = null;

    /**
     * 完全匹配到字段上，需要字段是StringField而非TextField
     *
     * @param field
     * @param date14StrStart
     */
    public DocumentRuleByRange(String field, String date14StrStart) {
        this.field = field;
        this.date14StrStart = FarmLuceneUtils.escapeSpecialCharacters(date14StrStart);
    }

    public DocumentRuleByRange(String field, String date14StrStart, String date14StrEnd) {
        this.field = field;
        this.date14StrStart = FarmLuceneUtils.escapeSpecialCharacters(date14StrStart);
        this.date14StrEnd = FarmLuceneUtils.escapeSpecialCharacters(date14StrEnd);
    }

    @Override
    public Query getQuery(Analyzer defaultAnalyzer) {
        Query query = TermRangeQuery.newStringRange(
                field,
                date14StrStart,
                date14StrEnd,
                true,  // 不包括下界
                true    // 包括上界（无意义）
        );
        return query;
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.LUCENE;
    }
}
