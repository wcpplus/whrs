package org.farm2.luser.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.farm2.base.db.SuperMapper;
import org.farm2.luser.domain.LocalUser;

@Mapper
public interface LocalUserMapper extends SuperMapper<LocalUser> {
    @Select("SELECT * FROM ${tableName} WHERE loginname = #{loginname}")
    LocalUser findByLoginname(@Param("tableName") String tableName, @Param("loginname") String loginname);
}