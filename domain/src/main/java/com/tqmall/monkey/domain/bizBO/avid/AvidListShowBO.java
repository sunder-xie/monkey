package com.tqmall.monkey.domain.bizBO.avid;

import lombok.Data;

/**
 * Created by zxg on 16/10/31.
 * 17:19
 * no bug,以后改代码的哥们，祝你好运~！！
 * 列表展示的类
 */
@Data
public class AvidListShowBO {
    private Integer id;

    private String cityName;//城市站
    private String shopName;//门店名称
    String shopTel;//门店电话

    private String brandName;//品牌
    private String seriesName;//车系

    private String gmtCreate;//急呼时间
    String turnWishTime;//转需求蛋时间

    Integer avidCallStatus;//急呼单状态
    String cancelReason;//取消状态

    //====拼接的数据
    private String carName;//急呼车型

    public String getCarName(){
        StringBuilder carNameBuild = new StringBuilder();
        if(brandName != null && !brandName.equals("")){
            carNameBuild.append(brandName);
        }
        if(seriesName != null && !seriesName.equals("")){
            carNameBuild.append(" ").append(seriesName);
        }
        return carNameBuild.toString().trim();
    }
}
