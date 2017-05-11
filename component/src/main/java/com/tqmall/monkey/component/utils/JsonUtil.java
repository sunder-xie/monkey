package com.tqmall.monkey.component.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Created by huangzhangting on 15/8/27.
 */
@Slf4j
public class JsonUtil {
    private static final ObjectMapper OM = new ObjectMapper();

    /*
    *  对象转换成json字符串
    * */
    public static String objectToJson(Object object) {
        try {
            return OM.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("objectToJson error, object:"+object, e);
        }
        return null;
    }

    /*
    *  json字符串转换成对象
    * */
    public static <T> T jsonToObject(String content, Class<T> valueType) {
        try {
            return OM.readValue(content, valueType);
        } catch (IOException e) {
            log.error("jsonToObject error, content:"+content, e);
        }
        return null;
    }

    /*
    *  json字符串，转换成集合
    *  content：本身必须是json数组
    *  collectionClass：集合class 例如 List.class
    *  elementClass：元素class 例如 User.class
    * */
    public static <T> T jsonToCollection(String content, Class<? extends Collection> collectionClass, Class<?> elementClass) {
        JavaType javaType = getCollectionType(collectionClass, elementClass);
        try {
            return OM.readValue(content, javaType);
        } catch (IOException e) {
            log.error("jsonToCollection error, content:"+content, e);
        }
        return null;
    }

    /**
     *
     * @param content
     * @param elementClass
     * @param <T>
     * @return
     */
    public static <T> T jsonToList(String content, Class<?> elementClass) {
        return jsonToCollection(content, List.class, elementClass);
    }

    /*
    *  JsonNode：就是json数组对象
    *  collectionClass：集合class 例如 List.class
    *  elementClass：元素class 例如 User.class
    * */
    public static <T> T jsonNodeToCollection(JsonNode jsonNode, Class<? extends Collection> collectionClass, Class<?> elementClass) {
        JavaType javaType = getCollectionType(collectionClass, elementClass);
        try {
            return OM.readValue(jsonNode.toString(), javaType);
        } catch (IOException e) {
            log.error("jsonNodeToCollection error, jsonNode:" + jsonNode, e);
        }
        return null;
    }

    /*
    *  将json字符串转换成json对象
    *
    *  JsonNode：就是json对象，包括数组、或单个对象
    *  content：json字符串
    *
    *  可以通过 jsonNode.findValue("pro") 查找属性值，返回值还是 JsonNode
    * */
    public static JsonNode getJsonNode(String content) {
        try {
            return OM.readTree(content);
        } catch (IOException e) {
            log.error("getJsonNode error, content:"+content, e);
        }
        return null;
    }

    /*
    *  获得java集合类型，json在转化成java集合时需要用到
    *  collectionClass：集合class 例如 List.class
    *  elementClass：元素class 例如 User.class
    * */
    public static JavaType getCollectionType(Class<? extends Collection> collectionClass, Class<?> elementClass) {
        return OM.getTypeFactory().constructCollectionType(collectionClass, elementClass);
    }

}
