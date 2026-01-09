package org.farm2.attendance.dao;

import lombok.Data;
import org.farm2.tools.db.DataQueryDto;

@Data
public class DateDataQueryDto {
    private DataQueryDto query;
    private String yyyyMm;
}
