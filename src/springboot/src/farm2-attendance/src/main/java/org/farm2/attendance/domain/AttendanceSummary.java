package org.farm2.attendance.domain;

import lombok.Data;

/**
 * 考勤结果
 *
 * @author cbtg自动生成  2026-1-6 15:34:44
 */
@Data
public class AttendanceSummary {
    private String id;
    private String ctime;
    private String userkey;
    private String attendancetime;
    private Float workhours;
    private Integer latem;
    private Integer earlym;
    private Integer overtimem;
    private String absentis;
    private String exceptiontype;
    private String state;
    private String exemptnote;
    private String working;
    private String backup;
    private String sstime;
    private String sxtime;
    private String xstime;
    private String xxtime;
}