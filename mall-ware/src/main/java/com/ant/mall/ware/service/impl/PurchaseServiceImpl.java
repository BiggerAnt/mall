package com.ant.mall.ware.service.impl;

import com.ant.common.constant.WareConstant;
import com.ant.mall.ware.entity.PurchaseDetailEntity;
import com.ant.mall.ware.service.PurchaseDetailService;
import com.ant.mall.ware.service.WareSkuService;
import com.ant.mall.ware.vo.MergeVo;
import com.ant.mall.ware.vo.PurchaseDoneVo;
import com.ant.mall.ware.vo.PurchaseItemDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ant.common.utils.PageUtils;
import com.ant.common.utils.Query;

import com.ant.mall.ware.dao.PurchaseDao;
import com.ant.mall.ware.entity.PurchaseEntity;
import com.ant.mall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    private PurchaseDetailService purchaseDetailService;

    @Autowired
    private WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceive(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>().eq("status",0).or().eq("status", 1)
        );

        return new PageUtils(page);
    }

    /**
     * 合并采购单
     * @param mergeVo
     */
    @Transactional
    @Override
    public void mergePurchase(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();
        if (purchaseId == null){
            //新建采购单
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();

        }
        //合并
        List<Long> items = mergeVo.getItems();
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> detailEntities = items.stream().map(item -> {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setId(item);
            purchaseDetailEntity.setPurchaseId(finalPurchaseId);
            purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
            return purchaseDetailEntity;
        }).collect(Collectors.toList());

        purchaseDetailService.updateBatchById(detailEntities);
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseId);
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);
    }

    /**
     * 领取采购单
     * @param ids
     */
    @Override
    public void received(List<Long> ids) {
        if(ids == null || ids.size() == 0){
            return;
        }
        // 1.确认当前采购单是已分配状态
        List<PurchaseEntity> collect = ids.stream().map(id -> this.getById(id)
                // 只能采购已分配的
        ).filter(item -> item.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode() || item.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode())
                .map(item -> {
                    item.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode());
                    item.setUpdateTime(new Date());
                    return item;
                }).collect(Collectors.toList());
        // 2.被领取之后重新设置采购状态
        this.updateBatchById(collect);

        // 3.改变采购项状态
        collect.forEach(item -> {
            List<PurchaseDetailEntity> entities = purchaseDetailService.listDetailByPurchaseId(item.getId());

            // 收集所有需要更新的采购单id
            List<PurchaseDetailEntity> detailEntities = entities.stream().map(entity -> {
                PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
                detailEntity.setId(entity.getId());
                detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.BUYING.getCode());
                return detailEntity;
            }).collect(Collectors.toList());
            // 根据id  批量更新
            purchaseDetailService.updateBatchById(detailEntities);
        });
    }

    /**
     * {
     * 	"id":"1",
     * 	"items":[
     *                {"itemId":1,"status":3,"reason":""},
     *        {"itemId":3,"status":4,"reason":"无货"}
     * 	]
     * }
     *
     * id：		采购单id
     * items：	采购项
     * itemId：	采购需求id
     * status：	采购状态
     */
    @Transactional
    @Override
    public void done(PurchaseDoneVo doneVo) {
        // 1.改变采购单状态
        Long id = doneVo.getId();
        Boolean flag = true;
        List<PurchaseItemDoneVo> items = doneVo.getItems();
        ArrayList<PurchaseDetailEntity> updates = new ArrayList<>();
        double price;
        double p = 0;
        double sum = 0;
        // 2.改变采购项状态
        for (PurchaseItemDoneVo item : items) {
            // 采购失败的情况
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            if(item.getStatus() == WareConstant.PurchaseDetailStatusEnum.FAILED.getCode()){
                flag = false;
                detailEntity.setStatus(item.getStatus());
            }else{
                detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.FINISH.getCode());
                // 3.将成功采购的进行入库
                // 查出当前采购项的详细信息
                PurchaseDetailEntity entity = purchaseDetailService.getById(item.getItemId());
                // skuId、到那个仓库、sku名字
                price = wareSkuService.addStock(entity.getSkuId(), entity.getWareId(), entity.getSkuNum());
                if(price != p){
                    p = entity.getSkuNum() * price;
                }
                detailEntity.setSkuPrice(new BigDecimal(p));
                sum += p;
            }
            // 设置采购成功的id
            detailEntity.setId(item.getItemId());
            updates.add(detailEntity);
        }
        // 批量更新采购单
        purchaseDetailService.updateBatchById(updates);

        // 对采购单的状态进行更新
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(id);
        purchaseEntity.setAmount(new BigDecimal(sum));
        purchaseEntity.setStatus(flag?WareConstant.PurchaseStatusEnum.FINISH.getCode():WareConstant.PurchaseStatusEnum.HASERR.getCode());
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);
    }
}