package com.tqmall.monkey.client.lop;

import com.tqmall.core.common.entity.Result;
import com.tqmall.data.epc.client.bean.dto.avid.AvidCallDTO;
import com.tqmall.monkey.domain.bizBO.avid.AvidListShowBO;
import com.tqmall.monkey.domain.bizBO.avid.AvidSearchBO;
import com.tqmall.monkey.domain.bizBO.avid.ModifyAvidCallBO;
import com.tqmall.monkey.domain.bizBO.lop.WishListRequestBO;
import com.tqmall.monkey.domain.commonBean.Page;

/**
 * Created by huangzhangting on 16/10/30.
 */
public interface AvidCallBiz {

    Result<AvidCallDTO> getAvidCallById(Integer id);

    Result<Integer> turnWish(WishListRequestBO requestBO);

    Result<Page<AvidListShowBO>> getAvidPageShow(AvidSearchBO avidSearchBO);

    Result cancelAvidCall(Integer id, String reason);

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
