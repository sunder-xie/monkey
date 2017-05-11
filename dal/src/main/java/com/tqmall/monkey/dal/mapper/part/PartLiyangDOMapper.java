package com.tqmall.monkey.dal.mapper.part;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.domain.entity.part.PartLiyangDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisRepository
public interface PartLiyangDOMapper {
    int deleteByPrimaryKey(@Param(value = "id")Integer id,@Param(value = "modifier") Integer updateUserId);

    int insertSelective(PartLiyangDO record);

    PartLiyangDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PartLiyangDO record);

    /*========BY 中西 0524=====*/
    List<PartLiyangDO> getAllParLiyang();

    List<PartLiyangDO> selectByDO(PartLiyangDO record);

    //取消删除状态
    int setIsNotDeletedPrimaryKey(@Param(value = "id")Integer id,@Param(value = "modifier") Integer updateUserId);

}