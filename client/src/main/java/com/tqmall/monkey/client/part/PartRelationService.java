package com.tqmall.monkey.client.part;

import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.part.PartLiyangRelationDO;
import com.tqmall.monkey.domain.entity.part.PartGoodsPicBaseDO;

import java.util.List;
import java.util.Map;

/**
 * 存取goods、car、picture三者关系
 * Created by zxg on 15/7/27.
 */
public interface PartRelationService {

    /**
     * 根据配件id和图片id获得力洋id
     * @param index 查询的页面
     * @param pageSize 当页显示的数据数目
     * @param goodsId 配件id
     * @param picId
     * @return
     */
    Page<String> selectLiyangIdPageByGoodsIdPicId(Integer index,Integer pageSize,String goodsId,String picId,Integer partLId,String brandName);

    /**
     * 根据partLId 到对应的关系子表中删除数据
     * @param partLId db_monkey_part_liyang_base 的主键（已整理的配件库 力洋车型）
     * @param brandName 品牌名，找到对应关系
     * @return
     */
    Boolean deleteByPartLId(Integer partLId,String brandName);
}
