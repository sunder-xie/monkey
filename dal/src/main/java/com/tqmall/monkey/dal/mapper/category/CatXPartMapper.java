package com.tqmall.monkey.dal.mapper.category;

import com.tqmall.monkey.dal.MyBatisRepository;

import java.util.List;
import java.util.Map;

/**
 * Created by lyj on 2016/05/26
 * 分类底层SQL优化
 */

@MyBatisRepository
public interface CatXPartMapper {
    List<Map<String, Object>> selectPageData(Map<String, Object> paramMap);

    int selectRowCount(Map<String, Object> paramMap);

    List<String> selectVehicleCode();

    List<Map> selectCategoryAttrToUseInSelect(Map<String, Object> paramMap);

    List<Map> selectPartAttrToUseInTable(Map<String, Object> paramMap);
}
