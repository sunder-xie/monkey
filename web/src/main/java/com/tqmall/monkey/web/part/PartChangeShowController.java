package com.tqmall.monkey.web.part;

import com.tqmall.core.common.entity.Result;
import com.tqmall.monkey.client.category.CategoryPartService;
import com.tqmall.monkey.client.part.PartGoodsService;
import com.tqmall.monkey.client.shiro.MonkeyJdbcRealm;
import com.tqmall.monkey.domain.entity.UserDO;
import com.tqmall.monkey.domain.entity.category.CategoryPartDO;
import com.tqmall.monkey.domain.entity.part.BasePartGoodDO;
import com.tqmall.monkey.domain.model.BasePartGoodsParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by zxg on 16/6/21.
 * 16:18
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Controller
@Transactional
@RequestMapping("/monkey/part/goodsChange")
public class PartChangeShowController {


    //获得当前登录用户信息
    @Autowired
    private MonkeyJdbcRealm monkeyJdbcRealm;
    @Autowired
    private PartGoodsService partGoodsService;
    @Autowired
    private CategoryPartService categoryPartService;

    @RequestMapping(value = "index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("monkey/part/goodsChange");
        UserDO userDO = monkeyJdbcRealm.getCurrentUser();

        if (userDO == null) {
            return new ModelAndView("monkey/login");
        }

        return modelAndView;
    }

    //根据oe码获得基础配件数据
    @RequestMapping(value = "/getGoodsByOE", method = RequestMethod.GET)
    public
    @ResponseBody
    Result getGoodsByOE(String oe){
        if(StringUtils.isEmpty(oe)){
            return Result.wrapErrorResult("001","参数错误");
        }
        oe = oe.replaceAll(" +", "").replace("／", "/").replace("（", "(").replace("）", ")").replace("—", "-").replace("“", "\"").replace("”", "\"").replace("\"","'").replace("，",",").toUpperCase();
        BasePartGoodsParams basePartGoodsParams = new BasePartGoodsParams();
        basePartGoodsParams.setOeNumber(oe);
        List<BasePartGoodDO> list = partGoodsService.getBaseGoodsByParams(basePartGoodsParams);
        if(CollectionUtils.isEmpty(list)){
            return Result.wrapErrorResult("001","无结果");
        }
        return Result.wrapSuccessfulResult(list.get(0));
    }

    //更改基础配件数据的标准零件编码
    @RequestMapping(value = "/changePart", method = RequestMethod.POST)
    public
    @ResponseBody
    Result changePart(String oe,String partCode){
        if(StringUtils.isEmpty(oe)){
            return Result.wrapErrorResult("001","参数错误");
        }
        oe = oe.replaceAll(" +", "").replace("／", "/").replace("（", "(").replace("）", ")").replace("—", "-").replace("“", "\"").replace("”", "\"").replace("\"","'").replace("，",",").toUpperCase();
        CategoryPartDO partDO = categoryPartService.findCategoryPartBySumCode(partCode);
        if(partDO == null){
            return Result.wrapErrorResult("001","无此标准零件编码");
        }
        String partName = partDO.getPartName();

        String firstCode = partCode.substring(0, 2);
        String secondCode = partCode.substring(2, 4);
        String thirdCode = partCode.substring(4, 7);

        Integer firstId = partDO.getFirstCatId();
        Integer secondId = partDO.getSecondCatId();
        Integer thirdId = partDO.getThirdCatId();

        BasePartGoodDO basePartGoodDO = new BasePartGoodDO();

        basePartGoodDO.setFirstCateId(firstId);
        basePartGoodDO.setFirstCateCode(firstCode);
        basePartGoodDO.setSecondCateId(secondId);
        basePartGoodDO.setSecondCateCode(secondCode);
        basePartGoodDO.setThirdCateId(thirdId);
        basePartGoodDO.setThirdCateCode(thirdCode);
        basePartGoodDO.setPartCode(partCode);
        basePartGoodDO.setPartName(partName);

        basePartGoodDO.setOeNumber(oe);
        Boolean result = partGoodsService.updateByOeNumber(basePartGoodDO);

        return Result.wrapSuccessfulResult(result);
    }

}
