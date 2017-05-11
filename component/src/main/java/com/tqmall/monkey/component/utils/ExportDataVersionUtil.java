package com.tqmall.monkey.component.utils;

import com.tqmall.monkey.component.redis.RedisClientTemplate;

/**
 * 需要版本号的导出业务数据 的版本号的 处理工具
 * Created by lyj on 16/1/29.
 */
public class ExportDataVersionUtil {
    /**
     * 需要传入初始版本号
     *
     * @param redisClientTemplate
     * @param key
     * @param initialVersion
     * @return
     */
    public static String modifyExportVersion(RedisClientTemplate redisClientTemplate, String key, int initialVersion) {
        String value = redisClientTemplate.get(key);
        if (value == null) {
            value = String.valueOf(initialVersion - 1);
        }
        Integer temp = Integer.parseInt(value);
        String version = String.valueOf(++temp);
        redisClientTemplate.set(key, version);
        return version;
    }

    /**
     * 需要传入初始版本号
     *
     * @param redisClientTemplate
     * @param key
     * @param initialVersion
     * @return
     */
    public static String getExportVersion(RedisClientTemplate redisClientTemplate, String key, int initialVersion) {
        String version = redisClientTemplate.get(key);
        if (version == null) {
            version = String.valueOf(initialVersion);
            redisClientTemplate.set(key, version);
        }
        return version;
    }

    /**
     * 需要输入为空时的返回值
     *
     * @param redisClientTemplate
     * @param key
     * @param nullReturn
     * @return
     */
    public static String deleteExportVersion(RedisClientTemplate redisClientTemplate, String key, int nullReturn) {
        String version = redisClientTemplate.get(key);
        if (version == null) {
            version = String.valueOf(nullReturn);
        } else {
            redisClientTemplate.delKey(key);
        }
        return version;
    }

    /**
     * 回滚版本号, 在生成文件失败的时候
     * @param redisClientTemplate
     * @param key
     * @param initialVersion
     */
    public static void rollbackExportVersion(RedisClientTemplate redisClientTemplate, String key, int initialVersion) {
        String value = redisClientTemplate.get(key);
        if (value == null) {
            value = String.valueOf(initialVersion + 1);
        }
        Integer temp = Integer.parseInt(value);
        String version = String.valueOf(--temp);
        redisClientTemplate.set(key, version);
    }
}
