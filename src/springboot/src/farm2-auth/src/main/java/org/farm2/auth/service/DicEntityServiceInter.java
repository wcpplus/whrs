package org.farm2.auth.service;

import org.farm2.auth.domain.DicEntity;
import org.farm2.auth.domain.DicType;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;

import java.util.List;
import java.util.Map;

/**
 * 数据字典
 *
 * @author cbtg自动生成  2025-1-7 13:49:27
 */
public interface DicEntityServiceInter {

    public DicEntity insertDicEntityEntity(DicEntity dicEntity);

    public DicEntity editDicEntityEntity(DicEntity dicEntity);

    public void delDicEntity(String id);

    public DicEntity getDicEntityById(String id);

    public List<DicEntity> getDicEntitys(DataQuery query);

    public DataResult searchDicEntity(DataQuery query);

    public int getDicEntityNum(DataQuery query);

    public int getNum(DataQuery query);

    public DicEntity getDicEntityByKey(String key);

    public Map<String, DicType> getTypes(String key);
    /*[tree：树形结构使用]
    public void moveTreeNode(List<String> ids, String id);
    
    void autoSort(List<String> ids);
    */
}
