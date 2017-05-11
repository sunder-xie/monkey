package com.tqmall.monkey.dal.mapper.category;

import com.tqmall.monkey.dal.MyBatisRepository;

import java.util.List;
import java.util.Map;

/**
 * Created by lyj on 2015/12/16.
 */

@MyBatisRepository
public interface CategoryXPartMapper {
    List<Map<String,Object>> selectCategoryXPart(Map<String, Object> paramMap);

    int selectCategoryXPartCount(Map<String, Object> paramMap);

    List<String> selectVehicleCode();

    List<Map> selectCategoryAttrToUseInSelect(Map<String, Object> paramMap);

    List<Map> selectPartAttrToUseInTable(Map<String, Object> paramMap);
}
