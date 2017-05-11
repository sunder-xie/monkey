package com.tqmall.monkey.web.customer;

import com.tqmall.core.common.entity.Result;
import com.tqmall.data.epc.client.bean.dto.avid.AvidCallDTO;
import com.tqmall.monkey.client.lop.AvidCallBiz;
import com.tqmall.monkey.client.shiro.MonkeyJdbcRealm;
import com.tqmall.monkey.component.utils.DateUtil;
import com.tqmall.monkey.domain.bizBO.avid.AvidListShowBO;
import com.tqmall.monkey.domain.bizBO.avid.AvidSearchBO;
import com.tqmall.monkey.domain.bizBO.avid.ModifyAvidCallBO;
import com.tqmall.monkey.domain.bizBO.lop.WishListRequestBO;
import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by zxg on 16/10/24.
 * 16:48
 * no bug,以后改代码的哥们，祝你好运~！！
 * 管家急呼
 */
@Controller
@RequestMapping("/monkey/avidCall")
public class AvidCallController {

    @Autowired
    private MonkeyJdbcRealm monkeyJdbcRealm;//获得当前登录用户信息

    @Autowired
    private AvidCallBiz avidCallBiz;


    @RequestMapping(value = "index")
    public ModelAndView index(String searchParamJson){
        ModelAndView modelAndView = new ModelAndView("monkey/avidCall/avidIndex");
        UserDO user = (UserDO) monkeyJdbcRealm.getSessionValue("currentUser");
        if (user == null) {
            return null;
        }
        modelAndView.addObject("searchParamJson", searchParamJson);
        return modelAndView;
    }

    @RequestMapping(value = "detail")
    public ModelAndView detail(Integer id, String searchParamJson){
        UserDO user = (UserDO) monkeyJdbcRealm.getSessionValue("currentUser");
        if (user == null) {
            return null;
        }
        ModelAndView modelAndView = new ModelAndView("monkey/avidCall/aviddetails");
        Result<AvidCallDTO> result = avidCallBiz.getAvidCallById(id);
        if(result.isSuccess()){
            modelAndView.addObject("avidCallDetail", result.getData());
        }
        modelAndView.addObject("token", DateUtil.token());
        modelAndView.addObject("searchParamJson", searchParamJson);
        return modelAndView;
    }

    @RequestMapping(value = "turnWish")
    @ResponseBody
    public Result turnWish(@RequestBody WishListRequestBO requestBO){
        return avidCallBiz.turnWish(requestBO);
    }

    @RequestMapping(value = "/getAvidPage", method = RequestMethod.POST)
    @ResponseBody
    public Result<Page<AvidListShowBO>> getAvidPage(AvidSearchBO avidSearchBO){
        return avidCallBiz.getAvidPageShow(avidSearchBO);
    }

    @RequestMapping(value = "cancelAvidCall")
    @ResponseBody
    public Result cancelAvidCall(Integer id, String reason){
        return avidCallBiz.cancelAvidCall(id, reason);
    }

    @RequestMapping(value = "modifyAvidCall")
    @ResponseBody
    public Result modifyAvidCall(@RequestBody ModifyAvidCallBO modifyAvidCallBO){
        return avidCallBiz.modifyAvidCall(modifyAvidCallBO);
    }

    @RequestMapping("getAllNewDataNum")
    @ResponseBody
    public Result<Long> getAllNewDataNum(){
        return avidCallBiz.getAllNewDataNum();
    }
    @RequestMapping("getFiveMinNewDataNum")
    @ResponseBody
    public Result<Long> getFiveMinNewDataNum(){
        return avidCallBiz.getFiveMinNewDataNum();
    }

}
