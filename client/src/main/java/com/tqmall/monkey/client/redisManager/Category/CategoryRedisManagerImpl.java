package com.tqmall.monkey.client.redisManager.Category;

import com.tqmall.monkey.component.redis.RedisClientTemplate;
import com.tqmall.monkey.component.redis.RedisKeyBean;
import com.tqmall.monkey.dal.dao.category.CategoryDao;
import com.tqmall.monkey.dal.dao.category.CategoryPartDao;
import com.tqmall.monkey.domain.entity.category.CategoryDO;
import com.tqmall.monkey.domain.entity.category.CategoryPartDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jca.cci.core.InteractionCallback;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zxg on 15/9/28.
 * 分类的缓存manage类
 */
@Service
public class CategoryRedisManagerImpl implements CategoryRedisManager {
    private static final String CateByPrimaryIdKey = RedisKeyBean.getCateByPrimaryIdKey;
    private static final String CateByCateAttrKey = RedisKeyBean.CateAttrKey;
    private static final String PartLikeNameCode = RedisKeyBean.PartLikeNameCode;

    private static final String catXPartExportExcelVersionKey = RedisKeyBean.catXPartExportExcelVersionKey;

    @Autowired
    private RedisClientTemplate redisClientTemplate;

    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private CategoryPartDao categoryPartDao;

    //根据唯一主键获得分类对象
    public CategoryDO getCateByPrimaryId(Integer id) {
        String key = String.format(CateByPrimaryIdKey, id);
        CategoryDO categoryDO = redisClientTemplate.lazyGet(key, CategoryDO.class);

        if (null == categoryDO) {
            categoryDO = categoryDao.getCategoryDOMapper().selectByPrimaryKey(id);
            if (null != categoryDO) {
                redisClientTemplate.lazySet(key, categoryDO, RedisKeyBean.RREDIS_EXP_WEEK);
            }
        }

        return categoryDO;

    }

    /**
     * 根据code和level获得CategoryDO
     *
     * @param vehicleCode 可选，车辆种类码：C X
     * @param code        必填，自身编码
     * @param level       必填，分类级别
     * @param pid         可选，此分类的父id
     * @return
     */
    public List<CategoryDO> getListByVeCodeLevelPid(String vehicleCode, String code, Integer level, Integer pid) {
        String tempFile = level + code;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("level", level);
        params.put("code", code);
        if (vehicleCode != null) {
            tempFile += vehicleCode;
            params.put("vehicleCode", vehicleCode);
        }
        if (pid != null && pid > 0) {
            tempFile += pid;
            params.put("pid", pid);
        }

        String key = String.format(CateByCateAttrKey, tempFile);
        List<CategoryDO> list = redisClientTemplate.lazyGetList(key, CategoryDO.class);
        if (null == list || list.isEmpty()) {
            list = categoryDao.getCategoryDOMapper().getCategoryForCheck(params);
            redisClientTemplate.lazySet(key, list, RedisKeyBean.RREDIS_EXP_WEEK);
        }
        return list;

    }


    //更新数据
    public void updateCategoryDO(CategoryDO categoryDO) {
        Integer catId = categoryDO.getCatId();
        if (null == catId) {
            return;
        }
        Integer id = catId;

        categoryDao.getCategoryDOMapper().updateByPrimaryKeySelective(categoryDO);
        String key = String.format(CateByPrimaryIdKey, id);

        redisClientTemplate.lazySet(key, categoryDO, RedisKeyBean.RREDIS_EXP_HOURS);

    }

    //增加数据
    public Integer insertCategoryDO(CategoryDO categoryDO) {
        categoryDao.getCategoryDOMapper().insertSelective(categoryDO);

        Integer catId = categoryDO.getCatId();
        Integer id = catId;
        String key = String.format(CateByPrimaryIdKey, id);

        redisClientTemplate.lazySet(key, categoryDO, RedisKeyBean.RREDIS_EXP_HOURS);
        return catId;
    }

    //=====================part==========================

    @Override
    public List<CategoryPartDO> getPartListLikeName(String name) {
        name = name.trim();
        String key = String.format(PartLikeNameCode, name);

        List<CategoryPartDO> list = redisClientTemplate.lazyGetList(key, CategoryPartDO.class);
        if (null == list) {
            list = categoryPartDao.getPartListLikeName(name);
            redisClientTemplate.lazySet(key, list, RedisKeyBean.RREDIS_EXP_WEEK);
        }


        return list;
    }

    @Override
    public List<CategoryPartDO> getPartListLikeCode(String code) {
        code = code.trim();
        String key = String.format(PartLikeNameCode, code);

        List<CategoryPartDO> list = redisClientTemplate.lazyGetList(key, CategoryPartDO.class);
        if (null == list) {
            list = categoryPartDao.getPartListLikeCode(code);
            redisClientTemplate.lazySet(key, list, RedisKeyBean.RREDIS_EXP_WEEK);
        }


        return list;
    }

    @Override
    public String updateCatXPartExportExcelVersion() {
        //版本号初始从2开始
        String value = redisClientTemplate.get(catXPartExportExcelVersionKey);
        if (value == null) {
            value = "1";
        }
        Integer temp = Integer.parseInt(value);
        String version = String.valueOf(++temp);
        redisClientTemplate.set(catXPartExportExcelVersionKey, version);
        return version;
    }

    @Override
    public String selectCatXPartExportExcelVersion() {
        String version = redisClientTemplate.get(catXPartExportExcelVersionKey);
        if (version == null) {
            version = "2";//版本号初始从2开始
            redisClientTemplate.set(catXPartExportExcelVersionKey, version);
        }
        return version;
    }

    @Override
    public void deleteCatXPartExportExcelVersion() {
        redisClientTemplate.delKey(catXPartExportExcelVersionKey);
    }

}
