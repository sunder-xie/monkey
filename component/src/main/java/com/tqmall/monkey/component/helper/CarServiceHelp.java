package com.tqmall.monkey.component.helper;

import com.tqmall.athena.domain.result.carcategory.CarCategoryDTO;

import java.util.List;

/**
 * 车有关的外部接口Service
 * Created by zxg on 15/8/10.
 */
public interface CarServiceHelp {


    //根据力洋Id 获得电商Car对象
    public CarCategoryDTO getOnlineCarCategoryDTOByLi(String liyangId);

    //根据carId，获得其所有父对象
    public List<CarCategoryDTO> getParentsByCarId(Integer carId);
}
