package com.tqmall.monkey.dal.dao.category;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.dal.dao.base.BaseDao;
import com.tqmall.monkey.dal.mapper.category.CategoryDOMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by huangzhangting on 15/6/24.
 */

@MyBatisRepository
public class CategoryDao extends BaseDao {
    private final static String NAMESPACE = "com.tqmall.monkey.dal.mapper.category.CategoryDOMapper";

    @Autowired
    private CategoryDOMapper categoryDOMapper;

    public CategoryDOMapper getCategoryDOMapper() {
        return categoryDOMapper;
    }


}
