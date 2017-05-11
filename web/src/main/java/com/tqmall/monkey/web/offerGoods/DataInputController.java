package com.tqmall.monkey.web.offerGoods;

import com.tqmall.monkey.client.excle.OfferExcelService;
import com.tqmall.monkey.client.offerGoods.OfferWrongDataService;
import com.tqmall.monkey.client.shiro.MonkeyJdbcRealm;
import com.tqmall.monkey.component.utils.PoiUtil;
import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.UserDO;
import com.tqmall.monkey.domain.entity.offerGoods.OfferWrongDataDO;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ximeng on 2015/7/6.
 */

@Controller
@RequestMapping("/monkey/offerGoods/dataInput")
public class DataInputController {

    Logger logger = LoggerFactory.getLogger(DataInputController.class);
    //文件的保存位置
    @Value(value = "${file.address}")
    protected String bashFile ;

    //获得当前登录用户信息
    @Autowired
    private MonkeyJdbcRealm monkeyJdbcRealm;
    @Autowired
    private OfferExcelService offerExcelService;
    @Autowired
    private OfferWrongDataService offerWrongDataService;


    //供应商数据导入页面
    @RequestMapping(value = "index" )
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView("monkey/offerGoods/dataInput");
        UserDO user = monkeyJdbcRealm.getCurrentUser();

        if(user == null){
            return new ModelAndView("monkey/login");
        }

//        modelAndView.addObject("listSum",sum);
        return modelAndView;
    }

    @RequestMapping(value = "/uploadExcle", method = RequestMethod.POST)
    public
    @ResponseBody
    HashMap<String,Object> uploadExcle(MultipartHttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.debug("uploadPost called");

        //存放路径
        String path = "";
        MultipartHttpServletRequest multipartRequest =  request;
        MultipartFile multipartFile = multipartRequest.getFile("excle_file");

        if(null == multipartFile){
            return null;
        }
        String sourceName = multipartFile.getOriginalFilename(); // 原始文件名
        String fileType = sourceName.substring(sourceName.lastIndexOf("."));

        logger.info("uploadExcle save as:"+ sourceName + "type:" + fileType);

        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd");
        String str2 = formatter.format(now);
        String base = bashFile + File.separator + "fileUpload" + File.separator + "offerGoods"+ File.separator+str2;

        Boolean hasFile = true;
        File file = new File(base);
        if(!file.exists()){
            hasFile = file.mkdirs();
        }
        if(!hasFile){
            monkeyJdbcRealm.setSession("offerGoods_upload_percentage", "-1");
            return null;
        }
        try {
            path = base +File.separator + sourceName;

            multipartFile.transferTo(new File(path));
            //service.insert("insertAttachment", attach);
            //上传成功后读取Excel表格里面的数据
//            System.out.println("路径是" + path);
            monkeyJdbcRealm.setSession("offerGoods_upload_percentage","20%");
        }catch (Exception e){
            monkeyJdbcRealm.setSession("offerGoods_upload_percentage", "-1");
            System.out.println(e.getMessage());
            return null;
        }

        //开始读取excle中文件进行逻辑处理
        HashMap<String,Object> number_map = offerExcelService.main_process(path, sourceName);
        //文件
//        File deleteFile = new File(path);
//        if (deleteFile.isFile() && deleteFile.exists()) {
//            deleteFile.delete();
//        }
        monkeyJdbcRealm.setSession("offerGoods_upload_percentage","100%");
        return number_map;
    }


    //动态获得文件上传处理进度
    @RequestMapping(value = "/getProgress",method = RequestMethod.GET )
    public
    @ResponseBody
    HashMap<String,Object> getProgress(){
        HashMap<String,Object> result_map = new HashMap<>();

        String progress = (String)monkeyJdbcRealm.getSessionValue("offerGoods_upload_percentage");
        if(progress == null){
            progress = "0%";
            monkeyJdbcRealm.setSession("offerGoods_upload_percentage",progress);
        }
        result_map.put("progress",progress);
//        if(progress.equals("100%")){
//            HashMap<String,Object> number_map = (HashMap<String, Object>) monkeyJdbcRealm.getSessionValue(User.getUserName()+"number");
//            result_map.put("sum_number",number_map.get("sum_number"));
//            result_map.put("success_number",number_map.get("success_number"));
//            result_map.put("fail_number",number_map.get("fail_number"));
//        }
        return result_map;
    }


    //分页查找失败的数据
    @RequestMapping(value = "/searchFailData",method = RequestMethod.GET )
    public
    @ResponseBody
    HashMap<String,Object> searchFailData(Integer index,Integer pageSize){
        UserDO user = monkeyJdbcRealm.getCurrentUser();

        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        String account = user.getUserName();
        Page<OfferWrongDataDO> wrongPage = offerWrongDataService.selectWrongPageBystatus(OfferWrongDataDO.status_new, account, index, pageSize);
        if(wrongPage == null){
            resultMap.put("wrongDataList", Collections.emptyList());
            resultMap.put("totalNumber", 0);
            resultMap.put("totalPages", 0);
        }else{
            List<OfferWrongDataDO> wrongList = wrongPage.getItems();

            //临时编号，存在id中
            Integer tempId = 1;
            for(OfferWrongDataDO offerWrongDataDO : wrongList) {
                String[] failReasonArray = offerWrongDataDO.getFailReason().trim().split(" ");
                offerWrongDataDO.setFailReasonArray(failReasonArray);
                offerWrongDataDO.setId(tempId);
                tempId ++;
            }

            resultMap.put("wrongDataList", wrongList);
            resultMap.put("totalNumber", wrongPage.getTotalNumber());
            resultMap.put("totalPages", wrongPage.getTotalPage());
        }

        return resultMap;
    }

    //该账号未导出的失败数据总数
    @RequestMapping(value = "/sumFailData",method = RequestMethod.GET )
    public
    @ResponseBody
    Integer sumFailData(){
        UserDO user = monkeyJdbcRealm.getCurrentUser();

        String account = user.getUserName();

        Integer failDataSum = offerWrongDataService.selectWrongDataSum(OfferWrongDataDO.status_new, account);
        return  failDataSum;
    }


    //导出失败数据的excle
    @RequestMapping(value = "/failDataToExportExcel")
    public void failDataToExportExcel(HttpServletResponse response) throws Exception{

        String excelTitle = "上传失败数据";
        String[] headName = {
                "失败原因","导入的excle名","原始index","供应商名称","城市","商品名称","属性","OE码","供应价","建议价","标准零件名称","标准零件编号","淘汽品牌","淘汽厂商","淘汽车系","淘汽车型","排量","年款","购买单位","包装规格","备注","规格型号","易损件"
        };
        String[] fieldName = {
                "failReason","recordName","indexs","providerName","city","goodsName","goodsAttribute","oeNumber","primaryPrice","advicePrice","partName","partCode","brand","company","series","model","displacement","year","measureUnit","packageFormat","remark","goodsFormat","quickWearLabel"
        };
        int[] columnWith = {
                2000,7000, 7000,3000,7000,5000, 4000, 3000, 4000, 3000, 3000, 5000, 5000, 3000, 6000, 5000,2000,3000,3000,3000,3000,2500,3000,2500
        };
        int freezeCol = 1;
        int freezeRow = 2;

        UserDO user = monkeyJdbcRealm.getCurrentUser();

        String account = user.getUserName();
        List<OfferWrongDataDO> wrongList = offerWrongDataService.selectWrongListBystatus(OfferWrongDataDO.status_new, account);

        //所有的数据均设置未已导出
        for(OfferWrongDataDO offerWrongDataDO :wrongList){
            Integer id = offerWrongDataDO.getId();
            offerWrongDataService.updateOfferWrongDatStatus(id,OfferWrongDataDO.status_已导出);
        }

        PoiUtil poiUtil = new PoiUtil();
        poiUtil.exportExcelCommon(response,excelTitle,headName,fieldName,wrongList,columnWith,true,freezeCol,freezeRow);


    }


    //导出供应商标准模板
   /* @RequestMapping(value = "/downLoadOfferGoodsTemplate")
    public String downLoadOfferGoodsTemplate(HttpServletResponse response,HttpServletRequest request) throws Exception{
        String fileName = "标准模板.xlsx";
        String path = request.getSession().getServletContext().getRealPath("/") + "/fileDownload" + File.separator + "offerGoods";
        File templateFile = new File(path+ File.separator + fileName);
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName="
                + fileName);

        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {

            in = new BufferedInputStream(new FileInputStream(templateFile));
            out = new BufferedOutputStream(response.getOutputStream());

            byte[] data = new byte[1024];
            int len = 0;
            while (-1 != (len=in.read(data, 0, data.length))) {
                out.write(data, 0, len);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            // 这里主要关闭
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
        //  返回值要注意，要不然就出现下面这句错误！
        //java+getOutputStream() has already been called for this response
        return null;
    }
*/
    //导出供应商标准模板
    @RequestMapping(value = "/downLoadOfferGoodsTemplate")
    public ResponseEntity<byte[]> downLoadOfferGoodsTemplate(HttpServletRequest request) throws IOException {
        String fileName = "标准模板.xlsx";
        String path = bashFile + File.separator + "fileDownload" + File.separator + "offerGoods";
        File templateFile = new File(path+ File.separator + fileName);
        HttpHeaders headers = new HttpHeaders();
        String newfileName=new String("供应商标准模板.xlsx".getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题
        headers.setContentDispositionFormData("attachment", newfileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(templateFile),
                headers, HttpStatus.CREATED);
    }

}
