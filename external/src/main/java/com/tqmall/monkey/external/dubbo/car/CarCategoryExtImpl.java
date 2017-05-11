package com.tqmall.monkey.external.dubbo.car;

import com.tqmall.athena.client.car.CarCategoryService;
import com.tqmall.athena.domain.result.carcategory.CarCategoryDTO;
import com.tqmall.core.common.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangzhangting on 16/2/29.
 */
@Service
public class CarCategoryExtImpl implements CarCategoryExt {
    @Autowired
    private CarCategoryService carAthenaService;

    @Override
    public List<CarCategoryDTO> getCarByPid(Integer pid) {
        if(pid==null || pid<0){
            return new ArrayList<>();
        }
        Result<List<CarCategoryDTO>> result = carAthenaService.getCarCategoryByPid(pid);
        if(result.isSuccess()){
            return result.getData();
        }
        return new ArrayList<>();
    }
}
