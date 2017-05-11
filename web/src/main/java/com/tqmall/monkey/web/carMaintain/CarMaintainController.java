package com.tqmall.monkey.web.carMaintain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tqmall.core.common.entity.Result;
import com.tqmall.monkey.client.carMaintain.*;
import com.tqmall.monkey.client.shiro.MonkeyJdbcRealm;
import com.tqmall.monkey.domain.entity.carMaintain.*;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Created by huangzhangting on 15/8/4.
 */

@Controller
@RequestMapping("/monkey/carMaintain")
public class CarMaintainController {

    @Autowired
    private ModelMaintainPlanService modelMaintainPlanService;
    @Autowired
    private ModelMaintainMileageService modelMaintainMileageService;
    @Autowired
    private MaintainItemService maintainItemService;
    @Autowired
    private CarMaintainPlanService carMaintainPlanService;

    //获得当前登录用户信息
    @Autowired
    private MonkeyJdbcRealm monkeyJdbcRealm;


    /*
    *   车型保养管理页面
    * */
    @RequiresRoles(value={"admin","data_admin","maintain_admin"}, logical= Logical.OR)
    @RequestMapping(value = "manage" )
    public ModelAndView manage(){
        ModelAndView modelAndView = new ModelAndView("monkey/carMaintain/manage");

        return modelAndView;
    }

    /*
    *  查询车型保养方案数据列表
    * */
    @RequestMapping(value="toGetModelMaintainPlan", method = RequestMethod.POST)
    @ResponseBody
    public Result toGetModelMaintainPlan(@RequestParam String carBrand, @RequestParam String factoryName,
                                       @RequestParam String carSeries, @RequestParam String vehicleType) throws Exception{

        List<ModelMaintainPlanDO> planList = this.modelMaintainPlanService.findMaintainPlanByModel(carBrand, factoryName, carSeries, vehicleType);

        return Result.wrapSuccessfulResult(planList);
    }

    /*
    *  查询保养方案明细
    * */
    @RequestMapping(value="toGetMaintainPlanDetail", method = RequestMethod.POST)
    @ResponseBody
    public Map toGetMaintainPlanDetail(@RequestParam Integer maintainPlanId) throws Exception{
        Map<String, Object> resultMap = new HashMap<>();
        List<Object> resultList = new ArrayList<>();
        List<ModelMaintainMileageDO> mileageList = this.modelMaintainMileageService.findMaintainMileageByPlanId(maintainPlanId);
        if(mileageList.isEmpty()){
            resultMap.put("success", false);
            resultMap.put("message", "未配置保养里程！");
        }else{
            List<MaintainItemDO> maintainList = this.maintainItemService.findAllMaintainItems();
            if(maintainList.isEmpty()){
                resultMap.put("success", false);
                resultMap.put("message", "没有保养项目！");
            }else{
                List<Object> relationList = new ArrayList<>();
                relationList.add("保养项目（单位）/ 里程");
                for(ModelMaintainMileageDO mileageDO : mileageList){
                    relationList.add(mileageDO.getMileage());
                }
                resultList.add(relationList);

                Map<String, Object> data;
                for(MaintainItemDO maintain : maintainList){
                    relationList = new ArrayList<>();
                    relationList.add(maintain.getName()+"（"+maintain.getUnit()+"）");
                    for(ModelMaintainMileageDO mileage : mileageList){
                        data = new HashMap<>();
                        data.put("modelMileageId", mileage.getId());
                        data.put("maintainId", maintain.getId());
                        data.put("flag", this.modelMaintainPlanService.findMaintainDetail(mileage.getId(), maintain.getId()));
                        data.put("title", maintain.getName()+", "+mileage.getMileage().toString());
                        relationList.add(data);
                    }
                    resultList.add(relationList);
                }

                resultMap.put("success", true);
                resultMap.put("data", resultList);
            }
        }

        return resultMap;
    }

    /*
    *  查询保养方案里程列表
    * */
    @RequestMapping(value="toGetMaintainMileage", method = RequestMethod.POST)
    @ResponseBody
    public List toGetMaintainMileage(@RequestParam Integer maintainPlanId) {

        List<ModelMaintainMileageDO> list = modelMaintainMileageService.findMaintainMileageByPlanId(maintainPlanId);

        return list;
    }

    /*
    *   修改保养方案明细
    * */
    @RequestMapping(value="toEditMaintainPlan", method = RequestMethod.POST)
    @ResponseBody
    public Result toEditMaintainPlan(@RequestParam String jsonString) throws Exception{

        JSONObject jsonObject = JSON.parseObject(jsonString);
        JSONArray addArray = jsonObject.getJSONArray("addArray");
        JSONArray deleteArray = jsonObject.getJSONArray("deleteArray");

        List<MaintainPlanDetail> addList = null;
        if(addArray.size()>0){
            addList = JSON.parseArray(addArray.toString(), MaintainPlanDetail.class);
        }

        List<MaintainPlanDetail> deleteList = null;
        if(deleteArray.size()>0){
            deleteList = JSON.parseArray(deleteArray.toString(), MaintainPlanDetail.class);
        }

        this.modelMaintainPlanService.modifyMaintainPlanDetail(addList, deleteList);

        return Result.wrapSuccessfulResult(null);
    }

    /*
    *  添加保养里程
    * */
    @RequestMapping(value="toAddMaintainMileage", method = RequestMethod.POST)
    @ResponseBody
    public Map toAddMaintainMileage(@RequestParam String jsonString) throws Exception{
        ModelMaintainMileageDO mileageDO = JSON.parseObject(jsonString, ModelMaintainMileageDO.class);
        int count = this.modelMaintainMileageService.saveModelMaintainMileage(mileageDO);

        Map<String, Object> resultMap = new HashMap<>();
        if(count==0){
            resultMap.put("success", false);
            resultMap.put("message", "里程数："+mileageDO.getMileage().toString()+" 已存在！");
        }else{
            resultMap.put("success", true);
            resultMap.put("message", "修改成功！");
        }

        return resultMap;
    }

    /*
    *  批量添加保养里程
    * */
    @RequestMapping(value="toAddMaintainMileageBatch", method = RequestMethod.POST)
    @ResponseBody
    public Result toAddMaintainMileageBatch(@RequestParam String jsonString) throws Exception{
        JSONObject jsonObject = JSON.parseObject(jsonString);
        int mileage = jsonObject.getIntValue("mileage");
        int addStep = jsonObject.getIntValue("addStep");
        int addNum = jsonObject.getIntValue("addNum");
        int maintainPlanId = jsonObject.getIntValue("maintainPlanId");

        this.modelMaintainMileageService.saveModelMaintainMileageBatch(maintainPlanId, mileage, addStep, addNum);

        return Result.wrapSuccessfulResult(null);
    }

    /*
    *  修改保养里程
    * */
    @RequestMapping(value="toModifyMaintainMileage", method = RequestMethod.POST)
    @ResponseBody
    public Map toModifyMaintainMileage(@RequestParam String jsonString) throws Exception{
        ModelMaintainMileageDO mileageDO = JSON.parseObject(jsonString, ModelMaintainMileageDO.class);
        int count = this.modelMaintainMileageService.modifyModelMaintainMileage(mileageDO);
        Map<String, Object> resultMap = new HashMap<>();
        if(count==0){
            resultMap.put("success", false);
            resultMap.put("message", "里程数："+mileageDO.getMileage().toString()+" 已存在！");
        }else{
            resultMap.put("success", true);
            resultMap.put("message", "修改成功！");
        }

        return resultMap;
    }

    /*
    *  删除保养里程
    * */
    @RequestMapping(value="toDeleteMaintainMileage", method = RequestMethod.POST)
    @ResponseBody
    public Result toDeleteMaintainMileage(@RequestParam String jsonString) throws Exception{
        ModelMaintainMileageDO mileageDO = JSON.parseObject(jsonString, ModelMaintainMileageDO.class);
        this.modelMaintainMileageService.removeModelMaintainMileage(mileageDO);

        return Result.wrapSuccessfulResult(null);
    }

    /*
    *  批量删除保养里程
    * */
    @RequestMapping(value="delSomeMile", method = RequestMethod.POST)
    @ResponseBody
    public Result delSomeMile(@RequestBody List<Integer> idList) {
        return Result.wrapSuccessfulResult(modelMaintainMileageService.removeModelMaintainMileage(idList));
    }

    /*
    *  添加保养方案
    * */
    @RequestMapping(value="toAddModelMaintainPlan", method = RequestMethod.POST)
    @ResponseBody
    public Result toAddModelMaintainPlan(@RequestParam String jsonString) throws Exception{
        ModelMaintainPlanDO planDO = JSON.parseObject(jsonString, ModelMaintainPlanDO.class);
        this.modelMaintainPlanService.saveModelMaintainPlan(planDO);

        return Result.wrapSuccessfulResult(null);
    }

    /*
    *  修改车型保养方案，目前就修改个名称而已
    *  逻辑删除也是访问这个方法
    * */
    @RequestMapping(value="toModifyModelMaintainPlan", method = RequestMethod.POST)
    @ResponseBody
    public Result toModifyModelMaintainPlan(@RequestParam String jsonString) throws Exception{
        ModelMaintainPlanDO planDO = JSON.parseObject(jsonString, ModelMaintainPlanDO.class);
        this.modelMaintainPlanService.modifyModelMaintainPlan(planDO);

        return Result.wrapSuccessfulResult(null);
    }

    /*
    *   修改车款保养方案
    * */
    @RequestMapping(value="toModifyCarMaintainPlan", method = RequestMethod.POST)
    @ResponseBody
    public Result toModifyCarMaintainPlan(@RequestParam String jsonString) throws Exception {

        JSONObject jsonObject = JSON.parseObject(jsonString);
        JSONArray addArray = jsonObject.getJSONArray("addArray");
        JSONArray deleteArray = jsonObject.getJSONArray("deleteArray");

        List<CarMaintainPlanDO> addList = null;
        if (addArray.size() > 0) {
            addList = JSON.parseArray(addArray.toString(), CarMaintainPlanDO.class);
        }

        List<CarMaintainPlanDO> deleteList = null;
        if (deleteArray.size() > 0) {
            deleteList = JSON.parseArray(deleteArray.toString(), CarMaintainPlanDO.class);
        }

        this.carMaintainPlanService.modifyCarMaintainPlan(addList, deleteList);

        return Result.wrapSuccessfulResult(null);
    }

    /*
    *  拷贝保养方案
    * */
    @RequestMapping(value="copyModelMaintainPlan", method = RequestMethod.POST)
    @ResponseBody
    public Result copyModelMaintainPlan(ModelMaintainPlanDO plan) throws Exception{
        modelMaintainPlanService.copyModelMaintainPlan(plan);
        return Result.wrapSuccessfulResult(null);
    }

}
