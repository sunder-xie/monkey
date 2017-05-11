package com.tqmall.monkey.client.CarCategory;


import com.tqmall.athena.domain.result.carcategory.CarCategoryDTO;
import com.tqmall.monkey.component.redis.RedisClientTemplate;
import com.tqmall.monkey.component.redis.RedisKeyBean;
import com.tqmall.monkey.component.utils.BdUtil;
import com.tqmall.monkey.component.utils.JsonUtil;
import com.tqmall.monkey.domain.bizBO.car.CarCategoryBO;
import com.tqmall.monkey.external.dubbo.car.CarCategoryExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by huangzhangting on 15/6/9.
 */
@Service
public class CarCategoryServiceImpl implements CarCategoryService{
    @Autowired
    private CarCategoryExt carCategoryExt;
    @Autowired
    private RedisClientTemplate redisClient;


    @Override
    public List<CarCategoryDTO> getCarByPid(Integer pid) {
        List<CarCategoryDTO> list = carCategoryExt.getCarByPid(pid);
        Collections.sort(list, new Comparator<CarCategoryDTO>() {
            @Override
            public int compare(CarCategoryDTO o1, CarCategoryDTO o2) {
                String str1 = o1.getFirstWord()+o1.getImportInfo()+o1.getName();
                String str2 = o2.getFirstWord()+o2.getImportInfo()+o2.getName();
                return str1.compareTo(str2);
            }
        });
        return list;
    }

    @Override
    public List<CarCategoryDTO> getModelForMaintain(Integer brandId) {
        //从redis查
        String key = String.format(RedisKeyBean.CAR_MODELS_FOR_MAINTAIN_KEY, brandId);
        String redisStr = redisClient.get(key);
        if(redisStr!=null){
            return JsonUtil.jsonToList(redisStr, CarCategoryDTO.class);
        }

        //从athena查
        List<CarCategoryDTO> seriesList = carCategoryExt.getCarByPid(brandId);
        List<CarCategoryDTO> list = new ArrayList<>();
        for(CarCategoryDTO carSeries : seriesList){
            list.addAll(carCategoryExt.getCarByPid(carSeries.getId()));
        }

        for(CarCategoryDTO car : list){
            if("进口".equals(car.getImportInfo())){
                car.setName("进口 "+car.getName());
            }else{
                car.setName(car.getCompany()+" "+car.getName());
            }
        }

        Collections.sort(list, new Comparator<CarCategoryDTO>() {
            @Override
            public int compare(CarCategoryDTO o1, CarCategoryDTO o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        //设置缓存
        redisClient.lazySet(key, list, RedisKeyBean.RREDIS_EXP_DAY);

        return list;
    }

    @Override
    public List<CarCategoryBO> getCarForMaintain(Integer modelId) {
        //从redis查
        String key = String.format(RedisKeyBean.CARS_FOR_MAINTAIN_KEY, modelId);
        String redisStr = redisClient.get(key);
        if(redisStr!=null){
            return JsonUtil.jsonToList(redisStr, CarCategoryBO.class);
        }

        //从athena查
        List<CarCategoryDTO> powerList = carCategoryExt.getCarByPid(modelId);
        List<CarCategoryDTO> yearList = new ArrayList<>();
        for(CarCategoryDTO carPower : powerList){
            yearList.addAll(carCategoryExt.getCarByPid(carPower.getId()));
        }

        //最终返回的数据
        List<CarCategoryBO> carCategoryBOList = new ArrayList<>();

        for(CarCategoryDTO carYear : yearList){
            List<CarCategoryDTO> carList = carCategoryExt.getCarByPid(carYear.getId());
            for(CarCategoryDTO car : carList){
                CarCategoryBO carCategoryBO = BdUtil.do2bo(car, CarCategoryBO.class);
                carCategoryBO.setName(carCategoryBO.getYear()+"款 "+carCategoryBO.getName());
                carCategoryBO.setPowerId(carYear.getPid());

                carCategoryBOList.add(carCategoryBO);
            }
        }

        Collections.sort(carCategoryBOList, new Comparator<CarCategoryBO>() {
            @Override
            public int compare(CarCategoryBO o1, CarCategoryBO o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        //设置缓存
        redisClient.lazySet(key, carCategoryBOList, RedisKeyBean.RREDIS_EXP_DAY);

        return carCategoryBOList;
    }

}
