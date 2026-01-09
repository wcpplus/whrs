package org.farm2.wdap.service;

import org.farm2.wdap.domain.WdapConvertor;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;

import java.util.List;

/**文件转换器 
 * @author cbtg自动生成  2025-1-24 10:22:07 
 */
public interface WdapConvertorServiceInter {

    public WdapConvertor insertWdapConvertorEntity(WdapConvertor wdapConvertor);

    public WdapConvertor editWdapConvertorEntity(WdapConvertor wdapConvertor);

    public void delWdapConvertor(String id);

    public WdapConvertor getWdapConvertorById(String id);

    public List<WdapConvertor> getWdapConvertors(DataQuery query);

    public DataResult searchWdapConvertor(DataQuery query);

    public int getWdapConvertorNum(DataQuery query);
    
    public int getNum(DataQuery query);
    /*[tree：树形结构使用]
    public void moveTreeNode(List<String> ids, String id);
    
    void autoSort(List<String> ids);
    */
}
