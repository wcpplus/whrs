package com.farm2.lucene.impl;

import com.farm2.lucene.inter.Farm2LuceneSearchInter;
import com.farm2.lucene.query.DocumentQuery;
import com.farm2.lucene.query.DocumentRule;
import com.farm2.lucene.query.DocumentSort;
import com.farm2.lucene.result.DocumentResult;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.web.FarmResponseCode;
import org.farm2.tools.web.FarmResponseResult;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Farm2LuceneSearchImpl implements Farm2LuceneSearchInter {
    private final Object lock = Farm2LuceneIndexImpl.lock;
    private String indexPath;
    //每次查询从索引中最多读取的结果数量
    private static int RESULT_SIZE_MAX = 1000;

    public Farm2LuceneSearchImpl(String indexPath) {
        this.indexPath = indexPath;
    }


    @Override
    public DocumentResult search(DocumentQuery docQuery) {
        synchronized (lock) {
            try {
                if (docQuery.getPage() == 0) {
                    docQuery.setPage(1);
                }
                try (FSDirectory directory = FSDirectory.open(Paths.get(indexPath)); DirectoryReader reader = DirectoryReader.open(directory);) {
                    IndexSearcher searcher = new IndexSearcher(reader);
                    //构造查询条件
                    BooleanQuery booleanQuery = getBooleanQuery(docQuery);
                    TopDocs topDocs = searcher.search(booleanQuery, docQuery.getAllSize());
                    List<Document> docs = new ArrayList<>();
                    for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                        Document document = searcher.doc(scoreDoc.doc);
                        if (docQuery.getFilter() != null) {
                            if (docQuery.getFilter().handle(document)) {
                                //存在过滤器就过滤权限
                                docs.add(document);
                            }
                        } else {
                            docs.add(document);
                        }
                        if (docs != null && docs.size() > RESULT_SIZE_MAX) {
                            break;
                        }
                    }
                    //排序
                    if (docQuery.getSort() != null) {
                        DocumentSort sort = docQuery.getSort();
                        docs.sort(new Comparator<Document>() {
                            @Override
                            public int compare(Document o1, Document o2) {
                                if (sort.getType().equals(DocumentSort.SortType.ASC)) {
                                    return o1.get(sort.getField()).compareTo(o2.get(sort.getField()));
                                } else {
                                    return o2.get(sort.getField()).compareTo(o1.get(sort.getField()));
                                }
                            }
                        });
                    }
                    DocumentResult result = new DocumentResult(docs, docs.size(), docQuery, searcher, booleanQuery);
                    return result;
                } catch (Exception e) {
                    throw e;
                }
            } catch (Exception e) {
                if (e instanceof FileNotFoundException) {
                    e.printStackTrace();
                    return new DocumentResult();
                }
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * 解析查询条件
     *
     * @param docQuery
     * @return
     * @throws ParseException
     */
    private BooleanQuery getBooleanQuery(DocumentQuery docQuery) throws ParseException {
        // 搜索逻辑...
        Analyzer analyzer = new IKAnalyzer(false);
        // 创建布尔查询对象
        BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();
        for (DocumentRule rule : docQuery.getRules()) {
            if (rule.getQueryType().equals(DocumentRule.QueryType.LUCENE)) {
                booleanQueryBuilder.add(rule.getQuery(analyzer), BooleanClause.Occur.MUST); // 必须满足
            }
        }
        // 构建布尔查询
        BooleanQuery finalQuery = booleanQueryBuilder.build();
        return finalQuery;
    }
}