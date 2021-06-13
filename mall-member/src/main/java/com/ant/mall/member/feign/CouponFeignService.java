package com.ant.mall.member.feign;

import com.ant.common.utils.R;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *  想要调用远程服务
 *  1、引入openfeign
 *  2、编写一个接口，告诉springcloud这个接口需要调用远程服务
 *   1)、声明接口的每一个方法都是调用哪个远程服务的哪个请求
 *  3、开启远程调用功能
 */
@FeignClient("mall-coupon") //声明式远程调用，需要远程调用哪个服务
@ComponentScan
public interface CouponFeignService {
    //需要远程调用的请求(方法)
    @RequestMapping(value = "/coupon/coupon/member/list")
    R memberCoupons();
}
