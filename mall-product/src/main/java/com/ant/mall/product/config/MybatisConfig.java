package com.ant.mall.product.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableTransactionManagement  //开启事务
@MapperScan("com/ant/mall/product/dao")
public class MybatisConfig {
    //引入分页插件
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        //设置请求大于最后一页的操作,true回到首页,false继续请求
        paginationInterceptor.setOverflow(true);
        //每页最大限制
        paginationInterceptor.setLimit(1000);
        return paginationInterceptor;
    }
}
