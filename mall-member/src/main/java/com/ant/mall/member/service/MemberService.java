package com.ant.mall.member.service;

import com.ant.mall.member.exception.PhoneExistException;
import com.ant.mall.member.exception.UserNameExistException;
import com.ant.mall.member.vo.MemberLoginVo;
import com.ant.mall.member.vo.SocialUser;
import com.ant.mall.member.vo.UserRegisterVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ant.common.utils.PageUtils;
import com.ant.mall.member.entity.MemberEntity;

import java.util.Map;

/**
 * 会员
 *
 * @author lic
 * @email 18340032515@163.com
 * @date 2021-06-12 14:07:01
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void regis(UserRegisterVo userRegisterVo);

    void checkPhone(String phone) throws PhoneExistException;

    void checkUserName(String username) throws UserNameExistException;

    /**
     * 普通登录
     */
    MemberEntity login(MemberLoginVo vo);

    /**
     * 社交登录
     */
    MemberEntity login(SocialUser socialUser);
}

