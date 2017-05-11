package com.tqmall.monkey.client.commodity;

import com.tqmall.monkey.client.shiro.MonkeyJdbcRealm;
import com.tqmall.monkey.component.utils.BigDataToMySql;
import com.tqmall.monkey.dal.dao.commodity.CommodityGoodsCarDao;
import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.commodity.CommodityGoodsCarDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zxg on 15/8/22.
 */
@Service
public class CommodityGoodsCarServiceImpl implements CommodityGoodsCarService {

    private static String headerSql = "LOAD DATA LOCAL INFILE 'sql.csv' IGNORE INTO TABLE modeldatas.TABLENAME";

    //获得当前登录用户信息
    @Autowired
    private MonkeyJdbcRealm monkeyJdbcRealm;
    //大数据导入
    @Autowired
    private BigDataToMySql bigDataToMySql;

    @Autowired
    private CommodityGoodsCarDao commodityGoodsCarDao;

    @Override
    public boolean insertGoodsCar(CommodityGoodsCarDO commodityGoodsCarDO) {
        // 判断是否存在
        CommodityGoodsCarDO exist = this.selectByUuIdLiyangWithoutDelete(commodityGoodsCarDO.getGoodsUuId(), commodityGoodsCarDO.getLiyangId());
        if(null == exist){
            commodityGoodsCarDao.getCommodityGoodsCarDOMapper().insertSelective(commodityGoodsCarDO);
        }else if(exist.getIsdelete().equals(1)){
            exist.setIsdelete(0);
            commodityGoodsCarDao.getCommodityGoodsCarDOMapper().updateByPrimaryKeySelective(exist);
        }
        return true;
    }

    @Override
    public boolean insertBatchCar(List<CommodityGoodsCarDO> list) {
        try{
            if(list.size() > 0) {
                commodityGoodsCarDao.getCommodityGoodsCarDOMapper().insertBatch(list);
            }
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    @Override
    public void updateGoodsCar(CommodityGoodsCarDO commodityGoodsCarDO) {
        Integer id = commodityGoodsCarDO.getId();
        if(id != null && id != 0) {
            if(!"".equals(commodityGoodsCarDO.getGoodsUuId()) && !"".equals(commodityGoodsCarDO.getLiyangId())) {
                commodityGoodsCarDao.getCommodityGoodsCarDOMapper().updateByPrimaryKeySelective(commodityGoodsCarDO);
            }
        }
    }

    @Override
    public boolean insertBatchCarByLoadData(List<CommodityGoodsCarDO> list) {
        try{
            if(list.size() > 0) {
                Integer userId = monkeyJdbcRealm.getCurrentUser().getId();

                long now = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date dt = new Date(now );
                String nowTime = sdf.format(dt);

                String relationSql = headerSql.replace("TABLENAME", "db_monkey_commodity_goods_car") +
                        "( goods_uuId, liyang_Id, creator, modifier, gmt_modified, gmt_create,isdelete)";
                StringBuilder builder = new StringBuilder();
                for(CommodityGoodsCarDO carDO : list){

                    builder.append(carDO.getGoodsUuId());
                    builder.append("\t");
                    builder.append(carDO.getLiyangId());
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
    public boolean deleteBatchAttr(List<String> goodsUuIdList, Integer userId) {
        try{
            if(goodsUuIdList.size() > 0 ) {
                commodityGoodsCarDao.deleteBatchGoodsUuId(goodsUuIdList, userId);
            }
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updataStatusBatch(Integer status, List<String> goodsUuIdList, Integer updateUserId) {
        try{
            if(goodsUuIdList.size() > 0 ) {
                commodityGoodsCarDao.updateStatusBatchGoodsUuId(status, goodsUuIdList, updateUserId);
            }
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updataStatusBatchByLiyang(Integer status, List<String> liyangList, Integer updateUserId) {
        try{
            if(liyangList.size() > 0 ) {
                commodityGoodsCarDao.updateStatusBatchLiyang(status, liyangList, updateUserId);
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
                commodityGoodsCarDao.updateBatchIds(isdetele, idsList, updateUserId);
            }
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    @Override
    public Page<CommodityGoodsCarDO> getPageByGoodsUuId(String uuId,String searchLiyang, Integer index, Integer pageSize) {
        return commodityGoodsCarDao.getPageByGoodsUuId(uuId,searchLiyang, index, pageSize);
    }

    @Override
    public List<CommodityGoodsCarDO> getListByGoodsUuId(String uuId) {
        return commodityGoodsCarDao.getListByGoodsUuIdStatusWithDelete(uuId, null,0);
    }

    @Override
    public List<CommodityGoodsCarDO> getListByGoodsUuIdWithOutDelete(String uuId) {
        return commodityGoodsCarDao.getListByGoodsUuIdStatusWithDelete(uuId, null,null);
    }

    @Override
    public List<CommodityGoodsCarDO> getListByGoodsUuIdStatus(String uuId, Integer status) {
        return commodityGoodsCarDao.getListByGoodsUuIdStatusWithDelete(uuId, status, 0);

    }

    @Override
    public List<String> getLiYangListGroupBy(List<String> uuIdList,Integer status) {
        List<String> list = new ArrayList<>();
        if(uuIdList.size() > 0){
            list = commodityGoodsCarDao.getLiYangListGroupBy(uuIdList,status);
        }
        return list;
    }

    @Override
    public List<CommodityGoodsCarDO> getListByGBrandGPart(Integer brandId, Integer partId) {
        List<CommodityGoodsCarDO> list = new ArrayList<>();
        if(null == partId || partId.equals(-1)){
            return list;
        }
        if(brandId != null && brandId.equals(-1)){
            brandId = null;
        }

        list = commodityGoodsCarDao.getListByGBrandGPart(brandId, partId);
        return list;
    }

    @Override
    public CommodityGoodsCarDO selectByUuIdLiyangWithoutDelete(String uuId, String liyangId) {

        return commodityGoodsCarDao.getCommodityGoodsCarDOMapper().selectByUuIdLiyangWithoutDelete(uuId, liyangId);
    }
}
