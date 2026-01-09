package org.farm2.base.db;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.QueryRule;
import org.farm2.tools.i18n.I18n;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 格式化查询构造器
 */
@Slf4j
public class DataSQLBuilder {

    public static String getInsertSql(Map<String, Object> values, String tableName) {
        String sql = new SQL() {{
            INSERT_INTO(tableName);
            for (Map.Entry<String, Object> entry : values.entrySet()) {
                VALUES(entry.getKey(), "#{values." + entry.getKey() + "}");
            }
        }}.toString();
        return sql;
    }


    public String getDeleteSql(String tableName, List<QueryRule> rules) {
        // 获取主键或条件信息，假设主键是 "id" 并且它总是存在于 values 中
        StringBuffer whereSql = new StringBuffer(" WHERE 1=1");
        if (rules == null || rules.size() <= 0) {
            throw new RuntimeException(I18n.msg("刪除语句必须添加WHERE条件"));
        }
        int n = 0;
        for (QueryRule rule : rules) {
            // #{rules[0].values.name}
            whereSql.append(rule.getSafeSql("#{rules[" + n + "].safeValues.?}", false));
            n++;
        }
        String sql = new StringBuffer().append("DELETE FROM ").append(tableName).append(whereSql).toString();
        if (whereSql.length() <= 10) {
            log.error("不安全的删除操作" + sql);
            throw new RuntimeException("不安全的删除操作");
        }
        return sql;
    }


    public String getDataSqlByRules(String tableName, List<QueryRule> rules) {
        // 获取主键或条件信息，假设主键是 "id" 并且它总是存在于 values 中
        StringBuffer whereSql = new StringBuffer(" WHERE 1=1");
        if (rules == null) {
            rules = new ArrayList<>();
        }
        int n = 0;
        for (QueryRule rule : rules) {
            // #{rules[0].values.name}
            whereSql.append(rule.getSafeSql("#{rules[" + n + "].safeValues.?}"));
            n++;
        }
        String sql = new StringBuffer().append("SELECT * FROM ").append(tableName).append(whereSql).toString();
        return sql;
    }

    public String getUpdateSql(Map<String, Object> values, String tableName, String primaryKey) {
        // 获取主键或条件信息，假设主键是 "id" 并且它总是存在于 values 中
        Map<String,Object> fields=new HashMap<>(values);
        Object id = fields.remove(primaryKey); // 移除主键，因为它不应该出现在 SET 子句中
        if (id == null) {
            id = fields.remove(primaryKey.toUpperCase());
        }
        if (id == null) {
            id = fields.remove(primaryKey.toLowerCase());
        }
        if (id == null) {
            throw new IllegalArgumentException("The 'id' field is required for updating records.");
        }
        String sql = new SQL() {{
            UPDATE(tableName);
            // 遍历 map 中的每一项，构建 SET 子句
            for (Map.Entry<String, Object> entry : fields.entrySet()) {
                // 确保不为空值进行更新
                if (entry.getValue() != null) {
                    SET(entry.getKey() + " = #{values." + entry.getKey() + "}");
                }
            }
            // 构建 WHERE 子句，这里假设使用 'id' 作为唯一标识符
            WHERE("id = #{values."+primaryKey+"}");
        }}.toString();
        return sql;
    }
}
