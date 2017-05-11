package com.tqmall.monkey.client.offerGoods;

import com.tqmall.monkey.dal.dao.offerGoods.OfferLiCarDao;
import com.tqmall.monkey.domain.entity.offerGoods.OfferLiCarDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zxg on 15/7/15.
 */
@Service
public class OfferLiCarServiceImpl implements  OfferLiCarService {
    @Autowired
    private OfferLiCarDao offerLiCarDao;

    @Override
    public Integer insertofferLiCarDOWithOut(OfferLiCarDO offerLiCarDO) {
        List<OfferLiCarDO> list = this.findOfferLiByGoodsIdLiIdStatus(offerLiCarDO.getOfferGoodsId(),offerLiCarDO.getLiId(),offerLiCarDO.getStatus());
        if(list.size() == 0) {
            return offerLiCarDao.getOfferLiCarDOMapper().insertSelective(offerLiCarDO);
        }
        return null;
    }

    @Override
    public Integer updateOfferLiCarDO(OfferLiCarDO offerLiCarDO) {
        return offerLiCarDao.getOfferLiCarDOMapper().updateByPrimaryKeySelective(offerLiCarDO);
    }

    @Override
    public void updateOfferLiCarDOByCustom(OfferLiCarDO offerLiCarDO, OfferLiCarDO existLiCarDO) {
        try {
            offerLiCarDao.getOfferLiCarDOMapper().updateOfferLiCarDOByCustom(offerLiCarDO, existLiCarDO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<OfferLiCarDO> findOfferLiByGoodsIdLiIdStatus(Integer goodsId, String liyangId, Integer status) {
        return offerLiCarDao.findOfferLiByGoodsIdLiIdStatus(goodsId, liyangId, status);
    }

    @Override
    public Integer getCarsSumByStatus(Integer status) {
        return offerLiCarDao.getOfferLiCarDOMapper().getCarsSumByStatus(status);
    }
}
