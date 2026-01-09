package org.farm2.files.dao;

import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.SuperDao;
import org.farm2.files.domain.ResourceFile;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.FarmUUID;
import org.farm2.tools.db.commons.QueryRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**附件 
 * @author cbtg自动生成  2025-1-13 14:50:11 
 */
@Repository
public class ResourceFileDao extends SuperDao {
    @Autowired
    private ResourceFileMapper resourceFileMapper;
    private static String TABLE_NAME = "FARM2_RESOURCE_FILE";
    private static String PRIMARY_KEY = "ID";


    public ResourceFile findById(String id) {
        return resourceFileMapper.findById(TABLE_NAME, id);
    }

    public List<ResourceFile> findAll() {
        return resourceFileMapper.findAll(TABLE_NAME);
    }

    public void insert(ResourceFile resourceFile) {
        insert(resourceFileMapper, resourceFile, TABLE_NAME, PRIMARY_KEY);
    }

    public void update(ResourceFile resourceFile) {
        update(resourceFileMapper, resourceFile, TABLE_NAME, PRIMARY_KEY);
    }


    public void deleteById(String id) {
        resourceFileMapper.deleteById(TABLE_NAME,id);
    }

    public void delete(List<QueryRule> rules) {
        resourceFileMapper.delete(TABLE_NAME, rules);
    }

    public DataResult queryData(DataQuery query) {
        return queryData(resourceFileMapper, query, generateSelectFields(ResourceFile.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"));
    }

    public ResourceFile queryOne(List<QueryRule> rules) {
        return queryOne(resourceFileMapper,
                DataQuery.getInstance().addRules(rules),
                generateSelectFields(ResourceFile.class, "A").ignore("PASSWORD").getTitles()
                , addAlias(TABLE_NAME, "A"),
                ResourceFile.class);
    }
    public int countData(DataQuery query) {
        return countData(resourceFileMapper, query, addAlias(TABLE_NAME, "A"));
    }

    public int countData(List<QueryRule> rules) {
        DataQuery query = DataQuery.getInstance().addRules(rules);
        query.setSql(generateSelectFields(ResourceFile.class, "A").getTitles(), addAlias(TABLE_NAME, "A"));
        return countData(resourceFileMapper, query, addAlias(TABLE_NAME, "A"));
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
        int n = resourceFileMapper.isExist(TABLE_NAME, excludeId, fieldName, value);
        return n > 0;
    }
    /**[tree：树形结构使用]
    public List<ResourceFile> findSubNodes(String id) {
        return resourceFileMapper.findSubNodes(TABLE_NAME, findById(id).getTreecode() + "%");
    }
    */
    /**[tree：树形结构使用]
    public List<ResourceFile> findByParentId(String parentId) {
        return resourceFileMapper.findByParentId(TABLE_NAME, parentId);
    }
     */
}
