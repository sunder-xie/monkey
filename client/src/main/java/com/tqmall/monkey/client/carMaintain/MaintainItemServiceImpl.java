package com.tqmall.monkey.client.carMaintain;

import com.tqmall.monkey.dal.dao.carMaintain.MaintainItemDao;
import com.tqmall.monkey.domain.entity.carMaintain.MaintainItemDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by huangzhangting on 15/8/12.
 */
@Service
public class MaintainItemServiceImpl implements MaintainItemService{
    @Autowired
    private MaintainItemDao maintainItemDao;

    @Override
    public List<MaintainItemDO> findAllMaintainItems() {
        return maintainItemDao.getMaintainItemMapper().getAllMaintainItems();
    }
}
