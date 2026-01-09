package org.farm2.auth.service;

import org.farm2.auth.domain.DicType;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;

import java.util.List;

/**字典类型 
 * @author cbtg自动生成  2025-1-7 15:27:42 
 */
public interface DicTypeServiceInter {

    public DicType insertDicTypeEntity(DicType dicType);

    public DicType editDicTypeEntity(DicType dicType);

    public void delDicType(String id);

    public DicType getDicTypeById(String id);

    public List<DicType> getDicTypes(DataQuery query);

    public DataResult searchDicType(DataQuery query);

    public int getDicTypeNum(DataQuery query);
    
    public int getNum(DataQuery query);
    /*[tree：树形结构使用]
    public void moveTreeNode(List<String> ids, String id);
    
    void autoSort(List<String> ids);
    */
}
