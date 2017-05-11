package com.tqmall.monkey.web.part;

import com.tqmall.core.common.entity.Result;
import com.tqmall.monkey.client.excle.PartExcelService;
import com.tqmall.monkey.client.part.PartLiyangService;
import com.tqmall.monkey.client.redisManager.part.PartGoodsRedisManager;
import com.tqmall.monkey.client.redisManager.part.PartLiyangRedisManager;
import com.tqmall.monkey.client.shiro.MonkeyJdbcRealm;
import com.tqmall.monkey.client.user.SysRecordService;
import com.tqmall.monkey.domain.entity.UserDO;
import com.tqmall.monkey.domain.entity.part.PartLiyangRelationDO;
import com.tqmall.monkey.domain.entity.sys.SysRecordDO;
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
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 配件（EPC）管理的导入模块
 * Created by ximeng on 2015/7/23.
 */

@Controller
@RequestMapping("/monkey/part/dataInput")
public class PartDataInputController {

    Logger logger = LoggerFactory.getLogger(PartDataInputController.class);
    //文件的保存位置
    @Value(value = "${file.address}")
    protected String bashFile ;

    //获得当前登录用户信息
    @Autowired
    private MonkeyJdbcRealm monkeyJdbcRealm;
    @Autowired
    private PartExcelService partExcelService;
    @Autowired
    private PartGoodsRedisManager partGoodsRedisManager;
    @Autowired
    private PartLiyangService partLiyangService;

    @Autowired
    private SysRecordService sysRecordService;

    //供应商数据导入页面
    @RequestMapping(value = "index" )
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView("monkey/part/dataInput");
        UserDO User = monkeyJdbcRealm.getCurrentUser();

        if(User == null){
            return new ModelAndView("monkey/login");
        }

        return modelAndView;
    }

    @RequestMapping(value = "/uploadExcle", method = RequestMethod.POST)
    public
    @ResponseBody
    Result uploadExcle(MultipartHttpServletRequest request,String brand,String factory,String series,String model)
            throws ServletException, IOException {
        logger.debug("uploadPost called");

        UserDO User = monkeyJdbcRealm.getCurrentUser();

        monkeyJdbcRealm.setSession("part_upload_percentage", "0%");
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
//        System.out.println("上传的文件名为:" + sourceName + "类型为:" + fileType);


        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd");
        String str2 = formatter.format(now);
        String base = bashFile + File.separator + "fileUpload" + File.separator + "part"+ File.separator+str2;
        File file = new File(base);
        if(!file.exists()){
            file.mkdirs();
        }
        try {
            path = base +File.separator + sourceName;

            multipartFile.transferTo(new File(path));
            monkeyJdbcRealm.setSession("part_upload_percentage","3%");
        }catch (Exception e){
            monkeyJdbcRealm.setSession("part_upload_percentage", "-1");
            logger.error(e.getMessage());
            return Result.wrapSuccessfulResult(e.getMessage());
        }
        //存记录
        SysRecordDO sysRecordDO = new SysRecordDO();
        sysRecordDO.setUserId(User.getId());
        sysRecordDO.setResourceName("配件库excle导入模块");
        sysRecordDO.setContent("文件:"+sourceName + " 选择的车型:"+model+"-导入数据库");
        sysRecordDO.setStatus(SysRecordDO.Status.NOTOK.getIndex());

        Integer sysRecorderId = sysRecordService.insertRecord(sysRecordDO);

        SysRecordDO updateSys = new SysRecordDO();

        updateSys.setId(sysRecorderId);

        //开始读取excle中文件进行逻辑处理
        monkeyJdbcRealm.setSession("part_upload_percentage", "3%");
        List<String> failList = partExcelService.main_process(path,sourceName,brand,factory,series,model);

        if(!CollectionUtils.isEmpty(failList)) {
            monkeyJdbcRealm.setSession("part_upload_percentage", "-1");
            updateSys.setStatus(SysRecordDO.Status.FAIL.getIndex());
            updateSys.setContent("文件：" + sourceName + " 失败原因："+failList);
        }else{
            monkeyJdbcRealm.setSession("part_upload_percentage", "100%");
            //更新记录状态
            updateSys.setStatus(SysRecordDO.Status.OK.getIndex());
            //清理缓存 -- 在 main_process 中做了
//            Integer partLId = partLiyangService.getPartLiyang(brand, factory, series, model);
//            partGoodsRedisManager.deleteGoodsByCar(partLId);
        }

        sysRecordService.updateRecord(updateSys);

        return Result.wrapSuccessfulResult(failList);
    }


    //动态获得文件上传处理进度
    @RequestMapping(value = "/getProgress",method = RequestMethod.GET )
    public
    @ResponseBody
    HashMap<String,Object> getProgress(){
        HashMap<String,Object> result_map = new HashMap<>();

        String progress = (String)monkeyJdbcRealm.getSessionValue("part_upload_percentage");
        if(progress == null){
            progress = "0%";
            monkeyJdbcRealm.setSession("part_upload_percentage",progress);
        }
        result_map.put("progress", progress);
        return result_map;
    }







    //导出供应商标准模板
    @RequestMapping(value = "/downLoadOfferGoodsTemplate")
    public ResponseEntity<byte[]> downLoadOfferGoodsTemplate(HttpServletRequest request) throws IOException {
        String fileName = "标准模板.xlsx";
        String path = bashFile + File.separator + "fileDownload" + File.separator + "part";
        File templateFile = new File(path+ File.separator + fileName);
        HttpHeaders headers = new HttpHeaders();
        String newfileName=new String("配件库标准模板.xlsx".getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题
        headers.setContentDispositionFormData("attachment", newfileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(templateFile),
                headers, HttpStatus.CREATED);
    }

}
