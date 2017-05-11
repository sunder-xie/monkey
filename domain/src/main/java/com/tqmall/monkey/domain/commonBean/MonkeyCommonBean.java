package com.tqmall.monkey.domain.commonBean;

/**
 * Created by zxg on 15/12/16.
 */
public class MonkeyCommonBean {

    public final static String IS_DELETED_TRUE = "Y";
    public final static String IS_DELETED_FALSE = "N";

    public final static int CREATING_EXCEL = -1;
    public final static int CREATING_EXCEL_ERROR = -2;
    public final static int CREATE_EXCEL_TYPE_RE = 1;
    public final static int CREATE_EXCEL_TYPE_NEW = 2;

    public final static String EXCEL_2003 = ".xls";
    public final static String EXCEL_2007 = ".xlsx";

    //用于标示是否正在生成excel文件,可以修改值(非 final)
    public static int CREATE_LIYANG_EXCEL_FLAG = 2;

    public final static String EXPORT_LIYANG_VEHICLE_TYPE_EXCEL_PATH = "/lyExcel";
    public final static String EXPORT_LIYANG_VEHICLE_TYPE_EXCEL_NAME = "力洋车型V";

}
