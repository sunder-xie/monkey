package com.tqmall.monkey.dal.dao.base;

import com.tqmall.monkey.domain.commonBean.Page;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by ximeng on 2015/4/14.
 */
public abstract class BaseDao {

    @Autowired
    protected SqlSessionTemplate sqlTemplate;

    /**
     * Page对象
     * @param selectSql 主查询SQL
     * countSql countSQL:"COUNT_SQL"代替，可用sql语句代替进去，查询总数
     * @param params 参数
     * @param index 第几页
     * @param pageSize 每页的记录数
     * @return
     */
    public <T> Page<T> queryPage(String selectSql,String countSql, Map<String, ?> params, int index, int pageSize) {
        if (index < 1) {
            index = 1;
        }
        int offset = (index - 1) * pageSize;

        List<T> list = this.sqlTemplate.selectList(selectSql, params, new RowBounds(offset, pageSize));

// TODO       Integer count = sqlTemplate.selectOne("COUNT_SQL", params);
        Integer count = 0;
        if(countSql != null){
            count = sqlTemplate.selectOne(countSql, params);
        }

        return new Page<T>(count, index, pageSize, list);
    }

    public <E> List<E> selectList(String selectSql){
        return this.sqlTemplate.selectList(selectSql);
    }

    public <T> T selectOne(String selectSql){
        return sqlTemplate.selectOne(selectSql);
    }

}