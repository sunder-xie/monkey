import com.tqmall.monkey.component.excelutils.ReadExcelByEventUserModel;
import com.tqmall.monkey.component.excelutils.ReadExcelXLS;
import com.tqmall.monkey.component.utils.PoiUtil;
import com.tqmall.monkey.domain.entity.offerGoods.OfferGoodsDO;
import org.junit.Test;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zxg on 15/7/8.
 */
public class ExcleTest {

    @Test
    public void excle_read_test(){
//        String file_name = "test.xlsx";
//        String file_path = "/Users/zxg/Documents/test.xlsx";

        String file_name = "test.xls";
        String file_path = "/Users/zxg/Documents/test.xls";

        if(file_name.toUpperCase().endsWith(".XLSX")){
            XLSXExcel xlsxUtil = new XLSXExcel();
            try {
                xlsxUtil.processOneSheet(file_path,1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(file_name.toUpperCase().endsWith(".XLS")){
            XLSExcle xlsUtil = new XLSExcle();

            try {
                xlsUtil.process(file_path,0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("wrong");
        }
    }

    @Test
    public void excle_write_test() throws Exception {
        HttpServletResponse response = null;

        String excelTitle = "测试";
        String[] headName = {
                "失败原因","原始index"
        };
        String[] fieldName = {
                "failReason","indexs"
        };
        int[] columnWith = {
                2000,7000,
        };
        int freezeCol = 1;
        int freezeRow = 2;

        List<HashMap<String,Object>> list = new ArrayList<>();

        HashMap<String,Object> map1 = new HashMap<>();
        HashMap<String,Object> map2 = new HashMap<>();

        map1.put("failReason","这是第一个原因");
        map1.put("indexs","序号未1");
        map2.put("failReason","第二个不解释");
        map2.put("indexs","序号未2");

        list.add(map1);
        list.add(map2);


        PoiUtil poiUtil = new PoiUtil();
        poiUtil.exportExcelCommon(response,excelTitle,headName,fieldName,list,columnWith,true,freezeCol,freezeRow);

    }

    //XLSX的读取和识别
    class XLSXExcel extends ReadExcelByEventUserModel {
        @Override
        public void operateRows(int sheetIndex, int curRow, List<String> rowlist) throws Exception {
            //第一行标题，特殊处理
            if (curRow == 0) {
                System.out.println("第一行");
                printlist(rowlist);
            } else {
                //第二行开始数据处理
                System.out.println("第二行");
                printlist(rowlist);
            }
        }
    }

    //XLS的读取
    class XLSExcle extends ReadExcelXLS {
        @Override
        public void operateRows(int sheetIndex, int curRow, List<String> rowList) throws Exception {
            //第一行标题，特殊处理
            if (curRow == 0) {
                System.out.println("第一行");
                printlist(rowList);
            } else {
                //第二行开始数据处理
                System.out.println("第二行");
                printlist(rowList);
            }
        }
    }

    public void printlist(List<String> list){
        System.out.println("tart");
        for(Object o : list){
            System.out.println(o);
        }
        System.out.println("end");
    }

}
