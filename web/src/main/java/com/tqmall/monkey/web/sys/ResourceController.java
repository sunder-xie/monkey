package com.tqmall.monkey.web.sys;

import com.alibaba.fastjson.JSON;
import com.tqmall.core.common.entity.Result;
import com.tqmall.monkey.client.shiro.MonkeyJdbcRealm;
import com.tqmall.monkey.client.user.ResourceService;
import com.tqmall.monkey.domain.entity.UserDO;
import com.tqmall.monkey.domain.entity.sys.ResourceDO;
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
 * 资源管理
 */
@Controller
@RequestMapping("/monkey/resource")
public class ResourceController extends BaseController {

    @Autowired
    private ResourceService resourceService;

    //获得当前登录用户信息
    @Autowired
    private MonkeyJdbcRealm monkeyJdbcRealm;


    //首页--权限控制
    @RequiresRoles(value={"admin"}, logical= Logical.OR)
    @RequestMapping(value = "index" )
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView("monkey/sys/resource");

        UserDO User = (UserDO)monkeyJdbcRealm.getSessionValue("currentUser");
        if(User == null){
            return null;
        }

        return modelAndView;
    }

    @RequestMapping(value = "/toSearchAllResource", method = RequestMethod.POST)
    @ResponseBody
    public Object toSearchAllResource() throws Exception{
        List<ResourceDO> list = this.resourceService.findAllResource();

        Map<String, Object> data;
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, List<Object>> parentIdMap = new HashMap<>();
        List<Object> children;
        for(ResourceDO resource : list){
            data = new HashMap<>();
            data.put("id", resource.getId());
            data.put("pid", resource.getParentId());
            data.put("name", resource.getResourceName());
            switch(resource.getType()){
                case 1: data.put("type", "菜单"); break;
                default: data.put("type", ""); break;
            }
            data.put("url", resource.getUrl());
            data.put("order", resource.getPriority());
            dataList.add(data);

            children = parentIdMap.get(resource.getParentId().toString());
            if(children==null){
                children = new ArrayList<>();
                parentIdMap.put(resource.getParentId().toString(), children);
            }
            children.add(data);
        }

        //处理成treegrid能识别的数据
        for(Map<String, Object> dataMap : dataList){
            children = parentIdMap.get(dataMap.get("id").toString());
            if(children!=null){
                dataMap.put("children", children);
            }

            parentIdMap.remove(dataMap.get("id").toString());

        }

        List<Object> resultList = new ArrayList<>();
        for(Map.Entry<String, List<Object>> entry : parentIdMap.entrySet()){
            resultList.addAll(entry.getValue());
        }

        return resultList;
    }

    @RequestMapping(value="/toSearchResourceByPid", method=RequestMethod.POST)
    @ResponseBody
    public Result toSearchResourceByPid(@RequestParam Integer pid){
        List<ResourceDO> list = this.resourceService.findByParentId(pid);

        return Result.wrapSuccessfulResult(list);
    }

    @RequestMapping(value="/toSearchResourceById", method=RequestMethod.POST)
    @ResponseBody
    public Result toSearchResourceById(@RequestParam Integer id){
        ResourceDO resource = this.resourceService.findResourceById(id);

        return Result.wrapSuccessfulResult(resource);
    }

    @RequestMapping(value="/toAddResource", method=RequestMethod.POST)
    @ResponseBody
    public Result toAddResource(@RequestParam String jsonString){
        ResourceDO resourceDO = JSON.parseObject(jsonString, ResourceDO.class);
        this.resourceService.saveResource(resourceDO);

        return Result.wrapSuccessfulResult(null);

    }

    @RequestMapping(value="/toModifyResource", method=RequestMethod.POST)
    @ResponseBody
    public Result toModifyResource(@RequestParam String jsonString){
        ResourceDO resourceDO = JSON.parseObject(jsonString, ResourceDO.class);
        this.resourceService.modifyResource(resourceDO);

        return Result.wrapSuccessfulResult(null);
    }

    @RequestMapping(value="/toDeleteResource", method=RequestMethod.POST)
    @ResponseBody
    public Result toDeleteResource(@RequestParam Integer id){



        return Result.wrapSuccessfulResult(null);
    }

    @RequestMapping(value="/toSearchResourceByRoleId", method=RequestMethod.POST)
    @ResponseBody
    public Map toSearchResourceByRoleId(@RequestParam Integer roleId){
        List<ResourceDO> resourceList = this.resourceService.findResourceForRole(roleId);
        List<Object> zTreeList = new ArrayList<>();

        if(resourceList!=null){
            Map<String, Object> zTree;
            for(ResourceDO resource : resourceList) {
                zTree = new HashMap<>();
                zTree.put("id", resource.getId());
                zTree.put("name", resource.getResourceName());
                zTree.put("pId", resource.getParentId());
                zTree.put("open", true);
                //已有的资源
                if(resource.getCheckFlag()){
                    zTree.put("checked", true);
                }
                zTreeList.add(zTree);
            }

        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("zTreeList", zTreeList);

        return resultMap;
    }

}
