package org.farm2.salary.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.FarmDbFields;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.base.exception.FarmAppException;
import org.farm2.base.exception.FarmExceptionUtils;
import org.farm2.base.password.FarmPasswordEncoder;
import org.farm2.luser.domain.LocalUser;
import org.farm2.luser.service.LocalUserServiceInter;
import org.farm2.salary.dao.SalaryUserDao;
import org.farm2.salary.domain.SalaryUser;
import org.farm2.salary.service.SalaryUserServiceInter;
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
 * 用户薪酬WHRS_SALARY_USER
 *
 * @author cbtg自动生成  2026-1-8 11:14:26
 */
@Service
@Slf4j
public class SalaryUserServiceImpl implements SalaryUserServiceInter {

    @Autowired
    private LocalUserServiceInter localUserServiceImpl;
    @Autowired
    private SalaryUserDao salaryUserDao;

    @Transactional
    @Override
    public SalaryUser insertSalaryUserEntity(SalaryUser salaryUser) {
        FarmDbFields.initInsertBean(salaryUser, FarmUserContextLoader.getCurrentUser());
        //FarmBeanUtils.runFunctionByBlank(salaryUser.getType(), "1", salaryUser::setType);
        salaryUserDao.insert(salaryUser);
        //[tree：树形结构使用]
        //initTreeCode(actions.getId());
        return salaryUser;
    }

    @Transactional
    @Override
    public SalaryUser editSalaryUserEntity(SalaryUser salaryUser) {
        SalaryUser saveSalaryUser = getSalaryUserById(salaryUser.getId());
        FarmExceptionUtils.throwNullEx(saveSalaryUser, I18n.msg("用户薪酬WHRS_SALARY_USER不存在:?", salaryUser.getId()));
        saveSalaryUser.setId(salaryUser.getId());
        saveSalaryUser.setUserkey(salaryUser.getUserkey());
        saveSalaryUser.setUsername(salaryUser.getUsername());
        saveSalaryUser.setNote(salaryUser.getNote());
        saveSalaryUser.setState(salaryUser.getState());
        saveSalaryUser.setTemplateid(salaryUser.getTemplateid());

        FarmDbFields.initUpdateBean(saveSalaryUser, FarmUserContextLoader.getCurrentUser());
        salaryUserDao.update(saveSalaryUser);
        return saveSalaryUser;
    }

    @Transactional
    @Override
    public SalaryUser getSalaryUserById(String id) {
        return salaryUserDao.findById(id);
    }

    @Override
    public List<SalaryUser> getSalaryUsers(DataQuery query) {
        return salaryUserDao.queryData(query.setCount(false)).getData(SalaryUser.class);
    }


    @Transactional
    @Override
    public DataResult searchSalaryUser(DataQuery query) {
        DataResult result = salaryUserDao.queryData(query);
        return result;
    }

    @Override
    public int getSalaryUserNum(DataQuery query) {
        return salaryUserDao.countData(query);
    }


    @Transactional
    @Override
    public void delSalaryUser(String id) {
        /*[tree：树形结构使用]
        if ( salaryUserDao.findByParentId(id).size() > 0) {
            throw new RuntimeException("不能删除该节点，请先删除其子节点");
        }
        */
        salaryUserDao.deleteById(id);
    }

    @Override
    public int getNum(DataQuery query) {
        return salaryUserDao.countData(query);
    }

    @Transactional
    @Override
    public SalaryUser initSalaryUser(String loginname) {
        SalaryUser salaryUser = salaryUserDao.queryOne(DBRuleList.getInstance().add(new DBRule("USERKEY", loginname, "=")).toList());
        if (salaryUser == null) {
            LocalUser user = localUserServiceImpl.getLocalUserByLoginName(loginname);
            salaryUser = new SalaryUser();
            salaryUser.setUserkey(user.getLoginname());
            salaryUser.setUsername(user.getName());
            salaryUser.setState("1");
            salaryUserDao.insert(salaryUser);
        }
        return salaryUser;
    }
    
    /*[tree：树形结构使用]
    @Transactional
    @Override
    public void moveTreeNode(List<String> sourceIds, String targetId) {
        for (String sourceId : sourceIds) {
            // 移动节点
            SalaryUser node = getSalaryUserById(sourceId);
            if (!"NONE".equals(targetId)) {
                SalaryUser target = getSalaryUserById(targetId);
                if (target.getTreecode().indexOf(node.getTreecode()) >= 0) {
                    throw new RuntimeException("不能够移动到其子节点下!");
                }
            }
            node.setParentid(targetId);
            salaryUserDao.update(node);
            // 构造所有树TREECODE
            List<SalaryUser> list = salaryUserDao.findSubNodes(sourceId);
            for (SalaryUser treenode : list) {
                initTreeCode(treenode.getId());
            }
        }
    }*/

    /**[tree：树形结构使用]
     * 构造treecode字段
     * @param treeNodeId
    private void initTreeCode(String treeNodeId) {
    SalaryUser node = salaryUserDao.findById(treeNodeId);
    if (node.getParentid().equals("NONE")) {
    node.setTreecode(node.getId());
    } else {
    node.setTreecode(salaryUserDao.findById(node.getParentid()).getTreecode() + node.getId());
    }
    salaryUserDao.update(node);
    }
     */
    /* [tree：树形结构使用]
    @Transactional
    @Override
    public void autoSort(List<String> ids) {
        int sort = 0;
        for (String id : ids) {
            SalaryUser node =  salaryUserDao.findById(id);
            if (sort == 0) {
                sort = node.getSortcode();
            }
            node.setSortcode(sort++);
            salaryUserDao.update(node);
        }
    }*/
}
