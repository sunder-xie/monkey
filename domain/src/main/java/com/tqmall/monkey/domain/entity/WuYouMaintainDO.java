package com.tqmall.monkey.domain.entity;

import lombok.Data;

/**抓取数据-商品表对应对象结构
 * Created by ruibai on 15/3/20.
 */

@Data
public class WuYouMaintainDO {
    private Integer goodsId ;  // 商品ID
    private String goodsTitle ; //商品
    private Integer carRunServiceId;
    private Integer modelServiceId ;
    private Integer carId ; //养车51车型ID
    private Integer tqmallCarId ;  //淘汽车型ID
    private Integer serviceId ; //服务ID，2家相同
    private Integer id ;
}
