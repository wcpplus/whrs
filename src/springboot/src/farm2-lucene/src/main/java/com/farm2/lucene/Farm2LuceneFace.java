package com.farm2.lucene;

import com.farm2.lucene.impl.Farm2LuceneIndexImpl;
import com.farm2.lucene.impl.Farm2LuceneSearchImpl;
import com.farm2.lucene.inter.Farm2LuceneIndexInter;
import com.farm2.lucene.inter.Farm2LuceneSearchInter;
import com.farm2.lucene.utils.FarmLuceneUtils;

public class Farm2LuceneFace {

    public static Farm2LuceneIndexInter getIndexService() {
        return new Farm2LuceneIndexImpl(FarmLuceneUtils.getLuceneIndexFilePath());
    }

    public static Farm2LuceneIndexInter getIndexService(String path) {
        return new Farm2LuceneIndexImpl(path);
    }


    public static Farm2LuceneSearchInter getSearchService() {
        return new Farm2LuceneSearchImpl(FarmLuceneUtils.getLuceneIndexFilePath());
    }


    public static Farm2LuceneSearchInter getSearchService(String path) {
        return new Farm2LuceneSearchImpl(path);
    }
}
