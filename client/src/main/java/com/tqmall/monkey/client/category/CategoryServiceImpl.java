package com.tqmall.monkey.client.category;

import com.tqmall.monkey.dal.mapper.category.CategoryDOMapper;
import com.tqmall.monkey.dal.mapper.category.CategoryPartDOMapper;
import com.tqmall.monkey.domain.entity.category.CategoryDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangzhangting on 15/6/25.
 */
@Transactional
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryDOMapper categoryDOMapper;

    @Autowired
    private CategoryPartDOMapper categoryPartDOMapper;

    @Override
    public List<CategoryDO> findCategoryForCheck(String vehicleCode, String catCode, Integer catLevel, Integer parentId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("vehicleCode", vehicleCode);
        paramMap.put("catCode", catCode);
        paramMap.put("catLevel", catLevel);
        paramMap.put("parentId", parentId);
        return categoryDOMapper.getCategoryForCheck(paramMap);
    }

    @Override
    public CategoryDO findCategoryById(Integer id) {
        return categoryDOMapper.selectByPrimaryKey(id);
    }

    @Override
    public void saveCategoryDO(CategoryDO category) {
        categoryDOMapper.insertSelective(category);
    }

    @Override
    public void modifyCategoryDO(CategoryDO category) {
        categoryDOMapper.updateByPrimaryKeySelective(category);
    }

    @Override
    @Transactional
    public void modifyCategoryDOBusiness(CategoryDO category) {
        //根据catId获取原来的对象
        CategoryDO pristineCat = categoryDOMapper.selectByPrimaryKey(category.getCatId());

        //为空或为-1，表示的是1J的修改   或   是2J或3J的修改且是没有选择上层分类，结果都是引用原来的上层分类
        if (category.getParentId() == null || category.getParentId() == -1) {
            category.setParentId(pristineCat.getParentId());
        }

        categoryDOMapper.updateByPrimaryKeySelective(category);

        //如果移动了目录，那么需要维护父级的is_leaf属性 和 VC
        if (pristineCat.getCatLevel().intValue() != CategoryDO.FIRST_LEVEL && category.getParentId() != -1 && category.getParentId().intValue() != pristineCat.getParentId().intValue()) {
            //维护老的目录
            maintainLeafVc(pristineCat.getCatId());

            //维护新的目录
            maintainLeafVc(category.getCatId());
        }

        //更新所有的part的相关层级ID和sumCode
        //categoryPartDOMapper.updateLevelCatIdSumCode(pristineCat.getCatLevel(), category.getCatId(), category.getParentId(), category.getCatCode());

        Integer parentId = null;
        String parentName = null;
        String parentCode = null;
        CategoryDO parentCat = categoryDOMapper.selectByPrimaryKey(category.getParentId());
        if(parentCat != null){
            parentId = parentCat.getCatId();
            parentName = parentCat.getCatName();
            parentCode = parentCat.getCatCode();
        }
        categoryPartDOMapper.updateLevelCatIdSumCode(pristineCat.getCatLevel(), category.getCatId(), category.getCatName(), category.getCatCode(), parentId, parentName,parentCode);
    }

    @Override
    public int getVehicleCodeCount(Integer catId) {
        return categoryDOMapper.selectVehicleCodeCount(catId);
    }

    @Override
    @Transactional
    public void stopCategoryById(Integer catId, Integer level) {
        //向后级联设置状态为 删除状态
        categoryDOMapper.setYesDeleteFlagInCascade(level, catId);

        //设置所有的part为删除状态
        categoryPartDOMapper.updateIsDeletedByLevelCatId(level, catId, CategoryDO.IS_DELETED_TRUE);

        switch (level) {
            case 1:
                break;
            case 2:
                maintainLeafVc(catId);
                break;
            case 3:
                maintainLeafVc(catId);
                break;
            default:
                break;
        }
    }

    @Override
    public List<CategoryDO> getStoppedCategory(Integer level, Integer parentId) {
        return categoryDOMapper.selectStoppedCategory(level, parentId);
    }

    @Override
    public CategoryDO findDeletedCategoryById(Integer catId) {
        return categoryDOMapper.selectDeletedByPrimaryKey(catId.shortValue());
    }

    @Override
    @Transactional
    public void reuseCategory(CategoryDO categoryDO) {
        categoryDO.setIsDeleted(CategoryDO.IS_DELETED_FALSE);
        categoryDO.setIsLeaf(CategoryDO.IS_LEAF_TRUE);
        categoryDO.setVehicleCode(CategoryDO.VEHICLE_CODE_NAN);
        this.categoryDOMapper.updateByPrimaryKeySelective(categoryDO);

        maintainLeafVc(categoryDO.getCatId());
    }

    @Override
    public void toMaintainLeafVc(Integer catId) {
        maintainLeafVc(catId);
    }

    /**
     * 维护catId上层的isLeaf和vehicleCode属性
     * BY LYJ
     *
     * @param catId
     */
    public void maintainLeafVc(Integer catId) {
        CategoryDO parentCategory = categoryDOMapper.selectParentById(catId);

        List<CategoryDO> childrenList = categoryDOMapper.selectChildren(parentCategory.getCatId());
        if (childrenList.size() == 0) {
            parentCategory.setIsLeaf(CategoryDO.IS_LEAF_TRUE);
            parentCategory.setVehicleCode(CategoryDO.VEHICLE_CODE_NAN);
        } else {
            if (categoryDOMapper.selectVehicleCodeCount(parentCategory.getCatId()) > 1) {
                parentCategory.setVehicleCode(CategoryDO.VEHICLE_CODE_CH);
            } else {
                parentCategory.setVehicleCode(childrenList.get(0).getVehicleCode());
            }
        }
        categoryDOMapper.updateByPrimaryKeySelective(parentCategory);

        if (parentCategory.getCatLevel() > 1) {
            maintainLeafVc(parentCategory.getCatId());//递归维护上级
        }
    }
}
