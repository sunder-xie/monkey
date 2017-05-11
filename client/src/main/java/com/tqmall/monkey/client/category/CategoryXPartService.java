package com.tqmall.monkey.client.category;

import java.util.List;
import java.util.Map;

/**
 * Created by lyj on 2015/12/16.
 * 10:06
 */
public interface CategoryXPartService {
    /**
     * 根据map参数获取 分类级联标准零件  的记录
     * @param paramMap
     * @return
     */
    List<Map<String,Object>> getCategoryXPart(Map<String, Object> paramMap);

    /**
     * 根据map参数获取 分类级联标准零件  的记录  的数量（用于分页）
     * @param paramMap
     * @return
     */
    int getCategoryXPartCount(Map<String, Object> paramMap);

    /**
     * 获取 分类级联标准零件 记录的所有不同的VehicleCode（用distinct过滤了）
     * @return
     */
    List<String> getVehicleCode();

    /**
     * 根据vehicleCode、level或parentId 获取 分类的cat_id as catId、catName、vehicleCode和catCode（基本用于，下拉列表框 和 展示已有项目 的时候）
     * @param paramMap
     * @return
     */
    List<Map> getCategoryAttrToUseInSelect(Map<String, Object> paramMap);

    /**
     * 根据thirdCatId获取 标准零件 的id、partName和partCode
     * @param paramMap
     * @return
     */
    List<Map> getPartAttrToUseInTable(Map<String, Object> paramMap);

    /**
     * 刷新redis缓存中的版本号(加一),返回更新后的版本号
     * @return
     */
    String refreshCatXPartExportExcelVersion();

    /**
     * 获取redis混存中的版本号
     */
    String getCatXPartExportExcelVersion();

    /**
     * 删除redis混存中的版本号
     */
    void deleteCatXPartExportExcelVersion();
}
