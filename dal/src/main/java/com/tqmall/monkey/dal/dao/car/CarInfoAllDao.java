package com.tqmall.monkey.dal.dao.car;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.dal.dao.base.BaseDao;
import com.tqmall.monkey.dal.mapper.car.CarInfoAllDOMapper;
import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.car.CarInfoAllDO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by huangzhangting on 15/7/22.
 */
@MyBatisRepository
public class CarInfoAllDao extends BaseDao{
    private final static String NAMESPACE = "com.tqmall.monkey.dal.mapper.car.CarInfoAllDOMapper";

    @Autowired
    private CarInfoAllDOMapper carInfoAllMapper;

    public CarInfoAllDOMapper getCarInfoAllMapper() {
        return carInfoAllMapper;
    }

    public Page<CarInfoAllDO> getCarInfoPage(Map<String, Object> params, int pageIndex, int pageSize){
        String selectSql = NAMESPACE + ".getCarInfos";
        String countSql = NAMESPACE + ".getCarInfosCount";
        Page<CarInfoAllDO> page = this.queryPage(selectSql, countSql, params, pageIndex, pageSize);

        return page;
    }

    public Page<CarInfoAllDO> getCarInfoPageByVehicleType(Map<String, Object> params, int pageIndex, int pageSize){
        String selectSql = NAMESPACE + ".getCarInfosByVehicleType";
        String countSql = NAMESPACE + ".getCarInfosByVehicleTypeCount";
        Page<CarInfoAllDO> page = this.queryPage(selectSql, countSql, params, pageIndex, pageSize);

        return page;
    }


}
