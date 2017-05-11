package com.tqmall.monkey.client.part;

import com.tqmall.monkey.client.redisManager.part.PartLiyangRedisManager;
import com.tqmall.monkey.client.shiro.MonkeyJdbcRealm;
import com.tqmall.monkey.dal.mapper.part.PartLiyangDOMapper;
import com.tqmall.monkey.domain.entity.UserDO;
import com.tqmall.monkey.domain.entity.part.PartLiyangDO;
import com.tqmall.monkey.domain.entity.part.PartLiyangRelationDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zxg on 16/5/24.
 * 10:26
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Service
public class PartLiyangServiceImpl implements PartLiyangService {

    @Autowired
    private PartLiyangDOMapper partLiyangDOMapper;
    @Autowired
    private PartLiyangRedisManager liyangRedisManager;
    @Autowired
    private MonkeyJdbcRealm monkeyJdbcRealm;

    @Override
    public List<PartLiyangDO> getPartCar() {
        return liyangRedisManager.getPartCar();
    }

    @Override
    public Integer getPartLiyang(String brand, String factory, String series, String model) {
        if(brand == null || factory == null || series == null || model == null ){
            return null;
        }
        PartLiyangDO partLiyangDO = new PartLiyangDO();
        partLiyangDO.setLiyangBrand(brand);
        partLiyangDO.setLiyangFactory(factory);
        partLiyangDO.setLiyangSeries(series);
        partLiyangDO.setLiyangModel(model);

        Integer resultId;

        List<PartLiyangDO> liyangList = partLiyangDOMapper.selectByDO(partLiyangDO);

        if(liyangList.isEmpty()){
            return null;
        }
        PartLiyangDO thisDO = liyangList.get(0);
        resultId = thisDO.getId();
        return resultId;
    }

    @Override
    public Integer savePartLiyang(String brand, String factory, String series, String model) {
        if(brand == null || factory == null || series == null || model == null ){
            return null;
        }
        PartLiyangDO partLiyangDO = new PartLiyangDO();
        partLiyangDO.setLiyangBrand(brand);
        partLiyangDO.setLiyangFactory(factory);
        partLiyangDO.setLiyangSeries(series);
        partLiyangDO.setLiyangModel(model);

        Integer resultId;

        List<PartLiyangDO> liyangList = partLiyangDOMapper.selectByDO(partLiyangDO);
        if(!liyangList.isEmpty()){
            PartLiyangDO thisDO = liyangList.get(0);
            resultId = thisDO.getId();
            String isDeleted = thisDO.getIsDeleted();
            if(isDeleted.equals("Y")){
                partLiyangDOMapper.setIsNotDeletedPrimaryKey(resultId,monkeyJdbcRealm.getCurrentUser().getId());
            }
        }else{
            partLiyangDOMapper.insertSelective(partLiyangDO);
            resultId = partLiyangDO.getId();
        }

        //保存纪录到redis中
        if(resultId != null){
            partLiyangDO.setId(resultId);
            liyangRedisManager.addPartCar(partLiyangDO);
        }
        return resultId;
    }

    @Override
    public Boolean deletePartLiyang(Integer id, String brand, String factory, String series, String model) {
        if(id == null){
            return false;
        }

        UserDO userDO = monkeyJdbcRealm.getCurrentUser();
        Integer userId = userDO.getId();
        partLiyangDOMapper.deleteByPrimaryKey(id,userId);

        PartLiyangDO partLiyangDO = new PartLiyangDO();
        partLiyangDO.setLiyangBrand(brand);
        partLiyangDO.setLiyangFactory(factory);
        partLiyangDO.setLiyangSeries(series);
        partLiyangDO.setLiyangModel(model);
        partLiyangDO.setId(id);
        liyangRedisManager.deletePartCar(partLiyangDO);

        return null;
    }


}
