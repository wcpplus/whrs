package org.farm2.tools.db.commons;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Setter
@Getter
public class DBSort {
    private String field;
    private SORT_TYPE sort_type;


    @Override
    public String toString() {
        return "DBSort{" +
                "field='" + Objects.toString(field, "") + '\'' + // 如果 field 为 null，则返回空字符串
                ", sort_type=" + (sort_type != null ? sort_type.name() : "null") + // 确保 sort_type 不为 null
                '}';
    }

    public static String getSql(List<DBSort> sorts) {
        StringBuffer sql = new StringBuffer();
        if (!sorts.isEmpty()) {
            sql.append(" ORDER BY");
        }
        for (int n = 0; n < sorts.size(); n++) {
            if (n == 0) {
                sql.append(" ").append(sorts.get(n).getField());
            } else {
                sql.append(",").append(sorts.get(n).getField());
            }
            if (n == sorts.size() - 1) {
                sql.append(" ").append(sorts.get(n).getSort_type().name());
            }
        }
        return sql.toString();
    }

    public enum SORT_TYPE {ASC, DESC}

    public DBSort(String field, SORT_TYPE sort_type) {
        FarmSqls.wipeVirus_key(field);
        this.field = field;
        this.sort_type = sort_type;
    }

}
