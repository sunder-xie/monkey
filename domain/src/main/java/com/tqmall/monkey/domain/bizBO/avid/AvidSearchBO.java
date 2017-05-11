package com.tqmall.monkey.domain.bizBO.avid;

import lombok.Data;

/**
 * Created by zxg on 16/10/31.
 * 16:28
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Data
public class AvidSearchBO {
    String shopName;
    String shopTel;
    String carName;
    Integer avidCallStatus;

    Integer pageIndex = 1;//页码，从1开始，默认第一页
    Integer pageSize = 20;//每页展示的数量,默认20 条
}
