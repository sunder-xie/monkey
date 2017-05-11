package com.tqmall.monkey.dal.mapper.offerGoods;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.domain.entity.offerGoods.OfferGoodsDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface OfferGoodsDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(OfferGoodsDO record);

    OfferGoodsDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OfferGoodsDO record);

    int updateByPartSelective(@Param(value = "record")OfferGoodsDO record,@Param(value = "oldPartId")Integer oldPartId);

    /*＝＝＝＝＝＝＝＝＝＝自定义函数＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝*/
    List<OfferGoodsDO> getGoodsByCarId(Integer carId);

    List<OfferGoodsDO> getAllGoodsByCateStatus(Map<String, Object> params);

    Integer getGoodsSumByCateStatus(Integer cateStatus);

    List<OfferGoodsDO> getGoodsByPartId(@Param(value = "partId")Integer partId);
}