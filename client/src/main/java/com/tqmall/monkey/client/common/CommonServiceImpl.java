package com.tqmall.monkey.client.common;

import com.tqmall.core.common.entity.Result;
import com.tqmall.monkey.domain.bizBO.goods.GoodsQualityBO;
import com.tqmall.monkey.domain.bizBO.lop.CateBO;
import com.tqmall.monkey.external.dubbo.lop.CateServiceExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by huangzhangting on 16/10/28.
 */
@Service
public class CommonServiceImpl implements CommonService {
    @Autowired
    private CateServiceExt cateServiceExt;


    @Override
    public Result<List<GoodsQualityBO>> getGoodsQuality() {
        List<GoodsQualityBO> list = new ArrayList<>();
        GoodsQualityBO qualityBO = new GoodsQualityBO();
        qualityBO.setId(1);
        qualityBO.setName("正厂原厂");
        list.add(qualityBO);

        qualityBO = new GoodsQualityBO();
        qualityBO.setId(2);
        qualityBO.setName("正厂配套");
        list.add(qualityBO);

        qualityBO = new GoodsQualityBO();
        qualityBO.setId(3);
        qualityBO.setName("正厂下线");
        list.add(qualityBO);

        qualityBO = new GoodsQualityBO();
        qualityBO.setId(4);
        qualityBO.setName("全新拆车");
        list.add(qualityBO);

        qualityBO = new GoodsQualityBO();
        qualityBO.setId(5);
        qualityBO.setName("旧件拆车");
        list.add(qualityBO);

        qualityBO = new GoodsQualityBO();
        qualityBO.setId(7);
        qualityBO.setName("品牌");
        list.add(qualityBO);

        return Result.wrapSuccessfulResult(list);
    }

    @Override
    public Result<Set<String>> searchCateName(String keyword) {
        if(StringUtils.isEmpty(keyword)){
            return Result.wrapErrorResult("", "输入参数不能为空");
        }
        List<CateBO> list = cateServiceExt.searchByKeyword(keyword);
        if(list.isEmpty()){
            return Result.wrapErrorResult("", "没有查到满足条件的数据");
        }
        Set<String> nameList = new TreeSet<>();
        for(CateBO cateBO : list){
            if(keyword.equals(cateBO.getWord())){
                continue;
            }
            nameList.add(cateBO.getWord());
        }

        return Result.wrapSuccessfulResult(nameList);
    }
}
