package com.tqmall.monkey.dal.mapper.category;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.domain.entity.category.AllCategoryViewDO;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface AllCategoryViewDOMapper {
    //查询所有未停用的数据
    List<AllCategoryViewDO> getAllCategory(Map<String, Object> params);

    List<AllCategoryViewDO> getAllCategoryByFirstCatId(Map<String, Object> params);

    List<AllCategoryViewDO> getAllCategoryBySecondCatId(Map<String, Object> params);

    List<AllCategoryViewDO> getAllCategoryByThirdCatId(Map<String, Object> params);

    //查询所有数据
    List<AllCategoryViewDO> selectAllCategorys(Map<String, Object> params);

    List<AllCategoryViewDO> selectAllCategorysByFirstCatId(Map<String, Object> params);

    List<AllCategoryViewDO> selectAllCategorysBySecondCatId(Map<String, Object> params);

    List<AllCategoryViewDO> selectAllCategorysByThirdCatId(Map<String, Object> params);
}