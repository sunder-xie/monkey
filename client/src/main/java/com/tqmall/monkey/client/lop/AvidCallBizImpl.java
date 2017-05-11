package com.tqmall.monkey.client.lop;

import com.tqmall.core.common.entity.Result;
import com.tqmall.data.epc.client.bean.dto.avid.AvidCallDTO;
import com.tqmall.data.epc.client.bean.param.avid.CancelAvidCallParam;
import com.tqmall.monkey.component.utils.BdUtil;
import com.tqmall.monkey.domain.bizBO.avid.AvidListShowBO;
import com.tqmall.monkey.domain.bizBO.avid.AvidSearchBO;
import com.tqmall.monkey.client.shiro.MonkeyJdbcRealm;
import com.tqmall.monkey.domain.bizBO.avid.ModifyAvidCallBO;
import com.tqmall.monkey.domain.bizBO.lop.WishListRequestBO;
import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.UserDO;
import com.tqmall.monkey.external.dubbo.lop.AvidCallServiceExt;
import com.tqmall.search.dubbo.client.cloudepc.result.EpcAvidCallDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by huangzhangting on 16/10/30.
 */
@Service
public class AvidCallBizImpl implements AvidCallBiz {
    @Autowired
    private MonkeyJdbcRealm monkeyJdbcRealm;//获得当前登录用户信息
    @Autowired
    private AvidCallServiceExt avidCallServiceExt;

    @Override
    public Result<AvidCallDTO> getAvidCallById(Integer id) {
        if(id==null || id<1){
            return Result.wrapErrorResult("", "参数错误，id："+id);
        }
        AvidCallDTO avidCallDTO = avidCallServiceExt.getAvidCallById(id);
        if(avidCallDTO==null){
            return Result.wrapErrorResult("", "未查到符合条件的数据，id："+id);
        }

        return Result.wrapSuccessfulResult(avidCallDTO);
    }

    @Override
    public Result<Integer> turnWish(WishListRequestBO requestBO) {
        UserDO user = (UserDO) monkeyJdbcRealm.getSessionValue("currentUser");
        if (user == null) {
            return Result.wrapErrorResult("", "请先登录");
        }
        if(requestBO==null){
            return Result.wrapErrorResult("", "参数错误");
        }
        if(requestBO.getAvidCallId()==null || requestBO.getAvidCallId()<1){
            return Result.wrapErrorResult("", "参数错误");
        }
        if(CollectionUtils.isEmpty(requestBO.getGoodsList())){
            return Result.wrapErrorResult("", "至少一个商品");
        }

        requestBO.setOperator("monkey_"+user.getUserName());
        return avidCallServiceExt.turnWish(requestBO);
    }

    @Override
    public Result<Page<AvidListShowBO>> getAvidPageShow(AvidSearchBO searchBO) {
        if(searchBO==null){
            return Result.wrapErrorResult("", "参数错误");
        }
        org.springframework.data.domain.Page<EpcAvidCallDTO> page = avidCallServiceExt.getAvidPage(searchBO);
        if(page == null){
            return Result.wrapErrorResult("", "暂无数据");
        }
        List<EpcAvidCallDTO> callDTOList = page.getContent();
        if(CollectionUtils.isEmpty(callDTOList)){
            return Result.wrapErrorResult("", "暂无数据");
        }
        Page<AvidListShowBO> showBOPage = new Page<>();
        showBOPage.setCurrentIndex(page.getNumber()+1);
        showBOPage.setItems(BdUtil.do2bo4List(callDTOList, AvidListShowBO.class));
        showBOPage.setPageSize(page.getSize());
        showBOPage.setTotalNumber((int) page.getTotalElements());

        return Result.wrapSuccessfulResult(showBOPage);
    }

    @Override
    public Result cancelAvidCall(Integer id, String reason) {
        UserDO user = (UserDO) monkeyJdbcRealm.getSessionValue("currentUser");
        if (user == null) {
            return Result.wrapErrorResult("", "请先登录");
        }
        if(id==null || id<1 || StringUtils.isEmpty(reason)){
            return Result.wrapErrorResult("", "参数错误");
        }
        CancelAvidCallParam param = new CancelAvidCallParam();
        param.setId(id);
        param.setCancelReason(reason);
        param.setOperator("monkey_"+user.getUserName());

        return avidCallServiceExt.cancelAvidCall(param);
    }

    @Override
    public Result modifyAvidCall(ModifyAvidCallBO modifyAvidCallBO) {
        UserDO user = (UserDO) monkeyJdbcRealm.getSessionValue("currentUser");
        if (user == null) {
            return Result.wrapErrorResult("", "请先登录");
        }
        if(modifyAvidCallBO==null){
            return Result.wrapErrorResult("", "参数错误");
        }
        if(modifyAvidCallBO.getId()==null || modifyAvidCallBO.getId()<1){
            return Result.wrapErrorResult("", "参数错误");
        }

        modifyAvidCallBO.setOperator("monkey_"+user.getUserName());

        return avidCallServiceExt.modifyAvidCall(modifyAvidCallBO);
    }

    @Override
    public Result<Long> getAllNewDataNum() {
        Result<Long> result = avidCallServiceExt.getAllNewDataNum();
        if(result.isSuccess()){
            Long num = result.getData();
            if(num != null && num > 0){
                return Result.wrapSuccessfulResult(num);
            }
        }
        return Result.wrapErrorResult("", "");
    }

    @Override
    public Result<Long> getFiveMinNewDataNum() {
        Result<Long> result = avidCallServiceExt.getFiveMinNewDataNum();
        if(result.isSuccess()){
            Long num = result.getData();
            if(num != null && num > 0){
                return Result.wrapSuccessfulResult(num);
            }
        }
        return Result.wrapErrorResult("", "");
    }

}
