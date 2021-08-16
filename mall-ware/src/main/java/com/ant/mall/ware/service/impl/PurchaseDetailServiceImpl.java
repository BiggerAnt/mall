package com.ant.mall.ware.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ant.common.utils.PageUtils;
import com.ant.common.utils.Query;

import com.ant.mall.ware.dao.PurchaseDetailDao;
import com.ant.mall.ware.entity.PurchaseDetailEntity;
import com.ant.mall.ware.service.PurchaseDetailService;
import org.springframework.util.ObjectUtils;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<PurchaseDetailEntity> queryWrapper = new QueryWrapper<>();

        if(!ObjectUtils.isEmpty(params.get("key"))){
            queryWrapper.and(wrapper -> {
                wrapper.eq("purchase_id",params.get("key")).or().eq("sku_id",params.get("key"));
            });
        }

        if(!ObjectUtils.isEmpty(params.get("status"))){
            queryWrapper.eq("status",params.get("status"));
        }

        if(!ObjectUtils.isEmpty(params.get("wareId"))){
            queryWrapper.eq("ware_id",params.get("wareId"));
        }

        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    /**
     * 根据采购单id 改变采购项
     */
    @Override
    public List<PurchaseDetailEntity> listDetailByPurchaseId(Long id) {
        // 获取所有采购项
        List<PurchaseDetailEntity> entities = this.list(new QueryWrapper<PurchaseDetailEntity>().eq("purchase_id", id));
        return entities;
    }
}