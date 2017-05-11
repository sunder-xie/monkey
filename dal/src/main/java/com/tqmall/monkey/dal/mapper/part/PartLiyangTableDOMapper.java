package com.tqmall.monkey.dal.mapper.part;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.domain.entity.part.PartLiyangTableDO;
import org.apache.ibatis.annotations.Param;

@MyBatisRepository
public interface PartLiyangTableDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(PartLiyangTableDO record);

    PartLiyangTableDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PartLiyangTableDO record);

    /*========add by zxg 2016.05.31========*/
    String selectByBrandName(@Param(value = "carBrandName") String brandName);

}