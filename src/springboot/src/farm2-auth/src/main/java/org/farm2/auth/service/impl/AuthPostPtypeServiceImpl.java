package org.farm2.auth.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.FarmDbFields;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.base.exception.FarmAppException;
import org.farm2.base.exception.FarmExceptionUtils;
import org.farm2.base.password.FarmPasswordEncoder;
import org.farm2.auth.dao.AuthPostPtypeDao;
import org.farm2.auth.domain.AuthPostPtype;
import org.farm2.auth.service.AuthPostPtypeServiceInter;
import org.farm2.tools.bean.FarmBeanUtils;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.DBRule;
import org.farm2.tools.db.commons.DBRuleList;
import org.farm2.tools.i18n.I18n;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 角色类别关系
 *
 * @author cbtg自动生成  2025-10-29 11:56:43
 */
@Service
@Slf4j
public class AuthPostPtypeServiceImpl implements AuthPostPtypeServiceInter {


    @Autowired
    private AuthPostPtypeDao authPostPtypeDao;

    @Transactional
    @Override
    public AuthPostPtype insertAuthPostPtypeEntity(AuthPostPtype authPostPtype) {
        authPostPtypeDao.delete(DBRuleList.getInstance()
                .add(new DBRule("POSTID", authPostPtype.getPostid(), "="))
                .add(new DBRule("PTYPEID", authPostPtype.getPtypeid(), "="))
                .toList());
        FarmDbFields.initInsertBean(authPostPtype, FarmUserContextLoader.getCurrentUser());
        //FarmBeanUtils.runFunctionByBlank(authPostPtype.getType(), "1", authPostPtype::setType);
        authPostPtypeDao.insert(authPostPtype);
        //[tree：树形结构使用]
        //initTreeCode(actions.getId());
        return authPostPtype;
    }

    @Transactional
    @Override
    public AuthPostPtype editAuthPostPtypeEntity(AuthPostPtype authPostPtype) {
        AuthPostPtype saveAuthPostPtype = getAuthPostPtypeById(authPostPtype.getId());
        FarmExceptionUtils.throwNullEx(saveAuthPostPtype, I18n.msg("角色类别关系不存在:?", authPostPtype.getId()));
        saveAuthPostPtype.setId(authPostPtype.getId());
        saveAuthPostPtype.setPostid(authPostPtype.getPostid());
        saveAuthPostPtype.setPtypeid(authPostPtype.getPtypeid());

        FarmDbFields.initUpdateBean(saveAuthPostPtype, FarmUserContextLoader.getCurrentUser());
        authPostPtypeDao.update(saveAuthPostPtype);
        return saveAuthPostPtype;
    }

    @Transactional
    @Override
    public AuthPostPtype getAuthPostPtypeById(String id) {
        return authPostPtypeDao.findById(id);
    }

    @Override
    public List<AuthPostPtype> getAuthPostPtypes(DataQuery query) {
        return authPostPtypeDao.queryData(query.setCount(false)).getData(AuthPostPtype.class);
    }


    @Transactional
    @Override
    public DataResult searchAuthPostPtype(DataQuery query) {
        DataResult result = authPostPtypeDao.queryData(query);
        return result;
    }

    @Override
    public int getAuthPostPtypeNum(DataQuery query) {
        return authPostPtypeDao.countData(query);
    }


    @Transactional
    @Override
    public void delAuthPostPtype(String id) {
        /*[tree：树形结构使用]
        if ( authPostPtypeDao.findByParentId(id).size() > 0) {
            throw new RuntimeException("不能删除该节点，请先删除其子节点");
        }
        */
        authPostPtypeDao.deleteById(id);
    }

    @Override
    public int getNum(DataQuery query) {
        return authPostPtypeDao.countData(query);
    }

    @Transactional
    @Override
    public void bind(String postId, String typeId) {
        AuthPostPtype pptype = new AuthPostPtype();
        pptype.setPtypeid(typeId);
        pptype.setPostid(postId);
        insertAuthPostPtypeEntity(pptype);
    }
    
    /*[tree：树形结构使用]
    @Transactional
    @Override
    public void moveTreeNode(List<String> sourceIds, String targetId) {
        for (String sourceId : sourceIds) {
            // 移动节点
            AuthPostPtype node = getAuthPostPtypeById(sourceId);
            if (!"NONE".equals(targetId)) {
                AuthPostPtype target = getAuthPostPtypeById(targetId);
                if (target.getTreecode().indexOf(node.getTreecode()) >= 0) {
                    throw new RuntimeException("不能够移动到其子节点下!");
                }
            }
            node.setParentid(targetId);
            authPostPtypeDao.update(node);
            // 构造所有树TREECODE
            List<AuthPostPtype> list = authPostPtypeDao.findSubNodes(sourceId);
            for (AuthPostPtype treenode : list) {
                initTreeCode(treenode.getId());
            }
        }
    }*/

    /**[tree：树形结构使用]
     * 构造treecode字段
     * @param treeNodeId
    private void initTreeCode(String treeNodeId) {
    AuthPostPtype node = authPostPtypeDao.findById(treeNodeId);
    if (node.getParentid().equals("NONE")) {
    node.setTreecode(node.getId());
    } else {
    node.setTreecode(authPostPtypeDao.findById(node.getParentid()).getTreecode() + node.getId());
    }
    authPostPtypeDao.update(node);
    }
     */
    /* [tree：树形结构使用]
    @Transactional
    @Override
    public void autoSort(List<String> ids) {
        int sort = 0;
        for (String id : ids) {
            AuthPostPtype node =  authPostPtypeDao.findById(id);
            if (sort == 0) {
                sort = node.getSortcode();
            }
            node.setSortcode(sort++);
            authPostPtypeDao.update(node);
        }
    }*/
}
