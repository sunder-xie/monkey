package com.tqmall.monkey.dal.dao.category;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.dal.dao.base.BaseDao;
import com.tqmall.monkey.dal.mapper.category.CategoryPartDOMapper;
import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.category.CategoryPartDO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangzhangting on 15/6/24.
 */

@MyBatisRepository
public class CategoryPartDao extends BaseDao{
    private final static String NAMESPACE = "com.tqmall.monkey.dal.mapper.category.CategoryPartDOMapper";

    @Autowired
    private CategoryPartDOMapper partDOMapper;

    public CategoryPartDOMapper getPartDOMapper() {
        return partDOMapper;
    }


    public List<CategoryPartDO> getPartListLikeName(String name){
        String selectSql = NAMESPACE + ".getPartListLikeNameCode";
        Map<String,String> map = new HashMap<>();
        map.put("name","%"+name+"%");

        return sqlTemplate.selectList(selectSql,map);
    }

    public List<CategoryPartDO> getPartListLikeCode(String code){
        String selectSql = NAMESPACE + ".getPartListLikeNameCode";
        Map<String,String> map = new HashMap<>();
        map.put("code","%"+code+"%");

        return sqlTemplate.selectList(selectSql,map);
    }
}
