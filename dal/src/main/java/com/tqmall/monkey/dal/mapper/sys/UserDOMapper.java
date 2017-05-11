package com.tqmall.monkey.dal.mapper.sys;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.domain.entity.UserDO;
import com.tqmall.monkey.domain.entity.sys.UserRole;

import java.util.List;

@MyBatisRepository
public interface UserDOMapper {
    //查询用户是否合法
    boolean checkLoginUser(UserDO userDO);

    //新增登陆用户
    int insertLoginUser(UserDO userDO);

    //根据用户名称查询用户ID
    int selectLoginUserIdByUserName(String userName);

    //根据用户登陆ID查询nickName
    UserDO selectUserDO(UserDO userDO);

    int deleteUserById(Integer userId);

    UserDO selectUserById(Integer userId);

    int updateUser(UserDO user);

    List<UserDO> getAllUsers();

    int insertUserRoleBatch(List<UserRole> dataList);

    int deleteUserRole(UserRole userRole);
}