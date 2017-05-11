package com.tqmall.monkey.client.user;

import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.sys.SysRecordDO;

/**
 * Created by zxg on 15/8/6.
 */
public interface SysRecordService {

    //返回自增id
    Integer insertRecord(SysRecordDO sysRecordDO);

    Boolean updateRecord(SysRecordDO sysRecordDO);

    Page<SysRecordDO> getRecordPage(Integer index,Integer pageSize,Integer userId);
}
