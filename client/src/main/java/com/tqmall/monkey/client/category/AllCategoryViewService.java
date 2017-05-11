package com.tqmall.monkey.client.category;

import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.category.AllCategoryViewDO;

import java.util.List;
import java.util.Map;

/**
 * Created by huangzhangting on 15/6/29.
 */
public interface AllCategoryViewService {
    public Page<AllCategoryViewDO> findAllCategoryPage(Integer cateId, Integer level, String vehicleCode, String partName, int pageIndex, int pageSize);

    //查询未停用的数据
    List<AllCategoryViewDO> findAllCategory(Integer cateId, Integer level, String vehicleCode, String partName);

    //查询所有数据
    List<AllCategoryViewDO> findAllCategorys(Integer cateId, Integer level, String vehicleCode, String partName);
}
