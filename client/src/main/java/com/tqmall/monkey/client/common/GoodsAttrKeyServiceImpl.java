package com.tqmall.monkey.client.common;

import com.tqmall.monkey.dal.dao.base.GoodsAttrKeyDao;
import com.tqmall.monkey.domain.entity.common.GoodsAttrKeyDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zxg on 15/8/21.
 */

@Service
public class GoodsAttrKeyServiceImpl implements GoodsAttrKeyService {

    @Autowired
    private GoodsAttrKeyDao goodsAttrKeyDao;
    @Override
    public Integer insertGoodsAttr(GoodsAttrKeyDO goodsAttrKeyDO) {
        String attrName = goodsAttrKeyDO.getAttrName();
        GoodsAttrKeyDO existDO = this.getGoodsAttrKeyDOByAttrName(attrName);
        if(null == existDO){
            goodsAttrKeyDao.getGoodsAttrKeyDOMapper().insertSelective(goodsAttrKeyDO);
            return goodsAttrKeyDO.getId();
        }
        return existDO.getId();
    }

    @Override
    public GoodsAttrKeyDO getGoodsAttrKeyDOByAttrName(String attrName) {
        try{
            GoodsAttrKeyDO goodsAttrKeyDO = goodsAttrKeyDao.getGoodsAttrKeyDOMapper().selectByAttrName(attrName);
            return goodsAttrKeyDO;

        }catch (Exception e){

            return null;
        }
    }
}
