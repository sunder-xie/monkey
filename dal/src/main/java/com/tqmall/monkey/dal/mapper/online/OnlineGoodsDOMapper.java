package com.tqmall.monkey.dal.mapper.online;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.domain.entity.online.OnlineGoodsDO;
import com.tqmall.monkey.domain.entity.online.OnlineGoodsDOWithBLOBs;

@MyBatisRepository
public interface OnlineGoodsDOMapper {
    int deleteByPrimaryKey(Integer goodsId);

    int insert(OnlineGoodsDOWithBLOBs record);

    int insertSelective(OnlineGoodsDOWithBLOBs record);

    OnlineGoodsDOWithBLOBs selectByPrimaryKey(Integer goodsId);

    int updateByPrimaryKeySelective(OnlineGoodsDOWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(OnlineGoodsDOWithBLOBs record);

    int updateByPrimaryKey(OnlineGoodsDO record);
}