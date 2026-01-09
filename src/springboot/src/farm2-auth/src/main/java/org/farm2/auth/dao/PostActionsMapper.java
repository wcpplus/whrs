package org.farm2.auth.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.farm2.base.db.SuperMapper;
import org.farm2.auth.domain.PostActions;

import java.util.List;

/**
 * 角色权限
 *
 * @author cbtg自动生成  2025-1-7 10:07:59
 */
@Mapper
public interface PostActionsMapper extends SuperMapper<PostActions> {

    @Select("SELECT ACTIONSID FROM ${tableName} WHERE POSTID = #{postid}")
    List<String> findActionsByPostId(@Param("tableName") String tableName, String postid);


    @Select({"<script>",
            "SELECT ACTIONSID FROM ${tableName} WHERE POSTID IN",
            "<foreach item='postId' collection='postIds' open='(' separator=',' close=')'>",
            "#{postId}",
            "</foreach>",
            "</script>"})
    List<String> findActionsByPostIds(@Param("tableName") String tableName, @Param("postIds") List<String> postIds);
}