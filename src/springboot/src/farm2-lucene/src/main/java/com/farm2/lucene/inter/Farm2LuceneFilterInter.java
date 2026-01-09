package com.farm2.lucene.inter;

import org.apache.lucene.document.Document;

/**
 * 全文检索内容过滤器接口
 */
public interface Farm2LuceneFilterInter {
    public boolean handle(Document doc);
}
