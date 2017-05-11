package com.tqmall.monkey.client.pool;

import com.tqmall.monkey.dal.dao.pool.PoolGoodsDao;
import com.tqmall.monkey.domain.entity.pool.PoolGoodsDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zxg on 15/7/7.
 */
@Transactional
@Service
public class PoolGoodsServiceImpl implements PoolGoodsService {
    @Autowired
    public PoolGoodsDao poolGoodsDao;

    @Override
    public PoolGoodsDO findByOE(String oe) {
        return poolGoodsDao.selectByOeNumber(oe);
    }

    @Override
    public Integer insertPoolGoodsDO(PoolGoodsDO poolGoodsDO) {
        Integer goodsId = null;
        String OE = poolGoodsDO.getOeNum();
        if (OE == null || OE.trim().equals("")) {
            return null;
        }
        PoolGoodsDO poolGoodsDO1 = this.findByOE(OE);
        if(poolGoodsDO1 == null){
            //库里该oe码不存在，插入，获得自增Id
            poolGoodsDao.getPoolGoodsDOMapper().insertSelective(poolGoodsDO);
            goodsId = poolGoodsDO.getId();
        }else{
            goodsId = poolGoodsDO1.getId();
        }
        return goodsId;
    }
}
