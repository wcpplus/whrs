package org.farm2.tools.db.commons;

import org.apache.commons.collections.KeyValue;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.farm2.tools.db.enums.FarmDbLikeModel;
import org.farm2.tools.i18n.I18n;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FarmSqls {

    public static String getMarkBy_INNER_JOIN(String tableName, Pair<String, String>... onLimit) {
        if (onLimit == null || onLimit.length == 0) {
            return " INNER JOIN " + tableName;
        }
        StringBuilder sb = new StringBuilder(" INNER JOIN ");
        sb.append(tableName).append(" ON ");
        for (int i = 0; i < onLimit.length; i++) {
            if (i > 0) {
                sb.append(" AND ");
            }
            Pair<String, String> pair = onLimit[i];
            sb.append(pair.getKey()).append(" = ").append(pair.getValue());
        }
        return sb.toString();
    }

    public static String getMarkBy_LEFT_JOIN(String tableName, Pair<String, String>... onLimit) {
        if (onLimit == null || onLimit.length == 0) {
            return " LEFT JOIN " + tableName;
        }
        StringBuilder sb = new StringBuilder(" LEFT JOIN ");
        sb.append(tableName).append(" ON ");
        for (int i = 0; i < onLimit.length; i++) {
            if (i > 0) {
                sb.append(" AND ");
            }
            Pair<String, String> pair = onLimit[i];
            sb.append(pair.getKey()).append(" = ").append(pair.getValue());
        }
        return sb.toString();
    }


    private static final String IDENTIFIER_REGEX = "^[a-zA-Z_][a-zA-Z0-9_]*(\\.[a-zA-Z_][a-zA-Z0-9_]*)*$";
    private static final Pattern IDENTIFIER_PATTERN = Pattern.compile(IDENTIFIER_REGEX);

    /**
     * 校验列名
     *
     * @param input
     */
    public static void wipeVirus_key(String input) {
        if (StringUtils.isBlank(input)) {
            throw new IllegalArgumentException("SQL字段列表不能为空或null");
        }
        String[] parts = input.split(",");
        for (String part : parts) {
            String trimmed = part.trim();
            if (trimmed.isEmpty()) {
                throw new IllegalArgumentException("字段列表中包含空字段: '" + input + "'");
            }
            if (!IDENTIFIER_PATTERN.matcher(trimmed).matches()) {
                throw new IllegalArgumentException(
                        "非法SQL标识符: '" + trimmed + "'. " +
                                "仅允许字母、数字、下划线和点号（用于schema），且每部分必须以字母或下划线开头。"
                );
            }
        }
    }

    public static void wipeVirus_compara(String input) {
        if (StringUtils.isBlank(input)) {
            throw new RuntimeException(I18n.msg("比较操作符不能为空:SQLSyntaxErrorException"));
        }
        // 统一转为大写，便于匹配（LIKE 通常是大写）
        String normalized = input.trim().toUpperCase();
        if (!Set.of(
                "=", "!=", "<>", ">", "<", ">=", "<=", "LIKE", "-LIKE", "LIKE-", "IS NOT", "IS"
        ).contains(normalized)) {
            throw new RuntimeException(I18n.msg("?违反SQL注入风险约束:SQLSyntaxErrorException！非法比较操作符：?", input));
        }
    }

    /**
     * 安全转义 SQL 值（仅用于临时应急！）
     *
     * @param value     原始值
     * @param isNumeric 是否应为数字（如 id、status_code）
     * @return 转义后的安全字符串
     */
    public static String escapeSqlValue(String value, boolean isNumeric) {
        if (value == null) {
            return "NULL"; // 注意：调用方需特殊处理 NULL（不能加引号）
        }

        if (isNumeric) {
            // 更严格的数字校验：禁止科学计数法、前导零（可选），这里保持宽松但安全
            if (!value.matches("^[+-]?(?:\\d+\\.?\\d*|\\.\\d+)$")) {
                throw new IllegalArgumentException("Invalid numeric value: " + value);
            }
            return value; // 数字不加引号，直接返回
        }

        // 字符串：必须转义 \ 和 '
        // MySQL 默认模式下：\ 是转义字符，' 是字符串边界
        String escaped = value
                .replace("\\", "\\\\") // \ → \\
                .replace("'", "\\'");  // ' → \'

        // 可选：移除或拒绝高危控制字符（增强健壮性）
        if (escaped.indexOf('\0') >= 0) {
            throw new IllegalArgumentException("Null byte not allowed in SQL value");
        }
        if (sql_inj(escaped)) {
            escaped = "NONE";
        }
        return escaped;
    }


    /**
     * 是否字符串有sql注入风险
     *
     * @param str
     * @return
     */
    private static boolean sql_inj(String str) {
        String lowerStr = str.toLowerCase();
        String inj_str = "'| and | exec | insert | select | delete | update | count |*|%| chr | mid | master | truncate | char | declare |;| or |,|--";
        // 这里的东西还可以自己添加
        String[] inj_stra = inj_str.split("\\|");
        for (int i = 0; i < inj_stra.length; i++) {
            String charstr = inj_stra[i];
            if (str.toLowerCase().indexOf(charstr) >= 0) {
                return true;
            }
        }
        return false;
    }


    /**
     * 获得查询条件中in后的字符串拼接，如'1','2','3'...
     *
     * @param vals
     * @return
     */
    public static String getWhereInStrings(List<String> vals) {
        StringBuffer sqlVals = new StringBuffer();
        for (String val : vals) {
            val = escapeSqlValue(val, false);
            if (sqlVals.length() == 0) {
                sqlVals.append("'" + val + "'");
            } else {
                sqlVals.append(",'" + val + "'");
            }
        }
        return sqlVals.toString();
    }


    /**
     * 获得查询条件中in后的字符串拼接，如'1','2','3'...（替换占位符）
     *
     * @param vals
     * @return
     */
    public static String getWhereInKeys(List<String> vals) {
        StringBuffer sqlVals = new StringBuffer();
        for (String val : vals) {
            if (sqlVals.length() == 0) {
                sqlVals.append(val);
            } else {
                sqlVals.append("," + val);
            }
        }
        return sqlVals.toString();
    }

    /**
     * 添加like检索条件的通配符
     *
     * @param val
     * @param farmDbLikeModel
     * @return
     */
    public static String getValueLike(String val, FarmDbLikeModel farmDbLikeModel) {
        if (farmDbLikeModel.equals(FarmDbLikeModel.LEFT)) {
            return "%" + val + "";
        }
        if (farmDbLikeModel.equals(FarmDbLikeModel.RIGHT)) {
            return "" + val + "%";
        }
        if (farmDbLikeModel.equals(FarmDbLikeModel.ALL)) {
            return "%" + val + "%";
        }
        return val;
    }

    /**生成类似如下sql： APPMODEL LIKE '%WTS_SUBJECT%' OR APPMODEL LIKE '%WTS_SUBJECT%'
     * @param appmodels
     * @return
     */
    public static String getOrLikes(List<String> appmodels) {
        String placeholders = appmodels.stream()
                .map(id -> "APPMODEL like ?")
                .collect(Collectors.joining(" OR "));
        return placeholders;
    }
}
