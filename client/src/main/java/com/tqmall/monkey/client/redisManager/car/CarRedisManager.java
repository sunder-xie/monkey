package com.tqmall.monkey.client.redisManager.car;

import com.tqmall.athena.domain.result.carcategory.CarCategoryDTO;
import com.tqmall.monkey.component.helper.CarServiceHelp;
import com.tqmall.monkey.component.redis.RedisClientTemplate;
import com.tqmall.monkey.component.redis.RedisKeyBean;
import com.tqmall.monkey.component.utils.ExportDataVersionUtil;
import com.tqmall.monkey.dal.dao.car.CarInfoAllDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * Created by zxg on 15/9/28.
 * 车型redis控制类
 */
@Service
public class CarRedisManager {
    private static final String onlineCarByLiyang = RedisKeyBean.onlineCarByLiyang;

    private static final String liyangBrandKey = RedisKeyBean.liyangBrandKey;
    private static final String liyangFactoryKey = RedisKeyBean.liyangFactoryKey;
    private static final String liyangSeriesKey = RedisKeyBean.liyangSeriesKey;
    private static final String liyangModelKey = RedisKeyBean.liyangModelKey;

    //导出力洋车型时使用的版本号
    private static final String liyangVehicleTypeExportExcelVersionKey = RedisKeyBean.liyangVehicleTypeExportExcelVersionKey;

    @Autowired
    private RedisClientTemplate redisClientTemplate;

    @Autowired
    private CarServiceHelp carServiceHelp;
    @Autowired
    private CarInfoAllDao carInfoAllDao;

    //根据力洋车型获得线上车
    public CarCategoryDTO getOnlineCarByLiyangId(String liyangId) {
        String key = String.format(onlineCarByLiyang, liyangId);

        CarCategoryDTO carCategoryDTO = redisClientTemplate.lazyGet(key, CarCategoryDTO.class);
        if (null == carCategoryDTO) {
            carCategoryDTO = carServiceHelp.getOnlineCarCategoryDTOByLi(liyangId);
            redisClientTemplate.lazySet(key, carCategoryDTO, RedisKeyBean.RREDIS_EXP_WEEK);
        }


        return carCategoryDTO;
    }

    //获得力洋品牌
    public List<String> findCarBrands() {
        String key = liyangBrandKey;
        List<String> list = redisClientTemplate.lazyGetList(key, String.class);
        if (null == list || list.isEmpty()) {
            list = new ArrayList<>();
            Set<HashMap<String,String>> brandFirstSet = carInfoAllDao.getCarInfoAllMapper().getCarBrands();
            List<HashMap<String,String>> brandFirstList = new ArrayList<>(brandFirstSet);
            Collections.sort(brandFirstList, new Comparator<HashMap<String, String>>() {
                @Override
                public int compare(HashMap<String, String> o1, HashMap<String, String> o2) {
                    return o1.get("firstWord").compareTo(o2.get("firstWord"));
                }
            });
            for(HashMap<String,String> map : brandFirstList){
                list.add(map.get("carBrand"));
            }
            redisClientTemplate.lazySet(key, list, RedisKeyBean.RREDIS_EXP_WEEK);
        }

        return list;
    }

    public List<String> findFactoryNamesByBrand(String carBrand) {
        Map<String, Object> params = new HashMap<>();
        params.put("carBrand", carBrand);

        String key = String.format(liyangFactoryKey, carBrand);
        List<String> list = redisClientTemplate.lazyGetList(key, String.class);
        if (null == list || list.isEmpty()) {
            list = carInfoAllDao.getCarInfoAllMapper().getFactoryNamesByBrand(params);
            redisClientTemplate.lazySet(key, list, RedisKeyBean.RREDIS_EXP_WEEK);
        }

        return list;

    }

    public List<String> findCarSeriesByBrand(String carBrand, String factoryName) {
        Map<String, Object> params = new HashMap<>();
        params.put("carBrand", carBrand);
        params.put("factoryName", factoryName);

        String key = String.format(liyangSeriesKey, carBrand + factoryName);
        List<String> list = redisClientTemplate.lazyGetList(key, String.class);
        if (null == list || list.isEmpty()) {
            list = carInfoAllDao.getCarInfoAllMapper().getCarSeriesByBrand(params);
            redisClientTemplate.lazySet(key, list, RedisKeyBean.RREDIS_EXP_WEEK);
        }

        return list;
    }

    public List<String> findVehicleTypesBySeries(String carBrand, String factoryName, String carSeries) {
        Map<String, Object> params = new HashMap<>();
        params.put("carBrand", carBrand);
        params.put("factoryName", factoryName);
        params.put("carSeries", carSeries);

        String key = String.format(liyangModelKey, carBrand + factoryName + carSeries);
        List<String> list = redisClientTemplate.lazyGetList(key, String.class);
        if (null == list || list.isEmpty()) {
            list = carInfoAllDao.getCarInfoAllMapper().getVehicleTypesBySeries(params);
            redisClientTemplate.lazySet(key, list, RedisKeyBean.RREDIS_EXP_WEEK);
        }

        return list;
    }

    //--------START 力洋车型数据导出到excel的版本号 相关的数据和缓存交互实现   BY LYJ--------//
    //版本号初始从2开始(原因:加版本号之前, 已经存在了一些版本了)
    public String updateVehicleTypeExportExcelVersion() {
        return ExportDataVersionUtil.modifyExportVersion(redisClientTemplate, liyangVehicleTypeExportExcelVersionKey, 2);
    }

    public String selectVehicleTypeExportExcelVersion() {
        return ExportDataVersionUtil.getExportVersion(redisClientTemplate, liyangVehicleTypeExportExcelVersionKey, 2);
    }

    public String deleteVehicleTypeExportExcelVersion() {
        return ExportDataVersionUtil.deleteExportVersion(redisClientTemplate, liyangVehicleTypeExportExcelVersionKey, -1);

    }

    public void rollbackVehicleTypeExportExcelVersion() {
        ExportDataVersionUtil.rollbackExportVersion(redisClientTemplate, liyangVehicleTypeExportExcelVersionKey, 2);
    }
    //--------END 力洋车型数据导出到excel的版本号 相关的数据和缓存交互实现   BY LYJ--------//
}
