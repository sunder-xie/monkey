package com.tqmall.monkey.dal.mapper.commodity;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.domain.entity.commodity.CommodityBrandDO;
import org.apache.ibatis.annotations.Param;


@MyBatisRepository
public interface CommodityBrandDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommodityBrandDO record);

    int insertSelective(CommodityBrandDO record);

    CommodityBrandDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CommodityBrandDO record);

    int updateByPrimaryKey(CommodityBrandDO record);

    //假性删除或恢复，数据仍旧保留在数据库
    int changeIsDelete(CommodityBrandDO record);

    // 根据三个主键，返回id
    Integer selectByNameCode(@Param("nameEn")String nameEn,@Param("nameCh")String nameCh,@Param("code")String code);

}