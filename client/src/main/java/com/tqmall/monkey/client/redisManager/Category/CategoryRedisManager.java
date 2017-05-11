package com.tqmall.monkey.client.redisManager.Category;

import com.alibaba.fastjson.JSON;
import com.tqmall.monkey.component.redis.RedisClientTemplate;
import com.tqmall.monkey.component.redis.RedisKeyBean;
import com.tqmall.monkey.dal.dao.category.CategoryDao;
import com.tqmall.monkey.domain.entity.category.CategoryDO;
import com.tqmall.monkey.domain.entity.category.CategoryPartDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zxg on 15/9/28.
 * 分类的缓存manage类
 */
@Service
public interface CategoryRedisManager {

    //根据唯一主键获得分类对象
    CategoryDO getCateByPrimaryId(Integer id);

    //更新数据
    void updateCategoryDO(CategoryDO categoryDO);

    //增加数据
    Integer insertCategoryDO(CategoryDO categoryDO);

    /**
     * 根据code和level获得CategoryDO
     * @param vehicleCode 可选，车辆种类码：C X
     * @param code 必填，自身编码
     * @param level 必填，分类级别
     * @param pid   可选，此分类的父id
     * @return
     */
    List<CategoryDO> getListByVeCodeLevelPid(String vehicleCode, String code, Integer level, Integer pid);


    //========================part-标准零件======================

    List<CategoryPartDO> getPartListLikeName(String name);

    List<CategoryPartDO> getPartListLikeCode(String code);


    //========================导出到excel的版本号的相关操作======================
    String updateCatXPartExportExcelVersion();

    String selectCatXPartExportExcelVersion();

    void deleteCatXPartExportExcelVersion();
}
