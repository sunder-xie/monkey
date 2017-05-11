package com.tqmall.monkey.client.part;

import com.tqmall.monkey.domain.entity.part.PartLiyangDO;
import com.tqmall.monkey.domain.entity.part.PartLiyangRelationDO;

import java.util.List;

/**
 * Created by zxg on 16/5/24.
 * 10:24
 * no bug,以后改代码的哥们，祝你好运~！！
 * 配件库已有的力洋冗余数据
 */

public interface PartLiyangService {

    List<PartLiyangDO> getPartCar();

    Integer getPartLiyang(String brand,String factory,String series,String model);

    /**
     * 保存力洋冗余数据，缓存起来
     * @param brand 品牌
     * @param factory 厂商
     * @param series 车系
     * @param model 车型
     * @return null：失败
     * 值：存入数据库的自增id值
     */
    Integer savePartLiyang(String brand,String factory,String series,String model);

    //删除力洋车型，删除缓存
    Boolean deletePartLiyang(Integer id,String brand,String factory,String series,String model);

}
