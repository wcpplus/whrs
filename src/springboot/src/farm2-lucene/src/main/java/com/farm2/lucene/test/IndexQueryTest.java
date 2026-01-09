package com.farm2.lucene.test;


import com.farm2.lucene.document.FarmDocument;
import com.farm2.lucene.impl.Farm2LuceneIndexImpl;
import com.farm2.lucene.impl.Farm2LuceneSearchImpl;
import com.farm2.lucene.inter.Farm2LuceneIndexInter;
import com.farm2.lucene.inter.Farm2LuceneSearchInter;
import com.farm2.lucene.query.DocumentQuery;
import com.farm2.lucene.query.DocumentRule;
import com.farm2.lucene.query.DocumentSort;
import com.farm2.lucene.query.rule.DocumentRuleByAccurate;
import com.farm2.lucene.query.rule.DocumentRuleByMultiField;
import com.farm2.lucene.query.rule.DocumentRuleByRange;
import com.farm2.lucene.query.rule.DocumentRuleByTerm;
import com.farm2.lucene.result.DocumentResult;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IndexQueryTest {

    private static String path = "C:\\skcFiles\\index";

    public static void main(String[] args) throws IOException, ParseException {
        Farm2LuceneIndexInter indexService = new Farm2LuceneIndexImpl(path);
        Farm2LuceneSearchInter searchService = new Farm2LuceneSearchImpl(path);
        DocumentQuery query = new DocumentQuery();
        query.addRule(new DocumentRuleByMultiField(List.of("title", "text"), "春菊秋明"));
//        query.addRule(new DocumentRuleByAccurate("tagKeys", List.of("WenZiMUoObS")));
//        query.addRule(new DocumentRuleByAccurate("typeIds", List.of("dac10fc9e8e04cf2bb1ce0d9ca2bd963")));
        query.addRule(new DocumentRule() {
            @Override
            public Query getQuery(Analyzer analyzer) {
                BooleanQuery.Builder builder = new BooleanQuery.Builder();
                Term term = new Term("tagKeys", "ceshiyanzhengcvbwnr");
                builder.add(new TermQuery(term), BooleanClause.Occur.SHOULD);
                // 可选：如果需要至少匹配一个 SHOULD 子句
                builder.setMinimumNumberShouldMatch(1);
                return builder.build();
            }

            @Override
            public QueryType getQueryType() {
                return QueryType.LUCENE;
            }
        });
        query.setPage(1);
        query.setSort(new DocumentSort("id", DocumentSort.SortType.DESC));
        DocumentResult result = searchService.search(query);
        System.out.println(result.toJson());
    }

}
