package com.tqmall.monkey.dal.mapper.commodity;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.domain.entity.commodity.CommodityGoodsOeDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisRepository
public interface CommodityGoodsOeDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommodityGoodsOeDO record);

    int insertSelective(CommodityGoodsOeDO record);

    CommodityGoodsOeDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CommodityGoodsOeDO record);

    int updateByPrimaryKey(CommodityGoodsOeDO record);

    void insertBatch(List<CommodityGoodsOeDO> list);

    //================自定义===

    // 删除
    void deleteGoodsUuId(@Param("goodsUuId")String goodsUuId, @Param("userId")Integer userId );

    // 判断存在不存在
    CommodityGoodsOeDO selectByUuIdOe(@Param("goodsUuId")String goodsUuId, @Param("oe")String oe);


}