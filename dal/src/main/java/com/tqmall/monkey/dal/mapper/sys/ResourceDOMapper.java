package com.tqmall.monkey.dal.mapper.sys;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.domain.entity.sys.ResourceDO;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface ResourceDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ResourceDO record);

    int insertSelective(ResourceDO record);

    ResourceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ResourceDO record);

    int updateByPrimaryKey(ResourceDO record);

    List<ResourceDO> getAllResource();

    List<ResourceDO> selectByParentId(Integer parentId);

    List<ResourceDO> selectResourceForRole(Map<String, Object> params);
}