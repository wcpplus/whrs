package org.farm2.auth.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.farm2.auth.dao.AuthPostPtypeDao;
import org.farm2.auth.domain.AuthPostPtype;
import org.farm2.base.db.FarmDbFields;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.base.exception.FarmAppException;
import org.farm2.base.exception.FarmExceptionUtils;
import org.farm2.base.password.FarmPasswordEncoder;
import org.farm2.auth.dao.AuthPtypeDao;
import org.farm2.auth.domain.AuthPtype;
import org.farm2.auth.service.AuthPtypeServiceInter;
import org.farm2.tools.bean.FarmBeanUtils;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.DBRule;
import org.farm2.tools.i18n.I18n;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色类别
 *
 * @author cbtg自动生成  2025-10-29 11:55:20
 */
@Service
@Slf4j
public class AuthPtypeServiceImpl implements AuthPtypeServiceInter {

    @Autowired
    private AuthPostPtypeDao authPostPtypeDao;
    @Autowired
    private AuthPtypeDao authPtypeDao;

    @Transactional
    @Override
    public AuthPtype insertAuthPtypeEntity(AuthPtype authPtype) {
        FarmDbFields.initInsertBean(authPtype, FarmUserContextLoader.getCurrentUser());
        //FarmBeanUtils.runFunctionByBlank(authPtype.getType(), "1", authPtype::setType);
        authPtypeDao.insert(authPtype);
        //[tree：树形结构使用]
        //initTreeCode(actions.getId());
        return authPtype;
    }

    @Transactional
    @Override
    public AuthPtype editAuthPtypeEntity(AuthPtype authPtype) {
        AuthPtype saveAuthPtype = getAuthPtypeById(authPtype.getId());
        FarmExceptionUtils.throwNullEx(saveAuthPtype, I18n.msg("角色类别不存在:?", authPtype.getId()));
        saveAuthPtype.setId(authPtype.getId());
        saveAuthPtype.setCtime(authPtype.getCtime());
        saveAuthPtype.setEtime(authPtype.getEtime());
        saveAuthPtype.setEuserkey(authPtype.getEuserkey());
        saveAuthPtype.setCuserkey(authPtype.getCuserkey());
        saveAuthPtype.setState(authPtype.getState());
        saveAuthPtype.setNote(authPtype.getNote());
        saveAuthPtype.setName(authPtype.getName());
        saveAuthPtype.setSortcode(authPtype.getSortcode());

        FarmDbFields.initUpdateBean(saveAuthPtype, FarmUserContextLoader.getCurrentUser());
        authPtypeDao.update(saveAuthPtype);
        return saveAuthPtype;
    }

    @Transactional
    @Override
    public AuthPtype getAuthPtypeById(String id) {
        return authPtypeDao.findById(id);
    }

    @Override
    public List<AuthPtype> getAuthPtypes(DataQuery query) {
        return authPtypeDao.queryData(query.setCount(false)).getData(AuthPtype.class);
    }


    @Transactional
    @Override
    public DataResult searchAuthPtype(DataQuery query) {
        DataResult result = authPtypeDao.queryData(query);
        return result;
    }

    @Override
    public int getAuthPtypeNum(DataQuery query) {
        return authPtypeDao.countData(query);
    }


    @Transactional
    @Override
    public void delAuthPtype(String id) {
        /*[tree：树形结构使用]
        if ( authPtypeDao.findByParentId(id).size() > 0) {
            throw new RuntimeException("不能删除该节点，请先删除其子节点");
        }
        */
        authPtypeDao.deleteById(id);
    }

    @Override
    public int getNum(DataQuery query) {
        return authPtypeDao.countData(query);
    }

    @Override
    public Set<String> getPostIds(String ptypeId, int num) {
        List<AuthPostPtype> tagCategorys = authPostPtypeDao.queryData(DataQuery.getInstance().setPageSize(num).addRule(new DBRule("PTYPEID", ptypeId, "=")).setCount(false)).getData(AuthPostPtype.class);
        return tagCategorys.stream().map(node -> node.getPostid()).collect(Collectors.toSet());
    }

    @Override
    public Integer getPtypeNum(String postId) {
        return authPostPtypeDao.countData(DataQuery.getInstance().addRule(new DBRule("POSTID", postId, "=")));
    }
    
    /*[tree：树形结构使用]
    @Transactional
    @Override
    public void moveTreeNode(List<String> sourceIds, String targetId) {
        for (String sourceId : sourceIds) {
            // 移动节点
            AuthPtype node = getAuthPtypeById(sourceId);
            if (!"NONE".equals(targetId)) {
                AuthPtype target = getAuthPtypeById(targetId);
                if (target.getTreecode().indexOf(node.getTreecode()) >= 0) {
                    throw new RuntimeException("不能够移动到其子节点下!");
                }
            }
            node.setParentid(targetId);
            authPtypeDao.update(node);
            // 构造所有树TREECODE
            List<AuthPtype> list = authPtypeDao.findSubNodes(sourceId);
            for (AuthPtype treenode : list) {
                initTreeCode(treenode.getId());
            }
        }
    }*/

    /**[tree：树形结构使用]
     * 构造treecode字段
     * @param treeNodeId
    private void initTreeCode(String treeNodeId) {
    AuthPtype node = authPtypeDao.findById(treeNodeId);
    if (node.getParentid().equals("NONE")) {
    node.setTreecode(node.getId());
    } else {
    node.setTreecode(authPtypeDao.findById(node.getParentid()).getTreecode() + node.getId());
    }
    authPtypeDao.update(node);
    }
     */
    /* [tree：树形结构使用]
    @Transactional
    @Override
    public void autoSort(List<String> ids) {
        int sort = 0;
        for (String id : ids) {
            AuthPtype node =  authPtypeDao.findById(id);
            if (sort == 0) {
                sort = node.getSortcode();
            }
            node.setSortcode(sort++);
            authPtypeDao.update(node);
        }
    }*/
}
