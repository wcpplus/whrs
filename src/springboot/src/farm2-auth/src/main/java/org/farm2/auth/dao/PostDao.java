package org.farm2.auth.dao;

import org.apache.commons.lang3.StringUtils;
import org.farm2.auth.domain.UserPost;
import org.farm2.base.db.SuperDao;
import org.farm2.auth.domain.Post;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.FarmUUID;
import org.farm2.tools.db.commons.QueryRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**角色 
 * @author cbtg自动生成  2025-1-6 15:10:13 
 */
@Repository
public class PostDao extends SuperDao {
    @Autowired
    private PostMapper postMapper;
    private static String TABLE_NAME = "FARM2_AUTH_POST";
    private static String PRIMARY_KEY = "ID";


    public Post findById(String id) {
        return postMapper.findById(TABLE_NAME, id);
    }

    public List<Post> findAll() {
        return postMapper.findAll(TABLE_NAME);
    }

    public void insert(Post post) {
        insert(postMapper, post, TABLE_NAME, PRIMARY_KEY);
    }

    public void update(Post post) {
        update(postMapper, post, TABLE_NAME, PRIMARY_KEY);
    }


    public void deleteById(String id) {
        postMapper.deleteById(TABLE_NAME,id);
    }

    public void delete(List<QueryRule> rules) {
        postMapper.delete(TABLE_NAME, rules);
    }

    public DataResult queryData(DataQuery query) {
        return queryData(postMapper, query, generateSelectFields(Post.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"));
    }

    public Post queryOne(List<QueryRule> rules) {
        return queryOne(postMapper, DataQuery.getInstance().addRules(rules), generateSelectFields(Post.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"), Post.class);
    }
    public int countData(DataQuery query) {
        return countData(postMapper, query, addAlias(TABLE_NAME, "A"));
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
        int n = postMapper.isExist(TABLE_NAME, excludeId, fieldName, value);
        return n > 0;
    }

    public List<Post> findByKeyid(String keyid) {
        return postMapper.findByKeyid(TABLE_NAME, keyid);
    }

    public List<Post> find(List<QueryRule> rules) {
        return postMapper.find(TABLE_NAME, rules);
    }
    public List<Post> findByName(String name) {
        return postMapper.findByName(TABLE_NAME, name);
    }
}
