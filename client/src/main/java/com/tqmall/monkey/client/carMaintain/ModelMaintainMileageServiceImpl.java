package com.tqmall.monkey.client.carMaintain;

import com.tqmall.monkey.dal.dao.carMaintain.ModelMaintainMileageDao;
import com.tqmall.monkey.domain.entity.carMaintain.ModelMaintainMileageDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by huangzhangting on 15/8/12.
 */

@Service
public class ModelMaintainMileageServiceImpl implements ModelMaintainMileageService{
    @Autowired
    private ModelMaintainMileageDao modelMaintainMileageDao;

    @Override
    public List<ModelMaintainMileageDO> findMaintainMileageByPlanId(Integer maintainPlanId) {
        List<ModelMaintainMileageDO> list =
                modelMaintainMileageDao.getMaintainMileageMapper().getMaintainMileageByPlanId(maintainPlanId);
        if(list.isEmpty()){
            return list;
        }
        List<ModelMaintainMileageDO> resultList = new ArrayList<>();
        Set<Integer> idSet = new HashSet<>();
        for(ModelMaintainMileageDO mileageDO : list){
            if(idSet.add(mileageDO.getId())){
                resultList.add(mileageDO);
            }
        }
        Collections.sort(resultList, new Comparator<ModelMaintainMileageDO>() {
            @Override
            public int compare(ModelMaintainMileageDO o1, ModelMaintainMileageDO o2) {
                return o1.getMileage().compareTo(o2.getMileage());
            }
        });

        return resultList;
    }

    @Override
    public int saveModelMaintainMileage(ModelMaintainMileageDO record) {
        return modelMaintainMileageDao.getMaintainMileageMapper().insertSelective(record);
    }

    @Override
    public int modifyModelMaintainMileage(ModelMaintainMileageDO record) {
        record.setIsDeleted(false);
        int count = modelMaintainMileageDao.getMaintainMileageMapper().checkMileageExsit(record);
        if(count>0) {
            return 0;
        }
        return modelMaintainMileageDao.getMaintainMileageMapper().updateByPrimaryKeySelective(record);
    }

    @Override
    public int removeModelMaintainMileage(ModelMaintainMileageDO record) {
//        record.setIsDeleted(true);
//        int count = modelMaintainMileageDao.getMaintainMileageMapper().checkMileageExsit(record);
//        if(count>0){
//            return modelMaintainMileageDao.getMaintainMileageMapper().deleteByPrimaryKey(record.getId());
//        }
//        return modelMaintainMileageDao.getMaintainMileageMapper().updateByPrimaryKeySelective(record);

        return modelMaintainMileageDao.getMaintainMileageMapper().deleteByPrimaryKey(record.getId());
    }

    @Override
    public int removeModelMaintainMileage(List<Integer> idList) {
        if(CollectionUtils.isEmpty(idList)){
            return 0;
        }
        for(Integer id : idList){
            modelMaintainMileageDao.getMaintainMileageMapper().deleteByPrimaryKey(id);
        }
        return 1;
    }

    @Override
    public int saveModelMaintainMileageBatch(int maintainPlanId, int mileage, int addStep, int addNum) {
        if(addStep<0){
            addStep = 0;
        }
        if(addNum<1){
            addNum = 1;
        }

        List<ModelMaintainMileageDO> list = new ArrayList<>();
        ModelMaintainMileageDO record;
        int mile;
        for(int i=0; i<addNum; i++){
            mile = mileage + addStep*i;
            record = new ModelMaintainMileageDO();
            record.setMaintainPlanId(maintainPlanId);
            record.setMileage(mile);
            list.add(record);
        }

        return modelMaintainMileageDao.getMaintainMileageMapper().insertMaintainMileageBatch(list);
    }
}
