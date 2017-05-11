package com.tqmall.monkey.dal.mapper.category;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.domain.entity.category.CategoryDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface CategoryDOMapper {

    int insertSelective(CategoryDO record);

    CategoryDO selectByPrimaryKey(Integer catId);

    int updateByPrimaryKeySelective(CategoryDO record);

    List<CategoryDO> getCategoryListByPid(@Param(value = "pid") Integer pid);

    List<CategoryDO> getCategoryForCheck(Map<String, Object> params);

    List<CategoryDO> getDeletedCategory();

    int selectVehicleCodeCount(@Param("catId") Integer catId);

    void setYesDeleteFlagInCascade(@Param("catLevel") Integer level, @Param("catId") Integer catId);

    void updateIsLeaf(@Param("catId") Integer catId, @Param("isLeaf") Integer isLeaf);

    void updateVehicleCode(@Param("catId") Integer catId, @Param("vehicleCode") String vehicleCode);

    String selectVehicleCode(@Param("catId") Integer catId);

    ///////////////////////////////////////////////////////////

    /**
     * 根据catId主键获取他的父节点（如果没有父节点，则返回null）
     *
     * @param catId
     * @return
     */
    CategoryDO selectParentById(@Param("catId") Integer catId);

    /**
     * 根据id主键获取所有的子节点
     *
     * @param catId
     * @return
     */
    List<CategoryDO> selectChildren(@Param("catId") Integer catId);

    /**
     * 获取已经停用的cat
     *
     * @param level
     * @param parentId
     * @return
     */
    List<CategoryDO> selectStoppedCategory(@Param("level") Integer level, @Param("parentId") Integer parentId);

    /**
     * 根据id获取已删除的分类
     *
     * @param catId
     * @return
     */
    CategoryDO selectDeletedByPrimaryKey(@Param("catId") short catId);
}