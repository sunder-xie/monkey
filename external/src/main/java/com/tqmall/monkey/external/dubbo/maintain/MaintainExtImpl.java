package com.tqmall.monkey.external.dubbo.maintain;

import com.tqmall.athena.client.maintain.MaintainService;
import com.tqmall.athena.domain.result.maintain.MaintainDetailDTO;
import com.tqmall.athena.domain.result.maintain.MaintainItemDTO;
import com.tqmall.core.common.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangzhangting on 16/2/29.
 */
@Service
public class MaintainExtImpl implements MaintainExt {
    @Autowired
    private MaintainService athenaMaintainService;

    @Override
    public Result<List<MaintainDetailDTO>> maintainDetailCommon(Integer carId, Integer mileage) {
        if(carId==null || carId<1){
            return Result.wrapErrorResult("", "请输入正确的车型分类id");
        }
        if(mileage!=null && mileage<0){
            return Result.wrapErrorResult("", "输入里程不能为负数");
        }
        return athenaMaintainService.maintainDetailCommon(carId, mileage);
    }

    @Override
    public List<MaintainItemDTO> allMaintainItems() {
        Result<List<MaintainItemDTO>> result = athenaMaintainService.allMaintainItems();
        if(result.isSuccess()){
            return result.getData();
        }
        return new ArrayList<>();
    }

}
