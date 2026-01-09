package com.farm2.lucene.inter;

import com.farm2.lucene.query.DocumentQuery;
import com.farm2.lucene.result.DocumentResult;
import org.apache.lucene.document.Document;
import org.farm2.tools.db.DataResult;

import java.util.List;

public interface Farm2LuceneSearchInter {

    public DocumentResult search(DocumentQuery query);

}
