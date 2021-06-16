package com.ant.mall.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * 解决跨域问题配置类
 */
@Configuration
public class MallCorsConfiguration {

    @Bean
    public CorsWebFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //跨域配置
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //哪些请求头跨域跨域
        corsConfiguration.addAllowedHeader("*");
        //哪些请求方式可以跨域
        corsConfiguration.addAllowedMethod("*");
        //哪些请求来源可以跨域
        //corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedOriginPattern("*");
        //是否允许携带cookies进行跨域
        corsConfiguration.setAllowCredentials(true);

        //哪些路径需要进行跨域，这里配置的所有路径
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsWebFilter(source);
    }
}
