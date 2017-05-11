package com.tqmall.monkey.web.offerGoods;

import com.tqmall.athena.domain.result.carcategory.CarCategoryDTO;
import com.tqmall.core.common.entity.Result;
import com.tqmall.monkey.client.category.CategoryPartService;
import com.tqmall.monkey.client.offerGoods.OfferGoodsService;
import com.tqmall.monkey.client.offerGoods.OfferLiCarService;
import com.tqmall.monkey.client.offerGoods.OfferRecordService;
import com.tqmall.monkey.client.redisManager.car.CarRedisManager;
import com.tqmall.monkey.client.shiro.MonkeyJdbcRealm;
import com.tqmall.monkey.component.utils.PoiUtil;
import com.tqmall.monkey.component.utils.ZipUtil;
import com.tqmall.monkey.domain.entity.UserDO;
import com.tqmall.monkey.domain.entity.category.CategoryPartDO;
import com.tqmall.monkey.domain.entity.offerGoods.OfferGoodsDO;
import com.tqmall.monkey.domain.entity.offerGoods.OfferLiCarDO;
import com.tqmall.monkey.domain.entity.offerGoods.OfferRecordDO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 供应商数据导出模块--分类匹配、车型匹配、导入pool池、导出供应商excel
 * Created by zxg on 15/7/13.
 */
//@Transactional
@Controller
@RequestMapping("/monkey/offerGoods/dataOutput")
public class DataOutputController {
    Logger logger = LoggerFactory.getLogger(DataOutputController.class);

    //文件的保存位置
    @Value(value = "${file.address}")
    protected String bashFile;

    //获得当前登录用户信息
    @Autowired
    private MonkeyJdbcRealm monkeyJdbcRealm;
    @Autowired
    private OfferGoodsService offerGoodsService;
    @Autowired
    private OfferLiCarService offerLiCarService;
    @Autowired
    private OfferRecordService offerRecordService;
    @Autowired
    private CategoryPartService categoryPartService;
    @Autowired
    private CarRedisManager carRedisManager;


    private PoiUtil poiUtil = new PoiUtil();

    @RequestMapping(value = "index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("monkey/offerGoods/dataOutput");
        UserDO userDO = monkeyJdbcRealm.getCurrentUser();

        if (userDO == null) {
            return new ModelAndView("monkey/login");
        }

        return modelAndView;
    }

    /*============导出电商需要的excle=============*/

    //判断record中有无记录
    @RequestMapping(value = "/hasRecord", method = RequestMethod.GET)
    public
    @ResponseBody
    Boolean hasRecord() {
        Boolean result = true;

        Integer recordSum = offerRecordService.getRecordSum(OfferRecordDO.status_not_dev);
        if (recordSum.equals(0)) {
            result = false;
        }

        return result;
    }

    //导出电商需要的excel
    @RequestMapping(value = "/exportExcelForDianShang")
    public void exportExcelForDianShang(HttpServletResponse response) throws Exception {
        String[] headName = {
                "一级分类", "二级分类", "三级分类", "商品名称（标准化）", "商品名称（供应商）", "商品属性", "商品品牌", "商品类型",
                "规格型号", "OE码", "包装规格", "购买单位", "红包价", "城市"
        };
        String[] fieldName = {
                "firstCate", "secondCate", "thirdCate", "partName", "goodsName", "quality", "brand", "goodsType",
                "goodsFormate", "OE", "package_format", "measureUnit", "advicePrice", "city"
        };
        int[] columnWith = {
                5000, 5000, 5000, 7000, 7000, 5000, 5000, 5000,
                5000, 7000, 5000, 3000, 3000, 4000
        };
        int freezeCol = 1;
        int freezeRow = 1;

        //压缩包路径
        String zipName = "导入电商.zip";
        String base = bashFile + File.separator + File.separator + "fileDownload" + File.separator + "temp";
        File zipfile = ZipUtil.getZip(base, zipName);
        List<Workbook> bookList = new ArrayList<>();

        //供应商数据分批导出excel
        List<String> providerNameList = offerRecordService.findProviderNameByStatus(OfferRecordDO.status_not_dev);

        List<String> excelNameList = new ArrayList<>();
        for (String providerName : providerNameList) {
            List<HashMap<String, Object>> excle_list = new ArrayList<>();
            excelNameList.add(providerName + ".xls");

            List<OfferRecordDO> recordDOList = offerRecordService.findRecordListByGoodsIdAndStatus(null, OfferRecordDO.status_not_dev, providerName);
            //遍历record，拼接goods信息
            for (OfferRecordDO exportRecord : recordDOList) {
                HashMap<String, Object> exportMap = new HashMap<>();

                Integer offerGoodsId = exportRecord.getOfferGoodsId();

                OfferGoodsDO offerGoodsDO = offerGoodsService.selectById(offerGoodsId);
                //分类匹配还未匹配
                if (Objects.equals(offerGoodsDO.getCateStatus(), OfferGoodsDO.cate_status_new)) {
                    continue;
                }
                //oe码出错，该条记录被删除,分类失败
                if (!Objects.equals(offerGoodsDO.getOeIswrong(), OfferGoodsDO.oe_right) || !Objects.equals(offerGoodsDO.getIsdelete(), OfferGoodsDO.not_delete)
                        || !Objects.equals(offerGoodsDO.getCateStatus(), OfferGoodsDO.cate_status_success)) {
                    //更新record状态
                    exportRecord.setStatus(OfferRecordDO.status_fail);
                    offerRecordService.updateRecord(exportRecord);
                    continue;
                }
                Integer qualityType = offerGoodsDO.getGoodsQualityType();
                //筛选出原厂和品牌
                if (qualityType.equals(0) || qualityType.equals(1)) {
                    // 数据拼接主题
//                    String city = "杭州";
                    String city = exportRecord.getCity();
                    String quality = "";
                    switch (qualityType) {
                        case 0:
                            quality = "品牌";
                            break;
                        case 1:
                            quality = "正厂原厂";
                            break;
                    }
                    String goodsType = "";
                    Integer cateKind = offerGoodsDO.getCateKind();
                    switch (cateKind) {
                        case 0:
                            goodsType = "全车件";
                            break;
                        case 1:
                            goodsType = "易损件";
                            break;
                    }

                    exportMap.put("firstCate", offerGoodsDO.getFirstCateName());
                    exportMap.put("secondCate", offerGoodsDO.getSecondCateName());
                    String thirdCate = offerGoodsDO.getThirdCateName();
                    if (thirdCate == null || "".endsWith(thirdCate)) {
                        thirdCate = "无";
                    }
                    exportMap.put("thirdCate", thirdCate);
                    exportMap.put("partName", offerGoodsDO.getPartName());
                    exportMap.put("goodsName", offerGoodsDO.getGoodsName());
                    exportMap.put("quality", quality);
                    exportMap.put("brand", offerGoodsDO.getBrandName());
                    exportMap.put("goodsType", goodsType);
                    exportMap.put("goodsFormate", offerGoodsDO.getGoodsFormat());
                    exportMap.put("OE", offerGoodsDO.getOeNum());
                    exportMap.put("package_format", offerGoodsDO.getPackageFormat());
                    exportMap.put("measureUnit", offerGoodsDO.getMeasureUnit());
                    exportMap.put("advicePrice", exportRecord.getAdviceSalePrice());
                    exportMap.put("city", city);

                    excle_list.add(exportMap);
                }

                //更新record状态
                exportRecord.setStatus(OfferRecordDO.status_dev);
                offerRecordService.updateRecord(exportRecord);
            }

            HSSFWorkbook oneBook = poiUtil.exportExcelByHashTemplate(providerName, headName, fieldName, excle_list, columnWith, false, freezeCol, freezeRow);
            bookList.add(oneBook);
        }
        ZipUtil.zip(zipfile, bookList, excelNameList);
        ZipUtil.toWriteZip(response, zipfile, zipName);

    }


    /*==================================cate ====================================================*/

    //获得分类的未匹配和未导出数量
    @RequestMapping(value = "/get_cate_number", method = RequestMethod.GET)
    public
    @ResponseBody
    HashMap<String, Object> get_cate_number() {
        HashMap<String, Object> result_map = new HashMap<>();
        //获得未匹配的分类数
        Integer cate_new_sum = offerGoodsService.findGoodsSumByCateStatus(OfferGoodsDO.cate_status_new);
        Integer cate_not_excle_sum = offerGoodsService.findGoodsSumByCateStatus(OfferGoodsDO.cate_status_fail_not_excle);

        result_map.put("cate_new_sum", cate_new_sum);
        result_map.put("cate_not_excle_sum", cate_not_excle_sum);

        return result_map;
    }

    //开始匹配分类
    @RequestMapping(value = "/startMatchGoodsCate", method = RequestMethod.GET)
    public
    @ResponseBody
    Result startMatchGoodsCate() {
        UserDO userDO = monkeyJdbcRealm.getCurrentUser();
        if (null == userDO) {
            return Result.wrapErrorResult("001", "登录超时，请重新登录");
        }

        try {
            List<OfferGoodsDO> goodsDOList = offerGoodsService.findAllGoodsByCateStatus(OfferGoodsDO.cate_status_new);
            for (OfferGoodsDO offerGoodsDO : goodsDOList) {
                CategoryPartDO partDO = categoryPartService.findCategoryPartBySumCode(offerGoodsDO.getPartSumCode());

                if (partDO == null) {
                    //匹配失败
                    offerGoodsDO.setCateStatus(OfferGoodsDO.cate_status_fail_not_excle);
                    //找到记录，更新为失效的数据
                    List<OfferRecordDO> recordDOList = offerRecordService.findRecordListByGoodsIdAndStatus(offerGoodsDO.getId(), OfferRecordDO.status_not_dev, null);
                    for (OfferRecordDO offerRecordDO : recordDOList) {
                        offerRecordDO.setStatus(OfferRecordDO.status_fail);
                        offerRecordDO.setUpdateId(userDO.getId());

                        offerRecordService.updateRecord(offerRecordDO);
                    }
                } else {

                    offerGoodsDO.setPartId(partDO.getId());
                    offerGoodsDO.setPartName(partDO.getPartName());

                    offerGoodsDO.setThirdCateId(partDO.getThirdCatId());
                    offerGoodsDO.setThirdCateName(partDO.getThirdCatName());
                    offerGoodsDO.setSecondCateId(partDO.getSecondCatId());
                    offerGoodsDO.setSecondCateName(partDO.getSecondCatName());
                    offerGoodsDO.setFirstCateId(partDO.getFirstCatId());
                    offerGoodsDO.setFirstCateName(partDO.getFirstCatName());

                    offerGoodsDO.setCateStatus(OfferGoodsDO.cate_status_success);
                }
                offerGoodsService.updateOfferGoods(offerGoodsDO);

            }


            return Result.wrapSuccessfulResult("success");

        } catch (Exception e) {
            e.printStackTrace();
            return Result.wrapErrorResult("002", e.getMessage());
        }

    }

    //导出分类匹配失败的excel
    @RequestMapping(value = "/failCateToExportExcel")
    public void failDataToExportExcel(HttpServletResponse response) throws Exception {
        UserDO userDO = monkeyJdbcRealm.getCurrentUser();

        SimpleDateFormat data_formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String excelTitle = "分类匹配失败数据";
        String[] headName = {
                "上传的原始文件名", "供应商名称", "商品名称", "OE码", "标准零件编号", "零件代码", "导入时间", "更新时间"
        };
        String[] fieldName = {
                "recordName", "providerName", "goodsName", "oeNum", "partName", "partSumCode", "gmtCreate", "gmtModified"
        };
        int[] columnWith = {
                2000, 7000, 5000, 5000, 5000, 5000, 5000, 5000, 5000
        };
        int freezeCol = 1;
        int freezeRow = 1;

        List<HashMap<String, Object>> excle_list = new ArrayList<>();

        List<OfferGoodsDO> goodsDOList = offerGoodsService.findAllGoodsByCateStatus(OfferGoodsDO.cate_status_fail_not_excle);

        //所有的数据均设置已导出
        Integer userId = userDO.getId();
        //商品
        OfferGoodsDO updateGoodsDO = new OfferGoodsDO();
        updateGoodsDO.setUpdateId(userId);
        updateGoodsDO.setCateStatus(OfferGoodsDO.cate_status_fail_excle);
        //报价单
        OfferRecordDO updateRecordDO = new OfferRecordDO();
        updateRecordDO.setUpdateId(userId);
        updateRecordDO.setStatus(OfferRecordDO.status_fail);
        //单条记录


        for (OfferGoodsDO offerGoodsDO : goodsDOList) {
            //更新商品状态
            Integer goodsId = offerGoodsDO.getId();
            updateGoodsDO.setId(goodsId);
            offerGoodsService.updateOfferGoods(updateGoodsDO);

            List<OfferRecordDO> recordDOList = offerRecordService.findRecordListByGoodsIdAndStatus(goodsId, OfferRecordDO.status_not_dev, null);
            for (OfferRecordDO recordDO : recordDOList) {
                //更新报价单状态
                updateRecordDO.setId(recordDO.getId());
                offerRecordService.updateRecord(recordDO);
                //拼接数据
                HashMap<String, Object> excle_map = new HashMap<>();
                excle_map.put("goodsName", offerGoodsDO.getGoodsName());
                excle_map.put("oeNum", offerGoodsDO.getOeNum());
                excle_map.put("partName", offerGoodsDO.getPartName());
                excle_map.put("partSumCode", offerGoodsDO.getPartSumCode());

                excle_map.put("recordName", recordDO.getRecordName());
                excle_map.put("providerName", recordDO.getProviderName());
                excle_map.put("gmtCreate", data_formatter.format(recordDO.getGmtCreate()));
                excle_map.put("gmtModified", data_formatter.format(recordDO.getGmtModified()));
                excle_list.add(excle_map);
            }
        }

        poiUtil.exportExcelByHash(response, excelTitle, headName, fieldName, excle_list, columnWith, true, freezeCol, freezeRow);
    }

    /*==================================car ====================================================*/

    //获得车型的未匹配和未导出数量
    @RequestMapping(value = "/get_car_number", method = RequestMethod.GET)
    public
    @ResponseBody
    HashMap<String, Object> get_car_number() {
        HashMap<String, Object> result_map = new HashMap<>();
        //未匹配及未导出的车型数
        Integer car_new_sum = offerLiCarService.getCarsSumByStatus(OfferLiCarDO.status_新建);
        Integer car_not_excle_sum = offerLiCarService.getCarsSumByStatus(OfferLiCarDO.status_匹配失败未导出);

        result_map.put("car_new_sum", car_new_sum);
        result_map.put("car_not_excle_sum", car_not_excle_sum);

        return result_map;
    }

    //匹配车型
    @RequestMapping(value = "/startMatchCar", method = RequestMethod.GET)
    public
    @ResponseBody
    Result startMatchCar() {
        UserDO userDO = monkeyJdbcRealm.getCurrentUser();

        try {
            List<OfferLiCarDO> offerLiCarDOList = offerLiCarService.findOfferLiByGoodsIdLiIdStatus(null, null, OfferLiCarDO.status_新建);
            Set<String> liyangSet = new HashSet<>();
            // 将liyangid归类
            for (OfferLiCarDO liCarDO : offerLiCarDOList) {
                String liyangId = liCarDO.getLiId();
                liyangSet.add(liyangId);
            }

            Integer userId = userDO.getId();
            for (String liyangId : liyangSet) {
                OfferLiCarDO offerLiCarDO = null;
                CarCategoryDTO carCategoryDTO = carRedisManager.getOnlineCarByLiyangId(liyangId);
                if (carCategoryDTO != null) {
                    offerLiCarDO = new OfferLiCarDO();
                    Integer onlineId = carCategoryDTO.getId();
                    String brandName = carCategoryDTO.getBrand();
                    String companyName = carCategoryDTO.getCompany();
                    String seriesName = carCategoryDTO.getSeries();

                    Integer pid = carCategoryDTO.getPid();
                    String power = carCategoryDTO.getPower();
                    if (null == onlineId) {
                        offerLiCarDO = null;
                    } else {
                        offerLiCarDO.setOnlineCarId(onlineId);
                        if (null != brandName) {
                            offerLiCarDO.setOnlineBrand(brandName);
                        }
                        if (null != companyName) {
                            offerLiCarDO.setOnlineCompany(companyName);
                        }
                        if (null != seriesName) {
                            offerLiCarDO.setOnlineSeries(seriesName);
                        }
                        if (null != pid) {
                            offerLiCarDO.setOnlinePid(pid);
                        }
                        if (null != power) {
                            offerLiCarDO.setOnlinePower(power);
                        }
                    }
                }

                if (offerLiCarDO == null) {
                    //匹配失败
                    offerLiCarDO = new OfferLiCarDO();
                    offerLiCarDO.setStatus(OfferLiCarDO.status_匹配失败未导出);
                } else {
                    offerLiCarDO.setStatus(OfferLiCarDO.status_匹配成功);
                }
                offerLiCarDO.setUpdateId(userId);

                OfferLiCarDO existDO = new OfferLiCarDO();
                existDO.setLiId(liyangId);
                existDO.setStatus(OfferLiCarDO.status_新建);
                offerLiCarService.updateOfferLiCarDOByCustom(offerLiCarDO, existDO);
            }

            return Result.wrapSuccessfulResult("success");

        } catch (Exception e) {
            logger.debug("carMatch bug is:===" + e.getMessage());
            return Result.wrapErrorResult("001", e.getMessage());
        }

    }


}
