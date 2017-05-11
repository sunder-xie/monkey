package com.tqmall.monkey.web.sys;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tqmall.core.common.entity.Result;
import com.tqmall.monkey.client.shiro.MonkeyJdbcRealm;
import com.tqmall.monkey.client.user.UserService;
import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.UserDO;
import com.tqmall.monkey.domain.entity.sys.UserRole;
import com.tqmall.monkey.web.BaseController;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Created by hzt on 2015/5/18.
 * 用户管理
 */
@Controller
@RequestMapping("/monkey/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    //获得当前登录用户信息
    @Autowired
    private MonkeyJdbcRealm monkeyJdbcRealm;


    //首页--权限控制
    @RequiresRoles(value={"admin"}, logical= Logical.OR)
    @RequestMapping(value = "index" )
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView("monkey/sys/user");

        UserDO User = (UserDO)monkeyJdbcRealm.getSessionValue("currentUser");
        if(User == null){
            return null;
        }

        return modelAndView;
    }

    @RequestMapping(value = "/toSearchUsers", method = RequestMethod.POST)
    @ResponseBody
    public Map toSearchUsers(@RequestParam String search_name,
        @RequestParam String search_cn_name, @RequestParam Integer pageIndex){

        Integer pageSize = 10;

        Page<UserDO> page = this.userService.findUserPage(search_name,search_cn_name,pageIndex,pageSize);
        List<UserDO> userList = page.getItems();

        List<HashMap<String, Object>> resultList = new ArrayList<HashMap<String, Object>>();

        if(userList!=null && !userList.isEmpty()) {
            HashMap<String, Object> dataMap;
            UserDO user;
            int size = userList.size();
            for (int i=0; i<size; i++) {
                user = userList.get(i);
                dataMap = new HashMap<String, Object>();
                dataMap.put("seqNumber", (i+1));
                dataMap.put("userId", user.getId());
                dataMap.put("userName", user.getUserName());
                dataMap.put("cnName",user.getNickName());

                resultList.add(dataMap);
            }
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("data", resultList);
        resultMap.put("totalRows", page.getTotalNumber());
        resultMap.put("totalPages", page.getTotalPage());

        return resultMap;
    }

    @RequestMapping(value = "/toSearchAllUsers", method = RequestMethod.POST)
    @ResponseBody
    public Map toSearchAllUsers() throws Exception {

        List<UserDO> userlist = this.userService.findAllUsers();

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("userlist", userlist);

        return resultMap;
    }


    @RequestMapping(value="/toSearchUserById", method=RequestMethod.POST)
    @ResponseBody
    public Result toSearchUserById(@RequestParam Integer userId){

        UserDO user = this.userService.findUserById(userId);

        return Result.wrapSuccessfulResult(user);
    }

    @RequestMapping(value="/toAddUser", method=RequestMethod.POST)
    @ResponseBody
    public Result toAddUser(@RequestParam String userJsonString){

        UserDO user = JSON.parseObject(userJsonString, UserDO.class);
        UserDO checkUser = this.userService.getUserDO(user.getUserName());
        if(checkUser != null){
            return Result.wrapErrorResult(null, "用户名已存在！");
        }else{
            user.setDept("");
            user.setCompany("");

            this.userService.addLoginUser(user);

            return Result.wrapSuccessfulResult(null);
        }

    }

    @RequestMapping(value="/toModifyUser", method=RequestMethod.POST)
    @ResponseBody
    public Result toModifyUser(@RequestParam String userJsonString){

        UserDO user = JSON.parseObject(userJsonString, UserDO.class);

        this.userService.modifyUser(user);

        return Result.wrapSuccessfulResult(null);
    }

    @RequestMapping(value="/toDeleteUser", method=RequestMethod.POST)
    @ResponseBody
    public Result toDeleteUser(@RequestParam Integer userId){

        this.userService.removeUserById(userId);

        return Result.wrapSuccessfulResult(null);
    }

    @RequestMapping(value="/toGetLoginUser", method=RequestMethod.POST)
    @ResponseBody
    public Result toGetLoginUser() throws Exception{
        UserDO User = (UserDO)monkeyJdbcRealm.getSessionValue("currentUser");
        if(User == null){
            return Result.wrapErrorResult(null, "获取用户失败");
        }

        return Result.wrapSuccessfulResult(User);
    }

    /*
    *   修改用户角色
    * */
    @RequestMapping(value="toModifyUserRole", method = RequestMethod.POST)
    @ResponseBody
    public Result toModifyUserRole(@RequestParam String jsonString) throws Exception{

        JSONObject jsonObject = JSON.parseObject(jsonString);
        JSONArray addArray = jsonObject.getJSONArray("addArray");
        JSONArray deleteArray = jsonObject.getJSONArray("deleteArray");

        List<UserRole> addList = null;
        if(addArray.size()>0){
            addList = JSON.parseArray(addArray.toString(), UserRole.class);
        }

        List<UserRole> deleteList = null;
        if(deleteArray.size()>0){
            deleteList = JSON.parseArray(deleteArray.toString(), UserRole.class);
        }

        this.userService.modifyUserRole(addList, deleteList);

        return Result.wrapSuccessfulResult(null);
    }

}
