package com.tqmall.monkey.dal.mapper.part;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.domain.entity.part.PartPictureDO;
import org.apache.ibatis.annotations.Param;

@MyBatisRepository
public interface PartPictureDOMapper {
    int deleteByPrimaryKey(Integer id);


    int insertSelective(PartPictureDO record);

    PartPictureDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PartPictureDO record);


    PartPictureDO selectByUuid(@Param(value = "uuid")String uuid);

}