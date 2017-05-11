package com.tqmall.monkey.web.commodity;

import com.google.common.collect.Multimap;
import com.tqmall.core.common.entity.Result;
import com.tqmall.monkey.client.car.CarInfoAllService;
import com.tqmall.monkey.client.commodity.CommodityGoodsAttrService;
import com.tqmall.monkey.client.commodity.CommodityGoodsCarService;
import com.tqmall.monkey.client.commodity.CommodityGoodsOeService;
import com.tqmall.monkey.client.commodity.CommodityGoodsService;
import com.tqmall.monkey.client.common.GoodsAttrKeyService;
import com.tqmall.monkey.client.redisManager.goods.GoodsRedisManager;
import com.tqmall.monkey.client.shiro.MonkeyJdbcRealm;
import com.tqmall.monkey.component.utils.PoiUtil;
import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.component.utils.UUIDGenerator;
import com.tqmall.monkey.domain.entity.UserDO;
import com.tqmall.monkey.domain.entity.car.CarInfoAllDO;
import com.tqmall.monkey.domain.entity.commodity.*;
import com.tqmall.monkey.domain.entity.common.GoodsAttrKeyDO;
import com.tqmall.monkey.domain.model.Pair;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by zxg on 15/8/20.
 * 展示商品库数据
 */

@Controller
@RequestMapping("/monkey/commodity/show")
public class CommodityShowController {


    //文件的保存位置
    @Value(value = "${file.address}")
    protected String bashFile;

    private Double commodity_downLoad_percentage = 0.0;

    @Autowired
    private CommodityGoodsService commodityGoodsService;
    @Autowired
    private CommodityGoodsAttrService commodityGoodsAttrService;
    @Autowired
    private CommodityGoodsCarService commodityGoodsCarService;
    @Autowired
    private CommodityGoodsOeService commodityGoodsOeService;

    @Autowired
    private CarInfoAllService carInfoAllService;


    @Autowired
    private GoodsAttrKeyService goodsAttrKeyService;

    //redis
    @Autowired
    private GoodsRedisManager goodsRedisManager;

    //获得当前登录用户信息
    @Autowired
    private MonkeyJdbcRealm monkeyJdbcRealm;

    private PoiUtil poiUtil = new PoiUtil();


    //商品库展示
    @RequestMapping(value = "index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("monkey/commodity/show");

        UserDO user = monkeyJdbcRealm.getCurrentUser();
        if (user == null) {
            return new ModelAndView("monkey/login");
        }

        return modelAndView;
    }

    //获得已有品牌和标准名称关系
    @RequestMapping(value = "/brandPartMap", method = RequestMethod.GET)
    public
    @ResponseBody
    HashMap<String, Object> brandPartMap() {
        List<CommodityGoodsDO> goodsList = commodityGoodsService.getBrandPartGroupByThis();

        //以品牌为key
        HashMap<Integer, Object> brandPartMap = new HashMap<>();
        HashMap<Integer, String> brandNameMap = new HashMap<>();
        //以标准名称为key
        HashMap<Integer, Object> partBrandMap = new HashMap<>();
        HashMap<Integer, String> partNameMap = new HashMap<>();

        for (CommodityGoodsDO goodsDO : goodsList) {
            final Integer brandId = goodsDO.getBrandId();
            final String brandName = goodsDO.getBrandName();
            final Integer partId = goodsDO.getPartId();
            final String partName = goodsDO.getPartName();

            //以品牌为key
            Set<Pair<Integer, String>> partValue = new HashSet<>();
            if (brandPartMap.containsKey(brandId)) partValue = (Set<Pair<Integer, String>>) brandPartMap.get(brandId);
            partValue.add(new Pair(partId, partName));
            brandPartMap.put(brandId, partValue);

            if (!brandNameMap.containsKey(brandId)) {
                brandNameMap.put(brandId, brandName);
            }

            //以标准名称为key
            Set<Pair<Integer, String>> brandValue = new HashSet<>();
            if (partBrandMap.containsKey(partId)) {
                brandValue = (Set<Pair<Integer, String>>) partBrandMap.get(partId);
            }
            brandValue.add(new Pair(brandId, brandName));

            partBrandMap.put(partId, brandValue);

            if (!partNameMap.containsKey(partId)) {
                partNameMap.put(partId, partName);
            }
        }
        HashMap<String, Object> result = new HashMap<>();
        result.put("brandPartMap", brandPartMap);
        result.put("brandNameMap", brandNameMap);
        result.put("partBrandMap", partBrandMap);
        result.put("partNameMap", partNameMap);

        return result;
    }

    //获得筛选后的商品数据
    @RequestMapping(value = "/getGoodsPage", method = RequestMethod.GET)
    public
    @ResponseBody
    Result getGoodsPage(Integer brandId, Integer partId, String format, Integer index, Integer pageSize) {
        HashMap<String, Object> resultMap = new HashMap<>();

        if (index == null) {
            index = 1;
        }
        if (null != brandId && brandId.equals(-1)) {
            brandId = null;
        }
        if (null != partId && partId.equals(-1)) {
            partId = null;
        }
        format = format.trim();
        if ("".equals(format)) {
            format = null;
        }

        Page<CommodityGoodsDO> page = commodityGoodsService.getGoodsPage(brandId, partId, format, index, pageSize);
        if (page == null) {
            resultMap.put("goodsList", Collections.emptyList());
            resultMap.put("totalNumber", 0);
            resultMap.put("totalPages", 0);
        } else {
            resultMap.put("goodsList", page.getItems());
            resultMap.put("totalNumber", page.getTotalNumber());
            resultMap.put("totalPages", page.getTotalPage());
        }

        return Result.wrapSuccessfulResult(resultMap);

    }


    //获得商品对应的所有车型
    @RequestMapping(value = "/searchLiyangCar", method = RequestMethod.GET)
    public
    @ResponseBody
    Result searchLiyangCar(Integer index, Integer pageSize, String uuId,String searchLiyang) {
        HashMap<String, Object> resultMap = new HashMap<>();

        if (index == null) {
            index = 1;
        }
        searchLiyang = searchLiyang.replaceAll(" +","").toUpperCase();
        if("".equals(searchLiyang)){
            searchLiyang = null;
        }
        Page<CommodityGoodsCarDO> liyangIdPage = commodityGoodsCarService.getPageByGoodsUuId(uuId, searchLiyang, index, pageSize);

        if (liyangIdPage == null) {
            resultMap.put("resultDataList", Collections.emptyList());
            resultMap.put("totalNumber", 0);
            resultMap.put("totalPages", 0);
        } else {
            List<CarInfoAllDO> liyangList = new ArrayList<>();
            for (CommodityGoodsCarDO carDO : liyangIdPage.getItems()) {
                String liyangId = carDO.getLiyangId();
                CarInfoAllDO carInfoAllDO = carInfoAllService.findCarInfoByLeyelId(liyangId);
                if(carInfoAllDO != null) {
                    liyangList.add(carInfoAllDO);
                }
            }

            resultMap.put("resultDataList", liyangList);
            resultMap.put("totalNumber", liyangIdPage.getTotalNumber());
            resultMap.put("totalPages", liyangIdPage.getTotalPage());
        }

        return Result.wrapSuccessfulResult(resultMap);
    }


    @RequestMapping(value = "/searchAttr", method = RequestMethod.GET)
    public
    @ResponseBody
    Result searchAttr(String uuId) {
        List<CommodityGoodsAttrDO> list = commodityGoodsAttrService.getListByGoodsUuId(uuId);
        if (list == null || list.size() == 0) {
            return Result.wrapErrorResult("001", "not have attr");
        }

        return Result.wrapSuccessfulResult(list);
    }

    @RequestMapping(value = "/searchOe", method = RequestMethod.GET)
    public
    @ResponseBody
    Result searchOe(String uuId) {
        List<CommodityGoodsOeDO> list = commodityGoodsOeService.getListByUuId(uuId);
        if (list == null || list.size() == 0) {
            return Result.wrapErrorResult("001", "not have oe");
        }

        return Result.wrapSuccessfulResult(list);
    }

    //获得品牌数据
    @RequestMapping(value = "/getGoodsBrand", method = RequestMethod.GET)
    public
    @ResponseBody
    Result getGoodsBrand() {
        List<CommodityBrandDO> list = goodsRedisManager.getAllBrand();
        if (list.isEmpty()) {
            return Result.wrapErrorResult("001", "wrong brandData");
        }

        return Result.wrapSuccessfulResult(list);
    }

    //保存商品数据
    @RequestMapping(value = "/saveGoods", method = RequestMethod.POST)
    public
    @ResponseBody
    Result saveGoods(CommodityGoodsDO commodityGoodsDO) {
        try {
            String uuId;
            UserDO user = monkeyJdbcRealm.getCurrentUser();
            if (null == user) {
                return Result.wrapErrorResult("001", "登录超时，请重新登录");
            }
            Integer userId = user.getId();

            Integer id = commodityGoodsDO.getId();
            commodityGoodsDO.setModifier(userId);
            if (id.equals(0)) {
                //判断是否存在
                CommodityGoodsDO exit = commodityGoodsService.getCommodityGoods(commodityGoodsDO.getGoodsCode(), commodityGoodsDO.getGoodsQualityType(), commodityGoodsDO.getBrandId());
                if (null != exit) {
                    return Result.wrapErrorResult("003", "已存在该商品编码、商品属性和品牌对应的商品");

                }
                //插入操作
                uuId = UUIDGenerator.getUUID();
                commodityGoodsDO.setUuId(uuId);
                commodityGoodsDO.setCreator(userId);
                commodityGoodsService.insertGoods(commodityGoodsDO);
            } else {
                //更新操作
                commodityGoodsService.updateStatus(commodityGoodsDO);
                uuId = commodityGoodsDO.getUuId();
            }


            return Result.wrapSuccessfulResult(uuId);
        } catch (Exception e) {
            return Result.wrapErrorResult("002", e.getMessage());
        }
    }

    //保存商品oe
    @RequestMapping(value = "/saveOe", method = RequestMethod.POST)
    public
    @ResponseBody
    Result saveOe(String oes, String goodsUuId) {
        try {
            UserDO user = monkeyJdbcRealm.getCurrentUser();
            if (null == user) {
                return Result.wrapErrorResult("001", "登录超时，请重新登录");
            }
            Integer userId = user.getId();
            //置此uuId下的数据为0
            commodityGoodsOeService.deleteOeByGoodsUuId(goodsUuId, userId);
            //插入oe，若存在，则将删除置为0--不删除
            oes = oes.replaceAll(" +", " ").trim();
            for (String oe : oes.split(" ")) {
                if ("".equals(oe)) {
                    continue;
                }
                CommodityGoodsOeDO commodityGoodsOeDO = new CommodityGoodsOeDO();
                commodityGoodsOeDO.setOeNumber(oe);
                commodityGoodsOeDO.setGoodsUuId(goodsUuId);
                commodityGoodsOeDO.setCreator(userId);
                commodityGoodsOeDO.setModifier(userId);
                commodityGoodsOeService.insertOe(commodityGoodsOeDO);
            }
            return Result.wrapSuccessfulResult("true");

        } catch (Exception e) {
            return Result.wrapErrorResult("002", e.getMessage());
        }

    }

    //保存商品参数
    @RequestMapping(value = "/saveAttr", method = RequestMethod.POST)
    public
    @ResponseBody
    Result saveAttr(String attrKey, String attrValue, String goodsUuId) {
        if (null == attrKey || "".equals(attrKey.trim())) {
            return Result.wrapErrorResult("001", "attrKey is null");
        }
        UserDO user = monkeyJdbcRealm.getCurrentUser();
        if (null == user) {
            return Result.wrapErrorResult("001", "登录超时，请重新登录");
        }
        Integer userId = user.getId();

        attrKey = attrKey.trim();
        attrValue = attrValue.trim();
        if ("".equals(attrValue)) {
            return Result.wrapErrorResult("002", "attrValue is empty");
        }
        GoodsAttrKeyDO goodsAttrKeyDO = new GoodsAttrKeyDO();
        goodsAttrKeyDO.setAttrName(attrKey);
        Integer attrId = goodsAttrKeyService.insertGoodsAttr(goodsAttrKeyDO);

        CommodityGoodsAttrDO attrDO = new CommodityGoodsAttrDO();
        attrDO.setAttrName(attrKey);
        attrDO.setGoodsUuId(goodsUuId);
        attrDO.setAttrId(attrId);
        attrDO.setAttrValue(attrValue);
        attrDO.setCreator(userId);
        attrDO.setModifier(userId);

        try {
            commodityGoodsAttrService.insertAttr(attrDO);

            return Result.wrapSuccessfulResult("success");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.wrapErrorResult("003", e.getMessage());

        }
    }

    //保存liyang
    @RequestMapping(value = "/saveLiyang", method = RequestMethod.POST)
    public
    @ResponseBody
    Result saveLiyang(String liyangId, String goodsUuId) {
        if (null == liyangId ) {
            return Result.wrapErrorResult("001", "liyangId is null");
        }
        UserDO user = monkeyJdbcRealm.getCurrentUser();
        if (null == user) {
            return Result.wrapErrorResult("001", "登录超时，请重新登录");
        }
        Integer userId = user.getId();

        liyangId = liyangId.replaceAll(" +","").toUpperCase();
        if("".equals(liyangId)){
            return Result.wrapSuccessfulResult("success");
        }

        CarInfoAllDO carInfoAllDO = carInfoAllService.findCarInfoByLeyelId(liyangId);
        if(null == carInfoAllDO){
            return Result.wrapErrorResult("002", "此力洋ID不存在");

        }
        CommodityGoodsCarDO goodsCarDO = new CommodityGoodsCarDO();

        goodsCarDO.setLiyangId(liyangId);
        goodsCarDO.setGoodsUuId(goodsUuId);
        goodsCarDO.setModifier(userId);
        goodsCarDO.setCreator(userId);

        try {
            commodityGoodsCarService.insertGoodsCar(goodsCarDO);
            return Result.wrapSuccessfulResult("success");

        } catch (Exception e) {
            e.printStackTrace();
            return Result.wrapErrorResult("003", e.getMessage());
        }

    }

    //删除liyang
    @RequestMapping(value = "/deleteLiyang", method = RequestMethod.POST)
    public
    @ResponseBody
    Result deleteLiyang(String liyangId, String goodsUuId) {
        if (null == liyangId || "".equals(liyangId.trim())) {
            return Result.wrapErrorResult("001", "liyangId is null");
        }
        UserDO user = monkeyJdbcRealm.getCurrentUser();
        if (null == user) {
            return Result.wrapErrorResult("001", "登录超时，请重新登录");
        }
        Integer userId = user.getId();

        CommodityGoodsCarDO goodsCarDO = commodityGoodsCarService.selectByUuIdLiyangWithoutDelete(goodsUuId,liyangId);
        if(null == goodsCarDO){
            return Result.wrapErrorResult("001", "不存在该记录，无需删除");
        }
        goodsCarDO.setModifier(userId);
        goodsCarDO.setIsdelete(1);


        try {
            commodityGoodsCarService.updateGoodsCar(goodsCarDO);
            return Result.wrapSuccessfulResult("success");

        } catch (Exception e) {
            e.printStackTrace();
            return Result.wrapErrorResult("003", e.getMessage());
        }

    }




        //删除商品参数
    @RequestMapping(value = "/deleteAttr", method = RequestMethod.POST)
    public
    @ResponseBody
    Result deleteAttr(String delete_value, String goodsUuId) {
        try {
            if (null == delete_value ) {
                return Result.wrapErrorResult("001", "delete_value is null");
            }
            if("".equals(delete_value.trim())){
                return Result.wrapSuccessfulResult("success");
            }
            UserDO user = monkeyJdbcRealm.getCurrentUser();
            if (null == user) {
                return Result.wrapErrorResult("001", "登录超时，请重新登录");
            }
            Integer userId = user.getId();

            String[] attryArray = delete_value.trim().split(" ");
            for(String attrData : attryArray){
                String attrKey = attrData.split(":")[0];
                GoodsAttrKeyDO goodsAttrKeyDO = new GoodsAttrKeyDO();
                goodsAttrKeyDO.setAttrName(attrKey);
                Integer attrId = goodsAttrKeyService.insertGoodsAttr(goodsAttrKeyDO);

                CommodityGoodsAttrDO attrDO = commodityGoodsAttrService.getByUuIdAttrId(goodsUuId, attrId);
                attrDO.setIsdelete(1);
                attrDO.setModifier(userId);
                commodityGoodsAttrService.updateAttr(attrDO);
            }

            return Result.wrapSuccessfulResult("success");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.wrapErrorResult("003", e.getMessage());

        }
    }
    //=============================================导出电商需要的excle=================================================


    //动态获得文件下载处理进度
    @RequestMapping(value = "/getProgress", method = RequestMethod.GET)
    public
    @ResponseBody
    HashMap<String, Object> getProgress() {
        HashMap<String, Object> result_map = new HashMap<>();

        String progress = (String) monkeyJdbcRealm.getSessionValue("commodity_downLoad_percentage");
        if (progress == null) {
            progress = "0%";
            monkeyJdbcRealm.setSession("commodity_downLoad_percentage", progress);
        }
        result_map.put("progress", progress);
        return result_map;
    }

    @RequestMapping(value = "/exportGoodsExcelForDianShang")
    public void exportGoodsExcelForDianShang(HttpServletResponse response, Integer brandId, String brandText,
                                             Integer partId,
                                             String partText, Boolean all) throws Exception {

        if (null != partId && -1 != partId) {
            monkeyJdbcRealm.setSession("commodity_downLoad_percentage", "0%");

            String[] goodsHeadName = {
                    "标识", "唯一uuId", "商品名称", "商品品牌",
                    "规格型号", "品牌零件号", "OE码",
                    "购买单位", "使用车类型", "车件类型"
            };
            String[] goodsFieldName = {
                    "isNew", "uuId", "goodsName", "brandName",
                    "format", "goodsCode", "OE",
                    "saleUnit", "carType", "catKind"
            };
            int[] goodsColumnWith = {
                    2000,
                    2000, 13000, 6000, 5000,
                    7000, 7000, 6000,
                    4000, 5000, 5000
            };

            String goodsExcelTitle = partText + "_" + brandText + "(商品)";

            int freezeCol = 1;
            int freezeRow = 1;

            if (brandId.equals(-1)) {
                brandId = null;
            }
            //goods基本表-生成excle文件
            List<CommodityGoodsDO> goodsList;

            if (null != all && all) {
                goodsList = commodityGoodsService.getGoodsByBrand(brandId);
                System.out.println("=====goodsList==="+goodsList.size());
            } else {
                goodsList = commodityGoodsService.getGoodsByBrandPart(brandId, partId);
            }

            List<HashMap<String, Object>> goods_excel_list = this.getDownGoodsMapList(goodsList);
            Workbook goodsBook = poiUtil.exportExcelByHashLargeDataTemplate(goodsExcelTitle, goodsHeadName, goodsFieldName, goods_excel_list,
                    goodsColumnWith, true, freezeCol, freezeRow);

            poiUtil.toWritexlsxExcel(response, goodsBook, goodsExcelTitle);

            monkeyJdbcRealm.setSession("commodity_downLoad_percentage", "100%");
        } else {

            monkeyJdbcRealm.setSession("commodity_downLoad_percentage", "-1");
        }

    }

    @RequestMapping(value = "/exportCarExcelForDianShang")
    public void exportCarExcelForDianShang(HttpServletResponse response, Integer brandId, String brandText,
                                           Integer partId,
                                           String partText) throws Exception {

        if (null != partId && -1 != partId) {
            monkeyJdbcRealm.setSession("commodity_downLoad_percentage", "0%");

            String carExcelTitle = partText + "_" + brandText + "(车型)";

            //力洋表
            List<CommodityGoodsCarDO> carList = commodityGoodsCarService.getListByGBrandGPart(brandId, partId);


            response.reset();// 清空输出流
            response.setContentType("text/plain");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(carExcelTitle, "UTF-8") + ".txt");
            response.setContentType("application/x-download;");

            BufferedOutputStream buff = null;
            ServletOutputStream outSTr = null;
            try {
                outSTr = response.getOutputStream();  // 建立
                buff = new BufferedOutputStream(outSTr);
                //拼接数据
                StringBuilder write = this.addCarToBuild(carList);

                buff.write(write.toString().getBytes("UTF-8"));
                buff.flush();
                buff.close();


                monkeyJdbcRealm.setSession("commodity_downLoad_percentage", "100%");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (buff != null) {
                        buff.close();
                    }
                    if (outSTr != null) {
                        outSTr.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        } else {

            monkeyJdbcRealm.setSession("commodity_downLoad_percentage", "-1");
        }

    }


    //===================private==========
    //生成goods对应的car
    private StringBuilder addCarToBuild(List<CommodityGoodsCarDO> carList) {
        StringBuilder resultBuild = new StringBuilder();
        for (CommodityGoodsCarDO carDO : carList) {
            resultBuild.append(carDO.getGoodsUuId());
            resultBuild.append("\t");
            resultBuild.append(carDO.getLiyangId());
            resultBuild.append("\r\n");
            percentage_add();
        }
        return resultBuild;
    }

    //处理商品导出的数据
    private List<HashMap<String, Object>> getDownGoodsMapList(List<CommodityGoodsDO> goodsList
    ) {
        List<HashMap<String, Object>> excle_list = new ArrayList<>();

        Multimap<String, Integer> onlineGoodsMap = goodsRedisManager.getGoodsListOfFormatBrand();

        for (CommodityGoodsDO goodsDO : goodsList) {
            String goodsName = goodsDO.getGoodsName();


            //品牌
            Integer bId = goodsDO.getBrandId();
            CommodityBrandDO brandDO = goodsRedisManager.getBrandByPrimaryKey(bId);
            if (brandDO == null) {
                continue;
            }
            String nameCh = brandDO.getNameCh().trim();
            String nameEn = brandDO.getNameEn().trim();

            String brandName = nameCh;
            if ("".equals(nameCh)) {
                brandName = nameEn;
            } else if (!"".equals(nameEn)) {
                brandName = nameCh + "/" + nameEn;
            }

            String format = goodsDO.getGoodsFormat();
            if (null != format) {
                format = format.replaceAll(" +", "");
            }
            String goodsCode = goodsDO.getGoodsCode();
            String saleUnit = goodsDO.getSaleUnit();
            String carType = "普通车";

            Integer catKindKey = goodsDO.getCateKind();
            String catKind = this.goods_carKind_map.get(catKindKey);

            //判断新增或更新
            String isNew = "更新";

            String key = brandName+"_"+format;
            Collection<Integer> onGoodsIds = onlineGoodsMap.get(key);
            if(null == onGoodsIds || onGoodsIds.isEmpty()){
                isNew = "新增";
            }

            String uuId = goodsDO.getUuId();
            List<CommodityGoodsOeDO> list = commodityGoodsOeService.getListByUuId(uuId);
            if (list.size() > 0) {
                for (CommodityGoodsOeDO goodsOeDO : list) {
                    String oe = goodsOeDO.getOeNumber();
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("isNew", isNew);
                    map.put("uuId", uuId);
                    map.put("goodsName", goodsName);
                    map.put("brandName", brandName);
                    map.put("format", format);
                    map.put("goodsCode", goodsCode);
                    map.put("OE", oe);
                    map.put("saleUnit", saleUnit);
                    map.put("carType", carType);
                    map.put("catKind", catKind);

                    excle_list.add(map);
                }
            } else {
                HashMap<String, Object> map = new HashMap<>();
                map.put("isNew", isNew);
                map.put("uuId", uuId);
                map.put("goodsName", goodsName);
                map.put("brandName", brandName);
                map.put("format", format);
                map.put("goodsCode", goodsCode);
                map.put("OE", "");
                map.put("saleUnit", saleUnit);
                map.put("carType", carType);
                map.put("catKind", catKind);

                excle_list.add(map);
            }

            percentage_add();
        }
//        dianDBHelp.close();

        return excle_list;
    }

    //伪造进程的百分比
    private void percentage_add() {
        BigDecimal b = new BigDecimal(commodity_downLoad_percentage);
        commodity_downLoad_percentage = b.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
        if (commodity_downLoad_percentage < 15.0) {
            commodity_downLoad_percentage = commodity_downLoad_percentage + 1;
        } else if (commodity_downLoad_percentage < 55.0) {
            commodity_downLoad_percentage = commodity_downLoad_percentage + 0.1;
        } else if (commodity_downLoad_percentage < 98.0) {
            commodity_downLoad_percentage = commodity_downLoad_percentage + 0.01;
        }
        monkeyJdbcRealm.setSession("commodity_downLoad_percentage", commodity_downLoad_percentage + "%");
    }

    //规定属性
    private HashMap<Integer, String> goods_carKind_map = new HashMap<Integer, String>() {
        {
            put(0, "普通件");
            put(1, "全车件");

        }
    };

}
