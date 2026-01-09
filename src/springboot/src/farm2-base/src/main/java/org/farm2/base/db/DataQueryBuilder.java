package org.farm2.base.db;

import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;

import java.util.List;
import java.util.Map;

/**
 * 格式化查询构造器
 */
public class DataQueryBuilder {

    public static String getDataSql(DataQuery query) {
        String sql = query.getRunSql()[0];
        return sql;
    }


    public static String getCountSql(DataQuery query) {
        String sql=query.getRunSql()[1];
        return sql;
    }


    public static DataResult getResult(List<Map<String, Object>> list, int num, DataQuery query) {
        return new DataResult(list, num, query);
    }
}
