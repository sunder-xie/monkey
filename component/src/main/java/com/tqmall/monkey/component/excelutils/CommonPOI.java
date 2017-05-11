package com.tqmall.monkey.component.excelutils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 * 正常的poi操作工具类
 * Created by zxg on 15/8/27.
 */
public class CommonPOI {

    private static DecimalFormat df = new DecimalFormat("0");

    public static void main(String[] args) throws Exception {
        String path = "/Users/zxg/Documents/work/code/java/monkey/web/file/fileUpload/temp/";
        String fileName = "现代-I30-20150821";
        String fileType = "xlsx";
        read(path, fileName, fileType);
    }

    public static void read(String path,String fileName,String fileType) throws Exception
    {
        InputStream stream = new FileInputStream(path+fileName+"."+fileType);
        Workbook wb = null;
        if (fileType.equals("xls")) {
            wb = new HSSFWorkbook(stream);
        }
        else if (fileType.equals("xlsx")) {
            wb = new XSSFWorkbook(stream);
        }
        else {
            System.out.println("您输入的excel格式不正确");
        }
        Sheet sheet1 = wb.getSheetAt(0);
        Sheet sheet2 = wb.getSheetAt(1);
        int i = sheet2.getFirstRowNum();
        int n = sheet2.getLastRowNum();
        int w = sheet1.getLastRowNum();
        int q = sheet2.getTopRow();
        for (Row row : sheet1) {
            int cellNum = row.getPhysicalNumberOfCells();
            int firstCell = row.getFirstCellNum();
            Cell acell = row.getCell(firstCell);
//            String result = acell.getStringCellValue();
            for (Cell cell : row) {
                System.out.print(getXSSFCellValue(cell)+"  ");
            }
            System.out.println();
        }
    }


    /**
     * 读取xlsx文档列
     * */
    public static String getXSSFCellValue(Cell cell)throws Exception
    {
        String cellvalue = "";
        if (cell!=null) {
            switch (cell.getCellType()) {
                case XSSFCell.CELL_TYPE_BOOLEAN:
                    cellvalue = String.valueOf(cell.getBooleanCellValue());
                    break;
                case XSSFCell.CELL_TYPE_NUMERIC:
                    cellvalue = df.format(cell.getNumericCellValue()).replace(".0","");
//                    cellvalue = String.valueOf(cell.getNumericCellValue()).replace(".0","");
                    break;
                case XSSFCell.CELL_TYPE_STRING:
                    cellvalue = cell.getStringCellValue();
                    break;
                case XSSFCell.CELL_TYPE_BLANK:
                    break;
                case XSSFCell.CELL_TYPE_ERROR:
                    break;
                case XSSFCell.CELL_TYPE_FORMULA:
                    break;
            }
        }
        return cellvalue;
    }
}
