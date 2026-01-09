package org.farm2.base.db;

import org.apache.ibatis.annotations.*;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.commons.QueryRule;

import java.util.List;
import java.util.Map;

public interface SuperMapper<T> {

    @Select("SELECT * FROM ${tableName}")
    List<T> findAll(@Param("tableName") String tableName);

    @Select("SELECT * FROM ${tableName} WHERE  SOURCETYPE = #{sourcetype} ")
    List<T> findBySourceType(@Param("tableName") String tableName, String sourcetype);

    @Delete("DELETE FROM ${tableName} WHERE id = #{id}")
    void deleteById(@Param("tableName") String tableName, String id);

    @Select("SELECT COUNT(ID) FROM ${tableName} WHERE id != #{id} AND ${field} = #{value}")
    int isExist(@Param("tableName") String tableName, String id, String field, String value);

    @Select("SELECT * FROM ${tableName} WHERE id = #{id}")
    T findById(@Param("tableName") String tableName, @Param("id") String id);

    @InsertProvider(type = DataSQLBuilder.class, method = "getInsertSql")
    void insert(@Param("values") Map<String, Object> values, @Param("tableName") String tableName);

    @UpdateProvider(type = DataSQLBuilder.class, method = "getUpdateSql")
    void update(@Param("values") Map<String, Object> values, @Param("tableName") String tableName, @Param("primaryKey") String primaryKey);

    //-------------------------------------------------------------------------------------------------
    @SelectProvider(type = DataQueryBuilder.class, method = "getDataSql")
    List<Map<String, Object>> dataQuery(@Param("query") DataQuery query);

    @SelectProvider(type = DataQueryBuilder.class, method = "getCountSql")
    int countQuery(@Param("query") DataQuery query);


    //----------------------------------------------------------------------------------
    @DeleteProvider(type = DataSQLBuilder.class, method = "getDeleteSql")
    void delete(@Param("tableName") String tableName, @Param("rules") List<QueryRule> rules);

    @SelectProvider(type = DataSQLBuilder.class, method = "getDataSqlByRules")
    List<T> find(@Param("tableName") String tableName, @Param("rules") List<QueryRule> rules);

    @Select("SELECT * FROM ${tableName} WHERE  TREECODE LIKE #{treecode} ORDER BY CTIME ")
    List<T> findSubNodes(@Param("tableName") String tableName, String treecode);


    @Select("SELECT * FROM ${tableName} WHERE  PARENTID = #{parentId} ")
    List<T> findByParentId(@Param("tableName") String tableName, String parentId);


    @Select("SELECT MAX(${field}) FROM ${tableName} ")
    Integer getMaxIntVal(@Param("tableName") String tableName, String field);

}