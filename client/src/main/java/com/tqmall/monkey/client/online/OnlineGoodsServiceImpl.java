package com.tqmall.monkey.client.online;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.tqmall.monkey.dal.dao.online.OnlineGoodsDao;
import com.tqmall.monkey.domain.entity.online.OnlineGoodsDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zxg on 15/9/11.
 */
@Service
public class OnlineGoodsServiceImpl implements OnlineGoodsService{

    @Autowired
    private OnlineGoodsDao onlineGoodsDao;

    @Override
    public Multimap<String, Integer> getGoodsListOfFormatBrand() {
        List<HashMap<String, Object>> list = onlineGoodsDao.getGoodsListOfFormatBrand();

        Multimap<String, Integer> brandFormatMap = ArrayListMultimap.create();
        for(HashMap<String, Object> resultMap : list){
            String format = ((String)resultMap.get("format")).trim().replaceAll(" +","");
            String brandName = ((String)resultMap.get("brandName")).trim().replaceAll(" +", "");
            Integer id = (Integer)resultMap.get("goodsId");

            String key = brandName+"_"+format;

            brandFormatMap.put(key,id);
        }

        return brandFormatMap;
    }
}
