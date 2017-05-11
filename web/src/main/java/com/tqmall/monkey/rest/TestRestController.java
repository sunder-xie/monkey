package com.tqmall.monkey.rest;

import com.tqmall.core.common.entity.Result;
import com.tqmall.monkey.client.TestAnnotationService;
import com.tqmall.monkey.client.lop.AvidCallBiz;
import com.tqmall.monkey.client.part.PartRelationService;
import com.tqmall.monkey.client.redisManager.part.PartGoodsRedisManager;
import com.tqmall.monkey.client.redisManager.part.PartLiyangRedisManager;
import com.tqmall.monkey.component.redis.RedisClientTemplate;
import com.tqmall.monkey.component.redis.RedisKeyBean;
import com.tqmall.monkey.domain.bizBO.avid.AvidSearchBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zxg on 15/7/20.
 * 测试的Controller
 */
@RestController
@RequestMapping("/rest/test")
@Slf4j
public class TestRestController {


    @Autowired
    private TestAnnotationService testAnnotationService;

    @Autowired
    public RedisClientTemplate redisClientTemplate;
    @Autowired
    private PartRelationService partRelationService;
    @Autowired
    private PartLiyangRedisManager partRelationRedisManager;
    @Autowired
    private PartGoodsRedisManager partGoodsRedisManager;
    @Autowired
    private AvidCallBiz avidCallBiz;

    //测试
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public Result test(){

        log.info("test is this-----=====");
        return Result.wrapSuccessfulResult("success");
    }
    //测试
    @RequestMapping(value = "/test1", method = RequestMethod.GET)
    public Result test(Integer a){

        log.info("test is this-----====="+a);
        return Result.wrapSuccessfulResult("success");
    }

 //测试事务管理
    @RequestMapping(value = "/annotation", method = RequestMethod.GET)
    public void annotation(){
        testAnnotationService.testFangfa();
    }


    @RequestMapping(value = "/redis", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String,String> redis() {
        redisClientTemplate.set("a12", "晚上的我2131！@3");
        String result = redisClientTemplate.get("a12");
        System.out.println(result);

        Map<String,String> map = new HashMap<>();
        map.put("ab","网民");
        map.put("q","我上的");
        map.put("w","asd！@＃收到啊");

        redisClientTemplate.setMapWithTime("map123", map, RedisKeyBean.RREDIS_EXP_HOURS);

        Map<String,String> anothMap = redisClientTemplate.getAllHash("map123");
        return anothMap;
    }



    @RequestMapping(value = "/testSearchAvid", method = RequestMethod.GET)
    public Result testSearchAvid(AvidSearchBO avidSearchBO){
        return avidCallBiz.getAvidPageShow(avidSearchBO);

    }


}
