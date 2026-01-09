package com.farm2.lucene.query;

import lombok.Data;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Query;

public interface DocumentRule {

    public enum QueryType {
        //在lucene查询阶段解析
        LUCENE,
        //在lucene查询后进行过虑
        LIMIT
    }

    public Query getQuery(Analyzer analyzer);

    public QueryType getQueryType();
}
