package com.tqmall.monkey.web.common;

import com.tqmall.core.common.entity.Result;
import com.tqmall.monkey.client.redisManager.Category.CategoryRedisManager;
import com.tqmall.monkey.domain.entity.category.CategoryPartDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zxg on 15/10/19.
 */
@Controller
@RequestMapping("/monkey/goods")
public class GoodsController {

    @Autowired
    private CategoryRedisManager categoryRedisManager;


    //模糊搜索标准零件名称，支持编号和名称的模糊搜索
    @RequestMapping(value = "/SearchPartByKey", method = RequestMethod.GET)
    public
    @ResponseBody
    Result SearchPartByKey(String key){
        if(StringUtils.isEmpty(key)){
            return Result.wrapErrorResult("001","key is null");
        }
        key = key.toUpperCase().trim();
        List<CategoryPartDO> list = new ArrayList<>();

        //正则判断 key是否含中文
        String regEx = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(regEx);
        Matcher matcher = pat.matcher(key);
        if (matcher.find()) {
            //包含中文，查名称
            list = categoryRedisManager.getPartListLikeName(key);

        }else{
            //不包含中文，查编号
            list = categoryRedisManager.getPartListLikeCode(key);
        }

        return Result.wrapSuccessfulResult(list);
    }
}
