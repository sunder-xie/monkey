package com.tqmall.monkey.external.dubbo.lop;

import com.tqmall.core.common.entity.Result;
import com.tqmall.monkey.component.utils.BdUtil;
import com.tqmall.monkey.component.utils.JsonUtil;
import com.tqmall.monkey.domain.bizBO.lop.CateBO;
import com.tqmall.tqmallstall.domain.result.carcategory.CarNameDTO;
import com.tqmall.tqmallstall.service.carcategory.RpcCarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangzhangting on 16/10/28.
 */
@Component
@Slf4j
public class CateServiceExt {
    @Autowired
    private RpcCarService rpcCarService;

    public List<CateBO> searchByKeyword(String keyword) {
        log.info("search cate by keyword, param:{}", keyword);

        Result<List<CarNameDTO>> result = rpcCarService.getCarNameFromSearch(keyword);

        log.info("search cate by keyword, result:{}", JsonUtil.objectToJson(result));

        if(result.isSuccess() && !CollectionUtils.isEmpty(result.getData())){
            return BdUtil.do2bo4List(result.getData(), CateBO.class);
        }
        return new ArrayList<>();
    }
}
