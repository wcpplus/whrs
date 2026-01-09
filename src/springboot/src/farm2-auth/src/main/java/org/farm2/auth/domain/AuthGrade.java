package org.farm2.auth.domain;

import lombok.Data;

/**职级 
 * @author cbtg自动生成  2026-1-5 11:41:21 
 */
@Data
public class AuthGrade {
    private String id;
    private String name;
    private String keyid;
    private String tracktype;
    private Integer sortcode;
    private Integer minsalary;
    private Integer maxsalary;
}