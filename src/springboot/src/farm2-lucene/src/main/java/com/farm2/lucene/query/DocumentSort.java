package com.farm2.lucene.query;

import lombok.Data;
import org.farm2.tools.db.commons.DBSort;

import java.util.List;
import java.util.Map;

@Data
public class DocumentSort {

    private String field;
    private SortType type;

    public DocumentSort(List<DBSort> sorts, Map<String, String> dic) {
        if (sorts.size() > 0) {
            field = dic.get(sorts.get(0).getField());
            if (sorts.get(0).getSort_type().equals(DBSort.SORT_TYPE.DESC)) {
                type = SortType.DESC;
            } else {
                type = SortType.ASC;
            }
        }
    }

    public enum SortType {
        ASC, DESC
    }

    public DocumentSort(String field, SortType type) {
        this.field = field;
        this.type = type;
    }
}
