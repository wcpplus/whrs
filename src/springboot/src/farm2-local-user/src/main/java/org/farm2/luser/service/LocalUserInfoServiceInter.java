package org.farm2.luser.service;

import org.farm2.luser.domain.LocalUserInfo;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;

import java.util.List;

/**
 * 用户信息
 *
 * @author cbtg自动生成  2026-1-5 21:07:40
 */
public interface LocalUserInfoServiceInter {

    public LocalUserInfo insertLocalUserInfoEntity(LocalUserInfo localUserInfo);

    public LocalUserInfo editLocalUserInfoEntity(LocalUserInfo localUserInfo);

    public void delLocalUserInfo(String id);

    public LocalUserInfo getLocalUserInfoById(String id);

    public List<LocalUserInfo> getLocalUserInfos(DataQuery query);

    public DataResult searchLocalUserInfo(DataQuery query);

    public int getLocalUserInfoNum(DataQuery query);

    public int getNum(DataQuery query);

    public LocalUserInfo initUserInfoByUserKey(String userkey);

    public LocalUserInfo getLocalUserInfoByUserKey(String loginname);
    /*[tree：树形结构使用]
    public void moveTreeNode(List<String> ids, String id);
    
    void autoSort(List<String> ids);
    */
}
