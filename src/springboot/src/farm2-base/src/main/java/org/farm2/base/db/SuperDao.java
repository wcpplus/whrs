package org.farm2.base.db;

import org.farm2.base.utils.FarmEmojiUtils;
import org.farm2.tools.bean.FarmBeanUtils;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.FarmUUID;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class SuperDao {

    /**
     * 生成类的select字段带别名
     *
     * @param beanClass
     * @param tableAlias
     * @return
     */
    public static DataSelectFields generateSelectFields(Class<?> beanClass, String tableAlias) {
        return new DataSelectFields(beanClass, tableAlias);
    }

    /**
     * 添加一个表别名
     *
     * @param tableName
     * @return
     */
    public String addAlias(String tableName, String tableAlias) {
        return tableName + " AS " + tableAlias;
    }

    public <T> void update(SuperMapper mapper, T bean, String tableName, String primaryKey) {
        FarmEmojiUtils.emojiHandle(bean);
        mapper.update(FarmBeanUtils.copyProperties(bean, new HashMap<>()), tableName, primaryKey);
    }

    public <T> void insert(SuperMapper mapper, T bean, String tableName, String primaryKey) {
        FarmEmojiUtils.emojiHandle(bean);
        mapper.insert(FarmBeanUtils.copyProperties(FarmUUID.to(bean, primaryKey), new HashMap<>()), tableName);
    }

    public DataResult queryData(SuperMapper mapper, DataQuery query, String select, String from) {
        query.setSql(select, from);
        return DataQueryBuilder.getResult(mapper.dataQuery(query), query.isCount() ? mapper.countQuery(query) : 0, query);
    }

    public int countData(SuperMapper mapper, DataQuery query, String from) {
        query.setSql(null, from);
        return mapper.countQuery(query);
    }

    public <T> T queryOne(SuperMapper mapper, DataQuery query, String select, String from, Class<T> dataClass) {
        query.setSql(select, from);
        query.setCount(false);
        DataResult result = DataQueryBuilder.getResult(mapper.dataQuery(query), query.isCount() ? mapper.countQuery(query) : 0, query);
        List<T> list = result.getData(dataClass);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public Integer getMaxIntValue(SuperMapper mapper, String field, String tableName) {
        return mapper.getMaxIntVal(tableName, field);
    }
}