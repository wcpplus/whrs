package org.farm2.files.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.FarmDbFields;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.base.exception.FarmAppException;
import org.farm2.base.exception.FarmExceptionUtils;
import org.farm2.base.password.FarmPasswordEncoder;
import org.farm2.files.dao.ResourceFileRegisteDao;
import org.farm2.files.domain.ResourceFileRegiste;
import org.farm2.files.service.ResourceFileRegisteServiceInter;
import org.farm2.tools.bean.FarmBeanUtils;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.i18n.I18n;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**附件注册 
 * @author cbtg自动生成  2025-2-4 10:42:08 
 */
@Service
@Slf4j
public class ResourceFileRegisteServiceImpl implements ResourceFileRegisteServiceInter {


    @Autowired
    private ResourceFileRegisteDao resourceFileRegisteDao;

    @Transactional
    @Override
    public ResourceFileRegiste insertResourceFileRegisteEntity(ResourceFileRegiste resourceFileRegiste) {
        FarmDbFields.initInsertBean(resourceFileRegiste, FarmUserContextLoader.getCurrentUser());
        //FarmBeanUtils.runFunctionByBlank(resourceFileRegiste.getType(), "1", resourceFileRegiste::setType);
        resourceFileRegisteDao.insert(resourceFileRegiste);
       //[tree：树形结构使用]
       //initTreeCode(actions.getId());
        return resourceFileRegiste;
    }

    @Transactional
    @Override
    public ResourceFileRegiste editResourceFileRegisteEntity(ResourceFileRegiste resourceFileRegiste) {
        ResourceFileRegiste saveResourceFileRegiste = getResourceFileRegisteById(resourceFileRegiste.getId());
        FarmExceptionUtils.throwNullEx(saveResourceFileRegiste, I18n.msg("附件注册不存在:?", resourceFileRegiste.getId()));
        saveResourceFileRegiste.setId(resourceFileRegiste.getId());
        saveResourceFileRegiste.setCtime(resourceFileRegiste.getCtime());
        saveResourceFileRegiste.setAppid(resourceFileRegiste.getAppid());
        saveResourceFileRegiste.setType(resourceFileRegiste.getType());
        saveResourceFileRegiste.setFileid(resourceFileRegiste.getFileid());
        saveResourceFileRegiste.setNote(resourceFileRegiste.getNote());
         
        FarmDbFields.initUpdateBean(saveResourceFileRegiste, FarmUserContextLoader.getCurrentUser());
        resourceFileRegisteDao.update(saveResourceFileRegiste);
        return saveResourceFileRegiste;
    }

    @Transactional
    @Override
    public ResourceFileRegiste getResourceFileRegisteById(String id) {
        return resourceFileRegisteDao.findById(id);
    }

    @Override
    public List<ResourceFileRegiste> getResourceFileRegistes(DataQuery query) {
        return resourceFileRegisteDao.queryData(query.setCount(false)).getData(ResourceFileRegiste.class);
    }



    @Transactional
    @Override
    public DataResult searchResourceFileRegiste(DataQuery query) {
        DataResult result = resourceFileRegisteDao.queryData(query);
        return result;
    }

    @Override
    public int getResourceFileRegisteNum(DataQuery query) {
        return resourceFileRegisteDao.countData(query);
    }


    @Transactional
    @Override
    public void delResourceFileRegiste(String id) {
        /*[tree：树形结构使用]
        if ( resourceFileRegisteDao.findByParentId(id).size() > 0) {
            throw new RuntimeException("不能删除该节点，请先删除其子节点");
        }
        */
        resourceFileRegisteDao.deleteById(id);
    }
    
    @Override
    public int getNum(DataQuery query) {
        return  resourceFileRegisteDao.countData(query);
    }
    
    /*[tree：树形结构使用]
    @Transactional
    @Override
    public void moveTreeNode(List<String> sourceIds, String targetId) {
        for (String sourceId : sourceIds) {
            // 移动节点
            ResourceFileRegiste node = getResourceFileRegisteById(sourceId);
            if (!"NONE".equals(targetId)) {
                ResourceFileRegiste target = getResourceFileRegisteById(targetId);
                if (target.getTreecode().indexOf(node.getTreecode()) >= 0) {
                    throw new RuntimeException("不能够移动到其子节点下!");
                }
            }
            node.setParentid(targetId);
            resourceFileRegisteDao.update(node);
            // 构造所有树TREECODE
            List<ResourceFileRegiste> list = resourceFileRegisteDao.findSubNodes(sourceId);
            for (ResourceFileRegiste treenode : list) {
                initTreeCode(treenode.getId());
            }
        }
    }*/
    
     /**[tree：树形结构使用]
      * 构造treecode字段
     * @param treeNodeId
    private void initTreeCode(String treeNodeId) {
        ResourceFileRegiste node = resourceFileRegisteDao.findById(treeNodeId);
        if (node.getParentid().equals("NONE")) {
            node.setTreecode(node.getId());
        } else {
            node.setTreecode(resourceFileRegisteDao.findById(node.getParentid()).getTreecode() + node.getId());
        }
        resourceFileRegisteDao.update(node);
    }
     */
    /* [tree：树形结构使用]
    @Transactional
    @Override
    public void autoSort(List<String> ids) {
        int sort = 0;
        for (String id : ids) {
            ResourceFileRegiste node =  resourceFileRegisteDao.findById(id);
            if (sort == 0) {
                sort = node.getSortcode();
            }
            node.setSortcode(sort++);
            resourceFileRegisteDao.update(node);
        }
    }*/
}
