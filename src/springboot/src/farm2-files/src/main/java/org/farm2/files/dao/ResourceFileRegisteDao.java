package org.farm2.files.dao;

import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.SuperDao;
import org.farm2.files.domain.ResourceFileRegiste;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.FarmUUID;
import org.farm2.tools.db.commons.QueryRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**附件注册 
 * @author cbtg自动生成  2025-2-4 10:42:08 
 */
@Repository
public class ResourceFileRegisteDao extends SuperDao {
    @Autowired
    private ResourceFileRegisteMapper resourceFileRegisteMapper;
    private static String TABLE_NAME = "FARM2_RESOURCE_FILE_REGISTE";
    private static String PRIMARY_KEY = "ID";


    public ResourceFileRegiste findById(String id) {
        return resourceFileRegisteMapper.findById(TABLE_NAME, id);
    }

    public List<ResourceFileRegiste> findAll() {
        return resourceFileRegisteMapper.findAll(TABLE_NAME);
    }

    public void insert(ResourceFileRegiste resourceFileRegiste) {
        insert(resourceFileRegisteMapper, resourceFileRegiste, TABLE_NAME, PRIMARY_KEY);
    }

    public void update(ResourceFileRegiste resourceFileRegiste) {
        update(resourceFileRegisteMapper, resourceFileRegiste, TABLE_NAME, PRIMARY_KEY);
    }


    public void deleteById(String id) {
        resourceFileRegisteMapper.deleteById(TABLE_NAME,id);
    }

    public void delete(List<QueryRule> rules) {
        resourceFileRegisteMapper.delete(TABLE_NAME, rules);
    }

    public DataResult queryData(DataQuery query) {
        return queryData(resourceFileRegisteMapper, query, generateSelectFields(ResourceFileRegiste.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"));
    }

    public ResourceFileRegiste queryOne(List<QueryRule> rules) {
        return queryOne(resourceFileRegisteMapper, DataQuery.getInstance().addRules(rules), generateSelectFields(ResourceFileRegiste.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"), ResourceFileRegiste.class);
    }
    public int countData(DataQuery query) {
        return countData(resourceFileRegisteMapper, query, addAlias(TABLE_NAME, "A"));
    }

    public int countData(List<QueryRule> rules) {
        DataQuery query = DataQuery.getInstance().addRules(rules);
        query.setSql(generateSelectFields(ResourceFileRegiste.class, "A").getTitles(), addAlias(TABLE_NAME, "A"));
        return countData(resourceFileRegisteMapper, query, addAlias(TABLE_NAME, "A"));
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
        int n = resourceFileRegisteMapper.isExist(TABLE_NAME, excludeId, fieldName, value);
        return n > 0;
    }
    
    
    public List<ResourceFileRegiste> find(List<QueryRule> rules) {
        return resourceFileRegisteMapper.find(TABLE_NAME, rules);
    }
    
    
    /**[tree：树形结构使用]
    public List<ResourceFileRegiste> findSubNodes(String id) {
        return resourceFileRegisteMapper.findSubNodes(TABLE_NAME, findById(id).getTreecode() + "%");
    }
    */
    /**[tree：树形结构使用]
    public List<ResourceFileRegiste> findByParentId(String parentId) {
        return resourceFileRegisteMapper.findByParentId(TABLE_NAME, parentId);
    }
     */
}
