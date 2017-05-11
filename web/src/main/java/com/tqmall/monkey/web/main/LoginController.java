package com.tqmall.monkey.web.main;

import com.tqmall.core.common.entity.Result;
import com.tqmall.monkey.client.shiro.MonkeyJdbcRealm;
import com.tqmall.monkey.client.user.UserService;
import com.tqmall.monkey.domain.entity.UserDO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ximeng on 2015/4/1.
 */
@Controller
@RequestMapping("/loginController")
public class LoginController {
    @Autowired
    private UserService userService;
    //获得当前登录用户信息
    @Autowired
    private MonkeyJdbcRealm monkeyJdbcRealm;

    //登录界面
    @RequestMapping(value = "login" )
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView("monkey/login");

        return modelAndView;
    }

    @RequestMapping("logout")
    public String logout(){
        // 获取当前用户
        Subject subject = SecurityUtils.getSubject();
        // 注销
        subject.logout();

        //必须重定向，否则会报异常
        return "redirect:/loginController/login";
    }

    //首页界面
    @RequestMapping(value = "main" )
    public ModelAndView main(){
        UserDO User = monkeyJdbcRealm.getCurrentUser();

        if(User == null){
            ModelAndView modelAndView = new ModelAndView("monkey/login");
            modelAndView.addObject("message", "用户名或密码错误");
            return modelAndView;
        }else{
            ModelAndView modelAndView1 = new ModelAndView("monkey/common/main");
            modelAndView1.addObject("account", User.getUserName());
            modelAndView1.addObject("name", User.getNickName());
            return modelAndView1;
        }

    }

    //根据用户名和密码查询是否可登陆
    @RequestMapping(value = "/checkLogin")
    public ModelAndView checkLogin(String username,String password){
        ModelAndView modelAndView = new ModelAndView("monkey/login");

        if(username == null || password == null){
            modelAndView.addObject("message", "用户名或密码为空");
            return modelAndView;
        }
        username = username.trim();
        password = password.trim();
        String account = username;
        UserDO userDO = new UserDO();
        userDO.setUserName(account);
        userDO.setPassWord(password);
        boolean isExist = userService.checkExistUser(userDO);
        modelAndView.addObject("message", "用户名或密码错误");

        if(isExist){
//            userDO = userService.getUserDO(account);
//            String name = "获取用户名失败";
//            if(userDO != null){
//                name = userDO.getNickName();
//            }
            //shiro session
            Subject subject = SecurityUtils.getSubject();
            subject.login(new UsernamePasswordToken(username, (password)));
            if (subject.isAuthenticated()) {

//                ModelAndView modelAndView1 = new ModelAndView("common/main");
//                modelAndView1.addObject("account", account);
//                modelAndView1.addObject("name", name);
                return this.main();
            }
        }
        return modelAndView;
    }



    //根据当前用户获得左侧菜单栏
    @RequestMapping(value = "/getLeftLi",method = RequestMethod.GET)
    public
    @ResponseBody
    Result getLeftLi(){
        UserDO User = monkeyJdbcRealm.getCurrentUser();

        if(User == null){
            return Result.wrapErrorResult("001", "no user");
        }
        Integer userId = User.getId();

        List<HashMap<String,Object>> resultList = userService.getLeftLi(userId);

        return Result.wrapSuccessfulResult(resultList);
    }
}
