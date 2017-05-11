package com.tqmall.monkey.client.offerGoods;

import com.tqmall.monkey.domain.entity.offerGoods.OfferLiCarDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zxg on 15/7/15.
 */
public interface OfferLiCarService {

    //插入记录：力洋id和offergoods-id
    Integer insertofferLiCarDOWithOut(OfferLiCarDO offerLiCarDO);

    //update
    Integer updateOfferLiCarDO(OfferLiCarDO offerLiCarDO);

    /**根据自定义需求更新数据**/
    void updateOfferLiCarDOByCustom(OfferLiCarDO offerLiCarDO,OfferLiCarDO existLiCarDO);

    /**
     * 根据goods_id、li_id/status查询记录，可谓空
     * @param goodsId 供应商商品ID——可null
     * @param liyangId  力洋库的主键Id——可null
     * @param status 状态--可null
     * @return
     */
    List<OfferLiCarDO> findOfferLiByGoodsIdLiIdStatus(Integer goodsId,String liyangId,Integer status);

    /**根据状态获得其数量
     * @param status 状态
     * @return
     */
    Integer getCarsSumByStatus(Integer status);


}
