package com.tqmall.monkey.component.helper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tqmall.monkey.component.utils.HttpClientUtil;
import com.tqmall.monkey.component.redis.RedisClientTemplate;
import com.tqmall.monkey.component.redis.RedisKeyBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by ximeng on 2015/5/7.
 */
@Service
@Slf4j
public class SearchServiceHelpImpl implements SearchServiceHelp {

    @Value(value = "${goods.search.url}")
    protected String searchHost;

    @Override
    public Integer getGoodsIdByOe(String OE) {
        String url = searchHost + "goods/sample_car_parts?oeNum=" + OE;
        try {
            String json = HttpClientUtil.getUrl(url).getResult();

            JSONObject object = JSON.parseObject(json);
            JSONObject response = object.getJSONObject("response");
            JSONArray list = response.getJSONArray("list");

            JSONObject result = list.getJSONObject(0);
            Integer goodsId = result.getInteger("id");
            return goodsId;
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public Integer getGoodsIdByFormatBrand(String format, String brandName) {
        String oldBrandName = brandName;
        try {
            //删去空格
            format = format.replaceAll(" +","");
            format = URLEncoder.encode(format, "UTF-8");
            brandName = URLEncoder.encode(brandName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = searchHost + "goods/convert?q=*&brandName=" + brandName + "&goodsFormat=" + format;
        Integer goodsId = 0;

        try {
            String json = HttpClientUtil.getUrl(url).getResult();

            JSONObject object = JSON.parseObject(json);
            JSONObject response = object.getJSONObject("response");
            JSONArray list = response.getJSONArray("list");

            if (list.size() > 0) {
                JSONObject result = list.getJSONObject(0);
                goodsId = result.getInteger("id");
//
//                String jsonBrandName = result.getString("brandName");
//                String jsonFormat = result.getString("goodsFormat");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }

        return goodsId;
    }

}
