package org.farm2.main.cecurity.config;

import org.apache.commons.lang3.StringUtils;
import org.farm2.base.password.FarmPasswordEncoder;
import org.farm2.main.cecurity.filter.JwtAuthenticationTokenFilter;
import org.farm2.main.exceptions.handle.SecurityAccessExceptionHandler;
import org.farm2.main.exceptions.handle.SecurityAuthExceptionPoint;
import org.farm2.tools.config.Farm2ConfigEnum;
import org.farm2.tools.config.Farm2ConfigUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * 覆盖spring-security框架的校验规则
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private UserDetailsService farmUserDetailsServiceImpl;
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        //使用自定义的登录密码编码器
//        return new PasswordEncoder;
//    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        //确保在登录方法中能注入使用authenticationManager作为登录验证服務
        return authenticationConfiguration.getAuthenticationManager();
    }


    private final SecurityAuthExceptionPoint securityAuthExceptionPoint;
    private final SecurityAccessExceptionHandler securityAccessExceptionHandler;

    public SecurityConfig(SecurityAuthExceptionPoint jwtAuthenticationEntryPoint, SecurityAccessExceptionHandler jwtAccessDeniedHandler) {
        this.securityAuthExceptionPoint = jwtAuthenticationEntryPoint;
        this.securityAccessExceptionHandler = jwtAccessDeniedHandler;
    }

    /**
     * 跨域请求配置
     *
     * @return
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        if (StringUtils.isNotBlank(Farm2ConfigUtils.getInstance(Farm2ConfigEnum.FARM2_PROPERTIES).getData("farm2.conf.web.domain.able.url"))) {
            //是否启用跨域支持
            configuration.setAllowedOrigins(Farm2ConfigUtils.getInstance(Farm2ConfigEnum.FARM2_PROPERTIES).getDatas("farm2.conf.web.domain.able.url"));// 替换为前端的实际域名
        }
        //configuration.setAllowedOrigins(List.of("*"));//支持所有前端域名
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //关闭csrf
        http.csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(corsConfigurationSource()))
                //不通过session获取securityContext
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 配置headers，允许同源iframe加载
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
                //
                .authorizeHttpRequests(authorize -> authorize
                        //课程学习
                        .requestMatchers("/api/wlearn/**").permitAll()
                        //课程学习
                        .requestMatchers("/api/wunit/**").permitAll()
                        //课程学习
                        .requestMatchers("/api/wcourse/**").permitAll()
                        //培训计划
                        .requestMatchers("/api/wplans/**").permitAll()
                        //图编辑器功能
                        .requestMatchers("/api/wgraph/**").permitAll()
                        //图谱功能
                        .requestMatchers("/api/wrelation/**").permitAll()
                        //积分功能
                        .requestMatchers("/api/wpoint/**").permitAll()
                        //AI聊天功能
                        .requestMatchers("/api/wfqa/**").permitAll()
                        //视图功能
                        .requestMatchers("/api/wview/**").permitAll()
                        //文件预览功能
                        .requestMatchers("/api/wfile/**").permitAll()
                        //获取系统参数配置
                        .requestMatchers("/api/current/config").permitAll()
                        //获取当前用户信息
                        .requestMatchers("/api/current/user").permitAll()
                        //AI聊天功能
                        .requestMatchers("/api/wchat/**").permitAll()
                        //AI客户端测试
                        .requestMatchers("/api/llmtest/stream").permitAll()
                        //进度处理信息
                        .requestMatchers("/api/process/**").permitAll()
                        //前端标签
                        .requestMatchers("/api/wtag/**").permitAll()
                        //前端分类
                        .requestMatchers("/api/wtype/**").permitAll()
                        // 检索权限
                        .requestMatchers("/api/search/**").permitAll()
                        // 知识访问权限
                        .requestMatchers("/api/wknow/**").permitAll()
                        // 允许登录路径
                        .requestMatchers("/api/login/**").permitAll()
                        // 社交功能
                        .requestMatchers("/api/social/**").permitAll()
                        // 接口文档允许访问
                        .requestMatchers("/apidoc/**").permitAll()
                        // 接口文档
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        //文件下载
                        .requestMatchers("/api/files/download/**").permitAll()
                        //文件播放
                        .requestMatchers("/api/files/media/**").permitAll()
                        //扩展文件下载
                        .requestMatchers("/api/exfiles/download/**").permitAll()
                        //测试数据
                        .requestMatchers("/api/wdemo/**").permitAll()
                        // 所有其他请求都需要认证
                        .anyRequest().authenticated());
       //配置自定义错误处理机制
        http.exceptionHandling(exception -> exception
                //用于处理未认证的请求异常处理
                .authenticationEntryPoint(securityAuthExceptionPoint)
                //通过了认证，但没有足够的权限异常处理
                .accessDeniedHandler(securityAccessExceptionHandler)
        );
        //配置jwt认证过滤器在验证密码之前
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
