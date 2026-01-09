package org.farm2.tools.db;


import org.apache.commons.lang3.StringUtils;
import org.farm2.tools.base.FarmMd5Utils;
import org.farm2.tools.db.commons.*;
import org.farm2.tools.db.enums.FarmDbLikeModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

/**
 * 格式化查詢組件
 */
public class DataQuery {
    private String selectSql;
    private String fromSql;

    /**
     * 当前有countSql时则按照该字段合计数量
     */
    private String countSql;
    private boolean isDistinct = false;
    private String distinctCountId;//distinct时count语句中去重的ID
    private List<QueryRule> rules = new ArrayList<>();
    private List<DBSort> sorts = new ArrayList<>();
    private DBSort defaultSort;
    private int page = 1;
    private int pageSize = 10;
    private boolean isCount = true;

    /**
     * 设置sql语句
     *
     * @param select
     * @param from
     * @return
     */
    public DataQuery setSql(String select, String from) {
        this.selectSql = select;
        this.fromSql = from;
        return this;
    }

    public static DataQuery getInstance() {
        DataQuery query = new DataQuery();
        return query;
    }

    public static DataQuery getInstance(DataQueryDto queryDto) {
        DataQuery query = new DataQuery();
        query.setPage(queryDto.getPage());
        query.setPageSize(queryDto.getPageSize());
        List<QueryRule> rules = new ArrayList<>();
        for (QueryRule rule : queryDto.getRules()) {
            rules.add(rule);
        }
        query.setRules(rules);
        query.setSorts(queryDto.getSorts());
        return query;
    }

    public DataQuery addRules(List<QueryRule> sorts) {
        this.rules.addAll(sorts);
        return this;
    }

    /**
     * 获得可执行的sql
     *
     * @return
     */
    public String[] getRunSql() {
        StringBuffer dataSql = new StringBuffer();
        StringBuffer countSql = new StringBuffer(this.countSql == null ? "" : this.countSql);
        StringBuffer whereSql = new StringBuffer(" WHERE 1=1");
        StringBuffer sortSql = new StringBuffer();
        int n = 0;
        for (QueryRule rule : rules) {
            whereSql.append(rule.getSafeSql("#{query.rules[" + n + "].safeValues.?}"));
            n++;
        }
        sortSql.append(DBSort.getSql(sorts));
        //没有排序添加默认排序
        if (sorts.size() == 0 && defaultSort != null) {
            List<DBSort> ds = new ArrayList<>();
            ds.add(defaultSort);
            sortSql.append(DBSort.getSql(ds));
        }
        dataSql.append("SELECT ").append(isDistinct ? "DISTINCT " : "")
                .append((selectSql == null ? " * " : selectSql).toUpperCase())
                //--------------
                .append(" FROM " + fromSql.toUpperCase())
                .append(whereSql.toString()).append(sortSql.toString())
                .append(getPageSql());

        if (countSql.length() <= 0) {
            //没有自定义排序，则自动构造
            countSql = new StringBuffer().append("SELECT COUNT(" + (isDistinct && distinctCountId != null ? "DISTINCT " + distinctCountId : "*") + ") AS NUM")
                    //--------------
                    .append(" FROM " + fromSql.toUpperCase())
                    .append(whereSql.toString());
        }
        return new String[]{dataSql.toString(), countSql.toString()};
    }

    /**
     * 获得分页sql
     *
     * @return
     */
    private String getPageSql() {
        if (this.pageSize < 0) {
            return "";
        }
        int page = this.page;
        int size = this.pageSize;
        int limit = size;
        int offset_start = (page - 1) * size;
        return " LIMIT " + limit + " OFFSET " + offset_start;
    }

    /**
     * 自定义合计sql
     *
     * @param sql
     * @return
     */
    public DataQuery setCountSql(String sql) {
        this.countSql = sql;
        return this;
    }

    public DataQuery addRule(QueryRule rule) {
        if (rule != null) {
            this.rules.add(rule);
        }
        return this;
    }

    public DataQuery addRule(QueryRule rule, boolean exist) {
        if (rule != null && exist) {
            this.rules.add(rule);
        }
        return this;
    }

    public DataQuery addSort(DBSort sort) {
        this.sorts.add(sort);
        return this;
    }
//----------------------------------------------------------------------

    public DataQuery setDistinct(boolean distinct) {
        isDistinct = distinct;
        return this;
    }

    /**
     * @param distinct
     * @param iDField  过滤重复时 合计数量以哪个ID进行过滤重复
     * @return
     */
    public DataQuery setDistinct(boolean distinct, String iDField) {
        isDistinct = distinct;
        distinctCountId = iDField;
        return this;
    }

    public void setRules(List<QueryRule> rules) {
        this.rules = rules;
    }

    public void setSorts(List<DBSort> sorts) {
        this.sorts = sorts;
    }

    public DataQuery setPage(int page) {
        if (page < 1) {
            page = 1;
        }
        this.page = page;
        return this;
    }

    public DataQuery setPageSize(int pageSize) {
        if (pageSize < 1) {
            pageSize = 1;
        }
        this.pageSize = pageSize;
        return this;
    }

    public DataQuery setPageSizeAll() {
        this.pageSize = -1;
        return this;
    }

    public DataQuery setCount(boolean count) {
        isCount = count;
        return this;
    }

    public String getSelectSql() {
        return selectSql;
    }

    public String getFromSql() {
        return fromSql;
    }

    public String getCountSql() {
        return countSql;
    }

    public boolean isDistinct() {
        return isDistinct;
    }

    public List<QueryRule> getRules() {
        return rules;
    }

    public List<DBSort> getSorts() {
        return sorts;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public boolean isCount() {
        return isCount;
    }

    /**
     * @param key       要替换的条件名称
     * @param sql
     * @param likeModel 条件值中的通配符类型
     */
    public void setRuleAsSql(String key, String sql, FarmDbLikeModel likeModel) {
        DBRule thisRule = null;
        Iterator<QueryRule> iterator = rules.iterator();
        while (iterator.hasNext()) {
            QueryRule element = iterator.next();
            if (element instanceof DBRule) {
                if (((DBRule) element).getKey().toUpperCase().equals(key.toUpperCase())) {
                    thisRule = (DBRule) element;
                    iterator.remove(); // 安全地删除当前元素
                }
            }
        }
        if (thisRule != null && thisRule.getValue() != null && StringUtils.isNotBlank(thisRule.getValue().toString())) {
            Long valNum = sql.chars().filter(c -> c == '?').count();
            if (valNum > 0) {
                String[] arr = new String[valNum.intValue()];
                // 用 fillValue 填充整个数组
                Object value = thisRule.getValue();
                String valueStr = value.toString();
                Arrays.fill(arr, FarmSqls.getValueLike(valueStr, likeModel));
                rules.add(new SqlRule(sql, arr));
            } else {
                rules.add(new SqlRule(sql));
            }
        }
    }

    public void addDefaultSort(DBSort sort) {
        defaultSort = sort;
    }

    /**
     * 获得查询条件的值
     *
     * @param key
     * @return
     */
    public Object getRuleValue(String key) {
        for (QueryRule rule : rules) {
            if ((rule instanceof DBRule) && ((DBRule) rule).getKey().equals(key)) {
                return ((DBRule) rule).getValue();
            }
        }
        return null;
    }

    public boolean hasRules() {
        boolean isHasRules = false;
        return getRules().stream().filter(new Predicate<QueryRule>() {
            @Override
            public boolean test(QueryRule queryRule) {
                return StringUtils.isNotBlank(queryRule.getSafeSql("FLAG"));
            }
        }).toList().size() > 0;
    }

    public void removeRule(String key) {
        Iterator<QueryRule> iterator = rules.iterator();
        while (iterator.hasNext()) {
            QueryRule element = iterator.next();
            if (element instanceof DBRule) {
                if (((DBRule) element).getKey().toUpperCase().equals(key.toUpperCase())) {
                    iterator.remove(); // 安全地删除当前元素
                }
            }
        }
    }

    /**
     * 创建一个用于判断条件是否变化的缓存key
     *
     * @return
     */
    public String getCacheKey() {
        StringBuilder sb = new StringBuilder();
        // 安全拼接 rules
        if (rules != null) {
            for (QueryRule rule : rules) {
                sb.append(rule != null ? rule.toString() : "NULL_RULE");
            }
        }
        // 安全拼接 sorts
        if (sorts != null) {
            for (DBSort sort : sorts) {
                sb.append(sort != null ? sort.toString() : "NULL_SORT");
            }
        }
        // 安全处理 defaultSort
        sb.append(defaultSort != null ? defaultSort.toString() : "NO_DEFAULT_SORT");
        // 分页参数
        sb.append("PAGE:").append(page);
        sb.append("PAGESIZE:").append(pageSize);
        return FarmMd5Utils.generateMD5(sb.toString());
    }
}
