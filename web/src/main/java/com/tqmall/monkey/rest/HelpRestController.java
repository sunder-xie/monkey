package com.tqmall.monkey.rest;

import com.tqmall.core.common.entity.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zxg on 15/9/10.
 * 外部接口的测试
 */
@RestController
@RequestMapping("/rest/help")
public class HelpRestController {


    //测试事务管理
    @RequestMapping(value = "/getParentsByCarId", method = RequestMethod.GET)
    public Result getParentsByCarId(Integer carId){


        return Result.wrapSuccessfulResult(carId);
    }
}
