package com.tqmall.monkey.component.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Map;

/**
 * do 和 bo 的互相转化类
 */
public class BdUtil {

    public static <DO, BO> BO do2bo(DO request, Class<BO> cls) {
        if (null == request) return null;
        BO result;
        try {
            result = cls.newInstance();
            BeanUtils.copyProperties(request, result);
        } catch (Exception e) {
            throw new IllegalArgumentException("对象copy失败，请检查相关module", e);
        }
        return result;
    }

    public static <DO, BO> List<BO> do2bo4List(List<DO> request, Class<BO> cls) {
        List<BO> result = Lists.newArrayList();
        for (DO obj : request) {
            result.add(do2bo(obj, cls));
        }
        return result;
    }

    public static <DO, BO, K> Map<K, DO> bo2do4Map(Map<K, BO> request, Class<DO> cls) {
        Map<K, DO> result = Maps.newHashMap();
        for (Map.Entry<K, BO> item : request.entrySet()) {
            K key = item.getKey();
            BO val = item.getValue();
            result.put(key, do2bo(val, cls));
        }
        return result;
    }

}
