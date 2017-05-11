package com.tqmall.monkey.dal.mapper.sys;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.domain.entity.sys.SysRecordDO;

//记录的存储
@MyBatisRepository
public interface SysRecordDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRecordDO record);

    int insertSelective(SysRecordDO record);

    SysRecordDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRecordDO record);

    int updateByPrimaryKey(SysRecordDO record);
}