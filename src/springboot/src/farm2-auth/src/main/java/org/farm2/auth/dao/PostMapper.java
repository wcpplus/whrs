package org.farm2.auth.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.farm2.base.db.SuperMapper;
import org.farm2.auth.domain.Post;

import java.util.List;

/**角色 
 * @author cbtg自动生成  2025-1-6 15:10:13 
 */
@Mapper
public interface PostMapper extends SuperMapper<Post> {
    @Select("SELECT * FROM ${tableName} WHERE  KEYID = #{keyid} ")
    List<Post> findByKeyid(String tableName, String keyid);

    @Select("SELECT * FROM ${tableName} WHERE  NAME = #{name} ")
    List<Post> findByName(String tableName, String name);
}