package org.farm2.auth.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.farm2.base.db.FarmDbFields;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.base.exception.FarmExceptionUtils;
import org.farm2.auth.dao.ParameterDao;
import org.farm2.auth.domain.Parameter;
import org.farm2.auth.service.ParameterServiceInter;
import org.farm2.tools.caches.FarmCacheKeys;
import org.farm2.tools.caches.FarmCaches;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.i18n.I18n;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统参数
 *
 * @author cbtg自动生成  2025-1-8 10:39:16
 */
@Service
@Slf4j
public class ParameterServiceImpl implements ParameterServiceInter {
    @Autowired
    private ParameterDao parameterDao;

    @Transactional
    @Override
    public Parameter insertParameterEntity(Parameter parameter) {
        FarmDbFields.initInsertBean(parameter, FarmUserContextLoader.getCurrentUser());
        //FarmBeanUtils.runFunctionByBlank(parameter.getType(), "1", parameter::setType);
        parameterDao.insert(parameter);
        //[tree：树形结构使用]
        //initTreeCode(actions.getId());
        return parameter;
    }

    @Transactional
    @Override
    public Parameter editParameterEntity(Parameter parameter) {
        Parameter saveParameter = getParameterById(parameter.getId());
        FarmExceptionUtils.throwNullEx(saveParameter, I18n.msg("系统参数不存在:?", parameter.getId()));
        saveParameter.setSourcetype(parameter.getSourcetype());
        saveParameter.setVtype(parameter.getVtype());
        saveParameter.setPkey(parameter.getPkey());
        saveParameter.setUserable(parameter.getUserable());
        saveParameter.setDescribes(parameter.getDescribes());
        saveParameter.setPvalue(parameter.getPvalue());
        saveParameter.setName(parameter.getName());
        saveParameter.setGkey(parameter.getGkey());
        saveParameter.setGname(parameter.getGname());
        saveParameter.setVersion(parameter.getVersion());
        FarmDbFields.initUpdateBean(saveParameter, FarmUserContextLoader.getCurrentUser());
        parameterDao.update(saveParameter);
        return saveParameter;
    }

    @Transactional
    @Override
    public Parameter getParameterById(String id) {
        return parameterDao.findById(id);
    }

    @Override
    public List<Parameter> getParameters(DataQuery query) {
        return parameterDao.queryData(query.setCount(false)).getData(Parameter.class);
    }


    @Transactional
    @Override
    public DataResult searchParameter(DataQuery query) {
        DataResult result = parameterDao.queryData(query);
        return result;
    }

    @Override
    public int getParameterNum(DataQuery query) {
        return parameterDao.countData(query);
    }


    @Transactional
    @Override
    public void delParameter(String id) {
        /*[tree：树形结构使用]
        if ( parameterDao.findByParentId(id).size() > 0) {
            throw new RuntimeException("不能删除该节点，请先删除其子节点");
        }
        */
        parameterDao.deleteById(id);
    }

    @Override
    public int getNum(DataQuery query) {
        return parameterDao.countData(query);
    }

    @Override
    public List<Parameter> getParametersBySourcetype(String sourcetype) {
        return parameterDao.findAllBySourceType(sourcetype);
    }

    @Transactional
    @Override
    public void editSourceType(String id, String sourceType) {
        Parameter node = parameterDao.findById(id);
        node.setSourcetype(sourceType);
        parameterDao.update(node);
    }

    @Override
    @Transactional
    public void editParameterVal(String key, String val,String note) {
        Parameter node = parameterDao.findByKey(key);
        node.setPvalue(val);
        node.setNote(note);
        parameterDao.update(node);
        FarmCaches.getInstance().removeCacheData(key, FarmCacheKeys.PARAMETER);
    }

    @Override
    @Transactional
    public Parameter getParameterByKey(String key) {
        return parameterDao.findByKey(key);
    }
    
    /*[tree：树形结构使用]
    @Transactional
    @Override
    public void moveTreeNode(List<String> sourceIds, String targetId) {
        for (String sourceId : sourceIds) {
            // 移动节点
            Parameter node = getParameterById(sourceId);
            if (!"NONE".equals(targetId)) {
                Parameter target = getParameterById(targetId);
                if (target.getTreecode().indexOf(node.getTreecode()) >= 0) {
                    throw new RuntimeException("不能够移动到其子节点下!");
                }
            }
            node.setParentid(targetId);
            parameterDao.update(node);
            // 构造所有树TREECODE
            List<Parameter> list = parameterDao.findSubNodes(sourceId);
            for (Parameter treenode : list) {
                initTreeCode(treenode.getId());
            }
        }
    }*/

    /**[tree：树形结构使用]
     * 构造treecode字段
     * @param treeNodeId
    private void initTreeCode(String treeNodeId) {
    Parameter node = parameterDao.findById(treeNodeId);
    if (node.getParentid().equals("NONE")) {
    node.setTreecode(node.getId());
    } else {
    node.setTreecode(parameterDao.findById(node.getParentid()).getTreecode() + node.getId());
    }
    parameterDao.update(node);
    }
     */
    /* [tree：树形结构使用]
    @Transactional
    @Override
    public void autoSort(List<String> ids) {
        int sort = 0;
        for (String id : ids) {
            Parameter node =  parameterDao.findById(id);
            if (sort == 0) {
                sort = node.getSortcode();
            }
            node.setSortcode(sort++);
            parameterDao.update(node);
        }
    }*/
}
