package com.tqmall.monkey.rest.test;

import com.alibaba.fastjson.JSON;
import com.tqmall.athena.client.car.CarCategoryService;
import com.tqmall.athena.domain.result.carcategory.CarCategoryDTO;
import com.tqmall.athena.domain.result.carcategory.CarListDTO;
import com.tqmall.athena.domain.result.carcategory.CarListSuit4GoodsDTO;
import com.tqmall.athena.domain.result.carcategory.HotCarBrandDTO;
import com.tqmall.core.common.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by zxg on 15/10/16.
 */
@RestController
@RequestMapping("/rest/athenaCar")
public class AthenaCarDubboRestController {

    @Autowired
    private CarCategoryService carAthenaService;


    @RequestMapping(value = "/testGetAllCarCategory", method = RequestMethod.GET)
    public String testGetAllCarCategory(){
        Result<List<CarCategoryDTO>> result = carAthenaService.getAllCarCategory();
        List<CarCategoryDTO> list = result.getData();
        System.out.println("=====================testGetAllCarCategory:"+list.size());
        for(CarCategoryDTO carCategoryDTO:list){
            System.out.println(carCategoryDTO.getId()+" "+carCategoryDTO.getName()+" pid:"+carCategoryDTO.getPid());
        }
        System.out.println("=====================end:");
        return JSON.toJSONString(result);
    }

    @RequestMapping(value = "/testGetCarCategoryByPid", method = RequestMethod.GET)
    public String testGetCarCategoryByPid(Integer pid){
//        Integer pid = 70158;
        Result<List<CarCategoryDTO>> result = carAthenaService.getCarCategoryByPid(pid);
        List<CarCategoryDTO> list = result.getData();
        System.out.println("=====================testGetCarCategoryByPid:"+list.size());
        for(CarCategoryDTO carCategoryDTO:list){
            System.out.println(carCategoryDTO.getId()+" "+carCategoryDTO.getName()+" pid:"+carCategoryDTO.getPid()+" level:");
        }

        System.out.println("=====================end:");
        return JSON.toJSONString(result);
    }

    @RequestMapping(value = "/testGetCarParentsByCarId", method = RequestMethod.GET)
    public String testGetCarParentsByCarId(Integer carId){
//        Integer carId = 48761;
        Result<List<CarCategoryDTO>> result = carAthenaService.getCarParentsByCarId(carId);
        List<CarCategoryDTO> list = result.getData();
        System.out.println("=====================testGetCarParentsByCarId:");
        for(CarCategoryDTO carCategoryDTO:list){
            System.out.println(carCategoryDTO.getId()+" "+carCategoryDTO.getName()+" pid:"+carCategoryDTO.getPid());
        }
        System.out.println("=====================end:");

        return JSON.toJSONString(result);
    }

    @RequestMapping(value = "/testQueryCarCatInfoByLId", method = RequestMethod.GET)
    public String testQueryCarCatInfoByLId(String liyangId){
//        String liyangId = "ACC0133A0001";
        Result<CarCategoryDTO> result = carAthenaService.queryCarCatInfoByLId(liyangId);
        CarCategoryDTO carCategoryDTO = result.getData();
        System.out.println("=====================testQueryCarCatInfoByLId:");
        System.out.println(carCategoryDTO.getId() + " " + carCategoryDTO.getName() + " :level" + carCategoryDTO.getLevel() + " pid:" + carCategoryDTO.getPid());

        return JSON.toJSONString(result);
    }


    @RequestMapping(value = "/testCategoryCarInfo", method = RequestMethod.GET)
    public String testCategoryCarInfo(Integer pid){
//        Integer pid = 76237;//奥迪 进口 A8L level 3

        Result<List<CarListDTO>> result = carAthenaService.categoryCarInfo(pid);
        List<CarListDTO> list = result.getData();
        System.out.println("=====================testCategoryCarInfo:" + list.size());
        for(CarListDTO carCategoryDTO:list){
            System.out.println(carCategoryDTO.getId()+" "+carCategoryDTO.getCarName()+" pid:"+carCategoryDTO.getPid());
        }
        System.out.println("=====================end:");
        return JSON.toJSONString(result);
    }

    @RequestMapping(value = "/testGetCartListByGoodsId", method = RequestMethod.GET)
    public String testGetCartListByGoodsId(Integer carId){
//        Integer carId = 9289;
        Result<List<CarListSuit4GoodsDTO>> result = carAthenaService.getCartListByGoodsId(carId);
        List<CarListSuit4GoodsDTO> list = result.getData();
        System.out.println("=====================testGetCarParentsByCarId:"+list.size());

        System.out.println(JSON.toJSONString(list));
        return JSON.toJSONString(result);
    }


    @RequestMapping(value = "/testHotCar", method = RequestMethod.GET)
    public String testHotCar(Integer cityId){
//        Integer cityId = 52;
        Result<List<HotCarBrandDTO>> result = carAthenaService.getHotCarBrandList(cityId);

        System.out.println(JSON.toJSONString(result));
        return JSON.toJSONString(result);
    }


}
