package com.tqmall.monkey.client.carMaintain;

import com.tqmall.core.common.entity.Result;


/**
 * Created by huangzhangting on 16/2/29.
 */
public interface MaintainService {

    /**
     *  根据 车型分类id、输入里程 查询保养详情（数据来源athena项目接口）
     *  carId：必填
     *  mileage：选填
     * */
    Result getMaintainDetail(Integer carId, Integer mileage);

}
