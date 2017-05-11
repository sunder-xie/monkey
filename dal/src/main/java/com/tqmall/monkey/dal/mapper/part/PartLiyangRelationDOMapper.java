package com.tqmall.monkey.dal.mapper.part;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.domain.entity.part.PartGoodsPicLiDO;
import com.tqmall.monkey.domain.entity.part.PartLiyangRelationDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@MyBatisRepository
public interface PartLiyangRelationDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(PartLiyangRelationDO record);

    PartLiyangRelationDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PartLiyangRelationDO record);

    //根据part_liyang 的主键id 在relation中查询
    List<PartLiyangRelationDO> selectByPartLId(@Param(value = "partLId")Integer partLId,@Param(value = "liyangTable") String theLiyangTable);

    //根据partLid和table 删除数据
    int deleteByPartLid(@Param(value = "partLId")Integer partLId,@Param(value = "liyangTable") String theLiyangTable);
}