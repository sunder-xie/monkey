package com.tqmall.monkey.dal.mapper.offerGoods;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.domain.entity.offerGoods.OfferLiCarDO;
import org.apache.ibatis.annotations.Param;

/**
 * Created by zhongxigeng on 15/7/16
 */
@MyBatisRepository
public interface OfferLiCarDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OfferLiCarDO record);

    int insertSelective(OfferLiCarDO record);

    OfferLiCarDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OfferLiCarDO record);

    int updateByPrimaryKey(OfferLiCarDO record);

    // ======================

    Integer getCarsSumByStatus(Integer status);

    //根据自定义需求更新数据
    void updateOfferLiCarDOByCustom(@Param(value = "updateDO") OfferLiCarDO offerLiCarDO,@Param(value = "existDO") OfferLiCarDO existLiCarDO);

}