package org.farm2.auth.service;

import org.farm2.auth.domain.AuthPostPtype;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;

import java.util.List;

/**
 * 角色类别关系
 *
 * @author cbtg自动生成  2025-10-29 11:56:43
 */
public interface AuthPostPtypeServiceInter {

    public AuthPostPtype insertAuthPostPtypeEntity(AuthPostPtype authPostPtype);

    public AuthPostPtype editAuthPostPtypeEntity(AuthPostPtype authPostPtype);

    public void delAuthPostPtype(String id);

    public AuthPostPtype getAuthPostPtypeById(String id);

    public List<AuthPostPtype> getAuthPostPtypes(DataQuery query);

    public DataResult searchAuthPostPtype(DataQuery query);

    public int getAuthPostPtypeNum(DataQuery query);

    public int getNum(DataQuery query);

    /**
     * 绑定一个角色到角色类别
     *
     * @param postId
     * @param typeId
     */
    public void bind(String postId, String typeId);
    /*[tree：树形结构使用]
    public void moveTreeNode(List<String> ids, String id);
    
    void autoSort(List<String> ids);
    */
}
