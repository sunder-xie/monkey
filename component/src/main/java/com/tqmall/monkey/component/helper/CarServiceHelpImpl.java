package com.tqmall.monkey.component.helper;

import com.tqmall.athena.client.car.CarCategoryService;
import com.tqmall.athena.domain.result.carcategory.CarCategoryDTO;
import com.tqmall.core.common.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zxg on 15/8/10.
 */
@Service
public class CarServiceHelpImpl implements CarServiceHelp {

    @Autowired
    private CarCategoryService carAthenaService;


    @Override
    public CarCategoryDTO getOnlineCarCategoryDTOByLi(String liyangId) {
        CarCategoryDTO carCategoryDTO ;

        Result<CarCategoryDTO> dubboResult = carAthenaService.queryCarCatInfoByLId(liyangId);
        carCategoryDTO = dubboResult.getData();

        if(null == carCategoryDTO){
            carCategoryDTO = new CarCategoryDTO();
        }


        return carCategoryDTO;
    }

    @Override
    public List<CarCategoryDTO> getParentsByCarId(Integer carId) {

        Result<List<CarCategoryDTO>> result = carAthenaService.getCarParentsByCarId(carId);
        List<CarCategoryDTO> list = result.getData();
        return list;
    }
}
