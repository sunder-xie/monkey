package com.tqmall.monkey.client.category;

import com.tqmall.monkey.client.redisManager.Category.CategoryRedisManager;
import com.tqmall.monkey.dal.mapper.category.CatXPartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by lyj on 2015/12/16.10:02
 * modified by lyj on 2016/06/01
 * 更改分类标准零件的mapper, 优化底层SQL
 */
@Transactional
@Service
public class CategoryXPartServiceImpl implements CategoryXPartService {

//    @Autowired
//    private CategoryXPartMapper categoryXPartMapper;

    @Autowired
    private CatXPartMapper catXPartMapper;

    @Autowired
    private CategoryRedisManager categoryRedisManager;

    @Override
    public List<Map<String, Object>> getCategoryXPart(Map<String, Object> paramMap) {
        return catXPartMapper.selectPageData(paramMap);
    }

    @Override
    public int getCategoryXPartCount(Map<String, Object> paramMap) {
        return catXPartMapper.selectRowCount(paramMap);
    }

    @Override
    public List<String> getVehicleCode() {
        return catXPartMapper.selectVehicleCode();
    }

    @Override
    public List<Map> getCategoryAttrToUseInSelect(Map<String, Object> paramMap) {
        return catXPartMapper.selectCategoryAttrToUseInSelect(paramMap);
    }

    @Override
    public List<Map> getPartAttrToUseInTable(Map<String, Object> paramMap) {
        return catXPartMapper.selectPartAttrToUseInTable(paramMap);
    }

    @Override
    public String refreshCatXPartExportExcelVersion() {
        return categoryRedisManager.updateCatXPartExportExcelVersion();
    }

    @Override
    public String getCatXPartExportExcelVersion() {
        return categoryRedisManager.selectCatXPartExportExcelVersion();
    }

    @Override
    public void deleteCatXPartExportExcelVersion() {
        categoryRedisManager.deleteCatXPartExportExcelVersion();
    }
}
