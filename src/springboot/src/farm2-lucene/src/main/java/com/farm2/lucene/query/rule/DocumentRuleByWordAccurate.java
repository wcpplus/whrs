package com.farm2.lucene.query.rule;

import com.farm2.lucene.query.DocumentRule;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

import java.util.List;

/**
 * 精确匹配(检索词的精确匹配)
 */
public class DocumentRuleByWordAccurate implements DocumentRule {
    private List<String> fields;
    private String word;

    /**
     * 完全匹配到字段上，需要字段是StringField而非TextField
     *
     * @param fields
     * @param word
     */
    public DocumentRuleByWordAccurate(List<String> fields, String word) {
        if (word.indexOf("\"") >= 0) {
            throw new RuntimeException("检索词中不能包含\"");
        }
        this.fields = fields;
        this.word = word;
    }


    @Override
    public Query getQuery(Analyzer defaultAnalyzer) {
        try {
            BooleanQuery.Builder builder = new BooleanQuery.Builder();
            for (String field : fields) {
                Query query1 = (new QueryParser(field, defaultAnalyzer)).parse("\"" + word.replace("\"", "") + "\"");
                builder.add(query1, BooleanClause.Occur.SHOULD);
            }
            builder.setMinimumNumberShouldMatch(1);
            return builder.build();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.LUCENE;
    }
}
