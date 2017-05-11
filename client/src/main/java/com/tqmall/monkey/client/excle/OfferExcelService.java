package com.tqmall.monkey.client.excle;

import com.tqmall.monkey.client.car.OfferCarService;
import com.tqmall.monkey.client.category.CategoryPartService;
import com.tqmall.monkey.client.offerGoods.OfferGoodsService;
import com.tqmall.monkey.client.offerGoods.OfferRecordService;
import com.tqmall.monkey.client.offerGoods.OfferWrongDataService;
import com.tqmall.monkey.client.shiro.MonkeyJdbcRealm;
import com.tqmall.monkey.component.excelutils.ReadExcelByEventUserModel;
import com.tqmall.monkey.component.excelutils.ReadExcelXLS;
import com.tqmall.monkey.component.utils.BigDataToMySql;
import com.tqmall.monkey.domain.entity.UserDO;
import com.tqmall.monkey.domain.entity.car.OfferCarDO;
import com.tqmall.monkey.domain.entity.category.CategoryPartDO;
import com.tqmall.monkey.domain.entity.offerGoods.OfferGoodsDO;
import com.tqmall.monkey.domain.entity.offerGoods.OfferLiCarDO;
import com.tqmall.monkey.domain.entity.offerGoods.OfferRecordDO;
import com.tqmall.monkey.domain.entity.offerGoods.OfferWrongDataDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 供应商表格导入入库的EXCL读取处理类Service
 * Created by ximeng on 2015/7/6.
 */
//@Transactional
@Scope("prototype")
@Service
public class
        OfferExcelService {

    Logger logger = LoggerFactory.getLogger(OfferExcelService.class);

    @Autowired
    private OfferGoodsService offerGoodsService;
    @Autowired
    private OfferCarService offerCarService;
    @Autowired
    private OfferRecordService offerRecordService;
    @Autowired
    private OfferWrongDataService offerWrongDataService;

    @Autowired
    private CategoryPartService categoryPartService;
    @Autowired
    private BigDataToMySql bigDataToMySql;

    @Autowired
    private MonkeyJdbcRealm monkeyJdbcRealm;

    public HashMap<String,Object> main_process(String file_path,String file_name){
        HashMap<String,Object> number_map = new HashMap<>();

        this.sum_number = 0;
        this.success_number = 0;
        this.fail_number = 0;
        offerLiCarDOList = new ArrayList<>();

        //将零件数据保存在内存中
        part_map = new HashMap<>();
        List<CategoryPartDO> allPart = categoryPartService.getAllPartLists();
        for(CategoryPartDO categoryPartDO : allPart){
            part_map.put(categoryPartDO.getSumCode(), categoryPartDO);
        }

        record_name = file_name.replace(".xls","").replace(".xlsx", "");
        try {
            if(file_name.toUpperCase().endsWith(".XLSX")){

                XLSXExcel xlsxUtil = new XLSXExcel();
                xlsxUtil.processAllSheets(file_path);
            }else if(file_name.toUpperCase().endsWith(".XLS")){

                XLSExcle xlsUtil = new XLSExcle();
                xlsUtil.process(file_path,1);

            }else{
                monkeyJdbcRealm.setSession("offerGoods_upload_percentage","-1");
                logger.info("file isn't end with (.xls/.xlsx)");
            }
            // 保存车型对应关系
            saveOfferLiCar();
        } catch (Exception e) {
            monkeyJdbcRealm.setSession("offerGoods_upload_percentage","-1");
            logger.debug(e.getMessage());
            e.printStackTrace();
        }
        number_map.put("sum_number",this.sum_number);
        number_map.put("success_number",this.success_number);
        number_map.put("fail_number",this.fail_number);
        return number_map;
    }
    // 保存车型对应关系
    private void saveOfferLiCar(){
        List<OfferLiCarDO> list = this.offerLiCarDOList;

        long now = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = new Date(now );
        String nowTime = sdf.format(dt);
        if(list.size() > 0){
            logger.debug("=====offerli size=="+list.size());
            UserDO userDO = monkeyJdbcRealm.getCurrentUser();
            Integer userId = userDO.getId();

            String headerSql = "LOAD DATA LOCAL INFILE 'sql.csv' IGNORE INTO TABLE modeldatas.TABLENAME";
            String relationSql = headerSql.replace("TABLENAME", "db_monkey_offer_li_car") +
                    "( offer_goods_id, li_id," +
                    "    update_id,create_id, gmt_modified, gmt_create)";
            StringBuilder builder = new StringBuilder();
            for(OfferLiCarDO offerLiCarDO : list){

                builder.append(offerLiCarDO.getOfferGoodsId());
                builder.append("\t");
                builder.append(offerLiCarDO.getLiId());
                builder.append("\t");
                builder.append(userId);
                builder.append("\t");
                builder.append(userId);
                builder.append("\t");
                builder.append(nowTime);
                builder.append("\t");
                builder.append(nowTime);
                builder.append("\t");
                builder.append("\n");
            }
            byte[] bytes = builder.toString().getBytes();
            InputStream relationInput = new ByteArrayInputStream(bytes);

            try {
                bigDataToMySql.bulkLoadFromInputStream(relationSql,relationInput);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //XLSX的读取和识别
    class XLSXExcel extends ReadExcelByEventUserModel {
        @Override
        public void operateRows(int sheetIndex, int curRow, List<String> rowlist) throws Exception {
            startExcle(sheetIndex, curRow, rowlist);
        }
    }

    //XLS的读取
    class XLSExcle extends ReadExcelXLS{
        @Override
        public void operateRows(int sheetIndex, int curRow, List<String> rowList) throws Exception {

            startExcle(sheetIndex, curRow, rowList);
        }
    }


    //设置当前sheet和当前行的处理逻辑
    private void startExcle(int sheetIndex, int curRow, List<String> rowList){
        //结束
        String indexs = rowList.get(0);
        if ("".equals(indexs)) {
            return;
        }
        //第一行标题，特殊处理
        //第一个sheet
        if(sheetIndex == 0) {
            if (curRow == 0) {
                firstRowProcess(rowList);
            } else {
                //第二行开始数据处理
                secondRowProcess(rowList);
            }
            percentage_add();
        }
        //第二个sheet,第一列为第一个sheet的商品序号，第二列为力洋库的编号
        if(sheetIndex == 1){
            if(curRow == 0){
                secondFirstRowProcess(rowList);
            }else{
                //第二行开始数据处理
                secondSheetRowProcess(rowList);
            }
            percentage_add();
        }
    }

    //伪造进程的百分比
    private void percentage_add(){
        BigDecimal   b   =   new   BigDecimal(offerGoods_upload_percentage);
        offerGoods_upload_percentage   =   b.setScale(3,   BigDecimal.ROUND_HALF_UP).doubleValue();
        if(offerGoods_upload_percentage < 75){
            offerGoods_upload_percentage = offerGoods_upload_percentage+0.1;
        }else if(offerGoods_upload_percentage < 95){
            offerGoods_upload_percentage = offerGoods_upload_percentage+0.001;
        }
        monkeyJdbcRealm.setSession("offerGoods_upload_percentage", offerGoods_upload_percentage+"%");
    }

    //第二个sheet-第yi行开始进行遍历
    public void secondFirstRowProcess(List<String> rowlist){
        int column_num = rowlist.size();

        for (int i = 0; i < column_num; i++) {
            String column_value = rowlist.get(i).trim().replaceAll(" +", "");
            if(!"".equals(column_value)) {
                this.index_second_map.put(i,column_value);
            }
        }
    }

    //第二个sheet-第二行开始进行遍历
    public void secondSheetRowProcess(List<String> rowlist){
        int column_num = rowlist.size();

        String index = "";
        String liyang_id = "";
        for (int i = 0; i < column_num; i++) {
            String column_value = rowlist.get(i).trim().replaceAll(" +", "").replace("／", "").replace("（", "").replace("）", "");
            if("".equals(column_value)) {
                continue;
            }
            if("#N/A".equals(column_value)){
                is_ok = false;
                fail_reason += " sheet2存在#N/A数据,修改后再导入";
                return;
            }

            String firstName = this.index_second_map.get(i);
            if(null == firstName){
                is_ok = false;
                fail_reason += " 检查第二列的第一行有无文字";
                return;
            }
            if(firstName.contains("商品") || firstName.contains("标识")){
                index = column_value;
            }else if(firstName.contains("力洋")){
                liyang_id = column_value;
            }
        }
//        String index = rowlist.get(0).trim().replaceAll(" +", "").replace("／", "").replace("（", "").replace("）", "").replace("#N/A", "");
//        String liyang_id = rowlist.get(1).trim().replaceAll(" +", "").replace("／", "").replace("（", "").replace("）", "").replace("#N/A","");

        if("".equals(index) || "".equals(liyang_id)){
            return;
        }
        //保存入库
        if(index_goodsId_map.containsKey(index)){
            OfferLiCarDO offerLiCarDO = new OfferLiCarDO();
            offerLiCarDO.setLiId(liyang_id);
            offerLiCarDO.setOfferGoodsId(index_goodsId_map.get(index));
            offerLiCarDOList.add(offerLiCarDO);
//            offerLiCarDO.setCreateId(user.getId());
//            offerLiCarDO.setUpdateId(user.getId());
//            offerLiCarDO.setStatus(OfferLiCarDO.status_新建);

//            offerLiCarService.insertofferLiCarDOWithOut(offerLiCarDO);
        }

    }

    //第一个sheet-第一行进行处理
    private void firstRowProcess(List<String> rowlist) {
        int column_num = rowlist.size();

        for (int i = 0; i < column_num; i++) {
            String column_value = rowlist.get(i).trim().replaceAll(" +", "");

            if (this.sql_goods_map.containsKey(column_value)) {
                String sql_name = this.sql_goods_map.get(column_value);
                this.index_sql_map.put(i, sql_name + " " + goods_table_name);
                continue;
            }
            if (this.sql_car_map.containsKey(column_value)) {
                String sql_name = this.sql_car_map.get(column_value);
                this.index_sql_map.put(i, sql_name + " " + car_table_name);
                continue;
            }
            if (this.sql_record_map.containsKey(column_value)) {
                String sql_name = this.sql_record_map.get(column_value);
                this.index_sql_map.put(i, sql_name + " " + record_table_name);
            }

        }

    }

    //第一个sheet-第二行开始进行逻辑处理
    private void secondRowProcess(List<String> rowlist) {
        HashMap<String, Object> goods_map = new HashMap<>();
        HashMap<String, Object> record_map = new HashMap<>();
        //多个车型信息
        HashMap<String, Object> car_list_map = new HashMap<>();

        // 设定记录名---即读取的excle名称
        record_map.put("record_name", record_name);

        //列的每个字段进行处理
        columnProcess(rowlist, goods_map, record_map, car_list_map);
        //判断数据有无效
        judgeOk(goods_map, record_map);

        this.sum_number ++;
        if (is_ok) {
            //保存至数据库
            Integer goods_id = saveGoods(goods_map);
            Integer record_id = saveRecord(record_map, goods_id);
            //TODO car不在第一个sheet中取，先注释掉
//            List<Integer> car_id_list = saveCarList(car_list_map);
//            saveRelation(goods_id, car_id_list);
            this.success_number ++;
            //供第二个sheet使用
            index_goodsId_map.put((String)goods_map.get("goods_indexs"),goods_id);
        }else{
            //保存信息至临时表中
            String index_num = rowlist.get(0);
            saveFailData(index_num,fail_reason);
            this.fail_number++;
        }

    }

    //供应商信息出错保存在数据库中
    private  void saveFailData(String index_num,String fail_reason){
        UserDO user = monkeyJdbcRealm.getCurrentUser();
        OfferWrongDataDO offerWrongDataDO = new OfferWrongDataDO();
        offerWrongDataDO.setStatus(OfferWrongDataDO.status_new);
        offerWrongDataDO.setIndexs(index_num);
        offerWrongDataDO.setCreateAccount(user.getUserName());
        offerWrongDataDO.setFailReason(fail_reason.trim());
        offerWrongDataDO.setRecordName(record_name);
        //商品goods
        if(fail_map.containsKey("oe_num")){
            offerWrongDataDO.setOeNumber(fail_map.get("oe_num"));
        }
        if(fail_map.containsKey("goods_name")){
            offerWrongDataDO.setGoodsName(fail_map.get("goods_name"));
        }
        if(fail_map.containsKey("brand")){
            offerWrongDataDO.setGoodsAttribute(fail_map.get("brand"));
        }
        if(fail_map.containsKey("remark")){
            offerWrongDataDO.setRemark(fail_map.get("remark"));
        }
        if(fail_map.containsKey("part_name")){
            offerWrongDataDO.setPartName(fail_map.get("part_name"));
        }
        if(fail_map.containsKey("part_sum_code")){
            offerWrongDataDO.setPartCode(fail_map.get("part_sum_code"));
        }
        if(fail_map.containsKey("goods_format")){
            offerWrongDataDO.setGoodsFormat(fail_map.get("goods_format"));
        }
        if(fail_map.containsKey("package_format")){
            offerWrongDataDO.setPackageFormat(fail_map.get("package_format"));
        }
        if(fail_map.containsKey("measure_unit")){
            offerWrongDataDO.setMeasureUnit(fail_map.get("measure_unit"));
        }
        if(fail_map.containsKey("cate_kind")){
            offerWrongDataDO.setQuickWearLabel(fail_map.get("cate_kind"));
        }
        //供应商
        if(fail_map.containsKey("advice_sale_price")){
            offerWrongDataDO.setAdvicePrice(fail_map.get("advice_sale_price"));
        }
        if(fail_map.containsKey("prime_price_tax")){
            offerWrongDataDO.setPrimaryPrice(fail_map.get("prime_price_tax"));
        }
        if(fail_map.containsKey("provider_name")){
            offerWrongDataDO.setProviderName(fail_map.get("provider_name"));
        }
        //car
        if(fail_map.containsKey("displacement")){
            offerWrongDataDO.setDisplacement(fail_map.get("displacement"));
        }
        if(fail_map.containsKey("year")){
            offerWrongDataDO.setYear(fail_map.get("year"));
        }
        if(fail_map.containsKey("offer_car_name")){
            offerWrongDataDO.setSeries(fail_map.get("offer_car_name"));
        }
        if(fail_map.containsKey("brand_name")){
            offerWrongDataDO.setBrand(fail_map.get("brand_name"));
        }
        if(fail_map.containsKey("company")){
            offerWrongDataDO.setCompany(fail_map.get("company"));
        }
        if(fail_map.containsKey("model")){
            offerWrongDataDO.setModel(fail_map.get("model"));
        }
        if(fail_map.containsKey("city")){
            offerWrongDataDO.setCity(fail_map.get("city"));
        }

        //插入数据库
        offerWrongDataService.insertOfferWrongDataWithoutExit(offerWrongDataDO);
    }

    //保存商品信息于数据库中
    private Integer saveGoods(HashMap<String, Object> goods_map){
        UserDO user = monkeyJdbcRealm.getCurrentUser();
        //保存商品信息
        OfferGoodsDO offerGoodsDO = new OfferGoodsDO();
        offerGoodsDO.setCreateId(user.getId());
        //对oe码进行数据处理
        String oe_num = (String) goods_map.get("oe_num");
        oe_num = oe_num.replace(".0", "");
        if(oe_num.startsWith("(") && oe_num.endsWith(")") ){
            oe_num = oe_num.substring(1,oe_num.length()-1);
        }
        while (true){
            if(oe_num.endsWith("-")){
                oe_num = oe_num.substring(0,oe_num.length()-1);
            }else{
                break;
            }
        }
        offerGoodsDO.setOeNum(oe_num);
        if(goods_map.containsKey("goods_name")){
            offerGoodsDO.setGoodsName((String) goods_map.get("goods_name"));
        }
        if(goods_map.containsKey("goods_quality_type")){
            offerGoodsDO.setGoodsQualityType((Integer) goods_map.get("goods_quality_type"));
        }
        if(goods_map.containsKey("remark")){
            offerGoodsDO.setRemark((String) goods_map.get("remark"));
        }
        if(goods_map.containsKey("part_name")){
            offerGoodsDO.setPartName((String) goods_map.get("part_name"));
        }
        if(goods_map.containsKey("part_sum_code")){
            String partCode = (String) goods_map.get("part_sum_code");
            CategoryPartDO categoryPartDO = part_map.get(partCode);
            offerGoodsDO.setPartSumCode(partCode);
            offerGoodsDO.setPartId(categoryPartDO.getId());
            offerGoodsDO.setFirstCateId(categoryPartDO.getFirstCatId());
            offerGoodsDO.setFirstCateName(categoryPartDO.getFirstCatName());
            offerGoodsDO.setSecondCateId(categoryPartDO.getSecondCatId());
            offerGoodsDO.setSecondCateName(categoryPartDO.getSecondCatName());
            offerGoodsDO.setThirdCateId(categoryPartDO.getThirdCatId());
            offerGoodsDO.setThirdCateName(categoryPartDO.getThirdCatName());
            offerGoodsDO.setCateStatus(OfferGoodsDO.cate_status_success);
        }
        if(goods_map.containsKey("goods_format")){
            offerGoodsDO.setGoodsFormat((String) goods_map.get("goods_format"));
        }
        if(goods_map.containsKey("package_format")){
            offerGoodsDO.setPackageFormat((String) goods_map.get("package_format"));
        }
        if(goods_map.containsKey("measure_unit")){
            String measure_unit = (String) goods_map.get("measure_unit");
            offerGoodsDO.setMeasureUnit(measure_unit);
            offerGoodsDO.setMinMeasureUnit(measure_unit);
        }
        if(goods_map.containsKey("brand_name")){
            offerGoodsDO.setBrandName((String) goods_map.get("brand_name"));
        }
        if(goods_map.containsKey("remark")){
            offerGoodsDO.setRemark(((String) goods_map.get("remark")).trim());
        }
        if(goods_map.containsKey("cate_kind")){
            offerGoodsDO.setCateKind(1);
        }else{
            offerGoodsDO.setCateKind(0);
        }
        //存入前在pool池查一遍，若存在，则取出分类信息
//        PoolGoodsDO poolGoodsDO = poolGoodsService.findByOE(oe_num);
//        if(poolGoodsDO != null){
//            offerGoodsDO.setBrandId(poolGoodsDO.getBrandId());
//            offerGoodsDO.setBrandName(poolGoodsDO.getBrandName());
//            offerGoodsDO.setPartId(poolGoodsDO.getPartId());
//            offerGoodsDO.setThirdCateId(poolGoodsDO.getThirdCateId());
//            offerGoodsDO.setThirdCateName(poolGoodsDO.getThirdCateName());
//            offerGoodsDO.setSecondCateId(poolGoodsDO.getSecondCateId());
//            offerGoodsDO.setSecondCateName(poolGoodsDO.getSecondCateName());
//            offerGoodsDO.setFirstCateId(poolGoodsDO.getFirstCateId());
//            offerGoodsDO.setFirstCateName(poolGoodsDO.getFirstCateName());
//
//            offerGoodsDO.setCateStatus(OfferGoodsDO.cate_status_toPool);
//            offerGoodsDO.setBrandStatus(OfferGoodsDO.brand_status_toPool);
//        }

        return offerGoodsService.insertOfferGoodsWithExit(offerGoodsDO);
    }

    //保存record信息于数据库
    private Integer saveRecord(HashMap<String, Object> record_map,Integer goods_id){
        UserDO user = monkeyJdbcRealm.getCurrentUser();
        //金钱符号被替换掉
        String advice_sale_price = ((String)record_map.get("advice_sale_price")).replace("$","").replace("￥","");
        BigDecimal advice_decimal = new BigDecimal(advice_sale_price);
        //四舍五入，保留两位小数
        BigDecimal advice_sale_decimal = advice_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);

        BigDecimal prime_price_decimal = BigDecimal.ZERO;
        if(record_map.containsKey("prime_price_tax")){
            String prime_price = ((String)record_map.get("prime_price_tax")).replace("$","").replace("￥","");
            prime_price_decimal = new BigDecimal(prime_price);
            //四舍五入，保留两位小数
            prime_price_decimal = prime_price_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
            //若供应淘气价格>建议零售价
//            if(prime_price_decimal.compareTo(advice_decimal) == 1){
//                is_ok = false;
//                fail_reason += " 供应淘气价格>建议零售价";
//            }
        }
        String providerName = (String)record_map.get("provider_name");
        Integer id;
        //根据goods_id和record_name和provider_name,status为未上架 获得数据库数据，比较，取小的值
        OfferRecordDO offerRecordDO = offerRecordService.findByGoodsIdRecordNameProviderNameStatus(goods_id, record_name, providerName, OfferRecordDO.status_not_dev);
        //不存在数据-》删除
        if(null == offerRecordDO){
            offerRecordDO = new OfferRecordDO();
            offerRecordDO.setAdviceSalePrice(advice_sale_decimal);
            offerRecordDO.setOfferGoodsId(goods_id);
            offerRecordDO.setPrimePriceTax(prime_price_decimal);
            offerRecordDO.setRecordName(record_name);
            offerRecordDO.setProviderName(providerName);
            offerRecordDO.setCreateId(user.getId());
            if(record_map.containsKey("city")){
                offerRecordDO.setCity((String)record_map.get("city"));
            }
            id = offerRecordService.insertRecord(offerRecordDO);
        }else {
            //存在数据，比较，取小的---同供应商同OE码价格不同数据：按最低价提取
            BigDecimal old_advice_decimal = offerRecordDO.getAdviceSalePrice();
            BigDecimal old_prime_price_decimal = offerRecordDO.getPrimePriceTax();
            if (advice_sale_decimal.compareTo(BigDecimal.ZERO) == 1 && advice_sale_decimal.compareTo(old_advice_decimal) == -1) {
                offerRecordDO.setAdviceSalePrice(advice_sale_decimal);
            }
            if (prime_price_decimal.compareTo(BigDecimal.ZERO) == 1 && prime_price_decimal.compareTo(old_prime_price_decimal) == -1) {
                offerRecordDO.setPrimePriceTax(old_prime_price_decimal);
            }
            offerRecordDO.setUpdateId(user.getId());
            id = offerRecordService.updateRecord(offerRecordDO);
        }
        return id;
    }

    //保存car信息于数据库
    private List<Integer> saveCarList(HashMap<String, Object> car_list_map){
        UserDO user = monkeyJdbcRealm.getCurrentUser();
        List<Integer> resultList = new ArrayList<>();

        String displacement = "";
        if(car_list_map.containsKey("displacement")){
            displacement = (String) car_list_map.get("displacement");
        }
        String brand_name = "";
        if(car_list_map.containsKey("brand_name")){
            brand_name = (String) car_list_map.get("brand_name");
        }
        String company = "";
        if(car_list_map.containsKey("company")){
            company = (String) car_list_map.get("company");
        }
        //多个车系
        List<String> car_name_list = (List<String>) car_list_map.get("car_name_list");
        if(car_name_list == null || car_name_list.size() == 0){
            car_name_list = new ArrayList<>();
            car_name_list.add("");
        }
        //多个年款
        List<HashMap<String, Object>> year_list = (List<HashMap<String, Object>>) car_list_map.get("year_list");
        if(year_list == null){
            year_list = new ArrayList<>();
            HashMap<String, Object> map = new HashMap<>();
            map.put("start_year","");
            map.put("end_year","");
            year_list.add(map);
        }
        //多个排量
        List<String> displaceList = Arrays.asList(displacement.split("/"));
        for(String car_name : car_name_list){
            for(String displ : displaceList) {
                for (HashMap<String, Object> year_map : year_list) {
                    OfferCarDO offerCarDO = new OfferCarDO();
                    offerCarDO.setStartYear((String) year_map.get("start_year"));
                    offerCarDO.setEndYear((String) year_map.get("end_year"));
                    offerCarDO.setOfferCarName(car_name);
                    offerCarDO.setDisplacement(displ);
                    offerCarDO.setBrandName(brand_name);
                    offerCarDO.setCompany(company);
                    offerCarDO.setCreateId(user.getId());
                    offerCarDO.setStatus(OfferCarDO.新建_STATUS);
                    Integer car_id = offerCarService.insertWithOutExit(offerCarDO);
                    resultList.add(car_id);
                }
            }
        }

        return resultList;
    }

    //保存goods-car的关系
    private void saveRelation(Integer goods_id,List<Integer> car_id_list){
        for(Integer car_id : car_id_list){
            offerCarService.insertCarAndGoodsWithOutExit(car_id, goods_id);
        }
    }

    //判断最后数据必要信息是否完全
    private void judgeOk(HashMap<String, Object> goods_map,
                         HashMap<String, Object> record_map) {
        //缺少OE码商品，视为无效数据
        if (!goods_map.containsKey("oe_num")) {
            is_ok = false;
            fail_reason += " 缺少OE码";
        } else {
            String oe_num = (String) goods_map.get("oe_num");
            oe_num = oe_num.replace(".0", "");
            if ("".endsWith(oe_num) || "0".endsWith(oe_num)) {
                is_ok = false;
                fail_reason += " OE码不能未空或0";
            }
            if (oe_num.contains(".") || oe_num.contains("=") || oe_num.contains("+") || oe_num.contains("->") || oe_num.contains("/")) {
                is_ok = false;
                fail_reason += " OE码含('.''=''+''->''/')特殊字符";
            }
            if(oe_num.contains("\n")){
                is_ok = false;
                fail_reason += " OE码含多行";
            }

            String regEx = "[\u4e00-\u9fa5]";
            Pattern pat = Pattern.compile(regEx);
            Matcher matcher = pat.matcher(oe_num);
            if (matcher.find()) {
                is_ok = false;
                fail_reason += " OE码含中文";
            }
        }
        //配件名为空
        if (!goods_map.containsKey("goods_name")) {
            is_ok = false;
            fail_reason += " 缺少商品名称";
        }
        //商品标识号为空
        if (!goods_map.containsKey("goods_indexs")) {
            is_ok = false;
            fail_reason += " 缺少商品标识号";
        }
        String part_name = null;
        if (!goods_map.containsKey("part_name")) {
            is_ok = false;
            fail_reason += " 缺少淘气标准零件名称";
        }else {
            part_name = (String) goods_map.get("part_name");
        }

        String part_sum_code = null;
        if (!goods_map.containsKey("part_sum_code")) {
            is_ok = false;
            fail_reason += " 缺少淘气标准零件编码";
        } else {
            part_sum_code = (String) goods_map.get("part_sum_code");
            part_sum_code = part_sum_code.replace(".0", "");
            if ("".endsWith(part_sum_code) || "0".endsWith(part_sum_code)) {
                is_ok = false;
                fail_reason += " 淘气标准零件编码不能未空或0";
                part_sum_code = null;
            }
        }

        if(part_name !=null && part_sum_code != null){
            if(!part_map.keySet().contains(part_sum_code)){
                is_ok = false;
                fail_reason += " 标准零件编号不在数据库中";
            }else {
                String stablePartName = part_map.get(part_sum_code).getPartName();
                if (!stablePartName.equals(part_name)) {
                    is_ok = false;
                    fail_reason += " 标准零件名称跟编码对应错误";
                }
            }
        }

        //商品属性：原厂正厂等出错
        if (!goods_map.containsKey("goods_quality_type")) {
            is_ok = false;
            fail_reason += " 缺少产品属性";
        }
        //建议零售价不能为空
        if (!record_map.containsKey("advice_sale_price") || "0".endsWith((String) record_map.get("advice_sale_price"))) {
            record_map.put("advice_sale_price","0");
//            is_ok = false;
//            fail_reason += " 建议零售价不能为空或0";
        }else {
            String advice_sale_price = (String) record_map.get("advice_sale_price");
            String regEx = "[\u4e00-\u9fa5]";
            Pattern pat = Pattern.compile(regEx);
            Matcher matcher = pat.matcher(advice_sale_price);
            if (matcher.find()) {
                is_ok = false;
                fail_reason += " 建议零售价含中文";
            }
        }
        //
        //供应商名称为空
        if(!record_map.containsKey("provider_name")){
            is_ok = false;
            fail_reason += " 供应商名称为空";
        }
    }

    //对当前行的每一列进行数据处理
    private void columnProcess(List<String> rowList, HashMap<String, Object> goods_map,
                               HashMap<String, Object> record_map, HashMap<String, Object> car_list_map) {
        int column_num = rowList.size();

        //每一行处理时，错误信息初始化
        is_ok = true;
        fail_reason = "";
        fail_map = new HashMap<>();

        for (int i = 0; i < column_num; i++) {
            //不需要的数据或是无效数据，不处理
            if (!this.index_sql_map.containsKey(i)) {
                continue;
            }

            String column_value = rowList.get(i).trim().replaceAll(" +", "").replace("／", "/").replace("（", "(").replace("）", ")").replace("—","-").toUpperCase();

            if ("".equals(column_value) || "-".equals(column_value) ) {
                continue;
            }
            if("#N/A".equals(column_value)){
                is_ok = false;
                fail_reason += " sheet1存在#N/A数据,修改后再导入";
                break;
            }
            String index_sql_string = this.index_sql_map.get(i);
            String[] sql_cols_array = index_sql_string.split(" ");
            String sql_cols_name = sql_cols_array[0];
            String sql_cols_type = sql_cols_array[1];

            //初始有效数据的保留
            fail_map.put(sql_cols_name,column_value);

            //goods相关列的处理
            if (goods_table_name.equals(sql_cols_type)) {
                if ("0".equals(sql_cols_name)) {
                    sql_cols_name = "";
                }
                //商品属性（正厂原厂等等数据）进行处理
                if ("brand".equals(sql_cols_name)) {
                    goodsBrandProcess(column_value, goods_map);
                    continue;
                }
                goods_map.put(sql_cols_name, column_value);
                continue;
            }
            //record相关列
            if (record_table_name.equals(sql_cols_type)) {
                record_map.put(sql_cols_name, column_value);
                continue;
            }
            //car相关列
            if (car_table_name.equals(sql_cols_type)) {
                //排量的处理
                if ("displacement".equals(sql_cols_name)) {
                    car_list_map.put(sql_cols_name, column_value);
                    continue;
                }
                //年款的处理
                if ("year".equals(sql_cols_name)) {
                    carYearProcess(column_value, car_list_map, goods_map);
                    continue;
                }

                column_value = column_value.replace(".0", "");
                //淘气车系的处理
                if ("offer_car_name".equals(sql_cols_name)) {
                    carNameProcess(column_value, car_list_map);
                    continue;
                }
                //厂商和品牌
                if (column_value.contains("/")) {
                    is_ok = false;
                    fail_reason += " 淘汽品牌和厂商不可有‘/’";
                }
                car_list_map.put(sql_cols_name, column_value);
            }
        }

    }

    //处理car的车系信息
    private void carNameProcess(String car_name_value, HashMap<String, Object> car_list_map) {
        List<String> car_name_list = new ArrayList<>();
        String[] car_name_array = car_name_value.split("/");
        Collections.addAll(car_name_list, car_name_array);
        car_list_map.put("car_name_list", car_name_list);
    }

    //处理car的年款信息
    private void carYearProcess(String year_value, HashMap<String, Object> car_list_map, HashMap<String, Object> goods_map) {

        List<HashMap<String, Object>> year_list = new ArrayList<>();
        String[] year_array = year_value.split("/");
        for (String year_site : year_array) {
            HashMap<String, Object> year_map = new HashMap<>();
            String start_year = "", end_year = "";
            String remark = "";

            String[] start_end_year_array = year_site.split("-");
            int start_end_length = start_end_year_array.length;
            //只适用于当前年2013，2013-,-
            if (start_end_length == 1) {
                int size = 0;
                // -
                if("-".endsWith(year_site)){
                    start_year = "";
                    end_year = "";
                }else {
                    //2012-
                    if (year_site.endsWith("-")) {
                        end_year = "";
                        start_year = year_site.replace("-", "");
                        size = start_year.length();
                        if (size > 4) {
                            start_year = start_year.substring(0, 4);
                            remark = "适用年款：" + year_site;
                        }
                    }
                    //例如：2012
                    if (!year_site.contains("-")) {
                        String new_year_site = year_site.replace(".0", "");
                        size = year_site.length();
                        if (size > 4) {
                            new_year_site = new_year_site.substring(0, 4);
                            remark = "适用年款：" + year_site;
                        }
                        start_year = new_year_site;
                        end_year = new_year_site;
                    }


                    if (size < 4) {
                        is_ok = false;
                        fail_reason += " 年款存在小于4位的数字,无法区分出为何年";
                    }
                }
            }
            //2010-2015,2015-2014,-2012
            if (start_end_length == 2) {
                start_year = start_end_year_array[0];
                end_year = start_end_year_array[1];
                int start_length = start_year.length();
                int end_length = end_year.length();

                if ((!"".equals(start_year) && start_length < 4) || end_length < 4) {
                    is_ok = false;
                    fail_reason += " 年款存在小于4位的数字,无法区分出为何年";
                }

                if (start_length > 4) {
                    start_year = start_year.substring(0, 4);
                    remark = "适用年款：" + year_site;
                }
                if (end_length > 4) {
                    end_year = end_year.substring(0, 4);
                    remark = "适用年款：" + year_site;
                }
                if (!"".endsWith(start_year) && !"".endsWith(end_year) &&
                        Integer.parseInt(start_year) > Integer.parseInt(end_year)) {
                    is_ok = false;
                    fail_reason += " 年款中的开始时间不可大于结束时间";
                    start_year = start_end_year_array[0];
                    end_year = start_end_year_array[1];
                    remark = null;
                }
            }
            //存储开始和结束日期
            year_map.put("start_year", start_year);
            year_map.put("end_year", end_year);
            year_list.add(year_map);
            //存储备注
            if (goods_map.containsKey("remark")) {
                remark = goods_map.get("remark") + " " + remark;
            }
            goods_map.put("remark", remark);

        }
        car_list_map.put("year_list", year_list);
    }

    //处理商品goods的brand属性信息
    private void goodsBrandProcess(String brand_value, HashMap<String, Object> goods_map) {
        if ("".equals(brand_value)) {
            is_ok = false;
            fail_reason += " 商品属性不能为0";
            return;
        }
        goods_map.put("brand_name", "");

        String[] brand_array = brand_value.split("/");

        //对普通的（博士、 正厂原厂等等）数据进行处理
        if (brand_array.length == 1) {
            if (this.goods_quality_type_map.containsKey(brand_value)) {
                goods_map.put("goods_quality_type", this.goods_quality_type_map.get(brand_value));
                return;
            }
            goods_map.put("goods_quality_type", 0);
            goods_map.put("brand_name", brand_value);
            return;
        }

        //对（品牌/博士）这类数据进行处理
        String key = brand_array[0];
        if (!this.goods_quality_type_map.containsKey(key)) {
            is_ok = false;
            fail_reason += " 商品属性'/'前的属性非规定的属性，如'正厂'";
            goods_map.put("brand_name", brand_value);
            return;
        }
        goods_map.put("goods_quality_type", this.goods_quality_type_map.get(key));
        goods_map.put("brand_name", brand_array[1]);

    }

    //区分三个表
    private final static String goods_table_name = "goods";
    private final static String car_table_name = "car";
    private final static String record_table_name = "record";
    //存当前记录的名称---函数里设定其值
    private String record_name = null;
    //存伪进度
    private Double offerGoods_upload_percentage = 40.0;

    //传入的总条数、成功条数、失败条数
    private Integer sum_number = 0;
    private Integer success_number = 0;
    private Integer fail_number = 0;

    //不合格数据，不合格原因,不合格的数据
    private boolean is_ok = true;
    private String fail_reason;
    private HashMap<String,String> fail_map = new HashMap<>();


    //每一列对应的数据库和字段名
    private HashMap<Integer, String> index_sql_map = new HashMap<>();

    //第二个sheet的
    private HashMap<Integer, String> index_second_map = new HashMap<>();

    // 存商品车型关系
    private List<OfferLiCarDO> offerLiCarDOList;


    /**part_code : part_name **/
    private Map<String, CategoryPartDO> part_map = new HashMap<>();

    //标题对应的数据库和数据库列名
    private HashMap<String, String> sql_goods_map = new HashMap<String, String>() {
        {
            put("零件名称", "goods_name");
            put("商品名称", "goods_name");
            put("商品名称（供应商）", "goods_name");
            put("产品属性（商品品牌）", "brand");
            put("商品属性", "brand");

            put("零件编码OE码", "oe_num");
            put("零件编码(OE码)", "oe_num");
            put("OE码", "oe_num");

            put("备注说明", "remark");
            put("淘汽零件名称", "part_name");
            put("标准零件名称", "part_name");
            put("淘汽标准零件名称", "part_name");
            put("淘汽零件编码", "part_sum_code");
            put("标准零件编码", "part_sum_code");

            put("规格型号", "goods_format");
            put("包装规格", "package_format");
            put("单位", "measure_unit");
            put("购买单位", "measure_unit");
            put("易损件标识", "cate_kind");

            //商品标识号，给sheet2使用
            put("商品标识号", "goods_indexs");
            put("商品标识", "goods_indexs");

        }
    };

    //存序号和商品入库后的id
    private HashMap<String,Integer> index_goodsId_map = new HashMap<>();

    //TODO 第一个sheet的车型信息 暂时不处理，注释
    private HashMap<String, String> sql_car_map = new HashMap<String, String>() {

        {
//            put("发动机排量", "displacement");
//            put("排量", "displacement");
//
//            put("年款", "year");
//            put("淘汽车系", "offer_car_name");
//            put("淘汽品牌", "brand_name");
//            put("淘汽厂商", "company");
//            put("淘气车系", "offer_car_name");
//            put("淘气品牌", "brand_name");
//            put("淘气厂商", "company");
//
//            //暂时没用
//            put("淘气车型", "model");
//            put("淘汽车型", "model");
        }
    };
    private HashMap<String, String> sql_record_map = new HashMap<String, String>() {
        {
            put("供应淘气价格", "prime_price_tax");
            put("建议零售价", "advice_sale_price");
            put("建议销售价", "advice_sale_price");
            put("配件商名称", "provider_name");
            put("供应商名称", "provider_name");
            put("有效数据标识", "is_true");
            put("城市", "city");
        }
    };

    private HashMap<String, Object> goods_quality_type_map = new HashMap<String, Object>() {
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
