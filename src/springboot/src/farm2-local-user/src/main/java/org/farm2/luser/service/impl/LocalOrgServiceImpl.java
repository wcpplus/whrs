package org.farm2.luser.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.FarmDbFields;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.base.exception.FarmExceptionUtils;
import org.farm2.luser.dao.LocalOrgDao;
import org.farm2.luser.domain.LocalOrg;
import org.farm2.luser.service.LocalOrgServiceInter;
import org.farm2.tools.base.FarmStringUtils;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.DBRule;
import org.farm2.tools.db.commons.DBRuleList;
import org.farm2.tools.db.commons.WhereInRule;
import org.farm2.tools.i18n.I18n;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 组织机构
 *
 * @author cbtg自动生成  2025-1-2 22:01:57
 */
@Service
@Slf4j
public class LocalOrgServiceImpl implements LocalOrgServiceInter {


    @Autowired
    private LocalOrgDao localOrgDao;

    @Transactional
    @Override
    public LocalOrg insertLocalOrgEntity(LocalOrg localOrg) {
        FarmDbFields.initInsertBean(localOrg, FarmUserContextLoader.getCurrentUser());
        //FarmBeanUtils.runFunctionByBlank(localOrg.getType(), "1", localOrg::setType);
        localOrgDao.insert(localOrg);
        if (StringUtils.isBlank(localOrg.getCode())) {
            localOrg.setCode(localOrg.getId());
            localOrgDao.update(localOrg);//更新机构code
        }
        initTreeCode(localOrg.getId());
        return localOrg;
    }

    @Transactional
    @Override
    public LocalOrg editLocalOrgEntity(LocalOrg localOrg) {
        LocalOrg saveLocalOrg = getLocalOrgById(localOrg.getId());
        FarmExceptionUtils.throwNullEx(saveLocalOrg, I18n.msg("组织机构不存在:?", localOrg.getId()));
        saveLocalOrg.setId(localOrg.getId());
        saveLocalOrg.setName(localOrg.getName());
        saveLocalOrg.setCtime(localOrg.getCtime());
        saveLocalOrg.setEtime(localOrg.getEtime());
        saveLocalOrg.setEuserkey(localOrg.getEuserkey());
        saveLocalOrg.setCuserkey(localOrg.getCuserkey());
        saveLocalOrg.setState(localOrg.getState());
        saveLocalOrg.setNote(localOrg.getNote());
        saveLocalOrg.setSortcode(localOrg.getSortcode());
        saveLocalOrg.setParentid(localOrg.getParentid());
        saveLocalOrg.setTreecode(localOrg.getTreecode());
        saveLocalOrg.setCode(localOrg.getCode());
        FarmDbFields.initUpdateBean(saveLocalOrg, FarmUserContextLoader.getCurrentUser());
        localOrgDao.update(saveLocalOrg);
        return saveLocalOrg;
    }

    @Transactional
    @Override
    public LocalOrg getLocalOrgById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return localOrgDao.findById(id);
    }

    @Override
    public List<LocalOrg> getLocalOrgs(DataQuery query) {
        return localOrgDao.queryData(query.setCount(false)).getData(LocalOrg.class);
    }

    @Transactional
    @Override
    public DataResult searchLocalOrg(DataQuery query) {
        DataResult result = localOrgDao.queryData(query);
        return result;
    }

    @Override
    public int getLocalOrgNum(DataQuery query) {
        return localOrgDao.countData(query);
    }

    /**
     * 移动树节点到目标节点下
     *
     * @param sids
     * @param targetId
     */
    @Transactional
    @Override
    public void moveLocalOrgs(List<String> sids, String targetId) {
        for (String sOrgid : sids) {
            // 移动节点
            LocalOrg node = getLocalOrgById(sOrgid);
            if (!"NONE".equals(targetId)) {
                LocalOrg target = getLocalOrgById(targetId);
                if (target.getTreecode().indexOf(node.getTreecode()) >= 0) {
                    throw new RuntimeException("不能够移动到其子节点下!");
                }
            }
            node.setParentid(targetId);
            localOrgDao.update(node);
            // 构造所有树TREECODE
            List<LocalOrg> list = localOrgDao.findSubNodes(sOrgid);
            for (LocalOrg org : list) {
                initTreeCode(org.getId());
            }
        }
    }

    @Override
    public List<LocalOrg> getOrgPath(String orgid) {
        LocalOrg node = localOrgDao.findById(orgid);
        if (node == null) {
            return List.of();
        }
        Set<String> ids = FarmStringUtils.splitStringByLength(node.getTreecode(), 32).stream().collect(Collectors.toSet());
        return localOrgDao.find(DBRuleList.getInstance().add(new WhereInRule("ID", ids)).toList());
    }


    @Override
    public List<String> getSubOrgids(String id) {
        if (StringUtils.isBlank(id)) {
            return List.of();
        }
        LocalOrg org = localOrgDao.findById(id);
        if (org == null || StringUtils.isBlank(org.getTreecode())) {
            return List.of();
        }
        String treecodePrefix = org.getTreecode();
        // 查询 treecode 以 prefix 开头，且 id 不等于当前 id 的所有机构
        List<LocalOrg> subOrgs = localOrgDao.find(
                DBRuleList.getInstance()
                        .add(new DBRule("TREECODE", treecodePrefix, "like-"))
                        .toList()
        );

        return subOrgs.stream()
                .map(LocalOrg::getId)
                .collect(Collectors.toList());
    }

    /**
     * 自动设置排序号
     *
     * @param ids
     */
    @Transactional
    @Override
    public void autoSort(List<String> ids) {
        int sort = 0;
        for (String id : ids) {
            LocalOrg org = localOrgDao.findById(id);
            if (sort == 0) {
                sort = org.getSortcode();
            }
            org.setSortcode(sort++);
            localOrgDao.update(org);
        }
    }

    /**
     * 构造treecode字段
     *
     * @param treeNodeId
     */
    private void initTreeCode(String treeNodeId) {
        LocalOrg node = localOrgDao.findById(treeNodeId);
        if (node.getParentid().equals("NONE")) {
            node.setTreecode(node.getId());
        } else {
            node.setTreecode(localOrgDao.findById(node.getParentid()).getTreecode() + node.getId());
        }
        localOrgDao.update(node);
    }

    @Transactional
    @Override
    public void delLocalOrg(String id) {
        if (localOrgDao.findByParentId(id).size() > 0) {
            throw new RuntimeException("不能删除该节点，请先删除其子节点");
        }
        localOrgDao.deleteById(id);
    }
}
