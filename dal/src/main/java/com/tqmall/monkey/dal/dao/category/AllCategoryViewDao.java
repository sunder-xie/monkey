package com.tqmall.monkey.dal.dao.category;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.dal.dao.base.BaseDao;
import com.tqmall.monkey.dal.mapper.category.AllCategoryViewDOMapper;
import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.category.AllCategoryViewDO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by huangzhangting on 15/6/29.
 */
@MyBatisRepository
public class AllCategoryViewDao extends BaseDao{
    private final static String NAMESPACE = "com.tqmall.monkey.dal.mapper.category.AllCategoryViewDOMapper";

    @Autowired
    private AllCategoryViewDOMapper mapper;

    public AllCategoryViewDOMapper getMapper() {
        return mapper;
    }


    public Page<AllCategoryViewDO> getAllCategoryPage(Map<String, Object> params, int pageIndex, int pageSize){
        String selectSql = NAMESPACE + ".getAllCategory";
        String countSql = NAMESPACE + ".getAllCategoryCount";

        return this.queryPage(selectSql, countSql, params, pageIndex, pageSize);
    }

    public Page<AllCategoryViewDO> getAllCategoryByFirstCatId(Map<String, Object> params, int pageIndex, int pageSize){
        String selectSql = NAMESPACE + ".getAllCategoryByFirstCatId";
        String countSql = NAMESPACE + ".getAllCategoryByFirstCatIdCount";

        return this.queryPage(selectSql, countSql, params, pageIndex, pageSize);
    }

    public Page<AllCategoryViewDO> getAllCategoryBySecondCatId(Map<String, Object> params, int pageIndex, int pageSize){
        String selectSql = NAMESPACE + ".getAllCategoryBySecondCatId";
        String countSql = NAMESPACE + ".getAllCategoryBySecondCatIdCount";

        return this.queryPage(selectSql, countSql, params, pageIndex, pageSize);
    }

    public Page<AllCategoryViewDO> getAllCategoryByThirdCatId(Map<String, Object> params, int pageIndex, int pageSize){
        String selectSql = NAMESPACE + ".getAllCategoryByThirdCatId";
        String countSql = NAMESPACE + ".getAllCategoryByThirdCatIdCount";

        return this.queryPage(selectSql, countSql, params, pageIndex, pageSize);
    }


    public Page<AllCategoryViewDO> selectAllCategorysPage(Map<String, Object> params, int pageIndex, int pageSize){
        String selectSql = NAMESPACE + ".selectAllCategorys";
        String countSql = NAMESPACE + ".selectAllCategorysCount";

        return this.queryPage(selectSql, countSql, params, pageIndex, pageSize);
    }

    public Page<AllCategoryViewDO> selectAllCategorysPageByFirstCatId(Map<String, Object> params, int pageIndex, int pageSize){
        String selectSql = NAMESPACE + ".selectAllCategorysByFirstCatId";
        String countSql = NAMESPACE + ".selectAllCategorysByFirstCatIdCount";

        return this.queryPage(selectSql, countSql, params, pageIndex, pageSize);
    }

    public Page<AllCategoryViewDO> selectAllCategorysPageBySecondCatId(Map<String, Object> params, int pageIndex, int pageSize){
        String selectSql = NAMESPACE + ".selectAllCategorysBySecondCatId";
        String countSql = NAMESPACE + ".selectAllCategorysBySecondCatIdCount";

        return this.queryPage(selectSql, countSql, params, pageIndex, pageSize);
    }

    public Page<AllCategoryViewDO> selectAllCategorysPageByThirdCatId(Map<String, Object> params, int pageIndex, int pageSize){
        String selectSql = NAMESPACE + ".selectAllCategorysByThirdCatId";
        String countSql = NAMESPACE + ".selectAllCategorysByThirdCatIdCount";

        return this.queryPage(selectSql, countSql, params, pageIndex, pageSize);
    }

}
