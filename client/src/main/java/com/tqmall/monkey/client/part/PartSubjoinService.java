package com.tqmall.monkey.client.part;

import com.tqmall.monkey.domain.entity.part.PartSubjoinDO;

/**
 * Created by zxg on 16/5/24.
 * 11:19
 * no bug,以后改代码的哥们，祝你好运~！！
 */
public interface PartSubjoinService {



    //是否存在该对象，存在则返回uuid
    String getUuIdWithExist(PartSubjoinDO partSubjoinDO);
}
