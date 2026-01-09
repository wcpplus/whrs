package com.farm2.lucene.impl;

import com.farm2.lucene.document.FarmDocument;
import com.farm2.lucene.inter.Farm2LuceneIndexInter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class Farm2LuceneIndexImpl implements Farm2LuceneIndexInter {
    public static final Object lock = new Object();
    private String indexPath;

    public Farm2LuceneIndexImpl(String indexPath) {
        this.indexPath = indexPath;
    }

    @Override
    public void createIndex(List<FarmDocument> farmDocuments) {
        List<Document> documents = farmDocuments.stream().map(node -> node.toDocument()).toList();
        synchronized (lock) {
            try (FSDirectory directory = FSDirectory.open(Paths.get(indexPath));
                 IndexWriter writer = createIndexWriter(directory)) {

                int batchSize = 100;  // 每批处理100个文档
                for (int i = 0; i < documents.size(); i += batchSize) {
                    int endIndex = Math.min(i + batchSize, documents.size());
                    List<Document> batch = documents.subList(i, endIndex);
                    writer.addDocuments(batch);
                    if ((i / batchSize) % 10 == 0) {  // 每10批次提交一次
                        writer.commit();
                    }
                }
                writer.commit();
            } catch (Exception e) {
                throw new RuntimeException("Failed to create index: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void deleteIndex(List<String> ids) {
        deleteIndex(ids, "id");
    }

    @Override
    public void mergeIndex() {
        synchronized (lock) {
            try (FSDirectory directory = FSDirectory.open(Paths.get(indexPath));
                 IndexWriter writer = createIndexWriter(directory)) {

                writer.forceMerge(1);  // 合并为一个段
                writer.commit();
            } catch (Exception e) {
                throw new RuntimeException("Failed to merge index segments: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void deleteIndex(List<String> ids, String field) {
        synchronized (lock) {
            try (FSDirectory directory = FSDirectory.open(Paths.get(indexPath)); IndexWriter writer = createIndexWriter(directory)) {
                for (String id : ids) {
                    writer.deleteDocuments(new Term(field, id));
                }
                writer.commit();
            } catch (Exception e) {
                throw new RuntimeException("Failed to delete index: " + e.getMessage(), e);
            }
        }
    }

    private IndexWriter createIndexWriter(FSDirectory directory) throws IOException {
        Analyzer analyzer = new IKAnalyzer(false);
        IndexWriterConfig config = new IndexWriterConfig(analyzer)
                .setRAMBufferSizeMB(256.0)  // 设置内存缓冲区大小
                .setMergePolicy(new LogByteSizeMergePolicy());  // 设置合并策略
        return new IndexWriter(directory, config);
    }
}