package com.tqmall.monkey.dal.mapper.category;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.domain.entity.category.CategoryDO;
import com.tqmall.monkey.domain.entity.category.CategoryPartDO;
import com.tqmall.monkey.domain.model.CategoryPartParams;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisRepository
public interface CategoryPartDOMapper {

    int insertSelective(CategoryPartDO record);

    CategoryPartDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CategoryPartDO record);

    /*=====================业务代码=============================*/
    List<CategoryPartDO> getCategoryListByParam(CategoryPartParams categoryPartParams);

    CategoryPartDO getCategoryPartBySumCode(String sumCode);

    int selectVehicleCodeCount(@Param("thirdCatId") Integer thirdCatId);

    int selectBrotherCountById(@Param("partId") Integer partId);

    void updateParentIsLeaf(@Param("partId") Integer partId, @Param("isLeaf") Integer isLeaf);

    void updateParentVehicleCode(@Param("partId") Integer partId, @Param("vehicleCode") String vehicleCode);

    String selectParentVehicleCode(@Param("partId") Integer partId);

    void updateIsDeletedByLevelCatId(@Param("level") Integer level, @Param("catId") Integer catId, @Param("isDeleted") String isDeleted);

    void updateLevelCatIdSumCode(@Param("level") Integer catLevel, @Param("catId") Integer catId, @Param("catName") String catName,
                                 @Param("catCode") String catCode, @Param("parentId") Integer parentId, @Param("parentName") String parentName,
                                 @Param("parentCode") String parentCode);

    //////////////////////////////////

    /**
     * 根据传入的层级和ID，获取所有的part
     *
     * @param level
     * @param catId
     * @return
     */
    List<CategoryPartDO> selectPartByLevelCatId(@Param("level") Integer level, @Param("catId") Integer catId);

    List<CategoryDO> selectStoppedCategoryPart(@Param("firstCatId") Integer firstCatId, @Param("secondCatId") Integer secondCatId, @Param("thirdCatId") Integer thirdCatId, @Param("partName") String partName);

}