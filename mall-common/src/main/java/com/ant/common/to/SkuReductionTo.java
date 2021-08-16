package com.ant.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuReductionTo {

    private Long skuId;

    private BigDecimal fullPrice;

    private BigDecimal reducePrice;

    private int priceStatus;

    private int fullCount;

    private BigDecimal discount;

    private int countStatus;

    /**
     * 会员价格
     */
    private List<MemberPrice> memberPrice;
}
