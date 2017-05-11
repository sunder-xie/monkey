package com.tqmall.monkey.web.main;

import com.tqmall.monkey.client.shiro.MonkeyJdbcRealm;
import com.tqmall.monkey.client.user.SysRecordService;
import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.UserDO;
import com.tqmall.monkey.domain.entity.sys.SysRecordDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by zxg on 15/8/6.
 */
@Controller
@RequestMapping("/monkey/main")
public class MainIndexController {
    //获得当前登录用户信息
    @Autowired
    private MonkeyJdbcRealm monkeyJdbcRealm;

    @Autowired
    private SysRecordService sysRecordService;

    //首页
    @RequestMapping(value = "index" )
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView("monkey/common/index");

        UserDO User = (UserDO)monkeyJdbcRealm.getSessionValue("currentUser");
        if(User == null){
            return null;
        }

        return modelAndView;
    }

    //获得记录数
    @RequestMapping(value = "/getRecordPage", method = RequestMethod.GET)
    public
    @ResponseBody
    HashMap<String, Object> getRecordPage(Integer index,Integer pageSize){

        HashMap<String, Object> resultMap = new HashMap<>();
        UserDO user = (UserDO)monkeyJdbcRealm.getSessionValue("currentUser");
        if(user == null){
            return null;
        }
        Integer userId = user.getId();
        Page<SysRecordDO> recordDOPage = sysRecordService.getRecordPage(index,pageSize,userId);

        Integer indexs = 1;
        for(SysRecordDO sysRecordDO : recordDOPage.getItems()){
            sysRecordDO.setIndexs(indexs);

            indexs ++;
            //更改日期显示形式
            Date modefiedDate = sysRecordDO.getGmtModified();
            SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
            String modifiedString = formatter.format(modefiedDate);

            sysRecordDO.setModifiedString(modifiedString);
            //状态文本
            Integer status = sysRecordDO.getStatus();
            String statusString = SysRecordDO.Status.getName(status);
            sysRecordDO.setStatusString(statusString);
        }

        resultMap.put("recordList", recordDOPage.getItems());
        resultMap.put("totalNumber", recordDOPage.getTotalNumber());
        resultMap.put("totalPages", recordDOPage.getTotalPage());

        return resultMap;
    }
}
