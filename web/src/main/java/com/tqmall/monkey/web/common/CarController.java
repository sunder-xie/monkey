package com.tqmall.monkey.web.common;

import com.tqmall.athena.domain.result.carcategory.CarDTO;
import com.tqmall.core.common.entity.Result;
import com.tqmall.monkey.client.CarCategory.CarCategoryService;
import com.tqmall.monkey.client.car.CarInfoAllService;
import com.tqmall.monkey.external.dubbo.car.CarVinExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zxg on 15/8/13.
 * 获得车型的公用controller
 */
@Controller
@RequestMapping("/monkey/car")
public class CarController {

    //力洋库的车型数据获得
    @Autowired
    private CarInfoAllService carInfoAllService;
    @Autowired
    private CarCategoryService carCategoryService;
    @Autowired
    private CarVinExt carVinExt;


    //获得力洋库的车型列表
    @RequestMapping(value = "/getLiyangCar", method = RequestMethod.GET)
    public
    @ResponseBody
    HashMap<String, Object> getLiyangCar(String carType, String carBrand, String factoryName, String carSeries) {
        HashMap<String, Object> result_map = new HashMap<>();
        List<String> resultList = new ArrayList<>();

        if (carType.equalsIgnoreCase("brand")) {
            resultList = this.carInfoAllService.findCarBrands();
        } else if (carType.equalsIgnoreCase("factory")) {
            resultList = this.carInfoAllService.findFactoryNamesByBrand(carBrand);

        } else if (carType.equalsIgnoreCase("series")) {
            resultList = this.carInfoAllService.findCarSeriesByBrand(carBrand, factoryName);

        } else if (carType.equalsIgnoreCase("model")) {
            resultList = this.carInfoAllService.findVehicleTypesBySeries(carBrand, factoryName, carSeries);

        }

        result_map.put("list", resultList);
        return result_map;
    }

    @RequestMapping("getCarByPid")
    @ResponseBody
    public Result getCarByPid(Integer pid){
        return Result.wrapSuccessfulResult(carCategoryService.getCarByPid(pid));
    }

    @RequestMapping("getCarByVin")
    @ResponseBody
    public Result<List<CarDTO>> getCarByVin(String vin){
        List<CarDTO> list = carVinExt.getCarListByVin(vin);
        if(list.isEmpty()){
            return Result.wrapErrorResult("", "未查到满足条件的车辆信息");
        }
        return Result.wrapSuccessfulResult(list);
    }

}
