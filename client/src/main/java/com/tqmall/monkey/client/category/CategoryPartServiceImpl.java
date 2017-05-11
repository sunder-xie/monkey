package com.tqmall.monkey.client.category;

import com.tqmall.monkey.dal.dao.category.CategoryPartDao;
import com.tqmall.monkey.dal.mapper.category.CategoryDOMapper;
import com.tqmall.monkey.dal.mapper.category.CategoryPartDOMapper;
import com.tqmall.monkey.domain.entity.category.CategoryDO;
import com.tqmall.monkey.domain.entity.category.CategoryPartDO;
import com.tqmall.monkey.domain.model.CategoryPartParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by huangzhangting on 15/6/25.
 * 17:02
 */
@Service
public class CategoryPartServiceImpl implements CategoryPartService {
    @Autowired
    private CategoryPartDao categoryPartDao;

    @Autowired
    CategoryPartDOMapper categoryPartDOMapper;

    @Autowired
    private CategoryDOMapper categoryDOMapper;

    @Autowired
    private CategoryServiceImpl categoryService;

    @Override
    public CategoryPartDO findCategoryPartBySumCode(String sumCode) {
        return categoryPartDao.getPartDOMapper().getCategoryPartBySumCode(sumCode);
    }

    @Override
    public int saveCategoryPart(CategoryPartDO categoryPart) {
        return categoryPartDOMapper.insertSelective(categoryPart);
    }

    @Override
    public int modifyCategoryPart(CategoryPartDO categoryPart) {
        return categoryPartDOMapper.updateByPrimaryKeySelective(categoryPart);
    }

    @Override
    public int getVehicleCodeCount(Integer thirdCatId) {
        return categoryPartDOMapper.selectVehicleCodeCount(thirdCatId);
    }

    @Override
    public CategoryPartDO findCategoryPartById(Integer partId) {
        return categoryPartDOMapper.selectByPrimaryKey(partId);
    }

    @Override
    public List<CategoryPartDO> findCategoryPartByThirdCatId(Integer thirdCatId) {
        CategoryPartParams categoryPartParams = new CategoryPartParams();
        categoryPartParams.setThirdCatId(thirdCatId);
        return categoryPartDOMapper.getCategoryListByParam(categoryPartParams);
    }

    @Override
    @Transactional
    public int modifyCategoryPartBusiness(CategoryPartDO pristinePart , CategoryPartDO categoryPart) {
        int resultFlat = -1;
        try {
            //为-1是没有选择上层分类，则引用原来的上层分类
            if (categoryPart.getThirdCatId() == -1) {
                categoryPart.setThirdCatId(pristinePart.getThirdCatId());
            }

            categoryPartDOMapper.updateByPrimaryKeySelective(categoryPart);

            //如果移动了目录 或 VC码变了，那么需要维护父级的is_leaf属性和VC码属性
            if (categoryPart.getThirdCatId().intValue() != pristinePart.getThirdCatId().intValue() || !categoryPart.getVehicleCode().equals(pristinePart.getVehicleCode())) {
                maintainLeafVc(pristinePart.getThirdCatId());
                maintainLeafVc(categoryPart.getThirdCatId());

                categoryService.toMaintainLeafVc(pristinePart.getThirdCatId());
                categoryService.toMaintainLeafVc(categoryPart.getThirdCatId());
            }

            categoryPartDOMapper.updateByPrimaryKeySelective(categoryPart);
            resultFlat = 1;
        } catch (Exception ex) {
            resultFlat = -1;
            ex.printStackTrace();
        } finally {
            return resultFlat;
        }

    }

    @Override
    public void reuseCategoryPart(CategoryPartDO categoryPartDO) {
        categoryPartDO.setIsDeleted(CategoryDO.IS_DELETED_FALSE);
        categoryPartDO.setVehicleCode(CategoryDO.VEHICLE_CODE_NAN);
        categoryPartDOMapper.updateByPrimaryKeySelective(categoryPartDO);

        maintainLeafVc(categoryPartDO.getThirdCatId());
        categoryService.toMaintainLeafVc(categoryPartDO.getThirdCatId());
    }

    @Override
    public List<CategoryPartDO> getAllPartLists() {
        return categoryPartDOMapper.getCategoryListByParam(new CategoryPartParams());
    }

    @Override
    @Transactional
    public void stopUsePart(Integer partId) {
        CategoryPartDO partDO = categoryPartDOMapper.selectByPrimaryKey(partId);
        partDO.setId(partId);
        partDO.setIsDeleted(CategoryDO.IS_DELETED_TRUE);
        categoryPartDOMapper.updateByPrimaryKeySelective(partDO);

        maintainLeafVc(partDO.getThirdCatId());
        categoryService.toMaintainLeafVc(partDO.getThirdCatId());

    }

    @Override
    public List<CategoryDO> getStoppedCategoryPart(Integer firstCatId, Integer secondCatId, Integer thirdCatId, String partName) {
        return categoryPartDOMapper.selectStoppedCategoryPart(firstCatId, secondCatId, thirdCatId, partName);
    }

    /**
     * 维护3J目录的isLeaf和vehicleCode属性
     * BY LYJ
     */
    private void maintainLeafVc(Integer catId) {
        CategoryDO parentCategory = categoryDOMapper.selectByPrimaryKey(catId);
        List<CategoryPartDO> childrenList = categoryPartDOMapper.selectPartByLevelCatId(CategoryDO.THIRD_LEVEL, catId);

        if (childrenList.size() == 0) {
            parentCategory.setIsLeaf(CategoryDO.IS_LEAF_TRUE);
            parentCategory.setVehicleCode(CategoryDO.VEHICLE_CODE_NAN);
        } else {
            if (categoryPartDOMapper.selectVehicleCodeCount(catId) > 1) {
                parentCategory.setVehicleCode(CategoryDO.VEHICLE_CODE_CH);
            }else{
                parentCategory.setVehicleCode(childrenList.get(0).getVehicleCode());
            }
        }
        categoryDOMapper.updateByPrimaryKeySelective(parentCategory);
    }
}


