package org.farm2.luser.dao;

import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.SuperDao;
import org.farm2.luser.domain.LocalUserInfo;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.FarmUUID;
import org.farm2.tools.db.commons.QueryRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**用户信息 
 * @author cbtg自动生成  2026-1-5 21:07:40 
 */
@Repository
public class LocalUserInfoDao extends SuperDao {
    @Autowired
    private LocalUserInfoMapper localUserInfoMapper;
    private static String TABLE_NAME = "FARM2_LOCAL_USER_INFO";
    private static String PRIMARY_KEY = "ID";


    public LocalUserInfo findById(String id) {
        return localUserInfoMapper.findById(TABLE_NAME, id);
    }

    public List<LocalUserInfo> findAll() {
        return localUserInfoMapper.findAll(TABLE_NAME);
    }

    public void insert(LocalUserInfo localUserInfo) {
        insert(localUserInfoMapper, localUserInfo, TABLE_NAME, PRIMARY_KEY);
    }

    public void update(LocalUserInfo localUserInfo) {
        update(localUserInfoMapper, localUserInfo, TABLE_NAME, PRIMARY_KEY);
    }


    public void deleteById(String id) {
        localUserInfoMapper.deleteById(TABLE_NAME,id);
    }

    public void delete(List<QueryRule> rules) {
        localUserInfoMapper.delete(TABLE_NAME, rules);
    }

    public DataResult queryData(DataQuery query) {
        return queryData(localUserInfoMapper, query, generateSelectFields(LocalUserInfo.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"));
    }

    public LocalUserInfo queryOne(List<QueryRule> rules) {
        return queryOne(localUserInfoMapper, DataQuery.getInstance().addRules(rules), generateSelectFields(LocalUserInfo.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"), LocalUserInfo.class);
    }
    public int countData(DataQuery query) {
        return countData(localUserInfoMapper, query, addAlias(TABLE_NAME, "A"));
    }

    public int countData(List<QueryRule> rules) {
        DataQuery query = DataQuery.getInstance().addRules(rules);
        query.setSql(generateSelectFields(LocalUserInfo.class, "A").getTitles(), addAlias(TABLE_NAME, "A"));
        return countData(localUserInfoMapper, query, addAlias(TABLE_NAME, "A"));
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
        int n = localUserInfoMapper.isExist(TABLE_NAME, excludeId, fieldName, value);
        return n > 0;
    }
    
    
    public List<LocalUserInfo> find(List<QueryRule> rules) {
        return localUserInfoMapper.find(TABLE_NAME, rules);
    }
    
    
    /**[tree：树形结构使用]
    public List<LocalUserInfo> findSubNodes(String id) {
        return localUserInfoMapper.findSubNodes(TABLE_NAME, findById(id).getTreecode() + "%");
    }
    */
    /**[tree：树形结构使用]
    public List<LocalUserInfo> findByParentId(String parentId) {
        return localUserInfoMapper.findByParentId(TABLE_NAME, parentId);
    }
     */
}
