package org.farm2.main.openapi.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info().title("Farm2接口文档")
                .description("基础框架").version("v1")).externalDocs(new ExternalDocumentation().description("wcp知识协作系统").url("http://www.wcpdoc.com"));
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("用户管理")
                .packagesToScan("org.farm2.luser.controller")  // 指定包含用户管理API的包
               // .pathsToMatch("/users/**")  // 指定匹配的路径
                .build();
    }

    @Bean
    public GroupedOpenApi loginApi() {
        return GroupedOpenApi.builder()
                .group("登录注销")
                .packagesToScan("org.farm2.main.cecurity")  // 指定包含用户管理API的包
                // .pathsToMatch("/users/**")  // 指定匹配的路径
                .build();
    }

}
