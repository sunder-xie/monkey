package com.tqmall.monkey.rest.test;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.tqmall.athena.domain.result.carcategory.CarCategoryDTO;
import com.tqmall.core.common.entity.Result;
import com.tqmall.monkey.client.commodity.CommodityGoodsCarService;
import com.tqmall.monkey.client.commodity.CommodityGoodsService;
import com.tqmall.monkey.client.online.OnlineGoodsService;
import com.tqmall.monkey.client.redisManager.car.CarRedisManager;
import com.tqmall.monkey.client.redisManager.goods.GoodsRedisManager;
import com.tqmall.monkey.component.helper.CarServiceHelp;
import com.tqmall.monkey.domain.entity.commodity.CommodityBrandDO;
import com.tqmall.monkey.domain.entity.commodity.CommodityGoodsCarDO;
import com.tqmall.monkey.domain.entity.commodity.CommodityGoodsDO;
import com.tqmall.monkey.domain.entity.online.OnlineGoodsCarDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zxg on 15/9/7.
 * 商品库的对应关系生成text，导入电商库
 */

@RestController
@RequestMapping("/rest/goodsCar")
public class CommodityOnlineGoodsCarController {
    Logger logger = LoggerFactory.getLogger(CommodityOnlineGoodsCarController.class);

    @Value(value = "${file.address}")
    protected String saveFile ;

    @Autowired
    private CommodityGoodsService commodityGoodsService;
    @Autowired
    private CommodityGoodsCarService commodityGoodsCarService;
    @Autowired
    private GoodsRedisManager goodsRedisManager;
    @Autowired
    private CarServiceHelp carServiceHelp;
    @Autowired
    private CarRedisManager carRedisManager;


    @RequestMapping(value = "/write", method = RequestMethod.GET)
    public Result write(Integer status){

        try{
            logger.debug("===============start=====write======");
            Date now = new Date();
            SimpleDateFormat day = new SimpleDateFormat( "yyyy-MM-dd");
            SimpleDateFormat hour = new SimpleDateFormat( "HH_mm");
            String dayString = day.format(now);
            String hourString = hour.format(now);

            String fileName =  saveFile+"/temp/"+dayString;
            File file = new File(fileName);
            if(!file.exists()){
                file.mkdirs();
            }

            String insertFileName = fileName+"/"+hourString+"_insertGoodsCar.text";
            String deleteFileName = fileName+"/"+hourString+"_deleteGoodsCar.text";

            String wrongFileName = fileName+"/"+hourString+"_insertGoodsCarWrong.text";
            String successFileName = fileName+"/"+hourString+"_insertGoodsCarSuccess.text";

            File insertFile = new File(insertFileName);
            if(!insertFile.exists()){
                insertFile.createNewFile();
            }

            File deleteFile = new File(deleteFileName);
            if(!deleteFile.exists()){
                deleteFile.createNewFile();
            }

            File wrongFile = new File(wrongFileName);
            if(!wrongFile.exists()){
                wrongFile.createNewFile();
            }

            File successFile = new File(successFileName);
            if(!successFile.exists()){
                successFile.createNewFile();
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(insertFile));
            BufferedWriter deleteWriter = new BufferedWriter(new FileWriter(deleteFile));
            BufferedWriter wrongWriter = new BufferedWriter(new FileWriter(wrongFile));
            BufferedWriter successWriter = new BufferedWriter(new FileWriter(successFile));

            //获得要插入的内容
            HashMap<String,StringBuilder> map = getAddBuilder(status);
            StringBuilder builder = map.get("insertBuild");
            StringBuilder deleteBuilder = map.get("deleteBuilder");
            StringBuilder wrongBuilder = map.get("wrongBuilder");
            StringBuilder successBuilder = map.get("successBuilder");

            writer.write(builder.toString());
            deleteWriter.write(deleteBuilder.toString());
            wrongWriter.write(wrongBuilder.toString());
            successWriter.write(successBuilder.toString());

            writer.close();
            deleteWriter.close();
            wrongWriter.close();
            successWriter.close();

            logger.debug("===============end=====write======");

            return Result.wrapSuccessfulResult("success");
        }catch(Exception e){
            logger.error("===============wrong=====write======" + e.getMessage());
            e.printStackTrace();
            return Result.wrapSuccessfulResult(e.getMessage());
        }
    }

//======================二级=====================

    private HashMap<String,StringBuilder> getAddBuilder(Integer status){
        HashMap<String,StringBuilder> map = new HashMap<>();
        StringBuilder builder = new StringBuilder();
        StringBuilder deleteBuilder = new StringBuilder();
        StringBuilder wrongBuilder = new StringBuilder();
        StringBuilder successBuilder = new StringBuilder();
        //要插入的数据
        Set<OnlineGoodsCarDO> insertSet = new HashSet<>();
        //要删除的goodsId
        Set<Integer> deleteGoodsSet = new HashSet<>();


        //获得商品信息--新建关系
        if(null == status){
            status = CommodityGoodsCarDO.NEW_STATUS;
        }
        if(!(status.equals(CommodityGoodsCarDO.FAIL_STATUS) || status.equals(CommodityGoodsCarDO.NEW_STATUS)
                || status.equals(CommodityGoodsCarDO.SUCCESS_STATUS) )){
            status = CommodityGoodsCarDO.NEW_STATUS;
        }

        List<CommodityGoodsDO> goodsList = commodityGoodsService.getGoodsAndHasNewCar(status);
        Map<String,Collection<Integer>> goodsMap = this.getOnlineGId(goodsList,status,wrongBuilder,successBuilder);

        //对应的车型信息
        //力洋id对应的CarCategoryDTO对象
        Set<String> keySet = goodsMap.keySet();
        List<String> successUUList = new ArrayList(keySet) ;

//        List<String> liyangIdList = commodityGoodsCarService.getLiYangListGroupBy(successUUList,null);

        boolean up = true;

        if(up) {
            HashMap<Integer,List<CarCategoryDTO>> carIdPListMap = new HashMap<>();

            HashMap<String, CarCategoryDTO> liyangMap = new HashMap<>();

            Set<String> failLiyangList = new HashSet<>();

            for (String uuId : keySet) {
                List<CommodityGoodsCarDO> carList = commodityGoodsCarService.getListByGoodsUuIdStatus(uuId, status);
                for (CommodityGoodsCarDO carDO : carList) {
                    String liyangId = carDO.getLiyangId();
                    CarCategoryDTO carCategoryDTO;
                    if(liyangMap.containsKey(liyangId)){
                        carCategoryDTO = liyangMap.get(liyangId);
                    }else{
                        carCategoryDTO = carRedisManager.getOnlineCarByLiyangId(liyangId);
                        liyangMap.put(liyangId, carCategoryDTO);
                    }
                    if (null != carCategoryDTO) {
                        OnlineGoodsCarDO onlineGoodsCarDO = new OnlineGoodsCarDO();
                        Integer carId = carCategoryDTO.getId();
                        if(null == carId){
                            logger.info("carCategoryDTO is empty,liYangId:"+liyangId);
                            wrongBuilder.append("liyangId:"+liyangId+"\t"+"无对应的线上carId");
                            wrongBuilder.append("\n");
                            failLiyangList.add(liyangId);
                            continue;
                        }

                        Integer level = carCategoryDTO.getLevel();
                        if(null == level){
                            continue;
                        }
                        Set<Integer> goodsIdList = Sets.newHashSet(goodsMap.get(uuId));

                        onlineGoodsCarDO.setCarId(carId);
                        onlineGoodsCarDO.setCarName(carCategoryDTO.getName());
                        onlineGoodsCarDO.setLevel(level);

                        //该carId对应的所有父id,拼接对象
                        List<CarCategoryDTO> pIdList = new ArrayList<>();
                        List<CarCategoryDTO> resultList = carIdPListMap.get(carId);
                        if(resultList == null) {
                            pIdList = this.getPidList(carId);
                            System.out.println("==this carId is ====" + carId);
                            carIdPListMap.put(carId, pIdList);
                        }else{
                            pIdList = resultList;
                        }
//
                        for(CarCategoryDTO carCategoryDTO1 : pIdList){
                            Integer newLevel = carCategoryDTO1.getLevel();
                            if(newLevel.equals(level)){
                                continue;
                            }
                            if(newLevel.equals(1)){
                                onlineGoodsCarDO.setCarBrand(carCategoryDTO1.getName());
                                onlineGoodsCarDO.setCarBrandId(carCategoryDTO1.getId());
                                continue;
                            }
                            if(newLevel.equals(2)){
                                onlineGoodsCarDO.setCarSeries(carCategoryDTO1.getName());
                                onlineGoodsCarDO.setCarSeriesId(carCategoryDTO1.getId());
                                continue;
                            }
                            if(newLevel.equals(3)){
                                onlineGoodsCarDO.setCarModel(carCategoryDTO1.getName());
                                onlineGoodsCarDO.setCarModelId(carCategoryDTO1.getId());
                                continue;
                            }
                            if(newLevel.equals(4)){
                                onlineGoodsCarDO.setCarPower(carCategoryDTO1.getName());
                                onlineGoodsCarDO.setCarPowerId(carCategoryDTO1.getId());
                                continue;
                            }
                            if(newLevel.equals(5)){
                                onlineGoodsCarDO.setCarYear(carCategoryDTO1.getName());
                                onlineGoodsCarDO.setCarYearId(carCategoryDTO1.getId());
                                continue;
                            }
                        }

                        for(Integer goodsId : goodsIdList) {
                            deleteGoodsSet.add(goodsId);

                            onlineGoodsCarDO.setGoodsId(goodsId);
                            insertSet.add(onlineGoodsCarDO);
                        }

                    }
                }
            }

            //置无力洋Id的数据为失败
            if (!status.equals(CommodityGoodsCarDO.SUCCESS_STATUS)) {
                up = commodityGoodsCarService.updataStatusBatch(CommodityGoodsCarDO.SUCCESS_STATUS, successUUList, null);
                commodityGoodsService.updateStatusBatch(CommodityGoodsDO.ONLINE_SALE_STATUS, successUUList, null, null);
            }
            commodityGoodsCarService.updataStatusBatchByLiyang(CommodityGoodsCarDO.FAIL_STATUS, new ArrayList<>(failLiyangList), null);

            //要删除的数据拼build
            deleteBuilder = addDeleteToBuild(deleteGoodsSet);
            //数据拼接至build里
            builder = addDataToBuild(insertSet);
        }
        map.put("insertBuild",builder);
        map.put("deleteBuilder",deleteBuilder);
        map.put("wrongBuilder",wrongBuilder);
        map.put("successBuilder",successBuilder);
        return map;
    }


//============================= 三级=get OnlineGoodsId==========================
    private StringBuilder addDeleteToBuild(Set<Integer> deleteGoodsSet){
        StringBuilder deleteBuilder = new StringBuilder();
        int size = deleteGoodsSet.size();

        if(size > 0){
            deleteBuilder.append("select @now_time := now();");
            for(Integer goodsId : deleteGoodsSet){
                deleteBuilder.append("update db_goods_car set status=2,gmt_modified=@now_time where goods_id = "+goodsId);
                deleteBuilder.append(";");
                deleteBuilder.append("\r\n");
            }
        }
        return deleteBuilder;
    }

    //生成insert语句
    private StringBuilder addDataToBuild(Set<OnlineGoodsCarDO> insertSet){
        StringBuilder builder = new StringBuilder();
        int size = insertSet.size();
        int index = 0;
        int pageSize = 2000;
        if(size > 0) {
            builder.append("select @now_time := now();");
            String insertSql = "insert into db_goods_car (goods_id,car_id,car_name,level,car_brand_id,car_brand,car_series_id,car_series,car_model_id,car_model,car_power_id,car_power," +
                    "car_year_id,car_year,status,gmt_create,gmt_modified) value \n";
            builder.append(insertSql);
            for (OnlineGoodsCarDO carDO : insertSet) {
                builder.append("(");
                builder.append(carDO.getGoodsId());
                builder.append(",");
                builder.append(carDO.getCarId());
                builder.append(", '");
                builder.append(carDO.getCarName());
                builder.append("',");
                builder.append(carDO.getLevel());
                builder.append(",");
                builder.append(carDO.getCarBrandId());
                builder.append(",'");
                builder.append(carDO.getCarBrand());
                builder.append("',");
                builder.append(carDO.getCarSeriesId());
                builder.append(",'");
                builder.append(carDO.getCarSeries());
                builder.append("',");
                builder.append(carDO.getCarModelId());
                builder.append(",'");
                builder.append(carDO.getCarModel());
                builder.append("',");
                builder.append(carDO.getCarPowerId());
                builder.append(",'");
                builder.append(carDO.getCarPower());
                builder.append("',");
                builder.append(carDO.getCarYearId());
                builder.append(",'");
                builder.append(carDO.getCarYear());
                builder.append("',");
                builder.append("1,@now_time,@now_time");
                builder.append(")");

                index ++;
                if(index == size){
                    builder.append(";");
                    builder.append("\r\n");

                }else{
                    if(index % pageSize == 0){
                        builder.append(";");
                        builder.append("\r\n");

                        builder.append(insertSql);
                    }else{
                        builder.append(",\n");
                    }
                }

            }
        }
        return builder;
    }

    private Map<String,Collection<Integer>> getOnlineGId(List<CommodityGoodsDO> goodsList,Integer status,StringBuilder wrongBuilder,StringBuilder successBuilder){
        Map<String,Collection<Integer>> goodsMap = new HashMap<>();
        List<String> failUUList = new ArrayList<>();

        boolean istrue = true;
        Integer gListSize = goodsList.size();
        if(gListSize > 0){
            Multimap<String, Integer> onlineGoodsMap = goodsRedisManager.getGoodsListOfFormatBrand();

            for(CommodityGoodsDO goodsDO : goodsList){
                Integer id = goodsDO.getId();
                String uuId = goodsDO.getUuId();

                //规格型号+品牌名称获得对应的online商品
                Integer brandId = goodsDO.getBrandId();
                String brandName = "" ;
                String goodsFormat = goodsDO.getGoodsFormat();

                String goodsCode = goodsDO.getGoodsCode();
                Integer goodsQuality = goodsDO.getGoodsQualityType();

                //拼品牌名称
                if (brandId.equals(0)){
                    if(this.goods_quality_type_map.containsKey(goodsQuality)) {
                        brandName = this.goods_quality_type_map.get(goodsQuality);
                    }else{
                        istrue = false;
                        wrongBuilder.append("id:"+id+"\t"+"uuId:"+uuId+"\t"+"无属性");
                        wrongBuilder.append("\n");
                        failUUList.add(uuId);
                    }
                }else{
                    CommodityBrandDO brandDO = goodsRedisManager.getBrandByPrimaryKey(brandId);
                    if(brandDO == null){
                        continue;
                    }
                    String nameCh = brandDO.getNameCh().trim();
                    String nameEn = brandDO.getNameEn().trim();

                    brandName = nameCh;
                    if("".equals(nameCh)){
                        brandName = nameEn;
                    }else if(! "".equals(nameEn)){
                        brandName = nameCh+"/"+nameEn;
                    }
                }


                if(null == goodsFormat || "".equals(goodsFormat)){
                    goodsFormat = goodsCode;
                }
                if (null != goodsFormat) {
                    goodsFormat = goodsFormat.replaceAll(" +", "");
                }
                if(istrue) {
                    String key = brandName.toUpperCase()+"_"+goodsFormat;
                    Collection<Integer> onGoodsIds = onlineGoodsMap.get(key);
//                    List<Integer> onlineGIdList = goodsRedisManager.getOnlineGoodsByFormatBrand(goodsFormat, brandName);
                    if(null == onGoodsIds || onGoodsIds.isEmpty()){
                        wrongBuilder.append("id:"+id+"\t"+"uuId:"+uuId+"\t"+"format:"+goodsFormat+"\t"+"brandName:"+brandName+"\t"+"对应不上线上goodsId");
                        wrongBuilder.append("\n");
                        failUUList.add(uuId);
                    }else {
                        successBuilder.append("id:"+id+"\t"+"uuId:"+uuId+"\t"+"format:"+goodsFormat+"\t"+"brandName:"+brandName+"\t"+"线上goodsId:"+ JSON.toJSONString(onGoodsIds));
                        successBuilder.append("\n");
                        goodsMap.put(uuId, onGoodsIds);
                    }
                }
            }
        }

        if (!status.equals(CommodityGoodsCarDO.FAIL_STATUS)) {
            commodityGoodsCarService.updataStatusBatch(CommodityGoodsCarDO.FAIL_STATUS, failUUList, null);
            commodityGoodsService.updateStatusBatch(CommodityGoodsDO.NEW_SALE_STATUS, failUUList, null, null);
        }
        return goodsMap;
    }

    //获得父id的所有
    private List<CarCategoryDTO> getPidList(Integer carId){
        List<CarCategoryDTO> resultList = carServiceHelp.getParentsByCarId(carId);

        return resultList;
    }









    //规定属性
    private HashMap<Integer, String> goods_quality_type_map = new HashMap<Integer,String>() {
        {
            put(0,"品牌");
            put(1,"正厂原厂");
            put(2,"正厂配套");
            put(3,"正厂下线");
            put(4,"全新拆车");
            put(5,"旧件拆车");
            put(6,"副厂");
            put(9,"高仿");
        }
    };
}
