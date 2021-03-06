package com.ant.mall.member.controller;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.ant.mall.member.feign.CouponFeignService;
import com.ant.mall.member.vo.UserRegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ant.mall.member.entity.MemberEntity;
import com.ant.mall.member.service.MemberService;
import com.ant.common.utils.PageUtils;
import com.ant.common.utils.R;



/**
 * 会员
 *
 * @author lic
 * @email 18340032515@163.com
 * @date 2021-06-12 14:07:01
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;


    @Autowired
    CouponFeignService couponFeignService;
    /**
     * 远程调用测试，发起调用方
     * @return
     */
    @RequestMapping(value = "/coupons")
    public R test() {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setNickname("张三");
        R memberCoupons = couponFeignService.memberCoupons();
        return R.ok().put("member" , memberEntity).put("coupons", memberCoupons.get("coupons"));
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("member:member:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("member:member:info")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("member:member:save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public R regis(@RequestBody UserRegisterVo userRegisterVo){
        memberService.regis(userRegisterVo);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("member:member:update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("member:member:delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
