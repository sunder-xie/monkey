package com.tqmall.monkey.dal.mapper.commodity;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.domain.entity.commodity.CommodityGoodsDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisRepository
public interface CommodityGoodsDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(CommodityGoodsDO record);

    CommodityGoodsDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CommodityGoodsDO record);

    /**
     * 根据where对象 更新指定对象
     * @param record 更新后 的字段级
     * @param whereRecord 判断条件
     */
    int updateByGoodsSelective(@Param(value = "record")CommodityGoodsDO record,@Param(value = "exist")CommodityGoodsDO whereRecord);

    /*=================自定义================*/
    CommodityGoodsDO getCommodityGoodsDOByCode(String goodsCode);

    boolean existCommodityGoods(String goodsCode);

    void insertBatch(List<CommodityGoodsDO> list);
}