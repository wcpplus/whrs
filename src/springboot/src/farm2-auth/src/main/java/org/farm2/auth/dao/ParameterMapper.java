package org.farm2.auth.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.farm2.base.db.SuperMapper;
import org.farm2.auth.domain.Parameter;

/**
 * 系统参数
 *
 * @author cbtg自动生成  2025-1-8 10:39:16
 */
@Mapper
public interface ParameterMapper extends SuperMapper<Parameter> {
    @Select("SELECT * FROM ${tableName} WHERE PKEY = #{key}")
    Parameter findByKey(@Param("tableName") String tableName, @Param("key") String key);
}