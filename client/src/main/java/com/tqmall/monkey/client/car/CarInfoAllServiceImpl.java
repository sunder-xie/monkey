package com.tqmall.monkey.client.car;

import com.tqmall.monkey.client.excle.CarInfoExcelUtil;
import com.tqmall.monkey.client.redisManager.car.CarRedisManager;
import com.tqmall.monkey.component.excelutils.ExcelUtil;
import com.tqmall.monkey.component.excelutils.ReadExcelByEventUserModel;
import com.tqmall.monkey.component.excelutils.ReadExcelXLS;
import com.tqmall.monkey.dal.dao.car.CarInfoAllDao;
import com.tqmall.monkey.domain.commonBean.MonkeyCommonBean;
import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.car.CarInfoAllDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangzhangting on 15/7/22.
 */
@Slf4j
@Service
public class CarInfoAllServiceImpl implements CarInfoAllService {
    //文件的保存位置
    @Value(value = "${file.address}")
    private String baseFilePath;

    @Autowired
    private CarInfoAllDao carInfoAllDao;

    @Autowired
    private CarRedisManager carRedisManager;

    @Override
    public List<String> findCarBrands() {
        return carRedisManager.findCarBrands();
    }

    @Override
    public List<String> findFactoryNamesByBrand(String carBrand) {

        return carRedisManager.findFactoryNamesByBrand(carBrand);
    }

    @Override
    public List<String> findCarSeriesByBrand(String carBrand, String factoryName) {

        return carRedisManager.findCarSeriesByBrand(carBrand, factoryName);
    }

    @Override
    public List<String> findVehicleTypesBySeries(String carBrand, String factoryName, String carSeries) {

        return carRedisManager.findVehicleTypesBySeries(carBrand, factoryName, carSeries);
    }

    @Override
    public Page<CarInfoAllDO> findCarInfoPage(String carBrand, String factoryName, String carSeries,
                                              String vehicleType, int pageIndex, int pageSize) {
        if (pageIndex < 1) {
            pageIndex = 1;
        }
        if (pageSize < 0) {
            pageIndex = 10;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("carBrand", carBrand);
        params.put("factoryName", factoryName);
        params.put("carSeries", carSeries);
        params.put("vehicleType", vehicleType);

        return carInfoAllDao.getCarInfoPage(params, pageIndex, pageSize);
    }

    @Override
    public Page<CarInfoAllDO> findCarInfoPageByVehicleType(String carBrand, String factoryName, String carSeries,
                                                           String vehicleType, int pageIndex, int pageSize) {
        if (pageIndex < 1) {
            pageIndex = 1;
        }
        if (pageSize < 0) {
            pageIndex = 10;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("vehicleType", "%" + vehicleType + "%");

        if (carBrand != null && !"".equals(carBrand)) {
            params.put("carBrand", carBrand);
        }
        if (factoryName != null && !"".equals(factoryName)) {
            params.put("factoryName", factoryName);
        }
        if (carSeries != null && !"".equals(carSeries)) {
            params.put("carSeries", carSeries);
        }

        return carInfoAllDao.getCarInfoPageByVehicleType(params, pageIndex, pageSize);
    }

    @Override
    public List<CarInfoAllDO> findCarInfos(String carBrand, String factoryName, String carSeries, String vehicleType) {
        Map<String, Object> params = new HashMap<>();
        params.put("carBrand", carBrand);
        params.put("factoryName", factoryName);
        params.put("carSeries", carSeries);
        params.put("vehicleType", vehicleType);

        return carInfoAllDao.getCarInfoAllMapper().getCarInfos(params);
    }

    @Override
    public List<CarInfoAllDO> findCarInfosByVehicleType(String carBrand, String factoryName, String carSeries, String vehicleType) {
        Map<String, Object> params = new HashMap<>();
        params.put("vehicleType", "%" + vehicleType + "%");

        if (carBrand != null && !"".equals(carBrand)) {
            params.put("carBrand", carBrand);
        }
        if (factoryName != null && !"".equals(factoryName)) {
            params.put("factoryName", factoryName);
        }
        if (carSeries != null && !"".equals(carSeries)) {
            params.put("carSeries", carSeries);
        }

        return carInfoAllDao.getCarInfoAllMapper().getCarInfosByVehicleType(params);
    }

    @Override
    public CarInfoAllDO findCarInfoById(Integer id) {
        return carInfoAllDao.getCarInfoAllMapper().selectByPrimaryKey(id);
    }

    @Override
    public CarInfoAllDO findCarInfoByLeyelId(String leyelId) {
        return carInfoAllDao.getCarInfoAllMapper().selectByLeyelId(leyelId);
    }

    @Override
    public boolean existLeyelId(String leyelId) {
        return carInfoAllDao.getCarInfoAllMapper().existLeyelId(leyelId);
    }

    @Override
    public boolean importDataByExcel(String fileName) throws Exception {
        if (fileName == null || "".equals(fileName)) {
            return false;
        }
        File file = new File(fileName);
        if (file.exists()) {
            if (file.isDirectory()) {
                return false;
            }

            String ext = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            boolean flag = false;
            switch (ext) {
                case "xlsx":
                    ReadExcelByEventUserModel excelUtilXLSX = new importDataByXLSX();
                    excelUtilXLSX.processOneSheet(fileName, 1);
                    flag = true;
                    break;
                case "xls":
                    ReadExcelXLS excelUtilXLS = new importDataByXLS();
                    excelUtilXLS.process(fileName);
                    flag = true;
                    break;
                default:
                    break;
            }

            return flag;
        }

        return false;
    }

    //处理xls数据
    private class importDataByXLS extends ReadExcelXLS {
        @Override
        public void operateRows(int sheetIndex, int curRow, List<String> rowList) throws Exception {
            if (sheetIndex == 0) {
                System.out.println(curRow + " " + rowList.size() + " " + rowList.toString());
            }
        }
    }

    //处理xlsx数据
    private class importDataByXLSX extends ReadExcelByEventUserModel {
        @Override
        public void operateRows(int sheetIndex, int curRow, List<String> rowlist) throws Exception {
            if (sheetIndex == 0) {
                System.out.println(curRow + " " + rowlist.size() + " " + rowlist.toString());
            }
        }
    }

    @Override
    public List<CarInfoAllDO> findCarInfoAll(String carBrand, String factoryName, String carSeries, String vehicleType) {
        Map<String, Object> params = new HashMap<>();
        if (vehicleType != null && !"".equals(vehicleType)) {
            params.put("vehicleType", vehicleType);
        }
        if (carBrand != null && !"".equals(carBrand)) {
            params.put("carBrand", carBrand);
        }
        if (factoryName != null && !"".equals(factoryName)) {
            params.put("factoryName", factoryName);
        }
        if (carSeries != null && !"".equals(carSeries)) {
            params.put("carSeries", carSeries);
        }

        return carInfoAllDao.getCarInfoAllMapper().getCarInfoAll(params);
    }

    @Override
    public List<CarInfoAllDO> findCarInfoForMaintain(Integer maintainPlanId) {
        return carInfoAllDao.getCarInfoAllMapper().getCarInfoForMaintain(maintainPlanId);
    }

    @Override
    public List<CarInfoAllDO> findNotRelatedCarInfoForMaintain(String carBrand, String factoryName, String carSeries, String vehicleType) {
        Map<String, Object> params = new HashMap<>();
        params.put("carBrand", carBrand);
        params.put("factoryName", factoryName);
        params.put("carSeries", carSeries);
        params.put("vehicleType", vehicleType);

        return carInfoAllDao.getCarInfoAllMapper().getNotRelatedCarInfoForMaintain(params);
    }

    //--------START 力洋车型数据导出到excel和版本号 相关的业务实现   BY LYJ--------//
    @Override
    public String refreshVehicleTypeExportExcelVersion() {
        String version = carRedisManager.updateVehicleTypeExportExcelVersion();

//        startThreadToCreateLiyangVTAllDataExcel(version, MonkeyCommonBean.CREATE_EXCEL_TYPE_NEW);

        return version;
    }

    @Override
    public String getVehicleTypeExportExcelVersion() {
        return carRedisManager.selectVehicleTypeExportExcelVersion();
    }

    @Override
    public String deleteVehicleTypeExportExcelVersion() {
        return carRedisManager.deleteVehicleTypeExportExcelVersion();
    }

    @Override
    public String createVehicleTypeAllDataExcel() {
        String version = carRedisManager.selectVehicleTypeExportExcelVersion();

        startThreadToCreateLiyangVTAllDataExcel(version, MonkeyCommonBean.CREATE_EXCEL_TYPE_RE);

        return version;
    }

    public void startThreadToCreateLiyangVTAllDataExcel(String version, int type) {

        //判断excel文件路径
        File filePath = new File(baseFilePath);
        if(!filePath.exists()){
            if(!filePath.mkdirs()){
                log.info("创建文件路径失败, filePath:{}", baseFilePath);
                return;
            }
        }

        //全局变量CREATE_LIYANG_EXCEL_FLAG, 设置为-1, 表示正在生成文件
        MonkeyCommonBean.CREATE_LIYANG_EXCEL_FLAG = MonkeyCommonBean.CREATING_EXCEL;

        log.info("力洋车型数据excel路径:{}", baseFilePath);

        ExportLiyangVTDataThread exportLiyangVTDataThread = new ExportLiyangVTDataThread(carInfoAllDao, carRedisManager, version, baseFilePath, type);
        Thread t = new Thread(exportLiyangVTDataThread);
        t.start();
    }

    public void toExportExcelAll(CarInfoAllDao carInfoAllDao, String version, String excelPath) throws Exception{
        String excelName = MonkeyCommonBean.EXPORT_LIYANG_VEHICLE_TYPE_EXCEL_NAME + version;

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

//        int[] columnWith = {
//                4500, 7000, 4000, 4000, 4000, 8000, 4000, 4000, 4000, 4000,
//                4000, 3000, 3000, 3000, 3000, 3000, 3000, 3000, 3000, 4000,
//                3000, 4000, 4000, 3000, 4000, 4000, 4000, 4000, 4000, 4000,
//                4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000,
//                4000, 4000, 4000, 4000, 8000, 4000, 4000, 4000, 4000, 4000,
//                4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000,
//                4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000,
//                4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000,
//                4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000
//        };

        List<CarInfoAllDO> dataList = carInfoAllDao.getCarInfoAllMapper().getCarInfosAll();

        log.info("从数据库获取, 力洋车型数据:{}", dataList.size());

        ExcelUtil excelUtil = new ExcelUtil();
        excelUtil.exportXLSX(excelName, excelPath, headName, fieldName, dataList);

        log.info("生成力洋车型数据完成");
    }
    //--------END 力洋车型数据导出到excel和版本号 相关的业务实现   BY LYJ--------//
}

/**
 * 创建目录,生成excel文件的线程
 */
@Slf4j
class ExportLiyangVTDataThread implements Runnable {
    private CarInfoAllDao carInfoAllDao;
    private CarRedisManager carRedisManager;
    private String version;
    private String lyExcelPath;
    private int type;

    public ExportLiyangVTDataThread(CarInfoAllDao carInfoAllDao,CarRedisManager carRedisManager, String version, String lyExcelPath,int type) {
        this.carInfoAllDao = carInfoAllDao;
        this.carRedisManager = carRedisManager;
        this.version = version;
        this.lyExcelPath = lyExcelPath;
        this.type = type;
    }

    @Override
    public void run() {
        try {
            log.info("开始生成力洋车型数据excel");
//            CarInfoAllServiceImpl carInfoAllServiceImpl = new CarInfoAllServiceImpl();
//            carInfoAllServiceImpl.toExportExcelAll(carInfoAllDao, version, lyExcelPath);

            CarInfoExcelUtil excelUtil = new CarInfoExcelUtil();
            excelUtil.exportAllData(version, lyExcelPath);

            MonkeyCommonBean.CREATE_LIYANG_EXCEL_FLAG = Integer.parseInt(version);
        } catch (Exception e) {
            MonkeyCommonBean.CREATE_LIYANG_EXCEL_FLAG = MonkeyCommonBean.CREATING_EXCEL_ERROR;
            if(MonkeyCommonBean.CREATE_EXCEL_TYPE_NEW == type){
                carRedisManager.rollbackVehicleTypeExportExcelVersion();
            }
            log.error("力洋车型数据导出excel的版本号, 更新失败", e);
        }
    }
}
