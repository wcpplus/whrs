package org.farm2.attendance.dao;

import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.SuperDao;
import org.farm2.attendance.domain.AttendanceSummary;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.FarmUUID;
import org.farm2.tools.db.commons.QueryRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**考勤结果 
 * @author cbtg自动生成  2026-1-6 15:34:44 
 */
@Repository
public class AttendanceSummaryDao extends SuperDao {
    @Autowired
    private AttendanceSummaryMapper attendanceSummaryMapper;
    private static String TABLE_NAME = "WHRS_ATTENDANCE_SUMMARY";
    private static String PRIMARY_KEY = "ID";


    public AttendanceSummary findById(String id) {
        return attendanceSummaryMapper.findById(TABLE_NAME, id);
    }

    public List<AttendanceSummary> findAll() {
        return attendanceSummaryMapper.findAll(TABLE_NAME);
    }

    public void insert(AttendanceSummary attendanceSummary) {
        insert(attendanceSummaryMapper, attendanceSummary, TABLE_NAME, PRIMARY_KEY);
    }

    public void update(AttendanceSummary attendanceSummary) {
        update(attendanceSummaryMapper, attendanceSummary, TABLE_NAME, PRIMARY_KEY);
    }


    public void deleteById(String id) {
        attendanceSummaryMapper.deleteById(TABLE_NAME,id);
    }

    public void delete(List<QueryRule> rules) {
        attendanceSummaryMapper.delete(TABLE_NAME, rules);
    }

    public DataResult queryData(DataQuery query) {
        return queryData(attendanceSummaryMapper, query, generateSelectFields(AttendanceSummary.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"));
    }

    public AttendanceSummary queryOne(List<QueryRule> rules) {
        return queryOne(attendanceSummaryMapper, DataQuery.getInstance().addRules(rules), generateSelectFields(AttendanceSummary.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"), AttendanceSummary.class);
    }
    public int countData(DataQuery query) {
        return countData(attendanceSummaryMapper, query, addAlias(TABLE_NAME, "A"));
    }

    public int countData(List<QueryRule> rules) {
        DataQuery query = DataQuery.getInstance().addRules(rules);
        query.setSql(generateSelectFields(AttendanceSummary.class, "A").getTitles(), addAlias(TABLE_NAME, "A"));
        return countData(attendanceSummaryMapper, query, addAlias(TABLE_NAME, "A"));
    }
    /**
     * 字段field上是否存在值value，id为id的记录除外（可以判断数据库中某个key是否存在的业务）
     *
     * @param excludeId 排除的id
     * @param fieldName
     * @param value
     * @return
     */
    public boolean isExist(String excludeId, String fieldName, String value) {
        if (StringUtils.isBlank(excludeId)) {
            excludeId = FarmUUID.getUUID32();
        }
        int n = attendanceSummaryMapper.isExist(TABLE_NAME, excludeId, fieldName, value);
        return n > 0;
    }
    
    
    public List<AttendanceSummary> find(List<QueryRule> rules) {
        return attendanceSummaryMapper.find(TABLE_NAME, rules);
    }
    
    
    /**[tree：树形结构使用]
    public List<AttendanceSummary> findSubNodes(String id) {
        return attendanceSummaryMapper.findSubNodes(TABLE_NAME, findById(id).getTreecode() + "%");
    }
    */
    /**[tree：树形结构使用]
    public List<AttendanceSummary> findByParentId(String parentId) {
        return attendanceSummaryMapper.findByParentId(TABLE_NAME, parentId);
    }
     */
}
