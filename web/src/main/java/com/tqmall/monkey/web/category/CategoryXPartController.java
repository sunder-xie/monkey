package com.tqmall.monkey.web.category;

import com.alibaba.fastjson.JSON;
import com.tqmall.core.common.entity.Result;
import com.tqmall.monkey.client.category.CategoryPartService;
import com.tqmall.monkey.client.category.CategoryService;
import com.tqmall.monkey.client.category.CategoryXPartService;
import com.tqmall.monkey.client.commodity.CommodityGoodsService;
import com.tqmall.monkey.client.offerGoods.OfferGoodsService;
import com.tqmall.monkey.client.part.PartGoodsService;
import com.tqmall.monkey.client.shiro.MonkeyJdbcRealm;
import com.tqmall.monkey.component.utils.PoiUtil;
import com.tqmall.monkey.component.utils.StringUtil;
import com.tqmall.monkey.domain.entity.UserDO;
import com.tqmall.monkey.domain.entity.category.CategoryDO;
import com.tqmall.monkey.domain.entity.category.CategoryPartDO;
import com.tqmall.monkey.domain.entity.commodity.CommodityGoodsDO;
import com.tqmall.monkey.domain.entity.offerGoods.OfferGoodsDO;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分类+part  控制器
 * Created by lyj on 2015/12/16.
 */
@Controller
@RequestMapping("/monkey/categoryXPart")
public class CategoryXPartController {
    private int PAGE_SIZE = 30;

    @Autowired
    private CategoryXPartService categoryXPartService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryPartService categoryPartService;
    @Autowired
    private CommodityGoodsService commodityGoodsService;
    @Autowired
    private OfferGoodsService offerGoodsService;
    @Autowired
    private PartGoodsService partGoodsService;
    @Autowired
    private MonkeyJdbcRealm monkeyJdbcRealm;//获得当前登录用户信息

    //首页--权限控制
    @RequiresRoles(value = {"data_operator", "data_admin"}, logical = Logical.OR)
    @RequestMapping(value = "index")
    public ModelAndView index() throws Exception {
        ModelAndView modelAndView = new ModelAndView("monkey/category/indexXPart");
        UserDO User = (UserDO) monkeyJdbcRealm.getSessionValue("currentUser");
        if (User == null) {
            return null;
        }
        return modelAndView;
    }

    @RequestMapping(value = "/searchCategoryXPart", method = RequestMethod.POST)
    @ResponseBody
    public Map searchCategoryXPart(@RequestParam String pageIndex, @RequestParam String vehicleCode,
                                   @RequestParam Integer levelF, @RequestParam Integer levelS, @RequestParam Integer levelT,
                                   @RequestParam String partName) throws Exception {
        int pageIndexInt = -1;
        if (StringUtil.isNum(pageIndex)) {
            pageIndexInt = Integer.parseInt(pageIndex);
        }
        if (pageIndexInt < 1) {
            pageIndexInt = 1;
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("pageSize", PAGE_SIZE);
        paramMap.put("startNum", (pageIndexInt - 1) * PAGE_SIZE);
        paramMap.put("vehicleCode", vehicleCode);
        paramMap.put("levelF", levelF);
        paramMap.put("levelS", levelS);
        paramMap.put("levelT", levelT);
        paramMap.put("partName", partName);

        //获取所有通过拼接得到的分类record
        List<Map<String, Object>> dataList = categoryXPartService.getCategoryXPart(paramMap);
        int totalRows = categoryXPartService.getCategoryXPartCount(paramMap);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("data", dataList);
        resultMap.put("totalRows", totalRows);
        resultMap.put("totalPages", totalRows % PAGE_SIZE == 0 ? totalRows / PAGE_SIZE : totalRows / PAGE_SIZE + 1);
        return resultMap;
    }

    @RequestMapping(value = "/getVehicleCode", method = RequestMethod.POST)
    @ResponseBody
    public Map getVehicleCode() throws Exception {
        List<String> vehicleCodeList = this.categoryXPartService.getVehicleCode();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("vehicleCodeList", vehicleCodeList);
        return resultMap;
    }

    @RequestMapping(value = "/getCategoryAttrToUseInSelect", method = RequestMethod.POST)
    @ResponseBody
    public Map getCategoryAttrToUseInSelect(Integer level, String vehicleCode, Integer parentId) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("level", level);
        paramMap.put("vehicleCode", vehicleCode);
        paramMap.put("parentId", parentId);
        List<Map> categoryAttrList = this.categoryXPartService.getCategoryAttrToUseInSelect(paramMap);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("categoryAttrList", categoryAttrList);
        return resultMap;
    }

    @RequestMapping(value = "/toExportExcel")
    public void toExportExcel(HttpServletResponse response, @RequestParam String vehicleCode, @RequestParam Integer levelF, @RequestParam Integer levelS, @RequestParam Integer levelT, @RequestParam String partName) throws Exception {
        String version = categoryXPartService.getCatXPartExportExcelVersion();
        String excelTitle = "标准分类V"+version;
        String[] headName = {"车辆种类码", "一级分类", "一级编码", "二级分类", "二级编码", "三级分类", "三级编码", "零件名称", "位置码", "零件编码", "标签", "零件别名", "类型"};
        String[] fieldName = {"vehicleCode", "catNameF", "catCodeF", "catNameS", "catCodeS", "catNameT", "catCodeT", "partName", "partCode", "sumCode", "labelNameText", "alissNameText", "catKind"};
        int[] columnWith = {2000, 3500, 5000, 3000, 5000, 3000, 5000, 3000, 6000, 2500, 4000, 7000, 8000, 4000};
        int freezeCol = 1;
        int freezeRow = 2;

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("vehicleCode", vehicleCode);
        paramMap.put("levelF", levelF);
        paramMap.put("levelS", levelS);
        paramMap.put("levelT", levelT);
        paramMap.put("partName", partName);
        List<Map<String, Object>> dataList = categoryXPartService.getCategoryXPart(paramMap);
        PoiUtil poiUtil = new PoiUtil();
        poiUtil.exportExcelByHashForCXR(response, excelTitle, headName, fieldName, dataList, columnWith, true, freezeCol, freezeRow);
    }

    //Start Modify----------------------------------------------------------------//
    @RequiresRoles(value = {"data_admin"})
    @RequestMapping(value = "modify")
    public ModelAndView modify() {
        ModelAndView modelAndView = new ModelAndView("monkey/category/modifyXPart");
        UserDO User = (UserDO) monkeyJdbcRealm.getSessionValue("currentUser");
        if (User == null) {
            return null;
        }
        return modelAndView;
    }

    @RequestMapping(value = "/getPartAttrToUseInTable", method = RequestMethod.POST)
    @ResponseBody
    public Map getPartAttrToUseInTable(Integer thirdCatId) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("thirdCatId", thirdCatId);
        List<Map> partAttrList = this.categoryXPartService.getPartAttrToUseInTable(paramMap);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("partAttrList", partAttrList);
        return resultMap;
    }

    @RequestMapping(value = "/toAddCategoryPart", method = RequestMethod.POST)
    @ResponseBody
    public Result toAddCategoryPart(@RequestBody CategoryPartDO categoryPart) throws Exception {
        Result result = new Result();
        //装填对象
        CategoryDO firstCat = this.categoryService.findCategoryById(categoryPart.getFirstCatId());
        categoryPart.setFirstCatName(firstCat.getCatName());
        CategoryDO secondCat = this.categoryService.findCategoryById(categoryPart.getSecondCatId());
        categoryPart.setSecondCatName(secondCat.getCatName());
        CategoryDO thirdCat = this.categoryService.findCategoryById(categoryPart.getThirdCatId());
        categoryPart.setThirdCatName(thirdCat.getCatName());

        categoryPart.setIsDeleted("N");

        categoryPartService.saveCategoryPart(categoryPart);

        //更新上层的VehicleCode
        String partVc = categoryPart.getVehicleCode();
        String firstVc = firstCat.getVehicleCode();
        String secondVc = secondCat.getVehicleCode();
        String thirdVc = thirdCat.getVehicleCode();
        //更新3J的VehicleCode
        if (!partVc.equalsIgnoreCase(thirdVc)) {//不相同会涉及到修改3J的VehicleCode
            if (!"CH".equalsIgnoreCase(thirdVc)) {//非CH的才会修改
                if (this.categoryPartService.getVehicleCodeCount(categoryPart.getThirdCatId()) > 1) {//C H CH 中至少有两个，那么修改3J目录为CH
                    thirdCat.setVehicleCode("CH");
                } else {
                    thirdCat.setVehicleCode(partVc);
                }
                this.categoryService.modifyCategoryDO(thirdCat);

                //更新2J的VehicleCode
                thirdVc = thirdCat.getVehicleCode();
                if (!thirdVc.equalsIgnoreCase(secondVc)) {
                    if (!"CH".equalsIgnoreCase(secondVc)) {
                        if (this.categoryService.getVehicleCodeCount(secondCat.getCatId()) > 1) {//C H CH 中至少有两个，那么修改2J目录为CH
                            secondCat.setVehicleCode("CH");
                        } else {
                            secondCat.setVehicleCode(thirdVc);
                        }
                        this.categoryService.modifyCategoryDO(secondCat);

                        //更新1J的VehicleCode
                        secondVc = secondCat.getVehicleCode();
                        if (!secondVc.equalsIgnoreCase(firstVc)) {
                            if (!"CH".equalsIgnoreCase(firstVc)) {
                                if (this.categoryService.getVehicleCodeCount(firstCat.getCatId()) > 1) {//C H CH 中至少有两个，那么修改1J目录为CH
                                    firstCat.setVehicleCode("CH");
                                } else {
                                    firstCat.setVehicleCode(secondVc);
                                }
                                this.categoryService.modifyCategoryDO(firstCat);

                            }//end:if(!"CH".equalsIgnoreCase(secondVc)){
                        }

                    }//end:if(!"CH".equalsIgnoreCase(secondVc)){
                }

            }//end:if (!"CH".equalsIgnoreCase(thirdVc)) {//非CH的才会修改
        }

        //修改3J的is_leaf字段，不是叶子节点
        thirdCat.setIsLeaf(CategoryDO.IS_LEAF_FALSE);
        this.categoryService.modifyCategoryDO(thirdCat);

        result.setSuccess(true);
        result.setMessage("零件添加成功，所有上级的车辆种类码更新完成！");
        return result;
    }

    @RequestMapping(value = "/toAddCategory", method = RequestMethod.POST)
    @ResponseBody
    public Result toAddCategory(@RequestParam String cateJsonString) throws Exception {
        CategoryDO category = JSON.parseObject(cateJsonString, CategoryDO.class);
        List<CategoryDO> checkCategoryList = this.categoryService.findCategoryForCheck(null, category.getCatCode(), category.getCatLevel(), category.getParentId());
        StringBuffer msg = new StringBuffer();
        boolean checkFlag = true;

        if (checkCategoryList != null && !checkCategoryList.isEmpty()) {
            checkFlag = false;
            if (checkCategoryList.size() == 1) {
                CategoryDO checkCategory = checkCategoryList.get(0);
                if (!"N".equals(checkCategory.getIsDeleted())) {
                    msg.append("存在【已停用】的重复编码的分类");
                } else {
                    msg.append("存在重复编码的分类");
                }
            } else {
                msg.append("存在多个重复编码的分类");
            }
        }

        if (checkFlag) {
            category.setVehicleCode(CategoryDO.VEHICLE_CODE_NAN);//给一个默认值，新增的1 2 3 J类目，必定是没有part的，这部分种类码设为NaN
            category.setIsLeaf(CategoryDO.IS_LEAF_TRUE);
            this.categoryService.saveCategoryDO(category);

            //设置他的父级的is_leaf字段为0，不是叶子节点
            Integer parentId = category.getParentId();
            CategoryDO parentCat = this.categoryService.findCategoryById(parentId == null ? Integer.valueOf(-1) : parentId);
            if (parentCat != null) {
                parentCat.setIsLeaf(CategoryDO.IS_LEAF_FALSE);
                this.categoryService.modifyCategoryDO(parentCat);
            }
            return Result.wrapSuccessfulResult(null);
        } else {
            return Result.wrapErrorResult(null, msg.toString());
        }
    }

    @RequestMapping(value = "/toStopUseCategory", method = RequestMethod.POST)
    @ResponseBody
    public Result toStopUseCategory(@RequestParam Integer cateId, @RequestParam Integer level) throws Exception {
        this.categoryService.stopCategoryById(cateId, level);
        return Result.wrapSuccessfulResult(null);
    }

    @RequestMapping(value = "/getCategoryAndUpperCat", method = RequestMethod.POST)
    @ResponseBody
    public Map toGetCategoryById(Integer catId, Integer level) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        CategoryDO category = this.categoryService.findCategoryById(catId);
        List<Map> parentCategoryList = null;

        Map<String, Object> paramMap = new HashMap<>();

        switch (level == null ? 0 : level) {
            case 2:
                paramMap.put("level", 1);
                parentCategoryList = this.categoryXPartService.getCategoryAttrToUseInSelect(paramMap);
                break;
            case 3:
                CategoryDO temp = this.categoryService.findCategoryById(category.getParentId());
                paramMap.put("level", 2);
                if (temp != null) {
                    paramMap.put("parentId", temp.getParentId());
                }
                parentCategoryList = this.categoryXPartService.getCategoryAttrToUseInSelect(paramMap);
                break;
            default:
                break;
        }

        resultMap.put("data", category);
        if (parentCategoryList != null) {
            resultMap.put("parentCategoryList", parentCategoryList);
        }
        return resultMap;
    }

    @RequestMapping(value = "/toModifyCategory", method = RequestMethod.POST)
    @ResponseBody
    public Result toModifyCategory(@RequestParam String catJsonString) throws Exception {
        CategoryDO category = JSON.parseObject(catJsonString, CategoryDO.class);

        List<CategoryDO> checkCategoryList = this.categoryService.findCategoryForCheck(null, category.getCatCode(), category.getCatLevel(), category.getParentId());
        StringBuffer msg = new StringBuffer();
        boolean checkFlag = true;

        if (checkCategoryList != null && !checkCategoryList.isEmpty()) {
            if (checkCategoryList.size() == 1) {
                CategoryDO checkCategory = checkCategoryList.get(0);
                if (!checkCategory.getCatId().equals(category.getCatId())) {
                    checkFlag = false;
                    if (!"N".equals(checkCategory.getIsDeleted())) {
                        msg.append("存在【已停用】的重复编码的分类");
                    } else {
                        msg.append("存在重复编码的分类");
                    }
                }
            } else {
                checkFlag = false;
                msg.append("存在多个重复编码的分类");
            }
        }

        if (checkFlag) {
            this.categoryService.modifyCategoryDOBusiness(category);
            return Result.wrapSuccessfulResult(null);
        } else {
            return Result.wrapErrorResult(null, msg.toString());
        }
    }

    @RequestMapping(value = "/toGetCategoryPart", method = RequestMethod.POST)
    @ResponseBody
    public Map toGetCategoryPart(@RequestParam Integer partId) throws Exception {
        CategoryPartDO partDO = this.categoryPartService.findCategoryPartById(partId);
        List<CategoryPartDO> partList = this.categoryPartService.findCategoryPartByThirdCatId(partDO.getThirdCatId());
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("level", 1);
        List<Map> categoryAttrListFirst = this.categoryXPartService.getCategoryAttrToUseInSelect(paramMap);

        paramMap.put("level", 2);
        paramMap.put("parentId", partDO.getFirstCatId());
        List<Map> categoryAttrListSecond = this.categoryXPartService.getCategoryAttrToUseInSelect(paramMap);

        paramMap.put("level", 3);
        paramMap.put("parentId", partDO.getSecondCatId());
        List<Map> categoryAttrListThird = this.categoryXPartService.getCategoryAttrToUseInSelect(paramMap);


        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("data", partDO);
        resultMap.put("categoryAttrListFirst", categoryAttrListFirst);
        resultMap.put("categoryAttrListSecond", categoryAttrListSecond);
        resultMap.put("categoryAttrListThird", categoryAttrListThird);
        resultMap.put("partList", partList);
        return resultMap;
    }

    @RequestMapping(value = "/toModifyCategoryPart", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public Result toModifyCategoryPart(@RequestBody CategoryPartDO categoryPart) throws Exception {
        Integer id = categoryPart.getId();
        CategoryPartDO oldCategoryPart = categoryPartService.findCategoryPartById(id);
        Result result = new Result();
        int flag = categoryPartService.modifyCategoryPartBusiness(oldCategoryPart, categoryPart);
        if (flag != 1) {
            result.setSuccess(false);
            result.setMessage("零件修改失败(留意位置码不能重复)！");
            return result;
        } else {
            if (!comparePartInAttrs(oldCategoryPart, categoryPart)) {
                commodityGoodsService.updateByPart(id, id);
                offerGoodsService.updateByPart(id, id);
                partGoodsService.updateByPart(oldCategoryPart.getSumCode(), id);
            }
        }
        result.setSuccess(true);
        result.setMessage("零件修改成功！");
        return result;
    }

    @RequestMapping(value = "/toStopUsePart", method = RequestMethod.POST)
    @ResponseBody
    public Result toStopUsePart(@RequestParam Integer partId) throws Exception {
        this.categoryPartService.stopUsePart(partId);
        return Result.wrapSuccessfulResult(null);
    }

    //Start goods----------------------------------------------------------------//
    @RequestMapping(value = "/testCG", method = RequestMethod.POST)
    @ResponseBody
    public List<CommodityGoodsDO> testCG(@RequestParam Integer partId) throws Exception {
        List<CommodityGoodsDO> commodityGoodsDOs = commodityGoodsService.getGoodsByBrandPart(null, partId);
        return commodityGoodsDOs;
    }

    @RequestMapping(value = "/testFG", method = RequestMethod.POST)
    @ResponseBody
    public List<OfferGoodsDO> testFG(@RequestParam Integer partId) throws Exception {
        List<OfferGoodsDO> tempList = offerGoodsService.findGoodsByPartId(partId);
        return tempList;
    }

    /*@RequestMapping(value = "/testFG", method = RequestMethod.POST)
    @ResponseBody
    public List<OfferGoodsDO> testFG(@RequestParam Integer partId) throws Exception {
        List<BasePartGoodDO>  tempList = partGoodsService.existBaseGoods(BasePartGoodsParams basePartGoodsParams);
        return tempList;
    }*/

    ////

    /**
     * 比较的属性只要有一个不相同的就返回false
     *
     * @param part1
     * @param part2
     * @return
     */
    private boolean comparePartInAttrs(CategoryPartDO part1, CategoryPartDO part2) {
        if (!part1.getThirdCatId().equals(part2.getThirdCatId())) {
            return false;
        }
        if (!part1.getPartName().equals(part2.getPartName())) {
            return false;
        }
        if (!part1.getPartCode().equals(part2.getPartCode())) {
            return false;
        }
        return true;
    }

    //Start Modify----------------------------------------------------------------//
    @RequiresRoles(value = {"data_admin"})
    @RequestMapping(value = "reuse")
    public ModelAndView reuse() {
        ModelAndView modelAndView = new ModelAndView("monkey/category/reuseXPart");
        UserDO User = (UserDO) monkeyJdbcRealm.getSessionValue("currentUser");
        if (User == null) {
            return null;
        }
        return modelAndView;
    }

    @RequestMapping(value = "/getStoppedCategory", method = RequestMethod.POST)
    @ResponseBody
    public List<CategoryDO> getStoppedCategory(@RequestParam Integer level, @RequestParam Integer parentId) throws Exception {
        return this.categoryService.getStoppedCategory(level, parentId);
    }

    @RequestMapping(value = "/getStoppedCategoryPart", method = RequestMethod.POST)
    @ResponseBody
    public List<CategoryDO> getStoppedCategoryPart(@RequestParam Integer firstCatId, @RequestParam Integer secondCatId, @RequestParam Integer thirdCatId, @RequestParam String partName) throws
            Exception {
        return this.categoryPartService.getStoppedCategoryPart(firstCatId, secondCatId, thirdCatId, partName);
    }

    @RequestMapping(value = "/reuseCategory", method = RequestMethod.POST)
    @ResponseBody
    public Result reuseCategory(@RequestParam Integer catId) throws Exception {
        Result result = new Result();
        CategoryDO temp = categoryService.findDeletedCategoryById(catId);
        if (temp == null) {
            result.setSuccess(false);
            result.setMessage("没有该分类");
        } else {
            this.categoryService.reuseCategory(temp);
            result.setSuccess(true);
            result.setMessage("分类复用成功");
        }
        return result;
    }

    @RequestMapping(value = "/reuseCategoryPart", method = RequestMethod.POST)
    @ResponseBody
    public Result reuseCategoryPart(@RequestParam Integer id) throws Exception {
        Result result = new Result();
        CategoryPartDO temp = categoryPartService.findCategoryPartById(id);
        if (temp == null) {
            result.setSuccess(false);
            result.setMessage("没有该标准零件");
        } else {
            this.categoryPartService.reuseCategoryPart(temp);
            result.setSuccess(true);
            result.setMessage("标准零件复用成功");
        }
        return result;
    }

    //更新导出excel的版本号
    @RequestMapping(value = "/refreshCatXPartExportExcelVersion", method = RequestMethod.POST)
    @ResponseBody
    public Result refreshCatXPartExportExcelVersion() throws Exception {
        String version = categoryXPartService.refreshCatXPartExportExcelVersion();
        Result result = new Result();
        result.setData(version);
        result.setCode("0");
        result.setMessage("分类导出excel的版本号,更新成功!");
        result.setSuccess(true);
        return result;
    }

    //获取导出excel的版本号
    @RequestMapping(value = "/getCatXPartExportExcelVersion", method = RequestMethod.POST)
    @ResponseBody
    public Result getCatXPartExportExcelVersion() throws Exception {
        String version = categoryXPartService.getCatXPartExportExcelVersion();
        Result result = new Result();
        result.setData(version);
        result.setCode("0");
        result.setMessage(version);
        result.setSuccess(true);
        return result;
    }

    //删除导出excel的版本号
    @RequestMapping(value = "/deleteCatXPartExportExcelVersion", method = RequestMethod.POST)
    @ResponseBody
    public Result deleteCatXPartExportExcelVersion() throws Exception {
        categoryXPartService.deleteCatXPartExportExcelVersion();
        Result result = new Result();
        result.setCode("0");
        result.setMessage("分类导出excel的版本号,删除成功!");
        result.setSuccess(true);
        return result;
    }

}
