package com.tqmall.monkey.dal.dao.sys;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.dal.dao.base.BaseDao;
import com.tqmall.monkey.dal.mapper.sys.SysRecordDOMapper;
import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.sys.SysRecordDO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * 存储用户操作记录
 * Created by zxg on 15/8/6.
 */
@MyBatisRepository
public class SysRecordDao extends BaseDao {
    private final static String NAMESPACE = "com.tqmall.monkey.dal.mapper.sys.SysRecordDOMapper";

    @Autowired
    private SysRecordDOMapper sysRecordDOMapper;

    public SysRecordDOMapper getSysRecordDOMapper() {
        return sysRecordDOMapper;
    }

    //searchmap
    public Page<SysRecordDO> getRecordPage(Integer index,Integer pageSize,HashMap<String,Object> map){
        String selectSql = NAMESPACE+".getRecordPage";
        String countSql = NAMESPACE+".getRecordPageCount";

        return this.queryPage(selectSql,countSql,map,index,pageSize);
    }
}
