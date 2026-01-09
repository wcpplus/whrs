package org.farm2.auth.dao;

import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.SuperDao;
import org.farm2.auth.domain.DicEntity;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.FarmUUID;
import org.farm2.tools.db.commons.QueryRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**数据字典 
 * @author cbtg自动生成  2025-1-7 13:49:27 
 */
@Repository
public class DicEntityDao extends SuperDao {
    @Autowired
    private DicEntityMapper dicEntityMapper;
    private static String TABLE_NAME = "FARM2_AUTH_DICENTITY";
    private static String PRIMARY_KEY = "ID";


    public DicEntity findById(String id) {
        return dicEntityMapper.findById(TABLE_NAME, id);
    }

    public List<DicEntity> findAll() {
        return dicEntityMapper.findAll(TABLE_NAME);
    }

    public void insert(DicEntity dicEntity) {
        insert(dicEntityMapper, dicEntity, TABLE_NAME, PRIMARY_KEY);
    }

    public void update(DicEntity dicEntity) {
        update(dicEntityMapper, dicEntity, TABLE_NAME, PRIMARY_KEY);
    }


    public void deleteById(String id) {
        dicEntityMapper.deleteById(TABLE_NAME,id);
    }

    public void delete(List<QueryRule> rules) {
        dicEntityMapper.delete(TABLE_NAME, rules);
    }

    public DataResult queryData(DataQuery query) {
        return queryData(dicEntityMapper, query, generateSelectFields(DicEntity.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"));
    }

    public DicEntity queryOne(List<QueryRule> rules) {
        return queryOne(dicEntityMapper, DataQuery.getInstance().addRules(rules), generateSelectFields(DicEntity.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"), DicEntity.class);
    }
    public int countData(DataQuery query) {
        return countData(dicEntityMapper, query, addAlias(TABLE_NAME, "A"));
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
        int n = dicEntityMapper.isExist(TABLE_NAME, excludeId, fieldName, value);
        return n > 0;
    }
    /**[tree：树形结构使用]
    public List<DicEntity> findSubNodes(String id) {
        return dicEntityMapper.findSubNodes(TABLE_NAME, findById(id).getTreecode() + "%");
    }
    */
    /**[tree：树形结构使用]
    public List<DicEntity> findByParentId(String parentId) {
        return dicEntityMapper.findByParentId(TABLE_NAME, parentId);
    }
     */
}
