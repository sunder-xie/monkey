package com.tqmall.monkey.client.user;

import com.tqmall.monkey.domain.entity.sys.ResourceDO;

import java.util.List;

/**
 * Created by ximeng on 2015/5/12.
 * 资源service
 */
public interface ResourceService {
    List<ResourceDO> findAllResource();

    List<ResourceDO> findByParentId(Integer parentId);

    void saveResource(ResourceDO record);

    void modifyResource(ResourceDO record);

    ResourceDO findResourceById(Integer id);

    List<ResourceDO> findResourceForRole(Integer roleId);
}
