package com.ant.mall.search.config;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.nio.protocol.HttpAsyncResponseConsumer;
import org.elasticsearch.client.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * elasticsearch配置类
 */
@Configuration
public class ElasticSearchConfig {
    public static final RequestOptions COMMON_OPTIONS;
    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
//        builder.setHttpAsyncResponseConsumerFactory(new HttpAsyncResponseConsumerFactory
//                .HeapBufferedResponseConsumerFactory(30*1024*1024*1024));
        COMMON_OPTIONS = builder.build();
    }

    @Bean
    public RestHighLevelClient esRestClient(){
        RestClientBuilder builder = RestClient.builder(new HttpHost("192.168.2.197",9200,"http"));
        RestHighLevelClient client = new RestHighLevelClient(builder);
        return client;
    }
}
