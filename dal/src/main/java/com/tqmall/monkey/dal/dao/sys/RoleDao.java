package com.tqmall.monkey.dal.dao.sys;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.dal.dao.base.BaseDao;
import com.tqmall.monkey.dal.mapper.sys.RoleDOMapper;
import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.sys.RoleDO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by ximeng on 2015/5/12.
 * 角色dao
 */

@MyBatisRepository
public class RoleDao extends BaseDao {
    private final static String NAMESPACE = "com.tqmall.monkey.dal.mapper.sys.RoleDOMapper";

    @Autowired
    private RoleDOMapper roleDOMapper;

    public RoleDOMapper getRoleDOMapper() {
        return roleDOMapper;
    }

    /**
     * 根据账号主键ID 返回角色列表
     * @param userId 用户ID
     * @return 角色列表
     */
    public List<RoleDO> selectRoleListByUserId(Integer userId){
        return sqlTemplate.selectList(NAMESPACE+".selectRoleListByUserId",userId);
    }

    public Page<RoleDO> getRolesPage( Map<String, Object> params, int pageIndex, int pageSize){
        String selectSql = NAMESPACE + ".getRolesPage";
        String countSql = NAMESPACE + ".getTotalCount";
        Page<RoleDO> page = this.queryPage(selectSql, countSql, params, pageIndex, pageSize);

        return page;
    }

}
