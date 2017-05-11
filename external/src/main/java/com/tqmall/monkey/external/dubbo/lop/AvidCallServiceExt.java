package com.tqmall.monkey.external.dubbo.lop;

import com.tqmall.core.common.entity.Result;
import com.tqmall.data.epc.client.bean.dto.avid.AvidCallDTO;
import com.tqmall.data.epc.client.bean.param.avid.CancelAvidCallParam;
import com.tqmall.monkey.domain.bizBO.avid.AvidSearchBO;
import com.tqmall.monkey.domain.bizBO.avid.ModifyAvidCallBO;
import com.tqmall.monkey.domain.bizBO.lop.WishListRequestBO;
import com.tqmall.search.dubbo.client.cloudepc.result.EpcAvidCallDTO;
import org.springframework.data.domain.Page;

/**
 * Created by huangzhangting on 16/10/30.
 */
public interface AvidCallServiceExt {

    AvidCallDTO getAvidCallById(Integer id);

    Result<Integer> turnWish(WishListRequestBO requestBO);

    Page<EpcAvidCallDTO> getAvidPage(AvidSearchBO searchBO);

    Result cancelAvidCall(CancelAvidCallParam param);

    Result modifyAvidCall(ModifyAvidCallBO modifyAvidCallBO);

    /**
     * 查询全部未跟进的记录数
     * @return
     */
    Result<Long> getAllNewDataNum();

    /**
     * 查询5分钟内未跟进的记录数
     * @return
     */
    Result<Long> getFiveMinNewDataNum();
}
