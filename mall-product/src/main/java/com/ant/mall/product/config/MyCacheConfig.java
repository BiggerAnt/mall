package com.ant.mall.product.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * spring cache的自定义配置类
 */
@EnableConfigurationProperties(CacheProperties.class)//开始属性配置绑定，绑定到CacheProperties，使它生效
@Configuration
public class MyCacheConfig {

//    @Autowired
//    private CacheProperties cacheProperties;

    @Bean
    RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties){
        //修改配置
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        //设置key-value的序列化方式
        config = config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));
        //value使用json方式序列化
        config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        // 使配置文件中的配置生效
        CacheProperties.Redis redisproperties = cacheProperties.getRedis();
        if(redisproperties.getTimeToLive() != null){
            config = config.entryTtl(redisproperties.getTimeToLive());
        }
        if(redisproperties.getKeyPrefix() != null){
            config = config.prefixCacheNameWith(redisproperties.getKeyPrefix());
        }
        if(!redisproperties.isCacheNullValues()){
            config = config.disableCachingNullValues();
        }
        if(!redisproperties.isUseKeyPrefix()){
            config = config.disableKeyPrefix();
        }
        return config;
    }
}
