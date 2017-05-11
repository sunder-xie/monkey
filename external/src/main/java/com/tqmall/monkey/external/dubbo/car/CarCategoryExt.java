package com.tqmall.monkey.external.dubbo.car;

import com.tqmall.athena.domain.result.carcategory.CarCategoryDTO;

import java.util.List;

/**
 * Created by huangzhangting on 16/2/29.
 */
public interface CarCategoryExt {
    List<CarCategoryDTO> getCarByPid(Integer pid);
}
