package org.farm2.auth.service;

import org.farm2.auth.domain.Parameter;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;

import java.util.List;

/**
 * 系统参数
 *
 * @author cbtg自动生成  2025-1-8 10:39:16
 */
public interface ParameterServiceInter {

    public Parameter insertParameterEntity(Parameter parameter);

    public Parameter editParameterEntity(Parameter parameter);

    public void delParameter(String id);

    public Parameter getParameterById(String id);

    public List<Parameter> getParameters(DataQuery query);

    public DataResult searchParameter(DataQuery query);

    public int getParameterNum(DataQuery query);

    public int getNum(DataQuery query);

    public List<Parameter> getParametersBySourcetype(String sourcetype);

    public void editSourceType(String id, String number);

    public void editParameterVal(String key, String val,String note);

    public Parameter getParameterByKey(String key);
    /*[tree：树形结构使用]
    public void moveTreeNode(List<String> ids, String id);
    
    void autoSort(List<String> ids);
    */
}
