package com.tqmall.monkey.client.commodity;

import com.tqmall.monkey.client.shiro.MonkeyJdbcRealm;
import com.tqmall.monkey.component.utils.BigDataToMySql;
import com.tqmall.monkey.dal.dao.commodity.CommodityGoodsOeDao;
import com.tqmall.monkey.domain.entity.commodity.CommodityGoodsOeDO;
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
@Service
@Slf4j
public class CommodityGoodsOeServiceImpl implements CommodityGoodsOeService {

    private static String headerSql = "LOAD DATA LOCAL INFILE 'sql.csv' IGNORE INTO TABLE modeldatas.TABLENAME";

    //获得当前登录用户信息
    @Autowired
    private MonkeyJdbcRealm monkeyJdbcRealm;
    //大数据导入
    @Autowired
    private BigDataToMySql bigDataToMySql;

    @Autowired
    private CommodityGoodsOeDao commodityGoodsOeDao;

    @Override
    public boolean insertOe(CommodityGoodsOeDO commodityGoodsOeDO) {
        try {
            //判断是否存在在库中
            CommodityGoodsOeDO exitOeDO = commodityGoodsOeDao.getCommodityGoodsOeDOMapper().selectByUuIdOe(commodityGoodsOeDO.getGoodsUuId(),commodityGoodsOeDO.getOeNumber());
            if(null == exitOeDO){
                commodityGoodsOeDao.getCommodityGoodsOeDOMapper().insertSelective(commodityGoodsOeDO);
            }else{
                exitOeDO.setIsdelete(0);
                exitOeDO.setModifier(commodityGoodsOeDO.getModifier());
                commodityGoodsOeDao.getCommodityGoodsOeDOMapper().updateByPrimaryKey(exitOeDO);
            }

            return true;
        } catch (Exception e) {
            log.error("insertOe wrong", e);
            return false;
        }
    }

    @Override
    public boolean insertBatchOe(List<CommodityGoodsOeDO> list) {
        try{
            if(list.size() > 0) {
                commodityGoodsOeDao.getCommodityGoodsOeDOMapper().insertBatch(list);
            }
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteOeByGoodsUuId(String goodsUuId, Integer userId) {
        try{
            commodityGoodsOeDao.getCommodityGoodsOeDOMapper().deleteGoodsUuId(goodsUuId,userId);

            return true;
        }catch (Exception e) {
            log.error("deleteAttr wrong", e);
            return false;
        }
    }

    @Override
    public boolean insertBatchOeByLoadData(List<CommodityGoodsOeDO> list) {
        try{
            if(list.size() > 0) {
                Integer userId = monkeyJdbcRealm.getCurrentUser().getId();

                long now = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date dt = new Date(now );
                String nowTime = sdf.format(dt);

                String relationSql = headerSql.replace("TABLENAME", "db_monkey_commodity_goods_oe") +
                        "( goods_uuId, oe_number, creator, modifier, gmt_modified, gmt_create,isdelete)";
                StringBuilder builder = new StringBuilder();
                for(CommodityGoodsOeDO oeDO : list){

                    builder.append(oeDO.getGoodsUuId());
                    builder.append("\t");
                    builder.append(oeDO.getOeNumber());
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
            return false;
        }
    }

    @Override
    public boolean deleteBatchOeByGoodsUuId(List<String> goodsUuIdList, Integer userId) {
        try{
            if(goodsUuIdList.size() > 0) {
                commodityGoodsOeDao.deleteBatchGoodsUuId(goodsUuIdList, userId);
            }
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updataBatchById(Integer isdetele, List<Integer> idsList, Integer updateUserId) {
        try{
            if(idsList.size() > 0 ) {
                commodityGoodsOeDao.updateBatchIds(isdetele, idsList, updateUserId);
            }
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<CommodityGoodsOeDO> getListByUuId(String goodsUuId) {
        return commodityGoodsOeDao.getListByUuId(goodsUuId);
    }

    @Override
    public CommodityGoodsOeDO getByUuIdOe(String uuId, String oe) {
        CommodityGoodsOeDO exitOeDO = commodityGoodsOeDao.getCommodityGoodsOeDOMapper().selectByUuIdOe(uuId,oe);
        return exitOeDO;
    }
}
