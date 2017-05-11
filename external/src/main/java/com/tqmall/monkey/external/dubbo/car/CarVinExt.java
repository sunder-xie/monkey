package com.tqmall.monkey.external.dubbo.car;

import com.tqmall.athena.client.car.CarVinService;
import com.tqmall.athena.domain.result.carcategory.CarDTO;
import com.tqmall.core.common.entity.Result;
import com.tqmall.monkey.component.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by huangzhangting on 16/6/16.
 */
@Slf4j
@Component
public class CarVinExt {
    @Autowired
    private CarVinService carVinService;

    public List<CarDTO> getCarListByVin(String vin){
        if(!StringUtil.isVin(vin)){
            return new ArrayList<>();
        }
        Result<List<CarDTO>> result = carVinService.getCarListByVin(vin);
        if(result.isSuccess()){
            List<CarDTO> list = result.getData();
            Collections.sort(list, new Comparator<CarDTO>() {
                @Override
                public int compare(CarDTO o1, CarDTO o2) {
                    return o1.getCarName().compareTo(o2.getCarName());
                }
            });
            return list;
        }
        log.info("get car list from athena failed, vin:{}, result:{}", vin, result);
        return new ArrayList<>();
    }
}
