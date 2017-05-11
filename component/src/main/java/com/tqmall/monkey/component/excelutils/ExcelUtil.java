package com.tqmall.monkey.component.excelutils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * Created by huangzhangting
 * 新版本poi工具类，还需后续维护
 *
 */
@Slf4j
public class ExcelUtil {

    public static short ExcelTitleFontSize = (short)18;
    public static short TableHeadFontSize = (short)11;
    public static short TableHeadFontSize_sup = (short)14;
    public static short TableContentFontSize = (short)13;
    public static short TableContentFontSize2 = (short)10;
    public static String TableFontName = "宋体";

    public static float ExcelTitleRowHeight = 50f;
    public static float TableHeadRowHeight = 22f;
    public static float TableContentRowHeight = 20f;

    public static float DefaultRowHeight = 20f;

    public static int DefaultColumnWith = 3500;


    //excel标题样式
    public CellStyle getExcelTitleStyle(Workbook wb){

        Font font = wb.createFont();
        font.setFontHeightInPoints(ExcelUtil.ExcelTitleFontSize);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setFontName(TableFontName);

        CellStyle style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setWrapText(true);
        style.setFont(font);

        return style;
    }

    //表格的表头样式
    public CellStyle getTableHeadStyle(Workbook wb){
        Font font = wb.createFont();
        font.setFontHeightInPoints(ExcelUtil.TableHeadFontSize);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setFontName(TableFontName);

        CellStyle style = wb.createCellStyle();
        style.setFont(font);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setWrapText(true);
        style.setBorderBottom((short) 1);
        style.setBorderLeft((short) 1);
        style.setBorderRight((short) 1);
        style.setBorderTop((short) 1);


        return style;
    }

    //表格的表头样式
    public CellStyle getTableHeadStyle_sup(Workbook wb){
        Font font = wb.createFont();
        font.setFontHeightInPoints(ExcelUtil.TableHeadFontSize_sup);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setFontName(TableFontName);

        CellStyle style = wb.createCellStyle();
        style.setFont(font);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setWrapText(true);
        style.setBorderBottom((short)1);
        style.setBorderLeft((short)1);
        style.setBorderRight((short)1);
        style.setBorderTop((short)1);

        return style;
    }

    //表格内容样式   水平垂直都剧中，有边框
    public CellStyle getStyle_CENTER_CENTER(Workbook wb, boolean boldWeight){
        Font font = wb.createFont();
        font.setFontHeightInPoints(ExcelUtil.TableContentFontSize);
        if(boldWeight){
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        }
        font.setFontName(TableFontName);

        CellStyle style = wb.createCellStyle();
        style.setFont(font);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setWrapText(true);
        style.setBorderBottom((short)1);
        style.setBorderLeft((short)1);
        style.setBorderRight((short)1);
        style.setBorderTop((short)1);

        return style;
    }

    //表格内容样式   水平居左，垂直剧中，有边框
    public CellStyle getStyle_LEFT_CENTER(Workbook wb, boolean boldWeight, boolean important){
        Font font = wb.createFont();
        font.setFontHeightInPoints(ExcelUtil.TableContentFontSize2);
        if(boldWeight){
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        }
        font.setFontName(TableFontName);

        CellStyle style = wb.createCellStyle();
        style.setFont(font);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//        style.setWrapText(true);
        style.setBorderBottom((short)1);
        style.setBorderLeft((short)1);
        style.setBorderRight((short)1);
        style.setBorderTop((short)1);
        if(important){
            style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            style.setFillForegroundColor(HSSFColor.YELLOW.index);
        }

        return style;
    }

    //表格内容样式   水平居右，垂直剧中，有边框
    public CellStyle getStyle_RIGHT_CENTER(Workbook wb, boolean boldWeight){
        Font font = wb.createFont();
        font.setFontHeightInPoints(ExcelUtil.TableContentFontSize);
        if(boldWeight){
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        }
        font.setFontName(TableFontName);

        CellStyle style = wb.createCellStyle();
        style.setFont(font);
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setWrapText(true);
        style.setBorderBottom((short)1);
        style.setBorderLeft((short)1);
        style.setBorderRight((short)1);
        style.setBorderTop((short)1);

        return style;
    }

    //表格内容样式  水平居左，垂直居上，有边框
    public CellStyle getStyle_LEFT_TOP(Workbook wb){
        Font font = wb.createFont();
        font.setFontHeightInPoints(ExcelUtil.TableContentFontSize);
        font.setFontName(TableFontName);

        CellStyle style = wb.createCellStyle();
        style.setFont(font);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        style.setWrapText(true);
        style.setBorderBottom((short) 1);
        style.setBorderLeft((short) 1);
        style.setBorderRight((short) 1);
        style.setBorderTop((short) 1);

        return style;
    }

    //表格内容样式   水平右对齐，垂直居中，无边框
    public CellStyle getStyle_RIGHT_CENTER_noBorder(Workbook wb, boolean boldWeight){
        Font font = wb.createFont();
        font.setFontHeightInPoints(ExcelUtil.TableContentFontSize);
        if(boldWeight){
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        }
        font.setFontName(TableFontName);


        CellStyle style = wb.createCellStyle();
        style.setFont(font);
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setWrapText(true);

        return style;
    }

    //输出excel到系统文件
    public void writeExcelToSystem(Workbook wb, String excelName, String type, String filePath) throws Exception{
//        excelName = excelName+"-"+DateUtils.dateToString(new Date(), DateUtils.yyyyMMdd);

        OutputStream os = null;
        try {
            String excel = filePath+'/'+excelName+type;
            log.info("write excel:{}", excel);

            File file = new File(excel);
            if(file.exists()){
                file.delete();
            }
            os = new FileOutputStream(file);
            wb.write(os);
            os.flush();
        } catch (Exception e) {
            log.error("write excel error", e);
            throw e;
        }finally {
            if(os != null){
                try {
                    os.close();
                } catch (IOException e) {
                    log.error("close output stream error", e);
                }
            }
        }
    }

    //导出xls
    public void exportXLS(String excelName, String filePath, String[] headList, String[] fieldList,
                          Collection<?> dataList) throws Exception {
        exportXLS(excelName, filePath, headList, fieldList, dataList, null, 0, 1);
    }

    public void exportXLS(String excelName, String filePath, String[] headList, String[] fieldList, Collection<?> dataList,
                           int[] columnWith, int freezeCol, int freezeRow) throws Exception {
        Workbook workbook = new HSSFWorkbook();
        handleWorkbookWithBean(workbook, headList, fieldList, dataList, columnWith, freezeCol, freezeRow);
        writeExcelToSystem(workbook, excelName, ".xls", filePath);
    }

    //导出xlsx
    public void exportXLSX(String excelName, String filePath, String[] headList, String[] fieldList,
                           Collection<?> dataList) throws Exception {
        exportXLSX(excelName, filePath, headList, fieldList, dataList, null, 0, 1);
    }

    public void exportXLSX(String excelName, String filePath, String[] headList, String[] fieldList, Collection<?> dataList,
                           int[] columnWith, int freezeCol, int freezeRow) throws Exception {
        Workbook workbook = new SXSSFWorkbook();
        handleWorkbookWithBean(workbook, headList, fieldList, dataList, columnWith, freezeCol, freezeRow);
        writeExcelToSystem(workbook, excelName, ".xlsx", filePath);
    }

    public void handleWorkbookWithBean(Workbook wb, String[] headList, String[] fieldList, Collection<?> dataList,
                             int[] columnWith, int freezeCol, int freezeRow) throws Exception {

        Sheet sheet = initSheet(wb, "sheet1", headList, columnWith, freezeCol, freezeRow);

        CellStyle style = getStyle_LEFT_CENTER(wb, false, false);

        int rowNum = 0;
        int size = headList.length;
        Field f;
        Object value;
        for(Object data : dataList){
            Row row = sheet.createRow(++rowNum);
            row.setHeightInPoints(TableContentRowHeight);

            Class cla = data.getClass();
            for(int i=0; i<size; i++){
                Cell cell = row.createCell(i);
                cell.setCellStyle(style);

                String fn = fieldList[i];
                if(fn.contains(".")){
                    String[] fns = fn.split(".");
                    f = cla.getDeclaredField(fns[0]);
                    f.setAccessible(true);
                    //引用对象
                    Object refObj = f.get(data);
                    Class refCla = refObj.getClass();
                    f = refCla.getDeclaredField(fns[1]);
                    f.setAccessible(true);
                    value = f.get(refObj);
                    cell.setCellValue(value==null?"":value.toString());
                }else{
                    f = cla.getDeclaredField(fn);
                    f.setAccessible(true);
                    value = f.get(data);
                    cell.setCellValue(value==null?"":value.toString());
                }
            }
        }

    }

    protected Sheet initSheet(Workbook wb, String name, String[] headList, int[] columnWith, int freezeCol, int freezeRow){
        Sheet sheet = wb.createSheet(name);
        sheet.setDefaultRowHeightInPoints(DefaultRowHeight);

        if (freezeCol < 0) {
            freezeCol = 0;
        }
        if (freezeRow < 0) {
            freezeRow = 0;
        }
        sheet.createFreezePane(freezeCol, freezeRow);//冻结 列、行

        Row row = sheet.createRow(0);
        row.setHeightInPoints(TableHeadRowHeight);

        CellStyle style = getStyle_LEFT_CENTER(wb, true, false);

        int size = headList.length;
        for (int i = 0; i < size; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(headList[i]);
            cell.setCellStyle(style);
            if (columnWith != null) {
                sheet.setColumnWidth(i, columnWith[i]);
            } else {
                sheet.setColumnWidth(i, DefaultColumnWith);
            }
        }

        return sheet;
    }


    //导出xls
    public void exportXlsWithMap(String excelName, String filePath, String[] headList,
                                 Collection<Map<String, String>> dataList) throws Exception {
        exportXlsWithMap(excelName, filePath, headList, dataList, null, 0, 1);
    }

    public void exportXlsWithMap(String excelName, String filePath, List<String> headList,
                                 Collection<Map<String, String>> dataList) throws Exception {

        exportXlsWithMap(excelName, filePath, convertList(headList), dataList, null, 0, 1);
    }

    public void exportXlsWithMap(String excelName, String filePath, String[] headList, Collection<Map<String, String>> dataList,
                                  int[] columnWith, int freezeCol, int freezeRow) throws Exception {
        if(dataList.isEmpty()){
            log.info("没有数据");
            return;
        }

        Workbook workbook = new HSSFWorkbook();
        handleWorkbookWithMap(workbook, headList, dataList, columnWith, freezeCol, freezeRow);
        writeExcelToSystem(workbook, excelName, ".xls", filePath);
    }

    //导出xlsx
    public void exportXlsxWithMap(String excelName, String filePath, String[] headList,
                                  Collection<Map<String, String>> dataList) throws Exception {
        exportXlsxWithMap(excelName, filePath, headList, dataList, null, 0, 1);
    }

    public void exportXlsxWithMap(String excelName, String filePath, List<String> headList,
                                  Collection<Map<String, String>> dataList) throws Exception {
        exportXlsxWithMap(excelName, filePath, convertList(headList), dataList, null, 0, 1);
    }

    public void exportXlsxWithMap(String excelName, String filePath, String[] headList, Collection<Map<String, String>> dataList,
                           int[] columnWith, int freezeCol, int freezeRow) throws Exception {

        if(dataList.isEmpty()){
            log.info("没有数据");
            return;
        }

        Workbook workbook = new SXSSFWorkbook();
        handleWorkbookWithMap(workbook, headList, dataList, columnWith, freezeCol, freezeRow);
        writeExcelToSystem(workbook, excelName, ".xlsx", filePath);
    }

    public void handleWorkbookWithMap(Workbook wb, String[] headList, Collection<Map<String, String>> dataList,
                               int[] columnWith, int freezeCol, int freezeRow){

        Sheet sheet = initSheet(wb, "sheet1", headList, columnWith, freezeCol, freezeRow);

        CellStyle style = getStyle_LEFT_CENTER(wb, false, false);

        int rowNum = 0;
        int size = headList.length;
        for(Map<String, String> data : dataList){
            Row row = sheet.createRow(++rowNum);
            row.setHeightInPoints(TableContentRowHeight);

            for(int i=0; i<size; i++){
                Cell cell = row.createCell(i);
                cell.setCellStyle(style);
                String value = data.get(headList[i]);
                cell.setCellValue(value==null?"":value);
            }
        }
    }

    //导出xlsx
    public void exportXlsxWithMap(String excelName, String filePath, String[] headList, String[] fieldList,
                                  Collection<Map<String, String>> dataList) throws Exception {
        exportXlsxWithMap(excelName, filePath, headList, fieldList, dataList, null, 0, 1);
    }

    public void exportXlsxWithMap(String excelName, String filePath, List<String> headList, List<String> fieldList,
                                  Collection<Map<String, String>> dataList) throws Exception {
        exportXlsxWithMap(excelName, filePath, convertList(headList),
                convertList(fieldList), dataList, null, 0, 1);
    }

    public void exportXlsxWithMap(String excelName, String filePath, String[] headList,
                                  String[] fieldList, Collection<Map<String, String>> dataList,
                                  int[] columnWith, int freezeCol, int freezeRow) throws Exception {

        if(dataList.isEmpty()){
            log.info("没有数据");
            return;
        }

        Workbook workbook = new SXSSFWorkbook();
        handleWorkbookWithMap(workbook, headList, fieldList, dataList, columnWith, freezeCol, freezeRow);
        writeExcelToSystem(workbook, excelName, ".xlsx", filePath);
    }

    public void handleWorkbookWithMap(Workbook wb, String[] headList, String[] fieldList, Collection<Map<String, String>> dataList,
                                      int[] columnWith, int freezeCol, int freezeRow){

        Sheet sheet = initSheet(wb, "sheet1", headList, columnWith, freezeCol, freezeRow);

        CellStyle style = getStyle_LEFT_CENTER(wb, false, false);

        int rowNum = 0;
        int size = fieldList.length;
        for(Map<String, String> data : dataList){
            Row row = sheet.createRow(++rowNum);
            row.setHeightInPoints(TableContentRowHeight);

            for(int i=0; i<size; i++){
                Cell cell = row.createCell(i);
                cell.setCellStyle(style);
                String value = data.get(fieldList[i]);
                cell.setCellValue(value==null?"":value);
            }
        }
    }


    /** 特殊方法 */
    //List转[]
    public static String[] convertList(List<String> list){
        int size = list.size();
        String[] strings = new String[size];
        for(int i=0; i<size; i++){
            strings[i] = list.get(i);
        }
        return strings;
    }

}
