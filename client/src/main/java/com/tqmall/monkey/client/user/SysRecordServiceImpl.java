package com.tqmall.monkey.client.user;

import com.tqmall.monkey.dal.dao.sys.SysRecordDao;
import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.sys.SysRecordDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Created by zxg on 15/8/6.
 */
@Service
public class SysRecordServiceImpl implements SysRecordService {

    @Autowired
    private SysRecordDao sysRecordDao;

    @Override
    public Integer insertRecord(SysRecordDO sysRecordDO) {

        try{
            sysRecordDao.getSysRecordDOMapper().insertSelective(sysRecordDO);
            return sysRecordDO.getId();
        }catch (Exception e){

            return 0;
        }
    }

    @Override
    public Boolean updateRecord(SysRecordDO sysRecordDO) {
        try{
            sysRecordDao.getSysRecordDOMapper().updateByPrimaryKeySelective(sysRecordDO);
            return true;
        }catch (Exception e){

            return false;
        }
    }

    @Override
    public Page<SysRecordDO> getRecordPage(Integer index, Integer pageSize,Integer userId) {
        HashMap<String,Object> map = new HashMap<>();
        map.put("userId",userId);
        return sysRecordDao.getRecordPage(index,pageSize,map);
    }
}
