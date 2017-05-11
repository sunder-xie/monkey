package com.tqmall.monkey.dal.dao.part;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.dal.dao.base.BaseDao;
import com.tqmall.monkey.dal.mapper.part.PartPictureDOMapper;
import com.tqmall.monkey.domain.entity.part.PartPictureDO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zxg on 15/7/25.
 */
@MyBatisRepository
public class PartPictureDao extends BaseDao {

    private final static String NAMESPACE = "com.tqmall.monkey.dal.mapper.part.PartPictureDOMapper";

    @Autowired
    private PartPictureDOMapper partPictureDOMapper;

    public PartPictureDOMapper getPartPictureDOMapper() {
        return partPictureDOMapper;
    }


    /**
     * 是否存在该partPicture，存在取出其id列表
     * @param partPictureDO
     * @return
     */
    public List<PartPictureDO> exitPartPicture(PartPictureDO partPictureDO) {
        return sqlTemplate.selectList(NAMESPACE+".exitPartPicture",partPictureDO);
    }


    //批量删除
    public Integer deleteBatchGoodsByUuId(List<String> uuList){
        String selectSql = NAMESPACE + ".deleteBatchGoodsByUuId";
        HashMap<String,Object> map = new HashMap<>();
        map.put("list",uuList);

        return sqlTemplate.selectOne(selectSql, map);
    }
}
