package com.tqmall.monkey.client.user;

import com.tqmall.monkey.component.utils.Sha1Util;
import com.tqmall.monkey.dal.dao.sys.ResourceDao;
import com.tqmall.monkey.dal.dao.sys.RoleDao;
import com.tqmall.monkey.dal.dao.sys.UserDao;
import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.UserDO;
import com.tqmall.monkey.domain.entity.sys.ResourceDO;
import com.tqmall.monkey.domain.entity.sys.RoleDO;
import com.tqmall.monkey.domain.entity.sys.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 全车数据工具用户相关操作实现类
 * Created by bairui on 14/12/18.
 */
@Transactional
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao ;

    @Autowired
    private ResourceDao resourceDao;

    @Autowired
    private RoleDao roleDao;


    //根据用户名和密码查询用户是否可登陆
    public boolean checkExistUser(UserDO userDO){
        userDO.setPassWord(Sha1Util.getSha1(userDO.getPassWord()));
        return userDao.getUserDOMapper().checkLoginUser(userDO);
    }

    @Override
    public int addLoginUser(UserDO userDO) {
        String password = userDO.getPassWord();
        password = Sha1Util.getSha1(password);
        userDO.setPassWord(password);
        return userDao.getUserDOMapper().insertLoginUser(userDO);
    }

    @Override
    public int queryLoginUserId(String userName) {
       int userId = userDao.getUserDOMapper().selectLoginUserIdByUserName(userName);
        return userId;
    }

    @Override
    public UserDO getUserDO(String userLoginId) {
        UserDO userDO = new UserDO();
        userDO.setUserName(userLoginId);
        userDO = userDao.getUserDOMapper().selectUserDO(userDO);
        return userDO;
    }

    @Override
    public Set<String> findRoles(String username) {
        int userId = this.queryLoginUserId(username);
        List<RoleDO> roleDOList = roleDao.selectRoleListByUserId(userId);

        Set<String> rolesSet = new HashSet<>();
        for(RoleDO roleDO :roleDOList){
            String roleName = roleDO.getRoleName();
            if( null != roleName) {
                rolesSet.add(roleName);
            }
        }
        return rolesSet;
    }

    @Override
    public Set<String> findPermissions(String username) {
        int userId = this.queryLoginUserId(username);
        List<RoleDO> roleDOList = roleDao.selectRoleListByUserId(userId);
        Set<String> permissionsSet = new HashSet<>();
        for(RoleDO roleDO :roleDOList){
            Integer roleId = roleDO.getId();
            List<ResourceDO> resourceDOList = resourceDao.selectResourceListByRoleId(roleId,null,null,null);
            for(ResourceDO resourceDO : resourceDOList) {
                String permissions = resourceDO.getPermission();
//                if(null != permissions || ! "".equals(permissions)) {
//                    permissionsSet.add(permissions);
//                }
                if(!StringUtils.isEmpty(permissions)) {
                    permissionsSet.add(permissions);
                }
            }
        }
        return permissionsSet;
    }

    @Override
    public List<HashMap<String,Object>> getLeftLi(Integer userId) {

        List<Integer> roleIdList = new ArrayList<>();
        //在库里查出所有的一级目录
        List<RoleDO> roleDOList = roleDao.selectRoleListByUserId(userId);
        for(RoleDO roleDO :roleDOList){
            roleIdList.add(roleDO.getId());
        }
        List<ResourceDO> resourceDOList = resourceDao.selectResourceListByRoleId(null,ResourceDO.TYPE_MENU,0,roleIdList);

        //遍历一级目录，找到它的二级目录
        List<HashMap<String,Object>> resultList = new ArrayList<>();
        for(ResourceDO resourceDO : resourceDOList){
            HashMap<String,Object> map = new HashMap<>();
            Integer parentId = resourceDO.getId();

            List<ResourceDO> secondResourceDOList = resourceDao.selectResourceListByRoleId(null, ResourceDO.TYPE_MENU, parentId,roleIdList);

            map.put("firstName",resourceDO.getResourceName());
            map.put("list",secondResourceDOList);

            resultList.add(map);
        }

        return resultList;
    }


    @Override
    public Page<UserDO> findUserPage(String userName, String cnName, Integer pageIndex, Integer pageSize) {
        if(pageIndex==null || pageIndex<1){
            pageIndex = 1;
        }
        if(pageSize==null || pageSize<1){
            pageSize = 10;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        if(userName!=null && !"".equals(userName)){
            params.put("user_name", userName.replaceAll(" +", ""));
        }
        if(cnName!=null && !"".equals(cnName)){
            params.put("cn_name", cnName.replaceAll(" +", ""));
        }

        return this.userDao.getUsersPage(params, pageIndex, pageSize);
    }

    @Override
    public boolean removeUserById(Integer userId) {
        int flag = this.userDao.getUserDOMapper().deleteUserById(userId);
        if(flag>0){
            return true;
        }
        return false;
    }

    @Override
    public UserDO findUserById(Integer userId) {
        return this.userDao.getUserDOMapper().selectUserById(userId);
    }

    @Override
    public boolean modifyUser(UserDO user) {
        UserDO oldUser = this.findUserById(user.getId());
        String password = user.getPassWord();
        if(oldUser.getPassWord()==null || !oldUser.getPassWord().equals(password)) {
            password = Sha1Util.getSha1(password);
            user.setPassWord(password);
        }

        int flag = this.userDao.getUserDOMapper().updateUser(user);
        if(flag>0){
            return true;
        }
        return false;
    }

    @Override
    public List<UserDO> findAllUsers() {

        return userDao.getUserDOMapper().getAllUsers();
    }

    @Override
    public void modifyUserRole(List<UserRole> addList, List<UserRole> deleteList) {
        if(addList!=null){
            userDao.getUserDOMapper().insertUserRoleBatch(addList);
        }
        if(deleteList!=null){
            for(UserRole userRole : deleteList){
                userDao.getUserDOMapper().deleteUserRole(userRole);
            }
        }
    }
}
