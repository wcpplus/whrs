package org.farm2.auth.service;

import org.farm2.auth.domain.AuthPtype;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;

import java.util.List;
import java.util.Set;

/**
 * 角色类别
 *
 * @author cbtg自动生成  2025-10-29 11:55:20
 */
public interface AuthPtypeServiceInter {

    public AuthPtype insertAuthPtypeEntity(AuthPtype authPtype);

    public AuthPtype editAuthPtypeEntity(AuthPtype authPtype);

    public void delAuthPtype(String id);

    public AuthPtype getAuthPtypeById(String id);

    public List<AuthPtype> getAuthPtypes(DataQuery query);

    public DataResult searchAuthPtype(DataQuery query);

    public int getAuthPtypeNum(DataQuery query);

    public int getNum(DataQuery query);

    /**
     * 获得类别下的角色
     *
     * @param ptypeId
     * @param num
     * @return
     */
    public Set<String> getPostIds(String ptypeId, int num);

    /**
     * 获得岗位绑定分类的数量
     *
     * @param postId
     * @return
     */
    public Integer getPtypeNum(String postId);
    /*[tree：树形结构使用]
    public void moveTreeNode(List<String> ids, String id);
    
    void autoSort(List<String> ids);
    */
}
