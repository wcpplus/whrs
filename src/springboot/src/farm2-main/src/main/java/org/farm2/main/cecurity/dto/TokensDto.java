package org.farm2.main.cecurity.dto;

import lombok.Data;

/**
 * 刷新jwt参数封装
 */
@Data
public class TokensDto {
    private String jwt;//jwt验证key
    private String longKey;//jwt刷新key
}
