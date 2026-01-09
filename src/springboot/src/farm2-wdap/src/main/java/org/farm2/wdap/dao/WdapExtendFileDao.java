package org.farm2.wdap.dao;

import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.SuperDao;
import org.farm2.wdap.domain.WdapExtendFile;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.FarmUUID;
import org.farm2.tools.db.commons.QueryRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 扩展附件
 *
 * @author cbtg自动生成  2025-1-25 18:24:40
 */
@Repository
public class WdapExtendFileDao extends SuperDao {
    @Autowired
    private WdapExtendFileMapper wdapExtendFileMapper;
    private static String TABLE_NAME = "WDAP2_EXTENDFILE";
    private static String PRIMARY_KEY = "ID";


    public WdapExtendFile findById(String id) {
        return wdapExtendFileMapper.findById(TABLE_NAME, id);
    }

    public List<WdapExtendFile> findAll() {
        return wdapExtendFileMapper.findAll(TABLE_NAME);
    }

    public void insert(WdapExtendFile wdapExtendFile) {
        insert(wdapExtendFileMapper, wdapExtendFile, TABLE_NAME, PRIMARY_KEY);
    }

    public void update(WdapExtendFile wdapExtendFile) {
        update(wdapExtendFileMapper, wdapExtendFile, TABLE_NAME, PRIMARY_KEY);
    }


    public void deleteById(String id) {
        wdapExtendFileMapper.deleteById(TABLE_NAME, id);
    }

    public void delete(List<QueryRule> rules) {
        wdapExtendFileMapper.delete(TABLE_NAME, rules);
    }

    public DataResult queryData(DataQuery query) {
        return queryData(wdapExtendFileMapper, query, generateSelectFields(WdapExtendFile.class, "A").ignore("PASSWORD").getTitles() + ",A.FILEMODEL AS MODELTITLE,B.TITLE AS TITLE", addAlias(TABLE_NAME, "A" + " LEFT JOIN FARM2_RESOURCE_FILE AS B on A.FILEID=B.ID"));
    }

    public WdapExtendFile queryOne(List<QueryRule> rules) {
        return queryOne(wdapExtendFileMapper, DataQuery.getInstance().addRules(rules), generateSelectFields(WdapExtendFile.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"), WdapExtendFile.class);
    }

    public int countData(DataQuery query) {
        return countData(wdapExtendFileMapper, query, addAlias(TABLE_NAME, "A"));
    }

    public int countData(List<QueryRule> rules) {
        DataQuery query = DataQuery.getInstance().addRules(rules);
        query.setSql(generateSelectFields(WdapExtendFile.class, "A").getTitles(), addAlias(TABLE_NAME, "A"));
        return countData(wdapExtendFileMapper, query, addAlias(TABLE_NAME, "A"));
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
        int n = wdapExtendFileMapper.isExist(TABLE_NAME, excludeId, fieldName, value);
        return n > 0;
    }


    public List<WdapExtendFile> find(List<QueryRule> rules) {
        return wdapExtendFileMapper.find(TABLE_NAME, rules);
    }


    /**[tree：树形结构使用]
     public List<WdapExtendFile> findSubNodes(String id) {
     return wdapExtendFileMapper.findSubNodes(TABLE_NAME, findById(id).getTreecode() + "%");
     }
     */
    /**[tree：树形结构使用]
     public List<WdapExtendFile> findByParentId(String parentId) {
     return wdapExtendFileMapper.findByParentId(TABLE_NAME, parentId);
     }
     */
}
