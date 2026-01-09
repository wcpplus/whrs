package org.farm2.auth.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.farm2.base.db.SuperMapper;
import org.farm2.auth.domain.UserPost;

import java.util.List;

/**用户角色 
 * @author cbtg自动生成  2025-1-7 12:31:51 
 */
@Mapper
public interface UserPostMapper extends SuperMapper<UserPost> {
    @Select("SELECT POSTID FROM ${tableName} WHERE USERKEY = #{loginname}")
    List<String> findPostIdsByUserKey(@Param("tableName") String tableName, String loginname);
}