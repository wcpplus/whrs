package org.farm2.base.domain;

import lombok.Data;

/**
 * 业务对象信息封装
 */
@Data
public class MinAppInfo {
    private FarmUserMin cuser;
    private String id;
    private String title;
}
