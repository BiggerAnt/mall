package com.ant.mall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ant.common.utils.PageUtils;
import com.ant.mall.member.entity.UndoLogEntity;

import java.util.Map;

/**
 * 
 *
 * @author lic
 * @email 18340032515@163.com
 * @date 2021-06-12 14:07:01
 */
public interface UndoLogService extends IService<UndoLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

