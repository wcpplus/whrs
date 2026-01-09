package org.farm2.social.dao;

import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.SuperDao;
import org.farm2.social.domain.UserMessage;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.FarmUUID;
import org.farm2.tools.db.commons.QueryRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**用户消息 
 * @author cbtg自动生成  2025-4-23 10:29:47 
 */
@Repository
public class UserMessageDao extends SuperDao {
    @Autowired
    private UserMessageMapper userMessageMapper;
    private static String TABLE_NAME = "USER_MESSAGE";
    private static String PRIMARY_KEY = "ID";


    public UserMessage findById(String id) {
        return userMessageMapper.findById(TABLE_NAME, id);
    }

    public List<UserMessage> findAll() {
        return userMessageMapper.findAll(TABLE_NAME);
    }

    public void insert(UserMessage userMessage) {
        insert(userMessageMapper, userMessage, TABLE_NAME, PRIMARY_KEY);
    }

    public void update(UserMessage userMessage) {
        update(userMessageMapper, userMessage, TABLE_NAME, PRIMARY_KEY);
    }


    public void deleteById(String id) {
        userMessageMapper.deleteById(TABLE_NAME,id);
    }

    public void delete(List<QueryRule> rules) {
        userMessageMapper.delete(TABLE_NAME, rules);
    }

    public DataResult queryData(DataQuery query) {
        return queryData(userMessageMapper, query, generateSelectFields(UserMessage.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"));
    }

    public UserMessage queryOne(List<QueryRule> rules) {
        return queryOne(userMessageMapper, DataQuery.getInstance().addRules(rules), generateSelectFields(UserMessage.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"), UserMessage.class);
    }
    public int countData(DataQuery query) {
        return countData(userMessageMapper, query, addAlias(TABLE_NAME, "A"));
    }

    public int countData(List<QueryRule> rules) {
        DataQuery query = DataQuery.getInstance().addRules(rules);
        query.setSql(generateSelectFields(UserMessage.class, "A").getTitles(), addAlias(TABLE_NAME, "A"));
        return countData(userMessageMapper, query, addAlias(TABLE_NAME, "A"));
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
        int n = userMessageMapper.isExist(TABLE_NAME, excludeId, fieldName, value);
        return n > 0;
    }
    
    
    public List<UserMessage> find(List<QueryRule> rules) {
        return userMessageMapper.find(TABLE_NAME, rules);
    }
    
    
    /**[tree：树形结构使用]
    public List<UserMessage> findSubNodes(String id) {
        return userMessageMapper.findSubNodes(TABLE_NAME, findById(id).getTreecode() + "%");
    }
    */
    /**[tree：树形结构使用]
    public List<UserMessage> findByParentId(String parentId) {
        return userMessageMapper.findByParentId(TABLE_NAME, parentId);
    }
     */
}
