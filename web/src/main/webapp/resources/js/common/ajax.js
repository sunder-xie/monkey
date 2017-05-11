/**
 * Created by huangzhangting on 16/6/28.
 */

//jquery ajax请求
var JqAjax = {
    sendAjaxRequest: function(type, url, successFun, data, errorCallback, contentType, async){
        var param = {
            url: url,
            type: type,
            success: successFun,
            errorCallback: errorCallback
        };
        if(data!==void 0){
            param['data'] = data;
        }
        if(contentType!==void 0){
            param['contentType'] = contentType;
        }
        if(async===false){
            param['async'] = false;
        }

        var reqParam = AjaxParamProcessor.process(param);

        $.ajax(reqParam);
    },
    get: function(url, successFun, data, errorCallback, async){
        this.sendAjaxRequest('GET', url, successFun, data, errorCallback, undefined, async);
    },
    post: function(url, successFun, data, errorCallback, contentType){
        this.sendAjaxRequest('POST', url, successFun, data, errorCallback, contentType);
    },
    postJson: function(url, successFun, data, errorCallback){
        this.post(url, successFun, data, errorCallback, 'application/json');
    }
};

//使用对象传参（推荐使用）
var Ajax = function(){
    var sendAjaxRequest = function(param){
        var reqParam = AjaxParamProcessor.process(param);

        $.ajax(reqParam);
    };
    return {
        get: function(param){
            sendAjaxRequest(param);
        },
        post: function(param){
            param.type = 'POST';
            this.get(param);
        },
        postJson: function(param){
            param.contentType = 'application/json';
            this.post(param);
        }
    };
}();


/** 请求参数处理器 */
var AjaxParamProcessor = {
    process: function(param){
        var reqParam = $.extend({
            url: '',
            type: 'GET',
            dataType: 'json',
            data: null,
            async: true,
            cache: true,
            contentType: 'application/x-www-form-urlencoded',
            timeout: 180000,
            success: function(){}
        }, param);


        /** 错误回调方法，封装对象 */
        var errorCallback = param.errorCallback;

        reqParam.error = function(XMLHttpRequest, textStatus, errorThrown){
            if(XMLHttpRequest.status==200 && 'parsererror'==textStatus){
                var hasTimeoutFun = false;
                if(errorCallback!==undefined && errorCallback!==null){
                    if($.isFunction(errorCallback.timeout)){
                        /** 登录超时，回调方法 */
                        errorCallback.timeout();
                        hasTimeoutFun = true;
                    }
                }
                if(!hasTimeoutFun){
                    alert('非常抱歉，您登录超时了，请刷新页面重新登录');
                    location.reload();
                }

            }else if(XMLHttpRequest.readyState==0 && XMLHttpRequest.statusText=='error'){

            }else{// if(XMLHttpRequest.readyState==4)
                alert('非常抱歉，系统发生内部错误');
            }
            if(errorCallback!==undefined && errorCallback!==null){
                if($.isFunction(errorCallback.unblockUI)){
                    /** 解除页面加载，回调方法 */
                    errorCallback.unblockUI();
                }
            }

            /* 简单粗暴的解除页面加载，能解决大部分 */
            App.unblockUI($('body'));
        };

        return reqParam;
    }
};
