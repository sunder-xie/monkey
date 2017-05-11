package com.tqmall.monkey.dal.mapper.commodity;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.domain.entity.commodity.CommodityGoodsCarDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisRepository

public interface CommodityGoodsCarDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommodityGoodsCarDO record);

    int insertSelective(CommodityGoodsCarDO record);

    CommodityGoodsCarDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CommodityGoodsCarDO record);

    int updateByPrimaryKey(CommodityGoodsCarDO record);


    void insertBatch(List<CommodityGoodsCarDO> list);

    //=====================

    CommodityGoodsCarDO selectByUuIdLiyangWithoutDelete(@Param("goodsUuId")String goodsUuId, @Param("liyangId")String liyangId);
}