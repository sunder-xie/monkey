package com.tqmall.monkey.dal.mapper.part;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.domain.entity.part.BasePartGoodDO;
import com.tqmall.monkey.domain.model.BasePartGoodsParams;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisRepository
public interface BasePartGoodDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(BasePartGoodDO record);

    BasePartGoodDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BasePartGoodDO record);

    int updateByOeNumber(BasePartGoodDO record);

    int updateByPartSelective(@Param(value = "record")BasePartGoodDO record,@Param(value = "oldPartCode")String oldPartCode);

    List<BasePartGoodDO> existBaseGoods(BasePartGoodsParams partGoodsParams);

    BasePartGoodDO selectByUuid(@Param(value = "uuid")String uuid);

    Integer selectOeNum();
}