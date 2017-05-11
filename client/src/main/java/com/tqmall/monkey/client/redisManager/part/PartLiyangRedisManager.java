package com.tqmall.monkey.client.redisManager.part;

import com.alibaba.fastjson.JSON;
import com.tqmall.monkey.component.redis.RedisClientTemplate;
import com.tqmall.monkey.component.redis.RedisKeyBean;
import com.tqmall.monkey.dal.mapper.part.PartLiyangDOMapper;
import com.tqmall.monkey.domain.entity.part.PartLiyangDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by zxg on 15/12/21.
 * 15:19
 */
@Service
public class PartLiyangRedisManager {


    @Autowired
    private PartLiyangDOMapper partLiyangDOMapper;
    @Autowired
    private RedisClientTemplate redisClientTemplate;

    //获得关系表中所有车型数据
    public List<PartLiyangDO> getPartCar(){
        String key = RedisKeyBean.PartRelationCarKey;
        List<PartLiyangDO> resultList = new ArrayList<>();

        Set<String> redisList = redisClientTemplate.getSet(key);
//        Set<String> redisList = null;
        if(null == redisList || redisList.isEmpty()){
            resultList = partLiyangDOMapper.getAllParLiyang();
            List<String> savedList = new ArrayList<>();
            for(PartLiyangDO partLiyangDO : resultList){
                PartLiyangDO saveDO = new PartLiyangDO();
                saveDO.setId(partLiyangDO.getId());
                saveDO.setLiyangBrand(partLiyangDO.getLiyangBrand());
                saveDO.setLiyangFactory(partLiyangDO.getLiyangFactory());
                saveDO.setLiyangSeries(partLiyangDO.getLiyangSeries());
                saveDO.setLiyangModel(partLiyangDO.getLiyangModel());
                String json = JSON.toJSONString(saveDO);
                savedList.add(json);
            }
            redisClientTemplate.addSet(key, savedList);
        }else{
            for(String str : redisList){
                PartLiyangDO partLiyangDO = JSON.parseObject(str, PartLiyangDO.class);
                resultList.add(partLiyangDO);
            }
        }

        return resultList;
    }

    //添加新增的车型数据
    public void addPartCar(PartLiyangDO PartLiyangDO){
        String key = RedisKeyBean.PartRelationCarKey;

        String json = JSON.toJSONString(PartLiyangDO);
        redisClientTemplate.addSet(key, json);
    }

    //移除删除的车型数据
    public void deletePartCar(PartLiyangDO PartLiyangDO){
        String key = RedisKeyBean.PartRelationCarKey;

        String json = JSON.toJSONString(PartLiyangDO);
        redisClientTemplate.deleteSetValue(key, json);
    }


}
