package com.tqmall.monkey.client.user;

import com.tqmall.monkey.dal.dao.sys.ResourceDao;
import com.tqmall.monkey.domain.entity.sys.ResourceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by huangzhangting on 15/7/2.
 */
@Transactional
@Service
public class ResourceServiceImpl implements ResourceService {
    @Autowired
    private ResourceDao resourceDao;

    @Override
    public List<ResourceDO> findAllResource() {
        return resourceDao.getResourceDOMapper().getAllResource();
    }

    @Override
    public List<ResourceDO> findByParentId(Integer parentId) {
        return resourceDao.getResourceDOMapper().selectByParentId(parentId);
    }

    @Override
    public void saveResource(ResourceDO record) {
        record.setAvailable(0);
        record.setParentIds(record.getParentId());
        resourceDao.getResourceDOMapper().insertSelective(record);
    }

    @Override
    public void modifyResource(ResourceDO record) {
        record.setParentIds(record.getParentId());
        resourceDao.getResourceDOMapper().updateByPrimaryKeySelective(record);
    }

    @Override
    public ResourceDO findResourceById(Integer id) {
        return resourceDao.getResourceDOMapper().selectByPrimaryKey(id);
    }

    @Override
    public List<ResourceDO> findResourceForRole(Integer roleId) {
        Map<String, Object> params = new HashMap<>();
        params.put("roleId", roleId);

        List<ResourceDO> list = resourceDao.getResourceDOMapper().selectResourceForRole(params);
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
