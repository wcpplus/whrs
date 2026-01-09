package org.farm2.tools.db.commons;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询条件的封装类
 *
 * @author 王东
 * @date 2015-01-28
 */
public class DBRuleList {
    @SuppressWarnings("unused")
    private static final long serialVersionUID = 1L;
    private List<QueryRule> list = new ArrayList<QueryRule>();

    private DBRuleList() {
    }

    public static DBRuleList getInstance() {
        return new DBRuleList();
    }

    public DBRuleList add(QueryRule rule) {
        list.add(rule);
        return this;
    }

    public DBRuleList add(QueryRule rule, boolean isAdd) {
        if (isAdd) {
            list.add(rule);
        }
        return this;
    }

    public DBRuleList add(SqlRule rule) {
        list.add(rule);
        return this;
    }

    public List<QueryRule> toList() {
        return list;
    }
}
