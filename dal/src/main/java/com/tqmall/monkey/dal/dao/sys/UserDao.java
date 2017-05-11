package com.tqmall.monkey.dal.dao.sys;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.dal.dao.base.BaseDao;
import com.tqmall.monkey.dal.mapper.sys.UserDOMapper;
import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.UserDO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by hzt on 2015/5/14.
 * 用户dao
 */

@MyBatisRepository
public class UserDao extends BaseDao {
    private final static String NAMESPACE = "com.tqmall.monkey.dal.mapper.sys.UserDOMapper";

    @Autowired
    private UserDOMapper userDOMapper;

    public UserDOMapper getUserDOMapper() {
        return userDOMapper;
    }

    /**
     *
     *
     */
    public Page<UserDO> getUsersPage( Map<String, Object> params, int pageIndex, int pageSize){
        String selectSql = NAMESPACE + ".getUsersPage";
        String countSql = NAMESPACE + ".getTotalCount";
        Page<UserDO> page = this.queryPage(selectSql, countSql, params, pageIndex, pageSize);

        return page;
    }
}
