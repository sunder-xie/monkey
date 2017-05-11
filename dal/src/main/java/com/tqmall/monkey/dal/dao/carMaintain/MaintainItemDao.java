package com.tqmall.monkey.dal.dao.carMaintain;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.dal.mapper.carMaintain.MaintainItemDOMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by huangzhangting on 15/8/12.
 */
@MyBatisRepository
public class MaintainItemDao {
    @Autowired
    private MaintainItemDOMapper maintainItemMapper;

    public MaintainItemDOMapper getMaintainItemMapper() {
        return maintainItemMapper;
    }
}
