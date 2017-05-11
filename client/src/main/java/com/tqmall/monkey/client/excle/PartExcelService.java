package com.tqmall.monkey.client.excle;

import com.google.common.collect.Lists;
import com.tqmall.monkey.client.car.CarInfoAllService;
import com.tqmall.monkey.client.category.CategoryPartService;
import com.tqmall.monkey.client.part.*;
import com.tqmall.monkey.client.shiro.MonkeyJdbcRealm;
import com.tqmall.monkey.component.excelutils.CommonPOI;
import com.tqmall.monkey.component.utils.BigDataToMySql;
import com.tqmall.monkey.component.utils.UUIDGenerator;
import com.tqmall.monkey.dal.mapper.part.PartLiyangTableDOMapper;
import com.tqmall.monkey.domain.entity.UserDO;
import com.tqmall.monkey.domain.entity.car.CarInfoAllDO;
import com.tqmall.monkey.domain.entity.category.CategoryPartDO;
import com.tqmall.monkey.domain.entity.part.BasePartGoodDO;
import com.tqmall.monkey.domain.entity.part.PartLiyangRelationDO;
import com.tqmall.monkey.domain.entity.part.PartPictureDO;
import com.tqmall.monkey.domain.entity.part.PartSubjoinDO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 配件库excel导入处理模块
 * Created by zxg on 15/7/24.
 */

@Transactional
@Scope("prototype")
@Service
public class PartExcelService {
    Logger logger = LoggerFactory.getLogger(PartExcelService.class);

    private String headerSql = "LOAD DATA LOCAL INFILE 'sql.csv'  INTO TABLE modeldatas.TABLENAME";

    @Autowired
    private MonkeyJdbcRealm monkeyJdbcRealm;

    @Autowired
    private PartGoodsService partGoodsService;

    @Autowired
    private PartPictureService partPictureService;
    @Autowired
    private PartLiyangService partLiyangService;

    @Autowired
    private PartRelationService partRelationService;

    @Autowired
    private BigDataToMySql bigDataToMySql;

    @Autowired
    private CarInfoAllService carInfoAllService;

    @Autowired
    private CategoryPartService categoryPartService;

    @Autowired
    private PartSubjoinService subjoinService;
    @Autowired
    private PartLiyangTableDOMapper partLiyangTableDOMapper;



    public List<String> main_process(String file_path,String file_name,
                               String liyangBrand,
                               String liyangFactory,
                               String liyangSeries,
                               String liyangModel){

        this.liyangBrand = liyangBrand;
        this.liyangFactory = liyangFactory;
        this.liyangSeries = liyangSeries;
        this.liyangModel = liyangModel;

        tableCar_goods_pic_set = new HashSet<>();


        insertBaseGoodsList = new ArrayList<>();
        insertPartPictureList = new ArrayList<>();
        insertPartSubjoinList = new ArrayList<>();
        insertPartCarList = new ArrayList<>();


        insertExitBaseMap = new HashMap<>();
        insertExitPicMap = new HashMap<>();
        insertExitSubjoinMap = new HashMap<>();

        basePicOnlyList = new ArrayList<>();
        //处理时，错误信息初始化，对整个excle
        is_ok = true;
        failReasonList = new ArrayList<>();
        table_liyang_map = new HashMap<>();

        //将零件数据保存在内存中
        part_map = new HashMap<>();
        //oe 码有且仅有一个
        oe_part_map = new HashMap<>();

        List<CategoryPartDO> allPart = categoryPartService.getAllPartLists();
        for(CategoryPartDO categoryPartDO : allPart){
            part_map.put(categoryPartDO.getSumCode(), categoryPartDO.getPartName());
        }

        part_upload_percentage = 4.0;
        firstSheetcellNum = 0;
        secondSheetcellNum = 0;
        try {
            monkeyJdbcRealm.setSession("part_upload_percentage", "4%");
            InputStream stream = new FileInputStream(file_path);
            Workbook wb ;

            if(file_name.toUpperCase().endsWith(".XLSX")){
                wb = new XSSFWorkbook(stream);
            }else if(file_name.toUpperCase().endsWith(".XLS")){
                wb = new HSSFWorkbook(stream);

            }else{
                monkeyJdbcRealm.setSession("part_upload_percentage", "-1");
                logger.info("file isn't end with (.xls/.xlsx)");
                return Lists.newArrayList("file isn't end with (.xls/.xlsx)");
            }
            index_sql_map = new HashMap<>();
            Sheet sheet1 = wb.getSheetAt(0);
            StartSheet1(sheet1);

            if (is_ok) {
                index_sql_map = new HashMap<>();
                Sheet sheet2 = wb.getSheetAt(1);
                StartSheet2(sheet2);

            }


            if (is_ok) {
                //存relation
                saveRelation();
            }

            if(is_ok){
                // 删除缓存和数据库数据
                deleteAll();
                //批量大文件存储数据
                saveToDataBase();
            }
        } catch (Exception e) {
            monkeyJdbcRealm.setSession("part_upload_percentage", "-1");
            logger.debug(e.getMessage());
            failReasonList.add(e.getMessage());
            e.printStackTrace();
        }
        return failReasonList;
    }

    //==============批量存储数据库=====================
    private void deleteAll(){
        Integer partLId = partLiyangService.getPartLiyang(liyangBrand, liyangFactory, liyangSeries, liyangModel);
        //删除 此车型于 redis和数据库中
        partLiyangService.deletePartLiyang(partLId, liyangBrand, liyangFactory, liyangSeries, liyangModel);
        // 删除此 车下的所有配件关系
        partRelationService.deleteByPartLId(partLId,liyangBrand);
    }

    private void saveToDataBase(){

        String relationTableName = partLiyangTableDOMapper.selectByBrandName(this.liyangBrand);
        if(StringUtils.isEmpty(relationTableName)){
            failReasonList.add("此品牌无 关系子表，请联系开发添加后，再进行导入");
            return;
        }
        //存冗余力洋数据到数据库中
        Integer partLId = partLiyangService.savePartLiyang(this.liyangBrand, this.liyangFactory, this.liyangSeries, this.liyangModel);
        if(partLId == null){
            failReasonList.add("mysql 保存数据到part_liyang错误，请检查后重试。");
            return;
        }

        saveBaseGoodsToDataBase();
        percentage_add();
        savePicToDataBase();
        percentage_add();
        saveSubjoinToDataBase();
        percentage_add();
        savecarRelationToDataBase(partLId,relationTableName);
        percentage_add();

    }

    private void savecarRelationToDataBase(Integer partLId,String relationTableName) {
        List<PartLiyangRelationDO> list = this.insertPartCarList;


        long now = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = new Date(now );
        String nowTime = sdf.format(dt);
        if(list.size() > 0){
            logger.debug("=====relation size=="+list.size());
            UserDO userDO = monkeyJdbcRealm.getCurrentUser();
            Integer userId = userDO.getId();

            String relationSql = headerSql.replace("TABLENAME", relationTableName) +
                    "( goods_id, pic_id, liyang_id,subjoin_id,part_liyang_id," +
                    "    creator, gmt_modified, gmt_create)";
            StringBuilder builder = new StringBuilder();
            for(PartLiyangRelationDO relationDO : list){

                builder.append(relationDO.getGoodsId());
                builder.append("\t");
                builder.append(relationDO.getPicId());
                builder.append("\t");
                builder.append(relationDO.getLiyangId());
                builder.append("\t");
                builder.append(relationDO.getSubjoinId());
                builder.append("\t");
                builder.append(partLId);
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

    private void savePicToDataBase() {
        List<PartPictureDO> list = this.insertPartPictureList;

        long now = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = new Date(now );
        String nowTime = sdf.format(dt);
        if(list.size() > 0){
            logger.debug("=====pic size=="+list.size());
            UserDO userDO = monkeyJdbcRealm.getCurrentUser();
            Integer userId = userDO.getId();

            String picSql = headerSql.replace("TABLENAME", "db_monkey_part_picture") +
                    "( uuid,picture_num, picture_index, creator, gmt_modified, gmt_create)";
            StringBuilder builder = new StringBuilder();
            for(PartPictureDO pictureDO : list){
                builder.append(pictureDO.getUuId());
                builder.append("\t");
                builder.append(pictureDO.getPictureNum());
                builder.append("\t");
                builder.append(pictureDO.getPictureIndex());
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
            InputStream picInput = new ByteArrayInputStream(bytes);

            try {
                bigDataToMySql.bulkLoadFromInputStream(picSql,picInput);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveSubjoinToDataBase() {
        List<PartSubjoinDO> list = this.insertPartSubjoinList;

        long now = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = new Date(now );
        String nowTime = sdf.format(dt);
        if(list.size() > 0){
            logger.debug("=====subjoin size=="+list.size());
            UserDO userDO = monkeyJdbcRealm.getCurrentUser();
            Integer userId = userDO.getId();

            String picSql = headerSql.replace("TABLENAME", "db_monkey_part_subjoin") +
                    "( uuid,remarks, application_amount, creator, gmt_modified, gmt_create)";
            StringBuilder builder = new StringBuilder();
            for(PartSubjoinDO subjoinDO : list){
                builder.append(subjoinDO.getUuid());
                builder.append("\t");
                builder.append(subjoinDO.getRemarks());
                builder.append("\t");
                builder.append(subjoinDO.getApplicationAmount());
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
            InputStream picInput = new ByteArrayInputStream(bytes);

            try {
                bigDataToMySql.bulkLoadFromInputStream(picSql,picInput);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void saveBaseGoodsToDataBase(){

        List<BasePartGoodDO> list = this.insertBaseGoodsList;

        long now =System.currentTimeMillis();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = new Date(now );
        String nowTime = sdf.format(dt);
        //存基础goods表
        if(list.size() > 0) {
            logger.debug("=====base size=="+list.size());
            UserDO userDO = monkeyJdbcRealm.getCurrentUser();
            Integer userId = userDO.getId();

            String baseGoodsSql = headerSql.replace("TABLENAME", "db_monkey_part_goods_base") + "(uuid,oe_number,old_oe_number, part_name, part_code, first_cate_id, first_cate_code, second_cate_id," +
                    " second_cate_code, third_cate_id, third_cate_code, replace_history,factory_name,creator," +
                    " gmt_modified,gmt_create)";
            StringBuilder builder = new StringBuilder();
            for (BasePartGoodDO basePartGoodDO : list) {
                builder.append(basePartGoodDO.getUuId());
                builder.append("\t");
                builder.append(basePartGoodDO.getOeNumber());
                builder.append("\t");
                builder.append(basePartGoodDO.getOldOeNumber());
                builder.append("\t");
                builder.append(basePartGoodDO.getPartName());
                builder.append("\t");
                builder.append(basePartGoodDO.getPartCode());
                builder.append("\t");
                builder.append(basePartGoodDO.getFirstCateId());
                builder.append("\t");
                builder.append(basePartGoodDO.getFirstCateCode());
                builder.append("\t");
                builder.append(basePartGoodDO.getSecondCateId());
                builder.append("\t");
                builder.append(basePartGoodDO.getSecondCateCode());
                builder.append("\t");
                builder.append(basePartGoodDO.getThirdCateId());
                builder.append("\t");
                builder.append(basePartGoodDO.getThirdCateCode());
                builder.append("\t");
                String replaceHistory = basePartGoodDO.getReplaceHistory();
                if(replaceHistory == null) replaceHistory = "";
                builder.append(replaceHistory);
                builder.append("\t");
                String factoryName = basePartGoodDO.getFactoryName();
                if(factoryName == null) factoryName = "";
                builder.append(factoryName);
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
            InputStream baseGoodsInput = new ByteArrayInputStream(bytes);

            try {
                bigDataToMySql.bulkLoadFromInputStream(baseGoodsSql,baseGoodsInput);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //==============end 批量存储数据库=====================


    //处理第一个sheet中的数据
    private void StartSheet1(Sheet sheet)  {
        //第一行
        Integer firstRowNum = sheet.getFirstRowNum();

        Row firstRow = sheet.getRow(firstRowNum);
        Integer rowNum = sheet.getLastRowNum()+1;
        try {
            firstRowProcess(firstRow);

            //其余行
            for (int i = 1; i < rowNum; i++) {
                Row nextRow = sheet.getRow(i);

                if(null != nextRow) {
                    secondRowProcess(nextRow);
                    percentage_add();
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //第一个sheet-第一行进行处理
    private void firstRowProcess(Row firstRow)  {
        List<String> sqlNameList = new ArrayList<>();

        firstSheetcellNum = firstRow.getPhysicalNumberOfCells()+1;
        for (int i =0 ;i<firstSheetcellNum;i++) {
            String column_value;
            try {
                column_value = CommonPOI.getXSSFCellValue(firstRow.getCell(i)).trim().replaceAll(" +", "");
                if (this.excel_goods_map.containsKey(column_value)) {
                    String sql_name = this.excel_goods_map.get(column_value);
                    sqlNameList.add(sql_name);
                    this.index_sql_map.put(i, sql_name + " " + goods_table_name);
                    continue;
                }
                if (this.excel_car_map.containsKey(column_value)) {
                    String sql_name = this.excel_car_map.get(column_value);
                    sqlNameList.add(sql_name);
                    this.index_sql_map.put(i, sql_name + " " + car_table_name);
                    continue;
                }
                if (this.excel_picture_map.containsKey(column_value)) {
                    String sql_name = this.excel_picture_map.get(column_value);
                    sqlNameList.add(sql_name);
                    this.index_sql_map.put(i, sql_name + " " + picture_table_name);
                    continue;
                }
                if (this.excel_subjoin_map.containsKey(column_value)) {
                    String sql_name = this.excel_subjoin_map.get(column_value);
                    sqlNameList.add(sql_name);
                    this.index_sql_map.put(i, sql_name + " " + subjoin_table_name);
                }
            } catch (Exception e) {
                is_ok = false;
                failReasonList.add("sheet1第一行导入出错");
                e.printStackTrace();
            }

        }

        if(!sqlNameList.contains("picture_num")){
            is_ok = false;
            failReasonList.add("sheet1 无'原厂图号'列");
        }
        if(!sqlNameList.contains("picture_index")){
            is_ok = false;
            failReasonList.add("sheet1 无'原厂序号'列");
        }
        if(!sqlNameList.contains("oe_number")){
            is_ok = false;
            failReasonList.add("sheet1 无'OE码'列");
        }
        if(!sqlNameList.contains("car_number")){
            is_ok = false;
            failReasonList.add("sheet1 无'车型'列");
        }
        if(!sqlNameList.contains("part_name")){
            is_ok = false;
            failReasonList.add("sheet1 无'标准零件名称'列");
        }
        if(!sqlNameList.contains("part_code")){
            is_ok = false;
            failReasonList.add("sheet1 无'标准零件编码'列");
        }



    }
    //第一个sheet-第二行开始进行逻辑处理
    private void secondRowProcess(Row row)  {
        //判断数据有无效
        try {
            int firstCellNum = 0;
            Cell firstCell = row.getCell(firstCellNum);
            String indexes;
            indexes = CommonPOI.getXSSFCellValue(firstCell);
            if("".equals(indexes)){
                return;
            }
            HashMap<String, String> goods_map = new HashMap<>();
            HashMap<String, Object> picture_map = new HashMap<>();
            HashMap<String, String> subjoin_map = new HashMap<>();
            //EXCLE车型拆分为一个list对象
            Set<String> carList = new HashSet<>();

            //列的每个字段进行处理
            columnProcess(row, goods_map, picture_map, subjoin_map, carList);

            if(carList.isEmpty()){
                is_ok = false;
                failReasonList.add("序号为"+indexes+"缺少车型代码");
            }
            if(is_ok) {
                judgeOk(goods_map, picture_map, indexes);
            }

            //保存至goods和goodsBase数据库x，错误的话 叠加识别错误
            String baseUUId = saveGoods(goods_map, indexes);
            if (is_ok) {
                if(baseUUId != null && is_ok) {
                    String picUuId = savePicture(picture_map);
                    String subjoinUuId = saveSubjoin(subjoin_map);
                    //生成map，判断其基本base和pic的组成是否出现过basePicOnlyList
                    HashMap<String,String> basePicMap = new HashMap<>();
                    basePicMap.put("baseUUId", baseUUId);
                    basePicMap.put("picUuId", picUuId);
                    basePicMap.put("subjoinUuId", subjoinUuId);
                    if(basePicOnlyList.contains(basePicMap)){
                        is_ok = false;
                        failReasonList.add("序号为 "+ indexes+" 除车型外其他项均相同--出现次数大于1次,即sheet1中出现多次相同数据");
                    }else{
                        basePicOnlyList.add(basePicMap);
                    }
                    //临时保存关系
                    saveLiyangAndGoodsAndPic(carList, baseUUId, picUuId,subjoinUuId);
                }
                percentage_add();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    //对当前行的每一列进行数据处理
    private void columnProcess(Row row, HashMap<String, String> goods_map,
                               HashMap<String, Object> picture_map,HashMap<String, String> subjoin_map, Set<String> carList) throws Exception {
        int firstCellNum = 0;
        Cell firstCell = row.getCell(firstCellNum);
        String indexs = CommonPOI.getXSSFCellValue(firstCell);
        for (int i =0 ;i<firstSheetcellNum;i++) {

            //不需要的数据或是无效数据，不处理
            if (!this.index_sql_map.containsKey(i)) {
                continue;
            }

            String old_column = CommonPOI.getXSSFCellValue(row.getCell(i)).replace("\\", "/").trim();
            String column_value = old_column.replaceAll(" +", "").replace("／", "/").replace("（", "(").replace("）", ")").replace("—", "-").replace("“", "\"").replace("”", "\"").replace("\"","'").replace("，",",").toUpperCase();

            if ("".equals(column_value) || "-".equals(column_value) ) {
                continue;
            }
            if("＃N/A".equals(column_value)){
                is_ok = false;
                failReasonList.add("sheet1存在＃N/A数据,修改后再导入");
                break;
            }
            String index_sql_string = this.index_sql_map.get(i);
            String[] sql_cols_array = index_sql_string.split(" ");
            String sql_cols_name = sql_cols_array[0];
            String sql_cols_type = sql_cols_array[1];

            //goods相关列的处理
            if (goods_table_name.equals(sql_cols_type)) {
                if ("0".equals(sql_cols_name)) {
                    sql_cols_name = "";
                }
                goods_map.put(sql_cols_name, column_value);
                if(sql_cols_name.equals("oe_number")){
                    goods_map.put(sql_cols_name, column_value.replace("-", ""));
                    goods_map.put("old_oe_number", old_column);
                }
                continue;
            }
            //pic相关列
            if (picture_table_name.equals(sql_cols_type)) {
                picture_map.put(sql_cols_name, column_value);
                continue;
            }
            //额外增加的列--备注和用量
            if (subjoin_table_name.equals(sql_cols_type)) {
                subjoin_map.put(sql_cols_name, column_value);
                continue;
            }

            //car相关列
            if (car_table_name.equals(sql_cols_type)) {
                //判断是否偶数长度，若无，则格式错误（因为均为两个A1等组成）
                if(column_value.length()%2 != 0){
                    is_ok = false;
                    failReasonList.add("序号为 "+indexs+" 的车型代码出现(例如：A11)的非字母+数字");
                }else{
                    //切割车型代码
                    while(column_value.length() > 0) {
                        String oneCar = column_value.substring(0, 2);//截取s中从begin开始至end结束时的字符串，并将其赋值给oneCar;
                        //对格式进行判断
                        Pattern pattern = Pattern.compile("[A-Z][A-Za-z0-9]");
                        Matcher matcher = pattern.matcher(oneCar);
                        boolean matcher_result= matcher.matches();
                        //格式为英文+字母
                        if(matcher_result) {
                            carList.add(oneCar);
                            column_value = column_value.substring(2); //截取掉s从首字母起长度为2的字符串，将剩余字符串赋值给s；
                        }else{
                            is_ok = false;
                            failReasonList.add("序号为"+indexs+"的车型代码格式错误,错误的代码："+oneCar);
                            break;
                        }
                    }
                }

            }
        }

    }



    //处理第2个sheet中的数据
    private void StartSheet2(Sheet sheet) throws Exception {
        //第一行
        Integer firstRowNum = sheet.getFirstRowNum();

        Row firstRow = sheet.getRow(firstRowNum);
        secondFirstRowProcess(firstRow);

        Integer rowNum = sheet.getLastRowNum()+1;
        //其余行
        for(int i = 1;i<rowNum;i++){
            Row nextRow = sheet.getRow(i);

            if(null != nextRow && is_ok) {
                secondSheetRowProcess(nextRow);
                percentage_add();
            }

        }
    }

    //第二个sheet-第yi行开始进行遍历
    public void secondFirstRowProcess(Row firstRow) throws Exception {
        this.index_second_map = new HashMap<>();
        secondSheetcellNum = firstRow.getPhysicalNumberOfCells();
        for (int i =0 ;i<secondSheetcellNum;i++) {
            String column_value = CommonPOI.getXSSFCellValue(firstRow.getCell(i)).trim().replaceAll(" +", "");

            if(!"".equals(column_value)) {
                this.index_second_map.put(i, column_value);
            }
        }
    }

    //第二行开始数据处理
    private void secondSheetRowProcess(Row row) throws Exception {

        String tableCar = "";
        String liyangId = "";
        for (int i =0 ;i<secondSheetcellNum;i++) {
            String column_value = CommonPOI.getXSSFCellValue(row.getCell(i)).trim().replaceAll(" +", "").replace("／", "/").replace("（", "(").replace("）", ")").replace("—", "-")
                    .replace("“","\"").replace("”","\"").replace("\"","'").replace("\\","/").replace("，",",").toUpperCase();

            if("".equals(column_value)) {
                continue;
            }
            if("＃N/A".equals(column_value)){
                is_ok = false;
                failReasonList.add("sheet2存在＃N/A数据,修改后再导入");
                return;
            }

            String firstName = this.index_second_map.get(i);
            if(null == firstName){
                is_ok = false;
                failReasonList.add("检查第二个sheet的第一行有无文字 ");
                return;
            }
            if(firstName.contains("车型") || firstName.contains("代码")){
                tableCar = column_value;
            }else if(firstName.contains("力洋") || firstName.contains("Level")){
                liyangId = column_value;
            }
        }

//        String tableCar = rowlist.get(0).trim().replaceAll(" +", "").replace("／", "").replace("（", "").replace("）", "").toUpperCase();
//        String liyangId = rowlist.get(1).trim().replaceAll(" +", "").replace("／", "").replace("（", "").replace("）", "").toUpperCase();

        if("".equals(tableCar) || "".equals(liyangId)){
            return;
        }
        //力洋数据为null
        if(liyangId.equalsIgnoreCase("null") || liyangId.equalsIgnoreCase("none")){
            is_ok = false;
            failReasonList.add("sheet2中"+tableCar+"力洋代码不能为null");
            return;
        }

        CarInfoAllDO carInfoAllDO = carInfoAllService.findCarInfoByLeyelId(liyangId);
        if(carInfoAllDO == null){
            is_ok = false;
            failReasonList.add(tableCar+"对应的"+liyangId+" 无此力洋编码");
            return;
        }
        String brand = carInfoAllDO.getCarBrand();
        String factoryName = carInfoAllDO.getFactoryName();
        String series = carInfoAllDO.getCarSeries();
        String model = carInfoAllDO.getVehicleType();
        if( !factoryName.endsWith(this.liyangFactory) || !brand.endsWith(this.liyangBrand)
                || !series.endsWith(this.liyangSeries) || !model.endsWith(this.liyangModel)){
            is_ok = false;
            failReasonList.add(liyangId+"跟选择的车型不统一，应为："+brand+"-"+factoryName+"-"+series+"-"+model);
        }

        //将二者关系保存至map中
        if(this.table_liyang_map.containsKey(tableCar) && !liyangId.equals(this.table_liyang_map.get(tableCar))){
            is_ok = false;
            failReasonList.add("sheet2中"+tableCar+"车型代码对应多个不同力洋车型");
            return;
        }
        if(is_ok) {
            this.table_liyang_map.put(tableCar, liyangId);
        }
    }




    //将sheet1中的数据对应关系，从sheet2中取出liyangID，存入库
    private void saveRelation(){
        if(!is_ok){
            return;
        }
//        //表格车和力洋车的对应关系


        for(Map<String,Object> relationMap : tableCar_goods_pic_set){
            String tableCar = (String) relationMap.get("tableCar");
            String liyangId = table_liyang_map.get(tableCar);

            if(liyangId == null){
                is_ok = false;
                failReasonList.add(tableCar+" 该车型代码不存在sheet2 中，请改正后重新上传");
                break;
            }

            String goodsId = (String) relationMap.get("goodsId");
            String picId = (String) relationMap.get("picId");
            String subjoinUuId = (String) relationMap.get("subjoinUuId");

            PartLiyangRelationDO partLiyangRelationDO = new PartLiyangRelationDO();
            partLiyangRelationDO.setGoodsId(goodsId);
            partLiyangRelationDO.setPicId(picId);
            partLiyangRelationDO.setLiyangId(liyangId);
            partLiyangRelationDO.setSubjoinId(subjoinUuId);

            insertPartCarList.add(partLiyangRelationDO);

        }


    }

    //临时保存关系
    private void saveLiyangAndGoodsAndPic(Set<String> carList,String goodsId,String picId,String subjoinUuId){
        for (String tableCar : carList){
            HashMap<String,Object> relationMap = new HashMap<>();
            relationMap.put("tableCar", tableCar);
            relationMap.put("goodsId", goodsId);
            relationMap.put("picId", picId);
            relationMap.put("subjoinUuId", subjoinUuId);

            tableCar_goods_pic_set.add(relationMap);
        }
    }
    //保存数据到part_goods相关的库,返回自增主键goods和baseGoods
    private String saveGoods(HashMap<String, String> goods_map,String indexes){

        BasePartGoodDO basePartGoodDO = new BasePartGoodDO();
        BasePartGoodDO exitbasePartGoodDO = new BasePartGoodDO();
        String oeNumber ="";
        String oldOeNumber ="";
        String partCode = "";
        //赋值
        for(Map.Entry<String,String> goodsEntry : goods_map.entrySet()){
            String key = goodsEntry.getKey();
            String value = goodsEntry.getValue();
            switch (key){
                case "oe_number":{
                    oeNumber = value;
                    basePartGoodDO.setOeNumber(value);
                    exitbasePartGoodDO.setOeNumber(value);
                    break;
                }
                case "part_name":{
                    basePartGoodDO.setPartName(value);
//                    exitbasePartGoodDO.setPartName(value);
                    break;
                }
                case "part_code":{
                    partCode = value;
                    basePartGoodDO.setPartCode(value);
//                    exitbasePartGoodDO.setPartCode(value);
                    break;
                }
                case "old_oe_number":basePartGoodDO.setOldOeNumber(value);oldOeNumber=value;break;
                case "replace_history":basePartGoodDO.setReplaceHistory(value);break;
                case "factory_name":basePartGoodDO.setFactoryName(value);break;
                default:break;
            }
        }


        String baseUUId;
        if(this.insertExitBaseMap.containsKey(exitbasePartGoodDO)){
            baseUUId = this.insertExitBaseMap.get(exitbasePartGoodDO);
        }else {
            BasePartGoodDO theDO = partGoodsService.getBaseGoodsUUIdWithExist(exitbasePartGoodDO);
            if (null == theDO) {
                baseUUId = UUIDGenerator.getUUID();
                this.insertExitBaseMap.put(exitbasePartGoodDO,baseUUId);
                //获得分类信息
                String sumCode = basePartGoodDO.getPartCode();

                CategoryPartDO partDO = categoryPartService.findCategoryPartBySumCode(sumCode);

                String firstCode = sumCode.substring(0, 2);
                String secondCode = sumCode.substring(2, 4);
                String thirdCode = sumCode.substring(4, 7);

                Integer firstId = partDO.getFirstCatId();
                Integer secondId = partDO.getSecondCatId();
                Integer thirdId = partDO.getThirdCatId();



                basePartGoodDO.setFirstCateId(firstId);
                basePartGoodDO.setFirstCateCode(firstCode);
                basePartGoodDO.setSecondCateId(secondId);
                basePartGoodDO.setSecondCateCode(secondCode);
                basePartGoodDO.setThirdCateId(thirdId);
                basePartGoodDO.setThirdCateCode(thirdCode);
                basePartGoodDO.setUuId(baseUUId);
                insertBaseGoodsList.add(basePartGoodDO);

                oe_part_map.put(oeNumber,sumCode);
            }else{
                baseUUId = theDO.getUuId();
                this.insertExitBaseMap.put(exitbasePartGoodDO,baseUUId);
                oe_part_map.put(oeNumber,theDO.getPartCode());
            }
        }

        String oldPartCode = oe_part_map.get(oeNumber);
        if(!oldPartCode.equals(partCode)){
            is_ok = false;
            failReasonList.add(""+oldOeNumber+" oe码的标准零件编码在数据库或当前表存在多份，历史编码为："+oldPartCode);
        }


        return baseUUId;
    }

    //保存图片信息，返回uuid
    private String savePicture(HashMap<String, Object> picture_map){
        PartPictureDO partPictureDO = new PartPictureDO();
        PartPictureDO existPartPictureDO = new PartPictureDO();

        String pictureIndex = (String) picture_map.get("picture_index");
        String pictureNum = (String) picture_map.get("picture_num");

        existPartPictureDO.setPictureIndex(pictureIndex);
        existPartPictureDO.setPictureNum(pictureNum);

        partPictureDO.setPictureIndex(pictureIndex);
        partPictureDO.setPictureNum(pictureNum);

        String picUuId;
        if(this.insertExitPicMap.containsKey(existPartPictureDO)){
            picUuId = this.insertExitPicMap.get(existPartPictureDO);
        }else {
            picUuId = partPictureService.getUuIdWithExist(partPictureDO);
            if (null == picUuId) {
                picUuId = UUIDGenerator.getUUID();
                this.insertExitPicMap.put(existPartPictureDO,picUuId);

                partPictureDO.setUuId(picUuId);
                insertPartPictureList.add(partPictureDO);
            }else{
                this.insertExitPicMap.put(existPartPictureDO,picUuId);
            }
        }
        return picUuId;
    }
    //保存额外信息，返回uuid
    private String saveSubjoin(HashMap<String, String> subjoin_map){
        PartSubjoinDO subjoinDO = new PartSubjoinDO();
        PartSubjoinDO existsubjoinDO = new PartSubjoinDO();

        Integer amount = 0;
        String remarks = "";
        if(subjoin_map.containsKey("application_amount")) {
            String application_amount = subjoin_map.get("application_amount");
            if(!"X".equals(application_amount)) {
                amount = Integer.valueOf(application_amount);
            }
        }
        if(subjoin_map.containsKey("remarks")) {
            remarks = subjoin_map.get("remarks");
        }

        subjoinDO.setApplicationAmount(amount);
        existsubjoinDO.setApplicationAmount(amount);
        subjoinDO.setRemarks(remarks);
        existsubjoinDO.setRemarks(remarks);

        String subjoinUuId;
        if(this.insertExitSubjoinMap.containsKey(existsubjoinDO)){
            subjoinUuId = this.insertExitSubjoinMap.get(existsubjoinDO);
        }else {
            subjoinUuId = subjoinService.getUuIdWithExist(existsubjoinDO);
            if (null == subjoinUuId) {
                subjoinUuId = UUIDGenerator.getUUID();
                this.insertExitSubjoinMap.put(existsubjoinDO,subjoinUuId);

                subjoinDO.setUuid(subjoinUuId);
                insertPartSubjoinList.add(subjoinDO);
            }else{
                this.insertExitSubjoinMap.put(existsubjoinDO, subjoinUuId);
            }
        }
        return subjoinUuId;
    }

    //判断数据是否无效
    private void judgeOk(HashMap<String,String> goods_map,HashMap<String, Object> picture_map,String indexs){
        //OE码
        if (!goods_map.containsKey("oe_number")) {
            is_ok = false;
            failReasonList.add("序号为"+indexs+"缺少OE码");
        } else {
            String oe_num = goods_map.get("oe_number");
            oe_num = oe_num.replace(".0", "");
            goods_map.put("oe_number",oe_num);
            if ("".endsWith(oe_num) || "0".endsWith(oe_num)) {
                is_ok = false;
                failReasonList.add("序号为"+indexs+"的OE码不能为空或0");
            }

            if (oe_num.contains("\n")){
                is_ok = false;
                failReasonList.add("序号为"+indexs+" OE码含多行");
            }

            String oe_re = oe_num.replace("#","");//不检测 # 符号数据
            String specialEx = "[^\\w]";
            Pattern specialPat = Pattern.compile(specialEx);
            Matcher specialMatcher = specialPat.matcher(oe_re);
            if (specialMatcher.find()) {
                is_ok = false;
                failReasonList.add("序号为"+indexs+"的OE码含特殊字符");
            }
            String regEx = "[\u4e00-\u9fa5]";
            Pattern pat = Pattern.compile(regEx);
            Matcher matcher = pat.matcher(oe_num);
            if (matcher.find()) {
                is_ok = false;
                failReasonList.add("序号为"+indexs+"的OE码含中文");
            }
        }

        //零件名称
        if(!goods_map.containsKey("part_name")) {
            is_ok = false;
            failReasonList.add("序号为"+indexs+"的无标准零件名");
        }

        //零件编号
        if(!goods_map.containsKey("part_code")) {
            is_ok = false;
            failReasonList.add("序号为"+indexs+"的无标准零件编号");
        }else {

            String part_name = goods_map.get("part_name");
            String part_code = goods_map.get("part_code");
            //判断零件编号和名字是否相同
            if (!part_map.keySet().contains(part_code)) {
                is_ok = false;
                failReasonList.add("序号为"+indexs+"的标准零件编号不在数据库中");
            } else {
                String stablePartName = part_map.get(part_code);
                if (!stablePartName.equals(part_name)) {
                    is_ok = false;
                    failReasonList.add("序号为"+indexs+"的标准零件名称跟编码对应错误");
                }
            }
        }
        /*put("图号", "picture_num");
            put("序号", "picture_index");*/
        if(!picture_map.containsKey("picture_num")) {
            is_ok = false;
            failReasonList.add("序号为"+indexs+"的无图号");
        }
        if(!picture_map.containsKey("picture_index")) {
            is_ok = false;
            failReasonList.add("序号为"+indexs+"的无图序号");
        }


    }

    //伪造进程的百分比
    private void percentage_add(){
        BigDecimal b   =   new   BigDecimal(part_upload_percentage);
        part_upload_percentage   =   b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue();
        if(part_upload_percentage < 45.0){
            part_upload_percentage = part_upload_percentage+0.1;
        }else if(part_upload_percentage < 75.0){
            part_upload_percentage = part_upload_percentage+0.01;
        }else if(part_upload_percentage < 98.0){
            part_upload_percentage = part_upload_percentage+0.001;
        }
        monkeyJdbcRealm.setSession("part_upload_percentage", part_upload_percentage+"%");
    }

    //区分4个表
    private final static String goods_table_name = "goods";
    private final static String car_table_name = "car";
    private final static String picture_table_name = "picture";
    private final static String subjoin_table_name = "subjoin";
    //操作的用户
    //力洋品牌、厂商、车系、车型
    private  String liyangBrand;
    private  String liyangFactory;
    private  String liyangSeries;
    private  String liyangModel;

    //识别列数
    private Integer firstSheetcellNum = 0;
    private Integer secondSheetcellNum = 0;

    //要插入的数据
    private List<BasePartGoodDO> insertBaseGoodsList;
    private List<PartPictureDO> insertPartPictureList;
    private List<PartSubjoinDO> insertPartSubjoinList;
    private List<PartLiyangRelationDO> insertPartCarList;

    private Map<BasePartGoodDO,String> insertExitBaseMap;
    private Map<PartPictureDO,String> insertExitPicMap;
    private Map<PartSubjoinDO,String> insertExitSubjoinMap;

    //baseGoods 和pic 唯一
    private List<Map<String,String>> basePicOnlyList ;

    //存伪进度
    private Double part_upload_percentage = 5.0;
    //不合格数据，不合格原因,不合格的数据
    private boolean is_ok = true;

    private List<String> failReasonList = new ArrayList<>();

    //表格车的car和goods和pic的关系
    private Set<Map<String,Object>> tableCar_goods_pic_set = new HashSet<>();
    //表格车和力洋车的对应关系
    private Map<String,String> table_liyang_map = new HashMap<>();

    //每一列对应的数据库和字段名
    private Map<Integer, String> index_sql_map = new HashMap<>();
    //第二个sheet的
    private Map<Integer, String> index_second_map = new HashMap<>();

    /**part_code : part_name **/
    private Map<String, String> part_map = new HashMap<>();

    /*oe_number:part_code*/
    private Map<String, String> oe_part_map = new HashMap<>();


    //excel中的每个字段区分
    private Map<String, String> excel_car_map = new HashMap<String, String>() {
        {
            put("车型代码", "car_number");
            put("车型", "car_number");
        }
    };
    private Map<String, String> excel_goods_map = new HashMap<String, String>() {
        {

            // 原始oe号 old_oe_number
            put("原厂OE码", "oe_number");
            put("OE码", "oe_number");

            put("原厂零件名称", "factory_name");



            put("标准名称", "part_name");
            put("标准零件名称", "part_name");
            put("标准编码", "part_code");
            put("标准零件ID", "part_code");
            put("标准零件编码", "part_code");
            put("替换历史", "replace_history");
        }
    };
    private Map<String, String> excel_picture_map = new HashMap<String, String>() {
        {
            put("图号", "picture_num");
            put("原厂图号", "picture_num");
            put("原厂序号", "picture_index");
        }
    };
    private Map<String, String> excel_subjoin_map = new HashMap<String, String>() {
        {
            put("备注", "remarks");
            put("用量", "application_amount");
        }
    };
}
