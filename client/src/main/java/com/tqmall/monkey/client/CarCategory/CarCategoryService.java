package com.tqmall.monkey.client.CarCategory;


import com.tqmall.athena.domain.result.carcategory.CarCategoryDTO;
import com.tqmall.monkey.domain.bizBO.car.CarCategoryBO;

import java.util.List;

/**
 * Created by huangzhangting on 15/6/9.
 */
public interface CarCategoryService {

    List<CarCategoryDTO> getCarByPid(Integer pid);

    /**
     * 车管家页面，根据品牌id，查询车型
     * @param brandId
     * @return
     */
    List<CarCategoryDTO> getModelForMaintain(Integer brandId);

    /**
     * 车管家页面，根据车型id，查询车款（level 6）
     * @param modelId
     * @return
     */
    List<CarCategoryBO> getCarForMaintain(Integer modelId);
}
