package com.tqmall.monkey.domain.bizBO.lop;

import lombok.Data;

/**
 * 电商类目，搜索返回结果对象
 * Created by huangzhangting on 16/8/1.
 */
@Data
public class CateBO {
    private Integer id; //主键id

    private String word; //名称
}
