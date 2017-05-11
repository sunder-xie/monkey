package com.tqmall.monkey.client.commodity;

import com.tqmall.monkey.client.category.CategoryPartService;
import com.tqmall.monkey.client.shiro.MonkeyJdbcRealm;
import com.tqmall.monkey.component.utils.BigDataToMySql;
import com.tqmall.monkey.dal.dao.commodity.CommodityGoodsDao;
import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.category.CategoryPartDO;
import com.tqmall.monkey.domain.entity.commodity.CommodityGoodsDO;
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
 * Created by zxg on 15/8/21.
 * 10:23
 */
@Service
public class CommodityGoodsServiceImpl implements CommodityGoodsService {

    //获得当前登录用户信息
    @Autowired
    private MonkeyJdbcRealm monkeyJdbcRealm;
    //大数据导入
    @Autowired
    private BigDataToMySql bigDataToMySql;

    @Autowired
    private CommodityGoodsDao commodityGoodsDao;
    @Autowired
    private CategoryPartService categoryPartService;

    @Override
    public CommodityGoodsDO getCommodityGoodsDOByCode(String goodsCode) {
        return commodityGoodsDao.getCommodityGoodsDOMapper().getCommodityGoodsDOByCode(goodsCode);
    }

    @Override
    public boolean existCommodityGoods(String goodsCode) {
        return commodityGoodsDao.getCommodityGoodsDOMapper().existCommodityGoods(goodsCode);
    }

    @Override
    public boolean insertGoods(CommodityGoodsDO goodsDO) {
        try {
                commodityGoodsDao.getCommodityGoodsDOMapper().insertSelective(goodsDO);
                return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateStatus(CommodityGoodsDO goodsDO) {
        try {
            if(null == goodsDO.getId()){
                return false;
            }
            commodityGoodsDao.getCommodityGoodsDOMapper().updateByPrimaryKeySelective(goodsDO);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public CommodityGoodsDO getCommodityGoods(String goodsCode, Integer goodsQualityType, Integer brandId) {
        return commodityGoodsDao.getCommodityGoods(goodsCode, goodsQualityType, brandId);
    }

    @Override
    public boolean insertBatchGoods(List<CommodityGoodsDO> list) {
        try {
            if (list.size() > 0) {
                commodityGoodsDao.getCommodityGoodsDOMapper().insertBatch(list);
            }
            return true;
        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean insertBatchGoodsByLoadData(List<CommodityGoodsDO> list) {
        try {
            if (list.size() > 0) {
                Integer userId = monkeyJdbcRealm.getCurrentUser().getId();
                long now = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date dt = new Date(now);
                String nowTime = sdf.format(dt);

                String headerSql = "LOAD DATA LOCAL INFILE 'sql.csv' IGNORE INTO TABLE modeldatas.TABLENAME";
                String relationSql = headerSql.replace("TABLENAME", "db_monkey_commodity_goods") +
                        "( uuId,goods_code, goods_quality_type, brand_id, brand_name,goods_name, goods_format, cate_kind," +
                        "    guarantee_time, sale_unit, sale_status, part_id, part_name, part_sum_code, remark, creator, " +
                        "    modifier, gmt_modified, gmt_create,isdelete)";
                StringBuilder builder = new StringBuilder();
                for (CommodityGoodsDO goodsDO : list) {

                    builder.append(goodsDO.getUuId());
                    builder.append("\t");
                    builder.append(goodsDO.getGoodsCode());
                    builder.append("\t");
                    builder.append(goodsDO.getGoodsQualityType());
                    builder.append("\t");
                    builder.append(goodsDO.getBrandId());
                    builder.append("\t");
                    builder.append(goodsDO.getBrandName());
                    builder.append("\t");
                    builder.append(goodsDO.getGoodsName());
                    builder.append("\t");
                    builder.append(goodsDO.getGoodsFormat());
                    builder.append("\t");
                    builder.append(goodsDO.getCateKind());
                    builder.append("\t");
                    builder.append(goodsDO.getGuaranteeTime());
                    builder.append("\t");
                    builder.append(goodsDO.getSaleUnit());
                    builder.append("\t");
                    builder.append(goodsDO.getSaleStatus());
                    builder.append("\t");
                    builder.append(goodsDO.getPartId());
                    builder.append("\t");
                    builder.append(goodsDO.getPartName());
                    builder.append("\t");
                    builder.append(goodsDO.getPartSumCode());
                    builder.append("\t");
                    builder.append(goodsDO.getRemark());
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
                    bigDataToMySql.bulkLoadFromInputStream(relationSql, relationInput);
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteBatchGoods(List<String> goodsUuIdList, Integer userId) {
        try {
            if (goodsUuIdList.size() > 0) {
                commodityGoodsDao.deleteBatchGoodsUuId(goodsUuIdList, userId);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean updateStatusBatch(Integer saleStatus, List<String> goodsUuIdList, Integer updateUserId, Integer isdelete) {
        try {
            if (goodsUuIdList.size() > 0) {
                commodityGoodsDao.updateSaleStatusBatchUuId(saleStatus, goodsUuIdList, updateUserId, isdelete);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateByPart(Integer oldPartId, Integer newPartId) {
        CategoryPartDO partDO = categoryPartService.findCategoryPartById(newPartId);
        if(null == partDO) {
            return false;
        }
        CommodityGoodsDO existGoodsDO = new CommodityGoodsDO();
        existGoodsDO.setPartId(oldPartId);
        existGoodsDO.setNone();
        existGoodsDO.setUuId(null);
        CommodityGoodsDO commodityGoodsDO = new CommodityGoodsDO();
        commodityGoodsDO.setNone();
        commodityGoodsDO.setPartId(newPartId);
        commodityGoodsDO.setPartSumCode(partDO.getSumCode());
        commodityGoodsDO.setPartName(partDO.getPartName());

        commodityGoodsDao.getCommodityGoodsDOMapper().updateByGoodsSelective(commodityGoodsDO,existGoodsDO);
        return true;
    }

    @Override
    public CommodityGoodsDO getCommodityGoodsByPrimaryKey(Integer id) {
        return commodityGoodsDao.getCommodityGoodsDOMapper().selectByPrimaryKey(id);
    }

    @Override
    public List<CommodityGoodsDO> getBrandPartGroupByThis() {
        return commodityGoodsDao.getBrandPartGroupByThis();
    }

    @Override
    public Page<CommodityGoodsDO> getGoodsPage(Integer brandId, Integer partId, String format, Integer index, Integer pageSize) {
        return commodityGoodsDao.getGoodsPage(brandId, partId, format, index, pageSize);
    }

    @Override
    public List<CommodityGoodsDO> getGoodsAndHasNewCar(Integer goodsCarStatus) {
        return commodityGoodsDao.getGoodsAndHasNewCar(goodsCarStatus);
    }

    //todo
    @Override
    public List<CommodityGoodsDO> getGoodsByBrandPart(Integer brandId, Integer partId) {
        List<CommodityGoodsDO> list = new ArrayList<>();
        if (null == partId || partId.equals(-1)) {
            return list;
        }
        if (brandId != null && brandId.equals(-1)) {
            brandId = null;
        }


        list = commodityGoodsDao.getGoodsByBrandPart(brandId, partId);

        return list;
    }

    @Override
    public List<CommodityGoodsDO> getGoodsByBrand(Integer brandId) {
        List<CommodityGoodsDO> list;
        if (brandId != null && brandId.equals(-1)) {
            brandId = null;
        }


        list = commodityGoodsDao.getGoodsByBrandPart(brandId, null);

        return list;
    }
}
