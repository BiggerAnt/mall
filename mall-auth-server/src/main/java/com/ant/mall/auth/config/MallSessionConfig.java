package com.ant.mall.auth.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * 共享session配置类，每个需要共享session的服务都需要有这个配置类，并且配置同样的DomainName和CookieName
 */
@Configuration
public class MallSessionConfig {

    /**
     * session共享的具体配置
     * @return
     */
    @Bean
    public CookieSerializer cookieSerializer(){
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
        //设置session的作用域
        cookieSerializer.setDomainName("mall.com");
        //设置cookie名字
        cookieSerializer.setCookieName("SESSION");

        return cookieSerializer;
    }

    /**
     * RedisSession序列化器，将session作为json存储
     * @return
     */
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer(){
        return new GenericJackson2JsonRedisSerializer();
    }
}
