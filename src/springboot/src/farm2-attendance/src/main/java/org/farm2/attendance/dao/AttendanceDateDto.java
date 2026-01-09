package org.farm2.attendance.dao;

import lombok.Data;
import org.farm2.attendance.dto.AttendanceSummaryDto;

/**
 * 封装一个日历天
 */
@Data
public class AttendanceDateDto {
    private String yyyy;
    private String mm;
    private String dd;
    private String week;
    private AttendanceSummaryDto summary;
}
