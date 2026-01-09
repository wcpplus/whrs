package org.farm2.luser.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.farm2.base.db.SuperMapper;
import org.farm2.luser.domain.LocalOrg;

import java.util.List;

/**组织机构 
 * @author cbtg自动生成  2025-1-2 22:01:57 
 */
@Mapper
public interface LocalOrgMapper extends SuperMapper<LocalOrg> {

    @Select("SELECT * FROM ${tableName} where name = #{orgname}")
    List<LocalOrg> findByName(@Param("tableName") String tableName, String orgname);
}