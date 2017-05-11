package com.tqmall.monkey.external.dubbo.lop;

import com.tqmall.core.common.entity.Result;
import com.tqmall.data.epc.client.bean.dto.avid.AvidCallDTO;
import com.tqmall.data.epc.client.bean.param.avid.*;
import com.tqmall.data.epc.client.server.avid.AvidCallService;
import com.tqmall.monkey.component.utils.BdUtil;
import com.tqmall.monkey.component.utils.JsonUtil;
import com.tqmall.monkey.domain.bizBO.avid.AvidSearchBO;
import com.tqmall.monkey.domain.bizBO.avid.ModifyAvidCallBO;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.dubbo.client.cloudepc.param.EpcAvidCallRequest;
import com.tqmall.search.dubbo.client.cloudepc.result.EpcAvidCallDTO;
import com.tqmall.search.dubbo.client.cloudepc.service.CloudEpcService;
import com.tqmall.monkey.domain.bizBO.lop.WishListRequestBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Created by huangzhangting on 16/10/30.
 */
@Slf4j
@Component
public class AvidCallServiceExtImpl implements AvidCallServiceExt {
    @Autowired
    private AvidCallService avidCallService;
    @Autowired
    private CloudEpcService cloudEpcService;


    @Override
    public AvidCallDTO getAvidCallById(Integer id) {
        Result<AvidCallDTO> result = avidCallService.getAvidCallById(id);
        if(result.isSuccess()){
            return result.getData();
        }
        log.info("getAvidCallById failed, id:{}, result:{}", id, JsonUtil.objectToJson(result));
        return null;
    }


    @Override
    public Result<Integer> turnWish(WishListRequestBO requestBO) {
        log.info("turnWish, param:{}", JsonUtil.objectToJson(requestBO));

        TurnWishParam param = BdUtil.do2bo(requestBO, TurnWishParam.class);
        param.setGoodsList(BdUtil.do2bo4List(requestBO.getGoodsList(), TurnWishGoodsParam.class));

        Result<Integer> result = avidCallService.turnWish(param);

        log.info("turnWish, result:{}", JsonUtil.objectToJson(result));
        return result;
    }


    @Override
    public Page<EpcAvidCallDTO> getAvidPage(AvidSearchBO searchBO) {
        EpcAvidCallRequest request = new EpcAvidCallRequest();
        String shopName = searchBO.getShopName();
        String shopTel = searchBO.getShopTel();
        String carName = searchBO.getCarName();
        Integer avidCallStatus = searchBO.getAvidCallStatus();
        if(!StringUtils.isEmpty(shopName)) {
            request.setShopName(shopName.replaceAll(" +", ""));
        }
        if(!StringUtils.isEmpty(shopTel)) {
            request.setShopTel(shopTel.replaceAll(" +", ""));
        }
        if(!StringUtils.isEmpty(carName)) {
            request.setCarName(carName.replaceAll(" +", ""));
        }
        if(avidCallStatus != null && avidCallStatus > -1 ) {
            request.setAvidCallStatus(avidCallStatus);
        }

        Integer page = searchBO.getPageIndex() < 1 ? 0:searchBO.getPageIndex()-1;
        PageableRequest pageableRequest = new PageableRequest(page,searchBO.getPageSize());
        com.tqmall.search.common.result.Result<Page<EpcAvidCallDTO>> result = cloudEpcService.queryEpcAvidCalls(request, pageableRequest);
        if (result.isSuccess()){
            return result.getData();
        }
        log.info("getAvidPage failed, param:{}, result:{}",
                JsonUtil.objectToJson(request), JsonUtil.objectToJson(result));
        return null;
    }

    @Override
    public Result cancelAvidCall(CancelAvidCallParam param) {
        log.info("cancelAvidCall param:{}", JsonUtil.objectToJson(param));
        Result result = avidCallService.cancelAvidCall(param);
        log.info("cancelAvidCall result:{}", JsonUtil.objectToJson(result));
        return result;
    }

    @Override
    public Result modifyAvidCall(ModifyAvidCallBO modifyAvidCallBO) {
        log.info("modifyAvidCall param:{}", JsonUtil.objectToJson(modifyAvidCallBO));

        ModifyAvidCallParam param = BdUtil.do2bo(modifyAvidCallBO, ModifyAvidCallParam.class);
        param.setGoodsParamList(BdUtil.do2bo4List(modifyAvidCallBO.getGoodsParamList(), ModifyAvidCallGoodsParam.class));

        Result result = avidCallService.modifyAvidCall(param);

        log.info("modifyAvidCall result:{}", JsonUtil.objectToJson(result));
        return result;
    }

    @Override
    public Result<Long> getAllNewDataNum() {
        Result<Long> result = avidCallService.getAllNewDataNum();
        log.info("getAllNewDataNum result:{}", JsonUtil.objectToJson(result));
        return result;
    }

    @Override
    public Result<Long> getFiveMinNewDataNum() {
        Result<Long> result = avidCallService.getFiveMinNewDataNum();
        log.info("getFiveMinNewDataNum result:{}", JsonUtil.objectToJson(result));
        return result;
    }
}
