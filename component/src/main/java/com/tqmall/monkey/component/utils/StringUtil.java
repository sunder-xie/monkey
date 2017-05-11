package com.tqmall.monkey.component.utils;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * Created by lyj on 2015/11/26.
 */
public class StringUtil {
    /**
     * vin码验证
     * @param vin
     * @return
     */
    public static boolean isVin(String vin){
        if(vin==null){
            return false;
        }
        vin = vin.replace(" ", "");
        if(vin.length()!=17){
            return false;
        }

        Pattern pattern = Pattern.compile("^[A-Z0-9]+$");
        Matcher matcher = pattern.matcher(vin.toUpperCase());

        return matcher.matches();
    }

    /**
     * 是纯数字返回true，不是纯数字返回false
     * @param str
     * @return
     */
    public static boolean isNum(String str) {
        if (str == null || "".equals(str)) {
            return false;
        }
        boolean result = true;
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     *
     * @param str 是以英文半角逗号分割的数字
     * @return 逗号分割的数字转换成Integer的List <br/>
     * 转换例子：<br/>
     * "1,2,3" -> [1,2,3] <br/>
     * "1,02, 3," -> [1,2,3] <br/>
     * "203,, , 03, 207, " -> [203,3,207] <br/>
     */
    public static List<Integer> stringtoIntList(String str) {
        Iterable<String> it = Splitter.on(',').trimResults().omitEmptyStrings().split(str);
        Iterable<Integer> target = Iterables.transform(it, new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return Integer.valueOf(input);
            }

        });
        return Lists.newArrayList(target.iterator());
    }

    /**
     * @param list    list
     * @param spliter 分隔符
     * @return String   如果参数不对，或者字符串为null或者length = 0, 都返回NULL
     */
    public static <T> String list2String(Collection<T> list, String spliter) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (T item : list) {
            if (item == null) continue;
            sb.append(item.toString()).append(spliter);
        }
        if (StringUtils.isEmpty(sb.toString())) {
            return null;
        }
        return sb.substring(0, sb.length() - spliter.length());
    }

    public static <K, V> String map2String(Map<K, V> map, String openSign, String closeSign, String kvSign, String spliter) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(openSign);
//        Set<K> keySet = map.keySet();
//        for (K key : keySet) {
//            sb.append(key.toString()).append(kvSign).append(map.get(key).toString()).append(spliter);
//        }
        for(Map.Entry<K,V> entry : map.entrySet()){
            K key = entry.getKey();
            sb.append(key.toString()).append(kvSign).append(map.get(key).toString()).append(spliter);
        }
        if (StringUtils.isEmpty(sb.toString())) {
            return null;
        }
        return sb.subSequence(0, sb.length() - spliter.length()) + closeSign;
    }

    public static <K, V> String map2String(Map<K, V> map) {
        return map2String(map, "{", "}", ":", ",");
    }


    /**
     * 默认","为拼接符
     *
     * @param list list
     * @param <T>  T
     * @return String 如果参数不对，或者字符串为null或者length = 0, 都返回NULL
     */
    public static <T> String list2String(Collection<T> list) {
        return list2String(list, ",");
    }

    public static List<String> string2List(String str) {
        List<String> list = Lists.newArrayList();

        try {
            if (!StringUtils.isEmpty(str.trim())) {
                StringTokenizer toKenizer = new StringTokenizer(str, ",");
                while (toKenizer.hasMoreElements()) {
                    list.add(toKenizer.nextToken());
                }
            }
        } catch (Exception e) {
            return Lists.newArrayList();
        }
        return list;
    }

    public static List<BigDecimal> string2BigDecimalList(String str) {
        List<BigDecimal> list = Lists.newArrayList();

        try {
            if (!StringUtils.isEmpty(str.trim())) {
                StringTokenizer toKenizer = new StringTokenizer(str, ",");
                while (toKenizer.hasMoreElements()) {
                    list.add(new BigDecimal(toKenizer.nextToken()));
                }
            }
        } catch (Exception e) {
            return Lists.newArrayList();
        }
        return list;
    }
}
