package com.tqmall.monkey.client.category;

import com.tqmall.monkey.domain.entity.category.CategoryDO;

import java.util.List;

/**
 * Created by huangzhangting on 15/6/25.
 */
public interface CategoryService {

    CategoryDO findCategoryById(Integer id);

    /**
     * 根据code和level获得CategoryDO
     *
     * @param vehicleCode 可选，车辆种类码：C H
     * @param code        必填，自身编码
     * @param level       必填，分类级别
     * @param pid         可选，此分类的父id
     * @return
     */
    List<CategoryDO> findCategoryForCheck(String vehicleCode, String code, Integer level, Integer pid);

    /**
     * 有业务的新增分类
     * @param category
     */
    void saveCategoryDO(CategoryDO category);

    /**
     * 单纯的根据对象修改
     * @param category
     */
    void modifyCategoryDO(CategoryDO category);

    /**
     * 有业务的修改分类
     * @param category
     */
    void modifyCategoryDOBusiness(CategoryDO category);

    /**
     * 获取指定catId的所有子类不同种类的VehicleCode的数量
     * @param catId
     * @return
     */
    int getVehicleCodeCount(Integer catId);

    /**
     * 根据catId 和 level(该分类所在的层级) 来停用分类
     * @param cateId
     * @param level
     */
    void stopCategoryById(Integer cateId, Integer level);

    /**
     * 根据层级和父ID获取停用的分类
     * @param level
     * @param parentId
     * @return
     */
    List<CategoryDO> getStoppedCategory(Integer level, Integer parentId);

    /**
     * 由于原有的根据主键查询的mapper只能查询出未删除的
     * 根据主键查询出已删除的
     * @param catId
     * @return
     */
    CategoryDO findDeletedCategoryById(Integer catId);

    /**
     * 复用分类
     * @param categoryDO
     */
    void reuseCategory(CategoryDO categoryDO);

    /**
     * 根据catId维护他的父级的Leaf 和 Vc属性
     * @param catId
     */
    public void toMaintainLeafVc(Integer catId);
}
