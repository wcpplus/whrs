package org.farm2.attendance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 考勤结果
 *
 * @author cbtg自动生成  2026-1-6 15:34:44
 */
@Data
@NoArgsConstructor
public class AttendanceSummaryDto {
    //@RegExValidator(type = ValidType.loginName)//自定义校验方法

    private String id;

    private String ctime;

    @Size(min = 0, max = 64, message = "{farm2.validate.template.size}")
    private String userkey;

    private String attendancetime;

    private Float workhours;

    private Integer latem;

    private Integer earlym;

    private Integer overtimem;

    private String absentis;

    private String exceptiontype;

    private String state;

    @Size(min = 0, max = 256, message = "{farm2.validate.template.size}")
    private String exemptnote;

    private String working;

    private String backup;

    private String sstime;

    private String sxtime;

    private String xstime;

    private String xxtime;
}