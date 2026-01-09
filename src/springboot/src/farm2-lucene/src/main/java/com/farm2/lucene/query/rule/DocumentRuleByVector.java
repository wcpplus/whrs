package com.farm2.lucene.query.rule;

import com.farm2.lucene.query.DocumentRule;
import com.farm2.lucene.utils.FarmLuceneUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.KnnFloatVectorQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

/**
 * 向量查询
 */
public class DocumentRuleByVector implements DocumentRule {
    private String field;
    private float[] vector;
    private int size;

    /**
     * 向量相似度的检索
     *
     * @param field
     * @param vector
     * @param size
     */
    public DocumentRuleByVector(String field, float[] vector, int size) {
        this.field = field;
        this.vector = vector;
        this.size = size;
    }

    @Override
    public Query getQuery(Analyzer defaultAnalyzer) {
        KnnFloatVectorQuery knnQuery = new KnnFloatVectorQuery(field, vector, size);
        return knnQuery;
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.LUCENE;
    }
}
