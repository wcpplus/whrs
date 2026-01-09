package org.farm2.auth.dao;

import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.SuperDao;
import org.farm2.auth.domain.DicType;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.FarmUUID;
import org.farm2.tools.db.commons.QueryRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**字典类型 
 * @author cbtg自动生成  2025-1-7 15:27:42 
 */
@Repository
public class DicTypeDao extends SuperDao {
    @Autowired
    private DicTypeMapper dicTypeMapper;
    private static String TABLE_NAME = "FARM2_AUTH_DICTYPE";
    private static String PRIMARY_KEY = "ID";


    public DicType findById(String id) {
        return dicTypeMapper.findById(TABLE_NAME, id);
    }

    public List<DicType> findAll() {
        return dicTypeMapper.findAll(TABLE_NAME);
    }

    public void insert(DicType dicType) {
        insert(dicTypeMapper, dicType, TABLE_NAME, PRIMARY_KEY);
    }

    public void update(DicType dicType) {
        update(dicTypeMapper, dicType, TABLE_NAME, PRIMARY_KEY);
    }


    public void deleteById(String id) {
        dicTypeMapper.deleteById(TABLE_NAME,id);
    }

    public void delete(List<QueryRule> rules) {
        dicTypeMapper.delete(TABLE_NAME, rules);
    }

    public DataResult queryData(DataQuery query) {
        return queryData(dicTypeMapper, query, generateSelectFields(DicType.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"));
    }

    public DicType queryOne(List<QueryRule> rules) {
        return queryOne(dicTypeMapper, DataQuery.getInstance().addRules(rules), generateSelectFields(DicType.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"), DicType.class);
    }
    public int countData(DataQuery query) {
        return countData(dicTypeMapper, query, addAlias(TABLE_NAME, "A"));
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
        int n = dicTypeMapper.isExist(TABLE_NAME, excludeId, fieldName, value);
        return n > 0;
    }
    /**[tree：树形结构使用]
    public List<DicType> findSubNodes(String id) {
        return dicTypeMapper.findSubNodes(TABLE_NAME, findById(id).getTreecode() + "%");
    }
    */
    /**[tree：树形结构使用]
    public List<DicType> findByParentId(String parentId) {
        return dicTypeMapper.findByParentId(TABLE_NAME, parentId);
    }
     */
}
