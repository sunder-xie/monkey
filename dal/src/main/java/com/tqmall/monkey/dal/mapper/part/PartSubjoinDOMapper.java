package com.tqmall.monkey.dal.mapper.part;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.domain.entity.part.PartSubjoinDO;

@MyBatisRepository
public interface PartSubjoinDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(PartSubjoinDO record);

    PartSubjoinDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PartSubjoinDO record);

    /*====自定义===*/

    PartSubjoinDO selectByDO(PartSubjoinDO partSubjoinDO);

}