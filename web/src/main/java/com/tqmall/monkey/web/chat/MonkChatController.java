package com.tqmall.monkey.web.chat;

import com.tqmall.monkey.domain.entity.UserDO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by zxg on 16/10/10.
 * 17:44
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Controller
@RequestMapping("/monkey/monkChat")
public class MonkChatController {

    @Value("${monk.host}")
    protected String monkHost;

    @RequestMapping(value = "index")
    public ModelAndView index() throws Exception {
        ModelAndView modelAndView = new ModelAndView("monkey/chat/indexIm");
        modelAndView.addObject("monkHost",monkHost);
        return modelAndView;
    }
}
