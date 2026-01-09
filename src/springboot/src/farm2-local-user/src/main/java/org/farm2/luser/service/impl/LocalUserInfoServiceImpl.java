package org.farm2.luser.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.FarmDbFields;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.base.exception.FarmAppException;
import org.farm2.base.exception.FarmExceptionUtils;
import org.farm2.base.password.FarmPasswordEncoder;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.files.utils.Farm2RegisteTypeEnum;
import org.farm2.luser.dao.LocalUserInfoDao;
import org.farm2.luser.domain.LocalUserInfo;
import org.farm2.luser.service.LocalUserInfoServiceInter;
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
 * 用户信息
 *
 * @author cbtg自动生成  2026-1-5 21:07:40
 */
@Service
@Slf4j
public class LocalUserInfoServiceImpl implements LocalUserInfoServiceInter {
    @Autowired
    private ResourceFileServiceInter resourceFileServiceImpl;

    @Autowired
    private LocalUserInfoDao localUserInfoDao;

    @Transactional
    @Override
    public LocalUserInfo insertLocalUserInfoEntity(LocalUserInfo localUserInfo) {
        FarmDbFields.initInsertBean(localUserInfo, FarmUserContextLoader.getCurrentUser());
        //FarmBeanUtils.runFunctionByBlank(localUserInfo.getType(), "1", localUserInfo::setType);
        localUserInfoDao.insert(localUserInfo);
        //[tree：树形结构使用]
        //initTreeCode(actions.getId());
        return localUserInfo;
    }

    @Transactional
    @Override
    public LocalUserInfo editLocalUserInfoEntity(LocalUserInfo localUserInfo) {
        LocalUserInfo saveLocalUserInfo = getLocalUserInfoById(localUserInfo.getId());
        FarmExceptionUtils.throwNullEx(saveLocalUserInfo, I18n.msg("用户信息不存在:?", localUserInfo.getId()));
        saveLocalUserInfo.setId(localUserInfo.getId());
        saveLocalUserInfo.setSex(localUserInfo.getSex());
        saveLocalUserInfo.setBirthdate(localUserInfo.getBirthdate());
        saveLocalUserInfo.setPhonecode(localUserInfo.getPhonecode());
        saveLocalUserInfo.setEmptime(localUserInfo.getEmptime());
        saveLocalUserInfo.setEmail(localUserInfo.getEmail());
        saveLocalUserInfo.setIdcode(localUserInfo.getIdcode());
        saveLocalUserInfo.setCitycode(localUserInfo.getCitycode());
        saveLocalUserInfo.setPan(localUserInfo.getPan());
        if (StringUtils.isNotBlank(localUserInfo.getContractfid())) {
            saveLocalUserInfo.setContractfid(localUserInfo.getContractfid());
            resourceFileServiceImpl.submit(localUserInfo.getContractfid(), Farm2RegisteTypeEnum.USER, saveLocalUserInfo.getUserkey());
        } else {
            resourceFileServiceImpl.cancel(localUserInfo.getContractfid(), Farm2RegisteTypeEnum.USER, saveLocalUserInfo.getUserkey());
            saveLocalUserInfo.setContractfid("");
        }
        if (StringUtils.isNotBlank(localUserInfo.getEqfid())) {
            saveLocalUserInfo.setEqfid(localUserInfo.getEqfid());
            resourceFileServiceImpl.submit(localUserInfo.getEqfid(), Farm2RegisteTypeEnum.USER, saveLocalUserInfo.getUserkey());
        } else {
            resourceFileServiceImpl.cancel(localUserInfo.getEqfid(), Farm2RegisteTypeEnum.USER, saveLocalUserInfo.getUserkey());
            saveLocalUserInfo.setEqfid("");
        }
        if (StringUtils.isNotBlank(localUserInfo.getDegfid())) {
            saveLocalUserInfo.setDegfid(localUserInfo.getDegfid());
            resourceFileServiceImpl.submit(localUserInfo.getDegfid(), Farm2RegisteTypeEnum.USER, saveLocalUserInfo.getUserkey());
        } else {
            resourceFileServiceImpl.cancel(localUserInfo.getDegfid(), Farm2RegisteTypeEnum.USER, saveLocalUserInfo.getUserkey());
            saveLocalUserInfo.setDegfid("");
        }
        if (StringUtils.isNotBlank(localUserInfo.getMerfid())) {
            saveLocalUserInfo.setMerfid(localUserInfo.getMerfid());
            resourceFileServiceImpl.submit(localUserInfo.getMerfid(), Farm2RegisteTypeEnum.USER, saveLocalUserInfo.getUserkey());
        } else {
            resourceFileServiceImpl.cancel(localUserInfo.getMerfid(), Farm2RegisteTypeEnum.USER, saveLocalUserInfo.getUserkey());
            saveLocalUserInfo.setMerfid("");
        }
        if (StringUtils.isNotBlank(localUserInfo.getIcardfid())) {
            saveLocalUserInfo.setIcardfid(localUserInfo.getIcardfid());
            resourceFileServiceImpl.submit(localUserInfo.getIcardfid(), Farm2RegisteTypeEnum.USER, saveLocalUserInfo.getUserkey());
        } else {
            resourceFileServiceImpl.cancel(localUserInfo.getIcardfid(), Farm2RegisteTypeEnum.USER, saveLocalUserInfo.getUserkey());
            saveLocalUserInfo.setIcardfid("");
        }
        //帮我计算setProcess为当前表单完善度，全部都填满为100
        // ✅ 自动计算完善度
        int totalFields = 13; // 参与计算的字段数量（见下方列表）
        int filledFields = countFilledFields(saveLocalUserInfo);
        int process = Math.round((float) filledFields / totalFields * 100);
        saveLocalUserInfo.setProcess(process);
        //saveLocalUserInfo.setUserkey(localUserInfo.getUserkey());

        FarmDbFields.initUpdateBean(saveLocalUserInfo, FarmUserContextLoader.getCurrentUser());
        localUserInfoDao.update(saveLocalUserInfo);
        return saveLocalUserInfo;
    }

    // 辅助方法：计算已填写的字段数
    private int countFilledFields(LocalUserInfo user) {
        int count = 0;
        // 1. sex
        if (user.getSex() != null && !user.getSex().trim().isEmpty()) count++;
        // 2. birthdate
        if (user.getBirthdate() != null) count++;
        // 3. phonecode
        if (user.getPhonecode() != null && !user.getPhonecode().trim().isEmpty()) count++;
        // 4. emptime
        if (user.getEmptime() != null) count++;
        // 5. email
        if (user.getEmail() != null && !user.getEmail().trim().isEmpty()) count++;
        // 6. idcode
        if (user.getIdcode() != null && !user.getIdcode().trim().isEmpty()) count++;
        // 7. citycode
        if (user.getCitycode() != null && !user.getCitycode().trim().isEmpty()) count++;
        // 8. pan (银行卡号)
        if (user.getPan() != null && !user.getPan().trim().isEmpty()) count++;
        // 9. contractfid
        if (user.getContractfid() != null && !user.getContractfid().trim().isEmpty()) count++;
        // 10. eqfid
        if (user.getEqfid() != null && !user.getEqfid().trim().isEmpty()) count++;
        // 11. degfid
        if (user.getDegfid() != null && !user.getDegfid().trim().isEmpty()) count++;
        // 12. merfid
        if (user.getMerfid() != null && !user.getMerfid().trim().isEmpty()) count++;
        // 13. icardfid
        if (user.getIcardfid() != null && !user.getIcardfid().trim().isEmpty()) count++;

        return count;
    }

    @Transactional
    @Override
    public LocalUserInfo getLocalUserInfoById(String id) {
        return localUserInfoDao.findById(id);
    }

    @Override
    public List<LocalUserInfo> getLocalUserInfos(DataQuery query) {
        return localUserInfoDao.queryData(query.setCount(false)).getData(LocalUserInfo.class);
    }


    @Transactional
    @Override
    public DataResult searchLocalUserInfo(DataQuery query) {
        DataResult result = localUserInfoDao.queryData(query);
        return result;
    }

    @Override
    public int getLocalUserInfoNum(DataQuery query) {
        return localUserInfoDao.countData(query);
    }


    @Transactional
    @Override
    public void delLocalUserInfo(String id) {
        /*[tree：树形结构使用]
        if ( localUserInfoDao.findByParentId(id).size() > 0) {
            throw new RuntimeException("不能删除该节点，请先删除其子节点");
        }
        */
        localUserInfoDao.deleteById(id);
    }

    @Override
    public int getNum(DataQuery query) {
        return localUserInfoDao.countData(query);
    }

    @Override
    public LocalUserInfo initUserInfoByUserKey(String userkey) {
        LocalUserInfo userinfo = localUserInfoDao.queryOne(DBRuleList.getInstance().add(new DBRule("USERKEY", userkey, "=")).toList());
        if (userinfo == null) {
            userinfo = new LocalUserInfo();
            userinfo.setUserkey(userkey);
            userinfo.setProcess(0);
            localUserInfoDao.insert(userinfo);
        }
        return userinfo;
    }

    @Override
    public LocalUserInfo getLocalUserInfoByUserKey(String loginname) {

        LocalUserInfo userinfo = localUserInfoDao.queryOne(DBRuleList.getInstance().add(new DBRule("USERKEY", loginname, "=")).toList());

        return userinfo;
    }
    
    /*[tree：树形结构使用]
    @Transactional
    @Override
    public void moveTreeNode(List<String> sourceIds, String targetId) {
        for (String sourceId : sourceIds) {
            // 移动节点
            LocalUserInfo node = getLocalUserInfoById(sourceId);
            if (!"NONE".equals(targetId)) {
                LocalUserInfo target = getLocalUserInfoById(targetId);
                if (target.getTreecode().indexOf(node.getTreecode()) >= 0) {
                    throw new RuntimeException("不能够移动到其子节点下!");
                }
            }
            node.setParentid(targetId);
            localUserInfoDao.update(node);
            // 构造所有树TREECODE
            List<LocalUserInfo> list = localUserInfoDao.findSubNodes(sourceId);
            for (LocalUserInfo treenode : list) {
                initTreeCode(treenode.getId());
            }
        }
    }*/

    /**[tree：树形结构使用]
     * 构造treecode字段
     * @param treeNodeId
    private void initTreeCode(String treeNodeId) {
    LocalUserInfo node = localUserInfoDao.findById(treeNodeId);
    if (node.getParentid().equals("NONE")) {
    node.setTreecode(node.getId());
    } else {
    node.setTreecode(localUserInfoDao.findById(node.getParentid()).getTreecode() + node.getId());
    }
    localUserInfoDao.update(node);
    }
     */
    /* [tree：树形结构使用]
    @Transactional
    @Override
    public void autoSort(List<String> ids) {
        int sort = 0;
        for (String id : ids) {
            LocalUserInfo node =  localUserInfoDao.findById(id);
            if (sort == 0) {
                sort = node.getSortcode();
            }
            node.setSortcode(sort++);
            localUserInfoDao.update(node);
        }
    }*/
}
