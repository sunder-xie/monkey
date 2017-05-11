package com.tqmall.monkey.dal.mapper.car;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.domain.entity.car.CarInfoAllDO;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@MyBatisRepository
public interface CarInfoAllDOMapper {
    int deleteByPrimaryKey(Integer carModel);

    int insert(CarInfoAllDO record);

    int insertSelective(CarInfoAllDO record);

    CarInfoAllDO selectByPrimaryKey(Integer carModel);

    CarInfoAllDO selectByLeyelId(String leyelId);

    int updateByPrimaryKeySelective(CarInfoAllDO record);

    int updateByPrimaryKey(CarInfoAllDO record);

    Set<HashMap<String,String>> getCarBrands();

    List<String> getFactoryNamesByBrand(Map<String, Object> params);

    List<String> getCarSeriesByBrand(Map<String, Object> params);

    List<String> getVehicleTypesBySeries(Map<String, Object> params);

    List<CarInfoAllDO> getCarInfos(Map<String, Object> params);

    List<CarInfoAllDO> getCarInfosByVehicleType(Map<String, Object> params);

    List<CarInfoAllDO> getCarInfoAll(Map<String, Object> params);
    int getCarInfoAllCount(Map<String, Object> params);

    /*
    *  保养相关查询
    * */
    List<CarInfoAllDO> getCarInfoForMaintain(Integer maintainPlanId);
    List<CarInfoAllDO> getNotRelatedCarInfoForMaintain(Map<String, Object> params);

    //是否存在力洋Id
    boolean existLeyelId(String leyelId);

    List<CarInfoAllDO> getCarInfosAll();
}