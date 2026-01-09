package org.farm2.auth.service;

import org.farm2.auth.domain.Actions;
import org.farm2.auth.dto.MenusDto;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;

import java.util.List;

/**系统权限 
 * @author cbtg自动生成  2025-1-6 11:01:26 
 */
public interface ActionsServiceInter {

    public Actions insertActionsEntity(Actions actions);

    public Actions editActionsEntity(Actions actions);

    public void delActions(String id);

    public Actions getActionsById(String id);

    public List<Actions> getActionss(DataQuery query);

    public DataResult searchActions(DataQuery query);

    public int getActionsNum(DataQuery query);
    
    public int getNum(DataQuery query);
    /*[tree：树形结构使用] */
    public void moveTreeNode(List<String> ids, String id);
    
    void autoSort(List<String> ids);

    /**
     * 获得用户菜单
     *
     * @param loginname
     * @param domain
     * @return
     */
    public List<MenusDto> getMenusByUserKey(String loginname, String domain);

    /**获得全部菜单
     * @param domain
     * @return
     */
    public List<MenusDto> getAllMenus(String domain);

    /**
     * 获得用户的所有权限key
     *
     * @param loginname
     * @return
     */
    public List<String> getActionsKeysByUserKey(String loginname);

    /**
     * 初始化treecode
     */
    public void initTreeCode();
}
