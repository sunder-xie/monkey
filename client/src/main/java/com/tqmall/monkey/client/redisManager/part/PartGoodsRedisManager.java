package com.tqmall.monkey.client.redisManager.part;

import com.tqmall.monkey.component.redis.RedisClientTemplate;
import com.tqmall.monkey.component.redis.RedisKeyBean;
import com.tqmall.monkey.dal.mapper.car.CarInfoAllDOMapper;
import com.tqmall.monkey.dal.mapper.category.CategoryPartDOMapper;
import com.tqmall.monkey.dal.mapper.part.*;
import com.tqmall.monkey.domain.bizBO.part.PartGoodsShowBO;
import com.tqmall.monkey.domain.entity.car.CarInfoAllDO;
import com.tqmall.monkey.domain.entity.category.CategoryPartDO;
import com.tqmall.monkey.domain.entity.part.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zxg on 15/12/22.
 * 10:10
 */
@Service
public class PartGoodsRedisManager {

    @Autowired
    private RedisClientTemplate redisClientTemplate;

    @Autowired
    private BasePartGoodDOMapper basePartGoodDOMapper;
    @Autowired
    private PartPictureDOMapper partPictureDOMapper;
    @Autowired
    private PartSubjoinDOMapper subjoinDOMapper;
    @Autowired
    private PartLiyangRelationDOMapper partLiyangRelationDOMapper;
    @Autowired
    private PartLiyangTableDOMapper partLiyangTableDOMapper;

    @Autowired
    private CategoryPartDOMapper categoryPartDOMapper;
    @Autowired
    private CarInfoAllDOMapper carInfoAllDOMapper;

    //当前车型下的商品数据
    public List<PartGoodsShowBO> getGoodsByCar(Integer partLId,String brandName){
        String key = String.format(RedisKeyBean.PartGoodsShowKey, partLId);

        List<PartGoodsShowBO> resultList = redisClientTemplate.lazyGetList(key, PartGoodsShowBO.class);
        if(resultList == null || resultList.isEmpty()){
            resultList = getShowBOFromSql(partLId,brandName);
            redisClientTemplate.lazySet(key, resultList, null);
        }

        return resultList;
    }

    //删除当前车型数据
    public void deleteGoodsByCar(Integer partLId) {
        String key = String.format(RedisKeyBean.PartGoodsShowKey, partLId);
        redisClientTemplate.delKey(key);
    }




    /*将数据库取出的数据拼成showBO，liyang拼凑进list*/
    private List<PartGoodsShowBO> getShowBOFromSql(Integer partLId,String brandName){
//        Set<PartGoodsShowBO> resultSet = new HashSet<>();
        List<PartGoodsShowBO> resultList = new ArrayList<>();

        //根据goodsid和picid 来凭借 show的数据
        Map<String,PartGoodsShowBO> showBOMap = new HashMap<>();

        String theLiyangTable = partLiyangTableDOMapper.selectByBrandName(brandName);
        if(theLiyangTable == null){
            return resultList;
        }
        List<PartLiyangRelationDO> liyangRelationDOList = partLiyangRelationDOMapper.selectByPartLId(partLId,theLiyangTable);

        Map<String,CategoryPartDO> partMap = new HashMap<>();
        Map<String,BasePartGoodDO> goodsMap = new HashMap<>();
        Map<String,PartPictureDO> picMap = new HashMap<>();
        Map<String,CarInfoAllDO> liyangMap = new HashMap<>();
        Map<String,PartSubjoinDO> subjoinMap = new HashMap<>();
        for(PartLiyangRelationDO partLiyangRelationDO : liyangRelationDOList){
            String goodsId = partLiyangRelationDO.getGoodsId();
            String picId = partLiyangRelationDO.getPicId();
            String liyangId = partLiyangRelationDO.getLiyangId();
            String subjoinId = partLiyangRelationDO.getSubjoinId();

            //取出数据
            BasePartGoodDO basePartGoodDO = goodsMap.get(goodsId);
            if(basePartGoodDO == null ){
                basePartGoodDO = basePartGoodDOMapper.selectByUuid(goodsId);
                if(basePartGoodDO == null){
                    System.out.println("basePartGoodDO null");
                }
                goodsMap.put(goodsId,basePartGoodDO);
            }

            PartPictureDO partPictureDO = picMap.get(picId);
            if(partPictureDO == null){
                partPictureDO = partPictureDOMapper.selectByUuid(picId);
                picMap.put(picId,partPictureDO);
            }

            CarInfoAllDO carInfoAllDO = liyangMap.get(liyangId);
            if(null == carInfoAllDO) {
                carInfoAllDO = carInfoAllDOMapper.selectByLeyelId(liyangId);
                liyangMap.put(liyangId,carInfoAllDO);
            }

            PartSubjoinDO subjoinDO = subjoinMap.get(subjoinId);
            if(null == subjoinDO){
                PartSubjoinDO searchDO = new PartSubjoinDO();
                searchDO.setUuid(subjoinId);
                subjoinDO = subjoinDOMapper.selectByDO(searchDO);
                subjoinMap.put(subjoinId,subjoinDO);
            }
            //拼接数据为 PartGoodsShowBO
            String showKey = goodsId+"-"+picId;
            PartGoodsShowBO partGoodsShowBO = showBOMap.get(showKey);
            if(null == partGoodsShowBO) {
                partGoodsShowBO = new PartGoodsShowBO();
                partGoodsShowBO.setPicId(picId);
                partGoodsShowBO.setPictureIndex(partPictureDO.getPictureIndex());
                partGoodsShowBO.setPictureNum(partPictureDO.getPictureNum());
                partGoodsShowBO.setGoodsId(goodsId);
                partGoodsShowBO.setOeNumber(basePartGoodDO.getOeNumber());
                partGoodsShowBO.setPartName(basePartGoodDO.getPartName());

                String sumCode = basePartGoodDO.getPartCode();
                partGoodsShowBO.setPartCode(sumCode);
                partGoodsShowBO.setRemarks(subjoinDO.getRemarks());
                partGoodsShowBO.setApplicationAmount(subjoinDO.getApplicationAmount());

                //设置分类
                CategoryPartDO partDO = partMap.get(sumCode);
                if(null == partDO){
                    partDO = categoryPartDOMapper.getCategoryPartBySumCode(sumCode);
                    partMap.put(sumCode,partDO);
                }

                String firstCode = sumCode.substring(0, 2);
                String secondCode = sumCode.substring(2, 4);
                String thirdCode = sumCode.substring(4, 7);

                partGoodsShowBO.setFirstCateCode(firstCode);
                partGoodsShowBO.setFirstCateName(partDO.getFirstCatName());

                partGoodsShowBO.setSecondCateCode(secondCode);
                partGoodsShowBO.setSecondCateName(partDO.getSecondCatName());

                partGoodsShowBO.setThirdCateCode(thirdCode);
                partGoodsShowBO.setThirdCateName(partDO.getThirdCatName());

                partGoodsShowBO.setVehicleCode(partDO.getVehicleCode());
            }
            //合并liyang
            List<String> liyangList = partGoodsShowBO.getLiyangList();
            if(null == liyangList) liyangList = new ArrayList<>();
            List<String> liyangShowList = partGoodsShowBO.getLiyangShowList();
            if(null == liyangShowList) liyangShowList = new ArrayList<>();
            String liyangShowValue = carInfoAllDO.getLeyelId() + "-" + carInfoAllDO.getMarketName() + "-" + carInfoAllDO.getDisplacement() + "-" + carInfoAllDO.getPublicYear() + "-" + carInfoAllDO.getTransmissionType();

            liyangList.add(liyangId);
            liyangShowList.add(liyangShowValue);
            partGoodsShowBO.setLiyangList(liyangList);
            partGoodsShowBO.setLiyangShowList(liyangShowList);
            showBOMap.put(showKey,partGoodsShowBO);
        }
        // 遍历map 取出数据
        for(Map.Entry<String,PartGoodsShowBO> showBOEntry : showBOMap.entrySet()){
            resultList.add(showBOEntry.getValue());
        }

        return  resultList;
    }


}
