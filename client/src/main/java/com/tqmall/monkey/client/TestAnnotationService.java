package com.tqmall.monkey.client;

import com.tqmall.monkey.client.car.OfferCarService;
import com.tqmall.monkey.client.offerGoods.OfferRecordService;
import com.tqmall.monkey.domain.entity.car.OfferCarDO;
import com.tqmall.monkey.domain.entity.offerGoods.OfferRecordDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 测试事务管理的类--测试通过
 * Created by zxg on 15/7/16.
 */

@Transactional
@Service
public class TestAnnotationService {
    @Autowired
    private OfferCarService offerCarService;
    @Autowired
    private OfferRecordService offerRecordService;


    public void testFangfa(){
        OfferCarDO offerCarDO = new OfferCarDO();
        offerCarDO.setBrandName("brand_name");
        offerCarService.insertWithOutExit(offerCarDO);

        OfferRecordDO offerRecordDO = new OfferRecordDO();
        offerRecordDO.setId(13);
        offerRecordDO.setRecordName("test");

        offerRecordService.updateRecord(offerRecordDO);
    }
}
