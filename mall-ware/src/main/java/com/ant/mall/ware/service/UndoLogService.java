package com.ant.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ant.common.utils.PageUtils;
import com.ant.mall.ware.entity.UndoLogEntity;

import java.util.Map;

/**
 * 
 *
 * @author lic
 * @email 18340032515@163.com
 * @date 2021-06-12 14:19:17
 */
public interface UndoLogService extends IService<UndoLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

