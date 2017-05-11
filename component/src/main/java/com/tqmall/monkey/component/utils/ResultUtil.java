package com.tqmall.monkey.component.utils;

import com.tqmall.core.common.entity.Result;

/**
 * 处理 后台返回前台的Result 的工具(扩展了原来的Result)
 * Created by lyj on 16/1/29.
 */
public class ResultUtil<D> extends Result<D> {
    public static <D> Result<D> wrapSuccessfulResult(D data, String message) {
        Result<D> result = new Result<>();
        result.setData(data);
        result.setCode("0");
        result.setMessage(message);
        result.setSuccess(true);
        return result;
    }

    public static <D> Result<D> errorResult(D data, String message){
        Result<D> result = new Result<>();
        result.setSuccess(false);
        result.setData(data);
        result.setMessage(message);
        return result;
    }
}
