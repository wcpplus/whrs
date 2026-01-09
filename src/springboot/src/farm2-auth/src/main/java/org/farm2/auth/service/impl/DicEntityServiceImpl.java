package org.farm2.auth.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.farm2.auth.dao.DicTypeDao;
import org.farm2.auth.domain.DicType;
import org.farm2.base.db.FarmDbFields;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.base.exception.FarmExceptionUtils;
import org.farm2.auth.dao.DicEntityDao;
import org.farm2.auth.domain.DicEntity;
import org.farm2.auth.service.DicEntityServiceInter;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.DBRule;
import org.farm2.tools.db.commons.DBRuleList;
import org.farm2.tools.i18n.I18n;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据字典
 *
 * @author cbtg自动生成  2025-1-7 13:49:27
 */
@Service
@Slf4j
public class DicEntityServiceImpl implements DicEntityServiceInter {
    @Autowired
    private DicTypeDao dicTypeDao;

    @Autowired
    private DicEntityDao dicEntityDao;

    @Transactional
    @Override
    public DicEntity insertDicEntityEntity(DicEntity dicEntity) {
        FarmDbFields.initInsertBean(dicEntity, FarmUserContextLoader.getCurrentUser());
        //FarmBeanUtils.runFunctionByBlank(dicEntity.getType(), "1", dicEntity::setType);
        dicEntityDao.insert(dicEntity);
        //[tree：树形结构使用]
        //initTreeCode(actions.getId());
        return dicEntity;
    }

    @Transactional
    @Override
    public DicEntity editDicEntityEntity(DicEntity dicEntity) {
        DicEntity saveDicEntity = getDicEntityById(dicEntity.getId());
        FarmExceptionUtils.throwNullEx(saveDicEntity, I18n.msg("数据字典不存在:?", dicEntity.getId()));
        saveDicEntity.setId(dicEntity.getId());
        saveDicEntity.setCtime(dicEntity.getCtime());
        saveDicEntity.setEtime(dicEntity.getEtime());
        saveDicEntity.setEuserkey(dicEntity.getEuserkey());
        saveDicEntity.setCuserkey(dicEntity.getCuserkey());
        saveDicEntity.setState(dicEntity.getState());
        saveDicEntity.setNote(dicEntity.getNote());
        saveDicEntity.setName(dicEntity.getName());
        saveDicEntity.setKeyid(dicEntity.getKeyid());
        saveDicEntity.setType(dicEntity.getType());

        FarmDbFields.initUpdateBean(saveDicEntity, FarmUserContextLoader.getCurrentUser());
        dicEntityDao.update(saveDicEntity);
        return saveDicEntity;
    }

    @Transactional
    @Override
    public DicEntity getDicEntityById(String id) {
        return dicEntityDao.findById(id);
    }

    @Override
    public List<DicEntity> getDicEntitys(DataQuery query) {
        return dicEntityDao.queryData(query.setCount(false)).getData(DicEntity.class);
    }


    @Transactional
    @Override
    public DataResult searchDicEntity(DataQuery query) {
        DataResult result = dicEntityDao.queryData(query);
        return result;
    }

    @Override
    public int getDicEntityNum(DataQuery query) {
        return dicEntityDao.countData(query);
    }


    @Transactional
    @Override
    public void delDicEntity(String id) {
        //删除字典选项
        dicTypeDao.delete(DBRule.getInstance("ENTITYID", id, "=").getRules());
        dicEntityDao.deleteById(id);
    }

    @Override
    public int getNum(DataQuery query) {
        return dicEntityDao.countData(query);
    }

    @Override
    public DicEntity getDicEntityByKey(String key) {
        return dicEntityDao.queryOne(DBRuleList.getInstance().add(new DBRule("KEYID", key, "=")).toList());
    }

    @Override
    public Map<String, DicType> getTypes(String key) {
        DicEntity entity = dicEntityDao.queryOne(DBRuleList.getInstance().add(new DBRule("KEYID", key, "=")).toList());
        if (entity == null) {
            return Map.of();
        }
        List<DicType> types = dicTypeDao.queryData(DataQuery.getInstance().addRule(new DBRule("ENTITYID", entity.getId(), "=")).setCount(false)).getData(DicType.class);
        return types.stream().collect(Collectors.toMap(node -> node.getKeyid(), node -> node));
    }
    
    /*[tree：树形结构使用]
    @Transactional
    @Override
    public void moveTreeNode(List<String> sourceIds, String targetId) {
        for (String sourceId : sourceIds) {
            // 移动节点
            DicEntity node = getDicEntityById(sourceId);
            if (!"NONE".equals(targetId)) {
                DicEntity target = getDicEntityById(targetId);
                if (target.getTreecode().indexOf(node.getTreecode()) >= 0) {
                    throw new RuntimeException("不能够移动到其子节点下!");
                }
            }
            node.setParentid(targetId);
            dicEntityDao.update(node);
            // 构造所有树TREECODE
            List<DicEntity> list = dicEntityDao.findSubNodes(sourceId);
            for (DicEntity treenode : list) {
                initTreeCode(treenode.getId());
            }
        }
    }*/

    /**[tree：树形结构使用]
     * 构造treecode字段
     * @param treeNodeId
    private void initTreeCode(String treeNodeId) {
    DicEntity node = dicEntityDao.findById(treeNodeId);
    if (node.getParentid().equals("NONE")) {
    node.setTreecode(node.getId());
    } else {
    node.setTreecode(dicEntityDao.findById(node.getParentid()).getTreecode() + node.getId());
    }
    dicEntityDao.update(node);
    }
     */
    /* [tree：树形结构使用]
    @Transactional
    @Override
    public void autoSort(List<String> ids) {
        int sort = 0;
        for (String id : ids) {
            DicEntity node =  dicEntityDao.findById(id);
            if (sort == 0) {
                sort = node.getSortcode();
            }
            node.setSortcode(sort++);
            dicEntityDao.update(node);
        }
    }*/
}
