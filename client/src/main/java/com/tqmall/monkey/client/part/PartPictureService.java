package com.tqmall.monkey.client.part;

import com.tqmall.monkey.domain.entity.part.PartPictureDO;

import java.util.List;

/**
 * 配件库的图片Service
 * Created by zxg on 15/7/27.
 */
public interface PartPictureService {

    /**
     * 是否存在该partPicture，存在取出其id列表
     * @param partPictureDO
     * @return
     */
    List<PartPictureDO> exitPartPicture(PartPictureDO partPictureDO);

    /**
     * 存在返回uuid，不存在返回null
     * @param partPictureDO
     * @return
     */
    String getUuIdWithExist(PartPictureDO partPictureDO);

    Integer insertPartPictureWithoutExit(PartPictureDO partPictureDO);

    /**
     * 根据uuId列表，批量删除该数据
     * @param uuIdList
     * @return
     */
    boolean deleteBatchGoodsByUuId(List<String> uuIdList);
}
