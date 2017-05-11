package com.tqmall.monkey.client.part;

import com.tqmall.monkey.dal.mapper.part.PartSubjoinDOMapper;
import com.tqmall.monkey.domain.entity.part.PartSubjoinDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zxg on 16/5/24.
 * 11:20
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Service
public class PartSubjoinServiceImpl implements PartSubjoinService {

    @Autowired
    private PartSubjoinDOMapper subjoinDOMapper;

    @Override
    public String getUuIdWithExist(PartSubjoinDO partSubjoinDO) {
        PartSubjoinDO result = subjoinDOMapper.selectByDO(partSubjoinDO);
        if(result != null){
            return result.getUuid();
        }
        return null;
    }
}
