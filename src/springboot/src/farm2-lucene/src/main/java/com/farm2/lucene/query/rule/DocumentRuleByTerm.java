package com.farm2.lucene.query.rule;

import com.farm2.lucene.query.DocumentRule;
import com.farm2.lucene.utils.FarmLuceneUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;


/**
 * 完全匹配到字段上，需要字段是StringField而非TextField
 */
public class DocumentRuleByTerm implements DocumentRule {
    private String field;
    private String queryStr;

    /**完全匹配到字段上，需要字段是StringField而非TextField
     * @param field
     * @param queryStr
     */
    public DocumentRuleByTerm(String field, String queryStr) {
        this.field = field;
        this.queryStr = FarmLuceneUtils.escapeSpecialCharacters(queryStr);
    }

    @Override
    public Query getQuery(Analyzer defaultAnalyzer) {
        Term term = new Term(field, queryStr);
        Query query = new TermQuery(term);
        return query;
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.LUCENE;
    }
}