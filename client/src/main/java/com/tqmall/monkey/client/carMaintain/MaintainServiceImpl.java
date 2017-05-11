package com.tqmall.monkey.client.carMaintain;

import com.tqmall.athena.domain.result.maintain.MaintainDetailDTO;
import com.tqmall.athena.domain.result.maintain.MaintainItemDTO;
import com.tqmall.core.common.entity.Result;
import com.tqmall.monkey.component.utils.ResultUtil;
import com.tqmall.monkey.external.dubbo.maintain.MaintainExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by huangzhangting on 16/2/29.
 */
@Service
public class MaintainServiceImpl implements MaintainService {

    @Autowired
    private MaintainExt maintainExt;


    private Result<Integer> findNextMile(Integer mile, Set<Integer> mileSet) {
        boolean exit = mileSet.contains(mile);
        if(exit){
            return Result.wrapSuccessfulResult(mile);
        }
        List<Integer> mileList = new ArrayList<>(mileSet);
        if(!exit){
            mileList.add(mile);
        }

        Collections.sort(mileList);
        int idx = mileList.indexOf(mile);
        if(idx==(mileList.size()-1) && !exit){
            Integer maxMile = mileList.get(idx - 1);
            return ResultUtil.errorResult(maxMile, "当前车款保养里程表只提供0至" + maxMile + "公里的保养数据，请您见谅");
        }

        return Result.wrapSuccessfulResult(mileList.get(idx+1));
    }

    private boolean handleCommonItem(MaintainItemDTO item, Integer mileage){
        Integer perMileage = item.getIntervalMileage();
        if(perMileage==0){ // 保养里程间隔
            return true;
        }
        Integer firstMileage = item.getFirstMileage();
        if(firstMileage.equals(mileage)){ // 首次保养里程
            return true;
        }
        if(mileage<firstMileage){
            return false;
        }
        if(mileage%perMileage==0){
            return true;
        }
        return false;
    }

    @Override
    public Result getMaintainDetail(Integer carId, Integer mileage) {
        if(mileage==null){
            mileage = 0;
        }
        if(mileage<0){
            return Result.wrapErrorResult("", "输入里程不能为负数");
        }

        Result<List<MaintainDetailDTO>> result = maintainExt.maintainDetailCommon(carId, null);
        if(!result.isSuccess()){
            return Result.wrapErrorResult(result.getCode(), result.getMessage());
        }
        List<MaintainDetailDTO> detailDTOList = result.getData();
        if(CollectionUtils.isEmpty(detailDTOList)){
            return Result.wrapErrorResult("", "抱歉，您所搜索的车款暂无保养项目，请您见谅");
        }
        // 组装里程数
        Map<Integer, List<Integer>> mileageMap = new TreeMap<>();
        for(MaintainDetailDTO detailDTO : detailDTOList){
            List<Integer> list = mileageMap.get(detailDTO.getMile());
            if(list==null){
                list = new ArrayList<>();
                mileageMap.put(detailDTO.getMile(), list);
            }
            list.add(detailDTO.getServiceId());
        }

        // 判断里程
        Result<Integer> nextMileResult = findNextMile(mileage, mileageMap.keySet());
        if(!nextMileResult.isSuccess()){
            return ResultUtil.errorResult(nextMileResult.getData(), nextMileResult.getMessage());
        }
        Integer nextMile = nextMileResult.getData();

        // 处理保养项目
        List<MaintainItemDTO> allItemList = maintainExt.allMaintainItems();
        List<MaintainItemDTO> itemList = new ArrayList<>();
        List<MaintainItemDTO> commonItemList = new ArrayList<>();
        for(MaintainItemDTO itemDTO : allItemList){
            if(itemDTO.getIsCommon()){
                if(handleCommonItem(itemDTO, nextMile)){
                    commonItemList.add(itemDTO);
                }
            }else{
                itemList.add(itemDTO);
            }
        }

        // 组装数据表格
        List<Object> resultList = new ArrayList<>();
        List<Object> relationList = new ArrayList<>();
        relationList.add("保养项目 / 里程");
        relationList.addAll(mileageMap.keySet());
        // 下次保养里程下标，页面展示使用到
        int nextMileIdx = relationList.indexOf(nextMile);
        resultList.add(relationList);

        for(MaintainItemDTO item : itemList){
            relationList = new ArrayList<>();
            relationList.add(item.getName());
            for(Map.Entry<Integer, List<Integer>> entry : mileageMap.entrySet()){
                Map<String, Object> data = new HashMap<>();
                data.put("flag", entry.getValue().contains(item.getId()));
                data.put("title", item.getName()+", "+entry.getKey());
                relationList.add(data);
            }
            resultList.add(relationList);
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("maintainDetail", resultList);
        resultMap.put("commonItemList", commonItemList);
        resultMap.put("nextMile", nextMile);
        resultMap.put("nextMileIdx", nextMileIdx);
        return Result.wrapSuccessfulResult(resultMap);
    }
}
