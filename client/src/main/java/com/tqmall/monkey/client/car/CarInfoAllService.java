package com.tqmall.monkey.client.car;

import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.car.CarInfoAllDO;

import java.util.List;

/**
 * Created by huangzhangting on 15/7/22.
 */
public interface CarInfoAllService {
    List<String> findCarBrands();

    List<String> findFactoryNamesByBrand(String carBrand);

    List<String> findCarSeriesByBrand(String carBrand, String factoryName);

    List<String> findVehicleTypesBySeries(String carBrand, String factoryName, String carSeries);

    /**
     * 准确搜索
     * @param carBrand 不可选
     * @param factoryName 不可选
     * @param carSeries 不可选
     * @param vehicleType 不可选
     * @param pageIndex 不可选
     * @param pageSize 不可选
     * @return
     */
    Page<CarInfoAllDO> findCarInfoPage(String carBrand, String factoryName, String carSeries,
                                       String vehicleType, int pageIndex, int pageSize);

    /**
     * 模糊检索
     * @param carBrand 可选，为null则不搜
     * @param factoryName 可选，为null则不搜
     * @param carSeries 可选，为null则不搜
     * @param vehicleType  like 模糊值
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Page<CarInfoAllDO> findCarInfoPageByVehicleType(String carBrand, String factoryName, String carSeries,
                                                    String vehicleType, int pageIndex, int pageSize);

    /**
     * 准确搜索
     * @param carBrand
     * @param factoryName
     * @param carSeries
     * @param vehicleType
     * @return
     */
    List<CarInfoAllDO> findCarInfos(String carBrand, String factoryName, String carSeries,
                                    String vehicleType);

    /**
     * 模糊检索
     * @param carBrand
     * @param factoryName
     * @param carSeries
     * @param vehicleType
     * @return
     */
    List<CarInfoAllDO> findCarInfosByVehicleType(String carBrand, String factoryName, String carSeries,
                                    String vehicleType);
    //根据自增id查找
    CarInfoAllDO findCarInfoById(Integer id);

    //根据力洋id查找
    CarInfoAllDO findCarInfoByLeyelId(String leyelId);

    boolean existLeyelId(String leyelId);

    //通过Excel导入数据
    boolean importDataByExcel(String fileName) throws Exception;

    /*
    *  根据条件查询车型数据，所有条件都是选填
    * */
    List<CarInfoAllDO> findCarInfoAll(String carBrand, String factoryName, String carSeries,
                                    String vehicleType);

    /*
    *  根据车型保养id查找数据
    * */
    List<CarInfoAllDO> findCarInfoForMaintain(Integer maintainPlanId);

    List<CarInfoAllDO> findNotRelatedCarInfoForMaintain(String carBrand, String factoryName,
                                                        String carSeries, String vehicleType);

    //--------START 力洋车型数据导出到excel和版本号 相关的业务方法   BY LYJ--------//

    /**
     * 刷新redis缓存中的 版本号
     */
    String refreshVehicleTypeExportExcelVersion();

    /**
     * 获取redis缓存中的 版本号
     * @return
     */
    String getVehicleTypeExportExcelVersion();

    /**
     * 删除redis缓存中的 版本号
     * @return
     */
    String deleteVehicleTypeExportExcelVersion();

    /**
     * 生成车型全量数据的excel文件(如果之前文件存在, 则先删除)
     * @return
     */
    String createVehicleTypeAllDataExcel();
    //--------END 力洋车型数据导出到excel和版本号 相关的业务方法   BY LYJ--------//
}
