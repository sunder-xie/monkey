package com.tqmall.monkey.client.offerGoods;

import com.tqmall.monkey.client.category.CategoryPartService;
import com.tqmall.monkey.dal.dao.offerGoods.OfferGoodsDao;
import com.tqmall.monkey.domain.entity.category.CategoryPartDO;
import com.tqmall.monkey.domain.entity.offerGoods.OfferGoodsDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zxg on 2015/4/26.
 * 报价单系统
 */
@Transactional
@Service
public class OfferGoodsServiceImpl implements OfferGoodsService {

    @Autowired
    private OfferGoodsDao offerGoodsDao;
    @Autowired
    private CategoryPartService categoryPartService;

    @Override
    public OfferGoodsDO selectById(Integer id) {
        return offerGoodsDao.getOfferGoodsDOMapper().selectByPrimaryKey(id);
    }

    @Override
    public OfferGoodsDO selectByOE(String OE) {
        return offerGoodsDao.selectByOeNumber(OE);
    }

    @Override
    public Integer insertOfferGoodsWithExit(OfferGoodsDO offerGoodsDO) {
        String OE = offerGoodsDO.getOeNum();
        if (OE == null || OE.trim().equals("")) {
            return null;
        }
        String brandName = offerGoodsDO.getBrandName();
        Integer qualityType = offerGoodsDO.getGoodsQualityType();
        //在已有库里寻找是否已存
        OfferGoodsDO goodsA = offerGoodsDao.selectByOeBrandQuality(OE, brandName, qualityType);
        if(goodsA != null){
            return goodsA.getId();
        }

        offerGoodsDao.getOfferGoodsDOMapper().insertSelective(offerGoodsDO);

        return offerGoodsDO.getId();
    }

    @Override
    public Integer updateOfferGoods(OfferGoodsDO offerGoodsDO) {
        Integer id = offerGoodsDO.getId();
        if (id == null ) {
            return null;
        }
        return offerGoodsDao.getOfferGoodsDOMapper().updateByPrimaryKeySelective(offerGoodsDO);
    }

    @Override
    public Boolean updateByPart(Integer oldPartId, Integer newPartId) {
        CategoryPartDO partDO = categoryPartService.findCategoryPartById(newPartId);
        if(null == partDO) {
            return false;
        }
        OfferGoodsDO offerGoodsDO = new OfferGoodsDO();
        offerGoodsDO.setFirstCateId(partDO.getFirstCatId());
        offerGoodsDO.setFirstCateName(partDO.getFirstCatName());
        offerGoodsDO.setSecondCateId(partDO.getSecondCatId());
        offerGoodsDO.setSecondCateName(partDO.getSecondCatName());
        offerGoodsDO.setThirdCateId(partDO.getThirdCatId());
        offerGoodsDO.setThirdCateName(partDO.getThirdCatName());
        offerGoodsDO.setPartId(partDO.getId());
        offerGoodsDO.setPartName(partDO.getPartName());
        offerGoodsDO.setPartSumCode(partDO.getSumCode());

        offerGoodsDao.getOfferGoodsDOMapper().updateByPartSelective(offerGoodsDO,oldPartId);
        return true;
    }


    @Override
    public List<OfferGoodsDO> findAllGoodsByCateStatus(Integer cateStatus) {
        Map<String, Object> params = new HashMap<>();
        if(cateStatus!=null){
            params.put("cateStatus", cateStatus);
        }
        return offerGoodsDao.getOfferGoodsDOMapper().getAllGoodsByCateStatus(params);
    }

    @Override
    public Integer findGoodsSumByCateStatus(Integer cateStatus) {
        return offerGoodsDao.getOfferGoodsDOMapper().getGoodsSumByCateStatus(cateStatus);
    }

    @Override
    public List<OfferGoodsDO> findGoodsByPartId(Integer partId) {
        List<OfferGoodsDO> list;
        if(StringUtils.isEmpty(partId)){
            list = new ArrayList<>();
        }
        list = offerGoodsDao.getOfferGoodsDOMapper().getGoodsByPartId(partId);
        return list;
    }

}
