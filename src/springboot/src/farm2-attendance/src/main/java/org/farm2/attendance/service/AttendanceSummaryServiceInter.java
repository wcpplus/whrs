package org.farm2.attendance.service;

import org.farm2.attendance.dao.AttendanceDateDto;
import org.farm2.attendance.domain.AttendanceSummary;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;

import java.util.List;

/**
 * 考勤结果
 *
 * @author cbtg自动生成  2026-1-6 15:34:44
 */
public interface AttendanceSummaryServiceInter {

    public AttendanceSummary insertAttendanceSummaryEntity(AttendanceSummary attendanceSummary);

    public AttendanceSummary editAttendanceSummaryEntity(AttendanceSummary attendanceSummary);

    public void delAttendanceSummary(String id);

    public AttendanceSummary getAttendanceSummaryById(String id);

    public List<AttendanceSummary> getAttendanceSummarys(DataQuery query);

    public DataResult searchAttendanceSummary(DataQuery query);

    public int getAttendanceSummaryNum(DataQuery query);

    public int getNum(DataQuery query);

    /**
     * 封装用户考勤记录（按月为单位）
     *
     * @param loginname 用户key
     * @param yyyyMm    年月
     * @return
     */
    public List<AttendanceDateDto> getDate(String loginname, String yyyyMm);

    /**
     * 测试打卡
     *
     * @param userkey
     * @param date14
     */
    public void bitByTest(String userkey, String date14);

    /**
     * 测试打卡
     *
     * @param userkey
     * @param state
     * @param note
     */
    public void updateState(String userkey, String state, String time8, String note);
    /*[tree：树形结构使用]
    public void moveTreeNode(List<String> ids, String id);
    
    void autoSort(List<String> ids);
    */
}
