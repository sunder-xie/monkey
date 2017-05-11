package com.tqmall.monkey.client.carMaintain;

import com.tqmall.monkey.domain.entity.carMaintain.MaintainItemDO;

import java.util.List;

/**
 * Created by huangzhangting on 15/8/12.
 */
public interface MaintainItemService {
    List<MaintainItemDO> findAllMaintainItems();
}
