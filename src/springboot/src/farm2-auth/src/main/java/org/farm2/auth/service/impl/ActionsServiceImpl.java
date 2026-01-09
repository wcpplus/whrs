package org.farm2.auth.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.farm2.auth.dao.PostActionsDao;
import org.farm2.auth.dao.UserPostDao;
import org.farm2.auth.domain.UserPost;
import org.farm2.auth.dto.MenusDto;
import org.farm2.auth.face.FarmParameter;
import org.farm2.base.db.FarmDbFields;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.base.exception.FarmExceptionUtils;
import org.farm2.auth.dao.ActionsDao;
import org.farm2.auth.domain.Actions;
import org.farm2.auth.service.ActionsServiceInter;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.DBRule;
import org.farm2.tools.i18n.I18n;
import org.farm2.tools.base.FarmStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统权限
 *
 * @author cbtg自动生成  2025-1-6 11:01:26
 */
@Service
@Slf4j
public class ActionsServiceImpl implements ActionsServiceInter {
    @Autowired
    private PostActionsDao postActionsDao;
    @Autowired
    private UserPostDao userPostDao;
    @Autowired
    private ActionsDao actionsDao;
    @Autowired
    private FarmParameter farmParameter;
    @Transactional
    @Override
    public Actions insertActionsEntity(Actions actions) {
        FarmDbFields.initInsertBean(actions, FarmUserContextLoader.getCurrentUser());
        //FarmBeanUtils.runFunctionByBlank(actions.getType(), "1", actions::setType);
        Actions parentActions = actionsDao.findById(actions.getParentid());
        if (StringUtils.isBlank(actions.getDomain())) {
            if (parentActions != null) {
                actions.setDomain(parentActions.getDomain());
            } else {
                if (actions.getParentid().equals("NONE")) {
                    actions.setParentid("APP");
                }
                actions.setDomain(actions.getParentid());
            }
        }
        actionsDao.insert(actions);
        //[tree：树形结构使用]
        initTreeCode(actions.getId());
        return actions;
    }

    @Transactional
    @Override
    public Actions editActionsEntity(Actions actions) {
        Actions saveActions = getActionsById(actions.getId());
        FarmExceptionUtils.throwNullEx(saveActions, I18n.msg("系统权限不存在:?", actions.getId()));
        saveActions.setState(actions.getState());
        saveActions.setNote(actions.getNote());
        saveActions.setName(actions.getName());
        saveActions.setSortcode(actions.getSortcode());
        saveActions.setMenukey(actions.getMenukey());
        saveActions.setType(actions.getType());
        saveActions.setActions(actions.getActions());
        saveActions.setDomain(actions.getDomain());
        FarmDbFields.initUpdateBean(saveActions, FarmUserContextLoader.getCurrentUser());
        actionsDao.update(saveActions);
        return saveActions;
    }

    @Transactional
    @Override
    public Actions getActionsById(String id) {
        return actionsDao.findById(id);
    }

    @Override
    public List<Actions> getActionss(DataQuery query) {
        return actionsDao.queryData(query.setCount(false)).getData(Actions.class);
    }


    @Transactional
    @Override
    public DataResult searchActions(DataQuery query) {
        DataResult result = actionsDao.queryData(query);
        return result;
    }

    @Override
    public int getActionsNum(DataQuery query) {
        return actionsDao.countData(query);
    }


    @Transactional
    @Override
    public void delActions(String id) {
        /*[tree：树形结构使用]*/
        if ( actionsDao.findByParentId(id).size() > 0) {
            throw new RuntimeException("不能删除该节点，请先删除其子节点");
        }
        postActionsDao.delete(DBRule.getInstance("ACTIONSID", id, "=").getRules());
        actionsDao.deleteById(id);
    }

    @Override
    public int getNum(DataQuery query) {
        return actionsDao.countData(query);
    }

    /*[tree：树形结构使用]
    @Transactional
    @Override*/
    public void moveTreeNode(List<String> sourceIds, String targetId) {
        for (String sourceId : sourceIds) {
            // 移动节点
            Actions node = getActionsById(sourceId);
            if (!"NONE".equals(targetId)) {
                Actions target = getActionsById(targetId);
                if (target.getTreecode().indexOf(node.getTreecode()) >= 0) {
                    throw new RuntimeException("不能够移动到其子节点下!");
                }
            }
            node.setParentid(targetId);
            actionsDao.update(node);
            // 构造所有树TREECODE
            List<Actions> list = actionsDao.findSubNodes(sourceId);
            for (Actions treenode : list) {
                initTreeCode(treenode.getId());
            }
        }
    }

    /**
     * [tree：树形结构使用]
     * 构造treecode字段
     *
     * @param treeNodeId
     */
    private void initTreeCode(String treeNodeId) {
        Actions node = actionsDao.findById(treeNodeId);
        if (node.getParentid().equals("FRAME") || node.getParentid().equals("APP")) {
            node.setTreecode(node.getId());
        } else {
            node.setTreecode(actionsDao.findById(node.getParentid()).getTreecode() + node.getId());
        }
        actionsDao.update(node);
    }

    /* [tree：树形结构使用]
    @Transactional
    @Override*/
    public void autoSort(List<String> ids) {
        int sort = 0;
        for (String id : ids) {
            Actions node = actionsDao.findById(id);
            if (sort == 0) {
                sort = node.getSortcode();
            }
            node.setSortcode(sort++);
            actionsDao.update(node);
        }
    }

    @Override
    public List<MenusDto> getMenusByUserKey(String loginname, String domain) {
        //加载用户岗位
        List<String> postIds = userPostDao.findPostIdsByUserKey(loginname);
        //加载用户菜单
        List<String> actionIds = postActionsDao.findByPostIds(postIds);
        //菜单
        List<Actions> dbactions = actionsDao.findByActionIds(actionIds);
        //当前有的菜单
        Set<String> haveIds = new HashSet<>();
        //有效菜单（可用且domain符合要求）
        List<Actions> actions = new ArrayList<>();
        for (Actions action : dbactions) {
            if (action.getState().equals("1") && action.getDomain().equals(domain) && action.getType().equals("1")) {
                actions.add(action);
                haveIds.add(action.getId());
            }
        }
        //需要重新查询的菜单
        List<String> queryIds = new ArrayList<>();
        //迭代菜单，补齐缺失的上级节点
        for (Actions node : actions) {
            List<String> ids = FarmStringUtils.splitStringByLength(node.getTreecode(), 32);
            for (String id : ids) {
                if (!haveIds.contains(id)) {
                    queryIds.add(id);
                }
            }
        }
        //补充没有的上级菜单
        actions.addAll(actionsDao.findByActionIds(queryIds));
        //汇总后构造上下级关系
        List<MenusDto> menus = getMenus(actions, domain);
        //farm2.config.permission.default.menus
        {
            if (domain.equals("APP")) {
                List<String> defaultMenuKeys = farmParameter.getStringListParameter("farm2.config.permission.default.menus");
                Set<String> menuKeys = menus.stream().map(node -> node.getMenukey()).collect(Collectors.toSet());
                for (String defaultMenukey : defaultMenuKeys) {
                    if (!menuKeys.contains(defaultMenukey) && !defaultMenukey.equals("NONE")) {
                        Actions action = actionsDao.findBykey(defaultMenukey);
                        if (action != null) {
                            MenusDto menu = new MenusDto();
                            menu.setId(action.getId());
                            menu.setName(action.getName());
                            menu.setMenukey(action.getMenukey());
                            menu.setSortcode(action.getSortcode());
                            menu.setChildren(new ArrayList<>());
                            menus.add(menu);
                        }
                    }
                }
            }
        }
        //构造Menus
        return menus;
    }

    @Override
    public List<MenusDto> getAllMenus(String domain) {
        //菜单
        List<Actions> dbactions = actionsDao.findAll();
        //有效菜单（可用且domain符合要求）
        List<Actions> actions = new ArrayList<>();
        for (Actions action : dbactions) {
            if (action.getState().equals("1") && action.getDomain().equals(domain) && action.getType().equals("1")) {
                actions.add(action);
            }
        }
        //迭代菜单，补齐缺失的上级节点
        //汇总后构造上下级关系
        List<MenusDto> menus = getMenus(actions, domain);
        //构造Menus
        return menus;
    }

    @Override
    public List<String> getActionsKeysByUserKey(String loginname) {
        //加载用户岗位
        List<String> postIds = userPostDao.findPostIdsByUserKey(loginname);
        //加载用户菜单
        List<String> actionIds = postActionsDao.findByPostIds(postIds);
        //加载默认菜单
        actionIds.addAll(actionsDao.findByMenuKeys(farmParameter.getStringListParameter("farm2.config.permission.default.menus")));
        //菜单
        List<Actions> dbactions = actionsDao.findByActionIds(actionIds.stream().distinct().toList());
        Set<String> actions = new HashSet<>();
        for (Actions action : dbactions) {
            if (action.getActions() != null) {
                for (String actionKey : action.getActions().replace("，", ",").split(",")) {
                    if (StringUtils.isNotBlank(actionKey)) {
                        actions.add(actionKey.trim());
                    }
                }
            }
        }
        return new ArrayList(actions);
    }

    @Transactional
    @Override
    public void initTreeCode() {
        //检索全部数据
        List<Actions> actions = actionsDao.findAll();
        Set<String> currentParentIds = new HashSet<>();
        Set<String> nextParentIds = new HashSet<>();
        currentParentIds.add("FRAME");
        currentParentIds.add("APP");
        int level = 0;
        //第一步查询parentid为FRAME或APP
        while (currentParentIds.size() > 0 && level < 10) {
            level++;
            nextParentIds.clear();
            for (Actions action : actions) {
                //然后逐步添加子分类进来
                if (currentParentIds.contains(action.getParentid())) {
                    initTreeCode(action.getId());
                    nextParentIds.add(action.getId());
                }
            }
            currentParentIds = new HashSet<>(nextParentIds);
        }
    }

    private List<MenusDto> getMenus(List<Actions> data, String domain) {
        if (data == null || data.isEmpty()) {
            return new ArrayList<MenusDto>(); // 如果输入数据为空，返回空列表
        }
        // 创建一个映射来保存所有节点，以便根据ID快速查找
        Map<String, MenusDto> nodeMap = new HashMap<>();
        // 第一次遍历：创建所有节点并添加到映射中
        for (Actions item : data) {
            if (item.getDomain().equals(domain)) {
                MenusDto menu = new MenusDto();
                menu.setId(item.getId());
                menu.setName(item.getName());
                menu.setMenukey(item.getMenukey());
                menu.setSortcode(item.getSortcode());
                menu.setChildren(new ArrayList<>());
                nodeMap.put(item.getId(), menu); // 将节点放入映射中
            }
        }

        // 第二次遍历：为每个节点分配父节点，并将子节点加入父节点的children列表中
        List<MenusDto> rootNodes = new ArrayList<>(); // 用于存储顶级节点
        for (Actions item : data) {
            if (item.getDomain().equals(domain)) {
                // 如果parentId为空或在rootParent中，则该节点为顶级节点
                if (item.getParentid().equals(domain)) {
                    rootNodes.add(nodeMap.get(item.getId())); // 添加到顶级节点列表
                } else {
                    // 查找父节点并将当前节点作为其子节点
                    MenusDto parentNode = nodeMap.get(item.getParentid());
                    if (parentNode != null) {
                        if (parentNode.getChildren() == null) {
                            parentNode.setChildren(new ArrayList<>());
                        }
                        parentNode.getChildren().add(nodeMap.get(item.getId()));
                    }
                }
            }
        }
        // 对每个节点的子节点按照sortKey进行排序
        for (MenusDto node : nodeMap.values()) {
            if (node.getChildren() != null) {
                node.getChildren().sort(new Comparator<MenusDto>() {
                    @Override
                    public int compare(MenusDto o1, MenusDto o2) {
                        return o1.getSortcode() - o2.getSortcode();
                    }
                });
            }
        }

        rootNodes.sort(new Comparator<MenusDto>() {
            @Override
            public int compare(MenusDto o1, MenusDto o2) {
                return o1.getSortcode() - o2.getSortcode();
            }
        });

        return rootNodes; // 返回顶级节点列表
    }
}
