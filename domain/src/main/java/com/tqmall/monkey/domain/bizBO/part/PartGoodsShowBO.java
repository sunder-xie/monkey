package com.tqmall.monkey.domain.bizBO.part;

import com.tqmall.monkey.domain.entity.part.PartGoodsPicBaseDO;
import lombok.Data;

import java.util.List;

/**
 * 前端展示
 * Created by zxg on 15/12/22.
 * 15:46
 */
@Data
public class PartGoodsShowBO extends PartGoodsPicBaseDO {
    //liyang
    private List<String> liyangList;

    //数据库获得数据:力洋ID-销售名称-排量-上市年款-变速器形式
    private List<String> liyangShowList;

}
