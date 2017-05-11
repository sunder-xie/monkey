package com.tqmall.monkey.client.category;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.tqmall.monkey.domain.entity.category.CategoryDO;
import com.tqmall.monkey.domain.entity.category.CategoryPartDO;

import java.util.List;

/**
 * Created by huangzhangting on 15/6/25.
 * modify by LYJ on 2015-12
 * 16:52
 */
public interface CategoryPartService {

    CategoryPartDO findCategoryPartById(Integer partId);

    int saveCategoryPart(CategoryPartDO categoryPart) throws MySQLIntegrityConstraintViolationException;

    int modifyCategoryPart(CategoryPartDO categoryPart);

    List<CategoryPartDO> getAllPartLists();

    /*=============================业务接口============================*/
    CategoryPartDO findCategoryPartBySumCode(String sumCode);

    /**
     * 获取同一个3J分类下的所有标准零件的VehicleCode的种类数
     *
     * @param thirdCatId
     * @return
     */
    int getVehicleCodeCount(Integer thirdCatId);

    List<CategoryPartDO> findCategoryPartByThirdCatId(Integer thirdCatId);

    /**
     * 停用标准零件
     *
     * @param partId
     */
    void stopUsePart(Integer partId);

    /**
     * 获取停用的part
     *
     * @param firstCatId
     * @param secondCatId
     * @param thirdCatId
     * @param partName
     * @return
     */
    List<CategoryDO> getStoppedCategoryPart(Integer firstCatId, Integer secondCatId, Integer thirdCatId, String partName);

    /**
     * 有具体业务的修改part
     *
     * @param oldCategoryPart(原来的对象)
     * @param categoryPart
     * @return
     */
    int modifyCategoryPartBusiness(CategoryPartDO oldCategoryPart, CategoryPartDO categoryPart);

    /**
     * 复用标准零件
     *
     * @param categoryPartDO
     */
    void reuseCategoryPart(CategoryPartDO categoryPartDO);
}
