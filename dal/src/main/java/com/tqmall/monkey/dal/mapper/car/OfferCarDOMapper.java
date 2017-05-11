package com.tqmall.monkey.dal.mapper.car;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.domain.entity.car.OfferCarDO;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface OfferCarDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OfferCarDO record);

    int insertSelective(OfferCarDO record);

    OfferCarDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OfferCarDO record);

    int updateByPrimaryKey(OfferCarDO record);

    List<OfferCarDO> getAllCarsByStatus(Map<String, Object> params);
    //获得该状态的车型数量
    Integer getAllCarsSumByStatus(Map<String, Object> params);

    List<OfferCarDO> getCarsByBrandName(Map<String, Object> params);

    List<String> getAllCarBrand(Map<String, Object> params);

    List<String> getCarYearsById(Integer carId);

    //根据已有条件选择对象
    OfferCarDO selectByOfferCarDO(OfferCarDO offerCarDO);
}