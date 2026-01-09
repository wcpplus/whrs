package org.farm2.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class SearchDto {
    //@RegExValidator(type = ValidType.loginName)//自定义校验方法
    //@NotBlank(message = "{farm2.validate.template.notnull}")
    private String word;
    private List<SearchLimitDto> limits;
    private int page;
    private String model;//KNOW_DOC/FILE\
    private int pagesize;
}
