package com.tqmall.monkey.client.part;

import com.tqmall.monkey.client.category.CategoryPartService;
import com.tqmall.monkey.dal.dao.part.BasePartGoodDao;
import com.tqmall.monkey.dal.mapper.part.BasePartGoodDOMapper;
import com.tqmall.monkey.domain.entity.category.CategoryPartDO;
import com.tqmall.monkey.domain.entity.part.BasePartGoodDO;
import com.tqmall.monkey.domain.model.BasePartGoodsParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxg on 15/7/27.
 * 13:20
 */
@Slf4j
@Service
public class PartGoodsServiceImpl implements PartGoodsService {

    @Autowired
    private BasePartGoodDao basePartGoodDao;

    @Autowired
    private CategoryPartService categoryPartService;

    @Autowired
    private BasePartGoodDOMapper basePartGoodDOMapper;

    /*=========================base============================*/


    @Override
    public List<BasePartGoodDO> getBaseGoodsByParams(BasePartGoodsParams basePartGoodDO) {
        return basePartGoodDao.getBasePartGoodDOMapper().existBaseGoods(basePartGoodDO);
    }

    @Override
    public BasePartGoodDO getBaseGoodsUUIdWithExist(BasePartGoodDO basePartGoodDO) {
        BasePartGoodsParams existBasePart = new BasePartGoodsParams();
        existBasePart.setOeNumber(basePartGoodDO.getOeNumber());
        existBasePart.setPartName(basePartGoodDO.getPartName());
        existBasePart.setPartCode(basePartGoodDO.getPartCode());

        List<BasePartGoodDO> existIdList = this.getBaseGoodsByParams(existBasePart);
        if(existIdList != null && existIdList.size() > 0){
            return existIdList.get(0);
        }
        return null;
    }

    @Override
    public Boolean updateByOeNumber(BasePartGoodDO basePartGoodDO) {
        try{
            basePartGoodDao.getBasePartGoodDOMapper().updateByOeNumber(basePartGoodDO);
            return true;
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return false;
        }
    }

    @Override
    public Integer insertBaseGoodsWithoutExist(BasePartGoodDO basePartGoodDO) {
        BasePartGoodsParams existBasePart = new BasePartGoodsParams();
        existBasePart.setOeNumber(basePartGoodDO.getOeNumber());
        existBasePart.setPartName(basePartGoodDO.getPartName());
        existBasePart.setPartCode(basePartGoodDO.getPartCode());

        List<BasePartGoodDO> existIdList = this.getBaseGoodsByParams(existBasePart);
        if(existIdList != null && existIdList.size() > 0){
            return existIdList.get(0).getId();
        }

        //若未匹配零件编号
        if(null == basePartGoodDO.getFirstCateCode()){
            String sumCode = basePartGoodDO.getPartCode();
            CategoryPartDO partDO = categoryPartService.findCategoryPartBySumCode(sumCode);

            String firstCode = sumCode.substring(0, 2);
            String secondCode = sumCode.substring(2, 4);
            String thirdCode = sumCode.substring(4, 7);


            Integer firstId = partDO.getFirstCatId();
            Integer secondId = partDO.getSecondCatId();
            Integer thirdId = partDO.getThirdCatId();



            basePartGoodDO.setFirstCateId(firstId);
            basePartGoodDO.setFirstCateCode(firstCode);
            basePartGoodDO.setSecondCateId(secondId);
            basePartGoodDO.setSecondCateCode(secondCode);
            basePartGoodDO.setThirdCateId(thirdId);
            basePartGoodDO.setThirdCateCode(thirdCode);
        }

        basePartGoodDao.getBasePartGoodDOMapper().insertSelective(basePartGoodDO);
        return basePartGoodDO.getId();
    }


    @Override
    public Boolean updateByPart(String oldPartSumCode, Integer newPartId) {
        CategoryPartDO partDO = categoryPartService.findCategoryPartById(newPartId);
        if(null == partDO) {
            return false;
        }
        String sumCode = partDO.getSumCode();
        String firstCode = sumCode.substring(0, 2);
        String secondCode = sumCode.substring(2, 4);
        String thirdCode = sumCode.substring(4, 7);


        BasePartGoodDO basePartGoodDO = new BasePartGoodDO();
        basePartGoodDO.setNone();
        basePartGoodDO.setPartCode(partDO.getSumCode());
        basePartGoodDO.setPartName(partDO.getPartName());
        basePartGoodDO.setFirstCateId(partDO.getFirstCatId());
        basePartGoodDO.setFirstCateCode(firstCode);
        basePartGoodDO.setSecondCateId(partDO.getSecondCatId());
        basePartGoodDO.setSecondCateCode(secondCode);
        basePartGoodDO.setThirdCateId(partDO.getThirdCatId());
        basePartGoodDO.setThirdCateCode(thirdCode);
        basePartGoodDao.getBasePartGoodDOMapper().updateByPartSelective(basePartGoodDO,oldPartSumCode);
        return true;
    }

    @Override
    public Integer getPartGoodsBaseOeNum() {
        return basePartGoodDOMapper.selectOeNum();
    }

    @Override
    public boolean deleteBatchGoodsBaseByUuId(List<String> uuIdList) {
        try{
            if(uuIdList.size() > 0){
                basePartGoodDao.deleteBatchGoodsByUuId(uuIdList);
            }
            return true;
        }catch (Exception e) {
            return false;
        }
    }
}
