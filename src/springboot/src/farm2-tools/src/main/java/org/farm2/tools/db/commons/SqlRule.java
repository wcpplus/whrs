package org.farm2.tools.db.commons;

import java.util.*;

public class SqlRule implements QueryRule {
    private String sql;
    private List<String> paras = new ArrayList<>();
    private Map<String, Object> paraMap = new HashMap<>();

    @Override
    public String toString() {
        return "SqlRule{" +
                "sql=" + Objects.toString(sql) +
                ", paras=" + paras +
                '}';
    }
    /**
     * @param sql   AND (CUSERKEY = '?' OR STATE='2')
     * @param paras 每一个？都需要绑定参数
     */
    public SqlRule(String sql, String... paras) {
        for (String para : paras) {
            this.paras.add(para);
        }
        this.sql = sql;
    }
    public SqlRule(String sql, List<String> paras) {
        for (String para : paras) {
            this.paras.add(para);
        }
        this.sql = sql;
    }

    //----------下面是安全的防止sql注入的实现--------------------------------------------
    @Override
    public Map<String, Object> getSafeValues() {
        int n = 0;
        for (String val : paras) {
            paraMap.put(getKey(n), val);
            n++;
        }
        return paraMap;
    }


    @Override
    public String getSafeSql(String keyFormat) {
        String backSql = this.sql;
        int n = 0;
        for (String flag : paras) {
            //替换？为参数占位符
            backSql = backSql.replaceFirst("\\?", keyFormat.replace("?", getKey(n)));
            n++;
        }
        backSql = ((!backSql.isEmpty() && Character.isWhitespace(backSql.charAt(0))) ? "" : " ") + backSql;
        return backSql;
    }

    @Override
    public String getSafeSql(String keyFormat, Boolean isNullPass) {
        return getSafeSql(keyFormat);
    }

    private String getKey(int n) {
        String key = "V" + n;
        return key;
    }

}
