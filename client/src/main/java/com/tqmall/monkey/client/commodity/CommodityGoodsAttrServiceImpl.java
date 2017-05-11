package com.tqmall.monkey.client.commodity;

import com.tqmall.monkey.client.shiro.MonkeyJdbcRealm;
import com.tqmall.monkey.component.utils.BigDataToMySql;
import com.tqmall.monkey.dal.dao.commodity.CommodityGoodsAttrDao;
import com.tqmall.monkey.domain.entity.UserDO;
import com.tqmall.monkey.domain.entity.commodity.CommodityGoodsAttrDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zxg on 15/8/22.
 */
@Slf4j
@Service
public class CommodityGoodsAttrServiceImpl implements CommodityGoodsAttrService {

    private static String headerSql = "LOAD DATA LOCAL INFILE 'sql.csv' IGNORE INTO TABLE modeldatas.TABLENAME";

    //获得当前登录用户信息
    @Autowired
    private MonkeyJdbcRealm monkeyJdbcRealm;
    //大数据导入
    @Autowired
    private BigDataToMySql bigDataToMySql;

    @Autowired
    private CommodityGoodsAttrDao commodityGoodsAttrDao;

    @Override
    public boolean insertAttr(CommodityGoodsAttrDO commodityGoodsAttrDO) {
        UserDO user = monkeyJdbcRealm.getCurrentUser();
        if(null == user){
            return false;
        }
        Integer userId = user.getId();
        commodityGoodsAttrDO.setCreator(userId);
        commodityGoodsAttrDO.setModifier(userId);

        //判断存不存在该属性
        CommodityGoodsAttrDO existDO = this.getByUuIdAttrId(commodityGoodsAttrDO.getGoodsUuId(), commodityGoodsAttrDO.getAttrId());
        if(null == existDO){
            commodityGoodsAttrDao.getCommodityGoodsAttrDOMapper().insertSelective(commodityGoodsAttrDO);
        }else{
            commodityGoodsAttrDO.setId(existDO.getId());
            commodityGoodsAttrDO.setIsdelete(0);
            commodityGoodsAttrDao.getCommodityGoodsAttrDOMapper().updateByPrimaryKeySelective(commodityGoodsAttrDO);
        }
        return true;
    }

    @Override
    public boolean updateAttr(CommodityGoodsAttrDO commodityGoodsAttrDO) {

        try {
            commodityGoodsAttrDao.getCommodityGoodsAttrDOMapper().updateByPrimaryKeySelective(commodityGoodsAttrDO);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }
    }

    @Override
    public boolean insertBatchAttr(List<CommodityGoodsAttrDO> list) {
        try{
            if(list.size() > 0) {
                commodityGoodsAttrDao.getCommodityGoodsAttrDOMapper().insertBatch(list);
            }
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean insertBatchAttrByLoadData(List<CommodityGoodsAttrDO> list) {
        try{
            if(list.size() > 0) {
                Integer userId = monkeyJdbcRealm.getCurrentUser().getId();

                long now = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date dt = new Date(now );
                String nowTime = sdf.format(dt);

                String relationSql = headerSql.replace("TABLENAME", "db_monkey_commodity_goods_attr") +
                        "( goods_uuId, attr_id, attr_name, attr_value, creator, modifier, gmt_modified,gmt_create,isdelete)";
                StringBuilder builder = new StringBuilder();
                for(CommodityGoodsAttrDO attrDO : list){

                    builder.append(attrDO.getGoodsUuId());
                    builder.append("\t");
                    builder.append(attrDO.getAttrId());
                    builder.append("\t");
                    builder.append(attrDO.getAttrName());
                    builder.append("\t");
                    builder.append(attrDO.getAttrValue());
                    builder.append("\t");
                    builder.append(userId);
                    builder.append("\t");
                    builder.append(userId);
                    builder.append("\t");
                    builder.append(nowTime);
                    builder.append("\t");
                    builder.append(nowTime);
                    builder.append("\t");
                    builder.append("0");
                    builder.append("\t");
                    builder.append("\n");
                }
                byte[] bytes = builder.toString().getBytes();
                InputStream relationInput = new ByteArrayInputStream(bytes);

                try {
                    bigDataToMySql.bulkLoadFromInputStream(relationSql,relationInput);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }catch (Exception e) {
            log.error(e.getMessage(),e);
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteBatchAttr(List<String> goodsUuIdList, Integer userId) {
        try{
            if(goodsUuIdList.size() > 0) {
                commodityGoodsAttrDao.deleteBatchGoodsUuId(goodsUuIdList, userId);
            }
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<CommodityGoodsAttrDO> getListByGoodsUuId(String goodsUuId) {
        return commodityGoodsAttrDao.getListByGoodsUuId(goodsUuId);
    }

    @Override
    public CommodityGoodsAttrDO getByUuIdAttrId(String uuId, Integer attrId) {
        CommodityGoodsAttrDO commodityGoodsAttrDO = commodityGoodsAttrDao.getCommodityGoodsAttrDOMapper().selectByAttrUuId(uuId,attrId);

        return commodityGoodsAttrDO;
    }

}
