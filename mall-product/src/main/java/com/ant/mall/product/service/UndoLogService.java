package com.ant.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ant.common.utils.PageUtils;
import com.ant.mall.product.entity.UndoLogEntity;

import java.util.Map;

/**
 * 
 *
 * @author lic
 * @email lic@gmail.com
 * @date 2021-06-10 16:51:01
 */
public interface UndoLogService extends IService<UndoLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

