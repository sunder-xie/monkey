package com.tqmall.monkey.client.offerGoods;

import com.tqmall.monkey.dal.dao.offerGoods.OfferWrongDataDao;
import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.offerGoods.OfferWrongDataDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zxg on 15/7/8.
 */
@Service
public class OfferWrongDataServiceImpl implements OfferWrongDataService {
    @Autowired
    private OfferWrongDataDao offerWrongDataDao;

    @Override
    public void insertOfferWrongDataWithoutExit(OfferWrongDataDO offerWrongDataDO) {
        OfferWrongDataDO exit = offerWrongDataDao.getOfferWrongDataDOMapper().selectByOfferWrongDataDO(offerWrongDataDO);
        if(null == exit) {
            offerWrongDataDao.getOfferWrongDataDOMapper().insertSelective(offerWrongDataDO);
        }
    }

    @Override
    public void updateOfferWrongDatStatus(Integer id, Integer status) {
        OfferWrongDataDO offerWrongDataDO = new OfferWrongDataDO();
        offerWrongDataDO.setId(id);
        offerWrongDataDO.setStatus(status);

        offerWrongDataDao.getOfferWrongDataDOMapper().updateByPrimaryKeySelective(offerWrongDataDO);

    }

    @Override
    public Page<OfferWrongDataDO> selectWrongPageBystatus(Integer status,String account, Integer pageIndex, Integer pageSize) {
        return offerWrongDataDao.pageByStatusAccount(status, account, pageIndex, pageSize);
    }

    @Override
    public List<OfferWrongDataDO> selectWrongListBystatus(Integer status, String account) {
        return offerWrongDataDao.selectListByStatusAccount(status, account);
    }

    @Override
    public Integer selectWrongDataSum(Integer status, String account) {
        return offerWrongDataDao.selectCountByStatusAccount(status, account);
    }
}
