package com.tqmall.monkey.client.excle;

import com.tqmall.monkey.component.excelutils.ExcelUtil;
import com.tqmall.monkey.component.utils.SpringIocUtil;
import com.tqmall.monkey.dal.mapper.car.CarInfoAllDOMapper;
import com.tqmall.monkey.domain.commonBean.MonkeyCommonBean;
import com.tqmall.monkey.domain.entity.car.CarInfoAllDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangzhangting on 17/3/22.
 */
@Slf4j
public class CarInfoExcelUtil extends ExcelUtil {
    private final int defaultPageSize = 5000;
    private CarInfoAllDOMapper carInfoAllMapper;
    private String excelName;
    private String[] headNames;
    private String[] fieldNames;
    private int rowNum = 0;

    private void setCarInfoAllDOMapper(){
        Object object = SpringIocUtil.getBean("carInfoAllDOMapper");
        Assert.notNull(object, "获取 CarInfoAllDOMapper 异常");
        carInfoAllMapper = (CarInfoAllDOMapper)object;
    }

    private void setExcelInfo(String version){
        excelName = MonkeyCommonBean.EXPORT_LIYANG_VEHICLE_TYPE_EXCEL_NAME + version;
        headNames = new String[]{
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
        fieldNames = new String[]{
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
    }

    public void exportAllData(String version, String excelPath) throws Exception{
        //设置mapper
        setCarInfoAllDOMapper();
        //设置excel
        setExcelInfo(version);
        //查询数据
        Map<String, Object> map = new HashMap<>();
        int count = carInfoAllMapper.getCarInfoAllCount(map);
        log.info("从数据库获取, 力洋车型数据: {}", count);
        if(count==0){
            return;
        }

        Workbook workbook = new SXSSFWorkbook();
        Sheet sheet = initSheet(workbook, "sheet1", headNames, null, 0, 1);
        CellStyle style = getStyle_LEFT_CENTER(workbook, false, false);
        //车型数据
        map.put("pageSize", defaultPageSize);
        int p = 1;
        List<CarInfoAllDO> carInfoAllDOs = getCarInfoList(map, p);
        handleWorkbook(carInfoAllDOs, sheet, style);

        if(count>defaultPageSize) {
            int page = count % defaultPageSize == 0 ? count / defaultPageSize : count / defaultPageSize + 1;
            while (p < page){
                p++;
                carInfoAllDOs = getCarInfoList(map, p);
                handleWorkbook(carInfoAllDOs, sheet, style);
            }
        }

        writeExcelToSystem(workbook, excelName, ".xlsx", excelPath);

        log.info("生成力洋车型数据完成");
    }

    private List<CarInfoAllDO> getCarInfoList(Map<String, Object> map, int pageIndex){
        int offset = (pageIndex-1)*defaultPageSize;
        map.put("offset", offset);
        return carInfoAllMapper.getCarInfoAll(map);
    }

    private void handleWorkbook(Collection<?> dataList, Sheet sheet, CellStyle style) throws Exception{
        int size = fieldNames.length;
        Field f;
        Object value;
        for(Object data : dataList){
            Row row = sheet.createRow(++rowNum);
            row.setHeightInPoints(TableContentRowHeight);

            Class cla = data.getClass();
            for(int i=0; i<size; i++){
                Cell cell = row.createCell(i);
                cell.setCellStyle(style);

                String fn = fieldNames[i];
                f = cla.getDeclaredField(fn);
                f.setAccessible(true);
                value = f.get(data);
                cell.setCellValue(value==null?"":value.toString());
            }
        }
    }

}
