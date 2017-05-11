package com.tqmall.monkey.component.utils;


import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**sha1sum加密算法
 * Created by ruibai on 15/3/27.
 */
public class Sha1Util {
    public static String getSha1(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }

        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };

        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes());

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * ERP团队加密方法
     * @param appId 约定好的appId
     * @param key 此appId对应的key
     * @param map   传入的其他参数和对应的值
     * @return
     */
    public static String ERPSha(String appId,String key,Map<String,Object> map){
        Set<String> strings = map.keySet();

        String[] keyArray = strings.toArray(new String[strings.size()]);

        Arrays.sort(keyArray);
        //拼接参数
        StringBuilder stringBuilder = new StringBuilder();
        for(String key1:keyArray){
            stringBuilder.append(key1).append(map.get(key1));
        }
        String info = stringBuilder.toString();

        return DigestUtils.sha1Hex(info + appId + key).toUpperCase();
    }
}
