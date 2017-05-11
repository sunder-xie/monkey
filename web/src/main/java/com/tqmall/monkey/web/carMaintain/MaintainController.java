package com.tqmall.monkey.web.carMaintain;

import com.tqmall.core.common.entity.Result;
import com.tqmall.monkey.client.CarCategory.CarCategoryService;
import com.tqmall.monkey.client.carMaintain.MaintainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by huangzhangting on 16/2/29.
 */

@Controller
@RequestMapping("monkey/maintain")
public class MaintainController {
    @Autowired
    private MaintainService maintainService;
    @Autowired
    private CarCategoryService carCategoryService;


    @RequestMapping("index")
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView("monkey/carMaintain/index");
        return mv;
    }

    @RequestMapping("detail")
    @ResponseBody
    public Result getDetail(Integer carId, Integer mileage){
        return maintainService.getMaintainDetail(carId, mileage);
    }

    @RequestMapping("carModels")
    @ResponseBody
    public Result carModels(Integer brandId){
        return Result.wrapSuccessfulResult(carCategoryService.getModelForMaintain(brandId));
    }

    @RequestMapping("cars")
    @ResponseBody
    public Result cars(Integer modelId){
        return Result.wrapSuccessfulResult(carCategoryService.getCarForMaintain(modelId));
    }

}
