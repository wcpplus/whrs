package org.farm2.wdap.service;

import org.farm2.wdap.domain.WdapFlowLink;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;

import java.util.List;

/**流程连线 
 * @author cbtg自动生成  2025-1-21 22:42:51 
 */
public interface WdapFlowLinkServiceInter {

    public WdapFlowLink insertWdapFlowLinkEntity(WdapFlowLink wdapFlowLink);

    public WdapFlowLink editWdapFlowLinkEntity(WdapFlowLink wdapFlowLink);

    public void delWdapFlowLink(String id);

    public WdapFlowLink getWdapFlowLinkById(String id);

    public List<WdapFlowLink> getWdapFlowLinks(DataQuery query);

    public DataResult searchWdapFlowLink(DataQuery query);

    public int getWdapFlowLinkNum(DataQuery query);
    
    public int getNum(DataQuery query);
    /*[tree：树形结构使用]
    public void moveTreeNode(List<String> ids, String id);
    
    void autoSort(List<String> ids);
    */
}
