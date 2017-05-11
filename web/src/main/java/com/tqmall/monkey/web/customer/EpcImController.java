package com.tqmall.monkey.web.customer;

import com.tqmall.monkey.client.shiro.MonkeyJdbcRealm;
import com.tqmall.monkey.domain.entity.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by zxg on 16/10/10.
 * 14:41
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Controller
@RequestMapping("/monkey/epcim")
public class EpcImController {
    @Value("${monk.host}")
    protected String monkHost;

    @Autowired
    private MonkeyJdbcRealm monkeyJdbcRealm;//获得当前登录用户信息

    @RequestMapping(value = "index")
    public ModelAndView index() throws Exception {
        ModelAndView modelAndView = new ModelAndView("monkey/chat/epcIm");
        UserDO user = (UserDO) monkeyJdbcRealm.getSessionValue("currentUser");
        if (user == null) {
            return null;
        }
        modelAndView.addObject("monkHost",monkHost);
        modelAndView.addObject("userId",user.getId());
        return modelAndView;
    }
}
