package org.farm2.files.service;

import org.farm2.files.domain.ResourceFileRegiste;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;

import java.util.List;

/**附件注册 
 * @author cbtg自动生成  2025-2-4 10:42:08 
 */
public interface ResourceFileRegisteServiceInter {

    public ResourceFileRegiste insertResourceFileRegisteEntity(ResourceFileRegiste resourceFileRegiste);

    public ResourceFileRegiste editResourceFileRegisteEntity(ResourceFileRegiste resourceFileRegiste);

    public void delResourceFileRegiste(String id);

    public ResourceFileRegiste getResourceFileRegisteById(String id);

    public List<ResourceFileRegiste> getResourceFileRegistes(DataQuery query);

    public DataResult searchResourceFileRegiste(DataQuery query);

    public int getResourceFileRegisteNum(DataQuery query);
    
    public int getNum(DataQuery query);
    /*[tree：树形结构使用]
    public void moveTreeNode(List<String> ids, String id);
    
    void autoSort(List<String> ids);
    */
}
