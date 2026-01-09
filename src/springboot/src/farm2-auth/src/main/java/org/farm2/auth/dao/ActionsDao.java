package org.farm2.auth.dao;

import org.apache.commons.lang3.StringUtils;
import org.farm2.auth.dto.MenusDto;
import org.farm2.base.db.SuperDao;
import org.farm2.auth.domain.Actions;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.FarmUUID;
import org.farm2.tools.db.commons.QueryRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**系统权限 
 * @author cbtg自动生成  2025-1-6 11:01:26 
 */
@Repository
public class ActionsDao extends SuperDao {
    @Autowired
    private ActionsMapper actionsMapper;
    private static String TABLE_NAME = "FARM2_AUTH_ACTIONS";
    private static String PRIMARY_KEY = "ID";


    public Actions findById(String id) {
        return actionsMapper.findById(TABLE_NAME, id);
    }

    public List<Actions> findAll() {
        return actionsMapper.findAll(TABLE_NAME);
    }

    public void insert(Actions actions) {
        insert(actionsMapper, actions, TABLE_NAME, PRIMARY_KEY);
    }

    public void update(Actions actions) {
        update(actionsMapper, actions, TABLE_NAME, PRIMARY_KEY);
    }


    public void deleteById(String id) {
        actionsMapper.deleteById(TABLE_NAME,id);
    }

    public void delete(List<QueryRule> rules) {
        actionsMapper.delete(TABLE_NAME, rules);
    }

    public DataResult queryData(DataQuery query) {
        return queryData(actionsMapper, query, generateSelectFields(Actions.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"));
    }

    public Actions queryOne(List<QueryRule> rules) {
        return queryOne(actionsMapper, DataQuery.getInstance().addRules(rules), generateSelectFields(Actions.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"), Actions.class);
    }
    public int countData(DataQuery query) {
        return countData(actionsMapper, query, addAlias(TABLE_NAME, "A"));
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
        int n = actionsMapper.isExist(TABLE_NAME, excludeId, fieldName, value);
        return n > 0;
    }
    /**[tree：树形结构使用] */
    public List<Actions> findSubNodes(String id) {
        return actionsMapper.findSubNodes(TABLE_NAME, findById(id).getTreecode() + "%");
    }

    /**[tree：树形结构使用] */
    public List<Actions> findByParentId(String parentId) {
        return actionsMapper.findByParentId(TABLE_NAME, parentId);
    }

    public List<Actions> findByActionIds(List<String> actionIds) {
        if (actionIds.size() <= 0) {
            return new ArrayList<>();
        }
        return actionsMapper.findByActionIds(TABLE_NAME, actionIds);
    }

    public Actions findBykey(String menukey) {
        return actionsMapper.findByMenuKey(TABLE_NAME, menukey);
    }



    public Collection<String> findByMenuKeys(List<String> menuKeys) {
        if (menuKeys.size() <= 0) {
            return new ArrayList<>();
        }
        return actionsMapper.findActionsByMenuKeys(TABLE_NAME, menuKeys);
    }
}
