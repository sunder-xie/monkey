package com.tqmall.monkey.domain.bizBO.cate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by zxg on 15/12/22.
 * 17:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryBO {
    private String catName;

    private String catCode;

    //part的vehicleCode
    private String vehicleCode;

}
