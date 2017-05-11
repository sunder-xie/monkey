package com.tqmall.monkey.web.sys;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tqmall.core.common.entity.Result;
import com.tqmall.monkey.client.shiro.MonkeyJdbcRealm;
import com.tqmall.monkey.client.user.RoleService;
import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.UserDO;
import com.tqmall.monkey.domain.entity.sys.RoleDO;
import com.tqmall.monkey.domain.entity.sys.RoleResource;
import com.tqmall.monkey.web.BaseController;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hzt on 2015/5/18.
 * 角色管理
 */
@Controller
@RequestMapping("/monkey/role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    //获得当前登录用户信息
    @Autowired
    private MonkeyJdbcRealm monkeyJdbcRealm;


    //首页--权限控制
    @RequiresRoles(value={"admin"}, logical= Logical.OR)
    @RequestMapping(value = "index" )
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView("monkey/sys/role");

        UserDO User = (UserDO)monkeyJdbcRealm.getSessionValue("currentUser");
        if(User == null){
            return null;
        }

        return modelAndView;
    }

    @RequestMapping(value = "/toSearchRoles", method = RequestMethod.POST)
    @ResponseBody
    public Map toSearchRoles(@RequestParam String search_name, @RequestParam Integer pageIndex){

        Integer pageSize = 10;

        Page<RoleDO> page = this.roleService.findRolesPage(search_name, pageIndex, pageSize);
        List<RoleDO> roleList = page.getItems();

        List<HashMap<String, Object>> resultList = new ArrayList<HashMap<String, Object>>();

        if(roleList!=null && !roleList.isEmpty()) {
            HashMap<String, Object> dataMap;
            RoleDO role;
            int size = roleList.size();
            for (int i=0; i<size; i++) {
                role = roleList.get(i);
                dataMap = new HashMap<String, Object>();
                dataMap.put("seqNumber", (i+1));
                dataMap.put("roleId", role.getId());
                dataMap.put("roleName", role.getRoleName());
                dataMap.put("description", role.getDescription());

                resultList.add(dataMap);
            }
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("data", resultList);
        resultMap.put("totalRows", page.getTotalNumber());
        resultMap.put("totalPages", page.getTotalPage());

        return resultMap;
    }

    @RequestMapping(value = "/toSearchAllRoles", method = RequestMethod.POST)
    @ResponseBody
    public Result toSearchAllRoles() throws Exception{
        List<RoleDO> list = this.roleService.findAllRoles();

        return Result.wrapSuccessfulResult(list);
    }


        @RequestMapping(value="/toSearchRoleById", method=RequestMethod.POST)
    @ResponseBody
    public Result toSearchRoleById(@RequestParam Integer roleId){

        RoleDO role = this.roleService.findRoleById(roleId);

        return Result.wrapSuccessfulResult(role);
    }

    @RequestMapping(value="/toAddRole", method=RequestMethod.POST)
    @ResponseBody
    public Result toAddRole(@RequestParam String jsonString){

        RoleDO role = JSON.parseObject(jsonString, RoleDO.class);
        RoleDO checkRole = this.roleService.findRoleByName(role.getRoleName());
        if(checkRole != null){
            return Result.wrapErrorResult(null, "角色名已存在！");
        }else{
            this.roleService.addRole(role);

            return Result.wrapSuccessfulResult(null);
        }

    }

    @RequestMapping(value="/toModifyRole", method=RequestMethod.POST)
    @ResponseBody
    public Result toModifyRole(@RequestParam String jsonString){

        RoleDO role = JSON.parseObject(jsonString, RoleDO.class);

        this.roleService.modifyRole(role);

        return Result.wrapSuccessfulResult(null);
    }

    @RequestMapping(value="/toDeleteRole", method=RequestMethod.POST)
    @ResponseBody
    public Result toDeleteRole(@RequestParam Integer roleId){

        this.roleService.removeRoleById(roleId);

        return Result.wrapSuccessfulResult(null);
    }

    /*
    *   修改角色资源
    * */
    @RequestMapping(value="toModifyRoleResource", method = RequestMethod.POST)
    @ResponseBody
    public Result toModifyRoleResource(@RequestParam String jsonString) throws Exception{

        JSONObject jsonObject = JSON.parseObject(jsonString);
        JSONArray addArray = jsonObject.getJSONArray("addArray");
        JSONArray deleteArray = jsonObject.getJSONArray("deleteArray");

        List<RoleResource> addList = null;
        if(addArray.size()>0){
            addList = JSON.parseArray(addArray.toString(), RoleResource.class);
        }

        List<RoleResource> deleteList = null;
        if(deleteArray.size()>0){
            deleteList = JSON.parseArray(deleteArray.toString(), RoleResource.class);
        }

        this.roleService.modifyRoleResource(addList, deleteList);

        return Result.wrapSuccessfulResult(null);
    }

    @RequestMapping(value="/toSearchRoleByUserId", method=RequestMethod.POST)
    @ResponseBody
    public List toSearchRoleByUserId(@RequestParam Integer userId){
        List<RoleDO> roleList = this.roleService.findRoleForUser(userId);
        List<Object> zTreeList = new ArrayList<>();

        if(roleList!=null){
            Map<String, Object> zTree;
            for(RoleDO role : roleList) {
                zTree = new HashMap<>();
                zTree.put("id", role.getId());
                zTree.put("name", role.getRoleName()+"【"+role.getDescription()+"】");
                zTree.put("open", true);
                //已有的角色
                if(role.getCheckFlag()){
                    zTree.put("checked", true);
                }
                zTreeList.add(zTree);
            }

        }

        return zTreeList;
    }


}
