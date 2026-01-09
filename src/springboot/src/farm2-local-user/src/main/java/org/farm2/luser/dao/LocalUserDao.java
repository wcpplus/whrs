package org.farm2.luser.dao;

import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.SuperDao;
import org.farm2.luser.domain.LocalUser;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.FarmUUID;
import org.farm2.tools.db.commons.QueryRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LocalUserDao extends SuperDao {
    @Autowired
    private LocalUserMapper localUseMapper;
    private static String TABLE_NAME = "FARM2_LOCAL_USER";
    private static String PRIMARY_KEY = "ID";


    public LocalUser findById(String id) {
        return localUseMapper.findById(TABLE_NAME, id);
    }

    public LocalUser findByLoginname(String loginname) {
        LocalUser user = localUseMapper.findByLoginname(TABLE_NAME, loginname);
        return user;
    }

    public List<LocalUser> findAll() {
        return localUseMapper.findAll(TABLE_NAME);
    }

    public void insert(LocalUser user) {
        insert(localUseMapper, user, TABLE_NAME, PRIMARY_KEY);
    }

    public void update(LocalUser user) {
        update(localUseMapper, user, TABLE_NAME, PRIMARY_KEY);
    }


    public void deleteById(String id) {
        localUseMapper.deleteById(TABLE_NAME, id);
    }

    public void delete(List<QueryRule> rules) {
        localUseMapper.delete(TABLE_NAME, rules);
    }

    public DataResult queryData(DataQuery query) {
        return queryData(localUseMapper, query, generateSelectFields(LocalUser.class, "A").ignore("PASSWORD").getTitles() + ", B.TREECODE AS TREECODE, B.NAME AS ORGNAME", addAlias(TABLE_NAME, "A") + " left join FARM2_LOCAL_ORG AS B on a.orgid=b.ID");
    }

    public LocalUser queryOne(List<QueryRule> rules) {
        return queryOne(localUseMapper, DataQuery.getInstance().addRules(rules), generateSelectFields(LocalUser.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"), LocalUser.class);
    }

    public int countData(DataQuery query) {
        return countData(localUseMapper, query, addAlias(TABLE_NAME, "A"));
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
        int n = localUseMapper.isExist(TABLE_NAME, excludeId, fieldName, value);
        return n > 0;
    }
}
