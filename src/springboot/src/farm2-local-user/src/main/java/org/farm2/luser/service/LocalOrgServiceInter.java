package org.farm2.luser.service;

import org.farm2.luser.domain.LocalOrg;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;

import java.util.List;

/**
 * 组织机构
 *
 * @author cbtg自动生成  2025-1-2 22:01:57
 */
public interface LocalOrgServiceInter {

    public LocalOrg insertLocalOrgEntity(LocalOrg localOrg);

    public LocalOrg editLocalOrgEntity(LocalOrg localOrg);

    public void delLocalOrg(String id);

    public LocalOrg getLocalOrgById(String id);

    public List<LocalOrg> getLocalOrgs(DataQuery query);

    public DataResult searchLocalOrg(DataQuery query);

    public int getLocalOrgNum(DataQuery query);

    /**
     * 移动树节点
     *
     * @param ids
     * @param id
     */
    void moveLocalOrgs(List<String> ids, String id);

    /**
     * 自动设置排序号
     *
     * @param ids
     */
    void autoSort(List<String> ids);

    public List<LocalOrg> getOrgPath(String orgid);

    public List<String> getSubOrgids(String id);
}
