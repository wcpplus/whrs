package org.farm2.auth.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.farm2.base.db.FarmDbFields;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.base.exception.FarmExceptionUtils;
import org.farm2.auth.dao.DicTypeDao;
import org.farm2.auth.domain.DicType;
import org.farm2.auth.service.DicTypeServiceInter;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.i18n.I18n;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 字典类型
 *
 * @author cbtg自动生成  2025-1-7 15:27:42
 */
@Service
@Slf4j
public class DicTypeServiceImpl implements DicTypeServiceInter {


    @Autowired
    private DicTypeDao dicTypeDao;

    @Transactional
    @Override
    public DicType insertDicTypeEntity(DicType dicType) {
        FarmDbFields.initInsertBean(dicType, FarmUserContextLoader.getCurrentUser());
        //FarmBeanUtils.runFunctionByBlank(dicType.getType(), "1", dicType::setType);
        dicTypeDao.insert(dicType);
        //[tree：树形结构使用]
        //initTreeCode(actions.getId());
        return dicType;
    }

    @Transactional
    @Override
    public DicType editDicTypeEntity(DicType dicType) {
        DicType saveDicType = getDicTypeById(dicType.getId());
        FarmExceptionUtils.throwNullEx(saveDicType, I18n.msg("字典类型不存在:?", dicType.getId()));
        saveDicType.setState(dicType.getState());
        saveDicType.setNote(dicType.getNote());
        saveDicType.setName(dicType.getName());
        saveDicType.setKeyid(dicType.getKeyid());
        saveDicType.setSortcode(dicType.getSortcode());
        FarmDbFields.initUpdateBean(saveDicType, FarmUserContextLoader.getCurrentUser());
        dicTypeDao.update(saveDicType);
        return saveDicType;
    }

    @Transactional
    @Override
    public DicType getDicTypeById(String id) {
        return dicTypeDao.findById(id);
    }

    @Override
    public List<DicType> getDicTypes(DataQuery query) {
        return dicTypeDao.queryData(query.setCount(false)).getData(DicType.class);
    }


    @Transactional
    @Override
    public DataResult searchDicType(DataQuery query) {
        DataResult result = dicTypeDao.queryData(query);
        return result;
    }

    @Override
    public int getDicTypeNum(DataQuery query) {
        return dicTypeDao.countData(query);
    }


    @Transactional
    @Override
    public void delDicType(String id) {
        /*[tree：树形结构使用]
        if ( dicTypeDao.findByParentId(id).size() > 0) {
            throw new RuntimeException("不能删除该节点，请先删除其子节点");
        }
        */
        dicTypeDao.deleteById(id);
    }

    @Override
    public int getNum(DataQuery query) {
        return dicTypeDao.countData(query);
    }
    
    /*[tree：树形结构使用]
    @Transactional
    @Override
    public void moveTreeNode(List<String> sourceIds, String targetId) {
        for (String sourceId : sourceIds) {
            // 移动节点
            DicType node = getDicTypeById(sourceId);
            if (!"NONE".equals(targetId)) {
                DicType target = getDicTypeById(targetId);
                if (target.getTreecode().indexOf(node.getTreecode()) >= 0) {
                    throw new RuntimeException("不能够移动到其子节点下!");
                }
            }
            node.setParentid(targetId);
            dicTypeDao.update(node);
            // 构造所有树TREECODE
            List<DicType> list = dicTypeDao.findSubNodes(sourceId);
            for (DicType treenode : list) {
                initTreeCode(treenode.getId());
            }
        }
    }*/

    /**[tree：树形结构使用]
     * 构造treecode字段
     * @param treeNodeId
    private void initTreeCode(String treeNodeId) {
    DicType node = dicTypeDao.findById(treeNodeId);
    if (node.getParentid().equals("NONE")) {
    node.setTreecode(node.getId());
    } else {
    node.setTreecode(dicTypeDao.findById(node.getParentid()).getTreecode() + node.getId());
    }
    dicTypeDao.update(node);
    }
     */
    /* [tree：树形结构使用]
    @Transactional
    @Override
    public void autoSort(List<String> ids) {
        int sort = 0;
        for (String id : ids) {
            DicType node =  dicTypeDao.findById(id);
            if (sort == 0) {
                sort = node.getSortcode();
            }
            node.setSortcode(sort++);
            dicTypeDao.update(node);
        }
    }*/
}
