package com.tqmall.monkey.component.utils;

import com.tqmall.monkey.domain.commonBean.MonkeyCommonBean;
import com.tqmall.monkey.domain.entity.category.AllCategoryViewDO;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.*;


/**
 * Created by huangzhangting on 15/6/27.
 *
 */
public class PoiUtil {

    public final static short ExcelTitleFontSize = (short) 18;
    public final static short TableHeadFontSize = (short) 11;
    public final static short TableHeadFontSize_sup = (short) 14;
    public final static short TableContentFontSize = (short) 11;
    public final static short TableContentFontSize2 = (short) 10;
    public static String TableFontName = "宋体";

    public final static float ExcelTitleRowHeight = 55f;
    public final static float TableHeadRowHeight = 22f;
    public final static float TableContentRowHeight = 20f;

    public final static float DefaultRowHeight = 20f;


    //excel标题样式
    public CellStyle getExcelTitleStyle(Workbook wb) throws Exception {

        Font font = wb.createFont();
        font.setFontHeightInPoints(PoiUtil.ExcelTitleFontSize);
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
    public CellStyle getTableHeadStyle(Workbook wb) throws Exception {
        Font font = wb.createFont();
        font.setFontHeightInPoints(PoiUtil.TableHeadFontSize);
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
    public CellStyle getTableHeadStyle_sup(Workbook wb) throws Exception {
        Font font = wb.createFont();
        font.setFontHeightInPoints(PoiUtil.TableHeadFontSize_sup);
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

    //表格内容样式   水平垂直都剧中，有边框
    public CellStyle getStyle_CENTER_CENTER(Workbook wb, boolean boldweight) throws Exception {
        Font font = wb.createFont();
        font.setFontHeightInPoints(PoiUtil.TableContentFontSize);
        if (boldweight) {
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        }
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

    //表格内容样式   水平居左，垂直剧中，有边框
    public CellStyle getStyle_LEFT_CENTER(Workbook wb, boolean boldweight) throws Exception {
        Font font = wb.createFont();
        font.setFontHeightInPoints(PoiUtil.TableContentFontSize);
        if (boldweight) {
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        }
        font.setFontName(TableFontName);

        CellStyle style = wb.createCellStyle();
        style.setFont(font);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setWrapText(true);
        style.setBorderBottom((short) 1);
        style.setBorderLeft((short) 1);
        style.setBorderRight((short) 1);
        style.setBorderTop((short) 1);

        return style;
    }

    //表格内容样式   水平居右，垂直剧中，有边框
    public CellStyle getStyle_RIGHT_CENTER(Workbook wb, boolean boldweight) throws Exception {
        Font font = wb.createFont();
        font.setFontHeightInPoints(PoiUtil.TableContentFontSize);
        if (boldweight) {
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        }
        font.setFontName(TableFontName);

        CellStyle style = wb.createCellStyle();
        style.setFont(font);
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setWrapText(true);
        style.setBorderBottom((short) 1);
        style.setBorderLeft((short) 1);
        style.setBorderRight((short) 1);
        style.setBorderTop((short) 1);

        return style;
    }

    //表格内容样式  水平居左，垂直居上，有边框
    public CellStyle getStyle_LEFT_TOP(Workbook wb) throws Exception {
        Font font = wb.createFont();
        font.setFontHeightInPoints(PoiUtil.TableContentFontSize);
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

    //表格内容样式   水平右对齐，垂直居中，去无框
    public CellStyle getStyle_RIGHT_CENTER_noBorder(Workbook wb, boolean boldweight) throws Exception {
        Font font = wb.createFont();
        font.setFontHeightInPoints(PoiUtil.TableContentFontSize);
        if (boldweight) {
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

    //输出excel
    public void toWriteExcel(HttpServletResponse response, Workbook wb, String excelTitle) throws Exception {
        OutputStream os = response.getOutputStream();// 取得输出流
        response.reset();// 清空输出流
        response.setHeader("Content-disposition", "attachment; filename=" + new String(excelTitle.getBytes(), "ISO-8859-1") + ".xls");// 设定输出文件头
        response.setContentType("application/x-download;");

        wb.write(os);
        os.flush();
        os.close();
    }

    //输出excel
    public void toWritexlsxExcel(HttpServletResponse response, Workbook wb, String excelTitle) throws Exception {
        OutputStream os = response.getOutputStream();// 取得输出流
        response.reset();// 清空输出流
        response.setHeader("Content-disposition", "attachment; filename=" + new String(excelTitle.getBytes(), "ISO-8859-1") + ".xlsx");// 设定输出文件头
        response.setContentType("application/x-download;");

        wb.write(os);
        os.flush();
        os.close();
    }

    //未提供导出的
    public void exportExcel_noProvide(HttpServletResponse response) throws Exception {
        //生成一个表格对象
        HSSFWorkbook wb = new HSSFWorkbook();
        //每个文件必须包含一个工作表
        //创建一个工作表
        String excelTitle = "该表单未提供导出功能";
        Sheet sheet = wb.createSheet("sheet1");
        sheet.setDefaultRowHeightInPoints(DefaultRowHeight);
        sheet.setColumnWidth(0, 15000);
        HSSFRow row = (HSSFRow) sheet.createRow(0);
        HSSFCellStyle style = (HSSFCellStyle) getExcelTitleStyle(wb);//excel标题样式
        HSSFCell cell = row.createCell(0);
        cell.setCellValue(excelTitle);
        cell.setCellStyle(style);

        toWriteExcel(response, wb, excelTitle);
    }

    //导出javaBean对象的excle
    public void exportExcelCommon(HttpServletResponse response, String excelTitle, String[] headName, String[] fieldName,
                                  List<?> dataList, int[] columnWith, boolean showNumber, int freezeCol, int freezeRow) throws Exception {

        //生成一个表格对象
        Workbook wb = this.exportExcelCommonBean(excelTitle, headName, fieldName, dataList, columnWith, showNumber, freezeCol, freezeRow);


        toWriteExcel(response, wb, excelTitle);//输出excel

    }


    //导出javaBean对象的excle
    public Workbook exportExcelCommonBean(String excelTitle, String[] headName, String[] fieldName,
                                          List<?> dataList, int[] columnWith, boolean showNumber, int freezeCol, int freezeRow) throws Exception {

        //生成一个表格对象
        Workbook wb = new HSSFWorkbook();
        //每个文件必须包含一个工作表
        //创建一个工作表
        Sheet sheet = wb.createSheet("sheet1");
        sheet.setDefaultRowHeightInPoints(DefaultRowHeight);

        if (freezeCol < 0) {
            freezeCol = 0;
        }
        if (freezeRow < 0) {
            freezeRow = 0;
        }
        sheet.createFreezePane(freezeCol, freezeRow);//冻结 列、行

        //合并标题行
        int size = headName.length;
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, showNumber ? size : size - 1));
        //创建行，参数说明的是第几行
        int rowNum = 0;
        Row row = sheet.createRow(rowNum);
        row.setHeightInPoints(ExcelTitleRowHeight);
        //创建单元格，参数说明的是第几个单元格
        Cell cell = row.createCell(0);
        cell.setCellValue(excelTitle);

        HSSFCellStyle style = (HSSFCellStyle) getExcelTitleStyle(wb);//excel标题样式
        cell.setCellStyle(style);

        rowNum++;
        row = sheet.createRow(rowNum);
        row.setHeightInPoints(TableHeadRowHeight);

        style = (HSSFCellStyle) getTableHeadStyle(wb);//表格的表头样式

        List<String> headNamelist = Arrays.asList(headName);
        if (showNumber) {
            LinkedList<String> tempList = new LinkedList<String>();
            tempList.addAll(headNamelist);
            tempList.offerFirst("序号");
            headNamelist = tempList;
        }

        for (int i = 0; i < headNamelist.size(); i++) {
            cell = row.createCell(i);
            cell.setCellValue(headNamelist.get(i));
            cell.setCellStyle(style);
            if (columnWith != null) {
                sheet.setColumnWidth(i, columnWith[i]);
            } else {
                sheet.setColumnWidth(i, 3500);
            }
        }

        style = (HSSFCellStyle) getStyle_CENTER_CENTER(wb, false);//表格内容样式

        Object obj;
        Class clazz;
        Field f;
        Object value;
        for (int i = 0; i < dataList.size(); i++) {
            rowNum++;
            row = sheet.createRow(rowNum);
            row.setHeightInPoints(TableContentRowHeight);
            if (showNumber) {
                cell = row.createCell(0);
                cell.setCellValue(i + 1);
                cell.setCellStyle(style);
            }
            obj = dataList.get(i);
            for (int j = 0; j < fieldName.length; j++) {
                if (showNumber) {
                    cell = row.createCell(j + 1);
                } else {
                    cell = row.createCell(j);
                }
                cell.setCellStyle(style);
                clazz = obj.getClass();
                String fName = fieldName[j];
                if (fName.indexOf(".") < 0) {
                    f = clazz.getDeclaredField(fieldName[j]);
//        			if(f != null){
                    f.setAccessible(true);
                    value = f.get(obj);
                    cell.setCellValue(value == null ? "" : value + "");
//        			}else{
//        				cell.setCellValue("");
//        			}
                } else {
                    String[] fn = fName.split("\\.");
                    f = clazz.getDeclaredField(fn[0]);
                    f.setAccessible(true);
                    value = f.get(obj);
                    clazz = value.getClass();
                    f = clazz.getDeclaredField(fn[1]);
                    f.setAccessible(true);
                    value = f.get(value);
                    cell.setCellValue(value == null ? "" : value + "");
                }
            }
        }

        return wb;

    }

    //updated by zhongxigeng on 15/7/16
    // 导出自定义的hashMap的excle--下载
    public void exportExcelByHash(HttpServletResponse response, String excelTitle, String[] headName, String[] fieldName,
                                  List<HashMap<String, Object>> hashList, int[] columnWith, boolean showNumber, int freezeCol, int freezeRow) throws Exception {

        //生成一个表格对象
        HSSFWorkbook wb = this.exportExcelByHashTemplate(excelTitle, headName, fieldName, hashList, columnWith, showNumber, freezeCol, freezeRow);

        toWriteExcel(response, wb, excelTitle);//输出excel

    }


    // 导出自定义的hashMap的大数据excle--下载
    public void exportExcelByHashLargeData(HttpServletResponse response, String excelTitle, String[] headName, String[] fieldName,
                                           List<HashMap<String, Object>> hashList, int[] columnWith, boolean showNumber, int freezeCol, int freezeRow) throws Exception {

        //生成一个表格对象
        Workbook wb = this.exportExcelByHashLargeDataTemplate(excelTitle, headName, fieldName, hashList, columnWith, showNumber, freezeCol, freezeRow);

        toWritexlsxExcel(response, wb, excelTitle);//输出excel

    }

    //====================数据量大的处理==Start================
    //导出javaBean对象的excle---若超出原有的数据范围，分为多个sheet，以5000行为准则
    public Workbook exportExcelByBeanLargeDataTemplate(String excelTitle, String[] headName, String[] fieldName,
                                                       List<?> dataList, int[] columnWith, boolean showNumber, int freezeCol, int freezeRow) throws Exception {

        //生成一个表格对象
        Workbook wb = new SXSSFWorkbook();
        //每个sheet最大多少行
        Integer maxRow = 5000;
        Integer listSize = dataList.size();

        Integer fromIndex = 0;
        Integer index = 0;
        while (true) {
            Integer toIndex = fromIndex + maxRow;
            if (toIndex >= listSize) toIndex = listSize;

            List thisList = new ArrayList<>();
            thisList.addAll(dataList.subList(fromIndex, toIndex));

//            dataList.removeAll(thisList);
            fromIndex = toIndex;
            //创建一个工作表
            index++;
            Sheet sheet = wb.createSheet("sheet" + index);
            sheet.setDefaultRowHeightInPoints(DefaultRowHeight);

            if (freezeCol < 0) {
                freezeCol = 0;
            }
            if (freezeRow < 0) {
                freezeRow = 0;
            }
            sheet.createFreezePane(freezeCol, freezeRow);//冻结 列、行

            //合并标题行
            int size = headName.length;
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, showNumber ? size : size - 1));
            //创建行，参数说明的是第几行
            int rowNum = 0;
            Row row = sheet.createRow(rowNum);
            row.setHeightInPoints(ExcelTitleRowHeight);
            //创建单元格，参数说明的是第几个单元格
            Cell cell = row.createCell(0);
            cell.setCellValue(excelTitle);

            CellStyle style = getExcelTitleStyle(wb);//excel标题样式
            cell.setCellStyle(style);

            rowNum++;
            row = sheet.createRow(rowNum);
            row.setHeightInPoints(TableHeadRowHeight);

            style = getTableHeadStyle(wb);//表格的表头样式

            List<String> headNamelist = Arrays.asList(headName);
            if (showNumber) {
                LinkedList<String> tempList = new LinkedList<String>();
                tempList.addAll(headNamelist);
                tempList.offerFirst("序号");
                headNamelist = tempList;
            }

            for (int i = 0; i < headNamelist.size(); i++) {
                cell = row.createCell(i);
                cell.setCellValue(headNamelist.get(i));
                cell.setCellStyle(style);
                if (columnWith != null) {
                    sheet.setColumnWidth(i, columnWith[i]);
                } else {
                    sheet.setColumnWidth(i, 3500);
                }
            }

            style = getStyle_CENTER_CENTER(wb, false);//表格内容样式

            Object obj;
            Class clazz;
            Field f;
            Object value;
            for (int i = 0; i < thisList.size(); i++) {
                rowNum++;
                row = sheet.createRow(rowNum);
                row.setHeightInPoints(TableContentRowHeight);
                if (showNumber) {
                    cell = row.createCell(0);
                    cell.setCellValue(i + 1);
                    cell.setCellStyle(style);
                }
                obj = thisList.get(i);
                for (int j = 0; j < fieldName.length; j++) {
                    if (showNumber) {
                        cell = row.createCell(j + 1);
                    } else {
                        cell = row.createCell(j);
                    }
                    cell.setCellStyle(style);
                    clazz = obj.getClass();
                    String fName = fieldName[j];
                    if (fName.indexOf(".") < 0) {
                        f = clazz.getDeclaredField(fieldName[j]);
//        			if(f != null){
                        f.setAccessible(true);
                        value = f.get(obj);
                        cell.setCellValue(value == null ? "" : value + "");
//        			}else{
//        				cell.setCellValue("");
//        			}
                    } else {
                        String[] fn = fName.split("\\.");
                        f = clazz.getDeclaredField(fn[0]);
                        f.setAccessible(true);
                        value = f.get(obj);
                        clazz = value.getClass();
                        f = clazz.getDeclaredField(fn[1]);
                        f.setAccessible(true);
                        value = f.get(value);
                        cell.setCellValue(value == null ? "" : value + "");
                    }
                }
            }

            if (toIndex.equals(listSize)) {
                break;
            }
        }

        return wb;

    }

    public Workbook exportExcelByHashLargeDataTemplate(String excelTitle, String[] headName, String[] fieldName,
                                                       List<HashMap<String, Object>> hashList, int[] columnWith, boolean showNumber, int freezeCol, int freezeRow) throws Exception {

        //生成一个表格对象
        Workbook wb = new SXSSFWorkbook();
        //每个sheet最大多少行
        Integer maxRow = 5000;
        Integer listSize = hashList.size();

        Integer fromIndex = 0;
        Integer index = 0;
        while (true) {
            Integer toIndex = fromIndex + maxRow;
            if (toIndex >= listSize) toIndex = listSize;

            List<HashMap<String, Object>> thisList = new ArrayList<>();
            thisList.addAll(hashList.subList(fromIndex, toIndex));

//            dataList.removeAll(thisList);
            fromIndex = toIndex;
            //每个文件必须包含一个工作表
            //创建一个工作shhet
            index++;
            Sheet sheet = wb.createSheet("sheet" + index);
            sheet.setDefaultRowHeightInPoints(DefaultRowHeight);

            if (freezeCol < 0) {
                freezeCol = 0;
            }
            if (freezeRow < 0) {
                freezeRow = 0;
            }
            sheet.createFreezePane(freezeCol, freezeRow);//冻结 列、行

            Cell cell = null;
//
            int rowNum = 0;
            Row row = sheet.createRow(rowNum);
            row.setHeightInPoints(TableHeadRowHeight);

            CellStyle style = getTableHeadStyle(wb);//表格的表头样式

            List<String> headNamelist = Arrays.asList(headName);
            if (showNumber) {
                LinkedList<String> tempList = new LinkedList<String>();
                tempList.addAll(headNamelist);
                tempList.offerFirst("序号");
                headNamelist = tempList;
            }

            for (int i = 0; i < headNamelist.size(); i++) {
                cell = row.createCell(i);
                cell.setCellValue(headNamelist.get(i));
                cell.setCellStyle(style);
                Integer col_with = columnWith[i];
                if (col_with != null) {
                    sheet.setColumnWidth(i, col_with);
                } else {
                    sheet.setColumnWidth(i, 3500);
                }
            }

            style = getStyle_CENTER_CENTER(wb, false);//表格内容样式

            Integer index_num = 1;
            for (HashMap<String, Object> oneMap : thisList) {
                rowNum++;
                row = sheet.createRow(rowNum);
                row.setHeightInPoints(TableContentRowHeight);
                if (showNumber) {
                    cell = row.createCell(0);
                    cell.setCellValue(index_num++);
                    cell.setCellStyle(style);
                }
                //从第二列开始，放数据
                Integer start_cell_num = 0;
                for (String key : fieldName) {
                    Object value = oneMap.get(key);
                    if (showNumber) {
                        cell = row.createCell(start_cell_num + 1);
                    } else {
                        cell = row.createCell(start_cell_num);
                    }
                    start_cell_num++;
                    cell.setCellStyle(style);
                    cell.setCellValue(value == null ? "" : value + "");
                }


            }
            if (toIndex == listSize) {
                break;
            }
        }

        return wb;
    }
    //====================数据量大的处理==End================


    //updated by zhongxigeng on 15/7/16
    // 导出自定义的hashMap的excle的模板，返回值为HSSFWork
    public HSSFWorkbook exportExcelByHashTemplate(String excelTitle, String[] headName, String[] fieldName,
                                                  List<HashMap<String, Object>> hashList, int[] columnWith, boolean showNumber, int freezeCol, int freezeRow) throws Exception {

        //生成一个表格对象
        HSSFWorkbook wb = new HSSFWorkbook();
        //每个文件必须包含一个工作表
        //创建一个工作表
        HSSFSheet sheet = wb.createSheet("sheet1");
        sheet.setDefaultRowHeightInPoints(DefaultRowHeight);

        if (freezeCol < 0) {
            freezeCol = 0;
        }
        if (freezeRow < 0) {
            freezeRow = 0;
        }
        sheet.createFreezePane(freezeCol, freezeRow);//冻结 列、行

        //合并标题行
//        int size = headName.length;
//        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, showNumber?size:size-1));
//        //创建行，参数说明的是第几行
//        row.setHeightInPoints(ExcelTitleRowHeight);
//        //创建单元格，参数说明的是第几个单元格
        HSSFCell cell = null;
//
        int rowNum = 0;
        HSSFRow row = sheet.createRow(rowNum);
        row.setHeightInPoints(TableHeadRowHeight);

        HSSFCellStyle style = (HSSFCellStyle) getTableHeadStyle(wb);//表格的表头样式

        List<String> headNamelist = Arrays.asList(headName);
        if (showNumber) {
            LinkedList<String> tempList = new LinkedList<String>();
            tempList.addAll(headNamelist);
            tempList.offerFirst("序号");
            headNamelist = tempList;
        }

        for (int i = 0; i < headNamelist.size(); i++) {
            cell = row.createCell(i);
            cell.setCellValue(headNamelist.get(i));
            cell.setCellStyle(style);
            Integer col_with = columnWith[i];
            if (col_with != null) {
                sheet.setColumnWidth(i, col_with);
            } else {
                sheet.setColumnWidth(i, 3500);
            }
        }

        style = (HSSFCellStyle) getStyle_CENTER_CENTER(wb, false);//表格内容样式

        Integer index_num = 1;
        for (HashMap<String, Object> oneMap : hashList) {
            rowNum++;
            row = sheet.createRow(rowNum);
            row.setHeightInPoints(TableContentRowHeight);
            if (showNumber) {
                cell = row.createCell(0);
                cell.setCellValue(index_num++);
                cell.setCellStyle(style);
            }
            //从第二列开始，放数据
            Integer start_cell_num = 0;
            for (String key : fieldName) {
                Object value = oneMap.get(key);
                if (showNumber) {
                    cell = row.createCell(start_cell_num + 1);
                } else {
                    cell = row.createCell(start_cell_num);
                }
                start_cell_num++;
                cell.setCellStyle(style);
                cell.setCellValue(value == null ? "" : value + "");
            }
        }
        return wb;
    }

    public void exportExcelForCategory(HttpServletResponse response, String excelTitle, String[] headName, String[] fieldName,
                                       List<AllCategoryViewDO> dataList, int[] columnWith, boolean showNumber, int freezeCol, int freezeRow) throws Exception {

        //生成一个表格对象
        HSSFWorkbook wb = new HSSFWorkbook();
        //每个文件必须包含一个工作表
        //创建一个工作表
        HSSFSheet sheet = wb.createSheet("sheet1");
        sheet.setDefaultRowHeightInPoints(DefaultRowHeight);

        if (freezeCol < 0) {
            freezeCol = 0;
        }
        if (freezeRow < 0) {
            freezeRow = 0;
        }
        sheet.createFreezePane(freezeCol, freezeRow);//冻结 列、行

        //合并标题行
        int size = headName.length;
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, showNumber ? size : size - 1));
        //创建行，参数说明的是第几行
        int rowNum = 0;
        HSSFRow row = sheet.createRow(rowNum);
        row.setHeightInPoints(ExcelTitleRowHeight);
        //创建单元格，参数说明的是第几个单元格
        HSSFCell cell = row.createCell(0);
        cell.setCellValue(excelTitle);

        HSSFCellStyle style = (HSSFCellStyle) getExcelTitleStyle(wb);//excel标题样式
        cell.setCellStyle(style);

        rowNum++;
        row = sheet.createRow(rowNum);
        row.setHeightInPoints(TableHeadRowHeight);

        style = (HSSFCellStyle) getTableHeadStyle(wb);//表格的表头样式

        List<String> headNamelist = Arrays.asList(headName);
        if (showNumber) {
            LinkedList<String> tempList = new LinkedList<String>();
            tempList.addAll(headNamelist);
            tempList.offerFirst("序号");
            headNamelist = tempList;
        }

        for (int i = 0; i < headNamelist.size(); i++) {
            cell = row.createCell(i);
            cell.setCellValue(headNamelist.get(i));
            cell.setCellStyle(style);
            if (columnWith != null) {
                sheet.setColumnWidth(i, columnWith[i]);
            } else {
                sheet.setColumnWidth(i, 3500);
            }
        }

        style = (HSSFCellStyle) getStyle_CENTER_CENTER(wb, false);//表格内容样式

        Object obj;
        Class clazz;
        Field f;
        Object value;
        for (int i = 0; i < dataList.size(); i++) {
            rowNum++;
            row = sheet.createRow(rowNum);
            row.setHeightInPoints(TableContentRowHeight);
            if (showNumber) {
                cell = row.createCell(0);
                cell.setCellValue(i + 1);
                cell.setCellStyle(style);
            }
            obj = dataList.get(i);
            for (int j = 0; j < fieldName.length; j++) {
                if (showNumber) {
                    cell = row.createCell(j + 1);
                } else {
                    cell = row.createCell(j);
                }
                cell.setCellStyle(style);
                String fName = fieldName[j];
                if ("".equals(fName)) {
                    cell.setCellValue(((AllCategoryViewDO) obj).getDeletedStatus());
                    continue;
                }
                clazz = obj.getClass();
                if (fName.indexOf(".") < 0) {
                    f = clazz.getDeclaredField(fieldName[j]);
//        			if(f != null){
                    f.setAccessible(true);
                    value = f.get(obj);
                    cell.setCellValue(value == null ? "" : value + "");
//        			}else{
//        				cell.setCellValue("");
//        			}
                } else {
                    String[] fn = fName.split("\\.");
                    f = clazz.getDeclaredField(fn[0]);
                    f.setAccessible(true);
                    value = f.get(obj);
                    clazz = value.getClass();
                    f = clazz.getDeclaredField(fn[1]);
                    f.setAccessible(true);
                    value = f.get(value);
                    cell.setCellValue(value == null ? "" : value + "");
                }
            }
        }


        toWriteExcel(response, wb, excelTitle);//输出excel

    }

    //=================抽象出来的处理类

    /**
     * BY LYJ ON 20151215
     *
     * @param excelTitle
     * @param headName
     * @param fieldName
     * @param list
     * @param columnWith
     * @param showNumber
     * @param freezeCol
     * @param freezeRow
     * @return
     * @throws Exception
     */
    //updated by zhongxigeng on 15/7/16
    // 导出自定义的hashMap的excle的模板，返回值为HSSFWork
    public HSSFWorkbook exportExcelByHashTemplateForCXR(String excelTitle, String[] headName, String[] fieldName,
                                                        List<Map<String, Object>> list, int[] columnWith, boolean showNumber, int freezeCol, int freezeRow) throws Exception {

        //生成一个表格对象
        HSSFWorkbook wb = new HSSFWorkbook();
        //每个文件必须包含一个工作表
        //创建一个工作表
        HSSFSheet sheet = wb.createSheet("sheet1");
        sheet.setDefaultRowHeightInPoints(DefaultRowHeight);

        if (freezeCol < 0) {
            freezeCol = 0;
        }
        if (freezeRow < 0) {
            freezeRow = 0;
        }
        sheet.createFreezePane(freezeCol, freezeRow);//冻结 列、行

        //合并标题行
//        int size = headName.length;
//        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, showNumber?size:size-1));
//        //创建行，参数说明的是第几行
//        row.setHeightInPoints(ExcelTitleRowHeight);
//        //创建单元格，参数说明的是第几个单元格
        HSSFCell cell = null;
//
        int rowNum = 0;
        HSSFRow row = sheet.createRow(rowNum);
        row.setHeightInPoints(TableHeadRowHeight);

        HSSFCellStyle style = (HSSFCellStyle) getTableHeadStyle(wb);//表格的表头样式

        List<String> headNamelist = Arrays.asList(headName);
        if (showNumber) {
            LinkedList<String> tempList = new LinkedList<String>();
            tempList.addAll(headNamelist);
            tempList.offerFirst("序号");
            headNamelist = tempList;
        }

        for (int i = 0; i < headNamelist.size(); i++) {
            cell = row.createCell(i);
            cell.setCellValue(headNamelist.get(i));
            cell.setCellStyle(style);
            Integer col_with = columnWith[i];
            if (col_with != null) {
                sheet.setColumnWidth(i, col_with);
            } else {
                sheet.setColumnWidth(i, 3500);
            }
        }

        style = (HSSFCellStyle) getStyle_CENTER_CENTER(wb, false);//表格内容样式

        Integer index_num = 1;
        for (Map<String, Object> oneMap : list) {
            rowNum++;
            row = sheet.createRow(rowNum);
            row.setHeightInPoints(TableContentRowHeight);
            if (showNumber) {
                cell = row.createCell(0);
                cell.setCellValue(index_num++);
                cell.setCellStyle(style);
            }
            //从第二列开始，放数据
            Integer start_cell_num = 0;
            for (String key : fieldName) {
                Object value = oneMap.get(key);
                if (showNumber) {
                    cell = row.createCell(start_cell_num + 1);
                } else {
                    cell = row.createCell(start_cell_num);
                }
                start_cell_num++;
                cell.setCellStyle(style);
                if ("catKind".equals(key)) {
                    if (Integer.parseInt(value.toString()) == 0) {
                        value = "易损件";
                    } else if (Integer.parseInt(value.toString()) == 1) {
                        value = "全车件";
                    }
                }
                cell.setCellValue(value == null ? "" : value + "");
            }
        }
        return wb;
    }

    // BY LYJ ON 20151215
    // 导出自定义的hashMap的excle--下载
    public void exportExcelByHashForCXR(HttpServletResponse response, String excelTitle, String[] headName, String[] fieldName,
                                        List<Map<String, Object>> list, int[] columnWith, boolean showNumber, int freezeCol, int freezeRow) throws Exception {
        //生成一个表格对象
        HSSFWorkbook wb = this.exportExcelByHashTemplateForCXR(excelTitle, headName, fieldName, list, columnWith, showNumber, freezeCol, freezeRow);
        toWriteExcel(response, wb, excelTitle);//输出excel
    }


    //--------START 力洋车型数据导出到excel 生成excel文件  BY LYJ--------//
    //导出javaBean对象的excle
    public void exportExcelCommonToFile(String excelTitle, String[] headName, String[] fieldName,
                                        List<?> dataList, int[] columnWith, boolean showNumber, int freezeCol, int freezeRow, String excelPath) throws Exception {

        //生成一个表格对象
        Workbook wb = this.exportOneSheetExcelByBeanLargeDataTemplate(excelTitle, headName, fieldName, dataList, columnWith, showNumber, freezeCol, freezeRow);

        toWriteExcelToFile(wb, excelTitle, excelPath);//输出excel
    }

    //输出excel到文件
    public void toWriteExcelToFile(Workbook wb, String excelTitle, String excelPath) throws Exception {
        //没有文件夹则创建文件夹
        File excelDir = new File(excelPath);
        if (!excelDir.exists() && !excelDir.isDirectory()) {
            excelDir.mkdir();
        }

        //已经存在该名字的文件则删除该文件
        String fileName = excelPath + "/" + excelTitle + MonkeyCommonBean.EXCEL_2007;
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }

        //写入文件
        FileOutputStream fOut = new FileOutputStream(fileName);
        wb.write(fOut);
        fOut.flush();
        fOut.close();
    }
    //--------END 力洋车型数据导出到excel 生成excel文件  BY LYJ--------//

    /*
    by lyj on 20160311
    导出javaBean对象的excle, 所有数据都放在一个sheet里面
     */
    public Workbook exportOneSheetExcelByBeanLargeDataTemplate(String excelTitle, String[] headName, String[] fieldName,
                                                               List<?> dataList, int[] columnWith, boolean showNumber, int freezeCol, int freezeRow) throws Exception {
        //生成一个表格对象
        Workbook wb = new SXSSFWorkbook();

        Sheet sheet = wb.createSheet("sheet1");
        sheet.setDefaultRowHeightInPoints(DefaultRowHeight);

        if (freezeCol < 0) {
            freezeCol = 0;
        }
        if (freezeRow < 0) {
            freezeRow = 0;
        }
        sheet.createFreezePane(freezeCol, freezeRow);//冻结 列、行

        //合并标题行
        int size = headName.length;
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, showNumber ? size : size - 1));
        //创建行，参数说明的是第几行
        int rowNum = 0;
        Row row = sheet.createRow(rowNum);
        row.setHeightInPoints(ExcelTitleRowHeight);
        //创建单元格，参数说明的是第几个单元格
        Cell cell = row.createCell(0);
        cell.setCellValue(excelTitle);

        CellStyle style = getExcelTitleStyle(wb);//excel标题样式
        cell.setCellStyle(style);

        rowNum++;
        row = sheet.createRow(rowNum);
        row.setHeightInPoints(TableHeadRowHeight);

        style = getTableHeadStyle(wb);//表格的表头样式

        List<String> headNamelist = Arrays.asList(headName);
        if (showNumber) {
            LinkedList<String> tempList = new LinkedList<String>();
            tempList.addAll(headNamelist);
            tempList.offerFirst("序号");
            headNamelist = tempList;
        }

        for (int i = 0; i < headNamelist.size(); i++) {
            cell = row.createCell(i);
            cell.setCellValue(headNamelist.get(i));
            cell.setCellStyle(style);
            if (columnWith != null) {
                sheet.setColumnWidth(i, columnWith[i]);
            } else {
                sheet.setColumnWidth(i, 3500);
            }
        }

        style = getStyle_CENTER_CENTER(wb, false);//表格内容样式

        Object obj;
        Class clazz;
        Field f;
        Object value;
        Integer listSize = dataList.size();
        for (int i = 0; i < listSize; i++) {
            rowNum++;
            row = sheet.createRow(rowNum);
            row.setHeightInPoints(TableContentRowHeight);
            if (showNumber) {
                cell = row.createCell(0);
                cell.setCellValue(i + 1);
                cell.setCellStyle(style);
            }
            obj = dataList.get(i);
            for (int j = 0; j < fieldName.length; j++) {
                if (showNumber) {
                    cell = row.createCell(j + 1);
                } else {
                    cell = row.createCell(j);
                }
                cell.setCellStyle(style);
                clazz = obj.getClass();
                String fName = fieldName[j];
                if (fName.indexOf(".") < 0) {
                    f = clazz.getDeclaredField(fieldName[j]);
                    f.setAccessible(true);
                    value = f.get(obj);
                    cell.setCellValue(value == null ? "" : value + "");
                } else {
                    String[] fn = fName.split("\\.");
                    f = clazz.getDeclaredField(fn[0]);
                    f.setAccessible(true);
                    value = f.get(obj);
                    clazz = value.getClass();
                    f = clazz.getDeclaredField(fn[1]);
                    f.setAccessible(true);
                    value = f.get(value);
                    cell.setCellValue(value == null ? "" : value + "");
                }
            }
        }
        return wb;
    }
}
