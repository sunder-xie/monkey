package com.tqmall.monkey.domain.entity.part;

import lombok.Data;

import java.util.Date;

/**
 * 力洋车型和goods和picture的关系
 * Created by zxg on 15/7/27.
 */
@Data
public class PartLiyangRelationDO {
    private Integer id;
    private String goodsId;
    private String picId;
    private String liyangId;
    private String partLiyangId;
    private String subjoinId;

    private Integer creator ;
    private Integer modifier ;

    private Date gmtModified;
    private Date gmtCreate;

}
