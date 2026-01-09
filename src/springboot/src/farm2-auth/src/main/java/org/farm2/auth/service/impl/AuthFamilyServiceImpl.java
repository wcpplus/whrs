package org.farm2.auth.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.FarmDbFields;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.base.exception.FarmAppException;
import org.farm2.base.exception.FarmExceptionUtils;
import org.farm2.base.password.FarmPasswordEncoder;
import org.farm2.auth.dao.AuthFamilyDao;
import org.farm2.auth.domain.AuthFamily;
import org.farm2.auth.service.AuthFamilyServiceInter;
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

/**岗位族 
 * @author cbtg自动生成  2026-1-5 10:12:10 
 */
@Service
@Slf4j
public class AuthFamilyServiceImpl implements AuthFamilyServiceInter {


    @Autowired
    private AuthFamilyDao authFamilyDao;

    @Transactional
    @Override
    public AuthFamily insertAuthFamilyEntity(AuthFamily authFamily) {
        FarmDbFields.initInsertBean(authFamily, FarmUserContextLoader.getCurrentUser());
        //FarmBeanUtils.runFunctionByBlank(authFamily.getType(), "1", authFamily::setType);
        authFamilyDao.insert(authFamily);
       //[tree：树形结构使用]
       //initTreeCode(actions.getId());
        return authFamily;
    }

    @Transactional
    @Override
    public AuthFamily editAuthFamilyEntity(AuthFamily authFamily) {
        AuthFamily saveAuthFamily = getAuthFamilyById(authFamily.getId());
        FarmExceptionUtils.throwNullEx(saveAuthFamily, I18n.msg("岗位族不存在:?", authFamily.getId()));
        saveAuthFamily.setId(authFamily.getId());
        saveAuthFamily.setKeyid(authFamily.getKeyid());
        saveAuthFamily.setName(authFamily.getName());
        saveAuthFamily.setNote(authFamily.getNote());
         
        FarmDbFields.initUpdateBean(saveAuthFamily, FarmUserContextLoader.getCurrentUser());
        authFamilyDao.update(saveAuthFamily);
        return saveAuthFamily;
    }

    @Transactional
    @Override
    public AuthFamily getAuthFamilyById(String id) {
        return authFamilyDao.findById(id);
    }

    @Override
    public List<AuthFamily> getAuthFamilys(DataQuery query) {
        return authFamilyDao.queryData(query.setCount(false)).getData(AuthFamily.class);
    }



    @Transactional
    @Override
    public DataResult searchAuthFamily(DataQuery query) {
        DataResult result = authFamilyDao.queryData(query);
        return result;
    }

    @Override
    public int getAuthFamilyNum(DataQuery query) {
        return authFamilyDao.countData(query);
    }


    @Transactional
    @Override
    public void delAuthFamily(String id) {
        /*[tree：树形结构使用]
        if ( authFamilyDao.findByParentId(id).size() > 0) {
            throw new RuntimeException("不能删除该节点，请先删除其子节点");
        }
        */
        authFamilyDao.deleteById(id);
    }
    
    @Override
    public int getNum(DataQuery query) {
        return  authFamilyDao.countData(query);
    }
    
    /*[tree：树形结构使用]
    @Transactional
    @Override
    public void moveTreeNode(List<String> sourceIds, String targetId) {
        for (String sourceId : sourceIds) {
            // 移动节点
            AuthFamily node = getAuthFamilyById(sourceId);
            if (!"NONE".equals(targetId)) {
                AuthFamily target = getAuthFamilyById(targetId);
                if (target.getTreecode().indexOf(node.getTreecode()) >= 0) {
                    throw new RuntimeException("不能够移动到其子节点下!");
                }
            }
            node.setParentid(targetId);
            authFamilyDao.update(node);
            // 构造所有树TREECODE
            List<AuthFamily> list = authFamilyDao.findSubNodes(sourceId);
            for (AuthFamily treenode : list) {
                initTreeCode(treenode.getId());
            }
        }
    }*/
    
     /**[tree：树形结构使用]
      * 构造treecode字段
     * @param treeNodeId
    private void initTreeCode(String treeNodeId) {
        AuthFamily node = authFamilyDao.findById(treeNodeId);
        if (node.getParentid().equals("NONE")) {
            node.setTreecode(node.getId());
        } else {
            node.setTreecode(authFamilyDao.findById(node.getParentid()).getTreecode() + node.getId());
        }
        authFamilyDao.update(node);
    }
     */
    /* [tree：树形结构使用]
    @Transactional
    @Override
    public void autoSort(List<String> ids) {
        int sort = 0;
        for (String id : ids) {
            AuthFamily node =  authFamilyDao.findById(id);
            if (sort == 0) {
                sort = node.getSortcode();
            }
            node.setSortcode(sort++);
            authFamilyDao.update(node);
        }
    }*/
}
