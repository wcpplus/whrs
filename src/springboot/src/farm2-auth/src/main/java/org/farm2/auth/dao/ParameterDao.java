package org.farm2.auth.dao;

import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.SuperDao;
import org.farm2.auth.domain.Parameter;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.FarmUUID;
import org.farm2.tools.db.commons.QueryRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统参数
 *
 * @author cbtg自动生成  2025-1-8 10:39:16
 */
@Repository
public class ParameterDao extends SuperDao {
    @Autowired
    private ParameterMapper parameterMapper;
    private static String TABLE_NAME = "FARM2_AUTH_PARAMETER";
    private static String PRIMARY_KEY = "ID";


    public Parameter findById(String id) {
        return parameterMapper.findById(TABLE_NAME, id);
    }

    public List<Parameter> findAll() {
        return parameterMapper.findAll(TABLE_NAME);
    }

    public void insert(Parameter parameter) {
        insert(parameterMapper, parameter, TABLE_NAME, PRIMARY_KEY);
    }

    public void update(Parameter parameter) {
        update(parameterMapper, parameter, TABLE_NAME, PRIMARY_KEY);
    }


    public void deleteById(String id) {
        parameterMapper.deleteById(TABLE_NAME, id);
    }

    public void delete(List<QueryRule> rules) {
        parameterMapper.delete(TABLE_NAME, rules);
    }

    public DataResult queryData(DataQuery query) {
        return queryData(parameterMapper, query, generateSelectFields(Parameter.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"));
    }

    public Parameter queryOne(List<QueryRule> rules) {
        return queryOne(parameterMapper, DataQuery.getInstance().addRules(rules), generateSelectFields(Parameter.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"), Parameter.class);
    }

    public int countData(DataQuery query) {
        return countData(parameterMapper, query, addAlias(TABLE_NAME, "A"));
    }

    /**
     * 字段field上是否存在值value，id为id的记录除外（可以判断数据库中某个key是否存在的业务）
     *
     * @param excludeId 排除的id
     * @param fieldName
     * @param value
     * @return
     */
    public boolean isExist(String excludeId, String fieldName, String value) {
        if (StringUtils.isBlank(excludeId)) {
            excludeId = FarmUUID.getUUID32();
        }
        int n = parameterMapper.isExist(TABLE_NAME, excludeId, fieldName, value);
        return n > 0;
    }

    public List<Parameter> findAllBySourceType(String sourcetype) {
        return parameterMapper.findBySourceType(TABLE_NAME, sourcetype);
    }

    public Parameter findByKey(String key) {
        return parameterMapper.findByKey(TABLE_NAME, key);
    }
    /**[tree：树形结构使用]
     public List<Parameter> findSubNodes(String id) {
     return parameterMapper.findSubNodes(TABLE_NAME, findById(id).getTreecode() + "%");
     }
     */
    /**[tree：树形结构使用]
     public List<Parameter> findByParentId(String parentId) {
     return parameterMapper.findByParentId(TABLE_NAME, parentId);
     }
     */
}
