package org.farm2.auth.service;

import org.farm2.auth.domain.AuthFamily;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;

import java.util.List;

/**岗位族 
 * @author cbtg自动生成  2026-1-5 10:12:10 
 */
public interface AuthFamilyServiceInter {

    public AuthFamily insertAuthFamilyEntity(AuthFamily authFamily);

    public AuthFamily editAuthFamilyEntity(AuthFamily authFamily);

    public void delAuthFamily(String id);

    public AuthFamily getAuthFamilyById(String id);

    public List<AuthFamily> getAuthFamilys(DataQuery query);

    public DataResult searchAuthFamily(DataQuery query);

    public int getAuthFamilyNum(DataQuery query);
    
    public int getNum(DataQuery query);
    /*[tree：树形结构使用]
    public void moveTreeNode(List<String> ids, String id);
    
    void autoSort(List<String> ids);
    */
}
