package com.tqmall.monkey.web.commodity;

import com.tqmall.monkey.client.commodity.CommodityGoodsAttrService;
import com.tqmall.monkey.client.commodity.CommodityGoodsCarService;
import com.tqmall.monkey.client.commodity.CommodityGoodsOeService;
import com.tqmall.monkey.client.commodity.CommodityGoodsService;
import com.tqmall.monkey.client.excle.CommodityExcel;
import com.tqmall.monkey.client.shiro.MonkeyJdbcRealm;
import com.tqmall.monkey.domain.entity.UserDO;
import com.tqmall.monkey.domain.entity.commodity.CommodityGoodsAttrDO;
import com.tqmall.monkey.domain.entity.commodity.CommodityGoodsCarDO;
import com.tqmall.monkey.domain.entity.commodity.CommodityGoodsDO;
import com.tqmall.monkey.domain.entity.commodity.CommodityGoodsOeDO;
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
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zxg on 15/8/22.
 * 商品库输入
 */

@Controller
@RequestMapping("/monkey/commodity/input")
public class CommodityInputController {

    Logger logger = LoggerFactory.getLogger(CommodityInputController.class);

    //文件的保存位置
    @Value(value = "${file.address}")
    protected String bashFile ;

    //获得当前登录用户信息
    @Autowired
    private MonkeyJdbcRealm monkeyJdbcRealm;

    @Autowired
    private CommodityExcel commodityExcel;
    @Autowired
    private CommodityGoodsService commodityGoodsService;
    @Autowired
    private CommodityGoodsAttrService commodityGoodsAttrService;
    @Autowired
    private CommodityGoodsCarService commodityGoodsCarService;
    @Autowired
    private CommodityGoodsOeService commodityGoodsOeService;

    //excle处理后的返回数据
    private static HashMap<String ,CommodityGoodsDO> upGoodsMap;
    private static List<CommodityGoodsCarDO> needUpGoodsCarList = null;
    private static List<CommodityGoodsOeDO> needUpGoodsOeList = null;
    private static List<CommodityGoodsAttrDO> needUpGoodsAttrList = null;

    //商品库展示
    @RequestMapping(value = "index" )
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView("monkey/commodity/input");

        UserDO user = monkeyJdbcRealm.getCurrentUser();
        if(user == null){
            return new ModelAndView("monkey/login");
        }
        return modelAndView;
    }



    @RequestMapping(value = "/uploadExcle", method = RequestMethod.POST)
    public
    @ResponseBody
    HashMap<String,Object> uploadExcle(MultipartHttpServletRequest  request , HttpServletResponse response)
            throws ServletException, IOException {
        logger.debug("uploadPost called");
        monkeyJdbcRealm.setSession("commodity_upload_percentage", "0%");

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
        String base = bashFile + File.separator + "fileUpload" + File.separator + "commodity"+ File.separator+str2;
        File file = new File(base);
        if(!file.exists()){
            file.mkdirs();
        }
        try {
            path = base +File.separator + sourceName;

            multipartFile.transferTo(new File(path));
            monkeyJdbcRealm.setSession("commodity_upload_percentage","5%");
        }catch (Exception e){
            monkeyJdbcRealm.setSession("commodity_upload_percentage", "-1");
            System.out.println(e.getMessage());
            return null;
        }

        //开始读取excle中文件进行逻辑处理
        commodityExcel.main_process(path, sourceName);

        HashMap<String,Object> resultMap = new HashMap<>();
        
        Integer successNum = commodityExcel.getSuccessSum();
        this.upGoodsMap = commodityExcel.getUpGoods();
        this.needUpGoodsCarList = commodityExcel.getUpGoodsCar();
        this.needUpGoodsOeList = commodityExcel.getUpGoodsOe();
        this.needUpGoodsAttrList = commodityExcel.getUpGoodsAttr();

        List<String> failList = commodityExcel.getfailList();
        
        if(null == upGoodsMap || upGoodsMap.isEmpty() ) {
            monkeyJdbcRealm.setSession("commodity_upload_percentage", "100%");
        }else{
            monkeyJdbcRealm.setSession("commodity_upload_percentage", "-2");
            Set<String> upGoodsFormatBrandList = new HashSet<>();
            Set<Map.Entry<String, CommodityGoodsDO>> entrySet = upGoodsMap.entrySet();
            for (Map.Entry<String, CommodityGoodsDO> entry : entrySet) {
                CommodityGoodsDO o = entry.getValue();
                upGoodsFormatBrandList.add(o.getGoodsFormat()+"-"+o.getBrandName());
            }
//            Set<String> upGoodsUuId = upGoodsMap.keySet();
            resultMap.put("upGoodsNum", upGoodsFormatBrandList.size());
            resultMap.put("upGoodsList", upGoodsFormatBrandList);
        }
        resultMap.put("successNum",successNum);
        resultMap.put("failNum",failList.size());
        resultMap.put("failList",failList);

        return resultMap;
    }


    //动态获得文件上传处理进度
    @RequestMapping(value = "/getProgress",method = RequestMethod.GET )
    public
    @ResponseBody
    HashMap<String,Object> getProgress(){
        HashMap<String,Object> result_map = new HashMap<>();

        String progress = (String)monkeyJdbcRealm.getSessionValue("commodity_upload_percentage");
        if(progress == null){
            progress = "0%";
            monkeyJdbcRealm.setSession("commodity_upload_percentage",progress);
        }
        result_map.put("progress",progress);
        return result_map;
    }

    //替换冲突的数据
    @RequestMapping(value = "/repalceHave",method = RequestMethod.GET )
    public
    @ResponseBody
    boolean repalceHave(){
        UserDO user = monkeyJdbcRealm.getCurrentUser();
        if(user == null){
            monkeyJdbcRealm.setSession("commodity_upload_percentage", "-1");
            return false;
        }
        Integer userId = user.getId();

        HashMap<String,CommodityGoodsDO> needUpGoodsMap = this.upGoodsMap;

        List<String> upGoodsList = new ArrayList<>();
        List<CommodityGoodsDO> upGoodsDOList = new ArrayList<>();
        Set<Map.Entry<String, CommodityGoodsDO>> entrySet = needUpGoodsMap.entrySet();
        for (Map.Entry<String, CommodityGoodsDO> entry : entrySet) {
            CommodityGoodsDO o = entry.getValue();
            String uuId = o.getUuId();
            if(Collections.frequency(upGoodsList, uuId) < 1) upGoodsList.add(uuId);
            if(o.getIsdelete().equals(1)) {
                upGoodsDOList.add(o);
            }
        }

//        commodityGoodsCarService.deleteBatchAttr(upGoodsList,userId);
        commodityGoodsOeService.deleteBatchOeByGoodsUuId(upGoodsList,userId);
        commodityGoodsAttrService.deleteBatchAttr(upGoodsList,userId);

        monkeyJdbcRealm.setSession("commodity_upload_percentage", "85%");
        //批量置基本商品数据 isdelete 为 0
        for(CommodityGoodsDO goodsDO : upGoodsDOList){
            goodsDO.setIsdelete(0);
            commodityGoodsService.updateStatus(goodsDO);
        }
        monkeyJdbcRealm.setSession("commodity_upload_percentage", "90%");

        //goods car
        List<CommodityGoodsCarDO> inGoodsCarDataBaseList = new ArrayList<>();
        List<CommodityGoodsCarDO> deleteGoodsCarDataBaseList = new ArrayList<>();
        HashMap<CommodityGoodsCarDO,Integer> idGoodsCarDataBaseMap = new HashMap<>();
        for(String uuId : upGoodsList ){
            List<CommodityGoodsCarDO> findGoodsList = commodityGoodsCarService.getListByGoodsUuIdWithOutDelete(uuId);
            for(CommodityGoodsCarDO carDO : findGoodsList){
                CommodityGoodsCarDO existDO = new CommodityGoodsCarDO();
                existDO.setGoodsUuId(uuId);
                existDO.setLiyangId(carDO.getLiyangId());
                inGoodsCarDataBaseList.add(existDO);

                Integer isdelete = carDO.getIsdelete();
                if (isdelete == 1) {
                    deleteGoodsCarDataBaseList.add(existDO);
                    idGoodsCarDataBaseMap.put(existDO, carDO.getId());
                }
            }
        }

        List<Integer> goodsCarIdList = new ArrayList<>();
        List<CommodityGoodsCarDO> insertBatchCarList = new ArrayList<>();
        for(CommodityGoodsCarDO carDO : this.needUpGoodsCarList){
            CommodityGoodsCarDO existDO = new CommodityGoodsCarDO();
            existDO.setGoodsUuId(carDO.getGoodsUuId());
            existDO.setLiyangId(carDO.getLiyangId());
            if(inGoodsCarDataBaseList.contains(existDO)){
                if(deleteGoodsCarDataBaseList.contains(existDO)){
                    Integer id = idGoodsCarDataBaseMap.get(existDO);
                    goodsCarIdList.add(id);
                }
            }else{
                insertBatchCarList.add(carDO);
            }
        }
        commodityGoodsCarService.updataBatchById(0,goodsCarIdList,userId );
        commodityGoodsCarService.insertBatchCarByLoadData(insertBatchCarList);
        monkeyJdbcRealm.setSession("commodity_upload_percentage", "95%");

        //goods oe
        List<Integer> goodsOeIdList = new ArrayList<>();
        List<CommodityGoodsOeDO> insertBatchGoodsOeList = new ArrayList<>();
        for(CommodityGoodsOeDO oeDO : this.needUpGoodsOeList) {
            CommodityGoodsOeDO existDO = commodityGoodsOeService.getByUuIdOe(oeDO.getGoodsUuId(),oeDO.getOeNumber());
            if(null == existDO){
                insertBatchGoodsOeList.add(oeDO);
            }else {
                goodsOeIdList.add(existDO.getId());
            }
        }
        commodityGoodsOeService.updataBatchById(0,goodsOeIdList,userId );
        commodityGoodsOeService.insertBatchOeByLoadData(insertBatchGoodsOeList);
        monkeyJdbcRealm.setSession("commodity_upload_percentage", "98%");

        //goods attr
        List<CommodityGoodsAttrDO> insertBatchGoodsAttrList = new ArrayList<>();
        for(CommodityGoodsAttrDO attrDO : this.needUpGoodsAttrList){
            CommodityGoodsAttrDO existDO = commodityGoodsAttrService.getByUuIdAttrId(attrDO.getGoodsUuId(),attrDO.getAttrId());
            if(null == existDO){
                insertBatchGoodsAttrList.add(attrDO);
            }else{
                attrDO.setId(existDO.getId());
                attrDO.setIsdelete(0);
                commodityGoodsAttrService.updateAttr(attrDO);
            }
        }

        commodityGoodsAttrService.insertBatchAttrByLoadData(insertBatchGoodsAttrList);

        monkeyJdbcRealm.setSession("commodity_upload_percentage", "100%");
        return true;
    }


    //导出商品库标准模板
    @RequestMapping(value = "/downLoadTemplate")
    public ResponseEntity<byte[]> downLoadOfferGoodsTemplate(HttpServletRequest request) throws IOException {
        String fileName = "标准模板.xlsx";
        String path = bashFile + File.separator + "fileDownload" + File.separator + "commodity";
        File templateFile = new File(path+ File.separator + fileName);
        HttpHeaders headers = new HttpHeaders();
        String newfileName=new String("商品库标准模板.xlsx".getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题
        headers.setContentDispositionFormData("attachment", newfileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(templateFile),
                headers, HttpStatus.CREATED);
    }
}
