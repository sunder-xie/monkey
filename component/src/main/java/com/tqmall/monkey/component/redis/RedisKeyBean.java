package com.tqmall.monkey.component.redis;

/**
 * Created by zxg on 15/9/10.
 * redis key 的管理
 */

public interface RedisKeyBean {
    /*===========变量自动为 final static*/
    //redis变量key的系统前缀
    String SYSTEM_PREFIX = "Monkey_";

    /*=====================自定义的各种key====================================*/

    /**
     * 商品
     */
    //商品品牌主键
    String brandByPrimaryKey = SYSTEM_PREFIX + "brand_by_primary_%d";
    //所有商品品牌,永久
    String AllBrandKey = SYSTEM_PREFIX+"all_commodity_brand";
    //线上商品集合
    String OnlineGoodsByBrandFormat = SYSTEM_PREFIX + "onlinaGoodsByBrandFormat";


    /**
     * 车型
     */
    //key:力洋id value:线上car对象
    String onlineCarByLiyang = SYSTEM_PREFIX + "online_car_by_liyang_%s";

    String liyangBrandKey = SYSTEM_PREFIX+"liyang_brand";
    String liyangFactoryKey = SYSTEM_PREFIX+"liyang_factory_by_brand_%s";
    String liyangSeriesKey = SYSTEM_PREFIX+"liyang_series_by_factory_%s";
    String liyangModelKey = SYSTEM_PREFIX+"liyang_model_by_series_%s";

    /**
     * 导出力洋车型时使用的版本号
     * 业务需要,不能设置过期时间
     */
    String liyangVehicleTypeExportExcelVersionKey = SYSTEM_PREFIX + "liyang_vehicleType_export_excel_version";

    /**
     * 分类
     */
    //key:monkey项目分类的id value:对应的分类对象
    String getCateByPrimaryIdKey = SYSTEM_PREFIX + "category_By_id_%d";

    //field:分类的level，code，pid和车辆种类码  value：对应的分类对象
    String CateAttrKey = SYSTEM_PREFIX + "category_list_By_Ve_Code_Level_Pid_%s";

    //模糊检索的分类名称和分类编码
    String PartLikeNameCode = SYSTEM_PREFIX + "part_like_part_code_%s";

    /*
    * 保养查询相关
    * */
    //车型key
    String CAR_MODELS_FOR_MAINTAIN_KEY = SYSTEM_PREFIX + "car_models_for_maintain_%d";
    //车款key
    String CARS_FOR_MAINTAIN_KEY = SYSTEM_PREFIX + "cars_for_maintain_%d";


    /**
     * part库相关缓存
     */
    //所有以导入的liyang车型对象列表key
    String PartRelationCarKey = SYSTEM_PREFIX + "part_relation_car";
    //当前车型下的商品展示数据列表--map
    String PartGoodsShowKey = SYSTEM_PREFIX + "part_goods_show_by_car_%d";

    /**
     * 导出分类的时使用的版本号
     */
    String catXPartExportExcelVersionKey = SYSTEM_PREFIX + "cat_x_part_export_excel_version";
    /*=============失效时间=====================================*/
    /**
     * 缓存时效 1分钟
     */
    int RREDIS_EXP_MINUTE = 60;

    /**
     * 缓存时效 10分钟
     */
    int RREDIS_EXP_MINUTES = 60 * 10;

    /**
     * 缓存时效 60分钟
     */
    int RREDIS_EXP_HOURS = 60 * 60;

    /**
     * 缓存时效 1天
     */
    int RREDIS_EXP_DAY = 3600 * 24;

    /**
     * 缓存时效 1周
     */
    int RREDIS_EXP_WEEK = 3600 * 24 * 7;

    /**
     * 缓存时效 1月
     */
    int RREDIS_EXP_MONTH = 3600 * 24 * 30 * 7;
}
