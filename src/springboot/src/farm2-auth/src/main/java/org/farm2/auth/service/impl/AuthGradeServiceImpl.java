package org.farm2.auth.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.FarmDbFields;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.base.exception.FarmAppException;
import org.farm2.base.exception.FarmExceptionUtils;
import org.farm2.base.password.FarmPasswordEncoder;
import org.farm2.auth.dao.AuthGradeDao;
import org.farm2.auth.domain.AuthGrade;
import org.farm2.auth.service.AuthGradeServiceInter;
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

/**职级 
 * @author cbtg自动生成  2026-1-5 11:41:21 
 */
@Service
@Slf4j
public class AuthGradeServiceImpl implements AuthGradeServiceInter {


    @Autowired
    private AuthGradeDao authGradeDao;

    @Transactional
    @Override
    public AuthGrade insertAuthGradeEntity(AuthGrade authGrade) {
        FarmDbFields.initInsertBean(authGrade, FarmUserContextLoader.getCurrentUser());
        //FarmBeanUtils.runFunctionByBlank(authGrade.getType(), "1", authGrade::setType);
        authGradeDao.insert(authGrade);
       //[tree：树形结构使用]
       //initTreeCode(actions.getId());
        return authGrade;
    }

    @Transactional
    @Override
    public AuthGrade editAuthGradeEntity(AuthGrade authGrade) {
        AuthGrade saveAuthGrade = getAuthGradeById(authGrade.getId());
        FarmExceptionUtils.throwNullEx(saveAuthGrade, I18n.msg("职级不存在:?", authGrade.getId()));
        saveAuthGrade.setId(authGrade.getId());
        saveAuthGrade.setName(authGrade.getName());
        saveAuthGrade.setKeyid(authGrade.getKeyid());
        saveAuthGrade.setTracktype(authGrade.getTracktype());
        saveAuthGrade.setSortcode(authGrade.getSortcode());
        saveAuthGrade.setMinsalary(authGrade.getMinsalary());
        saveAuthGrade.setMaxsalary(authGrade.getMaxsalary());
         
        FarmDbFields.initUpdateBean(saveAuthGrade, FarmUserContextLoader.getCurrentUser());
        authGradeDao.update(saveAuthGrade);
        return saveAuthGrade;
    }

    @Transactional
    @Override
    public AuthGrade getAuthGradeById(String id) {
        return authGradeDao.findById(id);
    }

    @Override
    public List<AuthGrade> getAuthGrades(DataQuery query) {
        return authGradeDao.queryData(query.setCount(false)).getData(AuthGrade.class);
    }



    @Transactional
    @Override
    public DataResult searchAuthGrade(DataQuery query) {
        DataResult result = authGradeDao.queryData(query);
        return result;
    }

    @Override
    public int getAuthGradeNum(DataQuery query) {
        return authGradeDao.countData(query);
    }


    @Transactional
    @Override
    public void delAuthGrade(String id) {
        /*[tree：树形结构使用]
        if ( authGradeDao.findByParentId(id).size() > 0) {
            throw new RuntimeException("不能删除该节点，请先删除其子节点");
        }
        */
        authGradeDao.deleteById(id);
    }
    
    @Override
    public int getNum(DataQuery query) {
        return  authGradeDao.countData(query);
    }
    
    /*[tree：树形结构使用]
    @Transactional
    @Override
    public void moveTreeNode(List<String> sourceIds, String targetId) {
        for (String sourceId : sourceIds) {
            // 移动节点
            AuthGrade node = getAuthGradeById(sourceId);
            if (!"NONE".equals(targetId)) {
                AuthGrade target = getAuthGradeById(targetId);
                if (target.getTreecode().indexOf(node.getTreecode()) >= 0) {
                    throw new RuntimeException("不能够移动到其子节点下!");
                }
            }
            node.setParentid(targetId);
            authGradeDao.update(node);
            // 构造所有树TREECODE
            List<AuthGrade> list = authGradeDao.findSubNodes(sourceId);
            for (AuthGrade treenode : list) {
                initTreeCode(treenode.getId());
            }
        }
    }*/
    
     /**[tree：树形结构使用]
      * 构造treecode字段
     * @param treeNodeId
    private void initTreeCode(String treeNodeId) {
        AuthGrade node = authGradeDao.findById(treeNodeId);
        if (node.getParentid().equals("NONE")) {
            node.setTreecode(node.getId());
        } else {
            node.setTreecode(authGradeDao.findById(node.getParentid()).getTreecode() + node.getId());
        }
        authGradeDao.update(node);
    }
     */
    /* [tree：树形结构使用]
    @Transactional
    @Override
    public void autoSort(List<String> ids) {
        int sort = 0;
        for (String id : ids) {
            AuthGrade node =  authGradeDao.findById(id);
            if (sort == 0) {
                sort = node.getSortcode();
            }
            node.setSortcode(sort++);
            authGradeDao.update(node);
        }
    }*/
}
