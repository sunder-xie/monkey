package com.tqmall.monkey.client.part;

import com.tqmall.monkey.client.redisManager.part.PartGoodsRedisManager;
import com.tqmall.monkey.client.redisManager.part.PartLiyangRedisManager;
import com.tqmall.monkey.dal.dao.part.PartLiyangRelationDao;
import com.tqmall.monkey.dal.mapper.part.PartLiyangTableDOMapper;
import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.part.PartLiyangRelationDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by zxg on 15/7/27.
 * 15:38
 */

@Service
@Slf4j
public class PartRelationServiceImpl implements PartRelationService {

    @Autowired
    private PartLiyangRelationDao partLiyangRelationDao;
    @Autowired
    private PartLiyangTableDOMapper partLiyangTableDOMapper;

    @Autowired
    private PartGoodsRedisManager partGoodsRedisManager;

    @Override
    public Page<String> selectLiyangIdPageByGoodsIdPicId(Integer index, Integer pageSize, String goodsId, String picId,Integer partLId,String brandName) {
        String theLiyangTable = partLiyangTableDOMapper.selectByBrandName(brandName);
        if(theLiyangTable == null){
            return null;
        }
        return partLiyangRelationDao.selectLiyangIdPageByGoodsIdPicId(index, pageSize, goodsId, picId,partLId,theLiyangTable);
    }

    @Override
    public Boolean deleteByPartLId(Integer partLId, String brandName) {
        if(StringUtils.isEmpty(partLId) || StringUtils.isEmpty(brandName)){
            return false;
        }
        String theLiyangTable = partLiyangTableDOMapper.selectByBrandName(brandName);
        if(theLiyangTable == null){
            return false;
        }

        partLiyangRelationDao.getPartLiyangRelationDOMapper().deleteByPartLid(partLId,theLiyangTable);
        partGoodsRedisManager.deleteGoodsByCar(partLId);

        return true;
    }

}
