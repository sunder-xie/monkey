package com.tqmall.monkey.dal.mapper.sys;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.domain.entity.sys.RoleDO;
import com.tqmall.monkey.domain.entity.sys.RoleResource;

import java.util.List;

@MyBatisRepository
public interface RoleDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RoleDO record);

    int insertSelective(RoleDO record);

    RoleDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoleDO record);

    int updateByPrimaryKey(RoleDO record);

    RoleDO getRoleByName(String roleName);

    RoleDO getRoleById(Integer roleId);

    List<RoleDO> getAllRoles();

    int insertRoleResourceBatch(List<RoleResource> dataList);

    int deleteRoleResource(RoleResource record);

    List<RoleDO> selectRoleForUser(Integer userId);
}