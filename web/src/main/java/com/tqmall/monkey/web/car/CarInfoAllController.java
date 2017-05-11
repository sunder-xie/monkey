package com.tqmall.monkey.web.car;

import com.tqmall.core.common.entity.Result;
import com.tqmall.monkey.client.car.CarInfoAllService;
import com.tqmall.monkey.client.shiro.MonkeyJdbcRealm;
import com.tqmall.monkey.component.utils.DateUtil;
import com.tqmall.monkey.component.utils.PoiUtil;
import com.tqmall.monkey.domain.commonBean.MonkeyCommonBean;
import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.UserDO;
import com.tqmall.monkey.domain.entity.car.CarInfoAllDO;
import com.tqmall.monkey.web.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * Created by huangzhangting on 15/7/22.
 */

@Slf4j
@Controller
@RequestMapping("/monkey/carInfoAll")
public class CarInfoAllController extends BaseController {

    @Autowired
    private CarInfoAllService carInfoAllService;

    //文件的保存位置
    @Value(value = "${file.address}")
    private String baseFilePath;

    //获得当前登录用户信息
    @Autowired
    private MonkeyJdbcRealm monkeyJdbcRealm;

    //首页--权限控制
    //@RequiresRoles(value={"data_operator","data_admin"}, logical= Logical.OR)
    @RequestMapping(value = "index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("monkey/carInfoAll/index");

        UserDO User = (UserDO) monkeyJdbcRealm.getSessionValue("currentUser");
        if (User == null) {
            return null;
        }

        return modelAndView;
    }

    @RequestMapping(value = "dataImport")
    public ModelAndView dataImport() {
        ModelAndView modelAndView = new ModelAndView("monkey/carInfoAll/dataImport");

        UserDO User = (UserDO) monkeyJdbcRealm.getSessionValue("currentUser");
        if (User == null) {
            return null;
        }

        return modelAndView;
    }

    @RequestMapping(value = "/toGetCarBrands")
    @ResponseBody
    public Result toGetCarBrands() throws Exception {
        List<String> brandList = this.carInfoAllService.findCarBrands();

        return Result.wrapSuccessfulResult(brandList);
    }

    @RequestMapping(value = "/toGetFactoryNames")
    @ResponseBody
    public Result toGetFactoryNames(@RequestParam String carBrand) throws Exception {
        List<String> factoryList = this.carInfoAllService.findFactoryNamesByBrand(carBrand);

        return Result.wrapSuccessfulResult(factoryList);
    }

    @RequestMapping(value = "/toGetCarSeries")
    @ResponseBody
    public Result toGetCarSeries(@RequestParam String carBrand, @RequestParam String factoryName) throws Exception {
        List<String> seriesList = this.carInfoAllService.findCarSeriesByBrand(carBrand, factoryName);

        return Result.wrapSuccessfulResult(seriesList);
    }

    @RequestMapping(value = "/toGetVehicleTypes")
    @ResponseBody
    public Result toGetVehicleTypes(@RequestParam String carBrand, @RequestParam String factoryName,
                                    @RequestParam String carSeries) throws Exception {
        List<String> list = this.carInfoAllService.findVehicleTypesBySeries(carBrand, factoryName, carSeries);

        return Result.wrapSuccessfulResult(list);
    }

    @RequestMapping(value = "/toSearchCarInfos")
    @ResponseBody
    public Map toSearchCarInfos(@RequestParam String carBrand, @RequestParam String factoryName,
                                @RequestParam String carSeries, @RequestParam String vehicleType,
                                @RequestParam int pageIndex) throws Exception {
        int pageSize = 30;

        Page<CarInfoAllDO> page = this.carInfoAllService.findCarInfoPage(carBrand, factoryName, carSeries, vehicleType,
                pageIndex, pageSize);

        Map<String, Object> resultMap = new HashMap<>();
        if (page == null) {
            resultMap.put("data", Collections.emptyList());
            resultMap.put("totalRows", 0);
            resultMap.put("totalPages", 0);
        } else {
            resultMap.put("data", page.getItems());
            resultMap.put("totalRows", page.getTotalNumber());
            resultMap.put("totalPages", page.getTotalPage());
        }

        return resultMap;
    }

    @RequestMapping(value = "/toSearchCarInfosByVehicleType")
    @ResponseBody
    public Map toSearchCarInfosByVehicleType(String carBrand, String factoryName, String carSeries,
                                             @RequestParam String vehicleType, @RequestParam int pageIndex) throws Exception {
        int pageSize = 30;

        Page<CarInfoAllDO> page = this.carInfoAllService
                .findCarInfoPageByVehicleType(carBrand, factoryName, carSeries, vehicleType, pageIndex, pageSize);

        Map<String, Object> resultMap = new HashMap<>();
        if (page == null) {
            resultMap.put("data", Collections.emptyList());
            resultMap.put("totalRows", 0);
            resultMap.put("totalPages", 0);
        } else {
            resultMap.put("data", page.getItems());
            resultMap.put("totalRows", page.getTotalNumber());
            resultMap.put("totalPages", page.getTotalPage());
        }

        return resultMap;
    }

    @RequestMapping(value = "/toGetCarInfoById")
    @ResponseBody
    public Result toGetCarInfoById(@RequestParam Integer id) throws Exception {
        CarInfoAllDO carInfoAll = this.carInfoAllService.findCarInfoById(id);
        return Result.wrapSuccessfulResult(carInfoAll);
    }

/*
    @RequestMapping(value = "/toGetCarInfos")
    @ResponseBody
    public Result toGetCarInfos(String carBrand, String factoryName, String carSeries, String vehicleType) throws Exception {

        List<CarInfoAllDO> list = this.carInfoAllService.findCarInfoAll(carBrand, factoryName, carSeries, vehicleType);

        return Result.wrapSuccessfulResult(list);
    }
*/


    /*
    *  导出excel
    */
    @RequestMapping(value = "/toExportExcel")
    public void toExportExcel(HttpServletResponse response, String carBrand, String factoryName, String carSeries,
                              @RequestParam String vehicleType, @RequestParam Integer type) throws Exception {
        String version = this.carInfoAllService.getVehicleTypeExportExcelVersion();
        String excelTitle = factoryName+vehicleType+"-V"+version;

        String[] headName = {
                "力洋ID", "厂家", "品牌", "车系", "车型", "销售名称", "年款", "排放标准", "车辆类型", "车辆级别"
                , "指导价格(万元)", "上市年份", "上市月份", "生产年份", "停产年份", "生产状态", "销售状态", "国别", "国产合资进口", "气缸容积"
                , "排量(升)", "进气形式", "燃料类型", "燃油标号", "最大马力(ps)", "最大功率(kw)", "最大功率转速(rpm)", "最大扭矩(N·m)", "最大扭矩转速(rpm)", "气缸排列形式"
                , "气缸数(个)", "每缸气门数(个)", "压缩比", "供油方式", "工信部综合油耗", "市区工况油耗", "市郊工况油耗", "加速时间(0-100km/h)", "最高车速", "发动机特有技术"
                , "三元催化器", "冷却方式", "缸径", "行程", "发动机描述", "变速器类型", "变速器描述", "档位数", "前制动器类型", "后制动器类型"
                , "前悬挂类型", "后悬挂类型", "转向机形式", "助力类型", "最小离地间隙", "最小转弯半径", "离去角", "接近角", "发动机位置", "驱动方式"
                , "驱动形式", "车身型式", "长度(mm)", "宽度(mm)", "高度(mm)", "轴距(mm)", "前轮距(mm)", "后轮距(mm)", "整备质量(kg)", "最大载重质量(kg)"
                , "油箱容积(L)", "行李厢容积(L)", "车顶型式", "车篷型式", "车门数", "座位数", "前轮胎规格", "后轮胎规格", "前轮毂规格", "后轮毂规格"
                , "轮毂材料", "备胎规格", "电动天窗", "全景天窗", "氙气大灯", "前雾灯", "后雨刷", "空调", "自动空调"
        };

        String[] fieldName = {
                "leyelId", "factoryName", "carBrand", "carSeries", "vehicleType", "marketName", "modelYear", "envStandard", "carType", "carLevel"
                , "guidePrice", "publicYear", "publicMonth", "createYear", "stopYear", "productionStatus", "marketStatus", "productionCountry", "productionType", "cylinderCapacity"
                , "displacement", "intakeStyle", "fuelType", "fuelFlag", "maxHorsepower", "maxPower", "maxPowerSpeed", "maxTorque", "maxTorqueSpeed", "cylinderStyle"
                , "cylinderNum", "valvePerCylinder", "compressionRatio", "fuelWay", "fuelConsumptionAverage", "fuelConsumptionDowntown", "fuelConsumptionSuburbs", "accelerationTime", "topSpeed", "engineUniqueTech"
                , "catalyticConverter", "coolStyle", "bore", "stroke", "engineDesc", "transmissionType", "transmissionDesc", "stallNum", "frontBrakeType", "backBrakeType"
                , "frontSuspensionNum", "rearSuspensionType", "steeringStyle", "boosterType", "minClearance", "minTurningRadius", "departureAngle", "approachAngle", "enginePosition", "driveWay"
                , "driveStyle", "bodyStyle", "length", "width", "height", "wheelbase", "frontTread", "rearTread", "curbWeight", "fullyLoadedWeight"
                , "tankCapacity", "trunkCapacity", "roofType", "hoodType", "doorNum", "seatNum", "frontTireStyle", "rearTireStyle", "frontWheelStyle", "rearWheelStyle"
                , "wheelMaterial", "spareTireStyle", "electronicSkylights", "panoramicSunroof", "xenonLamp", "frontFogLamp", "backWindshielWiper", "airConditioning", "autoAirConditioning"
        };

        int[] columnWith = {
                4500, 7000, 4000, 4000, 4000, 8000, 4000, 4000, 4000, 4000,
                4000, 3000, 3000, 3000, 3000, 3000, 3000, 3000, 3000, 4000,
                3000, 4000, 4000, 3000, 4000, 4000, 4000, 4000, 4000, 4000,
                4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000,
                4000, 4000, 4000, 4000, 8000, 4000, 4000, 4000, 4000, 4000,
                4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000,
                4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000,
                4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000,
                4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000
        };

        int freezeCol = 0;
        int freezeRow = 2;

        List<CarInfoAllDO> dataList = null;
        if (type == 1) {
            dataList = this.carInfoAllService.findCarInfosByVehicleType(carBrand, factoryName, carSeries, vehicleType);
        } else {
            dataList = this.carInfoAllService.findCarInfos(carBrand, factoryName, carSeries, vehicleType);
        }

        PoiUtil poiUtil = new PoiUtil();

        poiUtil.exportExcelCommon(response, excelTitle, headName, fieldName, dataList, columnWith, false, freezeCol, freezeRow);

    }

    /*
    *  导出excel，全部数据
    */
    /*
    @RequestMapping(value = "/toExportExcelAll")
    public void toExportExcelAll(HttpServletResponse response) throws Exception{
        String excelTitle = "车型信息";
        String[] headName = {
                "力洋ID","厂家","品牌","车系","车型","销售名称","年款","排放标准","车辆类型","车辆级别"
                ,"指导价格(万元)","上市年份","上市月份","生产年份","停产年份","生产状态","销售状态","国别","国产合资进口","气缸容积"
                ,"排量(升)","进气形式","燃料类型","燃油标号","最大马力(ps)","最大功率(kw)","最大功率转速(rpm)","最大扭矩(N·m)","最大扭矩转速(rpm)","气缸排列形式"
                ,"气缸数(个)","每缸气门数(个)","压缩比","供油方式","工信部综合油耗","市区工况油耗","市郊工况油耗","加速时间(0-100km/h)","最高车速","发动机特有技术"
                ,"三元催化器","冷却方式","缸径","行程","发动机描述","变速器类型","变速器描述","档位数","前制动器类型","后制动器类型"
                ,"前悬挂类型","后悬挂类型","转向机形式","助力类型","最小离地间隙","最小转弯半径","离去角","接近角","发动机位置","驱动方式"
                ,"驱动形式","车身型式","长度(mm)","宽度(mm)","高度(mm)","轴距(mm)","前轮距(mm)","后轮距(mm)","整备质量(kg)","最大载重质量(kg)"
                ,"油箱容积(L)","行李厢容积(L)","车顶型式","车篷型式","车门数","座位数","前轮胎规格","后轮胎规格","前轮毂规格","后轮毂规格"
                ,"轮毂材料","备胎规格","电动天窗","全景天窗","氙气大灯","前雾灯","后雨刷","空调","自动空调"
        };

        String[] fieldName = {
                "leyelId","factoryName","carBrand","carSeries","vehicleType","marketName","modelYear","envStandard","carType","carLevel"
                ,"guidePrice","publicYear","publicMonth","createYear","stopYear","productionStatus","marketStatus","productionCountry","productionType","cylinderCapacity"
                ,"displacement","intakeStyle","fuelType","fuelFlag","maxHorsepower","maxPower","maxPowerSpeed","maxTorque","maxTorqueSpeed","cylinderStyle"
                ,"cylinderNum","valvePerCylinder","compressionRatio","fuelWay","fuelConsumptionAverage","fuelConsumptionDowntown","fuelConsumptionSuburbs","accelerationTime","topSpeed","engineUniqueTech"
                ,"catalyticConverter","coolStyle","bore","stroke","engineDesc","transmissionType","transmissionDesc","stallNum","frontBrakeType","backBrakeType"
                ,"frontSuspensionNum","rearSuspensionType","steeringStyle","boosterType","minClearance","minTurningRadius","departureAngle","approachAngle","enginePosition","driveWay"
                ,"driveStyle","bodyStyle","length","width","height","wheelbase","frontTread","rearTread","curbWeight","fullyLoadedWeight"
                ,"tankCapacity","trunkCapacity","roofType","hoodType","doorNum","seatNum","frontTireStyle","rearTireStyle","frontWheelStyle","rearWheelStyle"
                ,"wheelMaterial","spareTireStyle","electronicSkylights","panoramicSunroof","xenonLamp","frontFogLamp","backWindshielWiper","airConditioning","autoAirConditioning"
        };

        int[] columnWith = {
                4500, 7000, 4000, 4000, 4000, 8000, 4000, 4000, 4000, 4000,
                4000, 3000, 3000, 3000, 3000, 3000, 3000, 3000, 3000, 4000,
                3000, 4000, 4000, 3000, 4000, 4000, 4000, 4000, 4000, 4000,
                4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000,
                4000, 4000, 4000, 4000, 8000, 4000, 4000, 4000, 4000, 4000,
                4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000,
                4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000,
                4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000,
                4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000
        };

        int freezeCol = 0;
        int freezeRow = 2;

        List<CarInfoAllDO> dataList = ;

        PoiUtil poiUtil = new PoiUtil();

        poiUtil.exportExcelCommon(response, excelTitle, headName, fieldName, dataList, columnWith, false, freezeCol, freezeRow);

    }
    */

    @RequestMapping(value = "/toUploadExcle", method = RequestMethod.POST)
    public
    @ResponseBody
    HashMap<String, Object> toUploadExcle(MultipartHttpServletRequest request, HttpServletResponse response)
            throws Exception {

        MultipartHttpServletRequest multipartRequest = request;
        MultipartFile multipartFile = multipartRequest.getFile("excle_file");
        if (null == multipartFile) {
            return null;
        }
        String sourceName = multipartFile.getOriginalFilename(); // 原始文件名
        //String fileType = sourceName.substring(sourceName.lastIndexOf("."));

        String timeStr = DateUtil.dateToString(new Date(), DateUtil.DateFormat_yyyy_MM_dd);
        StringBuffer basePath = new StringBuffer();
        basePath.append(baseFilePath).append(File.separator).append("fileUpload")
                .append(File.separator).append("carInfo").append(File.separator).append(timeStr);
        File file = new File(basePath.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName = basePath.toString() + File.separator + sourceName;
        try {
            multipartFile.transferTo(new File(fileName));

            monkeyJdbcRealm.setSession("carInfo_upload_percent", "20%");

            //开始读取excle中文件进行逻辑处理
            //this.carInfoAllService.importDataByExcel(fileName);

        } catch (Exception e) {
            monkeyJdbcRealm.setSession("carInfo_upload_percent", "-1");
            e.printStackTrace();
            return null;
        }

        monkeyJdbcRealm.setSession("carInfo_upload_percent", "100%");

        return null;
    }

    //动态获得文件上传处理进度
    @RequestMapping(value = "/toGetProgress")
    public
    @ResponseBody
    HashMap<String, Object> toGetProgress() {
        HashMap<String, Object> resultMap = new HashMap<>();

        String progress = monkeyJdbcRealm.getSessionValue("carInfo_upload_percent").toString();
        if (progress == null) {
            progress = "0%";
            monkeyJdbcRealm.setSession("carInfo_upload_percent", progress);
        }
        resultMap.put("progress", progress);

        return resultMap;
    }

    /* 查询车型保养方案适配车款 */
    @RequestMapping(value = "/toGetCarInfoForMaintain", method = RequestMethod.POST)
    @ResponseBody
    public Map toGetCarInfoForMaintain(@RequestParam String carBrand, @RequestParam String factoryName,
                                       @RequestParam String carSeries, @RequestParam String vehicleType,
                                       @RequestParam int maintainPlanId) throws Exception {

        Map<String, Object> resultMap = new HashMap<>();
        //适配车款
        List<CarInfoAllDO> carInfoList = this.carInfoAllService.findCarInfoForMaintain(maintainPlanId);
        Set<String> carInfoYear;
        Set<String> carInfoPower;
        Set<String> fuelTypeSet = new HashSet<>();
        Map<String, Set<String>> carInfoYearMap = new TreeMap<>(new MapKeyComparator());
        Map<String, Set<String>> carInfoPowerMap = new TreeMap<>(new MapKeyComparator());
        String power;
        String year;
        String fuelType;
        for (CarInfoAllDO obj1 : carInfoList) {
            year = obj1.getModelYear() == null ? "" : obj1.getModelYear().trim();
            obj1.setModelYear(year);

            power = obj1.getDisplacement();
            if (power == null || "".equals(power = power.trim())) {
                power = "电动";
            }
            obj1.setDisplacement(power);

            carInfoPower = carInfoYearMap.get(year);
            if (carInfoPower == null) {
                carInfoPower = new TreeSet<>(new MapKeyComparator());
                carInfoYearMap.put(year, carInfoPower);
            }
            carInfoPower.add(power);

            carInfoYear = carInfoPowerMap.get(power);
            if (carInfoYear == null) {
                carInfoYear = new TreeSet<>(new MapKeyComparator());
                carInfoPowerMap.put(power, carInfoYear);
            }
            carInfoYear.add(year);

            if (obj1.getFuelType() == null) {
                fuelType = "未知";
            } else {
                fuelType = obj1.getFuelType().trim();
                if ("".equals(fuelType)) {
                    fuelType = "未知";
                }
            }
            fuelTypeSet.add(fuelType);
            obj1.setFuelType(fuelType);
        }
        resultMap.put("carInfoYear", carInfoYearMap);
        resultMap.put("carInfoPower", carInfoPowerMap);
        resultMap.put("carInfoList", carInfoList);
        resultMap.put("fuelTypeSet", fuelTypeSet);

        //没配置保养方案的车款
        List<CarInfoAllDO> notRelatedCarInfoList = this.carInfoAllService
                .findNotRelatedCarInfoForMaintain(carBrand, factoryName, carSeries, vehicleType);
        Set<String> notRelatedYear;
        Set<String> notRelatedPower;
        Set<String> nrFuelTypeSet = new HashSet<>();
        Map<String, Set<String>> notRelatedYearMap = new TreeMap<>(new MapKeyComparator());
        Map<String, Set<String>> notRelatedPowerMap = new TreeMap<>(new MapKeyComparator());
        for (CarInfoAllDO obj2 : notRelatedCarInfoList) {
            year = obj2.getModelYear() == null ? "" : obj2.getModelYear().trim();
            obj2.setModelYear(year);

            power = obj2.getDisplacement();
            if (power == null || "".equals(power = power.trim())) {
                power = "电动";
            }
            obj2.setDisplacement(power);

            notRelatedPower = notRelatedYearMap.get(year);
            if (notRelatedPower == null) {
                notRelatedPower = new TreeSet<>(new MapKeyComparator());
                notRelatedYearMap.put(year, notRelatedPower);
            }
            notRelatedPower.add(power);

            notRelatedYear = notRelatedPowerMap.get(power);
            if (notRelatedYear == null) {
                notRelatedYear = new TreeSet<>(new MapKeyComparator());
                notRelatedPowerMap.put(power, notRelatedYear);
            }
            notRelatedYear.add(year);

            if (obj2.getFuelType() == null) {
                fuelType = "未知";
            } else {
                fuelType = obj2.getFuelType().trim();
                if ("".equals(fuelType)) {
                    fuelType = "未知";
                }
            }
            nrFuelTypeSet.add(fuelType);
            obj2.setFuelType(fuelType);
        }
        resultMap.put("notRelatedYear", notRelatedYearMap);
        resultMap.put("notRelatedPower", notRelatedPowerMap);
        resultMap.put("notRelatedCarInfoList", notRelatedCarInfoList);
        resultMap.put("nrFuelTypeSet", nrFuelTypeSet);

        return resultMap;
    }

    //map按key升序，key为String类型
    private class MapKeyComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    }

    //--------START 力洋车型数据导出到excel和版本号 相关的请求处理   BY LYJ--------//
    //更新导出excel的版本号
    @RequestMapping(value = "/refreshVehicleTypeExportExcelVersion", method = RequestMethod.POST)
    @ResponseBody
    public Result refreshVehicleTypeExportExcelVersion() throws Exception {
        if (MonkeyCommonBean.CREATE_LIYANG_EXCEL_FLAG == MonkeyCommonBean.CREATING_EXCEL) {
            return Result.wrapErrorResult("1", "正在生成excel文件，需要几分钟时间，请稍等");
        }
        String version = carInfoAllService.refreshVehicleTypeExportExcelVersion();
        return Result.wrapSuccessfulResult("版本号更新成功", version);
    }

    //获取导出excel的版本号
    @RequestMapping(value = "/getVehicleTypeExportExcelVersion", method = RequestMethod.GET)
    @ResponseBody
    public Result getVehicleTypeExportExcelVersion() throws Exception {
        String version = this.carInfoAllService.getVehicleTypeExportExcelVersion();
        return Result.wrapSuccessfulResult(version);
    }

    //删除导出excel的版本号
    @RequestMapping(value = "/deleteVehicleTypeExportExcelVersion", method = RequestMethod.DELETE)
    @ResponseBody
    public Result deleteVehicleTypeExportExcelVersion() throws Exception {
        String version = this.carInfoAllService.deleteVehicleTypeExportExcelVersion();
        return Result.wrapSuccessfulResult(version);
    }

    /**
     * 生成力洋车型全部数据excel
     * @return
     */
    @RequestMapping(value = "/createAllDataExcel", method = RequestMethod.GET)
    @ResponseBody
    public Result createAllDataExcel() {
        if (MonkeyCommonBean.CREATE_LIYANG_EXCEL_FLAG == MonkeyCommonBean.CREATING_EXCEL) {
            return Result.wrapErrorResult("1", "正在生成excel文件，需要几分钟时间，请稍等");
        }
        try {
            String version = carInfoAllService.createVehicleTypeAllDataExcel();
            return Result.wrapSuccessfulResult("开始生成excel文件，需要几分钟时间，请稍等", version);
        }catch (Exception e){
            MonkeyCommonBean.CREATE_LIYANG_EXCEL_FLAG = MonkeyCommonBean.CREATE_EXCEL_TYPE_NEW;
            log.error("create all li yang data excel error", e);
            return Result.wrapErrorResult("0", "系统内部发生错误");
        }
    }

    @RequestMapping(value = "/queryExportLyAllDataFlag", method = RequestMethod.GET)
    @ResponseBody
    public Result queryExportLyAllDataFlag() {
        if (MonkeyCommonBean.CREATE_LIYANG_EXCEL_FLAG == MonkeyCommonBean.CREATING_EXCEL) {
            return Result.wrapErrorResult("1", "正在生成excel文件，需要几分钟时间，请稍后再下载");
        }
        if(MonkeyCommonBean.CREATE_LIYANG_EXCEL_FLAG == MonkeyCommonBean.CREATING_EXCEL_ERROR){
            return Result.wrapErrorResult("3", "力洋车型数据excel生成失败，请重新生成或者联系开发童鞋");
        }

        File filePath = new File(baseFilePath);
        if(!filePath.isDirectory()){
            return Result.wrapErrorResult("3", "力洋车型数据excel生成失败，请重新生成或者联系开发童鞋");
        }

        String version = carInfoAllService.getVehicleTypeExportExcelVersion();
        String excel = baseFilePath + "/" + MonkeyCommonBean.EXPORT_LIYANG_VEHICLE_TYPE_EXCEL_NAME + version + MonkeyCommonBean.EXCEL_2007;

        File file = new File(excel);
        if(!file.exists()){
            return Result.wrapErrorResult("2", "力洋车型数据excel不存在, 请重新生成或者联系开发童鞋");
        }

        return Result.wrapSuccessfulResult("");
    }

    //下载力洋车型数据excel
    @RequestMapping("downloadLiYangDataExcel")
    public void downloadLiYangDataExcel(){
        OutputStream os = null;
        InputStream is = null;
        try {
            String version = carInfoAllService.getVehicleTypeExportExcelVersion();
            String excelName = MonkeyCommonBean.EXPORT_LIYANG_VEHICLE_TYPE_EXCEL_NAME + version;
            String excel = baseFilePath + "/" + excelName + MonkeyCommonBean.EXCEL_2007;
            File file = new File(excel);
            if(!file.exists()){
                log.info("li yang data excel not exists, excel:{}", excel);
                return;
            }

            response.reset();// 清空输出流
            response.setHeader("Content-disposition", "attachment; filename=" + new String(excelName.getBytes(), "ISO-8859-1") + ".xlsx");// 设定输出文件头
            response.setContentType("application/x-download;");
            os = response.getOutputStream();
            is = new FileInputStream(excel);
            int len;
            byte[] buffer = new byte[1024];
            while((len = is.read(buffer)) > 0) {
                os.write(buffer, 0, len);
            }
            os.flush();

        } catch (IOException e) {
            log.error("download li yang data excel error", e);
        }finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(os != null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    //--------END 力洋车型数据导出到excel和版本号 相关的请求处理   BY LYJ--------//

}

