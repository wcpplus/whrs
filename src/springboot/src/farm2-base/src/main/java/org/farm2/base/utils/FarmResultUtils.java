package org.farm2.base.utils;

import java.util.List;

public class FarmResultUtils {
    public static List<String> getListByPage(List<String> knowids, int page, int pageSize) {
        int start = ((page - 1) * pageSize);
        int end = start + pageSize;
        if (end > knowids.size()) {
            end = knowids.size();
        }
        List<String> currentKnowids = knowids.subList(start, end);
        return currentKnowids;
    }
}
