package org.farm2.auth.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.farm2.base.db.SuperMapper;
import org.farm2.auth.domain.Actions;

import java.util.List;

/**系统权限 
 * @author cbtg自动生成  2025-1-6 11:01:26 
 */
@Mapper
public interface ActionsMapper extends SuperMapper<Actions> {

    @Select({"<script>",
            "SELECT * FROM ${tableName} WHERE ID IN",
            "<foreach item='actionId' collection='actionIds' open='(' separator=',' close=')'>",
            "#{actionId}",
            "</foreach>",
            "</script>"})
    List<Actions> findByActionIds(@Param("tableName") String tableName, @Param("actionIds") List<String> actionIds);


    @Select("SELECT * FROM ${tableName} WHERE menukey = #{menukey}")
    Actions findByMenuKey(@Param("tableName") String tableName, @Param("menukey") String menukey);



    @Select({"<script>",
            "SELECT ID FROM ${tableName} WHERE MENUKEY IN",
            "<foreach item='menuKey' collection='menuKeys' open='(' separator=',' close=')'>",
            "#{menuKey}",
            "</foreach>",
            "</script>"})
    List<String> findActionsByMenuKeys(@Param("tableName") String tableName, @Param("menuKeys") List<String> menuKeys);


}