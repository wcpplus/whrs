package org.farm2.salary.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.FarmDbFields;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.base.exception.FarmAppException;
import org.farm2.base.exception.FarmExceptionUtils;
import org.farm2.base.password.FarmPasswordEncoder;
import org.farm2.salary.dao.*;
import org.farm2.salary.domain.*;
import org.farm2.salary.service.SalaryTemplateFormulaServiceInter;
import org.farm2.salary.service.SalaryUserBaseServiceInter;
import org.farm2.tools.bean.FarmBeanUtils;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.DBRule;
import org.farm2.tools.db.commons.DBRuleList;
import org.farm2.tools.db.commons.DBSort;
import org.farm2.tools.i18n.I18n;
import org.farm2.tools.time.FarmTimeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户薪酬定义
 *
 * @author cbtg自动生成  2026-1-8 14:45:43
 */
@Service
@Slf4j
public class SalaryUserBaseServiceImpl implements SalaryUserBaseServiceInter {


    @Autowired
    private SalaryUserUnitDao salaryUserUnitDao;
    @Autowired
    private SalaryUserBaseDao salaryUserBaseDao;
    @Autowired
    private SalaryUserDao salaryUserDao;
    @Autowired
    private SalaryTemplateItemDao salaryTemplateItemDao;
    @Autowired
    private SalaryTemplateFormulaServiceInter salaryTemplateFormulaServiceImpl;
    @Autowired
    private SalaryUserItemDao salaryUserItemDao;

    @Transactional
    @Override
    public void initUserBaseParas(String salarytime, String userkey) {
        if (salarytime.length() != 6) {
            throw new RuntimeException("周期描述错误salarytime:" + salarytime);
        }
        //查询user
        SalaryUser salaryUnit = salaryUserDao.queryOne(DBRuleList.getInstance()
                .add(new DBRule("USERKEY", userkey, "="))
                .toList());
        List<SalaryTemplateItem> items = salaryTemplateItemDao.find(DBRuleList.getInstance()
                .add(new DBRule("TEMPLATEID", salaryUnit.getTemplateid(), "="))
                .toList());
        Set<String> itemKeycodes = items.stream().map(SalaryTemplateItem::getKeycode).collect(Collectors.toSet());
        //加载模板参数
        for (SalaryTemplateItem item : items) {
            //1手动更新，2月更新，0不更新
            if (item.getUserover().equals("1") || item.getUserover().equals("2")) {
                initUserBasePara(item, salarytime, userkey);
            }
        }
        {
//            List<SalaryUserBase> bases = salaryUserBaseDao.find(DBRuleList.getInstance()
//                    .add(new DBRule("TEMPLATEID", salaryUnit.getTemplateid(), "="))
//                    .toList());

            //删除模板参数
        }
    }

    @Override
    public void runSalary(String userSalaryUnitId) {
        SalaryUserUnit userUnit = salaryUserUnitDao.findById(userSalaryUnitId);
        SalaryUser salaryUnit = salaryUserDao.queryOne(DBRuleList.getInstance()
                .add(new DBRule("USERKEY", userUnit.getUserkey(), "="))
                .toList());
        List<SalaryUserBase> bases = salaryUserBaseDao.find(DBRuleList.getInstance()
                .add(new DBRule("USERKEY", userUnit.getUserkey(), "="))
                .add(new DBRule("SALARYTIME", userUnit.getSalarytime(), "="))
                .toList());

        List<SalaryTemplateItem> items = salaryTemplateItemDao.find(DBRuleList.getInstance()
                .add(new DBRule("TEMPLATEID", salaryUnit.getTemplateid(), "="))
                .toList());
        Map<String, String> envTitle = new HashMap<>();
        for (SalaryTemplateItem item : items) {
            envTitle.put(item.getKeycode(), item.getName());
        }


        Map<String, Object> env = new HashMap<>();

        for (SalaryUserBase base : bases) {
            env.put(base.getKeycode(), base.getVal());
        }
        Map<String, Object> result = salaryTemplateFormulaServiceImpl.runAll(salaryUnit.getTemplateid(), env);
        userUnit.setCtime(FarmTimeTool.getTimeDate14());
        userUnit.setState("1");
        if (result.get("GROSS_SALARY_SYS") == null) {
            throw new RuntimeException("未生成【应得收入:GROSS_SALARY_SYS】");
        }
        if (result.get("NET_SALARY_SYS") == null) {
            throw new RuntimeException("未生成【实际所得:NET_SALARY_SYS】");
        }
        if (result.get("INCOME_TAX_SYS") == null) {
            throw new RuntimeException("未生成【个人所得税:INCOME_TAX_SYS】");
        }
        userUnit.setGrosspay(new BigDecimal(String.valueOf(result.get("GROSS_SALARY_SYS"))));//应的收入
        userUnit.setNetpay(new BigDecimal(String.valueOf(result.get("NET_SALARY_SYS"))));//实际所得
        userUnit.setTaxamount(new BigDecimal(String.valueOf(result.get("INCOME_TAX_SYS"))));
        salaryUserItemDao.delete(DBRuleList.getInstance()
                .add(new DBRule("USERKEY", userUnit.getUserkey(), "="))
                .add(new DBRule("SALARYTIME", userUnit.getSalarytime(), "="))
                .toList());
        for (String key : result.keySet()) {
            //envTitle
            SalaryUserItem item = new SalaryUserItem();
            item.setCtime(FarmTimeTool.getTimeDate14());
            item.setVal(new BigDecimal(String.valueOf(result.get(key))));
            item.setName(envTitle.getOrDefault(key, key));
            item.setSalarytime(userUnit.getSalarytime());
            item.setKeycode(key);
            item.setShowmodel("1");
            item.setUserkey(userUnit.getUserkey());
            item.setUsername(userUnit.getUsername());
            salaryUserItemDao.insert(item);
        }

        salaryUserUnitDao.update(userUnit);
    }


    private void initUserBasePara(SalaryTemplateItem item, String salarytime, String userkey) {
        SalaryUserBase salaryBase = salaryUserBaseDao.queryOne(DBRuleList.getInstance()
                .add(new DBRule("USERKEY", userkey, "="))
                .add(new DBRule("SALARYTIME", salarytime, "="))
                .add(new DBRule("KEYCODE", item.getKeycode(), "="))
                .toList());
        if (salaryBase == null) {
            List<SalaryUserBase> hisBase = getSalaryUserBases(DataQuery.getInstance().setCount(false)
                    .addRule(new DBRule("USERKEY", userkey, "="))
                    .addRule(new DBRule("KEYCODE", item.getKeycode(), "="))
                    .addSort(new DBSort("KEYCODE", DBSort.SORT_TYPE.DESC))
            );
            salaryBase = new SalaryUserBase();
            salaryBase.setSalarytime(salarytime);
            salaryBase.setUserkey(userkey);
            salaryBase.setUsername(userkey);
            salaryBase.setUserover(item.getUserover());
            salaryBase.setName(item.getName());
            salaryBase.setKeycode(item.getKeycode());
            if (!hisBase.isEmpty()) {
                salaryBase.setVal(hisBase.get(0).getVal());
            } else {
                salaryBase.setVal(item.getDefaultval());
            }
            salaryBase.setShowmodel("1");
            salaryUserBaseDao.insert(salaryBase);
        }
    }

    @Transactional
    @Override
    public SalaryUserBase insertSalaryUserBaseEntity(SalaryUserBase salaryUserBase) {
        FarmDbFields.initInsertBean(salaryUserBase, FarmUserContextLoader.getCurrentUser());
        //FarmBeanUtils.runFunctionByBlank(salaryUserBase.getType(), "1", salaryUserBase::setType);
        salaryUserBaseDao.insert(salaryUserBase);
        //[tree：树形结构使用]
        //initTreeCode(actions.getId());
        return salaryUserBase;
    }

    @Transactional
    @Override
    public SalaryUserBase editSalaryUserBaseEntity(SalaryUserBase salaryUserBase) {
        SalaryUserBase saveSalaryUserBase = getSalaryUserBaseById(salaryUserBase.getId());
        FarmExceptionUtils.throwNullEx(saveSalaryUserBase, I18n.msg("用户薪酬定义不存在:?", salaryUserBase.getId()));
        saveSalaryUserBase.setId(salaryUserBase.getId());
        saveSalaryUserBase.setName(salaryUserBase.getName());
        saveSalaryUserBase.setKeycode(salaryUserBase.getKeycode());
        saveSalaryUserBase.setVal(salaryUserBase.getVal());
        saveSalaryUserBase.setShowmodel(salaryUserBase.getShowmodel());
        saveSalaryUserBase.setUserkey(salaryUserBase.getUserkey());
        saveSalaryUserBase.setUsername(salaryUserBase.getUsername());
        saveSalaryUserBase.setSalarytime(salaryUserBase.getSalarytime());

        FarmDbFields.initUpdateBean(saveSalaryUserBase, FarmUserContextLoader.getCurrentUser());
        salaryUserBaseDao.update(saveSalaryUserBase);
        return saveSalaryUserBase;
    }

    @Transactional
    @Override
    public SalaryUserBase getSalaryUserBaseById(String id) {
        return salaryUserBaseDao.findById(id);
    }

    @Override
    public List<SalaryUserBase> getSalaryUserBases(DataQuery query) {
        return salaryUserBaseDao.queryData(query.setCount(false)).getData(SalaryUserBase.class);
    }


    @Transactional
    @Override
    public DataResult searchSalaryUserBase(DataQuery query) {
        DataResult result = salaryUserBaseDao.queryData(query);
        return result;
    }

    @Override
    public int getSalaryUserBaseNum(DataQuery query) {
        return salaryUserBaseDao.countData(query);
    }


    @Transactional
    @Override
    public void delSalaryUserBase(String id) {
        /*[tree：树形结构使用]
        if ( salaryUserBaseDao.findByParentId(id).size() > 0) {
            throw new RuntimeException("不能删除该节点，请先删除其子节点");
        }
        */
        salaryUserBaseDao.deleteById(id);
    }

    @Override
    public int getNum(DataQuery query) {
        return salaryUserBaseDao.countData(query);
    }


    /*[tree：树形结构使用]
    @Transactional
    @Override
    public void moveTreeNode(List<String> sourceIds, String targetId) {
        for (String sourceId : sourceIds) {
            // 移动节点
            SalaryUserBase node = getSalaryUserBaseById(sourceId);
            if (!"NONE".equals(targetId)) {
                SalaryUserBase target = getSalaryUserBaseById(targetId);
                if (target.getTreecode().indexOf(node.getTreecode()) >= 0) {
                    throw new RuntimeException("不能够移动到其子节点下!");
                }
            }
            node.setParentid(targetId);
            salaryUserBaseDao.update(node);
            // 构造所有树TREECODE
            List<SalaryUserBase> list = salaryUserBaseDao.findSubNodes(sourceId);
            for (SalaryUserBase treenode : list) {
                initTreeCode(treenode.getId());
            }
        }
    }*/

    /**[tree：树形结构使用]
     * 构造treecode字段
     * @param treeNodeId
    private void initTreeCode(String treeNodeId) {
    SalaryUserBase node = salaryUserBaseDao.findById(treeNodeId);
    if (node.getParentid().equals("NONE")) {
    node.setTreecode(node.getId());
    } else {
    node.setTreecode(salaryUserBaseDao.findById(node.getParentid()).getTreecode() + node.getId());
    }
    salaryUserBaseDao.update(node);
    }
     */
    /* [tree：树形结构使用]
    @Transactional
    @Override
    public void autoSort(List<String> ids) {
        int sort = 0;
        for (String id : ids) {
            SalaryUserBase node =  salaryUserBaseDao.findById(id);
            if (sort == 0) {
                sort = node.getSortcode();
            }
            node.setSortcode(sort++);
            salaryUserBaseDao.update(node);
        }
    }*/
}
