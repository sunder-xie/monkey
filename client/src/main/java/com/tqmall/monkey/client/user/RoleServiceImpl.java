package com.tqmall.monkey.client.user;

import com.tqmall.monkey.dal.dao.sys.RoleDao;
import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.sys.RoleDO;
import com.tqmall.monkey.domain.entity.sys.RoleResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by huangzhangting on 15/5/22.
 */
@Transactional
@Service
public class RoleServiceImpl implements RoleService{

    @Autowired
    private RoleDao roleDao;

    @Override
    public Page<RoleDO> findRolesPage(String roleName, Integer pageIndex, Integer pageSize) {
        if(pageIndex==null || pageIndex<1){
            pageIndex = 1;
        }
        if(pageSize==null || pageSize<1){
            pageSize = 10;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        if(roleName!=null && !"".equals(roleName)){
            params.put("roleName",roleName);
        }

        return roleDao.getRolesPage(params, pageIndex, pageSize);
    }

    @Override
    public RoleDO findRoleByName(String roleName) {
        return roleDao.getRoleDOMapper().getRoleByName(roleName);
    }

    @Override
    public RoleDO findRoleById(Integer roleId) {
        return roleDao.getRoleDOMapper().getRoleById(roleId);
    }

    @Override
    public boolean removeRoleById(Integer roleId) {
        RoleDO role = new RoleDO();
        role.setId(roleId);
        role.setAvailable(1);
        int result = this.roleDao.getRoleDOMapper().updateByPrimaryKeySelective(role);
        return result>0;
    }

    @Override
    public boolean addRole(RoleDO role) {
        int result = this.roleDao.getRoleDOMapper().insertSelective(role);
        return result>0;
    }

    @Override
    public boolean modifyRole(RoleDO role) {
        int result = this.roleDao.getRoleDOMapper().updateByPrimaryKeySelective(role);
        return result>0;
    }

    @Override
    public List<RoleDO> findAllRoles() {
        return roleDao.getRoleDOMapper().getAllRoles();
    }

    @Override
    public void modifyRoleResource(List<RoleResource> addList, List<RoleResource> deleteList) {
        if(addList!=null){
            this.roleDao.getRoleDOMapper().insertRoleResourceBatch(addList);
        }
        if(deleteList!=null){
            for(RoleResource rr : deleteList){
                this.roleDao.getRoleDOMapper().deleteRoleResource(rr);
            }
        }
    }

    @Override
    public List<RoleDO> findRoleForUser(Integer userId) {
        List<RoleDO> list = roleDao.getRoleDOMapper().selectRoleForUser(userId);
        if(list.isEmpty()){
            return list;
        }
        List<RoleDO> resultList = new ArrayList<>();
        Set<Integer> idSet = new HashSet<>();
        for(RoleDO roleDO : list){
            if(idSet.add(roleDO.getId())){
                resultList.add(roleDO);
            }
        }
        Collections.sort(resultList, new Comparator<RoleDO>() {
            @Override
            public int compare(RoleDO o1, RoleDO o2) {
                return o1.getRoleName().compareTo(o2.getRoleName());
            }
        });

        return resultList;
    }
}
