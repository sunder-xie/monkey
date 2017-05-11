package com.tqmall.monkey.client.user;

import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.sys.RoleDO;
import com.tqmall.monkey.domain.entity.sys.RoleResource;

import java.util.List;

/**
 * Created by ximeng on 2015/5/12.
 */
public interface RoleService {

    public Page<RoleDO> findRolesPage(String roleName, Integer pageIndex, Integer pageSize);

    public boolean addRole(RoleDO role);

    public boolean removeRoleById(Integer roleId);

    public boolean modifyRole(RoleDO role);

    public RoleDO findRoleById(Integer roleId);

    public RoleDO findRoleByName(String roleName);

    List<RoleDO> findAllRoles();

    void modifyRoleResource(List<RoleResource> addList, List<RoleResource> deleteList);

    List<RoleDO> findRoleForUser(Integer userId);
}
