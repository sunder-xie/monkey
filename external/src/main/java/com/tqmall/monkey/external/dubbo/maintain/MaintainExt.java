package com.tqmall.monkey.external.dubbo.maintain;

import com.tqmall.athena.domain.result.maintain.MaintainDetailDTO;
import com.tqmall.athena.domain.result.maintain.MaintainItemDTO;
import com.tqmall.core.common.entity.Result;

import java.util.List;

/**
 * Created by huangzhangting on 16/2/29.
 */
public interface MaintainExt {
    /**
    *  根据 车型分类id、输入里程 查询保养详情
    *  carId：必填
    *  mileage：选填，（ null：查询所有里程 ）
    * */
    Result<List<MaintainDetailDTO>> maintainDetailCommon(Integer carId, Integer mileage);

    /*
    * 查询所有保养项目
    * */
    List<MaintainItemDTO> allMaintainItems();

}
