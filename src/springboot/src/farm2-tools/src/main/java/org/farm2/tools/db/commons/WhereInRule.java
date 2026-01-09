package org.farm2.tools.db.commons;

import java.util.*;

public class WhereInRule implements QueryRule {
    private String field;
    private Set<String> vals;

    @Override
    public String toString() {
        return "WhereInRule{" +
                "field=" + Objects.toString(field) +
                ", vals=" + vals +
                '}';
    }

    public WhereInRule(String field, Set<String> vals) {
        this.field = field;
        if (vals == null || vals.size() <= 0) {
            vals = new HashSet<>();
            vals.add("NONE");
        }
        this.vals = vals;
    }

    //----------下面是安全的防止sql注入的实现--------------------------------------------
    @Override
    public Map<String, Object> getSafeValues() {
        Map<String, Object> paraMap = new HashMap<>();
        int n = 0;
        for (String val : vals) {
            paraMap.put(getKey(n), val);
            n++;
        }
        return paraMap;
    }

    @Override
    public String getSafeSql(String keyFormat) {
        List<String> keys = new ArrayList<>();
        int n = 0;
        for (String key : vals) {
            keys.add(keyFormat.replace("?", getKey(n)));
            n++;
        }
        return " AND " + field + " IN (" + FarmSqls.getWhereInKeys(keys.stream().toList()) + ")";
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
