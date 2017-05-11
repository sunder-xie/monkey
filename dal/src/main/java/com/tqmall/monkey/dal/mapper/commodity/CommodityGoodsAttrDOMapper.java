package com.tqmall.monkey.dal.mapper.commodity;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.domain.entity.commodity.CommodityGoodsAttrDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@MyBatisRepository

public interface CommodityGoodsAttrDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommodityGoodsAttrDO record);

    int insertSelective(CommodityGoodsAttrDO record);

    CommodityGoodsAttrDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CommodityGoodsAttrDO record);

    int updateByPrimaryKey(CommodityGoodsAttrDO record);

    //============自定义
    void insertBatch(List<CommodityGoodsAttrDO> list);

    //存在则返回主键id所在的对象
    CommodityGoodsAttrDO selectByAttrUuId(@Param("goodsUuId") String goodsUuId,@Param("attrId") Integer attrId);

}