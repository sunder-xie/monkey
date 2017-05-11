package com.tqmall.monkey.client.excle;

import com.tqmall.monkey.client.category.CategoryPartService;
import com.tqmall.monkey.client.commodity.*;
import com.tqmall.monkey.client.common.GoodsAttrKeyService;
import com.tqmall.monkey.client.shiro.MonkeyJdbcRealm;
import com.tqmall.monkey.component.excelutils.ReadExcelByEventUserModel;
import com.tqmall.monkey.component.excelutils.ReadExcelXLS;
import com.tqmall.monkey.component.utils.UUIDGenerator;
import com.tqmall.monkey.domain.entity.UserDO;
import com.tqmall.monkey.domain.entity.category.CategoryPartDO;
import com.tqmall.monkey.domain.entity.commodity.*;
import com.tqmall.monkey.domain.entity.common.GoodsAttrKeyDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by zxg on 15/8/20.
 */
@Service
@Scope("prototype")
public class CommodityExcel {
    Logger logger = LoggerFactory.getLogger(CommodityExcel.class);

    @Autowired
    private MonkeyJdbcRealm monkeyJdbcRealm;

    @Autowired
    private CommodityBrandService commodityBrandService;
    @Autowired
    private CommodityGoodsService commodityGoodsService;
    @Autowired
    private CommodityGoodsAttrService commodityGoodsAttrService;
    @Autowired
    private CommodityGoodsCarService commodityGoodsCarService;
    @Autowired
    private CommodityGoodsOeService commodityGoodsOeService;

    @Autowired
    private CategoryPartService categoryPartService;
    @Autowired
    private GoodsAttrKeyService goodsAttrKeyService;

    
    public HashMap<String,CommodityGoodsDO> getUpGoods(){
        return needUpGoodsMap;
    }
    public List<CommodityGoodsCarDO> getUpGoodsCar(){
        return new ArrayList<>(needUpGoodsCarMap);
    }
    public List<CommodityGoodsOeDO> getUpGoodsOe(){
        return new ArrayList<>(needUpGoodsOeMap);
    }
    public List<CommodityGoodsAttrDO> getUpGoodsAttr(){
        return new ArrayList<>(needUpGoodsAttrMap);
    }
    public List<String> getfailList(){
        return failList;
    }
    public Integer getSuccessSum(){
        return insertBatchGoodsList.size();
    }
    
    public void main_process(String file_path,String file_name){


        HashMap<String,Object> resultMap = new HashMap<>();

        commodity_upload_percentage = 5.1;

        failList = new ArrayList<>();
        rightIndexList = new ArrayList<>();

        indexAndUuIdMap = new HashMap<>();


        part_map = new HashMap<>();
        //将零件数据保存在内存中
        part_map = new HashMap<>();
        List<CategoryPartDO> allPart = categoryPartService.getAllPartLists();
        for(CategoryPartDO categoryPartDO : allPart){
            part_map.put(categoryPartDO.getSumCode(), categoryPartDO);
        }

        //存要插入的数据,初始化
        insertBatchGoodsList = new HashSet<>();
        insertBatchCarList = new HashSet<>();
        insertBatchGoodsOeList = new HashSet<>();
        insertBatchGoodsAttrList = new HashSet<>();
        //存要更新的数据,初始化
        needUpGoodsMap = new HashMap<>();
        needUpGoodsCarMap = new HashSet<>();
        needUpGoodsOeMap = new HashSet<>();
        needUpGoodsAttrMap = new HashSet<>();

        try {
            if(file_name.toUpperCase().endsWith(".XLSX")){
                XLSXExcel xlsxUtil = new XLSXExcel();
                xlsxUtil.processAllSheets(file_path);
            }else if(file_name.toUpperCase().endsWith(".XLS")){
                XLSExcle xlsUtil = new XLSExcle();
                xlsUtil.process(file_path,1);
            }else{
                monkeyJdbcRealm.setSession("commodity_upload_percentage","-1");
                logger.info("file isn't end with (.xls/.xlsx)");
            }
            //保存可保存的数据
            saveData();

        } catch (Exception e) {
            monkeyJdbcRealm.setSession("commodity_upload_percentage","-1");
            logger.debug(e.getMessage());
            e.printStackTrace();
        }

    }
/*=======================数据批量存入=====================================================*/
    private void saveData(){
        /*insertBatchGoodsList = new ArrayList<>();
        insertBatchCarList = new ArrayList<>();
        insertBatchGoodsOeList = new ArrayList<>();
        insertBatchGoodsAttrList = new ArrayList<>();*/

        //批量存基本商品数据
        commodityGoodsService.insertBatchGoodsByLoadData(new ArrayList<>(insertBatchGoodsList));
        commodityGoodsCarService.insertBatchCarByLoadData(new ArrayList<>(insertBatchCarList));
        commodityGoodsOeService.insertBatchOeByLoadData(new ArrayList<>(insertBatchGoodsOeList));
        commodityGoodsAttrService.insertBatchAttrByLoadData(new ArrayList<>(insertBatchGoodsAttrList));

    }

/*=======================excle 数据的读取识别处理=====================================================*/
    //XLSX的读取和识别
    class XLSXExcel extends ReadExcelByEventUserModel {
        @Override
        public void operateRows(int sheetIndex, int curRow, List<String> rowList) throws Exception {
            startExcle(sheetIndex, curRow, rowList);
        }
    }
    //XLS的读取
    class XLSExcle extends ReadExcelXLS {
        @Override
        public void operateRows(int sheetIndex, int curRow, List<String> rowList) throws Exception {
            startExcle(sheetIndex, curRow, rowList);
        }
    }

    //设置当前sheet和当前行的处理逻辑
    private void startExcle(int sheetIndex, int curRow, List<String> rowList){
        //结束
        String indexs = rowList.get(0);
        if ("".equals(indexs) ) {
            return;
        }
        //第一行标题，特殊处理
        //第一个sheet
        if(sheetIndex == 0) {
            if (curRow == 0) {
                goodsKeyProcess(rowList);
            } else {
                //第二行开始数据处理
                goodsValueProcess(rowList);
            }
            percentage_add();
            return;

        }
        //第二个sheet,适配车型 && OE码
        if(sheetIndex == 1){
            if (curRow == 0) {
                carKeyProcess(rowList);
            } else {
                //第二行开始数据处理
                carValueProcess(rowList);
            }
            return;

        }
        //第三个sheet，商品参数
        if(sheetIndex == 2){
            if (curRow == 0) {
                attrKeyProcess(rowList);
            } else {
                //第二行开始数据处理
                attrValueProcess(rowList);
            }

        }

    }

    /*==============================shheet3 处理======-=================================================*/

    //第二个sheet-第一行进行处理
    private void attrKeyProcess(List<String> rowlist) {
        this.first_row_map = new HashMap<>();
        int column_num = rowlist.size();

        for (int i = 0; i < column_num; i++) {
            String column_value = rowlist.get(i).trim().replaceAll(" +", "");
            if("".equals(column_value)){
                continue;
            }

            if(column_value.contains("标识位") ){
                this.first_row_map.put(i, "goods_index");
                continue;
            }
            GoodsAttrKeyDO goodsAttrKeyDO = new GoodsAttrKeyDO();
            goodsAttrKeyDO.setAttrName(column_value);
            Integer attrId = goodsAttrKeyService.insertGoodsAttr(goodsAttrKeyDO);
            this.first_row_map.put(i, attrId+" "+column_value);
        }
    }

    private void attrValueProcess(List<String> rowlist){
        //每一行处理时，错误信息初始化
        this.fail_reason = "";
        this.row_is_ok = true;
        this.row_is_not_update=true;

        HashMap<String, Object> goods_attr_map = new HashMap<>();
        List<CommodityGoodsAttrDO> attrList = new ArrayList<>();

        //列的每个字段进行处理
        valueColumnProcess(rowlist, goods_attr_map);
        //判断数据有无效,拼接数据
        judgeAttrValueOk(goods_attr_map, attrList);

        if (row_is_ok) {
            String goods_index = (String) goods_attr_map.get("goods_index");
//            String uuId = (String) indexAndUuIdMap.get(goods_index);
            if(row_is_not_update){
                if(this.rightIndexList.contains(goods_index)) {
                    insertBatchGoodsAttrList.addAll(attrList);
                }
            }else{
                //需要更新的数据
                if(0 != attrList.size()) {
                    this.needUpGoodsAttrMap.addAll(attrList);
                }
            }

        }else{
            //错误数据
            fail_reason = "sheet3中存在"+ fail_reason.trim();
            this.failList.add(fail_reason);
        }
        percentage_add();

    }

    //判断数据有无效,拼接数据
    private void judgeAttrValueOk(HashMap<String, Object> goods_attr_map,List<CommodityGoodsAttrDO> list){
        if (!goods_attr_map.containsKey("goods_index")) {
            row_is_ok = false;
            fail_reason += " 缺少sheet1编号.";
            return;
        }

        String goods_index = (String) goods_attr_map.get("goods_index");
        String uuId = (String) indexAndUuIdMap.get(goods_index);

        if(this.needUpGoodsMap.containsKey(goods_index)){
            row_is_not_update = false;
        }
        //获得每个属性的值
        UserDO userDO = monkeyJdbcRealm.getCurrentUser();
        Integer userId = userDO.getId();
        for(String key : goods_attr_map.keySet()){
            if("goods_index".equals(key)){
                continue;
            }
            String[] id_name_array = key.split(" ");
            String attr_id = id_name_array[0];
            String attr_name = id_name_array[1];

            String value = (String) goods_attr_map.get(key);

            CommodityGoodsAttrDO attrDO = new CommodityGoodsAttrDO();
            attrDO.setAttrName(attr_name);
            attrDO.setGoodsUuId(uuId);
            attrDO.setAttrId(Integer.valueOf(attr_id));
            attrDO.setAttrValue(value);
            attrDO.setCreator(userId);
            attrDO.setModifier(userId);

            list.add(attrDO);
        }

    }



 /*==============================shheet2 处理======-=================================================*/

    //第二个sheet-第一行进行处理
    private void carKeyProcess(List<String> rowlist) {
        this.first_row_map = new HashMap<>();
        int column_num = rowlist.size();

        for (int i = 0; i < column_num; i++) {
            String column_value = rowlist.get(i).trim().replaceAll(" +", "");
            if("".equals(column_value)){
                continue;
            }

            String sql_name = this.excle_car_oe_map.get(column_value);
            if(null != sql_name) {
                this.first_row_map.put(i, sql_name);
            }
        }
    }

    //第二个sheet-第二行开始进行逻辑处理
    private void carValueProcess(List<String> rowlist){
        //每一行处理时，错误信息初始化
        this.fail_reason = "";
        this.row_is_ok = true;
        this.row_is_not_update=true;

        HashMap<String, Object> goods_oe_map = new HashMap<>();

        CommodityGoodsCarDO goodsCarDO = new CommodityGoodsCarDO();
        CommodityGoodsOeDO goodsOeDO = new CommodityGoodsOeDO();

        //列的每个字段进行处理
        valueColumnProcess(rowlist, goods_oe_map);

        //商品属性：原厂正厂等出错
        if (!goods_oe_map.containsKey("goods_index")) {
            row_is_ok = false;
            fail_reason += " 缺少sheet1的标识位.";
            //错误数据
            fail_reason = "sheet2中存在"+ fail_reason.trim();
            this.failList.add(fail_reason);
            return;
        }
        //新增数据,数据在sheet1中的列表中,不在就不需要进行下列操作了
        String index = (String) goods_oe_map.get("goods_index");
        String uuId = (String) indexAndUuIdMap.get(index);
        if(!this.rightIndexList.contains(index) && !this.needUpGoodsMap.containsKey(index)){
            return;
        }

        //判断数据有无效,拼接数据
        if(this.needUpGoodsMap.containsKey(index)){
            row_is_not_update = false;
        }

        if(goods_oe_map.containsKey("oe_number")){
            String oe_number = (String) goods_oe_map.get("oe_number");
            goodsOeDO.setGoodsUuId(uuId);
            goodsOeDO.setOeNumber(oe_number);
        }else {
            goodsOeDO = null;
        }

        if(goods_oe_map.containsKey("liyang_Id")){
            String liyang_Id = (String) goods_oe_map.get("liyang_Id");

//            boolean isExist = carInfoAllService.existLeyelId(liyang_Id);
//            if(!isExist){
//                row_is_ok = false;
//                fail_reason += " "+liyang_Id+"在数据库中不存在.";
//                return;
//            }
            goodsCarDO.setLiyangId(liyang_Id);
            goodsCarDO.setGoodsUuId(uuId);
        }else {
            goodsCarDO = null;
        }


        if (row_is_ok) {
            if(row_is_not_update){

                    if (null != goodsCarDO) {
                        this.insertBatchCarList.add(goodsCarDO);
                    }
                    if (null != goodsOeDO) {
                        this.insertBatchGoodsOeList.add(goodsOeDO);
                    }


            }else{
                if(null != goodsCarDO) {
                    this.needUpGoodsCarMap.add(goodsCarDO);
                }
                if(null != goodsOeDO) {
                    this.needUpGoodsOeMap.add(goodsOeDO);
                }
            }

        }else{
            //错误数据
            fail_reason = "sheet2中存在"+ fail_reason.trim();
            this.failList.add(fail_reason);
        }
        percentage_add();

    }

    

    /*==============================shheet1 处理======-=================================================*/

    //第一个sheet-第一行进行处理
    private void goodsKeyProcess(List<String> rowlist) {
        this.first_row_map = new HashMap<>();
        int column_num = rowlist.size();

        for (int i = 0; i < column_num; i++) {
            String column_value = rowlist.get(i).trim().replaceAll(" +", "");
            if("".equals(column_value)){
                continue;
            }

            String sql_name = this.excle_goods_map.get(column_value);
            if(null != sql_name) {
                this.first_row_map.put(i, sql_name);
            }
        }
    }

    //第一个sheet-第二行开始进行逻辑处理
    private void goodsValueProcess(List<String> rowlist) {
        String index = rowlist.get(0);

        if(index.equals("")){
            return;
        }

        //每一行处理时，错误信息初始化
        this.fail_reason = "";
        this.row_is_ok = true;
        this.row_is_not_update=true;

        HashMap<String, Object> goods_map = new HashMap<>();
        CommodityGoodsDO commodityGoodsDO = new CommodityGoodsDO();

        //列的每个字段进行处理
        valueColumnProcess(rowlist, goods_map);
        //判断数据有无效,拼接数据
        commodityGoodsDO = judgeGoodsValueOk(goods_map,commodityGoodsDO);

        if (row_is_ok) {
            CommodityGoodsDO commodityGoodsDOExist = commodityGoodsService.getCommodityGoods(commodityGoodsDO.getGoodsCode(), commodityGoodsDO.getGoodsQualityType(), commodityGoodsDO.getBrandId());
            String uuId ;

            if (null != commodityGoodsDOExist) {
                row_is_not_update = false;
                //需要更新的数据
                uuId = commodityGoodsDOExist.getUuId();
                commodityGoodsDO.setId(commodityGoodsDOExist.getId());
                commodityGoodsDO.setUuId(uuId);
                commodityGoodsDO.setIsdelete(commodityGoodsDOExist.getIsdelete());
                this.needUpGoodsMap.put(index,commodityGoodsDO);
            } else {
                uuId = UUIDGenerator.getUUID();
                commodityGoodsDO.setUuId(uuId);
                rightIndexList.add((String) goods_map.get("goods_index"));

                //新增数据
                this.insertBatchGoodsList.add(commodityGoodsDO);

            }
            indexAndUuIdMap.put(index,uuId);
        }else{
            //错误数据
            fail_reason = "shee1中标识位为："+index +" "+ fail_reason.trim();
            this.failList.add(fail_reason);
        }

    }

    

    //判断商品表的数据是否有效
    private CommodityGoodsDO judgeGoodsValueOk(HashMap<String, Object> goods_map,CommodityGoodsDO commodityGoodsDO){
        //商品属性：原厂正厂等出错
        if (!goods_map.containsKey("goods_quality_type_name")) {
            row_is_ok = false;
            fail_reason += " 缺少产品属性.";
        }else{
            String goods_quality_type_name = (String) goods_map.get("goods_quality_type_name");
            Integer goods_quality_type = this.goods_quality_type_map.get(goods_quality_type_name);
            if(null == goods_quality_type){
                row_is_ok = false;
                fail_reason += " 产品属性非规定属性.";
            }else{
                commodityGoodsDO.setGoodsQualityType(goods_quality_type);
            }
        }

        //商品品牌中文
        if (goods_map.containsKey("brand_name")) {
            String brandName = (String) goods_map.get("brand_name");
            CommodityBrandDO brandDO = commodityBrandService.getCommodityBrandDOByName(brandName);
            if(null == brandDO) {
                row_is_ok = false;
                fail_reason += " 品牌名称不存在数据库，请在品牌管理页面上添加后再导入";
            }else{
                Integer brandId = brandDO.getId();
                commodityGoodsDO.setBrandId(brandId);
                commodityGoodsDO.setBrandName(brandName);
            }
        }

        //商品类别
        if(!goods_map.containsKey("cate_kind")){
            row_is_ok = false;
            fail_reason += " 不存在易损件标识.";
        }else{
            String cateName = (String) goods_map.get("cate_kind");
            Integer cateKind = this.cate_kind_map.get(cateName);
            if(null == cateKind){
                row_is_ok = false;
                fail_reason += " 易损件标识非（易损件，全车件）.";
            }else{
                commodityGoodsDO.setCateKind(cateKind);
            }
        }

        //在售状态
//        if(!goods_map.containsKey("sale_status")){
//            row_is_ok = false;
//            fail_reason += " 在售状态为空.";
//        }else{
//            String saleStatusName = (String) goods_map.get("sale_status");
//            Integer saleStatus = this.sale_status_map.get(saleStatusName);
//            if(null == saleStatus){
//                row_is_ok = false;
//                fail_reason += " 在售状态为空（预售，在售，停售）.";
//            }else{
//                commodityGoodsDO.setSaleStatus(saleStatus);
//            }
//        }

        commodityGoodsDO.setSaleStatus(0);
        //标准零件名称
        if(!goods_map.containsKey("part_name")){
            row_is_ok = false;
            fail_reason += " 标准零件名称为空.";
        }else{
            String partName = (String) goods_map.get("part_name");
            if(!goods_map.containsKey("part_sum_code")){
                row_is_ok = false;
                fail_reason += " 标准零件编号为空.";
            }else{
                String partCode = (String) goods_map.get("part_sum_code");
                if(!part_map.keySet().contains(partCode)){
                    row_is_ok = false;
                    fail_reason += " 标准零件编号不在数据库中";
                }else {
                    CategoryPartDO categoryPartDO = part_map.get(partCode);
                    if (null == categoryPartDO || !partName.equals(categoryPartDO.getPartName())) {
                        row_is_ok = false;
                        fail_reason += " 标准零件编号和标准名称错误，数据库无值.";
                    } else {
                        commodityGoodsDO.setPartId(categoryPartDO.getId());
                        commodityGoodsDO.setPartName(partName);
                        commodityGoodsDO.setPartSumCode(partCode);
                    }
                }
            }

        }

        if(!goods_map.containsKey("goods_index")){
            row_is_ok = false;
            fail_reason += " 无标识位.";
        }
        if(goods_map.containsKey("goods_name")){
            commodityGoodsDO.setGoodsName((String) goods_map.get("goods_name"));
        }
        if (goods_map.containsKey("goods_format")){
            commodityGoodsDO.setGoodsFormat((String) goods_map.get("goods_format"));
        }

        if (goods_map.containsKey("guarantee_time")){
            commodityGoodsDO.setGuaranteeTime((String) goods_map.get("guarantee_time"));
        }
        if(goods_map.containsKey("sale_unit")){
            commodityGoodsDO.setSaleUnit((String) goods_map.get("sale_unit"));
        }
        if(goods_map.containsKey("remark")){
            commodityGoodsDO.setRemark((String) goods_map.get("remark"));
        }

        //商品编码唯一，若存在则需跟前端交互是否更新
        if (!goods_map.containsKey("goods_code")) {
            row_is_ok = false;
            fail_reason += " 缺少商品编码.";
        }else{
            String goods_code = (String) goods_map.get("goods_code");
            commodityGoodsDO.setGoodsCode(goods_code);
        }
        return commodityGoodsDO;
    }

    /*=============================================================common============================*/
    //每列字段处理
    private void valueColumnProcess(List<String> rowList, HashMap<String, Object> goods_map){
        int column_num = rowList.size();

        for (int i = 0; i < column_num; i++) {
            //不需要的数据或是无效数据，不处理
            if (!this.first_row_map.containsKey(i)) {
                continue;
            }

            String column_value = rowList.get(i).trim().replaceAll(" +", " ").replace("／", "/").replace("（", "(").replace("）", ")").replace("—", "-").toUpperCase();

            if ("".equals(column_value) || "-".equals(column_value)) {
                continue;
            }
            if ("#N/A".equals(column_value)) {
                row_is_ok = false;
                fail_reason += "存在#N/A数据,修改后再导入.";
                return;
            }
            String goods_sql_name = (String) this.first_row_map.get(i);

            goods_map.put(goods_sql_name,column_value);
        }
    }
    
    //伪造进程的百分比
    private void percentage_add(){
        BigDecimal b   =   new   BigDecimal(commodity_upload_percentage);
        commodity_upload_percentage   =   b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue();
        if(commodity_upload_percentage < 75.0){
            commodity_upload_percentage = commodity_upload_percentage+0.01;
        }else if(commodity_upload_percentage < 98.0){
            commodity_upload_percentage = commodity_upload_percentage+0.001;
        }
        monkeyJdbcRealm.setSession("commodity_upload_percentage", commodity_upload_percentage+"%");
    }


    //存伪进度
    private Double commodity_upload_percentage = 5.0;

    //当前行
    private Boolean row_is_ok;
    private Boolean row_is_not_update;
    private String fail_reason;

    //记录正确的序号
    private List<String> rightIndexList = null;

    //整个excle中不合格数据原因
    private List<String> failList = null;


    //sheet1中的index 跟 uuId的对应
    private HashMap<String,Object> indexAndUuIdMap = null;

    //存要插入的数据
    private Set<CommodityGoodsDO> insertBatchGoodsList = null;
    private Set<CommodityGoodsCarDO> insertBatchCarList = null;
    private Set<CommodityGoodsOeDO> insertBatchGoodsOeList = null;
    private Set<CommodityGoodsAttrDO> insertBatchGoodsAttrList = null;


    //存要更新的数据
    private HashMap<String,CommodityGoodsDO> needUpGoodsMap = null;
    private Set<CommodityGoodsCarDO> needUpGoodsCarMap = null;
    private Set<CommodityGoodsOeDO> needUpGoodsOeMap = null;
    private Set<CommodityGoodsAttrDO> needUpGoodsAttrMap = null;

    /**part_code : part_name **/
    private Map<String, CategoryPartDO> part_map = new HashMap<>();

    //每个sheet第一列的数据
    private HashMap<Integer,Object> first_row_map = null;

    //sheet1 第一行字段
    private HashMap<String, String> excle_goods_map = new HashMap<String, String>() {
        {
            put("标识位", "goods_index");

            put("商品属性", "goods_quality_type_name");
            put("商品品牌", "brand_name");
            put("商品编码", "goods_code");

            put("商品名称", "goods_name");
            put("标准零件名称", "part_name");
            put("标准零件编码", "part_sum_code");
            put("易损件标识", "cate_kind");
            put("规格型号", "goods_format");
            put("质保期", "guarantee_time");
            put("销售单位", "sale_unit");
//            put("销售状态", "sale_status");
            put("备注", "remark");
        }
    };
    //sheet2 第一行字段
    private HashMap<String, String> excle_car_oe_map = new HashMap<String, String>() {
        {
            put("标识位", "goods_index");

//            put("商品编码","goods_code");
//            put("商品Id", "goods_code");

            put("OE码", "oe_number");
            put("OE", "oe_number");
            put("oe", "oe_number");
            
            put("力洋Id", "liyang_Id");
            put("力洋id", "liyang_Id");
            put("力洋ID", "liyang_Id");
            put("LevelID", "liyang_Id");
        }
    };
    
    //规定商品类别
    private HashMap<String, Integer> cate_kind_map = new HashMap<String, Integer>() {
        {
            put("易损件", 0);
            put("全车件", 1);
        }
    };
    //规定在售状态
    private HashMap<String, Integer> sale_status_map = new HashMap<String, Integer>() {
        {
            put("预售", 0);
            put("在售", 1);
            put("停售", 2);
        }
    };

    //规定属性
    private HashMap<String, Integer> goods_quality_type_map = new HashMap<String, Integer>() {
        {
            put("正厂原厂", 1);
            put("正厂配套", 2);
            put("正厂下线", 3);
            put("全新拆车", 4);
            put("旧件拆车", 5);
            put("副厂", 6);

            //其他名称
            put("原厂", 1);
            put("原厂正厂", 1);
            put("下线", 3);
            put("正厂", 1);
            put("配套", 2);
            put("品牌", 0);
            put("高仿", 9);
        }
    };
}
