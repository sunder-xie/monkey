package com.tqmall.monkey.dal.dao.offerGoods;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.dal.dao.base.BaseDao;
import com.tqmall.monkey.dal.mapper.offerGoods.OfferWrongDataDOMapper;
import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.offerGoods.OfferWrongDataDO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

/**
 * 错误的供应商数据的存储
 * Created by zxg on 15/7/8.
 */
@MyBatisRepository
public class OfferWrongDataDao extends BaseDao {
    private final static String NAMESPACE = "com.tqmall.monkey.dal.mapper.offerGoods.OfferWrongDataDOMapper";
    @Autowired
    private OfferWrongDataDOMapper offerWrongDataDOMapper;

    public OfferWrongDataDOMapper getOfferWrongDataDOMapper() {
        return offerWrongDataDOMapper;
    }

    public Page<OfferWrongDataDO> pageByStatusAccount(Integer status, String account, Integer pageIndex, Integer pageSize) {
        String selectSql = NAMESPACE + ".selectByStatusAndAccount";
        String countSql = NAMESPACE + ".selectByStatusAndAccountCount";

        HashMap<String, Object> params = new HashMap<>();
        params.put("status", status);
        params.put("createAccount", account);

        return this.queryPage(selectSql, countSql, params, pageIndex, pageSize);
    }

    public List<OfferWrongDataDO> selectListByStatusAccount(Integer status, String account) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("status", status);
        params.put("createAccount", account);

        return sqlTemplate.selectList(NAMESPACE + ".selectByStatusAndAccount", params);
    }

    public Integer selectCountByStatusAccount(Integer status, String account) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("status", status);
        params.put("createAccount", account);

        return sqlTemplate.selectOne(NAMESPACE + ".selectByStatusAndAccountCount", params);
    }
}
