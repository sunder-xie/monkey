package com.tqmall.monkey.client.user;


import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.UserDO;
import com.tqmall.monkey.domain.entity.sys.UserRole;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * success!
 * Created by bairui on 14/12/19.
 */
public interface UserService {

    //根据用户名密码查询是否合法用户
    boolean checkExistUser(UserDO userDO);

    //增加登陆用户
    int addLoginUser(UserDO userDO);

    //根据用户名查询用户ID,该ID用于插入车型保养记录时的creator字段
    int queryLoginUserId(String userName);

    //根据用户ID查询用户
    UserDO getUserDO(String userLoginId);

    /*====================================角色权限============================================================*/
    // 根据用户名查找其角色
    public Set<String> findRoles(String username);
    // 根据用户名查找其权限
    public Set<String> findPermissions(String username);
    //根据用户名查找其左侧菜单
    public List<HashMap<String,Object>> getLeftLi(Integer userId);

    public Page<UserDO> findUserPage(String userName, String cnName, Integer pageIndex, Integer pageSize);

    public boolean removeUserById(Integer userId);

    public UserDO findUserById(Integer userId);

    public boolean modifyUser(UserDO user);

    List<UserDO> findAllUsers();

    void modifyUserRole(List<UserRole> addList, List<UserRole> deleteList);
}
