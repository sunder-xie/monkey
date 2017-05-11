package com.tqmall.monkey.dal.mapper.carMaintain;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.domain.entity.carMaintain.MaintainItemDO;

import java.util.List;

@MyBatisRepository
public interface MaintainItemDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MaintainItemDO record);

    int insertSelective(MaintainItemDO record);

    MaintainItemDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MaintainItemDO record);

    int updateByPrimaryKey(MaintainItemDO record);

    List<MaintainItemDO> getAllMaintainItems();
}