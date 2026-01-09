package org.farm2.tools.db;

import lombok.Getter;
import lombok.Setter;
import org.farm2.tools.bean.FarmBeanUtils;
import org.farm2.tools.db.commons.DBSort;
import org.farm2.tools.json.FarmJsons;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DataResult {
    private int totalSize;
    private int totalPage;
    private int page;
    private int pageSize;
    private List<Map<String, Object>> data;
    private List<DBSort> sorts;
    @Setter
    @Getter
    private Map<String, Object> para;


    public DataResult(List<Map<String, Object>> list, int num, DataQuery query) {
        if (num == 0) {
            num = list.size();
        }
        this.data = list;
        this.sorts = query.getSorts();
        this.page = query.getPage();
        this.pageSize = query.getPageSize();
        this.totalSize = num;
        this.totalPage = num / query.getPageSize();
        if (num % query.getPageSize() > 0) {
            this.totalPage = this.totalPage + 1;
        }
    }

    public DataResult(List<Map<String, Object>> list, int num) {
        if (num == 0) {
            num = list.size();
        }
        this.data = list;
        this.totalSize = num;
        this.totalPage = 1;
        this.page = 1;
    }

    public DataResult(List<Map<String, Object>> list, int page, int pageSize, int totalSize) {
        this.data = list;
        this.totalSize = totalSize;
        this.totalPage = totalSize / pageSize + (totalSize % pageSize > 0 ? 1 : 0);
        this.pageSize = pageSize;
        this.page = page;
    }

    public List<DBSort> getSorts() {
        if (sorts == null) {
            return new ArrayList<>();
        }
        return sorts;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public <T> List<T> getData(Class<T> dataClass) {
        List<T> entityList = new ArrayList<>();
        for (Map<String, Object> map : data) {
            try {
                T entity = dataClass.getDeclaredConstructor().newInstance();
                FarmBeanUtils.copyProperties(map, entity);
                entityList.add(entity);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return entityList;
    }

    public String toJson() {
        return FarmJsons.toFormatJson(this);
    }

    public void runFormatTime(String title) {
        runFormatTime(title, "yyyy-MM-dd HH:mm");
    }

    public void runFormatTime(String title, String yyyyMMddHHmmss) {
        List<Map<String, Object>> list = this.data;
        for (Map<String, Object> node : list) {
            try {
                String time = null;
                if (node.get(title) instanceof java.sql.Date) {
                    java.util.Date d = new java.util.Date(((java.sql.Date) node.get(title)).getTime());
                    SimpleDateFormat ormat = new SimpleDateFormat("yyyyMMdd");
                    time = ormat.format(d);
                } else {
                    time = (String) node.get(title);
                }
                if (time == null || time.trim().length() <= 0) {
                    continue;
                }
                if (time.indexOf("-") + time.indexOf(":") > 0) {
                    // 只要有-和：就算是已经被转义过的时间，就不用再转换了
                    continue;
                }
                if (12 == time.length()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
                    SimpleDateFormat newSdf = new SimpleDateFormat(yyyyMMddHHmmss);
                    Date date = sdf.parse(time);
                    node.put(title, newSdf.format(date));
                } else {
                    try {
                        time = (time + "00000000000000").substring(0, 14);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                        SimpleDateFormat newSdf = new SimpleDateFormat(yyyyMMddHHmmss);
                        Date date = sdf.parse(time);
                        node.put(title, newSdf.format(date));
                    } catch (ParseException e) {
                        node.put(title, null);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 迭代结果数据
     *
     * @param handler
     */
    public void runDataHandle(ResultDataHandle handler) {
        for (Map<String, Object> row : data) {
            handler.handle(row);
        }
    }

    /**
     * 对结果集合中某个字段值进行替换，用于数据字典的翻译
     *
     * @param str   字典描述字符串 "1:可用,2:禁用"
     * @param title 翻译字段名 如：TYPE
     */
    public DataResult runDictionary(String str, String title) {
        String[] mapStr = str.trim().split(",");
        Map<String, String> dictionary = new HashMap<String, String>();
        for (String keyValue : mapStr) {
            String[] node = keyValue.split(":");
            try {
                dictionary.put(node[0], node[1]);
            } catch (Exception e) {
                throw new RuntimeException(e + ",请检查字典项：'" + keyValue + "'");
            }
        }
        runDictionary(dictionary, title);
        return this;
    }

    /**
     * 对结果集合中某个字段值进行替换，用于数据字典的翻译
     *
     * @param dictionary 字典map
     * @param title      翻译字段名 如：TYPE
     */
    public DataResult runDictionary(Map<String, String> dictionary, String title) {
        for (Map<String, Object> node : data) {
            String key = String.valueOf(node.get(title));
            Object value = dictionary.get(key);
            if (value != null) {
                node.put(title, value);
            }
        }
        return this;
    }

    public int getStartNum() {
        return (page - 1) * pageSize;
    }

    public int getEndNum() {
        int endNum = (page - 1) * pageSize + pageSize;
        if (totalSize < endNum) {
            endNum = totalSize;
        }
        return endNum;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }
}
