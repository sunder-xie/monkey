package com.tqmall.monkey.web.common;

import com.tqmall.core.common.entity.Result;
import com.tqmall.monkey.client.common.CommonService;
import com.tqmall.monkey.domain.bizBO.goods.GoodsQualityBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Set;

/**
 * Created by huangzhangting on 16/10/28.
 */
@Controller
@RequestMapping("monkey/common")
public class CommonController {
    @Autowired
    private CommonService commonService;

    /**
     * 获取商品品质
     * @return
     */
    @RequestMapping(value = "getGoodsQuality")
    @ResponseBody
    public Result<List<GoodsQualityBO>> getGoodsQuality() {
        return commonService.getGoodsQuality();
    }

    /**
     * 页面联想搜索
     * 根据关键词搜索商品类目名称（当做配件名称使用）
     *
     * @param keyword
     * @return
     */
    @RequestMapping(value = "cate/suggest")
    @ResponseBody
    public Result<Set<String>> suggest(String keyword) {
        return commonService.searchCateName(keyword);
    }

}
