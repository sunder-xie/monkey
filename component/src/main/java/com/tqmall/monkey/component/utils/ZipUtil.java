package com.tqmall.monkey.component.utils;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by zxg on 15/7/15.
 * 打成压缩包的工具类
 */
public class ZipUtil {

    public static File getZip(String base,String fileName){
        File file = new File(base);
        if( !file.exists() ){
            file.mkdirs();
        }
        File f = new File(base+  File.separator +fileName);
//        if(!f.exists()){
//            try {
//                File.createTempFile(fileName, ".zip");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        return f;
    }
    /**
     * 对Excel文件进行压缩
     * @param targetFile 压缩的文件路径
     * @param wbList excleList
     * @param excelTitleList excle头List
     */
    public static void zip(File targetFile, List<Workbook> wbList, List<String> excelTitleList) throws IOException {
        if (wbList == null || wbList.size() == 0) {
            return;
        }
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(targetFile));

            for(int i=0;i<wbList.size();i++) {
                ZipEntry entry = new ZipEntry(excelTitleList.get(i) );
                // 设置压缩包的入口
                zos.putNextEntry(entry);
                wbList.get(i).write(zos);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            zos.flush();

            zos.close();

        }
    }

    //下载zip压缩包
    public static  void toWriteZip( HttpServletResponse response,File file,String fileName)
            throws Exception {
        if( file.exists() ){
            response.setContentType("application/octet-stream");
            String name = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename="
                    + name);
            OutputStream os = response.getOutputStream();
            IOUtils.copy(new FileInputStream(file), os);
            os.flush();
            os.close();
            file.delete();
        }
    }
}
