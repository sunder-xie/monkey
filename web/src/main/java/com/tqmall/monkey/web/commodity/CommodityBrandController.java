package com.tqmall.monkey.web.commodity;

import com.tqmall.core.common.entity.Result;
import com.tqmall.monkey.client.commodity.CommodityBrandService;
import com.tqmall.monkey.client.shiro.MonkeyJdbcRealm;
import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.UserDO;
import com.tqmall.monkey.domain.entity.commodity.CommodityBrandDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.HashMap;

/**
 * Created by zxg on 15/8/13.
 * 商品品牌
 */


@Controller
@RequestMapping("/monkey/commodity/brand")
public class CommodityBrandController {

    @Autowired
    private CommodityBrandService commodityBrandService;

    //获得当前登录用户信息
    @Autowired
    private MonkeyJdbcRealm monkeyJdbcRealm;


    //商品库品牌维护页面--正常样式
    @RequestMapping(value = "index" )
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView("monkey/commodity/brand");

        UserDO user = monkeyJdbcRealm.getCurrentUser();
        if(user == null){
            return new ModelAndView("monkey/login");
        }

        return modelAndView;
    }

    //分页查找品牌数据
    @RequestMapping(value = "/searchBrandData",method = RequestMethod.GET )
    public
    @ResponseBody
    HashMap<String,Object> searchBrandData(Integer index,Integer pageSize,Integer country,String nameKey){
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        if(null != nameKey) {
            nameKey = nameKey.trim().replaceAll(" +", "").toUpperCase();
        }
        Page<CommodityBrandDO> searchPage = commodityBrandService.getPageByCountroyKey(index, pageSize,country,nameKey );
        if(searchPage == null){
            resultMap.put("brandList", Collections.emptyList());
            resultMap.put("totalNumber", 0);
            resultMap.put("totalPages", 0);
        }else{
            resultMap.put("brandList", searchPage.getItems());
            resultMap.put("totalNumber", searchPage.getTotalNumber());
            resultMap.put("totalPages", searchPage.getTotalPage());
        }

        return resultMap;
    }

    //插入品牌数据
    @RequestMapping(value = "/saveBrand",method = RequestMethod.POST )
    public
    @ResponseBody
    Result saveBrand(CommodityBrandDO commodityBrandDO){
        UserDO user = monkeyJdbcRealm.getCurrentUser();
        Integer userId = user.getId();
        commodityBrandDO.setCreator(userId);
        commodityBrandDO.setModifier(userId);

        String firstLetter = commodityBrandDO.getFirstLetter();
        if(null != firstLetter){
            commodityBrandDO.setFirstLetter(firstLetter.toUpperCase());
        }

        Integer id = commodityBrandDO.getId();
        try {
            if (null == id) {
                id = commodityBrandService.insertBrand(commodityBrandDO);
            } else {
                commodityBrandService.updateBrand(commodityBrandDO);
            }

            if (!id.equals(0)) {
                return Result.wrapSuccessfulResult(id);
            } else {
                return Result.wrapErrorResult("001", "已存在唯一的中文名、英文名、编码");
            }
        }catch (Exception e){
            return Result.wrapErrorResult("001", e.getMessage());

        }

    }

    //删除品牌数据
    @RequestMapping(value = "/deleteCommodityBrand",method = RequestMethod.POST )
    public
    @ResponseBody
    Result deleteCommodityBrand(Integer id){
        UserDO user = monkeyJdbcRealm.getCurrentUser();
        Integer userId = user.getId();

        CommodityBrandDO commodityBrandDO = new CommodityBrandDO();

        commodityBrandDO.setId(id);
        commodityBrandDO.setModifier(userId);
        boolean is_ok = commodityBrandService.deleteBrand(commodityBrandDO);

        return Result.wrapSuccessfulResult(is_ok);

    }

}
