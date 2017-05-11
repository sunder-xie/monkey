package com.tqmall.monkey.web.part;

import com.google.common.collect.ImmutableMap;
import com.tqmall.core.common.entity.Result;
import com.tqmall.monkey.client.car.CarInfoAllService;
import com.tqmall.monkey.client.category.CategoryService;
import com.tqmall.monkey.client.part.PartGoodsService;
import com.tqmall.monkey.client.part.PartLiyangService;
import com.tqmall.monkey.client.part.PartRelationService;
import com.tqmall.monkey.client.redisManager.part.PartGoodsRedisManager;
import com.tqmall.monkey.client.redisManager.part.PartLiyangRedisManager;
import com.tqmall.monkey.client.shiro.MonkeyJdbcRealm;
import com.tqmall.monkey.domain.bizBO.part.PartGoodsShowBO;
import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.UserDO;
import com.tqmall.monkey.domain.entity.car.CarInfoAllDO;
import com.tqmall.monkey.domain.entity.part.PartLiyangDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * 配件库数据的展示
 * Created by zxg on 15/7/28.
 */
@Controller
@Transactional
@RequestMapping("/monkey/part/dataShow")
public class PartShowController {

    //获得当前登录用户信息
    @Autowired
    private MonkeyJdbcRealm monkeyJdbcRealm;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PartRelationService partRelationService;

    @Autowired
    private PartGoodsService partGoodsService;

    @Autowired
    private PartLiyangService partLiyangService;
    @Autowired
    private PartGoodsRedisManager partGoodsRedisManager;

    //力洋库的车型数据获得
    @Autowired
    private CarInfoAllService carInfoAllService;


    //供应商数据导入页面
    @RequestMapping(value = "index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("monkey/part/partDataShow");
        UserDO userDO = monkeyJdbcRealm.getCurrentUser();

        if (userDO == null) {
            return new ModelAndView("monkey/login");
        }

        return modelAndView;
    }

    //获得该配件适配的所有力洋车型
    @RequestMapping(value = "/searchLiyangCar", method = RequestMethod.GET)
    public
    @ResponseBody
    Result<HashMap<String, Object>> searchLiyangCar(Integer index, Integer pageSize, String goodsId, String picId,Integer partLId,String brandName) {
        HashMap<String, Object> resultMap = new HashMap<>();

        Page<String> liyangIdPage = partRelationService.selectLiyangIdPageByGoodsIdPicId(index, pageSize, goodsId, picId,partLId,brandName);

        if (liyangIdPage == null) {
            resultMap.put("resultDataList", Collections.emptyList());
            resultMap.put("totalNumber", 0);
            resultMap.put("totalPages", 0);
        } else {
            List<CarInfoAllDO> liyangList = new ArrayList<>();
            for (String liyangId : liyangIdPage.getItems()) {
                CarInfoAllDO carInfoAllDO = carInfoAllService.findCarInfoByLeyelId(liyangId);
                liyangList.add(carInfoAllDO);
            }

            resultMap.put("resultDataList", liyangList);
            resultMap.put("totalNumber", liyangIdPage.getTotalNumber());
            resultMap.put("totalPages", liyangIdPage.getTotalPage());
        }

        return Result.wrapSuccessfulResult(resultMap);

    }

    //删除车型下的所有关联信息--删除relation表中关系 即可
    @RequestMapping(value = "/deleteAll", method = RequestMethod.GET)
    public
    @ResponseBody
    Result<String> deleteAll(Integer searchId, String searchBrand, String searchFactory, String searchSeries, String searchModel) {

        try {
            //删除 此车型于 redis和数据库中
            partLiyangService.deletePartLiyang(searchId, searchBrand, searchFactory, searchSeries, searchModel);
            // 删除此 车下的所有配件关系
            partRelationService.deleteByPartLId(searchId,searchBrand);


            return Result.wrapSuccessfulResult("success");
        } catch (Exception e) {
            return Result.wrapErrorResult("001", e.getMessage());
        }
    }

    //根据传入的车型数据获得其展示的所有
    @RequestMapping(value = "/getDataFromCar", method = RequestMethod.GET)
    public
    @ResponseBody
    Result<Map<String, Object>> getDataFromCar(Integer partLId,String brandName){
        if(partLId == null){
            return null;
        }
        Map<String, Object> resultMap = new HashMap<>();
        List<PartGoodsShowBO> partGoodsShowBOList = partGoodsRedisManager.getGoodsByCar(partLId,brandName);

        Set<String> goodsUuidSet = new HashSet<>();

        TreeSet<String> finalLiyangShowSet = new TreeSet<>();
        //分类map
        Map<Object,Object> categoryMap = new HashMap<>();
        for(PartGoodsShowBO partGoodsShowBO : partGoodsShowBOList){
            List<String> liyangShowList = partGoodsShowBO.getLiyangShowList();
            finalLiyangShowSet.addAll(liyangShowList);
            partGoodsShowBO.setLiyangShowList(new ArrayList<String>());

            goodsUuidSet.add(partGoodsShowBO.getGoodsId());
            // 生成分类map
            Map<String,String> firstCat = ImmutableMap.of("catName", partGoodsShowBO.getFirstCateName(), "catCode", partGoodsShowBO.getFirstCateCode());
            Map<String,String> secondCat = ImmutableMap.of("catName",partGoodsShowBO.getSecondCateName(),"catCode",partGoodsShowBO.getSecondCateCode());
            Map<String,String> thirdCat = ImmutableMap.of("catName",partGoodsShowBO.getThirdCateName(),"catCode",partGoodsShowBO.getThirdCateCode());
            Map<String,String> partCat = ImmutableMap.of("catName",partGoodsShowBO.getPartName(),"catCode",partGoodsShowBO.getPartCode(),"vehicleCode",partGoodsShowBO.getVehicleCode());

            Map<Object, Object> secondCatMap = (Map<Object, Object>) categoryMap.get(firstCat);
            if (null == secondCatMap) {
                secondCatMap = new HashMap<>();
                categoryMap.put(firstCat, secondCatMap);
            }
            Map<Object, Object> thirdCatMap = (Map<Object, Object>) secondCatMap.get(secondCat);
            if (null == thirdCatMap) {
                thirdCatMap = new HashMap<>();
                secondCatMap.put(secondCat, thirdCatMap);
            }
            Set<Object> partSet = (Set<Object>) thirdCatMap.get(thirdCat);
            if (null == partSet) partSet = new HashSet<>();
            partSet.add(partCat);
            thirdCatMap.put(thirdCat, partSet);


        }
        //真实数据排序
        Collections.sort(partGoodsShowBOList, new Comparator<PartGoodsShowBO>() {
            @Override
            public int compare(PartGoodsShowBO o1, PartGoodsShowBO o2) {
                Integer result = o1.getPartCode().compareToIgnoreCase(o2.getPartCode());
                return result;
            }
        });

        //力洋车型筛选map
        Map<String,Object> liyangMap = getLiYangShowMap(finalLiyangShowSet);

        resultMap.put("categoryMap",categoryMap);
        resultMap.put("liyangMap",liyangMap);
        resultMap.put("partGoodsShowBOList",partGoodsShowBOList);
        resultMap.put("goodsSum",goodsUuidSet.size());
        System.out.println(new Date());
        return Result.wrapSuccessfulResult(resultMap);
    }


    //根据传入的车型数据{brand:factory:series:model:id}
    @RequestMapping(value = "/getCar", method = RequestMethod.GET)
    public
    @ResponseBody
    Result<Map<String, Object>> getCar() {
        try {
            Map<String,Object> brandMap = new HashMap<>();

            List<PartLiyangDO> relationCarList = partLiyangService.getPartCar();
            for(PartLiyangDO partLiyangDO : relationCarList){
                Integer partLId = partLiyangDO.getId();
                String brandName = partLiyangDO.getLiyangBrand();
                String factoryName = partLiyangDO.getLiyangFactory();
                String seriesName = partLiyangDO.getLiyangSeries();
                String modelName = partLiyangDO.getLiyangModel();

                Map<String, Object> factoryMap = (Map<String, Object>) brandMap.get(brandName);
                if(null == factoryMap){
                    factoryMap = new HashMap<>();
                    brandMap.put(brandName,factoryMap);
                }
                Map<String, Object> seriesMap = (Map<String, Object>) factoryMap.get(factoryName);
                if(null == seriesMap){
                    seriesMap = new HashMap<>();
                    factoryMap.put(factoryName,seriesMap);
                }

                Map<String, Integer> modelMap = (Map<String, Integer>) seriesMap.get(seriesName);
                if(null == modelMap){
                    modelMap = new HashMap<>();
                    seriesMap.put(seriesName,modelMap);
                }
                modelMap.put(modelName,partLId);
            }

            return Result.wrapSuccessfulResult(brandMap);

        } catch (Exception e) {
            return Result.wrapErrorResult("001", e.getMessage());
        }

    }


    /*============private=========================*/
    //获得当前车型下的展示车型
    private Map<String,Object> getLiYangShowMap(TreeSet<String> finalLiyangShowSet){
        Map<String,Object> resultMap = new HashMap<>();
        //排量去重
        Set<String> displayYearSet = new HashSet<>();
        Set<String> transSet = new HashSet<>();

        List<Map<String, String>> AllliyangList = new ArrayList<>();

        for (String result : finalLiyangShowSet) {
            String[] array = result.split("-");
            displayYearSet.add(array[2] + "-" + array[3]);
            transSet.add(array[4]);

            Map<String, String> liyangIDMap = new HashMap<>();
            liyangIDMap.put("id", array[0]);
            liyangIDMap.put("name", array[1]);

            //list有序去重
            if (Collections.frequency(AllliyangList, liyangIDMap) < 1) {
                AllliyangList.add(liyangIDMap);
            }
        }

        resultMap.put("allList", finalLiyangShowSet);
        resultMap.put("displayYearSet", displayYearSet);
        resultMap.put("transSet", transSet);
        resultMap.put("AllliyangList", AllliyangList);

        return resultMap;
    }

    //获取 db_monkey_part_goods_base 表中除了含有"-" 的oe码的数量
    @RequestMapping(value = "/getPartGoodsBaseOeNum", method = RequestMethod.GET)
    public @ResponseBody Result<Integer> getPartGoodsBaseOeNum() {
        try {
            return Result.wrapSuccessfulResult(partGoodsService.getPartGoodsBaseOeNum());
        } catch (Exception e) {
            return Result.wrapErrorResult("002", e.getMessage());
        }

    }
}
