package com.farm2.lucene.document;

import org.apache.lucene.document.*;
import org.apache.lucene.index.VectorSimilarityFunction;

/**
 * 封装索引数据
 */
public class FarmDocument {
    private Document doc = new Document();

    public static FarmDocument getInstance(String id) {
        FarmDocument fdoc = new FarmDocument();
        fdoc.doc.add(new StringField("id", id, Field.Store.YES));
        return fdoc;
    }


    public FarmDocument addString(String key, String value) {
        if (value != null) {
            doc.add(new StringField(key, value, Field.Store.YES));
        }
        return this;
    }

    public FarmDocument addText(String key, String value) {
        if (value != null) {
            doc.add(new TextField(key, value, Field.Store.YES));
        }
        return this;
    }

    public FarmDocument addVector(String key, float[] embedding) {
        if (embedding != null) {
            doc.add(new KnnFloatVectorField(key, embedding, VectorSimilarityFunction.EUCLIDEAN));
        }
        return this;
    }

    public Document toDocument() {
        return doc;
    }


}
