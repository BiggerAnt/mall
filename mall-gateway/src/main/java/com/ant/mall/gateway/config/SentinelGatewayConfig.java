package com.ant.mall.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.fastjson.JSON;
import com.ant.common.utils.R;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
public class SentinelGatewayConfig {
    //TODO 响应式编程
    public SentinelGatewayConfig() {
        GatewayCallbackManager.setBlockHandler(new BlockRequestHandler() {
            /**
             * 网关限流了就会回调此方法
             * @param exchange
             * @param t
             * @return
             */
            @Override
            public Mono<ServerResponse> handleRequest(ServerWebExchange exchange, Throwable t) {
                //响应数据
                R error = R.error(400, "请求过多");
                String json = JSON.toJSONString(error);
                Mono<ServerResponse> responseMono = ServerResponse.ok().body(Mono.just(json), String.class);
                return responseMono;
            }
        });
    }
}
