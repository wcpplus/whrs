package com.farm2.lucene.test;

import com.farm2.lucene.document.FarmDocument;
import com.farm2.lucene.impl.Farm2LuceneIndexImpl;
import com.farm2.lucene.impl.Farm2LuceneSearchImpl;
import com.farm2.lucene.inter.Farm2LuceneIndexInter;
import com.farm2.lucene.inter.Farm2LuceneSearchInter;
import com.farm2.lucene.query.DocumentQuery;
import com.farm2.lucene.query.DocumentRule;
import com.farm2.lucene.query.DocumentSort;
import com.farm2.lucene.query.rule.DocumentRuleByMultiField;
import com.farm2.lucene.query.rule.DocumentRuleByTerm;
import com.farm2.lucene.result.DocumentResult;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IndexServiceTest {

    private static String path = "D:\\temp\\farm2files\\index";

    public static void main(String[] args) throws IOException, ParseException {


        Farm2LuceneIndexInter indexService = new Farm2LuceneIndexImpl(path);
        indexService.deleteIndex(List.of("33d620560a6445349eef595dd8ca6ab2"));



//        List<FarmDocument> docs = new ArrayList<>();
//        docs.add(FarmDocument.getInstance("1")
//                .addText("content1", "Lucene是一个开源的全文搜索引擎框架")
//                .addText("content2", "白日依山尽，黄河入海流")
//                .addString("content3", "水中花"));
//        docs.add(FarmDocument.getInstance("2")
//                .addText("content1", "Lucene可以用来构建强大的搜索应用")
//                .addText("content2", "欲穷千里目，更上一层楼")
//                .addString("content3", "镜中月"));
//        indexService.createIndex(docs);
//        indexService.mergeIndex();
//        Farm2LuceneSearchInter searchService = new Farm2LuceneSearchImpl(path);
//        DocumentQuery query = new DocumentQuery();
//        query.addRule(new DocumentRuleByMultiField(List.of("title", "text"), "前端请求后端数据-代码片段"));
//        //query.addRule(new DocumentRuleByTerm("content3", "镜中月"));
//        //query.setPageSize(1);
//        query.setPage(1);
//        query.setSort(new DocumentSort("id", DocumentSort.SortType.DESC));
//        DocumentResult result = searchService.search(query);
//        System.out.println(result.toJson());
    }

}
