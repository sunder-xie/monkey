package com.tqmall.monkey.domain.entity;


import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by ruibai on 15/3/10.
 * db_model_maintain_goods_relation表
 */

@Data
public class MaintainDO {
    private Integer id;
    private Timestamp gmtCreate ;
    private Timestamp gmtModified ;
    private Integer creator ;
    private Integer modifier ;
    private Integer modelId;
    private Integer serviceId ;
    private Integer goodId ;
}
