package org.farm2.tools.db.commons;


import java.util.Objects;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DBRule implements QueryRule {
    private String key;// where key
    private Object value;// where value
    private String compara;// like,} % = > < >= <=
    private List<QueryRule> rules = new ArrayList<QueryRule>();


    @Override
    public String toString() {
        return "DBRule{" +
                "key='" + key + '\'' +
                ", value=" + Objects.toString(value) +
                ", compara='" + compara + '\'' +
                '}';
    }

    public static DBRule getInstance(String key, String value, String comString) {
        DBRule cRule = new DBRule(key, value, comString);
        return cRule;
    }


    public DBRule addRule(String key, String value, String comString) {
        DBRule cRule = new DBRule(key, value, comString);
        rules.add(cRule);
        return this;
    }

    public DBRule(String key, Object value, String compara) {
        compara = compara.replaceAll("[;'\"]", "");
        FarmSqls.wipeVirus_key(key);
        FarmSqls.wipeVirus_compara(compara);
        this.key = key.trim().toUpperCase();
        this.value = value;
        this.compara = compara.trim().toUpperCase();
        this.rules.add(this);
    }

    /**
     * 构造值
     *
     * @return
     */
    private String expendVal() {
        StringBuffer valStr = new StringBuffer();
        if (compara.equals("LIKE")) {
            valStr.append(" '%");
            valStr.append(value.toString());
            valStr.append("%'");
        } else if (compara.equals("-LIKE")) {
            valStr.append(" '%");
            valStr.append(value.toString());
            valStr.append("'");
        } else if (compara.equals("LIKE-")) {
            valStr.append(" '");
            valStr.append(value.toString());
            valStr.append("%'");
        } else if (compara.equals("IS NOT")) {
            valStr.append(" ");
            valStr.append(value.toString());
            valStr.append(" ");
        } else if (compara.equals("IS")) {
            valStr.append(" ");
            valStr.append(value.toString());
            valStr.append(" ");
        } else if (compara.equals("IN")) {
            valStr.append("(");
            valStr.append(value.toString());
            valStr.append(")");
        } else {
            if (value instanceof String) {
                valStr.append(" '");
                valStr.append(value.toString());
                valStr.append("'");
            } else if (value instanceof BigDecimal) {
                valStr.append(" ");
                valStr.append(value.toString());
                valStr.append("");
            } else {
                valStr.append(" ");
                valStr.append(value.toString());
                valStr.append("");
            }
        }
        return valStr.toString();
    }

    public List<QueryRule> getRules() {
        return rules;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getCompara() {
        return compara;
    }

    public void setCompara(String compara) {
        this.compara = compara;
    }


//----------下面是安全的防止sql注入的实现--------------------------------------------


    @Override
    public Map<String, Object> getSafeValues() {
        StringBuffer valStr = new StringBuffer();
        if (compara.equals("LIKE")) {
            valStr.append("%");
            valStr.append(value.toString());
            valStr.append("%");
        } else if (compara.equals("-LIKE")) {
            valStr.append("%");
            valStr.append(value.toString());
        } else if (compara.equals("LIKE-")) {
            valStr.append(value.toString());
            valStr.append("%");
        } else if (compara.equals("IS NOT")) {
            valStr.append(value.toString());
        } else if (compara.equals("IS")) {
            valStr.append(value.toString());
        } else {
            valStr.append(value.toString());
        }
        return Map.of("key", valStr.toString());
    }

    @Override
    public String getSafeSql(String keyFormat) {
        return getSafeSql(keyFormat, true);
    }

    @Override
    public String getSafeSql(String keyFormat, Boolean isNullPass) {
        StringBuffer where_ = new StringBuffer();
        compara = compara.toUpperCase();
        if ((key != null && compara != null) || !isNullPass) {
            if (compara.indexOf("LIKE") >= 0 && value == null) {
                value = "";
            }
            if (value == null) {
                throw new RuntimeException("不能出现一个空的检索条件DBRule.value在条件" + key);
            }
            where_.append("  AND  ");
            where_.append(key.toUpperCase());
            if (compara.indexOf("LIKE") >= 0) {
                where_.append(" ");
                where_.append("LIKE");
            } else {
                where_.append(" ");
                where_.append(compara);
            }
            where_.append(keyFormat.replace("?", "key"));
            where_.append(" ");
        }
        return where_.toString();
    }
}
