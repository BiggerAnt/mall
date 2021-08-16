package com.ant.mall.product.config;

import com.alibaba.csp.sentinel.adapter.spring.webflux.callback.DefaultBlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.spring.webflux.callback.WebFluxCallbackManager;
import org.springframework.context.annotation.Configuration;

/**
 * Sentinel返回数据自定义配置类
 * 还没完成
 */
//@Configuration
public class SentinelConfig {
    public SentinelConfig(){
        DefaultBlockRequestHandler blockRequestHandler = new DefaultBlockRequestHandler();
        WebFluxCallbackManager.setBlockHandler(blockRequestHandler);

    }
}
