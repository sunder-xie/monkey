package com.tqmall.monkey.dal.dao.sys;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.dal.dao.base.BaseDao;
import com.tqmall.monkey.dal.mapper.sys.ResourceDOMapper;
import com.tqmall.monkey.domain.entity.sys.ResourceDO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by ximeng on 2015/5/12.
 * 资源dao
 */

@MyBatisRepository
public class ResourceDao extends BaseDao {
    private final static String NAMESPACE = "com.tqmall.monkey.dal.mapper.sys.ResourceDOMapper";

    @Autowired
    private ResourceDOMapper resourceDOMapper;

    public ResourceDOMapper getResourceDOMapper() {
        return resourceDOMapper;
    }

    /**
     * 根据角色ID 返回资源列表
     * @param roleId 角色ID
     * @param type 资源类型（可为null）
     * @param relationParentId 关联表的父Id
     * @return 资源列表
     */
    public List<ResourceDO> selectResourceListByRoleId(Integer roleId,Integer type,Integer relationParentId,List<Integer> roleList){
        HashMap<String,Object> params = new HashMap<>();
        params.put("roleId",roleId);
        params.put("type",type);
        params.put("relationParentId",relationParentId);
        params.put("roleList",roleList);

        List<ResourceDO> list = sqlTemplate.selectList(NAMESPACE+".selectResourceListByRoleId",params);
        if(list.isEmpty()){
            return list;
        }
        List<ResourceDO> resultList = new ArrayList<>();
        Set<Integer> idSet = new HashSet<>();
        for(ResourceDO resourceDO : list){
            if(idSet.add(resourceDO.getId())){
                resultList.add(resourceDO);
            }
        }
        Collections.sort(resultList, new Comparator<ResourceDO>() {
            @Override
            public int compare(ResourceDO o1, ResourceDO o2) {
                return o1.getPriority().compareTo(o2.getPriority());
            }
        });

        return resultList;
    }
}
