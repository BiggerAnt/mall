package com.ant.mall.search.service;

import com.ant.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * <p>Title: ProductSaveService</p>
 * Description：
 * date：2020/6/8 21:15
 */
public interface ProductSaveService {


	boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;
}
