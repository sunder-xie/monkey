package com.tqmall.monkey.client.part;

import com.tqmall.monkey.dal.dao.part.PartPictureDao;
import com.tqmall.monkey.domain.entity.part.PartPictureDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zxg on 15/7/27.
 */

@Service
public class PartPictureServiceImpl implements PartPictureService {

    @Autowired
    private PartPictureDao partPictureDao;

    @Override
    public List<PartPictureDO> exitPartPicture(PartPictureDO partPictureDO) {
        return partPictureDao.exitPartPicture(partPictureDO);
    }

    @Override
    public String getUuIdWithExist(PartPictureDO partPictureDO) {
        PartPictureDO exitPicture = new PartPictureDO();
        exitPicture.setPictureNum(partPictureDO.getPictureNum());
        exitPicture.setPictureIndex(partPictureDO.getPictureIndex());

        List<PartPictureDO> exitList = this.exitPartPicture(exitPicture);
        if(exitList != null && exitList.size() > 0){
            return exitList.get(0).getUuId();
        }
        return null;
    }

    @Override
    public Integer insertPartPictureWithoutExit(PartPictureDO partPictureDO) {
        PartPictureDO exitPicture = new PartPictureDO();
        exitPicture.setPictureNum(partPictureDO.getPictureNum());
        exitPicture.setPictureIndex(partPictureDO.getPictureIndex());

        List<PartPictureDO> exitList = this.exitPartPicture(exitPicture);
        if(exitList != null && exitList.size() > 0){
            return exitList.get(0).getId();
        }

        partPictureDao.getPartPictureDOMapper().insertSelective(partPictureDO);
        return partPictureDO.getId();
    }

    @Override
    public boolean deleteBatchGoodsByUuId(List<String> uuIdList) {
        try{
            if(uuIdList.size() > 0){
                partPictureDao.deleteBatchGoodsByUuId(uuIdList);
            }
            return true;
        }catch (Exception e) {
            return false;
        }
    }
}
