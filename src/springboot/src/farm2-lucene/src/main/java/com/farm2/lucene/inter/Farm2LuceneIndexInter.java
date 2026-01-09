package com.farm2.lucene.inter;

import com.farm2.lucene.document.FarmDocument;
import org.apache.lucene.document.Document;

import java.util.List;

/**
 * 索引创建服务
 */
public interface Farm2LuceneIndexInter {

    public void createIndex(List<FarmDocument> documents);

    public void deleteIndex(List<String> ids);

    public void mergeIndex();

    public void deleteIndex(List<String> ids, String field);
}
